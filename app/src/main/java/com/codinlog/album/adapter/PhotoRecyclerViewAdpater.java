package com.codinlog.album.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.bean.ImageBean;
import com.codinlog.album.util.WindowUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.io.File;
import java.util.ArrayList;

public class PhotoRecyclerViewAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> arrayList;
    private String TAG ="PhotoRecyclerViewAdpater";

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == WorthStoreUtil.photo_item_type) {
            PhotoItemViewHolder photoItemViewHolder = new PhotoItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_image_item, parent, false));
            return photoItemViewHolder;
        } else if (viewType == WorthStoreUtil.photo_title_type) {
            PhotoTitleViewHolder photoTitleViewHolder = new PhotoTitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_title, parent, false));
            return photoTitleViewHolder;
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoItemViewHolder) {
            ImageBean imageBean = (ImageBean) arrayList.get(position);
            Log.d(TAG, "onBindViewHolder: " + imageBean.getPath());
            Glide.with(AlbumApplication.mContext).load(imageBean.getPath()).thumbnail(0.1f).error(R.drawable.ic_photo_black_24dp).into(((PhotoItemViewHolder) holder).imageView);
        } else if (holder instanceof PhotoTitleViewHolder) {
            ((PhotoTitleViewHolder) holder).textView.setText("2019/12/29");
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = arrayList.get(position);
        if (o instanceof ImageBean)
            return WorthStoreUtil.photo_item_type;
        else if (o instanceof String)
            return WorthStoreUtil.photo_title_type;
        else
            return WorthStoreUtil.no_type;
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case WorthStoreUtil.photo_title_type:
                            Log.d("size", "getSpanSize: " + WorthStoreUtil.thumbnailTitleNum);
                            return WorthStoreUtil.thumbnailImageNum;
                        default:
                            Log.d("size", "getSpanSize: " + WorthStoreUtil.thumbnailImageNum);
                            return WorthStoreUtil.thumbnailTitleNum;
                    }
                }
            });
        }
    }

    public void setData(ArrayList<Object> objects) {
        this.arrayList = objects;
        notifyDataSetChanged();
    }

    private class PhotoItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CheckBox checkBox;

        public PhotoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(WindowUtil.thumbnailImageSize,WindowUtil.thumbnailImageSize));
            imageView = itemView.findViewById(R.id.iv);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    private class PhotoTitleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageButton imageButton;

        public PhotoTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            imageButton = itemView.findViewById(R.id.imageBtn);
        }
    }
}
