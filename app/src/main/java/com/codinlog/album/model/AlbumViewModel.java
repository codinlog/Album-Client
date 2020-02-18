package com.codinlog.album.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
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

    public void insertAlbumWithPhotoBeans(AlbumEntity albumEntity,List<PhotoBean> photoBeans,CommonListener commonListener){
        List<AlbumItemEntity> albumItemEntities = photoBeans.stream().map(v -> {
            AlbumItemEntity albumItemEntity = new AlbumItemEntity();
            albumItemEntity.setBelongToId(albumEntity.getAlbumId());
            albumItemEntity.setPhotoBean(v);
            albumItemEntity.setUuid((v.getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            return albumItemEntity;
        }).collect(Collectors.toList());
        new AlbumInsertWithPhotoBeansDBUtil(albumDAO,albumItemDAO,albumEntity,commonListener).execute( albumItemEntities.toArray(new AlbumItemEntity[albumItemEntities.size()]));
    }

    public void insertExistAlbumWithPhotoBeans(AlbumEntity albumEntity,List<PhotoBean> photoBeans,CommonListener commonListener){
        List<AlbumItemEntity> albumItemEntities = photoBeans.stream().map(v -> {
            AlbumItemEntity albumItemEntity = new AlbumItemEntity();
            albumItemEntity.setBelongToId(albumEntity.getAlbumId());
            albumItemEntity.setPhotoBean(v);
            albumItemEntity.setUuid((v.getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            return albumItemEntity;
        }).collect(Collectors.toList());
        new AlbumExistInsertWithPhotoBeansDBUtil(albumDAO,albumItemDAO,albumEntity,commonListener).execute(albumItemEntities.toArray(new AlbumItemEntity[albumItemEntities.size()]));
    }
}
