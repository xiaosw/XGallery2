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
import com.xiaosw.gallery.viewer.BaseRecyclerView;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link AlbumAdapter}
 * @Description : 相册目录预览
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 16:16:05
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.DateLineRecyclerHolder> implements
        BaseRecyclerAdapter<MediaItem> {
    private Context mContext;
    private BaseRecyclerView mRecyclerView;
    private RequestManager mRequestManager;
    private ArrayList<MediaItem> mMediaItems;
    private ViewGroup.LayoutParams mWarpContentParams;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private boolean isSelectMode;
    /** 仿重复点击 */
    private long mLastClickTime;

    public AlbumAdapter(Context context, ArrayList<MediaItem> data) {
        this.mContext = context.getApplicationContext();
        this.mRequestManager = Glide.with(mContext);
        mMediaItems = data;
    }

    @Override
    public AlbumAdapter.DateLineRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mWarpContentParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!(parent instanceof BaseRecyclerView)) {
            throw new IllegalArgumentException("RecyclerView must instaceof BaseRecyclerView!!!");
        }
        mRecyclerView = (BaseRecyclerView) parent;
        mRecyclerView.handleNumColumns();
        return new DateLineRecyclerHolder(View.inflate(mContext, R.layout.item_album, null));
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.DateLineRecyclerHolder holder, int position) {
        MediaItem mediaItem = mMediaItems.get(position);
        holder.itemView.setTag(-1);
        if (holder.iv_image.getLayoutParams().height != mRecyclerView.getItemSize().width) {
            holder.iv_image.getLayoutParams().height = mRecyclerView.getItemSize().width;
        }
        if (holder.iv_image.getLayoutParams().width != mRecyclerView.getItemSize().width) {
            holder.iv_image.getLayoutParams().width = mRecyclerView.getItemSize().width;
        }
        if (!TextUtils.isEmpty(mediaItem.getData())) {
            mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(mediaItem.getData()))
                .priority(Priority.HIGH)
                .override(mRecyclerView.getItemSize().width, mRecyclerView.getItemSize().width)
                .centerCrop()
                .into(holder.iv_image);
        }
    }

    @Override
    public int getItemCount() {
        return mMediaItems.size();
    }

    public boolean isSelectMode (){
        return isSelectMode;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setSelectMode(boolean isSelectMode) {
        this.isSelectMode = isSelectMode;
        for (MediaItem mediaItem : mMediaItems) {
            mediaItem.setState(MediaItem.SelectState.DISABLE);
        }
        notifyDataSetChanged();
    }

    @Override
    public ArrayList<MediaItem> getData() {
        return mMediaItems;
    }

    public class DateLineRecyclerHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        @Bind(R.id.iv_image)
        ImageView iv_image;
        public DateLineRecyclerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastClickTime < AppConfig.DURATION_CLICK_GAP) {
                mLastClickTime = currentTime;
                return;
            }
            mLastClickTime = currentTime;
            if (null != mOnItemClickListener) {
                int position = getAdapterPosition();
                if (position < 0 || position >= getItemCount()) {
                    return;
                }
                MediaItem mediaItem = mMediaItems.get(position);
                if (isSelectMode) {
                    MediaItem.SelectState state = mediaItem.getState();
                    if (state == MediaItem.SelectState.CHECKED) {
                        mediaItem.setState(MediaItem.SelectState.UNCHECKED);
                    } else {
                        mediaItem.setState(MediaItem.SelectState.CHECKED);
                    }
                    notifyItemChanged(position);
                }
                int layoutPosition = getLayoutPosition();
                mOnItemClickListener.onItemClick(v, layoutPosition, isSelectMode);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != mOnItemLongClickListener) {
                if (isSelectMode()) { // 已经是选择模式时，不做处理
                    return false;
                }
                isSelectMode = true;
                for (MediaItem mediaItem : mMediaItems) {
                    mediaItem.setState(MediaItem.SelectState.UNCHECKED);
                }
                mMediaItems.get(getAdapterPosition()).setState(MediaItem.SelectState.CHECKED);
                notifyItemChanged(getLayoutPosition());
                mOnItemLongClickListener.onItemLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }

}