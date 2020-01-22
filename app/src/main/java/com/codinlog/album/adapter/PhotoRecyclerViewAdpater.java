package com.codinlog.album.adapter;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codinlog.album.R;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.bean.ImageBean;
import com.codinlog.album.listener.PhotoItemCheckBoxListener;
import com.codinlog.album.listener.PhotoItemOnClickListener;
import com.codinlog.album.listener.PhotoItemOnLongClickListenser;
import com.codinlog.album.util.WindowUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PhotoRecyclerViewAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> arrayList;
    private String TAG = "PhotoRecyclerViewAdpater";
    private WorthStoreUtil.MODE mode = WorthStoreUtil.MODE.MODE_NORMAL;
    private PhotoItemOnLongClickListenser photoItemOnLongClickListenser;
    private PhotoItemOnClickListener photoItemOnClickListener;
    private PhotoItemCheckBoxListener photoItemCheckBoxListener;


    public PhotoRecyclerViewAdpater(PhotoItemOnLongClickListenser photoItemOnLongClickListenser, PhotoItemOnClickListener photoItemOnClickListener, PhotoItemCheckBoxListener photoItemCheckBoxListener) {
        this.photoItemOnLongClickListenser = photoItemOnLongClickListenser;
        this.photoItemOnClickListener = photoItemOnClickListener;
        this.photoItemCheckBoxListener = photoItemCheckBoxListener;
    }

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty())
            onBindViewHolder(holder,position);
        else{
            isSelectMode(holder,position);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PhotoItemViewHolder) {
            ImageBean imageBean = (ImageBean) arrayList.get(position);
            final PhotoItemViewHolder photoItemViewHolder = (PhotoItemViewHolder) holder;
            photoItemViewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    photoItemOnLongClickListenser.handleEvent(position);
                    return true;
                }
            });
            photoItemViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoItemOnClickListener.handleEvent(position);
                }
            });
            photoItemViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoItemCheckBoxListener.handleEvent(position);
                }
            });
            Glide.with(AlbumApplication.mContext).load(imageBean.getPath()).thumbnail(0.1f).error(R.drawable.ic_photo_black_24dp).into(photoItemViewHolder.imageView);
        } else if (holder instanceof PhotoTitleViewHolder) {
            PhotoTitleViewHolder photoTitleViewHolder = (PhotoTitleViewHolder) holder;
            photoTitleViewHolder.textView.setText("2019/12/29");
        }
        isSelectMode(holder, position);
    }

    private void isSelectMode(RecyclerView.ViewHolder holder, int position) {
        switch (mode) {
            case MODE_NORMAL:
                if (holder instanceof PhotoItemViewHolder) {
                    PhotoItemViewHolder photoItemViewHolder = (PhotoItemViewHolder) holder;
                    photoItemViewHolder.checkBox.setVisibility(INVISIBLE);
                } else if (holder instanceof PhotoTitleViewHolder) {

                }
                break;
            case MODE_SELECT:
                if (holder instanceof PhotoItemViewHolder) {
                    PhotoItemViewHolder photoItemViewHolder = (PhotoItemViewHolder) holder;
                    photoItemViewHolder.checkBox.setVisibility(VISIBLE);
                    Object o = arrayList.get(position);
                    if (o instanceof ImageBean) {
                        ImageBean imageBean = (ImageBean) o;
                        photoItemViewHolder.checkBox.setChecked(imageBean.isSelected());
                    }
                } else if (holder instanceof PhotoTitleViewHolder) {

                }
                break;
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

    public void setMode(WorthStoreUtil.MODE mode) {
        this.mode = mode;
        notifyItemRangeChanged(0, arrayList == null ? 0 : arrayList.size(),"payload");
    }

    private class PhotoItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CheckBox checkBox;

        public PhotoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(WindowUtil.thumbnailImageSize, WindowUtil.thumbnailImageSize));
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
