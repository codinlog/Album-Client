package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.listener.CommonListener

class AlbumUpdateDBUtil (private val albumDAO : AlbumDAO,private val commonListener: CommonListener): AsyncTask<AlbumEntity, Unit, Int>() {
    override fun doInBackground(vararg params: AlbumEntity?):Int {
        return albumDAO.updateAlbum(*params)
    }

    override fun onPostExecute(result: Int?) {
        commonListener.handleEvent(result);
    }
}