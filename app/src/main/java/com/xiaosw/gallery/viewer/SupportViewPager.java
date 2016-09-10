package com.xiaosw.gallery.viewer;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @ClassName {@link SupportViewPager}
 * @Description
 * @Date 2016-09-10 15:43.
 * @Author xiaoshiwang.
 */
public class SupportViewPager extends ViewPager {

    private GestureDetector mGestureDetector;
    private OnItemClickListener mOnItemClickListener;

    public SupportViewPager(Context context) {
        super(context);
        initViewPager(context);
    }

    public SupportViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager(context);
    }

    private void initViewPager(Context context) {
        mGestureDetector = new GestureDetector(context, new EventGestureDetector());

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    private class EventGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (null != mOnItemClickListener) {
                return mOnItemClickListener.onItemClick(getCurrentItem());
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public boolean onItemClick(int position);
    }

}
