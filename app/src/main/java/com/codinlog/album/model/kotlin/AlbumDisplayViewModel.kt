package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase

class AlbumDisplayViewModel : ViewModel() {
    var displayData: MutableLiveData<List<PhotoBean>> = MutableLiveData()
        get() = field
    public fun setDisplayData(value : List<PhotoBean>) : Unit{
        displayData.value = value
    }
}