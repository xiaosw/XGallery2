package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.GlobalDataStorage;

import java.util.ArrayList;

/**
 * @ClassName : {@link MediaDataObserverFragment}
 * @Description : 监听数据变动并执行Refresh
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public abstract class MediaDataObserverFragment<T> extends BaseFragment implements GlobalDataStorage.MediaDataChangeObserver<T> {

    private boolean mNeedRefresh;
    ArrayList<MediaItem> mMediaItems;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GlobalDataStorage.INSTANCE.registerMediaDataObserver(this);
        return onCreateRootView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedBack()) {
            return;
        }
        if (mNeedRefresh) {
            mNeedRefresh = false;
            refresh();
        }
    }

    abstract View onCreateRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        GlobalDataStorage.INSTANCE.unregisterMediaDataObserver(this);
        super.onDestroyView();
    }

    @Override
    public final void notifyChange(ArrayList<T> srcData, ArrayList<T> handleData) {
        updateDateNeeded();
        if (isVisible()) {
            if (!isNeedBack()) {
                refresh();
            }
        } else {
            mNeedRefresh = true;
        }
    }

    /**
     * 根据数据源判断是否需要执行返回
     * @return
     */
    private boolean isNeedBack() {
        if (getRealMediaItemSize() < 1) {
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

    /**
     * 根据需求更新当前操作数据
     */
    void updateDateNeeded() {

    }

    @Override
    public void refresh() {

    }

    /**
     * 如果MediaItem size为0时需要退出当前界面，则覆写该方法返回当前实际操作集合数量，默认为1,即为空不退出当前界面
     * @return
     */
    int getRealMediaItemSize() {
        return 1;
    }

}
