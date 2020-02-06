package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity

class AlbumUpdateDBUtil (albumDAO : AlbumDAO): AsyncTask<AlbumEntity, Unit, Unit>() {
    private  val albumDao : AlbumDAO ? = albumDAO
    override fun doInBackground(vararg params: AlbumEntity?) {
        albumDao?.updateAlbum(*params)
    }
}