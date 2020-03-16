package com.codinlog.album.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.database.AlbumDatabase;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.util.WorthStoreUtil;
import com.codinlog.album.util.kotlin.AlbumDeleteDBUtil;
import com.codinlog.album.util.kotlin.AlbumExistInsertWithPhotoBeansDBUtil;
import com.codinlog.album.util.kotlin.AlbumInsertDBUtil;
import com.codinlog.album.util.kotlin.AlbumInsertWithPhotoBeansDBUtil;
import com.codinlog.album.util.kotlin.AlbumItemDeleteDBUtil;
import com.codinlog.album.util.kotlin.AlbumItemQueryByAlbumIdDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryByAlbumIdDBUtil;
import com.codinlog.album.util.kotlin.AlbumQueryDBUtil;
import com.codinlog.album.util.kotlin.AlbumUpdateDBUtil;

import java.util.ArrayList;
import java.util.List;

public class AlbumViewModel extends ViewModel {

    private LiveData<List<AlbumEntity>> displayData;
    private MutableLiveData<WorthStoreUtil.MODE> mode;
    private MutableLiveData<List<AlbumEntity>> selectedData;
    public MainViewModel mainViewModel;
    private AlbumDAO albumDAO;
    private AlbumItemDAO albumItemDAO;

    public LiveData<List<AlbumEntity>> getDisplayData() {
        if(displayData == null){
            displayData = getAlbumDAO().queryAllAlbum();
        }
        return displayData;
    }

    public MutableLiveData<WorthStoreUtil.MODE> getMode() {
        if(mode == null){
            mode = new MediatorLiveData<>();
            mode.setValue(WorthStoreUtil.MODE.MODE_NORMAL);
        }
        return mode;
    }

    public void setDisplayData(List<AlbumEntity> value) {
        getSelectedData().setValue(value);
    }

    public MutableLiveData<List<AlbumEntity>> getSelectedData() {
        if(selectedData == null){
            selectedData = new MediatorLiveData<>();
            selectedData.setValue(new ArrayList<>());
        }
        return selectedData;
    }

    public void setSelectedData(int position) {
        AlbumEntity albumEntity = displayData.getValue().get(position);
        albumEntity.setSelect(!albumEntity.isSelect());
        if(albumEntity.isSelect()){
            if(!getSelectedData().getValue().contains(albumEntity))
                getSelectedData().getValue().add(albumEntity);
        }else{
            getSelectedData().getValue().remove(albumEntity);
        }
        getSelectedData().setValue(getSelectedData().getValue());
    }

    public void setMode(WorthStoreUtil.MODE mode) {
        getMode().setValue(mode);
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

    public void queryAlbum(CommonListener commonListener){
        new AlbumQueryDBUtil(getAlbumDAO(),commonListener).execute();
    }

    public void queryAlbumById(int albumId, CommonListener commonListener){
        new AlbumQueryByAlbumIdDBUtil(getAlbumDAO(),commonListener).execute(albumId);
    }

    public void updateAlbum(AlbumEntity... albumEntities){
        new AlbumUpdateDBUtil(getAlbumDAO()).execute(albumEntities);
    }

    public void deleteAlbumItem(AlbumItemEntity ... albumItemEntities){
        new AlbumItemDeleteDBUtil(getAlbumItemDAO()).execute(albumItemEntities);
    }

    public void queryAlbumItemById(int albumId, CommonListener commonListener){
        new AlbumItemQueryByAlbumIdDBUtil(getAlbumItemDAO(),commonListener).execute(albumId);
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

    public void resetSelectData(){
        getSelectedData().getValue().forEach(
                it ->{it.setSelect(false);}
        );
        getSelectedData().setValue(new ArrayList<>());
    }

}
