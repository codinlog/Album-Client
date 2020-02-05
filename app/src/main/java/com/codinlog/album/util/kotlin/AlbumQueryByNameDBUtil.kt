package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity

class AlbumQueryByNameDBUtil (albumDAO : AlbumDAO): AsyncTask<String, Unit, Unit>() {
    private  val albumDao : AlbumDAO ? = albumDAO

    override fun doInBackground(vararg params: String?): Unit {
        albumDao?.queryByAlbumName(*params)
    }
}