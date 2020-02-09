package com.codinlog.album.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;
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
    LiveData<List<AlbumEntity>> albumExistLiveData;
    private String albumName;
    private PhotoBean diaplayPhotoBean;

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
        setAlbumName(strings[0]);
        setAlbumExistLiveData(strings);
        new AlbumQueryByNameDBUtil(albumDAO).execute(strings);
    }

    public void updateAlbum(AlbumEntity... albumEntities){
        new AlbumUpdateDBUtil(albumDAO).execute(albumEntities);
    }

    public LiveData<List<AlbumEntity>> getAlbumExistLiveData() {
        if(albumExistLiveData == null)
            setAlbumExistLiveData("");
        return albumExistLiveData;
    }

    public void setAlbumExistLiveData(String... strings) {
        if(albumExistLiveData == null){
            albumExistLiveData = albumDAO.queryByAlbumName(strings);
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public PhotoBean getDiaplayPhotoBean() {
        return diaplayPhotoBean;
    }

    public void setDiaplayPhotoBean(PhotoBean diaplayPhotoBean) {
        this.diaplayPhotoBean = diaplayPhotoBean;
    }
}
