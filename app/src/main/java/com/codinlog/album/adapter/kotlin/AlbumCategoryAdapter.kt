package com.codinlog.album.adapter.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.bean.kotlin.CategoryBean
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.Window

class AlbumCategoryAdapter(private val onClickListener:CommonListener) : RecyclerView.Adapter<AlbumCategoryAdapter.ViewHolder>() {
    var displayData = listOf<CategoryBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_category_item,parent,false))
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.iv.setOnClickListener {onClickListener.handleEvent(displayData[position])}
        holder.tv.text = displayData[position].categroy
        Glide.with(AlbumApplication.context).load(displayData[position].photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).into(holder.iv)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var iv: ImageView = view.findViewById(R.id.iv)
        var tv: TextView = view.findViewById(R.id.tv)
        init {
            view.layoutParams = ViewGroup.LayoutParams(Window.albumItemSize, Window.albumItemSize / 4 * 5)
        }
    }
}