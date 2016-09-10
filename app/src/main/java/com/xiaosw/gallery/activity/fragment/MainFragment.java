package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.viewer.CheckBoxTab;
import com.xiaosw.gallery.widget.adapter.MainPagerAdapter;

import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName : {@link MainFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:55
 */
public class MainFragment extends BaseFragment {

	/** 时间轴 */
	public static final int TAB_POSITION_DATE_LIEN = 0;
	/** 相册 */
	public static final int TAB_POSITION_ALBUM = 1;

	@Bind(R.id.view_pager_main)
	ViewPager mMainViewPager;

	// pager数据
	private ArrayList<Fragment> mPages;
	private MainPagerAdapter mMainPagerAdapter;

	// 头部
	@Bind(R.id.cb_date_line_tab)
	CheckBoxTab mDateLineTab; // 时间轴

	@Bind(R.id.cb_album_tab)
	CheckBoxTab mAlbummTab; // 时间轴

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_main, null);
		ButterKnife.bind(this, mRootView);
		initViewPager();
		return mRootView;
	}

	private void initViewPager() {
		mPages = new ArrayList<>();
		initPages();
		mMainPagerAdapter = new MainPagerAdapter(getChildFragmentManager(), mPages);
		mMainViewPager.setAdapter(mMainPagerAdapter);
		mMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case TAB_POSITION_DATE_LIEN:
						mDateLineTab.setChecked(true);
						mAlbummTab.setChecked(false);
						break;
					case TAB_POSITION_ALBUM:
						mDateLineTab.setChecked(false);
						mAlbummTab.setChecked(true);
						break;

					default:
						// do nothing
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private ArrayList<Fragment> initPages() {
		mPages.clear();

		mPages.add(new DateLineFragment());
		mPages.add(new AlbumFragment());
		return mPages;
	}

	/**
	 * 头部切换事件
	 * @param view
	 */
	@OnClick({R.id.cb_album_tab, R.id.cb_date_line_tab})
	public void onSwitchTab(View view) {
		switch (view.getId()) {
			case R.id.cb_date_line_tab:
				mMainViewPager.setCurrentItem(TAB_POSITION_DATE_LIEN);
				break;

			case R.id.cb_album_tab:
				mMainViewPager.setCurrentItem(TAB_POSITION_ALBUM);
				break;

			default:
				// do nothing
		}
	}
}
