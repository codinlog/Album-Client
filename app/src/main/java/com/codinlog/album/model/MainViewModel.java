package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.List;

import static com.codinlog.album.util.WorthStoreUtil.MODE.MODE_NORMAL;
import static com.codinlog.album.util.WorthStoreUtil.albumPager;
import static com.codinlog.album.util.WorthStoreUtil.photoPager;
import static com.codinlog.album.util.WorthStoreUtil.timePager;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FragmentBean>> fragments;//界面
    private MutableLiveData<List<PhotoBean>> photoBeans;//图片数据
    private MutableLiveData<WorthStoreUtil.MODE> mode;//选择模式
    private MutableLiveData<Boolean> isSelectAll;//是否选择所有
    private MutableLiveData<Integer> currentPager;//当前页面
    private MutableLiveData<String> title;
    public PhotoViewModel photoViewModel;
    public AlbumViewModel albumViewModel;
    public TimeViewModel timeViewModel;

    public MutableLiveData<ArrayList<FragmentBean>> getFragments() {
        if (fragments == null) {
            fragments = new MutableLiveData<>();
            fragments.setValue(new ArrayList<>());
        }
        return fragments;
    }

    public void setFragments(ArrayList<FragmentBean> value){
        getFragments().setValue(value);
    }

    public MutableLiveData<String> getTitle() {
        if(title == null){
            title = new MutableLiveData<>();
            title.setValue("");
        }
        return title;
    }

    public void setTitle() {
        String title = "";
        switch (getCurrentPager().getValue()) {
            case photoPager:
                title = String.format(AlbumApplication.mContext.getString(R.string.top_bar_select_notice),photoViewModel.getSelectedData().getValue().size());break;
            case albumPager:
                title = String.format(AlbumApplication.mContext.getString(R.string.top_bar_select_notice),albumViewModel.getSelectedData().getValue().size());break;
            case timePager:break;
        }
        getTitle().setValue(title);
    }

    public MutableLiveData<List<PhotoBean>> getPhotoBeans() {
        if(photoBeans == null){
            photoBeans = new MutableLiveData<>();
            photoBeans.setValue(new ArrayList<>());
        }
        return photoBeans;
    }

    public void setPhotoBeans(List<PhotoBean> value){
        getPhotoBeans().setValue(value);
    }

    public MutableLiveData<WorthStoreUtil.MODE> getMode() {
        if (mode == null) {
            mode = new MutableLiveData<>();
            mode.setValue(WorthStoreUtil.MODE.MODE_NORMAL);
        }
        return mode;
    }

    public void setMode(WorthStoreUtil.MODE value) {
        getMode().setValue(value);
    }

    public MutableLiveData<Boolean> getIsSelectAll() {
        if(isSelectAll == null){
            isSelectAll = new MutableLiveData<>();
            isSelectAll.setValue(false);
        }
        return isSelectAll;
    }

    public void setIsSelectAll(boolean value) {
        getIsSelectAll().setValue(value);
    }


    public MutableLiveData<Integer> getCurrentPager() {
        if(currentPager == null){
            currentPager = new MutableLiveData<>();
            currentPager.setValue(0);
        }
        return currentPager;
    }

    public void setCurrentPager(int value) {
        getCurrentPager().setValue(value);
    }
    public void setIsSelectAllToOtherViewModel(){
        switch (getCurrentPager().getValue()){
            case photoPager:
                photoViewModel.selectAllGroup(!getIsSelectAll().getValue());break;
            case albumPager:break;
            case timePager:break;
        }
    }

    public void modeChanged(){
        if(getMode().getValue() == MODE_NORMAL)
            getIsSelectAll().setValue(false);
        switch (getCurrentPager().getValue()){
            case photoPager :{
                photoViewModel.setMode(getMode().getValue());
            }break;
            case albumPager: {
                albumViewModel.setMode(getMode().getValue());
            }break;
            case timePager:break;
        }
    }

}
