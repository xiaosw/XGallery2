package com.xiaosw.gallery.controll;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.Map;

/**
 * @ClassName : {@link BaseMediaControll}
 * @Description: 多媒体相关操作
 * 
 * @Date 2016-09-24 13:14
 * @Author xiaosw <xiaosw0802@163.com>
 */
public abstract class BaseMediaControll {
    
    private static final String TAG = "BaseMediaControll";

    MediaPlayer mMediaPlayer;
    Callback mCallback;
    Context mContext;
    // settable by the client
    private Uri mUri;
    private Map<String, String> mHeaders;
    private int         mSeekWhenPrepared;  // recording the seek position while preparin

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

    private void openVideo() {

    }

    boolean isNull(Object object) {
        return null == object;
    }

    public void onStart() {
        if (null != mMediaPlayer
                && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            if (null != mCallback) {
                mCallback.onStart();
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
            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    public void resume() {
        openVideo();
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

    public void onPause() {
        if (!isNull(mMediaPlayer)
                && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (null != mCallback) {
                mCallback.onPause();
            }
        }
    }

    public void onStop() {
        if (!isNull(mMediaPlayer)) {
            mMediaPlayer.stop();
            if (null != mCallback) {
                mCallback.onStop();
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public boolean isPlaying() {
        if (!isNull(mMediaPlayer)) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }
    
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        
        public void onStart();

        public void onPause();

        public void onStop();
    }

}
