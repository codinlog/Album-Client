package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity

class AlbumUpdateDBUtil (private val albumDAO : AlbumDAO): AsyncTask<AlbumEntity, Unit, Unit>() {
    override fun doInBackground(vararg params: AlbumEntity?) {
        albumDAO.updateAlbum(*params)
    }
}