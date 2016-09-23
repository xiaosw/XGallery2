package com.xiaosw.gallery.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.activity.MainActivity;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.viewer.SuperRecyclervView;
import com.xiaosw.gallery.viewer.divider.DividerGridItemDecoration;
import com.xiaosw.gallery.widget.adapter.AlbumAdapter;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link AlbumFragment}
 * @Description : 相册目录预览
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-22 15:15:09
 */
public class AlbumFragment extends ContainerHeaderFragment<MediaItem> implements OnItemClickListener,
    OnItemLongClickListener {

    @Bind(R.id.super_recycler_view)
    SuperRecyclervView mSuperRecyclervView;

    private AlbumAdapter mAlbumAdapter;
    private MainActivity mMainActivity;
    private String mBucketeId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) mActivity;
        mBucketeId = getArguments().getString(PhotoPageFragment.KEY_BUCKET_ID);
        mMediaItems = GlobalDataStorage.INSTANCE.getTargetMediaItems();
    }

    @Override
    View onCreateRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_album, null);
        ButterKnife.bind(this, mRootView);
        setTitle(getArguments().getString(KEY_TITLE));
        int orientation = getResources().getConfiguration().orientation;
        int numColumns = 0;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numColumns = getResources().getInteger(R.integer.num_coloum_date_line_land);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            numColumns = getResources().getInteger(R.integer.num_coloum_date_line_port);
        }
        mSuperRecyclervView.setOnItemClickListener(this);
        mSuperRecyclervView.setOnItemLongClickListener(this);
        mSuperRecyclervView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        mSuperRecyclervView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
        mAlbumAdapter = new AlbumAdapter(getContext(), mMediaItems);
        mSuperRecyclervView.setAdapter(mAlbumAdapter);
        return mRootView;
    }

    @Override
    int getRealMediaItemSize() {
        return mMediaItems.size();
    }

    @Override
    void updateDateNeeded() {
        mMediaItems = GlobalDataStorage.INSTANCE.filterTargetMediaItemsBucketId(mBucketeId);
    }

    @Override
    public void refresh() {
        mAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position, boolean isSelecteMode) {
        MediaItem mediaItem = GlobalDataStorage.INSTANCE.getSrcMediaItems().get(position);
        if (mediaItem.getId() == -1) {
            return;
        }
        if ( mMainActivity.getActionType() == MainActivity.ACTION_TYPE_SELECT) { // select
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
        PhotoPageFragment photoFragment = new PhotoPageFragment();
        Bundle args = new Bundle();
        args.putInt(PhotoPageFragment.KEY_CURRENT_INDEX, position);
        args.putString(PhotoPageFragment.KEY_BUCKET_ID, mBucketeId);
        photoFragment.setArguments(args);
        switchFragment(photoFragment);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
