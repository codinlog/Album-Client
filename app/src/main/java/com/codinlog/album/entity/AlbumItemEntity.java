package com.codinlog.album.entity;

import androidx.room.*;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "albumItemTB",foreignKeys = @ForeignKey(entity = AlbumEntity.class,
        parentColumns = "albumId",childColumns = "belongToId",onDelete = CASCADE),
indices = {@Index(value = "belongToId")})
public class AlbumItemEntity {
    @ColumnInfo(name = "Id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "belongToId")
    private int belongToId;

    @ColumnInfo(name = "photoId")
    private int photoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBelongToId() {
        return belongToId;
    }

    public void setBelongToId(int belongToId) {
        this.belongToId = belongToId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
