package com.codinlog.album.model;

import android.app.Activity;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.kotlin.CategoryBean;
import com.codinlog.album.bean.kotlin.FolderBean;
import com.codinlog.album.dao.AlbumDAO;
import com.codinlog.album.dao.AlbumItemDAO;
import com.codinlog.album.dao.kotlin.CategoryDAO;
import com.codinlog.album.database.AlbumDatabase;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;
import com.codinlog.album.entity.kotlin.CategoryEntity;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.util.Classify;
import com.codinlog.album.util.WorthStore;
import com.codinlog.album.util.kotlin.AlbumCategory;
import com.codinlog.album.util.kotlin.AlbumDeleteDB;
import com.codinlog.album.util.kotlin.AlbumExistInsertWithPhotoBeansDB;
import com.codinlog.album.util.kotlin.AlbumInsertDB;
import com.codinlog.album.util.kotlin.AlbumInsertWithPhotoBeansDB;
import com.codinlog.album.util.kotlin.AlbumItemDeleteDB;
import com.codinlog.album.util.kotlin.AlbumItemQueryByAlbumIdDB;
import com.codinlog.album.util.kotlin.AlbumMergeDB;
import com.codinlog.album.util.kotlin.AlbumQueryByAlbumIdDB;
import com.codinlog.album.util.kotlin.AlbumQueryDB;
import com.codinlog.album.util.kotlin.AlbumRenameDB;
import com.codinlog.album.util.kotlin.AlbumUpdateDB;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kotlin.Pair;

public class AlbumViewModel extends ViewModel {

    public MainViewModel mainViewModel;
    Object lock;
    private MutableLiveData<Integer> displayOption;
    private LiveData<List<AlbumEntity>> albumDisplayData;
    private MutableLiveData<Map<FolderBean, List<PhotoBean>>> folderDisplayData;
    private MutableLiveData<Pair<List<PhotoBean>, List<CategoryEntity>>> categoryClassifiedData;
    private MutableLiveData<Map<CategoryBean, List<PhotoBean>>> categoryDisplayData;
    private LiveData<List<CategoryEntity>> categoryData;
    private MutableLiveData<WorthStore.MODE> mode;
    private MutableLiveData<List<AlbumEntity>> selectedData;
    private MutableLiveData<Boolean> isSelectAll;
    private AlbumDAO albumDAO;
    private AlbumItemDAO albumItemDAO;
    private CategoryDAO categoryDAO;
    private Handler handler = new Handler();
    private Runnable classifiedRunnable;

    public LiveData<List<AlbumEntity>> getAlbumDisplayData() {
        if (albumDisplayData == null)
            albumDisplayData = getAlbumDAO().queryAllAlbum();
        return albumDisplayData;
    }

    public void setAlbumDisplayData(List<AlbumEntity> value) {
        getSelectedData().setValue(value);
    }

    public MutableLiveData<WorthStore.MODE> getMode() {
        if (mode == null) {
            mode = new MutableLiveData<>();
            mode.setValue(WorthStore.MODE.MODE_NORMAL);
        }
        return mode;
    }

    public void setMode(WorthStore.MODE mode) {
        getMode().setValue(mode);
    }

    public MutableLiveData<List<AlbumEntity>> getSelectedData() {
        if (selectedData == null) {
            selectedData = new MediatorLiveData<>();
            selectedData.setValue(new ArrayList<>());
        }
        return selectedData;
    }

    public void setSelectedData(int position) {
        AlbumEntity albumEntity = albumDisplayData.getValue().get(position);
        albumEntity.setSelect(!albumEntity.isSelect());
        if (albumEntity.isSelect()) {
            if (!getSelectedData().getValue().contains(albumEntity))
                getSelectedData().getValue().add(albumEntity);
        } else {
            getSelectedData().getValue().remove(albumEntity);
        }
        getSelectedData().setValue(getSelectedData().getValue());
    }

    public AlbumDAO getAlbumDAO() {
        if (albumDAO == null)
            albumDAO = AlbumDatabase.getInstance().getAlbumDAO();
        return albumDAO;
    }

