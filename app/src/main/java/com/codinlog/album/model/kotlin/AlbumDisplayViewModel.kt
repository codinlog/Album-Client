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
    var displayData:LiveData<List<PhotoBean>> = MutableLiveData<List<PhotoBean>>().apply { value = listOf() }
    var albumPreviewViewModel : AlbumPreviewViewModel? = null
}