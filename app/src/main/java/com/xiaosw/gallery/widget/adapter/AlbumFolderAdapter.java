package com.xiaosw.gallery.widget.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.config.AppConfig;
import com.xiaosw.gallery.util.GlobalDataStorage;
import com.xiaosw.gallery.viewer.SupportGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName {@link AlbumFolderAdapter}
 * @Description
 * @Date 2016-09-10 16:50.
 * @Author xiaoshiwang.
 */
public class AlbumFolderAdapter extends AbsBaseAdapter<MediaFolder> {
    private RequestManager mRequestManager;
    private SupportGridView mGridView;
    private int mHorizontalSpacing;
    public AlbumFolderAdapter(Context context, SupportGridView gridView) {
        super(context, GlobalDataStorage.INSTANCE.getMediaFolders(), R.layout.item_album_folder);
        this.mRequestManager = Glide.with(context);
        this.mGridView = gridView;
        Resources res = context.getResources();
        mHorizontalSpacing = (int) res.getDimension(R.dimen.horizontal_spacing_album);
    }

    @Override
    protected void bindData(View convertView, int position, MediaFolder itemData) {
        ImageView imageView = getViewFromHolder(R.id.iv_album_photo_cover);
        String filePath = itemData.getCoverPath();
        int itemSize = mGridView.getRealScreenWidth() / mGridView.getRealNumColumns() - mHorizontalSpacing;
        if (!TextUtils.isEmpty(filePath)) {
            mRequestManager.load(AppConfig.GLIDE_NATIVE_PREFIX.concat(itemData.getCoverPath()))
                    .priority(Priority.HIGH)
                    .override(itemSize, itemSize)
                    .centerCrop()
                    .into(imageView);
        } else {
            mRequestManager.load(R.mipmap.ic_folder_cover_other)
                    .priority(Priority.HIGH)
                    .override(itemSize, itemSize)
                    .centerCrop()
                    .into(imageView);
        }

        bindText(R.id.tv_album_folder_name, itemData.getFolderName() + " ");
        bindText(R.id.tv_album_folder_total_size, " (" + itemData.getTotalSize() + ")");
    }
}
