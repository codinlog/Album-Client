package com.codinlog.album.model.kotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.DataStoreUtil
import com.codinlog.album.util.WorthStoreUtil
import com.codinlog.album.util.kotlin.AlbumExistInsertWithPhotoBeansDBUtil
import kotlinx.coroutines.flow.asFlow
import kotlin.streams.toList

class AlbumPreviewViewModel : ViewModel() {
    private val albumDAO = AlbumDatabase.getInstance().albumDAO
    private val albumItemDAO = AlbumDatabase.getInstance().albumItemDAO
    var fromWhere: FromWhere = FromWhere.None
    var fromValue: Any? = null
    var albumPhotoDisplayViewModel: AlbumPhotoDisplayViewModel? = null
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel? = null
    var title = MutableLiveData<String>().apply { value = "" }
    var currentModel = MutableLiveData<WorthStoreUtil.MODE>().apply { value = WorthStoreUtil.MODE.MODE_NORMAL }
    var navController = MutableLiveData<NavController>()

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
            FromWhere.AlbumFolderPreview -> albumPhotoDisplayViewModel?.displayData = MutableLiveData(DataStoreUtil.getInstance().folderDisplayData)
        }
        setDisplayTitle()
    }

    fun setNavController(value: NavController) {
        navController.value = value
    }

    fun setDisplayTitle() {
        title.value = when (fromWhere) {
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

    fun insertExistAlbumWithPhotoBeans(commonListener: CommonListener?) {
        if (fromValue is AlbumEntity) {
            val albumEntity = fromValue as AlbumEntity
            val photoBeans = albumPhotoSelectViewModel?.selectData?.value
            photoBeans?.let {
                val data = it.map<PhotoBean,AlbumItemEntity> { v ->
                    if (v.tokenDate > albumEntity.photoBean.tokenDate)
                        albumEntity.photoBean = v
                    AlbumItemEntity().apply {
                        belongToId = albumEntity.albumId
                        photoBean = v
                        uuid = (v.photoPath + albumEntity.albumName).hashCode()
                    }
                }.toTypedArray()
                AlbumExistInsertWithPhotoBeansDBUtil(albumDAO, albumItemDAO, albumEntity, commonListener!!).execute(*data)
            }
        }
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

enum class FromWhere {
    None, PhotoPreview, AlbumPreview, SelectPreview, AlbumFolderPreview
}