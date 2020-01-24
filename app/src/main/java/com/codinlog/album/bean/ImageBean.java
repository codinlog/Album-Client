package com.codinlog.album.bean;

import androidx.annotation.NonNull;

public class ImageBean implements Comparable{

//    private String thumbnail;               // 缩略图

//    private int imageWidth;                 // 图片宽度
//    private int imageHeight;                // 图片高度

    //    private long modified;                  // 图片修改时间
//    private double latitude;                // 纬度
//    private double longitude;               // 经度
//    private double altitude;                // 海拔
    private String path;                    // 路径
    private double size;                       // 图片大小
    private int imageId;                    // 图片ID
    private long tokenDate;                  // 拍摄时间
    private boolean isSelected = false;
    private boolean isDelete = false;

    @NonNull
    @Override
    public String toString() {
        return "isSelect:" + isSelected;
    }

    public static ImageBean newInstance() {
        return new ImageBean();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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

    @Override
    public int compareTo(Object o) {
        ImageBean imageBean = (ImageBean) o;
        if(this.tokenDate > imageBean.getTokenDate())
            return -1;
        return 1;
    }
}
