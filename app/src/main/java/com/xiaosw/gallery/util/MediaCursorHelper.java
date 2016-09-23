package com.xiaosw.gallery.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.xiaosw.gallery.GalleryApplication;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.bean.MediaItem;
import com.xiaosw.gallery.config.AppConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @ClassName : {@link MediaCursorHelper}
 * @Description : 处理与媒体库相关信息
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 14:14:28
 */
public class MediaCursorHelper {

    public static final int INDEX_ID = 0;
    public static final int INDEX_DATA = 1;
    public static final int INDEX_DISPLAY_NAME = 2;
    public static final int INDEX_MIME_TYPE = 3;
    public static final int INDEX_TITLE = 4;
    public static final int INDEX_DATE_TAKEN = 5;
    public static final int INDEX_BUCKET_ID = 6;
    public static final int INDEX_BUCKET_DISPLAY_NAME = 7;

    /**
     * 图片列数
     */
    public static final String[] IMAGE_PROJECTION = new String[] {
            MediaStore.Images.ImageColumns._ID,                    // 0
            MediaStore.Images.ImageColumns.DATA,                    // 1
            MediaStore.Images.ImageColumns.DISPLAY_NAME,            // 2
            MediaStore.Images.ImageColumns.MIME_TYPE,               // 3
            MediaStore.Images.ImageColumns.TITLE,                   // 4
            MediaStore.Images.ImageColumns.DATE_TAKEN,              // 5
            MediaStore.Images.ImageColumns.BUCKET_ID,               // 6
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME      // 7
    };

    /** 查询图片条件 */
    public static final String FILTER_ALL_PHOTO_WHERE = null;
    /** 查询图片参数 */
    public static final String[] FILTER_ALL_PHOTO_ARGS = null;
    /** 降序(desc,默认aes) */
    public static final String IMAGE_ORDER_CLAUSE_DESC = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC, "
            + MediaStore.Images.ImageColumns._ID + " DESC";


    /**
     * 解析图片数据
     * @param cursor
     */
    public static void parseImageCursor(final Cursor cursor, final ContentResolver contentResolver) {
        final ArrayList<MediaItem> temp = new ArrayList<MediaItem>();
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                temp.add(getMediaItemByCursor(cursor));
            }
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                Cursor videoCursor = contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    FILTER_ALL_PHOTO_WHERE,
                    FILTER_ALL_PHOTO_ARGS,
                    null);
                if (null != videoCursor) {
                    videoCursor.moveToPosition(-1);
                    while (videoCursor.moveToNext()) {
                        temp.add(getMediaItemByCursor(videoCursor));
                    }
                }
                Collections.sort(temp, new Comparator<MediaItem>() {
                    @Override
                    public int compare(MediaItem lhs, MediaItem rhs) {
                        return lhs.getDateTaken() > rhs.getDateTaken() ? -1 : (lhs.getDateTaken() == rhs.getDateTaken() ? 0 : 1);
                    }
                });
                GalleryApplication.mApp.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GlobalDataStorage.INSTANCE.replaceAllMediaItems(temp);
                    }
                });
            }
        }.start();
    }

    private static MediaItem getMediaItemByCursor(Cursor cursor) {
        MediaItem mediaItem = new MediaItem();
        mediaItem.setId(cursor.getInt(INDEX_ID));
        mediaItem.setData(cursor.getString(INDEX_DATA));
        mediaItem.setDisplayName(cursor.getString(INDEX_DISPLAY_NAME));
        mediaItem.setMimeType(cursor.getString(INDEX_MIME_TYPE));
        mediaItem.setTitle(cursor.getString(INDEX_TITLE));
        long dateTaken = cursor.getLong(INDEX_DATE_TAKEN);
        mediaItem.setDateTaken(dateTaken);
        mediaItem.setCreateDate(CommonUtil.getYearAndMonthAndDay(dateTaken));
        mediaItem.setYear(CommonUtil.getYear(dateTaken));
        mediaItem.setMonthAndDay(CommonUtil.getMonthAndDay(dateTaken));
        mediaItem.setBucketId(cursor.getString(INDEX_BUCKET_ID));
        mediaItem.setBucketDisplayName(cursor.getString(INDEX_BUCKET_DISPLAY_NAME));
        return mediaItem;
    }

    /**
     * 解析图片数据
     * @param cursor
     */
    public static void parseFolderCursor(Context context, Cursor cursor) {
        ArrayList<MediaFolder> temp = new ArrayList<MediaFolder>();
        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                temp.add(getMediaFolderByCursor(context, cursor));
            }
        }
        GlobalDataStorage.INSTANCE.replaceAllMediaFolders(temp);
    }

    private static MediaFolder getMediaFolderByCursor(Context context, Cursor cursor) {
        MediaFolder mediaFolder = new MediaFolder();
        mediaFolder.setCoverPath(cursor.getString(0));
        String folderName = cursor.getString(1);
        mediaFolder.setFolderName(getNativeFolderName(context, folderName));
        mediaFolder.setBucketId(cursor.getString(2));
        mediaFolder.setTotalSize(cursor.getInt(3));
        boolean isMainFolder = AppConfig.MAIN_MEDIA_FOLDER.containsKey(folderName);
        if (isMainFolder) {
            mediaFolder.setFolderPosition(AppConfig.MAIN_MEDIA_FOLDER.get(folderName));
        } else {
            mediaFolder.setFolderPosition(Integer.parseInt(mediaFolder.getBucketId()));
        }
        mediaFolder.setOther(!isMainFolder);
        return mediaFolder;
    }

    private static String getNativeFolderName(Context context, String folderName) {
        if ("Camera".equals(folderName)) {
            return context.getString(R.string.str_folder_name_camera);
        } else if ("Video".equalsIgnoreCase(folderName)) {
            return context.getString(R.string.str_folder_name_video);
        } else if ("Screenshots".equalsIgnoreCase(folderName)) {
            return context.getString(R.string.str_folder_name_screenshots);
        } else if ("Download".equalsIgnoreCase(folderName)) {
            return context.getString(R.string.str_folder_name_download);
        }

        return folderName;
    }

}
