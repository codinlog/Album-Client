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
    public abstract List<Long> insertAlbumItem(AlbumItemEntity... albumItemEntities);

    @Delete
    public abstract void deleteAlbumItem(AlbumItemEntity... albumItemEntities);

    @Query("select * from albumItemTB where belongToId == :albumId order by tokenDate desc")
    public abstract List<AlbumItemEntity> queryAllAlbumItem(int albumId);

    @Query("select * from albumItemTB where belongToId == :albumId order by tokenDate desc")
    public abstract LiveData<List<AlbumItemEntity>> queryAllAlbum(int albumId);
}
