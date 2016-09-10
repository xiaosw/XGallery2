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
import com.xiaosw.gallery.util.GlobalDataStorage;
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
    private ViewRecyler<ImageView> mViewRecyler;
    private RequestManager mRequestManager;

    public PhotoPageAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.mContext = context;
        this.mRequestManager = Glide.with(mContext);
        mViewRecyler = new ViewRecyler<ImageView>(3);
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
        ImageView convertView = mViewRecyler.pop();
        if (convertView == null) {
            convertView = (ImageView) View.inflate(mContext, R.layout.view_image, null);
        }
        convertView.setImageDrawable(null);
        MediaItem mediaItem = mMediaItems.get(position);
        String filePath = mediaItem.getData();
        if (!TextUtils.isEmpty(filePath)) {
            mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(mediaItem.getData()))
                    .priority(Priority.HIGH)
                    .override(container.getMeasuredWidth(), container.getMeasuredHeight())
                    .fitCenter()
                    .into(convertView);
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mMediaItems.get(position).getCreateDate();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof ImageView) {
            ImageView imageView = (ImageView) object;
            imageView.setImageDrawable(null);
            container.removeView(imageView);
            mViewRecyler.add(imageView);
        }
    }

}