    public AlbumItemDAO getAlbumItemDAO() {
        if (albumItemDAO == null)
            albumItemDAO = AlbumDatabase.getInstance().getAlbumItemDAO();
        return albumItemDAO;
    }

    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null)
            categoryDAO = AlbumDatabase.getInstance().getCategoryDAO();
        return categoryDAO;
    }

    public void insertAlbum(AlbumEntity... albumEntities) {
        new AlbumInsertDB(getAlbumDAO()).execute(albumEntities);
    }

    public void deleteAlbum(CommonListener commonListener, AlbumEntity... albumEntities) {
        new AlbumDeleteDB(getAlbumDAO(), commonListener).execute(albumEntities);
    }

    public void queryAlbum(CommonListener commonListener) {
        new AlbumQueryDB(getAlbumDAO(), commonListener).execute();
    }

    public void queryAlbumById(int albumId, CommonListener commonListener) {
        new AlbumQueryByAlbumIdDB(getAlbumDAO(), commonListener).execute(albumId);
    }

    public void renameAlbum(int oldId, String albumName, CommonListener commonListener) {
        new AlbumRenameDB(commonListener, oldId, getAlbumDAO()).execute(albumName);
    }

    public void updateAlbum(CommonListener commonListener, AlbumEntity... albumEntities) {
        new AlbumUpdateDB(getAlbumDAO(), commonListener).execute(albumEntities);
    }

    public void deleteAlbumItem(AlbumItemEntity... albumItemEntities) {
        new AlbumItemDeleteDB(getAlbumItemDAO()).execute(albumItemEntities);
    }

    public void queryAlbumItemById(int albumId, CommonListener commonListener) {
        new AlbumItemQueryByAlbumIdDB(getAlbumItemDAO(), commonListener).execute(albumId);
    }

    public void insertAlbumWithPhotoBeans(AlbumEntity albumEntity, List<PhotoBean> photoBeans, CommonListener commonListener) {
        new AlbumInsertWithPhotoBeansDB(albumDAO, getAlbumItemDAO(), albumEntity, commonListener).execute(photoBeans.stream().map(v -> {
            AlbumItemEntity albumItemEntity = new AlbumItemEntity();
            albumItemEntity.setBelongToId(albumEntity.getAlbumId());
            albumItemEntity.setPhotoBean(v);
            albumItemEntity.setUuid((v.getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            return albumItemEntity;
        }).toArray(AlbumItemEntity[]::new));
    }

    public void insertExistAlbumWithPhotoBeans(AlbumEntity albumEntity, List<PhotoBean> photoBeans, CommonListener commonListener) {
        new AlbumExistInsertWithPhotoBeansDB(albumDAO, getAlbumItemDAO(), albumEntity, commonListener).execute(photoBeans.stream().map(v -> {
            AlbumItemEntity albumItemEntity = new AlbumItemEntity();
            albumItemEntity.setBelongToId(albumEntity.getAlbumId());
            albumItemEntity.setPhotoBean(v);
            albumItemEntity.setUuid((v.getPhotoPath() + albumEntity.getAlbumName()).hashCode());
            return albumItemEntity;
        }).toArray(AlbumItemEntity[]::new));
    }

    public void mergeAlbum(AlbumEntity targetAlbumEntity, List<AlbumEntity> todoAlbumEntities, boolean keepOldAlbum, boolean createNew, CommonListener commonListener) {
        new AlbumMergeDB(getAlbumDAO(), getAlbumItemDAO(), targetAlbumEntity
                , keepOldAlbum, createNew, commonListener).execute(todoAlbumEntities);
    }

    public void resetSelectData() {
        getSelectedData().getValue().forEach(
                it -> {
                    it.setSelect(false);
                }
        );
        getSelectedData().setValue(new ArrayList<>());
    }

    public MutableLiveData<Integer> getDisplayOption() {
        if (displayOption == null) {
            displayOption = new MediatorLiveData<>();
            displayOption.setValue(BottomSheetBehavior.STATE_HIDDEN);
        }
        return displayOption;
    }

    public void setDisplayOption(int value) {
        getDisplayOption().setValue(value);
    }

    public MutableLiveData<Map<FolderBean, List<PhotoBean>>> getFolderDisplayData() {
        if (folderDisplayData == null) {
            folderDisplayData = new MediatorLiveData<>();
            Map<FolderBean, List<PhotoBean>> map = new LinkedHashMap<>();
            folderDisplayData.setValue(map);
        }
        return folderDisplayData;
    }

    public void setFolderDisplayData(Map<FolderBean, List<PhotoBean>> value) {
        getFolderDisplayData().setValue(value);
    }

    public void setFolderClassifiedData(List<PhotoBean> it) {
        synchronized (lock) {
            if (classifiedRunnable == null)
                classifiedRunnable = () -> setFolderDisplayData(Classify.PhotoBeansFolderClassify(it, getFolderDisplayData().getValue()));
            handler.removeCallbacks(classifiedRunnable);
            handler.post(classifiedRunnable);
        }
    }

    public void beginCategoryClassify(Pair<List<PhotoBean>, List<CategoryEntity>> pair, Activity activity) {
        new AlbumCategory(pair, activity, getCategoryDAO()).categoryClassify();
    }

    public MutableLiveData<Boolean> getIsSelectAll() {
        if (isSelectAll == null)
            isSelectAll = new MediatorLiveData<>();
        return isSelectAll;
    }

    public void setIsSelectAll(boolean isSelectAll) {
        getIsSelectAll().setValue(isSelectAll);
    }

    public void selectAllAlbum(boolean isAll) {
        albumDisplayData.getValue().forEach(it -> {
            it.setSelect(isAll);
            if (it.isSelect()) {
                if (!getSelectedData().getValue().contains(it))
                    getSelectedData().getValue().add(it);
            } else
                getSelectedData().getValue().remove(it);
        });
        getSelectedData().setValue(getSelectedData().getValue());
        setIsSelectAll(isAll);
    }

    public LiveData<List<CategoryEntity>> getCategoryData() {
        if (categoryData == null)
            categoryData = getCategoryDAO().queryAll();
        return categoryData;
    }

    public MutableLiveData<Pair<List<PhotoBean>, List<CategoryEntity>>> getCategoryClassifiedData() {
        if (categoryClassifiedData == null) {
            categoryClassifiedData = new MediatorLiveData<>();
            categoryClassifiedData.setValue(new Pair<>(null, null));
        }
        return categoryClassifiedData;
    }

    public void setCategoryClassifiedData(List<PhotoBean> photoBeans, List<CategoryEntity> categoryEntities) {
        getCategoryClassifiedData().setValue(new Pair<>(photoBeans, categoryEntities));
    }

    public MutableLiveData<Map<CategoryBean, List<PhotoBean>>> getCategoryDisplayData() {
        if (categoryDisplayData == null)
            categoryDisplayData = new MutableLiveData<>();
        return categoryDisplayData;
    }

    public void setCategoryDisplayData(Map<CategoryBean, List<PhotoBean>> value) {
        getCategoryDisplayData().setValue(value);
    }
}
