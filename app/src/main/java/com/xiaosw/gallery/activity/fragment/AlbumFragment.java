package com.xiaosw.gallery.activity.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.util.SortedList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.config.AppConfig;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.util.MediaCursorHelper;
import com.xiaosw.gallery.viewer.SupportGridView;
import com.xiaosw.gallery.widget.adapter.AlbumFolderAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link AlbumFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class AlbumFragment extends MediaDataObserverFragment<MediaFolder> implements AdapterView.OnItemClickListener {
	private String TAG = "AlbumFragment";

	@Bind(R.id.grid_view)
	SupportGridView mGridView;

	private AlbumFolderAdapter mAlbumFolderAdapter;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mRootView = inflater.inflate(R.layout.fragment_album_page, null);
		ButterKnife.bind(this, mRootView);
		mGridView.setOnItemClickListener(this);
		mAlbumFolderAdapter = new AlbumFolderAdapter(getContext(), mGridView);
		mGridView.setAdapter(mAlbumFolderAdapter);
		return mRootView;
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
		MediaFolder mediaFolder = mAlbumFolderAdapter.getData().get(position);
		PhotoPageFragment photoFragment = new PhotoPageFragment();
		Bundle args = new Bundle();
		args.putInt(PhotoPageFragment.KEY_CURRENT_INDEX, 0);
		args.putString(PhotoPageFragment.KEY_BUCKET_ID, mediaFolder.getBucketId());
		photoFragment.setArguments(args);
		mActivity.getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content, photoFragment, PhotoPageFragment.class.getSimpleName())
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void notifyChange(ArrayList<MediaFolder> srcData, ArrayList<MediaFolder> handleData) {
		LogUtil.e(TAG, "notifyChange----------> isVisible = " + isVisible());
		if (isVisible()) {
			mAlbumFolderAdapter.notifyDataSetChanged();
		} else {
			needRefresh = true;
		}

	}
}
