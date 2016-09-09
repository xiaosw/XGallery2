package com.xiaosw.gallery.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.PTToast;
import com.xiaosw.gallery.viewer.DateLineRecyclerView;
import com.xiaosw.gallery.viewer.divider.DividerGridItemDecoration;
import com.xiaosw.gallery.widget.adapter.DateLineAdapter;
import com.xiaosw.gallery.widget.adapter.PhotoAdapter;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link DateLineFragment}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 11:11:51
 */
public class DateLineFragment extends MediaDataObserverFragment implements OnItemClickListener,
		OnItemLongClickListener {
	@Bind(R.id.recycler_view)
	DateLineRecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_date_line_page, null);
		ButterKnife.bind(this, mRootView);
		initRecyclerView();
		return mRootView;
	}

	private void initRecyclerView() {
		mAdapter = new DateLineAdapter(getContext(), mRecyclerView);
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
		int realPosition = GlobalDataStorage.INSTANCE.getRealPositionByMediaItem(mediaItem);
		PhotoFragment photoFragment = new PhotoFragment();
		Bundle args = new Bundle();
		args.putInt(PhotoFragment.KEY_CURRENT_INDEX, realPosition);
		photoFragment.setArguments(args);
		mActivity.getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content, photoFragment, PhotoFragment.class.getSimpleName())
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	@Override
	public void notifyChange(ArrayList<MediaItem> srcData, ArrayList<MediaItem> handleData) {
		super.notifyChange(srcData, handleData);
		mAdapter.notifyDataSetChanged();
	}
}
