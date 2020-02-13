package com.codinlog.album.util;

import android.util.DisplayMetrics;

import com.codinlog.album.application.AlbumApplication;

import static com.codinlog.album.util.WorthStoreUtil.albumItemNum;
import static com.codinlog.album.util.WorthStoreUtil.albumPhotoItemNum;
import static com.codinlog.album.util.WorthStoreUtil.thumbnailPhotoNum;

public class WindowUtil {
    public static DisplayMetrics dm = AlbumApplication.mApplictaion.getResources().getDisplayMetrics();
    public static int displayWidth = dm.widthPixels;//屏幕宽度
    public static int dispalyHeight = dm.heightPixels;//屏幕高度
    public static int thumbnailImageSize = Math.min(displayWidth / thumbnailPhotoNum,dispalyHeight / thumbnailPhotoNum);
    public static int albumItemSize = Math.min(displayWidth / albumItemNum,dispalyHeight / albumItemNum);
    public static int albumPhotoItemSize = Math.min(displayWidth / albumPhotoItemNum,dispalyHeight / albumPhotoItemNum);
}
