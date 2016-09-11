package com.xiaosw.gallery.viewer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @ClassName {@link PhotoPageRecyclerView}
 * @Description
 * @Date 2016-09-10 15:25.
 * @Author xiaoshiwang.
 */
public class PhotoPageRecyclerView extends RecyclerView {
    private boolean isTouch;

    public PhotoPageRecyclerView(Context context) {
        this(context, null);
    }

    public PhotoPageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoPageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        isTouch = true;
        return super.dispatchTouchEvent(ev);
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }
}
