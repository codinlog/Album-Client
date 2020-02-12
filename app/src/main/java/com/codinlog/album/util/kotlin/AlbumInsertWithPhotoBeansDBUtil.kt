package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import android.util.Log
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener

class AlbumInsertWithPhotoBeansDBUtil(albumDAO: AlbumDAO, albumItemDAO: AlbumItemDAO, albumEntity: AlbumEntity,commonListener: CommonListener) : AsyncTask<AlbumItemEntity, Unit, List<Long>>() {
    private var albumDAO: AlbumDAO = albumDAO
    private var albumItemDAO: AlbumItemDAO = albumItemDAO
    private var albumEntity : AlbumEntity = albumEntity
    private var commonListener:CommonListener = commonListener
    override fun doInBackground(vararg params: AlbumItemEntity?): List<Long> {
        for (param in params) {
            Log.d("err",param.toString())
        }
        return albumDAO.insertToAlbumWithPhotoBeans(albumItemDAO,albumEntity, *params)
    }

    override fun onPostExecute(result: List<Long>?) {
        commonListener.handleEvent(result)
    }
}