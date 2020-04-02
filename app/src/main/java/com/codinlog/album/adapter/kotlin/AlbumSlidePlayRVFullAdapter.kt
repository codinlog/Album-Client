package com.codinlog.album.adapter.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.widget.kotlin.AlbumImageView

class AlbumSlidePlayRVFullAdapter(private val onClickListener: CommonListener)
    : RecyclerView.Adapter<AlbumSlidePlayRVFullAdapter.ViewHolder>() {
    var displayData = listOf<PhotoBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_preview, parent, false))
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.iv.setCommonListener(onClickListener)
        Glide.with(AlbumApplication.mContext).load(displayData[position].photoPath).error(R.drawable.ic_photo_black_24dp).into(holder.iv)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv : AlbumImageView = view.findViewById(R.id.iv)
    }
}