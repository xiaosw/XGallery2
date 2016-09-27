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

import com.android.gallery3d.filtershow.FilterShowActivity;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.activity.MainActivity;
import com.xiaosw.gallery.activity.MediaInfoActivity;
import com.xiaosw.gallery.activity.MovieActivity;
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
    SupportViewPager.OnItemClickListener, PhotoPageAdapter.OnPlayListener {

    public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";
    public static final String KEY_BUCKET_ID = "BUCKET_ID";

    @Bind(R.id.view_pager_photo_page)
    SupportViewPager mViewPager;

    @Bind(R.id.view_navigation_span)
    View view_navigation_span;

    @Bind(R.id.iv_edit)
    View iv_edit;

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
//        view_header_container.setBackgroundColor(Color.TRANSPARENT);
        view_header_container.setBackgroundResource(R.mipmap.bg_top_bar);
        iv_back.setImageResource(R.mipmap.ic_back_white);
        tv_title.setTextColor(Color.WHITE);
        tv_function.setTextColor(Color.WHITE);
//        setFunctionDescription(R.string.str_photo_perview_header_function);
        ViewGroup.LayoutParams params = view_navigation_span.getLayoutParams();
        params.height = ((MainActivity) mActivity).getNavigationHeight();

        mMediaItems = GlobalDataStorage.INSTANCE.getTargetMediaItems();
        // ViewPager
        mPageAdapter = new PhotoPageAdapter(getContext(), mMediaItems);
        mPageAdapter.setOnPlayListener(this);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.setOnItemClickListener(this);
        setTitle(mPageAdapter.getPageTitle(mCurrentIndex));
        handleFunctionItemByMimeType(mMediaItems.get(mCurrentIndex).getMimeType());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPlay(String path, int id) {
        Intent intent = new Intent(getActivity(), MovieActivity.class);
        intent.putExtra(MainActivity.KEY_NAVIGATION_HEIGHT, ((MainActivity) getActivity()).getNavigationHeight());
        intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        setTitle(mPageAdapter.getPageTitle(position));
        handleFunctionItemByMimeType(mMediaItems.get(position).getMimeType());
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
        MediaItem mediaItem = mPageAdapter.getObjectByPosition(mCurrentIndex);
        switch (view.getId()) {
            case R.id.iv_share:
                String mimeType = mediaItem.getMimeType();
                doShareSingle(getContentUri(mimeType, mediaItem.getId()), mimeType);
                break;

            case R.id.iv_edit:
                Intent intent = new Intent(mActivity, FilterShowActivity.class);
                intent.setDataAndType(getContentUri(mediaItem.getMimeType(), mediaItem.getId()), mediaItem.getMimeType())
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(FilterShowActivity.LAUNCH_FULLSCREEN, true);
                mActivity.startActivityForResult(intent, 100);
                break;

            case R.id.iv_info:
                showInfo(mediaItem);
                break;

            case R.id.iv_delete:
                doDelete(mediaItem);
                break;

            default:
                // do nothing
        }
    }

    private void showInfo(MediaItem mediaItem) {
        Intent intent = new Intent(getActivity(), MediaInfoActivity.class);
        intent.putExtra(MediaInfoActivity.KEY_MEDIA_ITEM, mediaItem);
        startActivity(intent);

    }

    private void handleFunctionItemByMimeType(String mimeType) {
        if (mimeType.contains("video/")) {
            iv_edit.setVisibility(View.GONE);
        } else {
            iv_edit.setVisibility(View.VISIBLE);
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

    private void doShareSingle(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.str_tips_please_select)));
    }

    /**
     * 分享多张照片
     */
    private void doShareMultiple(ArrayList<Uri> uris, String mimeType){
        Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType(mimeType);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//        intent.putExtra(Intent.EXTRA_TEXT, "你好 ");
//        intent.putExtra(Intent.EXTRA_TITLE, "我是标题");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
