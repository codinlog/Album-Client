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
import com.codinlog.album.util.WindowUtil.albumPhotoItemSize
import com.codinlog.album.util.WorthStoreUtil

class AlbumDisplayRVAdapter constructor(photoItemOnClickListener: CommonListener,photoItemOnLongClickListener: CommonListener) : RecyclerView.Adapter<AlbumDisplayRVAdapter.ViewHolder>() {
    private var mode: WorthStoreUtil.MODE = WorthStoreUtil.MODE.MODE_NORMAL
        set(value) {
            field = value
            notifyItemRangeChanged(0, photoBeans.size, "payloads")
        }
    var photoBeans: List<PhotoBean> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val photoItemOnClickListener = photoItemOnClickListener
    private val photoItemOnLongClickListener = photoItemOnLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_photo_item, parent, false))
    }

    override fun getItemCount(): Int {
        return photoBeans.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setOnClickListener{ photoItemOnClickListener.handleEvent(position) }
        holder.imageView.setOnLongClickListener{
            photoItemOnLongClickListener.handleEvent(position)
            return@setOnLongClickListener true
        }
        if(photoBeans[position].height > 0 && photoBeans[position].width > 0){
            var scale = albumPhotoItemSize.toFloat() / photoBeans[position].width
            holder.v.layoutParams = ViewGroup.LayoutParams(albumPhotoItemSize,(photoBeans[position].height.toFloat() * scale).toInt())
        }
        Glide.with(AlbumApplication.mContext).load(photoBeans[position].photoPath).thumbnail(0.4f).error(R.drawable.ic_photo_black_24dp).into(holder.imageView)
        doSelectModel(holder, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            doSelectModel(holder, position)
    }

    fun doSelectModel(holder: ViewHolder, position: Int) {
        when (mode) {
            WorthStoreUtil.MODE.MODE_NORMAL -> {
                holder.checkBox.visibility = View.INVISIBLE
                holder.checkBox.isChecked = photoBeans[position].isSelected
            }
            WorthStoreUtil.MODE.MODE_SELECT -> {
                holder.checkBox.visibility = View.VISIBLE
                holder.checkBox.isChecked = photoBeans[position].isSelected
            }
        }
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var imageView: ImageView = v.findViewById(R.id.iv)
        var checkBox: CheckBox = v.findViewById(R.id.checkBox)
        var v: View = v
        init {
            v.layoutParams = ViewGroup.LayoutParams(albumPhotoItemSize,albumPhotoItemSize)
        }
    }

}