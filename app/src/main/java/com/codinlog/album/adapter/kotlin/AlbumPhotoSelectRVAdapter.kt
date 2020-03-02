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
import com.codinlog.album.listener.PhotoGroupListener
import com.codinlog.album.util.WindowUtil

class AlbumPhotoSelectRVAdapter(ivOnClickListener: CommonListener, cbOnClickListener: CommonListener) : RecyclerView.Adapter<AlbumPhotoSelectRVAdapter.ViewHolder>() {
    private var ivOnClickListener = ivOnClickListener
    private var cbOnClickListener = cbOnClickListener as PhotoGroupListener
    var displayData: List<PhotoBean> = arrayListOf()
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
        holder.checkBox.visibility = View.VISIBLE
        holder.checkBox.setOnClickListener { cbOnClickListener.handleEvent(position,holder.checkBox.isChecked) }
        holder.imageView.setOnClickListener { ivOnClickListener.handleEvent(position) }
        doSelectModel(holder, position)
        Glide.with(AlbumApplication.mContext).load(displayData[position].photoPath).error(R.drawable.ic_photo_black_24dp).thumbnail(0.2f).into(holder.imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty())
            onBindViewHolder(holder,position)
        else
            doSelectModel(holder,position)
    }

    fun doSelectModel(holder: ViewHolder, position: Int) {
        holder.checkBox.isChecked = displayData[position].isSelected
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var checkBox: CheckBox = v.findViewById(R.id.checkBox)
        var imageView: ImageView = v.findViewById(R.id.iv)

        init {
            v.layoutParams = ViewGroup.LayoutParams(WindowUtil.thumbnailImageSize, WindowUtil.thumbnailImageSize)
        }
    }
}
