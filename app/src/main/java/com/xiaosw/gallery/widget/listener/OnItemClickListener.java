package com.xiaosw.gallery.widget.listener;

import android.view.View;

/**
 * @ClassName : {@link OnItemClickListener}
 * @Description :{@link android.support.v7.widget.RecyclerView} 条目点击事件
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 21:21:48
 */
public interface OnItemClickListener {
	/**
	 * {@link android.support.v7.widget.RecyclerView} 条目点击事件
	 * @param view
	 * @param position
	 * @param isSelecteMode
	 */
	public void onItemClick(View view, int position, boolean isSelecteMode);
}