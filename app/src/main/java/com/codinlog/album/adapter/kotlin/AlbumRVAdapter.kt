package com.codinlog.album.adapter.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.entity.AlbumEntity
import com.codinlog.album.listener.kotlin.AlbumItemListener
import com.codinlog.album.util.WindowUtil

class AlbumRVAdapter(albumItemListener: AlbumItemListener) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {
    val albumItemListener: AlbumItemListener = albumItemListener
    var albumEntities: List<AlbumEntity> = listOf()
        get() {
            return field
        }
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return albumEntities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.setText(albumEntities[position].albumName)
        holder.imageView.setOnClickListener { _ -> albumItemListener.handleEvent(position) }
        Glide.with(AlbumApplication.mContext).load(albumEntities[position].photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).into(holder.imageView)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        lateinit var imageView: ImageView
        lateinit var textView: TextView

        constructor(v: View) : super(v) {
            v.layoutParams = ViewGroup.LayoutParams(WindowUtil.albumItemSize, WindowUtil.albumItemSize / 4  * 5)
            imageView = v.findViewById(R.id.imageView)
            textView = v.findViewById(R.id.textView)
        }
    }
}

