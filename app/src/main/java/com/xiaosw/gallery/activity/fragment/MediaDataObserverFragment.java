package com.xiaosw.gallery.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.GlobalDataStorage;

import java.util.ArrayList;

/**
 * @ClassName : {@link MediaDataObserverFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class MediaDataObserverFragment extends BaseFragment implements GlobalDataStorage.MediaDataChangeObserver {

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
	public void notifyChange(ArrayList<MediaItem> srcData, ArrayList<MediaItem> handleData) {

	}
}
