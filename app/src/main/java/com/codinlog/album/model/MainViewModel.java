package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.FragmentBean;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<FragmentBean>> fragmentBeans;

    public MutableLiveData<ArrayList<FragmentBean>> getFragmentList() {
        if (fragmentBeans == null)
            fragmentBeans = new MutableLiveData<>();
        return fragmentBeans;
    }

    public void setFragmentList(ArrayList<FragmentBean> value){
        fragmentBeans.setValue(value);
    }
}
