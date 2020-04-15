package com.codinlog.album.adapter.kotlin

import android.util.Log
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
import com.codinlog.album.util.Window.gallerySize

class AlbumSlidePlayRVMinAdapter(private val onClickListener: CommonListener) : RecyclerView.Adapter<AlbumSlidePlayRVMinAdapter.ViewHolder>() {
    var disPlayData = listOf<PhotoBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_slide, parent, false))
    }

    override fun getItemCount(): Int {
        return disPlayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.iv.setOnClickListener { onClickListener.handleEvent(position) }
        if (disPlayData[position].height > 0 && disPlayData[position].width > 0) {
            val scale = gallerySize.toFloat() / disPlayData[position].height
            holder.v.layoutParams = ViewGroup.LayoutParams((disPlayData[position].width.toFloat() * scale).toInt(), gallerySize)
        }
        Glide.with(AlbumApplication.context).load(disPlayData[position].photoPath).thumbnail(0.2f).into(holder.iv)
        doOpIvMask(holder, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            doOpIvMask(holder, position)
    }

    private fun doOpIvMask(holder: ViewHolder, position: Int) {
        Log.d("select", "" + disPlayData[position].isSelected + "" + position)
        holder.ivMask.visibility = if (disPlayData[position].isSelected) View.VISIBLE else View.INVISIBLE
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv: ImageView = itemView.findViewById(R.id.iv)
        var ivMask: ImageView = itemView.findViewById(R.id.ivMask)
        var v = itemView.apply { layoutParams = ViewGroup.LayoutParams(gallerySize, gallerySize) }
    }
}