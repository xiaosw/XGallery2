package com.xiaosw.gallery.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.activity.MainActivity;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.PTToast;
import com.xiaosw.gallery.util.ScreenUtil;
import com.xiaosw.gallery.viewer.SupportViewPager;
import com.xiaosw.gallery.widget.adapter.PhotoPageAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link PhotoPageFragment}
 * @Description : 单张预览
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:09
 */
public class PhotoPageFragment extends ContainerHeaderFragment<MediaItem> implements ViewPager.OnPageChangeListener {

    public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";
    public static final String KEY_BUCKET_ID = "BUCKET_ID";

    @Bind(R.id.view_pager_photo_page)
    SupportViewPager mViewPager;

    @Bind(R.id.view_navigation_span)
    View view_navigation_span;

    /** 当前现实图片的下标 */
    private int mCurrentIndex;
    /** 过滤条件 */
    private String mFilterWhere;
    /** 单张图片 */
    private PhotoPageAdapter mPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.setFullScreen(getActivity());
        mRootView = inflater.inflate(R.layout.fragment_photo_page, null);
        mCurrentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
        mFilterWhere = getArguments().getString(KEY_BUCKET_ID);
        ButterKnife.bind(this, mRootView);
        initView();
        ArrayList<MediaItem> mediaItems = GlobalDataStorage.INSTANCE.getSrcMediaItems();
        ArrayList<MediaItem> mData = new ArrayList<>();
        if (!TextUtils.isEmpty(mFilterWhere)) {
            for (MediaItem mediaItem : mediaItems) {
                if (mFilterWhere.equals(mediaItem.getBucketId())) {
                    mData.add(mediaItem);
                }
            }
        } else {
            mData.addAll(mediaItems);
        }

        // ViewPager
        mPageAdapter = new PhotoPageAdapter(getContext(), mData);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mCurrentIndex);

        setTitle(mPageAdapter.getPageTitle(mCurrentIndex));
        return mRootView;
    }

    private void initView() {
        view_header_container.setBackgroundColor(Color.TRANSPARENT);
        iv_back.setImageResource(R.mipmap.ic_back_white);
        tv_title.setTextColor(Color.WHITE);
        tv_function.setTextColor(Color.WHITE);
//        setFunctionDescription(R.string.str_photo_perview_header_function);
        ViewGroup.LayoutParams params = view_navigation_span.getLayoutParams();
        params.height = ((MainActivity) mActivity).getNavigationHeight();
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
        setTitle(mPageAdapter.getPageTitle(position));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ScreenUtil.cancelFullScreen(getActivity());
    }

    @Override
    void doFunctionOperation() {
        PTToast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT);
    }
}
