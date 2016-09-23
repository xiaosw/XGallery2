//package com.xiaosw.gallery.activity.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.xiaosw.gallery.R;
//import com.xiaosw.gallery.bean.MediaItem;
//import com.xiaosw.gallery.util.GlobalDataStorage;
//import com.xiaosw.gallery.util.PTToast;
//import com.xiaosw.gallery.util.ScreenUtil;
//import com.xiaosw.gallery.viewer.PhotoPageRecyclerView;
//import com.xiaosw.gallery.viewer.SupportViewPager;
//import com.xiaosw.gallery.widget.adapter.PhotoPageAdapter;
//import com.xiaosw.gallery.widget.adapter.PhotoPageGallerAdapter;
//import com.xiaosw.gallery.widget.listener.OnItemClickListener;
//
//import java.util.ArrayList;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * @ClassName : {@link PhotoPageFragment_Temp}
// * @Description : 单张预览
// *
// * @Author xiaosw<xiaoshiwang@putao.com>
// * @Date 2016-09-09 22:22:09
// */
//public class PhotoPageFragment_Temp extends ContainerHeaderFragment<MediaItem> implements ViewPager.OnPageChangeListener,
//        OnItemClickListener, PhotoPageGallerAdapter.OnItemSelectedChangedListener, View.OnTouchListener {
//
//    public static final String KEY_CURRENT_INDEX = "CURRENT_INDEX";
//    public static final String KEY_BUCKET_ID = "BUCKET_ID";
//
//    @Bind(R.id.view_pager_photo_page)
//    SupportViewPager mViewPager;
//
//    @Bind(R.id.recycler_view_phogo_page_gallery)
//    PhotoPageRecyclerView mRecyclerView;
//
//    /** 当前现实图片的下标 */
//    private int mCurrentIndex;
//    /** 过滤条件 */
//    private String mFilterWhere;
//    /** 单张图片 */
//    private PhotoPageAdapter mPageAdapter;
//    /** 底部gallery */
//    private PhotoPageGallerAdapter mPageGalleryAdapter;
//    private LinearLayoutManager mLayoutManager;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mRootView = inflater.inflate(R.layout.fragment_photo_page, null);
//        mCurrentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
//        mFilterWhere = getArguments().getString(KEY_BUCKET_ID);
//        ButterKnife.bind(this, mRootView);
//
//        // RecyclerView
//        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        ArrayList<MediaItem> mediaItems = GlobalDataStorage.INSTANCE.getSrcMediaItems();
//        ArrayList<MediaItem> mData = new ArrayList<>();
//        if (!TextUtils.isEmpty(mFilterWhere)) {
//            for (MediaItem mediaItem : mediaItems) {
//                if (mFilterWhere.equals(mediaItem.getBucketId())) {
//                    mData.add(mediaItem);
//                }
//            }
//        } else {
//            mData.addAll(mediaItems);
//        }
//        mPageGalleryAdapter = new PhotoPageGallerAdapter(getContext(), mData, this);
//        mRecyclerView.setAdapter(mPageGalleryAdapter);
//        mRecyclerView.setOnTouchListener(this);
//        mPageGalleryAdapter.setOnItemClickListener(this);
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!mRecyclerView.isTouch()) {
//                    return;
//                }
//                int first = mLayoutManager.findFirstVisibleItemPosition();
//                if (first == 0) {
//                    reqeustFocus(first);
//                    return;
//                }
//                int last = mLayoutManager.findLastVisibleItemPosition();
//                if (last == mPageGalleryAdapter.getItemCount() - 1) {
//                    reqeustFocus(last);
//                    return;
//                }
//                if (first != last) {
//                    reqeustFocus(last - (last - first) / 2);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//            }
//        });
//
//        // ViewPager
//        mPageAdapter = new PhotoPageAdapter(getContext(), mData);
//        mViewPager.setAdapter(mPageAdapter);
//        mViewPager.setOnTouchListener(this);
//        mViewPager.addOnPageChangeListener(this);
//        mViewPager.setOnItemClickListener(new SupportViewPager.OnItemClickListener() {
//            @Override
//            public boolean onItemClick(int position) {
//                if (view_header_container.getVisibility() == View.VISIBLE) {
//                    ScreenUtil.setFullScreen(getActivity());
//                    view_header_container.setVisibility(View.GONE);
//                    mRecyclerView.setVisibility(View.GONE);
//                } else {
//                    ScreenUtil.cancelFullScreen(getActivity());
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    view_header_container.setVisibility(View.VISIBLE);
//                }
//                view_header_container.requestLayout();
//                return false;
//            }
//        });
//        mViewPager.setCurrentItem(mCurrentIndex);
//
//        setTitle(mPageAdapter.getPageTitle(mCurrentIndex));
//        setFunctionDescription(R.string.str_photo_perview_header_function);
//        return mRootView;
//    }
//
//    private void reqeustFocus(int focusPosition) {
//        View view = mLayoutManager.findViewByPosition(focusPosition);
//        if (null != view) {
//            view.requestFocus();
//            mViewPager.setCurrentItem(focusPosition);
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            switch (v.getId()) {
//                case R.id.view_pager_photo_page:
//                    mRecyclerView.setTouch(false);
//                    break;
//
//                default:
//
//                    // do nothing
//            }
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            switch (v.getId()) {
//                case R.id.view_pager_photo_page:
//                    //
//                    break;
//
//                default:
//
//                    // do nothing
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        mCurrentIndex = position;
//        mLayoutManager.scrollToPosition(mCurrentIndex);
//        reqeustFocus(mCurrentIndex);
//        setTitle(mPageAdapter.getPageTitle(position));
//    }
//
//    @Override
//    public void onItemClick(View view, int position, boolean isSelecteMode) {
//        mViewPager.setCurrentItem(position);
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        return mCurrentIndex;
//    }
//
//    public void updateCurrentIndex(int newIndex) {
//        this.mCurrentIndex = newIndex;
//    }
//
//    @Override
//    void doFunctionOperation() {
//        PTToast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT);
//    }
//}
