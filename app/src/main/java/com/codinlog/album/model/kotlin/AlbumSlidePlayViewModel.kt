package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean

class AlbumSlidePlayViewModel : ViewModel() {
    val  displayData = MutableLiveData<List<PhotoBean>>().apply { value = arrayListOf() }

    fun setDisplayData(value : List<PhotoBean>){
        displayData.value = value
    }
}