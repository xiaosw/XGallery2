package com.xiaosw.gallery.controll;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;

import com.xiaosw.gallery.GalleryApplication;
import com.xiaosw.gallery.util.LogUtil;

import java.util.Formatter;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName : {@link BaseMediaControll}
 * @Description: 多媒体相关操作
 * 
 * @Date 2016-09-24 13:14
 * @Author xiaosw <xiaosw0802@163.com>
 */
public abstract class BaseMediaControll implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnCompletionListener{
    
    private static final String TAG = "BaseMediaControll";

    /** 默认更新进度间隔时间 */
    private static final int DEFAULT_UPDATE_DURATION_GAP = 1000;

    MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private OnProgressInfoUpdateListener mOnProgressInfoUpdateListener;
    Context mContext;
    // settable by the client
    private Uri mUri;
    private Map<String, String> mHeaders;
    private int         mSeekWhenPrepared;  // recording the seek position while preparin
    // 格式化显示时间
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    SurfaceView mSurfaceView;
    private Handler mHandler;
    private int mProgressUpdateGap = DEFAULT_UPDATE_DURATION_GAP;
    private boolean resumeNeededPlay = true;

    public BaseMediaControll(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mHandler = GalleryApplication.mApp.getHandler();
        initMediaPlayer();
    }

    protected void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    /**
     * Sets video/audio path.
     *
     * @param path the path of the video.
     */
    public void setMediaPath(String path) {
        setMediaURI(Uri.parse(path));
    }

    /**
     * Sets video/audio URI.
     *
     * @param uri the URI of the video.
     */
    public void setMediaURI(Uri uri) {
        setMediaURI(uri, null);

    }

    /**
     * Sets video/audio URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     *                Note that the cross domain redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross domain redirection.
     */
    public void setMediaURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        openVideo();
    }

    private boolean openVideo() {
        try {
            if (mUri == null) {
                // not ready for playback just yet, will try again later
                return false;
            }
            requestAudioFocus(mContext);
            mMediaPlayer.reset();//重置为初始状态
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音乐流的类型
            if (null != mSurfaceView) {
                mMediaPlayer.setDisplay(mSurfaceView.getHolder());//设置video影片以surfaceviewholder播放
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mContext, mUri, mHeaders);
            } else {
                mMediaPlayer.setDataSource(mContext, mUri);
            }
            mMediaPlayer.prepare();//缓冲
            mHandler.removeCallbacks(mUpdateProgressTask);
            mHandler.postDelayed(mUpdateProgressTask, mProgressUpdateGap);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        return false;
    }

    boolean isNull(Object object) {
        return null == object;
    }

    public void onStart() {
        if (null == mMediaPlayer) {
            return;
        }
        if (!mMediaPlayer.isPlaying()) {
            requestAudioFocus(mContext);
            seekTo(mSeekWhenPrepared);
            mMediaPlayer.start();
            mHandler.removeCallbacks(mUpdateProgressTask);
            mHandler.postDelayed(mUpdateProgressTask, mProgressUpdateGap);
        }
    }

    public void onResume() {
        onStart();
    }

    public void onPause() {
        if (isNull(mMediaPlayer)) {
            return;
        }
        abandonAudioFocus(mContext);
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        mSeekWhenPrepared = mMediaPlayer.getCurrentPosition();
        mHandler.removeCallbacks(mUpdateProgressTask);
    }

    public void onStop() {
        if (isNull(mMediaPlayer)) {
            return;
        }
        abandonAudioFocus(mContext);
        mMediaPlayer.stop();
        release();
    }

    public void toggle() {
        if (!isNull(mMediaPlayer)) {
            if (isPlaying()) {
                abandonAudioFocus(mContext);
                mMediaPlayer.pause();
                mHandler.removeCallbacks(mUpdateProgressTask);
            } else {
                requestAudioFocus(mContext);
                mMediaPlayer.start();
                mHandler.removeCallbacks(mUpdateProgressTask);
                mHandler.postDelayed(mUpdateProgressTask, mProgressUpdateGap);
            }
        }
    }

    /*
     * release the media player in any state
     */
    private void release() {
        if (!isNull(mMediaPlayer)) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            abandonAudioFocus(mContext);
        }
        mSeekWhenPrepared = 0;
        mHandler.removeCallbacks(mUpdateProgressTask);
    }

    public int getDuration() {
        if (!isNull(mMediaPlayer)) {
            return mMediaPlayer.getDuration();
        }

        return -1;
    }

    public int getCurrentPosition() {
        if (!isNull(mMediaPlayer)) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (!isNull(mMediaPlayer)) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static boolean requestAudioFocus(Context context) {
        return handleAudioFocus(context, false);
    }

    public static boolean abandonAudioFocus(Context context) {
        return handleAudioFocus(context, true);
    }

    /**@param isAbandon 值为true时为关闭背景音乐。*/
    private static boolean handleAudioFocus(Context context, boolean isAbandon) {
        if(context == null){
            LogUtil.w("context is null.");
            return false;
        }
        if(Build.VERSION_CODES.FROYO > Build.VERSION.SDK_INT){
            // 2.1以下的版本不支持下面的API：requestAudioFocus和abandonAudioFocus
            LogUtil.e("Android 2.1 and below can not stop music");
            return false;
        }
        boolean bool;
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(isAbandon){
            int result = am.requestAudioFocus(null,AudioManager.STREAM_MUSIC ,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }else{
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return bool;
    }

    public boolean isPlaying() {
        if (!isNull(mMediaPlayer)) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        onStart();
        if (!resumeNeededPlay) {
            onPause();
        }
        if (null != mOnProgressInfoUpdateListener) {
            mOnProgressInfoUpdateListener.onPrepared(mp);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        abandonAudioFocus(mContext);
        if (null != mOnCompletionListener) {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
       this.mOnCompletionListener = listener;
    }

    public void setOnProgressInfoUpdateListener(OnProgressInfoUpdateListener listener) {
        this.mOnProgressInfoUpdateListener = listener;
    }

    public void setProgressUpdateGap(int durationGap){
        if (durationGap >= 1) {
            this.mProgressUpdateGap = durationGap;
        }
    }

    public void setResumeNeededPlay(boolean resumeNeededPlay) {
        this.resumeNeededPlay = resumeNeededPlay;
    }

    private Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            if (null != mOnProgressInfoUpdateListener) {
                int totalTime = mMediaPlayer.getDuration();
                int curentTime = mMediaPlayer.getCurrentPosition();
                mOnProgressInfoUpdateListener.onProgressUpdate(totalTime, curentTime);
                if (totalTime > curentTime) {
                    mHandler.postDelayed(this, mProgressUpdateGap);
                }
            }
        }
    };

    public interface OnProgressInfoUpdateListener {

        public void onPrepared(MediaPlayer mp);

        public void onProgressUpdate(int total, int current);

    }

}
