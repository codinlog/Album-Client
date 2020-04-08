package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewViewModel extends ViewModel {
    private MutableLiveData<List<PhotoBean>> displayData;
    private MutableLiveData<Integer> currentPosition;

    public MutableLiveData<List<PhotoBean>> getDisplayData() {
        if (displayData == null) {
            displayData = new MutableLiveData<>();
            displayData.setValue(new ArrayList<>());
        }
        return displayData;
    }

    public void setDisplayData(List<PhotoBean> value) {
        getDisplayData().setValue(value);
    }

    public MutableLiveData<Integer> getCurrentPosition() {
        if (currentPosition == null) {
            currentPosition = new MutableLiveData<>();
            currentPosition.setValue(1);
        }
        return currentPosition;
    }

    public void setCurrentPosition(int value) {
        getCurrentPosition().setValue(value);
    }
}
