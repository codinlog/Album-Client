package com.codinlog.album.adapter.kotlin

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Gallery
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.util.WindowUtil.gallerySize

class AlbumGalleryAdapter: BaseAdapter() {
    var displayData = listOf<PhotoBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getView(i: Int, view: View?, parent: ViewGroup?): View {
        val iv: ImageView
        if (view == null) {
            iv = ImageView(parent?.context)
            iv.layoutParams = Gallery.LayoutParams(gallerySize,gallerySize)
            iv.scaleType = ImageView.ScaleType.FIT_CENTER
        } else
            iv = view as ImageView
        Glide.with(AlbumApplication.mContext).load(displayData[i].photoPath).error(R.drawable.ic_photo_black_24dp).into(iv)
        return iv
    }

    override fun getItem(i: Int): Any {
        return displayData[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return displayData.size
    }
}