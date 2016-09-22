package com.xiaosw.gallery.bean;

/**
 * @ClassName : {@link MediaFolder}
 * @Description : 相册夹信息
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 22:22:07
 */
public class MediaFolder {

    /** 文件夹内容数量 */
    private int totalSize;
    /** 文件夹名称 */
    private String folderName;
    /** 文件夹封面 */
    private String coverPath;
    /** 文件夹封面 */
    private String bucketId;
    /** 是否属于其它（Camera,Video,Download不属于其它） */
    private boolean isOther;
    /** 用于排序 */
    private int folderPosition;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setOther(boolean other) {
        isOther = other;
    }

    public int getFolderPosition() {
        return folderPosition;
    }

    public void setFolderPosition(int folderPosition) {
        this.folderPosition = folderPosition;
    }
}
