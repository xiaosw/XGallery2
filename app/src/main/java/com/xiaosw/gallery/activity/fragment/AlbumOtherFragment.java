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
import android.widget.TextView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.widget.adapter.AlbumFolderOtherAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName {@link AlbumOtherFragment}
 * @Description 相册其它页
 *
 * @Date 2016-09-10 22:02.
 * @Author xiaoshiwang.
 */
public class AlbumOtherFragment extends MediaDataObserverFragment<MediaFolder> {
    private String TAG = "AlbumOtherFragment";
    @Bind(R.id.list_view_album_other)
    ListView mListView;

    @Bind(R.id.tv_title)
    TextView tv_title;

    private ArrayList<MediaFolder> mMediaFolders;
    private AlbumFolderOtherAdapter mAlbumFolderOtherAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMediaFolders = new ArrayList<>();
        initOtherFolders();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_album_other, null);
        ButterKnife.bind(this, mRootView);
        tv_title.setText(R.string.str_folder_name_other);
        mAlbumFolderOtherAdapter = new AlbumFolderOtherAdapter(getContext(), mMediaFolders, mListView);
        mListView.setAdapter(mAlbumFolderOtherAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaFolder mediaFolder = mMediaFolders.get(position);
                Fragment fragment = new PhotoPageFragment();
                Bundle args = new Bundle();
                args.putInt(PhotoPageFragment.KEY_CURRENT_INDEX, 0);
                args.putString(PhotoPageFragment.KEY_BUCKET_ID, mediaFolder.getBucketId());
                fragment.setArguments(args);
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, fragment, fragment.getClass().getCanonicalName())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return mRootView;
    }

    @Override
    public void notifyChange(ArrayList<MediaFolder> srcData, ArrayList<MediaFolder> handleData) {
        LogUtil.e(TAG, "notifyChange----------> isVisible = " + isVisible());
        initOtherFolders();
        if (isVisible()) {
            mAlbumFolderOtherAdapter.notifyDataSetChanged();
        } else {
            needRefresh = true;
        }

    }

    @OnClick(R.id.iv_back)
    public void onBack(View view) {
        getActivity().onBackPressed();
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
