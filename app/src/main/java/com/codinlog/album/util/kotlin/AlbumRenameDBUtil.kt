package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.listener.CommonListener

class AlbumRenameDBUtil(private val commonListener: CommonListener, private val oldId: Int, private val albumDAO: AlbumDAO) : AsyncTask<String, Unit, Int>() {
    override fun doInBackground(vararg params: String?): Int {
        return albumDAO.renameAlbum(oldId, params[0].hashCode(), params[0])
    }

    override fun onPostExecute(result: Int?) {
        commonListener.handleEvent(result)
    }
}