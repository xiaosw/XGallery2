package com.xiaosw.gallery.viewer.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.xiaosw.gallery.R;

/**
 * Created by xiaos on 2016/5/18 11:11:48.
 * mail: xiaosw@putao.com
 */
public class DividerVerticalDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public DividerVerticalDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.item_grid_recycler_view_divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
    }

    /**
     * 绘制垂直分割线
     * @param c
     * @param parent
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    }
}
