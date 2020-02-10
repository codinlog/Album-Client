package com.codinlog.album.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.util.TypeConverterUtitl;

import java.util.Date;
@TypeConverters(TypeConverterUtitl.class)
@Entity(tableName = "albumTB",indices = {@Index(value = {"albumName","albumId"},unique = true)})
public class AlbumEntity {
    @PrimaryKey
    @ColumnInfo(name = "albumId")
    private int albumId;

    @ColumnInfo(name = "createDate")
    private Date date;

    @NonNull
    @ColumnInfo(name = "albumName")
    private String albumName;

    @Embedded
    private PhotoBean photoBean;

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

    public PhotoBean getPhotoBean() {
        return photoBean;
    }

    public void setPhotoBean(PhotoBean photoBean) {
        this.photoBean = photoBean;
    }

    @NonNull
    @Override
    public String toString() {
        return "albumName:" + albumName + ",albumId:" + albumId + "\n";
    }

    @Override
    public int hashCode() {
        return albumName.hashCode();
    }
}
