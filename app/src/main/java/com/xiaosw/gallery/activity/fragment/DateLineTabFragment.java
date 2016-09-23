package com.xiaosw.gallery.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.activity.MainActivity;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.viewer.DateLineRecyclerView;
import com.xiaosw.gallery.viewer.divider.DividerGridItemDecoration;
import com.xiaosw.gallery.widget.adapter.DateLineTabAdapter;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link DateLineTabFragment}
 * @Description : 时间轴页
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class DateLineTabFragment extends MediaDataObserverFragment<MediaItem> implements OnItemClickListener,
        OnItemLongClickListener {

    private String TAG = "DateLineTabFragment";

    @Bind(R.id.recycler_view)
    DateLineRecyclerView mRecyclerView;

    @Bind(R.id.iv_not_media)
    ImageView mImageView;
    private RecyclerView.Adapter mAdapter;

    private MainActivity mMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mActivity instanceof MainActivity) {
            mMainActivity = (MainActivity) mActivity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_date_line_page, null);
        ButterKnife.bind(this, mRootView);
        initRecyclerView();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh) {
            mAdapter.notifyDataSetChanged();
            needRefresh = false;
        }
    }

    private void initRecyclerView() {
        mAdapter = new DateLineTabAdapter(getContext());
        // 设置布局管理器
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemLongClickListener(this);
        mRecyclerView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position, boolean isSelecteMode) {
        MediaItem mediaItem = GlobalDataStorage.INSTANCE.getHandleMediaItems().get(position);
        if (mediaItem.getId() == -1) {
            return;
        }
        if (null != mMainActivity
            && mMainActivity.getActionType() == MainActivity.ACTION_TYPE_SELECT) { // select
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; // default is image
            if (mediaItem.getMimeType().contains("video/")) {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            }
            Intent intent = new Intent(null, Uri.parse(uri + File.separator + mediaItem.getId()))
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
            return;
        }
        int realPosition = GlobalDataStorage.INSTANCE.getRealPositionByMediaItem(mediaItem);
        PhotoPageFragment photoFragment = new PhotoPageFragment();
        Bundle args = new Bundle();
        args.putInt(PhotoPageFragment.KEY_CURRENT_INDEX, realPosition);
        photoFragment.setArguments(args);
        switchFragment(photoFragment);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void notifyChange(ArrayList<MediaItem> srcData, ArrayList<MediaItem> handleData) {
        LogUtil.e(TAG, "notifyChange----------> isVisible = " + isVisible());
        mImageView.setVisibility(srcData.size() > 0 ? View.GONE : View.VISIBLE);
        if (isVisible()) {
            mAdapter.notifyDataSetChanged();
        } else {
            needRefresh = true;
        }

    }
}
