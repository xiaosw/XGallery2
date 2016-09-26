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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName : {@link MainFragment}
 * @Description : 首页
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:55
 */
public class MainFragment extends BaseFragment implements DateLineTabFragment.OnSelectStateChange {

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

    @Bind(R.id.main_header_container)
    View view_main_header_container; // 主页头部

    @Bind(R.id.view_header_container)
    View view_functoin_header_container; // 功能操作头部

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, mRootView);
        view_functoin_header_container.setVisibility(View.GONE);
        initViewPager();
        return mRootView;
    }

    @Override
    public void onSelectStateChange(boolean inSelectMode) {
        if (inSelectMode) {
            view_main_header_container.setVisibility(View.GONE);
            view_functoin_header_container.setVisibility(View.VISIBLE);
        }

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

    DateLineTabFragment mDateLineTabFragment;
    private ArrayList<Fragment> initPages() {
        mPages.clear();
        mDateLineTabFragment = null;
        mDateLineTabFragment = new DateLineTabFragment();
        mDateLineTabFragment.setOnSelectStateChange(this);
        mPages.add(mDateLineTabFragment);
        mPages.add(new AlbumTabFragment());
        return mPages;
    }

    /**
     * 头部切换事件
     * @param view
     */
    @OnClick({R.id.cb_album_tab, R.id.cb_date_line_tab, R.id.iv_back})
    public void onSwitchTab(View view) {
        switch (view.getId()) {
            case R.id.cb_date_line_tab:
                mMainViewPager.setCurrentItem(TAB_POSITION_DATE_LIEN);
                break;

            case R.id.cb_album_tab:
                mMainViewPager.setCurrentItem(TAB_POSITION_ALBUM);
                break;

            case R.id.iv_back:
                view_main_header_container.setVisibility(View.VISIBLE);
                view_functoin_header_container.setVisibility(View.GONE);
                mDateLineTabFragment.exitSelectMode();
                break;

            default:
                // do nothing
        }
    }


}
