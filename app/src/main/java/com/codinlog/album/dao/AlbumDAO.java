package com.codinlog.album.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.codinlog.album.entity.AlbumEntity;

import java.util.List;

@Dao
public interface AlbumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(AlbumEntity... albumEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateAlbum(AlbumEntity... albumEntities);

    @Delete
    int deleteAlbum(AlbumEntity... albumEntities);

    @Query("select * from albumTB order by albumId desc")
    LiveData<List<AlbumEntity>> queryAllAlbum();

    @Query("select * from albumTB where albumName == :albumName")
    LiveData<List<AlbumEntity>> queryByAlbumName(String ... albumName);
}
