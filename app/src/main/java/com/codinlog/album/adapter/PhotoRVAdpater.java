package com.codinlog.album.adapter;

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
import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.listener.PhotoGroupListener;
import com.codinlog.album.listener.PhotoItemListener;
import com.codinlog.album.util.WindowUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PhotoRVAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> objectClassifiedResList;
    private WorthStoreUtil.MODE mode = WorthStoreUtil.MODE.MODE_NORMAL;
    private PhotoItemListener photoItemIVOnLongListener, photoItemIVOnClickListener,photoItemCheckBoxListener;
    private PhotoGroupListener photoGroupCheckBoxListener, photoGroupTVOnLongListener;

    public PhotoRVAdpater(PhotoItemListener photoItemIVOnLongListener, PhotoItemListener photoItemIVOnClickListener, PhotoItemListener photoItemCheckBoxListener,
                          PhotoGroupListener photoGroupCheckBoxListener, PhotoGroupListener photoGroupTVOnLongListener) {
        this.photoItemIVOnLongListener = photoItemIVOnLongListener;
        this.photoItemIVOnClickListener = photoItemIVOnClickListener;
        this.photoItemCheckBoxListener = photoItemCheckBoxListener;
        this.photoGroupCheckBoxListener = photoGroupCheckBoxListener;
        this.photoGroupTVOnLongListener = photoGroupTVOnLongListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == WorthStoreUtil.photoItemType) {
            PhotoItemViewHolder photoItemViewHolder = new PhotoItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false));
            return photoItemViewHolder;
        } else if (viewType == WorthStoreUtil.photoGroupType) {
            PhotoGroupViewHolder photoGroupViewHolder = new PhotoGroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_title, parent, false));
            return photoGroupViewHolder;
        } else
            return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else
            doSelectMode(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PhotoItemViewHolder) {
            PhotoBean photoBean = (PhotoBean) objectClassifiedResList.get(position);
            PhotoItemViewHolder photoItemViewHolder = (PhotoItemViewHolder) holder;
            photoItemViewHolder.imageView.setOnLongClickListener(v -> {
                photoItemIVOnLongListener.handleEvent(position);
                return true;
            });
            photoItemViewHolder.imageView.setOnClickListener(v -> photoItemIVOnClickListener.handleEvent(position));
            photoItemViewHolder.checkBox.setOnClickListener(v -> photoItemCheckBoxListener.handleEvent(position));
            Glide.with(AlbumApplication.mContext).load(photoBean.getPhotoPath()).thumbnail(0.1f).error(R.drawable.ic_photo_black_24dp).into(photoItemViewHolder.imageView);
        } else if (holder instanceof PhotoGroupViewHolder) {
            final PhotoGroupViewHolder photoGroupViewHolder = (PhotoGroupViewHolder) holder;
            GroupBean groupBean = (GroupBean) objectClassifiedResList.get(position);
            photoGroupViewHolder.textView.setText(groupBean.getGroupId());
            photoGroupViewHolder.checkBox.setOnClickListener(v -> (photoGroupCheckBoxListener).handleEvent(position,((CheckBox)v).isChecked()));
            photoGroupViewHolder.textView.setOnLongClickListener(v -> {
                CheckBox checkBox = ((View)v.getParent()).findViewById(R.id.checkBox);
                (photoGroupTVOnLongListener).handleEvent(position,checkBox.isChecked());
                return true;
            });
        }
        doSelectMode(holder, position);
    }

    private void doSelectMode(RecyclerView.ViewHolder holder, int position) {
        switch (mode) {
            case MODE_NORMAL:
                if (holder instanceof PhotoItemViewHolder) {
                    PhotoItemViewHolder photoItemViewHolder = (PhotoItemViewHolder) holder;
                    photoItemViewHolder.checkBox.setVisibility(INVISIBLE);
                    Object o = objectClassifiedResList.get(position);
                    if (o instanceof PhotoBean) {
                        PhotoBean photoBean = (PhotoBean) o;
                        photoItemViewHolder.checkBox.setChecked(photoBean.isSelected());
                    }
                } else if (holder instanceof PhotoGroupViewHolder) {
                    PhotoGroupViewHolder photoGroupViewHolder = (PhotoGroupViewHolder) holder;
                    photoGroupViewHolder.checkBox.setVisibility(INVISIBLE);
                    Object o = objectClassifiedResList.get(position);
                    if(o instanceof GroupBean){
                        GroupBean groupBean = (GroupBean)o;
                        photoGroupViewHolder.checkBox.setChecked(groupBean.isSelected());
                    }
                }
                break;
            case MODE_SELECT:
                if (holder instanceof PhotoItemViewHolder) {
                    PhotoItemViewHolder photoItemViewHolder = (PhotoItemViewHolder) holder;
                    photoItemViewHolder.checkBox.setVisibility(VISIBLE);
                    Object o = objectClassifiedResList.get(position);
                    if (o instanceof PhotoBean) {
                        PhotoBean photoBean = (PhotoBean) o;
                        photoItemViewHolder.checkBox.setChecked(photoBean.isSelected());
                    }
                } else if (holder instanceof PhotoGroupViewHolder) {
                    PhotoGroupViewHolder photoGroupViewHolder = (PhotoGroupViewHolder) holder;
                    photoGroupViewHolder.checkBox.setVisibility(VISIBLE);
                    Object o = objectClassifiedResList.get(position);
                    if(o instanceof GroupBean){
                        GroupBean groupBean = (GroupBean)o;
                        photoGroupViewHolder.checkBox.setChecked(groupBean.isSelected());
                    }
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = objectClassifiedResList.get(position);
        if (o instanceof PhotoBean)
            return WorthStoreUtil.photoItemType;
        else if (o instanceof GroupBean)
            return WorthStoreUtil.photoGroupType;
        else
            return WorthStoreUtil.viewHolderNoType;
    }

    @Override
    public int getItemCount() {
        return objectClassifiedResList == null ? 0 : objectClassifiedResList.size();
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
                        case WorthStoreUtil.photoGroupType:
                            return WorthStoreUtil.thumbnailPhotoNum;
                        default:
                            return WorthStoreUtil.thumbnailGroupNum;
                    }
                }
            });
        }
    }

    public void setData(List<Object> objects) {
        this.objectClassifiedResList = objects;
        notifyDataSetChanged();
    }

    public void setMode(WorthStoreUtil.MODE mode) {
        this.mode = mode;
        notifyChange(null,true);
    }
    public void notifyChange(Integer position,boolean changeAll){
        if(changeAll)
            notifyItemRangeChanged(0, objectClassifiedResList == null ? 0 : objectClassifiedResList.size(), "payload");
        else
            notifyItemChanged(position);
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

    private class PhotoGroupViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageButton imageButton;
        public CheckBox checkBox;

        public PhotoGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            imageButton = itemView.findViewById(R.id.imageBtn);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
