package com.xiaosw.gallery.widget.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
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
import com.xiaosw.gallery.widget.helper.ViewRecyler;

import java.util.ArrayList;

/**
 * @ClassName {@link PhotoPageAdapter}
 * @Description 单张照片预览
 * @Date 2016-09-10 11:19.
 * @Author xiaoshiwang.
 */
public class PhotoPageAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<MediaItem> mMediaItems;
    private ViewRecyler<ViewGroup> mViewRecyler;
    private RequestManager mRequestManager;
    private OnPlayListener mOnPlayListener;

    public PhotoPageAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.mContext = context;
        this.mRequestManager = Glide.with(mContext);
        mViewRecyler = new ViewRecyler<ViewGroup>(3);
        mMediaItems = mediaItems;
        Glide.with(mContext);
    }

    @Override
    public int getCount() {
        return mMediaItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup viewGroup = mViewRecyler.pop();
        if (viewGroup == null) {
            viewGroup = (ViewGroup) View.inflate(mContext, R.layout.item_photo_page, null);
        }
        final MediaItem mediaItem = mMediaItems.get(position);
        View iv_play = viewGroup.getChildAt(1);
        iv_play.setVisibility(View.GONE);
        if (mediaItem.getMimeType().contains("video/")) {
            iv_play.setVisibility(View.VISIBLE);
            if (null != mOnPlayListener) {
                iv_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    mOnPlayListener.onPlay(mediaItem.getData(), mediaItem.getId());
                    }
                });
            }
        }

        ImageView iv_photo = (ImageView) viewGroup.getChildAt(0);
        String filePath = mediaItem.getData();
        if (!TextUtils.isEmpty(filePath)) {
            mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(mediaItem.getData()))
                    .priority(Priority.HIGH)
                    .override(container.getMeasuredWidth(), container.getMeasuredHeight())
                    .fitCenter()
                    .into(iv_photo);
        }
        container.addView(viewGroup);
        return viewGroup;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= mMediaItems.size()) {
            return null;
        }
        return  mMediaItems.get(position).getCreateDate();
    }

    public MediaItem getObjectByPosition(int position) {
        if (position >= mMediaItems.size()) {
            return null;
        }
        return mMediaItems.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) object;
            ImageView imageView = (ImageView) viewGroup.getChildAt(0);
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(null);
            container.removeView(viewGroup);
            mViewRecyler.add(viewGroup);
        }
    }

    @Override
    public int getItemPosition(Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

    public void setOnPlayListener(OnPlayListener listener) {
        this.mOnPlayListener = listener;
    }

    public interface OnPlayListener {
        public void onPlay(String path, int id);
    }

}
