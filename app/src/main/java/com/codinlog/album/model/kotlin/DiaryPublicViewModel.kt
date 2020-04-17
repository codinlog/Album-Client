package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.kotlin.DiaryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DiaryPublicViewModel : ViewModel() {
    private val diaryDAO = AlbumDatabase.getInstance().diaryDAO
    val selectData = MutableLiveData<MutableList<PhotoBean>>().also { it.value = mutableListOf() }
    val displayData = MutableLiveData<List<PhotoBean>>().also { it.value = LinkedList() }


    fun setDisplayData(value: List<PhotoBean>) {
        value.forEach { it.isSelected = false }
        displayData.value = value
    }

    fun addSelectData(photoBean: PhotoBean) {
        if (photoBean.isSelected)
            selectData.value?.add(photoBean)
        else
            selectData.value?.remove(photoBean)
    }

    fun publishDiary(title: String, content: String) {
        viewModelScope.launch {
            val res = withContext(Dispatchers.IO) {
                val diaryEntity = DiaryEntity()
                with(diaryEntity){
                    this.title = title
                    this.content = content
                    this.photoBeans = selectData.value!!.toList()
                }
                diaryDAO.insertDiary(diaryEntity)
        }
        }
    }
}