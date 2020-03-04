package com.codinlog.album.model.kotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.DataStoreUtil
import com.codinlog.album.util.WorthStoreUtil
import com.codinlog.album.util.kotlin.AlbumItemQueryByAlbumIdDBUtil

class AlbumPreviewViewModel : ViewModel() {
    enum class FromWhere {
        None,PhotoPreview, AlbumPreview, SelectPreview
    }

    private val albumItemDAO: AlbumItemDAO = AlbumDatabase.getInstance().albumItemDAO
    private var albumEntity: AlbumEntity? = null
    var fromWhere : FromWhere = FromWhere.None
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
            val displayData = (o as List<out AlbumItemEntity>).map { it.photoBean }.toList()
            albumDisplayViewModel?.setDisplayData(displayData)
            albumPhotoSelectViewModel?.setDisplayData(DataStoreUtil.getInstance().allDisplayDataList.filter { i ->
                displayData.all { t ->
                    t.hashCode() != i.hashCode()
                }
            }.toList())
        }).execute(value.albumId)
    }

    fun setNavController(value: NavController) {
        navControllerMutableLiveData.value = value
    }

    fun setDisplayTitle(value: FromWhere, title: String = "") {
        Log.d("title", title)
        titleMutableLiveData.value = when (value) {
            FromWhere.AlbumPreview -> albumEntity?.albumName
            FromWhere.PhotoPreview -> if ("" == title) titleMutableLiveData.value else title
            FromWhere.SelectPreview -> albumPhotoSelectViewModel?.selectData?.value?.size.toString() + "/" + albumPhotoSelectViewModel?.displayData?.value?.size.toString()
            else -> ""
        }
        Log.d("title", "aaa" + titleMutableLiveData.value)
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