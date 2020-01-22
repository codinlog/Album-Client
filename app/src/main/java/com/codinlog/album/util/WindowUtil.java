package com.codinlog.album.util;

import android.util.DisplayMetrics;

import com.codinlog.album.application.AlbumApplication;

import static com.codinlog.album.util.WorthStoreUtil.thumbnailImageNum;

public class WindowUtil {
    public static DisplayMetrics dm = AlbumApplication.mApplictaion.getResources().getDisplayMetrics();
    public static int displayWidth = dm.widthPixels;//屏幕宽度
    public static int dispalyHeight = dm.heightPixels;//屏幕高度
    public static int thumbnailImageSize = Math.min(displayWidth / thumbnailImageNum,dispalyHeight / thumbnailImageNum);
}
