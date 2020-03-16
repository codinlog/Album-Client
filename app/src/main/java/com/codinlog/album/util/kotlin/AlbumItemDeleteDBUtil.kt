package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumItemEntity

class AlbumItemDeleteDBUtil(private val albumItemDAO: AlbumItemDAO) : AsyncTask<AlbumItemEntity, Unit, Unit>() {
    override fun doInBackground(vararg items: AlbumItemEntity?) {
        albumItemDAO.deleteAlbumItem(*items)
    }
}