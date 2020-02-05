package com.codinlog.album.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.util.kotlin.AlbumDeleteDBUtil;
import com.codinlog.album.util.kotlin.AlbumInsertDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryByNameDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryDBUtil;
import com.codinlog.album.util.kotlin.AlbumUpdateDBUtil;

import java.util.List;

public class AlbumViewModel extends ViewModel {

    LiveData<List<AlbumEntity>> albumEntityLiveData;

    public MainViewModel mainViewModel;
    public AlbumDAO albumDAO;
    public AlbumItemDAO albumItemDAO;

    public LiveData<List<AlbumEntity>> getAlbumEntityLiveData() {
        return albumEntityLiveData;
    }

    public void setAlbumEntityLiveData() {
        this.albumEntityLiveData = albumDAO == null ? new MutableLiveData<>() : albumDAO.queryAllAlbum();
    }


    public void insertAlbum(AlbumEntity... albumEntities){
        new AlbumInsertDBUtil(albumDAO).execute(albumEntities);
    }

    public void deleteAlbum(AlbumEntity... albumEntities){
        new AlbumDeleteDBUtil(albumDAO).execute(albumEntities);
    }

    public void queryAlbum(){
        new AlbumQueryDBUtil(albumDAO).execute();
    }

    public void queryByNameAlbum(String... strings){
        new AlbumQueryByNameDBUtil(albumDAO).execute(strings);
    }

    public void updateAlbum(AlbumEntity... albumEntities){
        new AlbumUpdateDBUtil(albumDAO).execute(albumEntities);
    }
}
