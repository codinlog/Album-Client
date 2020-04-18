package com.codinlog.album.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.dao.kotlin.CategoryDAO;
import com.codinlog.album.dao.kotlin.DiaryDAO;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;
import com.codinlog.album.entity.kotlin.CategoryEntity;
import com.codinlog.album.entity.kotlin.DiaryEntity;


@Database(entities = {AlbumEntity.class, AlbumItemEntity.class, DiaryEntity.class, CategoryEntity.class}, version = 3, exportSchema = false)
public abstract class AlbumDatabase extends RoomDatabase {
    static Migration migration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table albumTB add column displayPhotoPath text");
        }
    };

    public static AlbumDatabase getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    public abstract AlbumDAO getAlbumDAO();

    public abstract AlbumItemDAO getAlbumItemDAO();

    public abstract DiaryDAO getDiaryDAO();

    public abstract CategoryDAO getCategoryDAO();

    private enum SingletonEnum {
        INSTANCE;
        AlbumDatabase albumDatabase;

        SingletonEnum() {
            albumDatabase = Room.databaseBuilder(AlbumApplication.context, AlbumDatabase.class, AlbumApplication.context.getString(R.string.albumDB))
                    .addMigrations(migration_1_2)
                    .build();
        }

        public AlbumDatabase getInstance() {
            return albumDatabase;
        }
    }
}
