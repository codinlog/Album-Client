package com.codinlog.album.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import com.codinlog.album.entity.AlbumItemEntity;

import java.util.List;

@Dao
public interface AlbumItemDAO {
    @Delete
    void deleteAlbumItem(AlbumItemEntity... albumItemEntities);

    @Query("select * from albumItemTB where belongToId == :albumId")
    LiveData<List<AlbumItemEntity>> queryAllAlbumItem(int albumId);
}
