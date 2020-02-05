package com.codinlog.album.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;


@Database(entities = {AlbumEntity.class, AlbumItemEntity.class},version = 1,exportSchema = false)
public abstract class AlbumDatabase extends RoomDatabase {
    public abstract AlbumDAO getAlbumDAO();
    public abstract AlbumItemDAO getAlbumItemDAO();

    private enum SingletonEnum{
        INSTANCE;
        AlbumDatabase albumDatabase;
        SingletonEnum(){
            albumDatabase = Room.databaseBuilder(AlbumApplication.mContext, AlbumDatabase.class,AlbumApplication.mContext.getString(R.string.albumDB)).build();;
        }
        public AlbumDatabase getInstance(){
            return albumDatabase;
        }
    }

    public static AlbumDatabase getInstance(){
        return SingletonEnum.INSTANCE.getInstance();
    }
}
