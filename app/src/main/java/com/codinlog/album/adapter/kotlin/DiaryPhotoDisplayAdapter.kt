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
import com.codinlog.album.util.Window.thumbnailImageSize

class DiaryPhotoDisplayAdapter(private val ivOnClickListener: CommonListener, private val cbOnClickListener: CommonListener) : RecyclerView.Adapter<DiaryPhotoDisplayAdapter.ViewHolder>() {
    var displayData: List<PhotoBean> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoBean = displayData[position]
        holder.iv.setOnClickListener { ivOnClickListener.handleEvent(position) }
        holder.cb.setOnClickListener { cbOnClickListener.handleEvent(Pair(position,holder.cb.isChecked)) }
        updateCb(holder,position)
        Glide.with(AlbumApplication.context).load(photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).thumbnail(0.2f).into(holder.iv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty())
            onBindViewHolder(holder,position)
        else
            updateCb(holder,position)
    }

    private fun updateCb(holder: ViewHolder, position: Int){
        holder.cb.isChecked = displayData[position].isSelected
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val iv: ImageView = view.findViewById(R.id.iv)
        val cb: CheckBox = view.findViewById(R.id.cb)

        init {
            view.layoutParams = ViewGroup.LayoutParams(thumbnailImageSize, thumbnailImageSize)
        }
    }
}