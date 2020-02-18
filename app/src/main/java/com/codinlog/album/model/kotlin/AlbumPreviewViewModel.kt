package com.codinlog.album.model.kotlin

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
    enum class FromTo{
        photo_preivew,album_preview,select_preview
    }

    private val albumItemDAO: AlbumItemDAO = AlbumDatabase.getInstance().albumItemDAO
    lateinit var previewDisplayData: List<PhotoBean>
    lateinit var albumEntity: AlbumEntity
    lateinit var title :String
    var albumDisplayViewModel: AlbumDisplayViewModel ? = null
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel ? = null
    var currentModelMutableLiveData: MutableLiveData<WorthStoreUtil.MODE> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = WorthStoreUtil.MODE.MODE_NORMAL
            return field
        }
    var navControllerMutableLiveData : MutableLiveData<NavController> = MutableLiveData()

    fun queryAlbumItemByAlbumEntity(value: AlbumEntity) {
        albumEntity = value
        AlbumItemQueryByAlbumIdDBUtil(albumItemDAO,object : CommonListener(){
            override fun handleEvent(o: Any?) {
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
            }
        }).execute(albumEntity.albumId)
    }

    fun setNavController(value:NavController){
        navControllerMutableLiveData.value = value
    }

    fun setDisplayTitle(value : FromTo){
        title = when(value){
            FromTo.album_preview -> albumEntity.albumName
            FromTo.photo_preivew -> ""
            FromTo.select_preview ->  albumPhotoSelectViewModel?.selectData?.value?.size.toString() + "/" + albumPhotoSelectViewModel?.disPlayData?.value?.size.toString()
            else -> ""
        }
    }
}