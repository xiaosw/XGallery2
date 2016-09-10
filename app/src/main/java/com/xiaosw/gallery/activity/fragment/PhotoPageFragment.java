package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.util.PTToast;
import com.xiaosw.gallery.util.ScreenUtil;
import com.xiaosw.gallery.viewer.divider.DividerVerticalDecoration;
import com.xiaosw.gallery.widget.adapter.PhotoPageGallerAdapter;
import com.xiaosw.gallery.widget.adapter.PhotoPageAdapter;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName : {@link PhotoPageFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:09
 */
public class PhotoPageFragment extends MediaDataObserverFragment implements ViewPager.OnPageChangeListener,
		OnItemClickListener {

	public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";

	@Bind(R.id.view_pager_photo_page)
	ViewPager mViewPager;

	@Bind(R.id.recycler_view_phogo_page_gallery)
	RecyclerView mRecyclerView;

	@Bind(R.id.tv_title)
	TextView tv_title;

	@Bind(R.id.tv_function)
	TextView tv_function;

	/** 当前现实图片的下标 */
	private int mCurrentIndex;
	/** 单张图片 */
	private PhotoPageAdapter mPageAdapter;
	/** 底部gallery */
	private PhotoPageGallerAdapter mPageGalleryAdapter;
	private LinearLayoutManager mLayoutManager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_photo_page, null);
		mCurrentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
		ButterKnife.bind(this, mRootView);
		mViewPager.addOnPageChangeListener(this);
		mPageAdapter = new PhotoPageAdapter(getContext());
		mViewPager.setAdapter(mPageAdapter);

		mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		mRecyclerView.addItemDecoration(new DividerVerticalDecoration(getContext()));
		mRecyclerView.setLayoutManager(mLayoutManager);
		mPageGalleryAdapter = new PhotoPageGallerAdapter(getContext());
		mPageGalleryAdapter.setOnItemClickListener(this);
		mRecyclerView.setAdapter(mPageGalleryAdapter);
		mLayoutManager.scrollToPosition(mCurrentIndex);
		mViewPager.setCurrentItem(mCurrentIndex);

		tv_function.setText(R.string.str_photo_perview_header_function);
		return mRootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageSelected(int position) {
		mCurrentIndex = position;
		mLayoutManager.scrollToPosition(mCurrentIndex);
		tv_title.setText(mPageAdapter.getPageTitle(position));
	}

	@Override
	public void onItemClick(View view, int position, boolean isSelecteMode) {
		mViewPager.setCurrentItem(position);
	}

	public void updateCurrentIndex(int newIndex) {
		this.mCurrentIndex = newIndex;
	}

	@OnClick(R.id.iv_back)
	public void onBack(View view) {
		getActivity().onBackPressed();
	}

	@OnClick(R.id.tv_function)
	public void doFunction(View view) {
		PTToast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT);
	}

}
