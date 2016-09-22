package com.xiaosw.gallery.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.viewer.SupportGridView;
import com.xiaosw.gallery.widget.adapter.AlbumFolderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link AlbumFragment}
 * @Description : 相册页
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class AlbumFragment extends MediaDataObserverFragment<MediaFolder> implements AdapterView.OnItemClickListener {
    private String TAG = "AlbumFragment";

    @Bind(R.id.grid_view)
    SupportGridView mGridView;

    private AlbumFolderAdapter mAlbumFolderAdapter;
    private ArrayList<MediaFolder> mMediaFolders;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMediaFolders = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_album_page, null);
        ButterKnife.bind(this, mRootView);
        mGridView.setOnItemClickListener(this);
        ArrayList<MediaFolder> mediaFolders = GlobalDataStorage.INSTANCE.getMediaFolders();
        initMediaFolders(mediaFolders);
        LogUtil.e("mainMediaFolders-----------> " + mMediaFolders.size() + ", mediaFolders = " + mediaFolders.size());
        mAlbumFolderAdapter = new AlbumFolderAdapter(getContext(), mMediaFolders, mGridView);
        mGridView.setAdapter(mAlbumFolderAdapter);
        return mRootView;
    }

    @NonNull
    private ArrayList<MediaFolder> initMediaFolders(ArrayList<MediaFolder> mediaFolders) {
        mMediaFolders.clear();
        for (MediaFolder mediaFolder : mediaFolders) {
            if (!mediaFolder.isOther()) {
                mMediaFolders.add(mediaFolder);
            }
        }
        return mMediaFolders;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh) {
            mAlbumFolderAdapter.notifyDataSetChanged();
            needRefresh = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<MediaFolder> mediaFolders = mAlbumFolderAdapter.getData();
        MediaFolder mediaFolder = mediaFolders.get(position);
        Fragment fragment;
        if (position == mediaFolders.size() - 1) {
            fragment = new AlbumOtherFragment();
        } else {
            fragment = new PhotoPageFragment();
            Bundle args = new Bundle();
            args.putInt(PhotoPageFragment.KEY_CURRENT_INDEX, 0);
            args.putString(PhotoPageFragment.KEY_BUCKET_ID, mediaFolder.getBucketId());
            fragment.setArguments(args);
        }
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void notifyChange(ArrayList<MediaFolder> srcData, ArrayList<MediaFolder> handleData) {
        LogUtil.e(TAG, "notifyChange----------> isVisible = " + isVisible());
        initMediaFolders(GlobalDataStorage.INSTANCE.getMediaFolders());
        if (isVisible()) {
            mAlbumFolderAdapter.notifyDataSetChanged();
        } else {
            needRefresh = true;
        }

    }
}
