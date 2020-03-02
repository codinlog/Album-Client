package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean

class AlbumPhotoSelectViewModel : ViewModel() {
    var albumPreviewViewModel : AlbumPreviewViewModel? = null
    var displayData: MutableLiveData<List<PhotoBean>> = MutableLiveData()
    var selectData: MutableLiveData<MutableList<PhotoBean>> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = mutableListOf()
            return field
        }

    fun setDisplayData(value: List<PhotoBean>) {
        displayData.value = value
    }

    fun addSelectData(position: Int, isSelect: Boolean?) {
        displayData.value?.let {
            var photoBean = it[position]
            photoBean.isSelected = isSelect ?: !photoBean.isSelected
            if (photoBean.isSelected)
                selectData.value?.add(photoBean)
            else
                selectData.value?.remove(photoBean)
            selectData.value = selectData.value
        }
    }
}