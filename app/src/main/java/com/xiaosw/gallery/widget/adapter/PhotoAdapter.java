package com.xiaosw.gallery.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.config.AppConfig;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.util.LogUtil;
import com.xiaosw.gallery.util.ScreenUtil;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link PhotoAdapter}
 * @Description : 照片预览
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:15
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> implements BaseRecyclerAdapter<MediaItem> {

	private Context mContext;
	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;
	private ArrayList<MediaItem> mMediaItems;
	private RequestManager mRequestManager;
	public PhotoAdapter(Context context) {
		this.mContext = context.getApplicationContext();
		this.mRequestManager = Glide.with(mContext);
		mMediaItems = GlobalDataStorage.INSTANCE.getSrcMediaItems();
	}

	@Override
	public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new PhotoViewHolder(View.inflate(mContext, R.layout.item_photo_preview, null));
	}

	@Override
	public void onBindViewHolder(PhotoViewHolder holder, int position) {
		MediaItem mediaItem = mMediaItems.get(position);
		String filePath = mediaItem.getData();
		if (!TextUtils.isEmpty(filePath)) {
			mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(mediaItem.getData()))
					.priority(Priority.HIGH)
					.override(holder.mPhotoView.getLayoutParams().width, holder.mPhotoView.getLayoutParams().height)
					.centerCrop()
					.into(holder.mPhotoView);
		}
	}

	@Override
	public int getItemCount() {
		return mMediaItems.size();
	}

	@Override
	public ArrayList<MediaItem> getData() {
		return mMediaItems;
	}

	@Override
	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		this.mOnItemClickListener = itemClickListener;
	}

	@Override
	public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
		this.mOnItemLongClickListener = itemLongClickListener;
	}

	class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
			View.OnLongClickListener {

		View mItemView;
		@Bind(R.id.iv_photo)
		ImageView mPhotoView;

		public PhotoViewHolder(View itemView) {
			super(itemView);
			this.mItemView = itemView;
			ButterKnife.bind(this, mItemView);
			ViewGroup.LayoutParams layoutParams = mPhotoView.getLayoutParams();
			int[] wh = ScreenUtil.getScreenWH(mContext);
			layoutParams.width = wh[0];
			layoutParams.height = wh[1];
			mPhotoView.setLayoutParams(layoutParams);
			mItemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (null != mOnItemClickListener) {
				int layoutPosition = getLayoutPosition();
				mOnItemClickListener.onItemClick(v, layoutPosition, false);
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (mOnItemLongClickListener != null) {
				mOnItemLongClickListener.onItemLongClick(v, getLayoutPosition());
				return true;
			}
			return false;
		}
	}

}
