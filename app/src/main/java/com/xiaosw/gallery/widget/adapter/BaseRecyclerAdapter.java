package com.xiaosw.gallery.widget.adapter;

import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.util.ArrayList;

/**
 * @ClassName : {@link BaseRecyclerAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 21:21:33
 */
public interface BaseRecyclerAdapter<T> {

    public void setOnItemClickListener(OnItemClickListener onItemClickListener);

    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener);

    public ArrayList<T> getData();

}
