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

class DiaryPhotoSelectRVAdapter(private val commonListener: CommonListener) :RecyclerView.Adapter<DiaryPhotoSelectRVAdapter.ViewHolder>() {
    val displayData = arrayListOf<PhotoBean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.diary_img_item,parent))
    }

    override fun getItemCount(): Int {
       return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivClear.setOnClickListener { commonListener.handleEvent(displayData[position]) }
        Glide.with(AlbumApplication.context).load(displayData[position].photoPath).thumbnail(0.2f).into(holder.iv)
    }

    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view){
        val iv : ImageView = view.findViewById(R.id.iv)
        val ivClear:ImageView = view.findViewById(R.id.ivClear)
    }
}