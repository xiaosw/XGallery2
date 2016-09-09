package com.xiaosw.gallery.bean;

/**
 * @ClassName : {@link MediaItem}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 14:14:35
 */
public class MediaItem extends BaseRecyclerBean {

	private int id = -1;
	private String data; // /storage/emulated/0/DCIM/Camera/IMG_20160909_142529408.jpg
	private String displayName; // IMG_20160909_142529408.jpg
	private String mimeType; // image/jpeg;video/mp4
	private String title; // IMG_20160909_142529408
	private long dateTaken; // 时间
	private String bucketId; // 文件夹hash
	private String bucketDisplayName;// 文件夹

	/** 是否绘制年（如何跨年则为true） */
 	private boolean needDrawYear;
	/** 文件创建日期 yyyy/MM/dd */
	private String createDate;
	/** 文件创建年份 yyyy */
	private String year;
	/** 文件创建月/日 MM/dd */
	private String monthAndDay;
	/** 记录绘制圆点颜色 */
	private int titlePosition;
	/** 是否选中 */
	private SelectState state = SelectState.DISABLE;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(long dateTaken) {
		this.dateTaken = dateTaken;
	}

	public String getBucketId() {
		return bucketId;
	}

	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
	}

	public String getBucketDisplayName() {
		return bucketDisplayName;
	}

	public void setBucketDisplayName(String bucketDisplayName) {
		this.bucketDisplayName = bucketDisplayName;
	}

	public boolean isNeedDrawYear() {
		return needDrawYear;
	}

	public void setNeedDrawYear(boolean needDrawYear) {
		this.needDrawYear = needDrawYear;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonthAndDay() {
		return monthAndDay;
	}

	public void setMonthAndDay(String monthAndDay) {
		this.monthAndDay = monthAndDay;
	}

	public int getTitlePosition() {
		return titlePosition;
	}

	public void setTitlePosition(int titlePosition) {
		this.titlePosition = titlePosition;
	}

	public SelectState getState() {
		return state;
	}

	public void setState(SelectState state) {
		this.state = state;
	}

	public enum SelectState {
		DISABLE, // 禁用状态，不可见
		UNCHECKED, // 未选中状态，可见
		CHECKED; // 选中，可见
	}
}
