package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.util.PTToast;
import com.xiaosw.gallery.util.ScreenUtil;
import com.xiaosw.gallery.viewer.PhotoPageRecyclerView;
import com.xiaosw.gallery.viewer.SupportViewPager;
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
		OnItemClickListener, PhotoPageGallerAdapter.OnItemSelectedChangedListener, View.OnTouchListener {

	public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";

	@Bind(R.id.view_pager_photo_page)
	SupportViewPager mViewPager;

	@Bind(R.id.recycler_view_phogo_page_gallery)
	PhotoPageRecyclerView mRecyclerView;

	@Bind(R.id.tv_title)
	TextView tv_title; // 头标题

	@Bind(R.id.tv_function)
	TextView tv_function; // 头右侧按钮

	@Bind(R.id.view_header_container)
	View view_header_container; // 头


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

		// RecyclerView
		mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mPageGalleryAdapter = new PhotoPageGallerAdapter(getContext(), this);
		mRecyclerView.setAdapter(mPageGalleryAdapter);
		mRecyclerView.setOnTouchListener(this);
		mPageGalleryAdapter.setOnItemClickListener(this);
		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (!mRecyclerView.isTouch()) {
					return;
				}
				int first = mLayoutManager.findFirstVisibleItemPosition();
				if (first == 0) {
					reqeustFocus(first);
					return;
				}
				int last = mLayoutManager.findLastVisibleItemPosition();
				if (last == mPageGalleryAdapter.getItemCount() - 1) {
					reqeustFocus(last);
					return;
				}
				if (first != last) {
					reqeustFocus(last - (last - first) / 2);
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

			}
		});

		// ViewPager
		mPageAdapter = new PhotoPageAdapter(getContext());
		mViewPager.setAdapter(mPageAdapter);
		mViewPager.setOnTouchListener(this);
		mViewPager.addOnPageChangeListener(this);
		mViewPager.setOnItemClickListener(new SupportViewPager.OnItemClickListener() {
			@Override
			public boolean onItemClick(int position) {
				if (view_header_container.getVisibility() == View.VISIBLE) {
					ScreenUtil.setFullScreen(getActivity());
					view_header_container.setVisibility(View.GONE);
					mRecyclerView.setVisibility(View.GONE);
				} else {
					ScreenUtil.cancelFullScreen(getActivity());
					mRecyclerView.setVisibility(View.VISIBLE);
					view_header_container.setVisibility(View.VISIBLE);
				}
				view_header_container.requestLayout();
				return false;
			}
		});
		mViewPager.setCurrentItem(mCurrentIndex);

		tv_title.setText(mPageAdapter.getPageTitle(mCurrentIndex));
		tv_function.setText(R.string.str_photo_perview_header_function);
		return mRootView;
	}

	private void reqeustFocus(int focusPosition) {
		View view = mLayoutManager.findViewByPosition(focusPosition);
		if (null != view) {
            view.requestFocus();
			mViewPager.setCurrentItem(focusPosition);
        }
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
				case R.id.view_pager_photo_page:
					mRecyclerView.setTouch(false);
					break;

				default:

					// do nothing
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.view_pager_photo_page:
					//
					break;

				default:

					// do nothing
			}
		}
		return false;
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
		reqeustFocus(mCurrentIndex);
		tv_title.setText(mPageAdapter.getPageTitle(position));
	}

	@Override
	public void onItemClick(View view, int position, boolean isSelecteMode) {
		mViewPager.setCurrentItem(position);
	}

	@Override
	public int getCurrentPosition() {
		return mCurrentIndex;
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
