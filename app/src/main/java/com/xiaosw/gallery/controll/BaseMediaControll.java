package com.xiaosw.gallery.controll;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceView;

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

    MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    Context mContext;
    // settable by the client
    private Uri mUri;
    private Map<String, String> mHeaders;
    private int         mSeekWhenPrepared;  // recording the seek position while preparin
    // 格式化显示时间
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    SurfaceView mSurfaceView;

    public BaseMediaControll(Context context) {
        this.mContext = context;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        initMediaPlayer();
    }

    protected void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     *                Note that the cross domain redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross domain redirection.
     */
    public void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;
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
            mMediaPlayer.start();//播放
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
        if (null != mMediaPlayer
                && !mMediaPlayer.isPlaying()) {
            requestAudioFocus(mContext);
            mMediaPlayer.start();
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
    }

    public void onResume() {
        requestAudioFocus(mContext);
        openVideo();
    }

    public void toggle() {
        if (!isNull(mMediaPlayer)) {
            if (isPlaying()) {
                abandonAudioFocus(mContext);
                mMediaPlayer.pause();
            } else {
                requestAudioFocus(mContext);
                mMediaPlayer.start();
            }
        }
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

    public void onPause() {
        abandonAudioFocus(mContext);
        if (!isNull(mMediaPlayer)
                && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void onStop() {
        abandonAudioFocus(mContext);
        if (!isNull(mMediaPlayer)) {
            mMediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        if (!isNull(mMediaPlayer)) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

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

}
