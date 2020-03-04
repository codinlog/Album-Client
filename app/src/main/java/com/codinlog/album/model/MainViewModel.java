package com.codinlog.album.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;
import com.codinlog.album.database.AlbumDatabase;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.codinlog.album.util.WorthStoreUtil.albumPager;
import static com.codinlog.album.util.WorthStoreUtil.photoPager;
import static com.codinlog.album.util.WorthStoreUtil.timePager;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FragmentBean>> fragmentLiveData;//界面
    private MutableLiveData<List<PhotoBean>> photoBeansLiveData;//图片数据
    private MutableLiveData<WorthStoreUtil.MODE> modeLiveData;//选择模式
    private MutableLiveData<Boolean> isSelectAllLiveData;//是否选择所有
    private MutableLiveData<Integer> currentPagerLiveData;//当前页面
    public AlbumDatabase albumDatabase;
    public PhotoViewModel photoViewModel;
    public AlbumViewModel albumViewModel;
    public TimeViewModel timeViewModel;

    public MutableLiveData<ArrayList<FragmentBean>> getFragmentLiveData() {
        if (fragmentLiveData == null) {
            fragmentLiveData = new MutableLiveData<>();
            fragmentLiveData.setValue(new ArrayList<>());
        }
        return fragmentLiveData;
    }

    public void setFragmentLiveData(ArrayList<FragmentBean> value){
        getFragmentLiveData().setValue(value);
    }


    public MutableLiveData<List<PhotoBean>> getPhotoBeansLiveData() {
        if(photoBeansLiveData == null){
            photoBeansLiveData = new MutableLiveData<>();
            photoBeansLiveData.setValue(new ArrayList<>());
        }
        return photoBeansLiveData;
    }

    public void setPhotoBeansLiveData(List<PhotoBean> value){
        Collections.sort(value);
        Log.d("hi", "v:" + value.size());
        getPhotoBeansLiveData().setValue(value);
    }

    public MutableLiveData<WorthStoreUtil.MODE> getModeLiveData() {
        if (modeLiveData == null) {
            modeLiveData = new MutableLiveData<>();
            modeLiveData.setValue(WorthStoreUtil.MODE.MODE_NORMAL);
        }
        return modeLiveData;
    }

    public void setModeLiveData(WorthStoreUtil.MODE value) {
        photoViewModel.resetSelectChangeCount();
        getModeLiveData().setValue(value);
    }

    public MutableLiveData<Boolean> getIsSelectAllLiveData() {
        if(isSelectAllLiveData == null){
            isSelectAllLiveData = new MutableLiveData<>();
            isSelectAllLiveData.setValue(false);
        }
        return isSelectAllLiveData;
    }

    public void setIsSelectAllLiveData(boolean value) {
        getIsSelectAllLiveData().setValue(value);
    }

    public void setIsSelectAllToOtherViewModel(){
        switch (getCurrentPagerLiveData().getValue()){
            case photoPager:
                photoViewModel.setIsSelectAllGroupFromMainViewMode(getIsSelectAllLiveData().getValue());break;
            case albumPager:break;
            case timePager:break;
        }
    }

    public MutableLiveData<Integer> getCurrentPagerLiveData() {
        if(currentPagerLiveData == null){
            currentPagerLiveData = new MutableLiveData<>();
            currentPagerLiveData.setValue(0);
        }
        return currentPagerLiveData;
    }

    public void setCurrentPagerLiveData(int value) {
        getCurrentPagerLiveData().setValue(value);
    }

    public AlbumDatabase getAlbumDatabase() {
        return albumDatabase;
    }

    public void setAlbumDatabase(AlbumDatabase albumDatabase) {
        this.albumDatabase = albumDatabase;
    }
}
