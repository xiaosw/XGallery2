package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.widget.adapter.PhotoAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link PhotoFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:09
 */
public class PhotoFragment extends MediaDataObserverFragment {

	public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";

	@Bind(R.id.recycler_view_photo)
	RecyclerView mRecyclerview;

	private int mCurrentIndex;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_photo, null);
		mCurrentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
		ButterKnife.bind(this, mRootView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		mRecyclerview.setLayoutManager(layoutManager);
		mRecyclerview.setAdapter(new PhotoAdapter(getContext()));
		mRecyclerview.getRecycledViewPool().setMaxRecycledViews(0, 20);
		layoutManager.scrollToPosition(mCurrentIndex);
		return mRootView;
	}

	public void updateCurrentIndex(int newIndex) {
		this.mCurrentIndex = newIndex;
	}

}
