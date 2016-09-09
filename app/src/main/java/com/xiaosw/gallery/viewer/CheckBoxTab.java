package com.xiaosw.gallery.viewer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.xiaosw.gallery.R;

/**
 * @ClassName {@link CheckBoxTab}
 * @Description 实现上面图片，下面文字
 * @Date 2016-05-14 04:51.
 * @Author xiaoshiwang.
 */
public class CheckBoxTab extends CheckBox {
    /** TopDrawable宽度 */
    private int mDrawableWidth;
    /** TopDrawable高度 */
    private int mDrawableHeight;
    private Drawable mTopDrawable;
    // 标记是否已计算图片尺寸信息
    private boolean isCompute;
    private Rect mRect = new Rect();
    public CheckBoxTab(Context context) {
        super(context);
    }

    public CheckBoxTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Drawable);
        mDrawableWidth = ta.getInt(R.styleable.Drawable_drawable_width, -1);
        mDrawableHeight = ta.getInt(R.styleable.Drawable_drawable_height, -1);
        ta.recycle();
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isCompute = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        mTopDrawable = getCompoundDrawables()[1];
        if (null != mTopDrawable
                && -1 != mDrawableWidth
                && -1 != mDrawableHeight
                && widthSize > 0) {
            int left = (widthSize - mDrawableWidth) / 2 + getPaddingLeft();
            int top =  getPaddingTop();
            int right = left + mDrawableWidth;
            int bottom = top + mDrawableHeight;
            mTopDrawable.setBounds(new Rect(left, top, right, bottom));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        Paint paint = getPaint();
        paint.getTextBounds(text, 0, text.length(), mRect);
        if (null != mTopDrawable) {
            mTopDrawable.draw(canvas);
        }
        ColorStateList colors = getTextColors();
        paint.setColor(colors.getColorForState(getDrawableState(), 0));
        canvas.drawText(getText().toString(), (getWidth() - mRect.width()) / 2.0f, getHeight() - mRect.bottom - getPaddingBottom(), paint);
    }

}
