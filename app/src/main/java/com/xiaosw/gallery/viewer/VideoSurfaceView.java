package com.xiaosw.gallery.viewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * @ClassName : {@link VideoSurfaceView}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-26 16:16:53
 */
public class VideoSurfaceView extends SurfaceView {

    private GestureDetector mGestureDetector;
    private VideoSurfaceViewGestureListener mVideoSurfaceViewGestureListener;

    public VideoSurfaceView(Context context) {
        super(context);
        initialize(context);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(21)
    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        mGestureDetector = new GestureDetector(context, new VideoSimpleOnGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != mVideoSurfaceViewGestureListener) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL) {
                mVideoSurfaceViewGestureListener.onUp();
            }
        }

        return mGestureDetector.onTouchEvent(event)
            || super.onTouchEvent(event);
    }

    private class VideoSimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            if (null != mVideoSurfaceViewGestureListener) {
                return mVideoSurfaceViewGestureListener.onDown();
            }
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (null != mVideoSurfaceViewGestureListener
                && mVideoSurfaceViewGestureListener.onSurfaceViewClick(e)) {
                return true;
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (null != mVideoSurfaceViewGestureListener
                && mVideoSurfaceViewGestureListener.onSurfaceViewScroll(e1, e2, distanceX, distanceY)) {
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public void setVideoSurfaceViewGestureListener(VideoSurfaceViewGestureListener listener) {
        this.mVideoSurfaceViewGestureListener = listener;
    }

    public interface VideoSurfaceViewGestureListener {

        public boolean onDown();

        public boolean onSurfaceViewClick(MotionEvent e);

        public boolean onSurfaceViewScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

        public void onUp();
    }

}
