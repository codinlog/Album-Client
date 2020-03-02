package com.codinlog.album.model.kotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.DataStoreUtil
import com.codinlog.album.util.WorthStoreUtil
import com.codinlog.album.util.kotlin.AlbumItemQueryByAlbumIdDBUtil

class AlbumPreviewViewModel : ViewModel() {
    enum class From {
        PhotoPreview, AlbumPreview,SelectPreview
    }

    private val albumItemDAO: AlbumItemDAO = AlbumDatabase.getInstance().albumItemDAO
    lateinit var previewDisplayData: List<PhotoBean>
    lateinit var albumEntity: AlbumEntity
    var albumDisplayViewModel: AlbumDisplayViewModel? = null
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel? = null
    var titleMutableLiveData: MutableLiveData<String> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = ""
            return field
        }
    var currentModelMutableLiveData: MutableLiveData<WorthStoreUtil.MODE> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = WorthStoreUtil.MODE.MODE_NORMAL
            return field
        }
    var navControllerMutableLiveData: MutableLiveData<NavController> = MutableLiveData()

    fun queryAlbumItemByAlbumEntity(value: AlbumEntity) {
        albumEntity = value
        AlbumItemQueryByAlbumIdDBUtil(albumItemDAO, CommonListener { o ->
            previewDisplayData = (o as List<out AlbumItemEntity>).map { it.photoBean }.toList()
            albumDisplayViewModel?.setDisplayData(previewDisplayData)
            albumPhotoSelectViewModel?.setDisplayData(DataStoreUtil.getInstance().classifiedPhotoBeanResList.filter { i ->
                //                    for (t in previewDisplayData) {
//                        if (t.hashCode() == i.hashCode())
//                            return@filter false
//                    }
//                    return@filter true
                previewDisplayData.all { t ->
                    t.hashCode() != i.hashCode()
                }
            }.toList())
        }).execute(albumEntity.albumId)
    }

    fun setNavController(value: NavController) {
        navControllerMutableLiveData.value = value
    }

    fun setDisplayTitle(value: From) {
        titleMutableLiveData.value = when (value) {
            From.AlbumPreview -> albumEntity.albumName
            From.PhotoPreview -> ""
            From.SelectPreview -> albumPhotoSelectViewModel?.selectData?.value?.size.toString() + "/" + albumPhotoSelectViewModel?.displayData?.value?.size.toString()
        }
        Log.d("title",titleMutableLiveData.value)
    }

    fun resetSelectData() {
        albumPhotoSelectViewModel?.selectData?.value.let { i ->
            i?.forEach { t ->
                t.isSelected = false
            }
            i?.clear()
        }
    }
}