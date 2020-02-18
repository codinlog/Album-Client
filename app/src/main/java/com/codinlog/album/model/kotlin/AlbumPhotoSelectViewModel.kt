package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean

class AlbumPhotoSelectViewModel : ViewModel() {
    var disPlayData : MutableLiveData<List<PhotoBean>> = MutableLiveData()
    var selectData : MutableLiveData<List<PhotoBean>> = MutableLiveData()
    get() {
        if(field.value == null)
            field.value = mutableListOf()
        return field
    }

    fun setDisplayData(value : List<PhotoBean>){
        disPlayData.value = value
    }

    fun addSelectData(value : PhotoBean){
        if(value.isSelected)
            selectData.value = selectData.value?.plus(value)
        else
            selectData.value = selectData.value?.minus(value)
        selectData.value = selectData.value
    }
}