package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.ImageBean;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FragmentBean>> fragmentBeans;
    private MutableLiveData<ArrayList<ImageBean>> imageBeans;

    public MutableLiveData<ArrayList<FragmentBean>> getFragmentBeans() {
        if (fragmentBeans == null)
            fragmentBeans = new MutableLiveData<>();
        return fragmentBeans;
    }

    public void setFragmentBeans(ArrayList<FragmentBean> value){
        fragmentBeans.setValue(value);
    }


    public MutableLiveData<ArrayList<ImageBean>> getImageBeans() {
        if(imageBeans == null){
            imageBeans = new MutableLiveData<>();
        }
        return imageBeans;
    }

    public void setImageBeans(ArrayList<ImageBean> value){
        imageBeans.postValue(value);
    }


}
