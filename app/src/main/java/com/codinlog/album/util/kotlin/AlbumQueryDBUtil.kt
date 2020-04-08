package com.codinlog.album.util.kotlin

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.codinlog.album.dao.AlbumDAO
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.listener.CommonListener

class AlbumQueryDBUtil(private val albumDAO: AlbumDAO, private val commonListener: CommonListener) : AsyncTask<Unit, Unit, LiveData<List<AlbumEntity>>>() {
    override fun doInBackground(vararg p0: Unit?): LiveData<List<AlbumEntity>> {
        return albumDAO.queryAllAlbum()
    }

    override fun onPostExecute(result: LiveData<List<AlbumEntity>>?) {
        commonListener.handleEvent(result?.value);
    }
}