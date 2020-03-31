package com.codinlog.album.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import java.text.SimpleDateFormat;

public class PhotoBean implements Comparable, Parcelable {
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
    @Ignore
    private int rotation = 0;

    protected PhotoBean(Parcel in) {
        photoPath = in.readString();
        photoSize = in.readDouble();
        photoId = in.readInt();
        groupId = in.readString();
        tokenDate = in.readLong();
        isSelected = in.readByte() != 0;
        isDelete = in.readByte() != 0;
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel in) {
            return new PhotoBean(in);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };

    public PhotoBean() {}

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

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof PhotoBean){
            PhotoBean photoBean = (PhotoBean) obj;
            return this.photoPath.equals(photoBean.getPhotoPath());
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photoPath);
        parcel.writeDouble(photoSize);
        parcel.writeInt(photoId);
        parcel.writeString(groupId);
        parcel.writeLong(tokenDate);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeByte((byte) (isDelete ? 1 : 0));
        parcel.writeInt(width);
        parcel.writeInt(height);
    }
}
