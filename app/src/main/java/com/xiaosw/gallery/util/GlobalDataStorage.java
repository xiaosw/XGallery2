package com.xiaosw.gallery.util;

import com.xiaosw.gallery.bean.MediaItem;

import java.util.ArrayList;
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
		if (mediaItems == null || mediaItems.size() == 0) {
			return;
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

	public interface MediaDataChangeObserver {
		public void notifyChange(ArrayList<MediaItem> srcData, ArrayList<MediaItem> handleData);
	}

}
