package com.codinlog.album.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.widget.kotlin.AlbumImageView;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewVPAdapter extends RecyclerView.Adapter<PhotoPreviewVPAdapter.ViewHolder> {
    private List<PhotoBean> photoBeans;
    private CommonListener commonListener;

    public PhotoPreviewVPAdapter(CommonListener commonListener) {
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageView.setOnClickListener(v -> photoCheckListener.handleEvent(position));
        holder.iv.setCommonListener(commonListener);
        Glide.with(AlbumApplication.mContext).load(getPhotoBeans().get(position).getPhotoPath()).error(R.drawable.ic_photo_black_24dp).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return getPhotoBeans().size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setItemViewCacheSize(0);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.iv != null) {
            holder.iv.initMatrix();
        }
    }

    private List<PhotoBean> getPhotoBeans() {
        if (photoBeans == null)
            photoBeans = new ArrayList<>();
        return photoBeans;
    }

    public void setData(List<PhotoBean> data) {
        this.photoBeans = data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AlbumImageView iv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
