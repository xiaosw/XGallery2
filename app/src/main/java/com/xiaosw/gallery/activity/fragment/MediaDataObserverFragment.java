package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.util.GlobalDataStorage;

import java.util.ArrayList;

/**
 * @ClassName : {@link MediaDataObserverFragment}
 * @Description : 监听数据变动并执行Refresh
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class MediaDataObserverFragment<T> extends BaseFragment implements GlobalDataStorage.MediaDataChangeObserver<T> {

    boolean needRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GlobalDataStorage.INSTANCE.registerMediaDataObserver(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        GlobalDataStorage.INSTANCE.unregisterMediaDataObserver(this);
        super.onDestroyView();
    }

    @Override
    public void notifyChange(ArrayList<T> srcData, ArrayList<T> handleData) {

    }
}
