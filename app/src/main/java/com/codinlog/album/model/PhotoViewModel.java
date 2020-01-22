package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.ImageBean;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<ArrayList<Object>> objectMutableLiveData; //图片数据据
    private MutableLiveData<WorthStoreUtil.MODE> modeMutableLiveData;//选择模式
    private MutableLiveData<ArrayList<Integer>> selectMutableLiveData;//已选择

    public MutableLiveData<ArrayList<Object>> getObjectMutableLiveData() {
        if (objectMutableLiveData == null)
            objectMutableLiveData = new MutableLiveData<>();
        return objectMutableLiveData;
    }

    public void setObjectMutableLiveData(ArrayList<Object> value) {
        objectMutableLiveData.postValue(value);
    }

    public MutableLiveData<WorthStoreUtil.MODE> getModeMutableLiveData() {
        if (modeMutableLiveData == null) {
            modeMutableLiveData = new MutableLiveData<>();
            modeMutableLiveData.setValue(WorthStoreUtil.MODE.MODE_NORMAL);
        }
        return modeMutableLiveData;
    }

    public void setModeMutableLiveData(WorthStoreUtil.MODE value) {
        modeMutableLiveData.postValue(value);
    }

    public MutableLiveData<ArrayList<Integer>> getSelectMutableLiveData() {
        if (selectMutableLiveData == null) {
            selectMutableLiveData = new MutableLiveData<>();
            selectMutableLiveData.postValue(new ArrayList<Integer>());
        }
        return selectMutableLiveData;
    }

    public void addSelectMutableLiveData(Integer value) {
        selectMutableLiveData.getValue().add(value);
    }
}
