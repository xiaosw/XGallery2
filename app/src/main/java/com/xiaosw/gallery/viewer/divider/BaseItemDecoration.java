package com.xiaosw.gallery.viewer.divider;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

/**
 * @ClassName : {@link BaseItemDecoration}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-22 19:19:25
 */
public abstract class BaseItemDecoration extends RecyclerView.ItemDecoration {

    Drawable mDrawable;

    public abstract int getItemDecorationWidth();

    public abstract int getItemDecorationHeight();

}
