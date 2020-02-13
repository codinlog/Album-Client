package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewViewModel extends ViewModel {
    private MutableLiveData<List<PhotoBean>> displayPhotoBeansMutableLiveData;
    private MutableLiveData<Integer> currentPositionMutableLiveData;

    public MutableLiveData<List<PhotoBean>> getDisplayPhotoBeansMutableLiveData() {
        if(displayPhotoBeansMutableLiveData == null){
            displayPhotoBeansMutableLiveData = new MutableLiveData<>();
            displayPhotoBeansMutableLiveData.setValue(new ArrayList<>());
        }
        return displayPhotoBeansMutableLiveData;
    }

    public void setDisplayPhotoBeansMutableLiveData(List<PhotoBean> value) {
        getDisplayPhotoBeansMutableLiveData().setValue(value);
    }

    public MutableLiveData<Integer> getCurrentPositionMutableLiveData() {
        if(currentPositionMutableLiveData == null){
            currentPositionMutableLiveData = new MutableLiveData<>();
            currentPositionMutableLiveData.setValue(1);
        }
        return currentPositionMutableLiveData;
    }

    public void setCurrentPositionMutableLiveData(int value) {
        getCurrentPositionMutableLiveData().setValue(value);
    }
}
