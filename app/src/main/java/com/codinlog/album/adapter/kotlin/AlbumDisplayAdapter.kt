package com.codinlog.album.adapter.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.Window.albumPhotoItemSize
import com.codinlog.album.util.WorthStore

class AlbumDisplayAdapter constructor(private val photoItemOnClickListener: CommonListener,
                                      private val photoItemOnLongClickListener: CommonListener)
    : RecyclerView.Adapter<AlbumDisplayAdapter.ViewHolder>() {
    private var mode: WorthStore.MODE = WorthStore.MODE.MODE_NORMAL
        set(value) {
            field = value
            notifyItemRangeChanged(0, photoBeans.size, "payloads")
        }
    var photoBeans: List<PhotoBean> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_photo_item, parent, false))
    }

    override fun getItemCount(): Int {
        return photoBeans.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.iv.setOnClickListener { photoItemOnClickListener.handleEvent(position) }
        holder.iv.setOnLongClickListener {
            photoItemOnLongClickListener.handleEvent(position)
            return@setOnLongClickListener true
        }
        val photoBean = photoBeans[position]
        if (photoBean.width > 0 && photoBean.height > 0) {
            val scale = albumPhotoItemSize.toFloat() / photoBean.width
            holder.v.layoutParams = ViewGroup.LayoutParams(albumPhotoItemSize, (photoBeans[position].height * scale).toInt())
        }
        Glide.with(AlbumApplication.context).load(photoBeans[position].photoPath).thumbnail(0.4f).error(R.drawable.ic_photo_black_24dp).into(holder.iv)
        doSelectModel(holder, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            doSelectModel(holder, position)
    }

    private fun doSelectModel(holder: ViewHolder, position: Int) {
        when (mode) {
            WorthStore.MODE.MODE_NORMAL -> {
                holder.cb.visibility = View.INVISIBLE
                holder.cb.isChecked = photoBeans[position].isSelected
            }
            WorthStore.MODE.MODE_SELECT -> {
                holder.cb.visibility = View.VISIBLE
                holder.cb.isChecked = photoBeans[position].isSelected
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv: ImageView = view.findViewById(R.id.iv)
        var cb: CheckBox = view.findViewById(R.id.cb)
        var v: View = view.apply { layoutParams = ViewGroup.LayoutParams(albumPhotoItemSize, albumPhotoItemSize) }
    }

}