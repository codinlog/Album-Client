package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.util.DataStoreUtil
import com.codinlog.album.util.WorthStoreUtil

class AlbumPreviewViewModel : ViewModel() {
    enum class FromWhere {
        None, PhotoPreview, AlbumPreview, SelectPreview
    }

    private val albumItemDAO: AlbumItemDAO = AlbumDatabase.getInstance().albumItemDAO
    var fromWhere: FromWhere = FromWhere.None
    var fromValue: Any? = null
    var albumPhotoDisplayViewModel: AlbumPhotoDisplayViewModel? = null
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel? = null
    var titleMutableLiveData = MutableLiveData<String>().apply { value = "" }
    var currentModelMutableLiveData = MutableLiveData<WorthStoreUtil.MODE>().apply { value = WorthStoreUtil.MODE.MODE_NORMAL }
    var navControllerMutableLiveData = MutableLiveData<NavController>()

//    private fun queryAlbumItemByAlbumEntity(value: AlbumEntity) {
//        AlbumItemQueryByAlbumIdDBUtil(albumItemDAO, CommonListener { o ->
//            val displayData = (o as List<out AlbumItemEntity>).map { it.photoBean }.toList()
//            //albumDisplayViewModel?.setDisplayData(displayData)
//            albumPhotoSelectViewModel?.setDisplayData(DataStoreUtil.getInstance().allDisplayData.filter { i ->
//                displayData.all { t ->
//                    t.hashCode() != i.hashCode()
//                }
//            }.toList())
//        }).execute(value.albumId)
//    }

    fun handOutData(fromValue: Any, fromWhere: FromWhere) {
        this.fromValue = fromValue
        this.fromWhere = fromWhere
        when (fromWhere) {
            FromWhere.PhotoPreview -> albumPhotoDisplayViewModel?.displayData = MutableLiveData(DataStoreUtil.getInstance().displayData)
            FromWhere.AlbumPreview -> albumPhotoDisplayViewModel?.displayData = albumItemDAO.queryAllAlbumPhotoItem((fromValue as AlbumEntity).albumId)
        }
        setDisplayTitle()
    }

    fun setNavController(value: NavController) {
        navControllerMutableLiveData.value = value
    }

    fun setDisplayTitle() {
        titleMutableLiveData.value = when (fromWhere) {
            FromWhere.SelectPreview -> albumPhotoSelectViewModel?.selectData?.value?.size.toString() + "/" + albumPhotoSelectViewModel?.displayData?.value?.size.toString()
            else -> {
                when (fromValue) {
                    is AlbumEntity -> (fromValue as AlbumEntity).albumName
                    is String -> fromValue
                    else -> ""
                }
            }
        }.toString()
    }

    fun resetSelectData() {
        albumPhotoSelectViewModel?.selectData?.value.let { i ->
            i?.forEach { t ->
                t.isSelected = false
            }
            return@let i?.clear()
        }
    }
}