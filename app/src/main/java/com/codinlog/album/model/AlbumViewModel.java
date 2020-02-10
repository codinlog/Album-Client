package com.codinlog.album.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.util.kotlin.AlbumDeleteDBUtil;
import com.codinlog.album.util.kotlin.AlbumInsertDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryByAlbumIdDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryDBUtil;
import com.codinlog.album.util.kotlin.AlbumUpdateDBUtil;

import java.util.List;

public class AlbumViewModel extends ViewModel {

    private LiveData<List<AlbumEntity>> albumEntityLiveData;
    private String albumName;
    private PhotoBean displayPhotoBean;

    public MainViewModel mainViewModel;
    public AlbumDAO albumDAO;
    public AlbumItemDAO albumItemDAO;

    public LiveData<List<AlbumEntity>> getAlbumEntityLiveData() {
        return albumEntityLiveData;
    }

    public void setAlbumEntityLiveData() {
        albumEntityLiveData = albumDAO.queryAllAlbum();
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

    public void queryByAlbumId(int albumId, CommonListener commonListener){
        new AlbumQueryByAlbumIdDBUtil(albumDAO,commonListener).execute(albumId);
    }

    public void updateAlbum(AlbumEntity... albumEntities){
        new AlbumUpdateDBUtil(albumDAO).execute(albumEntities);
    }
}
