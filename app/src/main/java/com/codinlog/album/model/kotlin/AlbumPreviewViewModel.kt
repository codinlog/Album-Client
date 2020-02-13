package com.codinlog.album.model.kotlin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.kotlin.AlbumItemQueryByAlbumIdDBUtil
import java.util.*
import java.util.stream.Collectors

class AlbumPreviewViewModel : ViewModel() {
    val albumItemDAO: AlbumItemDAO = AlbumDatabase.getInstance().albumItemDAO
        get() = field
    var albumDisplayViewModel: AlbumDisplayViewModel ? = null
        get() = field
        set(value) {
            field = value
        }
    var albumPhotoSelectViewModel: AlbumPhotoSelectViewModel ? = null
        get() = field
        set(value) {
            field = value
        }

    fun queryAlbumItemByAlbumId(albumId : Int){
        AlbumItemQueryByAlbumIdDBUtil(albumItemDAO,object : CommonListener(){
            override fun handleEvent(o: Any?) {
                var data = o as List<AlbumItemEntity>
                Log.d("bean","Query")
                albumDisplayViewModel?.setDisplayData(data.stream().map { it.photoBean }.collect(Collectors.toList()))
            }
        }).execute(albumId)
    }
}