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
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.Window
import com.codinlog.album.util.WorthStore

class AlbumAdapter(private val albumItemOnClickListener: CommonListener,
                   private val albumItemLongClickListener: CommonListener)
    : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    private var mode = WorthStore.MODE.MODE_NORMAL
    var displayData: List<AlbumEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumEntity = displayData[position]
        holder.tv.text = albumEntity.albumName
        holder.iv.setOnClickListener { albumItemOnClickListener.handleEvent(position) }
        holder.iv.setOnLongClickListener { albumItemLongClickListener.handleEvent(position);return@setOnLongClickListener true; }
        Glide.with(AlbumApplication.context).load(albumEntity.photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).into(holder.iv)
        doSelectMode(holder, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else
            doSelectMode(holder, position)
    }

    private fun doSelectMode(holder: ViewHolder, position: Int) {
        when (mode) {
            WorthStore.MODE.MODE_NORMAL -> holder.ivMask.visibility = View.INVISIBLE
            WorthStore.MODE.MODE_SELECT -> holder.ivMask.visibility = if (displayData[position].isSelect) View.VISIBLE else View.INVISIBLE
        }
    }

    fun setMode(mode: WorthStore.MODE) {
        this.mode = mode
        notifyItemRangeChanged(0, displayData.size, "payload")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv: ImageView
        var tv: TextView
        var ivMask: ImageView

        init {
            view.layoutParams = ViewGroup.LayoutParams(Window.albumItemSize, Window.albumItemSize / 4 * 5)
            iv = view.findViewById(R.id.iv)
            tv = view.findViewById(R.id.tv)
            ivMask = view.findViewById(R.id.ivMask)
        }
    }
}

