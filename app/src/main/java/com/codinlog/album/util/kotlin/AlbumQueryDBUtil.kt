package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity

class AlbumQueryDBUtil (albumDAO : AlbumDAO): AsyncTask<Unit, Unit, Unit>() {
    private  val albumDao : AlbumDAO ? = albumDAO
    override fun doInBackground(vararg params: Unit?) {
        albumDao?.queryAllAlbum()
    }
}