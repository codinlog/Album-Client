package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.database.AlbumDatabase

class DiaryPublicViewModel : ViewModel() {
    val diaryDAO = AlbumDatabase.getInstance().diaryDAO
    val selectData = MutableLiveData<List<PhotoBean>>().also { it.value = arrayListOf() }
    val displayData = MutableLiveData<List<PhotoBean>>().also { it.value = arrayListOf() }


    fun setDisplayData(value: List<PhotoBean>) {
        value.forEach {it.isSelected = false}
        displayData.value = value
    }
}