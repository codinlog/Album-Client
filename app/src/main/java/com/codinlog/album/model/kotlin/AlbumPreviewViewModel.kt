package com.codinlog.album.model.kotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.WorthStoreUtil
import com.codinlog.album.util.kotlin.AlbumItemQueryByAlbumIdDBUtil

class AlbumPreviewViewModel : ViewModel() {
    val albumItemDAO: AlbumItemDAO = AlbumDatabase.getInstance().albumItemDAO
    var albumDisplayViewModel: AlbumDisplayViewModel ? = null
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel ? = null
    var currentModelMutableLiveData: MutableLiveData<WorthStoreUtil.MODE> = MutableLiveData()
        get() {
            if (field.value == null)
                field.value = WorthStoreUtil.MODE.MODE_NORMAL
            return field
        }

    fun queryAlbumItemByAlbumId(albumId : Int){
        AlbumItemQueryByAlbumIdDBUtil(albumItemDAO,object : CommonListener(){
            override fun handleEvent(o: Any?) {
                val data = o as List<out AlbumItemEntity>
                albumDisplayViewModel?.setDisplayData(data.map { it.photoBean }.toList())
            }
        }).execute(albumId)
    }
}