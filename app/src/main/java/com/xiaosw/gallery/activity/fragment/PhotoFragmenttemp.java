package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.util.ScreenUtil;
import com.xiaosw.gallery.viewer.divider.DividerVerticalDecoration;
import com.xiaosw.gallery.widget.adapter.PhotoPageGallerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link PhotoPageFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:09
 */
public class PhotoFragmenttemp extends MediaDataObserverFragment {

	public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";

	@Bind(R.id.recycler_view_photo)
	RecyclerView mRecyclerview;

	private int mCurrentIndex;
	private PhotoPageGallerAdapter mAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_photo, null);
		ScreenUtil.setFullScreen(getActivity());
		mCurrentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
		ButterKnife.bind(this, mRootView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		mRecyclerview.addItemDecoration(new DividerVerticalDecoration(getContext()));
		mRecyclerview.setLayoutManager(layoutManager);
		mAdapter = new PhotoPageGallerAdapter(getContext());
		mRecyclerview.setAdapter(mAdapter);
		layoutManager.scrollToPosition(mCurrentIndex);
		return mRootView;
	}

	@Override
	public void onDestroyView() {
		ScreenUtil.cancelFullScreen(getActivity());
		super.onDestroyView();
	}

	public void updateCurrentIndex(int newIndex) {
		this.mCurrentIndex = newIndex;
	}

}
