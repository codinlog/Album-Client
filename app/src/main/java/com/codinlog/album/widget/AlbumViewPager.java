package com.codinlog.album.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class AlbumViewPager extends ViewPager {

    private boolean canScroll = true;
    public AlbumViewPager(@NonNull Context context) {
        super(context);
    }

    public AlbumViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean b) {
        this.canScroll = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!canScroll)
            return false;
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!canScroll)
            return false;
        return super.onInterceptTouchEvent(ev);
    }
}
