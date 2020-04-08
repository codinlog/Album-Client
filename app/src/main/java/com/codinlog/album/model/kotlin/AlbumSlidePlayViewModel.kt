package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean

class AlbumSlidePlayViewModel : ViewModel() {
    val displayData = MutableLiveData<List<PhotoBean>>().apply { value = arrayListOf() }
    val currentPosition = MutableLiveData<Int>().apply { value = 0 }
    fun setDisplayData(value: List<PhotoBean>) {
        value.forEach { it.isSelected = false }
        displayData.value = value
    }
}