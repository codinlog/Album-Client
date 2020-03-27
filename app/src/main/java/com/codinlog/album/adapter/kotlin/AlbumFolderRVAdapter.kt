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
import com.codinlog.album.bean.kotlin.FolderBean
import com.codinlog.album.listener.CommonListener

class AlbumFolderRVAdapter(private val onClickListener:CommonListener) : RecyclerView.Adapter<AlbumFolderRVAdapter.ViewHolder>() {
    var displayData = listOf<FolderBean>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.folder_item,parent,false))
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(AlbumApplication.mContext).load(displayData[position].photoBean?.photoPath).error(R.drawable.ic_photo_black_24dp).into(holder.iv)
        holder.tvFolder.text = displayData[position].folderName
        holder.tvNum.text = displayData[position].folderNum.toString()
        holder.view.setOnClickListener { onClickListener.handleEvent(displayData[position]) }
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var iv: ImageView = view.findViewById(R.id.iv)
        var tvFolder: TextView = view.findViewById(R.id.tv_folder)
        var tvNum: TextView = view.findViewById(R.id.tv_num)
    }
}