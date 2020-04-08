package com.codinlog.album.model.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean

class AlbumPhotoDisplayViewModel : ViewModel() {
    var displayData: LiveData<List<PhotoBean>> = MutableLiveData<List<PhotoBean>>().apply { value = listOf() }
    var albumPreviewViewModel: AlbumPreviewViewModel? = null
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel? = null
}