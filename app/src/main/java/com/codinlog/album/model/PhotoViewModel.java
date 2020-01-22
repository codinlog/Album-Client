package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.ImageBean;

import java.util.ArrayList;

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<ArrayList<Object>> mutableLiveData;

    public MutableLiveData<ArrayList<Object>> getMutableLiveData() {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();
        return mutableLiveData;
    }

    public void setMutableLiveData(ArrayList<Object> value){
        mutableLiveData.postValue(value);
    }
}
