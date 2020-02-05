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
import com.codinlog.album.widget.AlbumImageView;

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
        holder.imageView.setCommonListener(commonListener);
        Glide.with(AlbumApplication.mContext).load(getPhotoBeans().get(position).getPath()).error(R.drawable.ic_photo_black_24dp).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return getPhotoBeans().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public AlbumImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
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
}
