package com.xiaosw.gallery.activity.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
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
import butterknife.OnClick;

/**
 * @ClassName : {@link PhotoPageFragment}
 * @Description : 单张预览
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:09
 */
public class PhotoPageFragment extends ContainerHeaderFragment<MediaItem> implements ViewPager.OnPageChangeListener,
    SupportViewPager.OnItemClickListener {

    public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";
    public static final String KEY_BUCKET_ID = "BUCKET_ID";

    @Bind(R.id.view_pager_photo_page)
    SupportViewPager mViewPager;

    @Bind(R.id.view_navigation_span)
    View view_navigation_span;

    @Bind(R.id.view_bottom_function)
    View view_bottom_function;

    /** 当前现实图片的下标 */
    private int mCurrentIndex;
    /** 过滤条件 */
    private String mFilterWhere;
    /** 单张图片 */
    private PhotoPageAdapter mPageAdapter;
    private ArrayList<MediaItem> mMediaItems;

    @Override
    View onCreateRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScreenUtil.setFullScreen(getActivity());
        mRootView = inflater.inflate(R.layout.fragment_photo_page, null);
        mCurrentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
        mFilterWhere = getArguments().getString(KEY_BUCKET_ID);
        ButterKnife.bind(this, mRootView);
        initView();
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

        mMediaItems = GlobalDataStorage.INSTANCE.getTargetMediaItems();
        // ViewPager
        mPageAdapter = new PhotoPageAdapter(getContext(), mMediaItems);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnItemClickListener(this);
        setTitle(mPageAdapter.getPageTitle(mCurrentIndex));
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
    public boolean onItemClick(int position) {
        toggleAnim();
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ScreenUtil.cancelFullScreen(getActivity());
    }

    @Override
    void doFunctionOperation() {
        PTToast.show(getContext(), "Edit", Toast.LENGTH_SHORT);
    }

    @Override
    int getRealMediaItemSize() {
        return mMediaItems.size();
    }

    @Override
    void updateDateNeeded() {
        mMediaItems = GlobalDataStorage.INSTANCE.getTargetMediaItems();
    }

    @Override
    public void refresh() {
        mPageAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_share, R.id.iv_edit, R.id.iv_info, R.id.iv_delete})
    void onFunctionClick(View view) {
        switch (view.getId()) {
            case R.id.iv_share:
                MediaItem mediaItem = mPageAdapter.getObjectByPosition(mCurrentIndex);
                String mimeType = mediaItem.getMimeType();
                doShare(getContentUri(mimeType, mediaItem.getId()), mimeType);
                break;

            case R.id.iv_edit:

                break;

            case R.id.iv_info:

                break;

            case R.id.iv_delete:
                doDelete(mPageAdapter.getObjectByPosition(mCurrentIndex));
                break;

            default:
                // do nothing
        }
    }

    public Uri getContentUri(String mimeType, int id) {
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        if (mimeType.contains("video")) {
            baseUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        return baseUri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    private void doDelete(MediaItem mediaItem) {
        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.delete(getContentUri(mediaItem.getMimeType(), mediaItem.getId()), null, null);
    }

    private void doShare(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.setDataAndType(uri, mimeType);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.str_tips_please_select)));
    }


    ///////////////////////////////////////////////////////////////////////////
    // 动效处理
    ///////////////////////////////////////////////////////////////////////////
    private volatile boolean isShowing = true;
    private ValueAnimator mValueAnimator;
    private float lastTo;

    public void toggleAnim() {
        if (isShowing) {
            hide();
        } else {
            show();
        }
        isShowing = !isShowing;
    }

    private void show() {
        startAnimator(0f, 1f, new DecelerateInterpolator(), 300);
    }

    private void hide() {
        startAnimator(1f, 0f, new AccelerateInterpolator(), 300);
    }

    private void startAnimator(float from, final float to, Interpolator interpolator, long duration) {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.setFloatValues(lastTo);
            mValueAnimator.end();
            view_bottom_function.clearAnimation();
            view_header_container.clearAnimation();
        }
        lastTo = to;
        mValueAnimator = ValueAnimator.ofFloat(from, to);
        mValueAnimator.setDuration(duration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                ViewHelper.setTranslationY(view_header_container, (int) (view_header_container.getHeight() * (value - 1.0f)));
                ViewHelper.setTranslationY(view_bottom_function, (int) (view_bottom_function.getHeight() * (1.0f - value)));
            }
        });
        mValueAnimator.start();
    }
}
