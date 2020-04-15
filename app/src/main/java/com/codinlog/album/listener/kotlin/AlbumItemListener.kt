package com.codinlog.album.listener.kotlin

import com.codinlog.album.listener.CommonListener

abstract class AlbumItemListener : CommonListener {
    override fun handleEvent(o: Any?) {}
    abstract fun handleEvent(position: Int)
}