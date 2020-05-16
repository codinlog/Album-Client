package com.codinlog.album.model.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.entity.kotlin.DiaryEntity

class DiaryDetailViewModel : ViewModel() {
    val displayData = MutableLiveData<DiaryEntity>()
}