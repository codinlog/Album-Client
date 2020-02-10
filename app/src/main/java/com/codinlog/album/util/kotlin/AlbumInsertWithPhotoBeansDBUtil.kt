package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumEntity

class AlbumInsertWithPhotoBeansDBUtil(albumDAO: AlbumDAO, albumItemDAO: AlbumItemDAO, albumEntity: AlbumEntity) : AsyncTask<List<PhotoBean>, Unit, Unit>() {
    private var albumDAO: AlbumDAO = albumDAO
    private var albumItemDAO: AlbumItemDAO = albumItemDAO
    private var albumEntity : AlbumEntity = albumEntity;
    override fun doInBackground(vararg params: List<PhotoBean>?): Unit {
        return albumDAO.addToAlbumWithPhotoBeans(albumItemDAO,albumEntity,params[0])
    }
}