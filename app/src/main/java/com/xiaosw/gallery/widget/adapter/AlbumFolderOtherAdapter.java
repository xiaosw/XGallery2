package com.xiaosw.gallery.widget.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.config.AppConfig;

import java.util.List;


/**
 * @ClassName {@link AlbumFolderOtherAdapter}
 * @Description
 * @Date 2016-09-10 22:29.
 * @Author xiaoshiwang.
 */
public class AlbumFolderOtherAdapter extends AbsBaseAdapter<MediaFolder> {
    private RequestManager mRequestManager;
    private ListView mListView;
    private int mIconSize;

    public AlbumFolderOtherAdapter(Context context, List<MediaFolder> data, ListView listView) {
        super(context, data, R.layout.item_album_folder_other);
        this.mRequestManager = Glide.with(context);
        this.mListView = listView;
        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.view_height_album_other_icon);
    }

    @Override
    protected void bindData(View convertView, int position, MediaFolder itemData) {
        String filePath = itemData.getCoverPath();
        if (!TextUtils.isEmpty(filePath)) {
            ImageView imageView = getViewFromHolder(R.id.iv_icon);
            mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(itemData.getCoverPath()))
                    .priority(Priority.HIGH)
                    .override(mIconSize, mIconSize)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
