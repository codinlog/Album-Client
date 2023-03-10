package com.codinlog.album.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.codinlog.album.bean.PhotoBean;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "albumItemTB", foreignKeys = @ForeignKey(entity = AlbumEntity.class,
        parentColumns = "albumId", childColumns = "belongToId", onDelete = CASCADE, onUpdate = CASCADE), indices = {@Index("belongToId")})
public class AlbumItemEntity {
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private int uuid;

    @ColumnInfo(name = "belongToId")
    private int belongToId;

    @Embedded
    private PhotoBean photoBean;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public int getBelongToId() {
        return belongToId;
    }

    public void setBelongToId(int belongToId) {
        this.belongToId = belongToId;
    }

    public PhotoBean getPhotoBean() {
        return photoBean;
    }

    public void setPhotoBean(PhotoBean photoBean) {
        this.photoBean = photoBean;
    }
}
