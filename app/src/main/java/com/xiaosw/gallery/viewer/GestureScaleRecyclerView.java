package com.xiaosw.gallery.viewer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.xiaosw.gallery.util.LogUtil;

/**
 * @ClassName {@link GestureScaleRecyclerView}
 * @Description
 * @Date 2016-09-10 10:09.
 * @Author xiaoshiwang.
 */
public class GestureScaleRecyclerView extends RecyclerView {
    private ScaleGestureDetector mScaleGestureDetector;
    private static final float MIN_SCALE = 0.4f;
    private static final float MAX_SCALE = 1.2f;


    private int mInitialHeight = -1;
    private int mMinHeight;
    public GestureScaleRecyclerView(Context context) {
        this(context, null);
    }

    public GestureScaleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureScaleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleGestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    private void initView() {
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new SimpleOnScaleGesture());
    }

    private class SimpleOnScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (mInitialHeight < 1) {
                mInitialHeight = getMeasuredHeight();
                mMinHeight = (int) (mInitialHeight * MIN_SCALE);
            }
            int height = (int) (getHeight() * detector.getScaleFactor());
            getLayoutParams().height = Math.max(mMinHeight, height);
            requestLayout();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }
}
