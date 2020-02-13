package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener

class AlbumExistInsertWithPhotoBeansDBUtil(albumDAO: AlbumDAO,albumItemDAO: AlbumItemDAO,albumEntity :AlbumEntity, commonListener: CommonListener) : AsyncTask<AlbumItemEntity, Unit, List<Long>>(){
    private val albumDAO = albumDAO
    private val albumItemDAO = albumItemDAO
    private val albumEntity = albumEntity
    private val commonListener = commonListener
    override fun doInBackground(vararg params: AlbumItemEntity?): List<Long> {
        return albumDAO.insertToExistAlbumWithPhotoBeans(albumItemDAO,albumEntity,*params)
    }

    override fun onPostExecute(result: List<Long>?) {
        commonListener.handleEvent(result)
    }
}