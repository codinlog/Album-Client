package com.codinlog.album.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.text.SimpleDateFormat;

public class PhotoBean implements Comparable{
    @ColumnInfo(name = "photoPath")
    private String photoPath;                    // 路径
    @ColumnInfo(name = "photoSize")
    private double photoSize;                       // 图片大小
    @ColumnInfo(name = "photoId")
    private int photoId;                    // 图片ID
    @Ignore
    private String groupId;
    @ColumnInfo(name = "tokenDate")
    private long tokenDate;                  // 拍摄时间
    @Ignore
    private boolean isSelected = false;
    @Ignore
    private boolean isDelete = false;
    @ColumnInfo(name = "photoWidth")
    private int width;                 // 图片宽度
    @ColumnInfo(name = "photoHeight")
    private int height;                // 图片高度

    @NonNull
    @Override
    public String toString() {
        return "isSelected:" + isSelected + ";Path:" + photoPath + "; Id :" + photoId + ";tokenDate:" +  new SimpleDateFormat("yyyy-MM-dd").format(tokenDate);
    }

    public static PhotoBean newInstance() {
        return new PhotoBean();
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public double getPhotoSize() {
        return photoSize;
    }

    public void setPhotoSize(double photoSize) {
        this.photoSize = photoSize;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(long tokenDate) {
        this.tokenDate = tokenDate;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int compareTo(Object o) {
        PhotoBean photoBean = (PhotoBean) o;
        if(this.tokenDate > photoBean.getTokenDate())
            return -1;
        return 1;
    }

    @Override
    public int hashCode() {
        if(photoPath != null)
            return photoPath.hashCode();
        return super.hashCode();
    }
}
