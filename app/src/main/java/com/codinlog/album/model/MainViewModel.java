package com.codinlog.album.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.codinlog.album.util.WorthStoreUtil.albumPager;
import static com.codinlog.album.util.WorthStoreUtil.photoPager;
import static com.codinlog.album.util.WorthStoreUtil.timePager;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FragmentBean>> fragmentMutableLiveData;//界面
    private MutableLiveData<ArrayList<PhotoBean>> photoMutableLiveData;//图片数据
    private MutableLiveData<WorthStoreUtil.MODE> modeMutableLiveData;//选择模式
    private MutableLiveData<Boolean> isSelectAllMutableLiveData;//是否选择所有
    private MutableLiveData<Integer> currentPagerMutableLiveData;//当前页面
    public PhotoViewModel photoViewModel;
    public AlbumViewModel albumViewModel;
    public TimeViewModel timeViewModel;

    public MutableLiveData<ArrayList<FragmentBean>> getFragmentMutableLiveData() {
        if (fragmentMutableLiveData == null) {
            fragmentMutableLiveData = new MutableLiveData<>();
            fragmentMutableLiveData.setValue(new ArrayList<FragmentBean>());
        }
        return fragmentMutableLiveData;
    }

    public void setFragmentMutableLiveData(ArrayList<FragmentBean> value){
        getFragmentMutableLiveData().setValue(value);
    }


    public MutableLiveData<ArrayList<PhotoBean>> getPhotoMutableLiveData() {
        if(photoMutableLiveData == null){
            photoMutableLiveData = new MutableLiveData<>();
            photoMutableLiveData.setValue(new ArrayList<PhotoBean>());
        }
        return photoMutableLiveData;
    }

    public void setPhotoMutableLiveData(ArrayList<PhotoBean> value){
        getPhotoMutableLiveData().setValue(value);
    }

    public MutableLiveData<WorthStoreUtil.MODE> getModeMutableLiveData() {
        if (modeMutableLiveData == null) {
            modeMutableLiveData = new MutableLiveData<>();
            modeMutableLiveData.setValue(WorthStoreUtil.MODE.MODE_NORMAL);
        }
        return modeMutableLiveData;
    }

    public void setModeMutableLiveData(WorthStoreUtil.MODE value) {
        photoViewModel.resetSelectChangeCount();
        getModeMutableLiveData().setValue(value);
    }

    public MutableLiveData<Boolean> getIsSelectAllMutableLiveData() {
        if(isSelectAllMutableLiveData == null){
            isSelectAllMutableLiveData = new MutableLiveData<>();
            isSelectAllMutableLiveData.setValue(false);
        }
        return isSelectAllMutableLiveData;
    }

    public void setIsSelectAllMutableLiveData(boolean value) {
        getIsSelectAllMutableLiveData().setValue(value);
    }

    public void setIsSelectAllToOtherViewModel(){
        switch (getCurrentPagerMutableLiveData().getValue()){
            case photoPager:
                photoViewModel.setIsSelectAllGroupFromMainViewMode(getIsSelectAllMutableLiveData().getValue());break;
            case albumPager:break;
            case timePager:break;
        }
    }

    public void setPhotoViewModelListData(List<Object> resList){
        photoViewModel.setClassifiedResListMutableLiveData(resList);
    }

    public void setPhotoViewModelMapData(Map<String,List<PhotoBean>> resMap){
        photoViewModel.setClassifiedResMapMutableLiveData(resMap);
    }

    public void setPhotoViewModelMapNumData(Map<String, PhotoSelectedNumBean> resNumMap){
        photoViewModel.setClassifiedPhotoResNumMapMutableLiveData(resNumMap);
    }

    public MutableLiveData<Integer> getCurrentPagerMutableLiveData() {
        if(currentPagerMutableLiveData == null){
            currentPagerMutableLiveData = new MutableLiveData<>();
            currentPagerMutableLiveData.setValue(0);
        }
        return currentPagerMutableLiveData;
    }

    public void setCurrentPagerMutableLiveData(int value) {
        getCurrentPagerMutableLiveData().setValue(value);
    }


}
