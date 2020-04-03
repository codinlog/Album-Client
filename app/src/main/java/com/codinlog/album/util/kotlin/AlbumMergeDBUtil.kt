package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.listener.CommonListener

class AlbumMergeDBUtil(private val albumDAO: AlbumDAO
                       , private val albumItemDAO: AlbumItemDAO
                       , private val albumEntity: AlbumEntity
                       , private val keepOldAlbum: Boolean
                       , private val commonListener: CommonListener)
    : AsyncTask<List<AlbumEntity>, Unit, Boolean>() {
    override fun doInBackground(vararg params: List<AlbumEntity>?): Boolean {
        return albumDAO.mergeAlbum(albumEntity,albumItemDAO,keepOldAlbum,params[0])
    }

    override fun onPostExecute(result: Boolean?) {
        commonListener.handleEvent(result)
    }
}