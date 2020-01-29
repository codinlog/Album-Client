package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailViewModel extends ViewModel {
    private MutableLiveData<List<PhotoBean>> classifiedPhotoBeanResListMutableLiveData;
    private MutableLiveData<Integer> currentPositionMutableLiveData;

    public MutableLiveData<List<PhotoBean>> getClassifiedPhotoBeanResListMutableLiveData() {
        if(classifiedPhotoBeanResListMutableLiveData == null){
            classifiedPhotoBeanResListMutableLiveData = new MutableLiveData<>();
            classifiedPhotoBeanResListMutableLiveData.setValue(new ArrayList<>());
        }
        return classifiedPhotoBeanResListMutableLiveData;
    }

    public void setClassifiedPhotoBeanResListMutableLiveData(List<PhotoBean> value) {
        getClassifiedPhotoBeanResListMutableLiveData().setValue(value);
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
