package com.xiaosw.gallery.util;

import android.content.Context;

import com.xiaosw.gallery.GalleryApplication;
import com.xiaosw.gallery.R;
import com.xiaosw.gallery.bean.MediaFolder;
import com.xiaosw.gallery.bean.MediaItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * @ClassName : {@link GlobalDataStorage}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 14:14:37
 */
public enum  GlobalDataStorage {
	INSTANCE;

	///////////////////////////////////////////////////////////////////////////
	// 文件相关
	///////////////////////////////////////////////////////////////////////////
	/** 数据库查询的原始数据 */
	private ArrayList<MediaItem> mSrcMediaItems = new ArrayList<>();
	/** 处理过后的数据库数据 size >= {@link #mSrcMediaItems} */
	private ArrayList<MediaItem> mHandleMediaItems = new ArrayList<>();
	/** 数据监听 */
	private HashSet<MediaDataChangeObserver> mMediaDataObserver = new HashSet<MediaDataChangeObserver>();

	public ArrayList<MediaItem> getSrcMediaItems() {
		return mSrcMediaItems;
	}

	public ArrayList<MediaItem> getHandleMediaItems() {
		return mHandleMediaItems;
	}

	public synchronized void replaceAllMediaItems(ArrayList<MediaItem> newMediaItems) {
		replaceAllMediaItems(newMediaItems, 1);
	}

	public synchronized void replaceAllMediaItems(ArrayList<MediaItem> newMediaItems, int numColumns) {
		if (null == newMediaItems) {
			throw new NullPointerException("newMediaItems must not null!!!");
		}
		mSrcMediaItems.clear();
		mSrcMediaItems.addAll(newMediaItems);
		handleBreakLine(mSrcMediaItems, Math.max(1, numColumns));
	}

	public synchronized void reHandleMediaItems(int numColumns) {
		handleBreakLine(mSrcMediaItems, Math.max(1, numColumns));
	}

	public int getRealPositionByHandlePosition(int handlePosition) {
		return getRealPositionByMediaItem(mHandleMediaItems.get(handlePosition));
	}

	public int getRealPositionByMediaItem(MediaItem mediaItem) {
		int position = 0;
		for (MediaItem item : mSrcMediaItems) {
			if (mediaItem.getId() == item.getId()) {
				return position;
			}
			position ++;
		}
		return -1;
	}

	/**
	 * 处理换行
	 * @param mediaItems 待处理数据
	 * @param numColumns 列数
	 */
	private void handleBreakLine(ArrayList<MediaItem> mediaItems, int numColumns) {
		MediaItem lastMediaItem = null;
		if (mediaItems == null) {
			mediaItems = new ArrayList<>();
		}
		mHandleMediaItems.clear();
		int titleLineCount = 0;
		mHandleMediaItems.add(getTitleLine(mediaItems.get(0), titleLineCount, true)); // 标题行
		titleLineCount++;
		for (MediaItem mediaItem : mediaItems) {
			if (null != lastMediaItem && !lastMediaItem.getCreateDate().equals(mediaItem.getCreateDate())) { // 不同天
				int nullItem = numColumns - (mHandleMediaItems.size() - titleLineCount) % numColumns;
				if (nullItem != numColumns) { // 不满行，添加空格
					for (int j = 0; j < nullItem; j++) {
						MediaItem temp = new MediaItem();
						mHandleMediaItems.add(temp);
					}
				}
				mHandleMediaItems.add(getTitleLine(mediaItem, titleLineCount, !lastMediaItem.getYear().equals(mediaItem.getYear())));
				titleLineCount++;
			}
			mHandleMediaItems.add(mediaItem);
			lastMediaItem = mediaItem;
		}
		lastMediaItem = null;
		notifyMediaDataChange();
	}

	/**
	 * 添加空行在，显示地址信息
	 * @param mediaItem
	 */
	private MediaItem getTitleLine(MediaItem mediaItem, int titlePosition, boolean needDrawYear) {
		MediaItem titleLine = new MediaItem();
		titleLine.setTitleLine(true);
		titleLine.setNeedDrawYear(needDrawYear);
		titleLine.setMonthAndDay(mediaItem.getMonthAndDay());
		titleLine.setYear(mediaItem.getYear());
		titleLine.setTitlePosition(titlePosition);
		return titleLine;
	}

	public void registerMediaDataObserver(MediaDataChangeObserver observer) {
		mMediaDataObserver.add(observer);
	}

	public void unregisterMediaDataObserver(MediaDataChangeObserver observer) {
		mMediaDataObserver.remove(observer);
	}

	public void notifyMediaDataChange() {
		for (MediaDataChangeObserver observre: mMediaDataObserver) {
			observre.notifyChange(mSrcMediaItems, mHandleMediaItems);
		}
	}

	public interface MediaDataChangeObserver<T> {
		public void notifyChange(ArrayList<T> srcData, ArrayList<T> handleData);
	}


	///////////////////////////////////////////////////////////////////////////
	// 目录信息
	///////////////////////////////////////////////////////////////////////////
	private ArrayList<MediaFolder> mMediaFolders = new ArrayList<>();
	public void replaceAllMediaFolders(ArrayList<MediaFolder> mediaFolders) {
		if (null == mediaFolders) {
			throw new NullPointerException("newMediaItems must not null!!!");
		}
		mMediaFolders.clear();
		mMediaFolders.addAll(mediaFolders);
		Collections.sort(mMediaFolders, new Comparator<MediaFolder>() {
			@Override
			public int compare(MediaFolder lhs, MediaFolder rhs) {
				return lhs.getFolderPosition() < rhs.getFolderPosition() ? -1 : (lhs == rhs ? 0 : 1);
			}
		});
		mMediaFolders.add(getOtherMediaFolder());
	}

	public ArrayList<MediaFolder> getMediaFolders() {
		return mMediaFolders;
	}

	private MediaFolder mOtherFolder;
	private MediaFolder getOtherMediaFolder() {
		if (null == mOtherFolder) {
			mOtherFolder = new MediaFolder();
			mOtherFolder.setOther(false); // 需要在主界面现实，所以为false
			mOtherFolder.setFolderPosition(Integer.MAX_VALUE);
			mOtherFolder.setFolderName(GalleryApplication.mApp.getString(R.string.str_folder_name_other));
		}
		return mOtherFolder;
	}

}
