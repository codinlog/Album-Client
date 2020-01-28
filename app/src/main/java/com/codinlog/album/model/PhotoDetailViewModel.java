package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailViewModel extends ViewModel {
    private MutableLiveData<List<PhotoBean>> classifiedPhotoBeanResList;

    public MutableLiveData<List<PhotoBean>> getClassifiedPhotoBeanResList() {
        if(classifiedPhotoBeanResList == null){
            classifiedPhotoBeanResList = new MutableLiveData<>();
            classifiedPhotoBeanResList.setValue(new ArrayList<>());
        }
        return classifiedPhotoBeanResList;
    }

    public void setPhotoMutableLiveData(List<PhotoBean> value) {
        getClassifiedPhotoBeanResList().setValue(value);
    }
}
