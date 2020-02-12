package com.codinlog.album.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.codinlog.album.entity.AlbumItemEntity;

import java.util.List;

@Dao
public abstract class AlbumItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract List<Long> insertAlbumItem(AlbumItemEntity... albumItemEntities);

    @Delete
    abstract void deleteAlbumItem(AlbumItemEntity... albumItemEntities);

    @Query("select * from albumItemTB where belongToId == :albumId")
    abstract LiveData<List<AlbumItemEntity>> queryAllAlbumItem(int albumId);
}
