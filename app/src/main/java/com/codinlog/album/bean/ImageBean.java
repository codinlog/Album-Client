package com.codinlog.album.bean;

import androidx.annotation.NonNull;

public class ImageBean {
    private String path;                    // 路径
//    private String thumbnail;               // 缩略图
//    private int imageId;                    // 图片ID
//    private int imageWidth;                 // 图片宽度
//    private int imageHeight;                // 图片高度
    private long size;                       // 图片大小
//    private int tokenDate;                  // 拍摄时间
//    private long modified;                  // 图片修改时间
//    private double latitude;                // 纬度
//    private double longitude;               // 经度
//    private double altitude;                // 海拔
    private boolean isSelected = false;

    public ImageBean() {
    }

    @NonNull
    @Override
    public String toString() {
        return "isSelect:"+ isSelected;
    }

    public static ImageBean newInstance(){
        return new ImageBean();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public String getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//
//    public int getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }
//
//    public int getImageWidth() {
//        return imageWidth;
//    }
//
//    public void setImageWidth(int imageWidth) {
//        this.imageWidth = imageWidth;
//    }
//
//    public int getImageHeight() {
//        return imageHeight;
//    }
//
//    public void setImageHeight(int imageHeight) {
//        this.imageHeight = imageHeight;
//    }
//
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
//
//    public int getTokenDate() {
//        return tokenDate;
//    }
//
//    public void setTokenDate(int tokenDate) {
//        this.tokenDate = tokenDate;
//    }
//
//    public long getModified() {
//        return modified;
//    }
//
//    public void setModified(long modified) {
//        this.modified = modified;
//    }
//
//    public double getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    public double getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
//
//    public double getAltitude() {
//        return altitude;
//    }
//
//    public void setAltitude(double altitude) {
//        this.altitude = altitude;
//    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
