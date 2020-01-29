package com.codinlog.album.adapter;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.listener.BaseListener;
import com.codinlog.album.widget.AlbumImageView;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailVPAdapter extends RecyclerView.Adapter<PhotoDetailVPAdapter.DetailViewHolder> {
    private List<PhotoBean> photoBeans;

    private BaseListener photoCheckListener;

    public PhotoDetailVPAdapter(BaseListener photoCheckListener) {
        this.photoCheckListener = photoCheckListener;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Log.d("value", "onBindViewHolder: " + photoBeans.get(position));
        holder.imageView.setOnClickListener(v -> photoCheckListener.handleEvent(position));
        if (getPhotoBeans().get(position) != null);
            Glide.with(AlbumApplication.mContext).load(getPhotoBeans().get(position).getPath()).error(R.drawable.ic_photo_black_24dp).into(holder.imageView);
    }

    public List<PhotoBean> getPhotoBeans() {
        if (photoBeans == null)
            photoBeans = new ArrayList<>();
        return photoBeans;
    }


    public void setData(List<PhotoBean> data) {
        this.photoBeans = data;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return getPhotoBeans().size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        public AlbumImageView imageView;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
