package com.xiaosw.gallery.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
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
 * @ClassName : {@link PhotoPageGallerAdapter}
 * @Description : 照片预览
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:15
 */
public class PhotoPageGallerAdapter extends RecyclerView.Adapter<PhotoPageGallerAdapter.PhotoViewHolder> implements BaseRecyclerAdapter<MediaItem> {

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private ArrayList<MediaItem> mMediaItems;
    private RequestManager mRequestManager;
    private RecyclerView mRecyclerView;
    private OnItemSelectedChangedListener mOnItemSelectedChangedListener;

    public PhotoPageGallerAdapter(Context context, ArrayList<MediaItem> mediaItems, OnItemSelectedChangedListener listener) {
        this.mContext = context.getApplicationContext();
        this.mRequestManager = Glide.with(mContext);
        mMediaItems = mediaItems;
        this.mOnItemSelectedChangedListener = listener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mRecyclerView = (RecyclerView) parent;
        return new PhotoViewHolder(View.inflate(mContext, R.layout.item_photo_preview, null));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.mPhotoView.setImageDrawable(null);
        MediaItem mediaItem = mMediaItems.get(position);
        String filePath = mediaItem.getData();
        if (!TextUtils.isEmpty(filePath)) {
            mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(mediaItem.getData()))
                    .priority(Priority.HIGH)
                    .override(holder.mPhotoView.getLayoutParams().width, mRecyclerView.getMeasuredHeight())
                    .centerCrop()
                    .into(holder.mPhotoView);
        }
        if (mOnItemSelectedChangedListener != null
                && mOnItemSelectedChangedListener.getCurrentPosition()  == position) {
            holder.mItemView.requestFocus();
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
            int size = mContext.getResources().getDimensionPixelSize(R.dimen.view_height_gallery_item);
            layoutParams.width = (int) (size * 0.8f);
            layoutParams.height = size;
            mPhotoView.setLayoutParams(layoutParams);
            mItemView.setOnClickListener(this);
            mItemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            onClick(v);
                            break;
                    }
                    return false;
                }
            });
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

    public interface OnItemSelectedChangedListener {
        public int getCurrentPosition();
    }

}
