package com.xiaosw.gallery.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.nineoldandroids.view.ViewHelper;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.config.AppConfig;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.viewer.DateLineRecyclerView;
import com.xiaosw.gallery.widget.listener.OnItemClickListener;
import com.xiaosw.gallery.widget.listener.OnItemLongClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @ClassName : {@link DateLineTabAdapter}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 16:16:05
 */
public class DateLineTabAdapter extends RecyclerView.Adapter<DateLineTabAdapter.DateLineRecyclerHolder> implements
        BaseRecyclerAdapter<MediaItem> {
    private Context mContext;
    private DateLineRecyclerView mRecyclerView;
    private RequestManager mRequestManager;
    private ArrayList<MediaItem> mMediaItems;

    private ViewGroup.LayoutParams mTitleViewParams;
    private ViewGroup.LayoutParams mWarpContentParams;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private boolean isSelectMode;
    /** 仿重复点击 */
    private long mLastClickTime;

    public DateLineTabAdapter(Context context) {
        this.mContext = context.getApplicationContext();
        this.mRequestManager = Glide.with(mContext);
        mMediaItems = GlobalDataStorage.INSTANCE.getHandleMediaItems();
    }

    @Override
    public DateLineTabAdapter.DateLineRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mRecyclerView = (DateLineRecyclerView) parent;
        mTitleViewParams =  new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) mContext.getResources().getDimension(R.dimen.view_height_date_line_title));
        mWarpContentParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return new DateLineRecyclerHolder(View.inflate(mContext, R.layout.item_date_line_tab, null));
    }

    @Override
    public void onBindViewHolder(DateLineTabAdapter.DateLineRecyclerHolder holder, int position) {
        MediaItem mediaItem = mMediaItems.get(position);
        holder.iv_video_tag.setVisibility(View.GONE);
        if (mediaItem.getState() == MediaItem.SelectState.CHECKED) {
            holder.cb_select_state.setVisibility(View.VISIBLE);
            onScale(holder, 0.8f);
        } else {
            onScale(holder, 1f);
            holder.cb_select_state.setVisibility(View.GONE);
        }
        if (mediaItem.isTitleLine()) {
            holder.getItemView().setLayoutParams(mTitleViewParams);
            holder.iv_image.setImageDrawable(null);
            holder.getItemView().setTag(mediaItem.getTitlePosition());
            mRecyclerView.setDrawCircle(mediaItem, holder.getItemView());
        } else {
            holder.getItemView().setTag(-1);
            if (holder.iv_image.getLayoutParams().height != mRecyclerView.getItemWidth()) {
                holder.iv_image.getLayoutParams().height = mRecyclerView.getItemWidth();
            }
            if (holder.iv_image.getLayoutParams().width != mRecyclerView.getItemWidth()) {
                holder.iv_image.getLayoutParams().width = mRecyclerView.getItemWidth();
            }
            if (!TextUtils.isEmpty(mediaItem.getData())) {
                if (mediaItem.getMimeType().contains("video")) {
                    holder.iv_video_tag.setVisibility(View.VISIBLE);
                }
                mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(mediaItem.getData()))
                        .priority(Priority.HIGH)
                        .override(mRecyclerView.getItemWidth(), mRecyclerView.getItemWidth())
                        .centerCrop()
                        .into(holder.iv_image);
            }
        }
    }

    private void onScale(DateLineRecyclerHolder holder, float scale) {
        ViewHelper.setPivotX(holder.iv_image, mRecyclerView.getItemWidth() / 2);
        ViewHelper.setPivotY(holder.iv_image, mRecyclerView.getItemWidth() / 2);
        ViewHelper.setScaleX(holder.iv_image, scale);
        ViewHelper.setScaleY(holder.iv_image, scale);
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
        private View mItemView;
        @Bind(R.id.iv_image)
        ImageView iv_image;

        @Bind(R.id.iv_video_tag)
        ImageView iv_video_tag;

        @Bind(R.id.cb_select_state)
        CheckBox cb_select_state;
        public DateLineRecyclerHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            ButterKnife.bind(this, mItemView);
            mItemView.setOnClickListener(this);
            mItemView.setOnLongClickListener(this);
        }

        public View getItemView() {
            return mItemView;
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
                    return;
                }
                mOnItemClickListener.onItemClick(v, getLayoutPosition(), isSelectMode);
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
                int position = getAdapterPosition();
                mMediaItems.get(position).setState(MediaItem.SelectState.CHECKED);
                notifyItemChanged(position);
                mOnItemLongClickListener.onItemLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }

}