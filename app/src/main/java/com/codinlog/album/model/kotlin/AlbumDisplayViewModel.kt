package com.codinlog.album.model.kotlin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumItemEntity

class AlbumDisplayViewModel : ViewModel() {
    var displayData = MutableLiveData<List<PhotoBean>>().apply { value = listOf() }
    var albumPreviewViewModel : AlbumPreviewViewModel? = null
    var data : LiveData<List<AlbumItemEntity>>? = null

    fun setDisplayData(value : List<PhotoBean>) : Unit{
        Log.d("err","size :" + value.size)
        Log.d("err","ViewModel Running")
        displayData.value = value
    }
}