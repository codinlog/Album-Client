package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener

class AlbumItemQueryByAlbumIdDBUtil(private val albumItemDAO: AlbumItemDAO, private val commonListener: CommonListener) : AsyncTask<Int, Unit, List<AlbumItemEntity>>() {
    override fun doInBackground(vararg params: Int?): MutableList<AlbumItemEntity>? {
        return params[0]?.let { albumItemDAO.queryAllAlbumItem(it) }
    }

    override fun onPostExecute(result: List<AlbumItemEntity>?) {
        super.onPostExecute(result)
        commonListener.handleEvent(result)
    }
}