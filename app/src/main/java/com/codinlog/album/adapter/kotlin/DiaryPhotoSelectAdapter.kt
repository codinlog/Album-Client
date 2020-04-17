package com.codinlog.album.adapter.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.Window.diaryPhotoItemSize

class DiaryPhotoSelectAdapter(private val commonListener: CommonListener) : RecyclerView.Adapter<DiaryPhotoSelectAdapter.ViewHolder>() {
    var displayData = listOf<PhotoBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.diary_photo_item, parent,false))
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoBean = displayData[position]
        holder.ivClear.setOnClickListener { commonListener.handleEvent(Pair(position,photoBean)) }
        Glide.with(AlbumApplication.context).load(photoBean.photoPath).thumbnail(0.2f).into(holder.iv)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val iv: ImageView = view.findViewById(R.id.iv)
        val ivClear: ImageView = view.findViewById(R.id.ivClear)
        init {
            view.layoutParams = ViewGroup.LayoutParams(diaryPhotoItemSize,diaryPhotoItemSize)
        }
    }
}