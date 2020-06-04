package com.codinlog.album.adapter.kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.entity.kotlin.DiaryEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.util.Window
import java.text.SimpleDateFormat

class DiaryAdapter(private val btnClickListener: CommonListener, private val detailClickListener: CommonListener) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    var displayData = listOf<DiaryEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.diary_item, parent, false))
    }

    override fun getItemCount(): Int {
        return displayData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diaryEntity = displayData[position]
        holder.tvTime.text = SimpleDateFormat("yyyy年MM月dd日hh:mm:ss").format(diaryEntity.diaryId).toString()
        holder.tvTitle.text = diaryEntity.title
        holder.tvContent.text = diaryEntity.content
        holder.tvContent.setOnClickListener {
            diaryEntity.isExpand = !diaryEntity.isExpand
            updateIndicator(holder, position)
        }
        holder.imgBtn.setOnClickListener { btnClickListener.handleEvent(Pair(holder.imgBtn, diaryEntity)) }
        holder.constraintLayout.setOnClickListener { detailClickListener.handleEvent(diaryEntity) }
        holder.ivLayout.removeAllViews()
        if (diaryEntity.photoBeans.size <= 1) {
            val photoBean = diaryEntity.photoBeans.first()
            if (photoBean.width > 0 && photoBean.height > 0) {
                val scale = Window.displayWidth.toFloat() / photoBean.width
                holder.ivBig.layoutParams = ConstraintLayout.LayoutParams(Window.displayWidth, (photoBean.height * scale).toInt())
            }
            Glide.with(AlbumApplication.context).load(photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).thumbnail(0.5F).into(holder.ivBig)
        } else {
            val photoBean = diaryEntity.photoBeans.first()
            holder.ivBig.layoutParams = ConstraintLayout.LayoutParams(Window.diaryMaxSize, Window.diaryMinSize * 3)
            Glide.with(AlbumApplication.context).load(photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).thumbnail(0.5F).into(holder.ivBig)
            diaryEntity.photoBeans.forEachIndexed { i, p ->
                if (i != 0 && i <= 3) {
                    val imageView = createImageView(p, holder.ivLayout.context, i == 3)
                    holder.ivLayout.addView(imageView)
                } else if (i > 3)
                    return@forEachIndexed
            }
        }
        updateIndicator(holder, position)
    }


    private fun updateIndicator(holder: ViewHolder, position: Int) {
        val diaryEntity = displayData[position]
        if (diaryEntity.isExpand)
            holder.tvContent.maxLines = Int.MAX_VALUE
        else
            holder.tvContent.maxLines = 3
    }

    private fun createImageView(photoBean: PhotoBean, context: Context, lastOne: Boolean): View {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = LinearLayoutCompat.LayoutParams(Window.diaryMinSize, Window.diaryMinSize)
        Glide.with(AlbumApplication.context).load(photoBean.photoPath).error(R.drawable.ic_photo_black_24dp).thumbnail(0.5F).into(imageView)
        if (lastOne)
            imageView.foreground = context.getDrawable(R.drawable.ic_view_module_black_24dp)
        return imageView
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val ivBig: ImageView = view.findViewById(R.id.ivBig)
        val ivLayout: LinearLayout = view.findViewById(R.id.ivLayout)
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintLayout_2)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val imgBtn: ImageButton = view.findViewById(R.id.imgBtn)
    }
}