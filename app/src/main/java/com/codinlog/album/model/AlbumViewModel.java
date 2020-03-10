package com.codinlog.album.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.database.AlbumDatabase;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.util.kotlin.AlbumDeleteDBUtil;
import com.codinlog.album.util.kotlin.AlbumExistInsertWithPhotoBeansDBUtil;
import com.codinlog.album.util.kotlin.AlbumInsertDBUtil;
import com.codinlog.album.util.kotlin.AlbumInsertWithPhotoBeansDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryByAlbumIdDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryDBUtil;
import com.codinlog.album.util.kotlin.AlbumUpdateDBUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumViewModel extends ViewModel {

    private LiveData<List<AlbumEntity>> albumEntityLiveData;
    public MainViewModel mainViewModel;
    private AlbumDAO albumDAO;
    private AlbumItemDAO albumItemDAO;

    public LiveData<List<AlbumEntity>> getAlbumEntityLiveData() {
        if(albumEntityLiveData == null){
            albumEntityLiveData = getAlbumDAO().queryAllAlbum();
        }
        return albumEntityLiveData;
    }

    public AlbumDAO getAlbumDAO() {
        if(albumDAO == null)
            albumDAO = AlbumDatabase.getInstance().getAlbumDAO();
        return albumDAO;
    }

    public AlbumItemDAO getAlbumItemDAO() {
        if(albumItemDAO == null)
            albumItemDAO = AlbumDatabase.getInstance().getAlbumItemDAO();
        return albumItemDAO;
    }

    public void insertAlbum(AlbumEntity... albumEntities){
        new AlbumInsertDBUtil(getAlbumDAO()).execute(albumEntities);
    }

    public void deleteAlbum(AlbumEntity... albumEntities){
        new AlbumDeleteDBUtil(getAlbumDAO()).execute(albumEntities);
    }

    public void queryAlbum(){
        new AlbumQueryDBUtil(getAlbumDAO()).execute();
    }

    public void queryByAlbumId(int albumId, CommonListener commonListener){
        new AlbumQueryByAlbumIdDBUtil(getAlbumDAO(),commonListener).execute(albumId);
    }

    public void updateAlbum(AlbumEntity... albumEntities){
        new AlbumUpdateDBUtil(getAlbumDAO()).execute(albumEntities);
    }

    public void insertAlbumWithPhotoBeans(AlbumEntity albumEntity,List<PhotoBean> photoBeans,CommonListener commonListener){
        new AlbumInsertWithPhotoBeansDBUtil(albumDAO,getAlbumItemDAO(),albumEntity,commonListener).execute(photoBeans.stream().map(v -> {
            AlbumItemEntity albumItemEntity = new AlbumItemEntity();
            albumItemEntity.setBelongToId(albumEntity.getAlbumId());
            albumItemEntity.setPhotoBean(v);
            albumItemEntity.setUuid((v.getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            return albumItemEntity;
        }).toArray(AlbumItemEntity[]::new));
    }

    public void insertExistAlbumWithPhotoBeans(AlbumEntity albumEntity,List<PhotoBean> photoBeans,CommonListener commonListener){
        new AlbumExistInsertWithPhotoBeansDBUtil(albumDAO,getAlbumItemDAO(),albumEntity,commonListener).execute(photoBeans.stream().map(v -> {
            AlbumItemEntity albumItemEntity = new AlbumItemEntity();
            albumItemEntity.setBelongToId(albumEntity.getAlbumId());
            albumItemEntity.setPhotoBean(v);
            albumItemEntity.setUuid((v.getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            return albumItemEntity;
        }).toArray(AlbumItemEntity[]::new));
    }
}
