package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.listener.CommonListener

class AlbumQueryByAlbumIdDBUtil(albumDAO: AlbumDAO, commonListener: CommonListener) : AsyncTask<Int, Unit, AlbumEntity>() {
    private val albumDao: AlbumDAO? = albumDAO
    private val commonListener: CommonListener = commonListener
    override fun doInBackground(vararg params: Int?): AlbumEntity? {
        return params[0]?.let { albumDao?.queryByAlbumId(it) }
    }

    override fun onPostExecute(result: AlbumEntity?) {
        commonListener.handleEvent(result)
    }
}