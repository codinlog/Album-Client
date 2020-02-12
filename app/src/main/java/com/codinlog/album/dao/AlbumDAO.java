package com.codinlog.album.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;

import java.util.List;
import java.util.stream.Collectors;

@Dao
public abstract class AlbumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAlbum(AlbumEntity... albumEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract int updateAlbum(AlbumEntity... albumEntities);

    @Delete
    public abstract int deleteAlbum(AlbumEntity... albumEntities);

    @Query("select * from albumTB order by createDate desc")
    public abstract LiveData<List<AlbumEntity>> queryAllAlbum();

    @Query("select * from albumTB where albumId= :id")
    public abstract AlbumEntity queryByAlbumId(int id);

    @Transaction
    public List<Long> insertToAlbumWithPhotoBeans(AlbumItemDAO albumItemDAO, AlbumEntity albumEntity, AlbumItemEntity... albumItemEntities) {
        insertAlbum(albumEntity);
        return albumItemDAO.insertAlbumItem(albumItemEntities);
    }
}
