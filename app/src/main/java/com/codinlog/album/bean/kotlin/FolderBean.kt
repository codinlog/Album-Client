package com.codinlog.album.bean.kotlin

import com.codinlog.album.bean.PhotoBean

class FolderBean(var folderName: String, var folderPath: String, var folderNum: Int = 0, var photoBean: PhotoBean? = null) : Comparable<FolderBean> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FolderBean
        if (folderName != other.folderName) return false
        return true
    }

    override fun hashCode(): Int {
        return folderName.hashCode()
    }

    override fun compareTo(other: FolderBean): Int {
        return this.folderName.compareTo(other.folderName, true)
    }
}