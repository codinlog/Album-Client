package com.codinlog.album.entity;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.util.kotlin.AlbumTypeConverter;

import java.util.Date;

@TypeConverters(AlbumTypeConverter.class)
@Entity(tableName = "albumTB", indices = {@Index(value = {"albumName", "albumId"}, unique = true)})
public class AlbumEntity implements Parcelable {
    public static final Creator<AlbumEntity> CREATOR = new Creator<AlbumEntity>() {
        @Override
        public AlbumEntity createFromParcel(Parcel in) {
            return new AlbumEntity(in);
        }

        @Override
        public AlbumEntity[] newArray(int size) {
            return new AlbumEntity[size];
        }
    };
    @PrimaryKey
    @ColumnInfo(name = "albumId")
    private int albumId;
    @NonNull
    @ColumnInfo(name = "albumName")
    private String albumName;
    @ColumnInfo(name = "createDate")
    private Date date;
    @Ignore
    private boolean isSelect = false;
    @Embedded
    private PhotoBean photoBean;

    public AlbumEntity() {
    }

    protected AlbumEntity(Parcel in) {
        albumId = in.readInt();
        date = new Date(in.readLong());
        albumName = in.readString();
        photoBean = (PhotoBean) in.readValue(Thread.currentThread().getContextClassLoader());
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
        setAlbumId(this.hashCode());
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public PhotoBean getPhotoBean() {
        return photoBean;
    }

    public void setPhotoBean(PhotoBean photoBean) {
        this.photoBean = photoBean;
    }

    @Override
    public int hashCode() {
        return albumName.hashCode();
    }

    @Override
    public String toString() {
        return "AlbumEntity{" +
                "albumId=" + albumId +
                ", date=" + date +
                ", albumName='" + albumName + '\'' +
                ", photoBean=" + photoBean +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(albumId);
        dest.writeLong(date.getTime());
        dest.writeString(albumName);
        dest.writeValue(photoBean);
    }
}
