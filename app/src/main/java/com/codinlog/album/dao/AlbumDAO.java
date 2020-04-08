package com.codinlog.album.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;

import java.util.List;

@Dao
public abstract class AlbumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAlbum(AlbumEntity... albumEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract int updateAlbum(AlbumEntity... albumEntities);

    @Delete
    public abstract int deleteAlbum(AlbumEntity... albumEntities);

    @Query("update albumTB set albumId=:newId,albumName=:albumName where albumId=:oldId")
    public abstract int renameAlbum(int oldId, int newId, String albumName);

    @Query("select * from albumTB order by createDate desc")
    public abstract LiveData<List<AlbumEntity>> queryAllAlbum();

    @Query("select * from albumTB order by createDate desc")
    public abstract List<AlbumEntity> queryAllAlbumWithList();

    @Query("select * from albumTB where albumId= :id")
    public abstract AlbumEntity queryByAlbumId(int id);

    @Transaction
    public List<Long> insertToAlbumWithPhotoBeans(AlbumItemDAO albumItemDAO, AlbumEntity albumEntity, AlbumItemEntity... albumItemEntities) {
        insertAlbum(albumEntity);
        return albumItemDAO.insertAlbumItem(albumItemEntities);
    }

    @Transaction
    public List<Long> insertToExistAlbumWithPhotoBeans(AlbumItemDAO albumItemDAO, AlbumEntity albumEntity, AlbumItemEntity... albumItemEntities) {
        updateAlbum(albumEntity);
        return albumItemDAO.insertAlbumItem(albumItemEntities);
    }

    @Transaction
    public boolean mergeAlbum(AlbumEntity albumEntity, AlbumItemDAO albumItemDAO, boolean keepOldAlbum, boolean createNew, List<AlbumEntity> albumEntities) {
        if (createNew)
            insertAlbum(albumEntity);
        albumEntities.forEach(it -> {
            List<AlbumItemEntity> albumItemEntities = albumItemDAO.queryAllAlbumItem(it.getAlbumId());
            albumItemDAO.insertAlbumItem(albumItemEntities.stream().peek(i -> {
                i.setBelongToId(albumEntity.getAlbumId());
                i.setUuid((i.getPhotoBean().getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            }).toArray(AlbumItemEntity[]::new));
        });
        AlbumItemEntity albumItemEntity = albumItemDAO.queryTopOneAlbumItem(albumEntity.getAlbumId());
        if (albumItemEntity != null) {
            albumEntity.setPhotoBean(albumItemEntity.getPhotoBean());
            updateAlbum(albumEntity);
        }
        if (!keepOldAlbum)
            deleteAlbum(albumEntities.stream().toArray(AlbumEntity[]::new));
        return true;
    }
}
