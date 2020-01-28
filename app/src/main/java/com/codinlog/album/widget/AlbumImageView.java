package com.codinlog.album.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class AlbumImageView extends ImageView {
    public AlbumImageView(Context context) {
        super(context);
    }

    public AlbumImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlbumImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
