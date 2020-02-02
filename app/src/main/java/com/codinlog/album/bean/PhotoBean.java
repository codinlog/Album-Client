package com.codinlog.album.bean;

import androidx.annotation.NonNull;

public class PhotoBean implements Comparable{

//    private String thumbnail;               // 缩略图
//    private long modified;                  // 图片修改时间
    //    private double latitude;                // 纬度
//    private double longitude;               // 经度
//    private double altitude;                // 海拔
    private String path;                    // 路径
    private double size;                       // 图片大小
    private int photoId;                    // 图片ID
    private String groupId;
    private long tokenDate;                  // 拍摄时间
    private boolean isSelected = false;
    private boolean isDelete = false;
    private int width;                 // 图片宽度
    private int height;                // 图片高度

    @NonNull
    @Override
    public String toString() {
        return "isSelected:" + isSelected + ";Path:" + path;
    }




    public static PhotoBean newInstance() {
        return new PhotoBean();
    }

    public int getWidth() {
        return width;
    }

    public PhotoBean setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public PhotoBean setHeight(int height) {
        this.height = height;
        return this;
    }

    public String getPath() {
        return path;
    }

    public PhotoBean setPath(String path) {
        this.path = path;
        return this;
    }

    public double getSize() {
        return size;
    }

    public PhotoBean setSize(double size) {
        this.size = size;
        return this;
    }

    public int getPhotoId() {
        return photoId;
    }

    public PhotoBean setPhotoId(int photoId) {
        this.photoId = photoId;
        return this;
    }

    public long getTokenDate() {
        return tokenDate;
    }

    public PhotoBean setTokenDate(long tokenDate) {
        this.tokenDate = tokenDate;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getGroupId() {
        return groupId;
    }

    public PhotoBean setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        PhotoBean photoBean = (PhotoBean) o;
        if(this.tokenDate > photoBean.getTokenDate())
            return -1;
        return 1;
    }
}
