package com.xiaosw.gallery.widget.listener;

import android.view.View;

/**
 * @ClassName : {@link OnItemLongClickListener}
 * @Description : {@link android.support.v7.widget.RecyclerView}条长按事件
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 21:21:46
 */
public interface OnItemLongClickListener {
	/**
	 * {@link android.support.v7.widget.RecyclerView}长按事件
	 * @param view
	 * @param position
	 */
	public void onItemLongClick(View view, int position);
}
