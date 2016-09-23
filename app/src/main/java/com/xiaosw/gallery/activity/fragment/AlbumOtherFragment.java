package com.xiaosw.gallery.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.widget.adapter.AlbumFolderOtherAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName {@link AlbumOtherFragment}
 * @Description 相册其它页
 *
 * @Date 2016-09-10 22:02.
 * @Author xiaoshiwang.
 */
public class AlbumOtherFragment extends ContainerHeaderFragment<MediaItem> {
    private String TAG = "AlbumOtherFragment";
    @Bind(R.id.list_view_album_other)
    ListView mListView;

    private ArrayList<MediaFolder> mMediaFolders;
    private AlbumFolderOtherAdapter mAlbumFolderOtherAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMediaFolders = new ArrayList<>();
        initOtherFolders();
    }

    @Override
    View onCreateRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_album_other, null);
        ButterKnife.bind(this, mRootView);
        setTitle(R.string.str_folder_name_other);
        mAlbumFolderOtherAdapter = new AlbumFolderOtherAdapter(getContext(), mMediaFolders, mListView);
        mListView.setAdapter(mAlbumFolderOtherAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaFolder mediaFolder = mMediaFolders.get(position);
                Fragment fragment = new AlbumFragment();
                Bundle args = new Bundle();
                args.putString(PhotoPageFragment.KEY_BUCKET_ID, mediaFolder.getBucketId());
                args.putString(ContainerHeaderFragment.KEY_TITLE, mediaFolder.getFolderName());
                fragment.setArguments(args);
                switchFragment(fragment);
            }
        });
        return mRootView;
    }

    @Override
    public void notifyChange(ArrayList<MediaItem> srcData, ArrayList<MediaItem> handleData) {
        initOtherFolders();
        if (isVisible()) {
            mAlbumFolderOtherAdapter.notifyDataSetChanged();
        } else {
            needRefresh = true;
        }

    }

    private ArrayList<MediaFolder> initOtherFolders() {
        mMediaFolders.clear();
        for (MediaFolder mediaFolder : GlobalDataStorage.INSTANCE.getMediaFolders()) {
            if (!mediaFolder.isOther()) {
                mMediaFolders.add(mediaFolder);
            }
        }
        return mMediaFolders;
    }
}
