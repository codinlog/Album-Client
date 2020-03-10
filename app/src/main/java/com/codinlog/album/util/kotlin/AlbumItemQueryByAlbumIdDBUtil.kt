package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumItemDAO
import com.codinlog.album.entity.AlbumItemEntity
import com.codinlog.album.listener.CommonListener

class AlbumItemQueryByAlbumIdDBUtil(albumItemDAO: AlbumItemDAO,commonListener: CommonListener) :AsyncTask<Int,Unit,List<AlbumItemEntity>>() {
    private val albumItemDAO = albumItemDAO
    private val commonListener = commonListener
    override fun doInBackground(vararg params: Int?): MutableList<AlbumItemEntity>? {
        return params[0]?.let { albumItemDAO.queryAllAlbumItem(it) }
    }

    override fun onPostExecute(result: List<AlbumItemEntity>?) {
        super.onPostExecute(result)
        commonListener.handleEvent(result)
    }
}