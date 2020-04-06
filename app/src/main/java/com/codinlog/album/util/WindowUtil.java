package com.codinlog.album.util;

import android.util.DisplayMetrics;

import com.codinlog.album.application.AlbumApplication;

import static com.codinlog.album.util.WorthStoreUtil.albumItemNum;
import static com.codinlog.album.util.WorthStoreUtil.albumPhotoItemNum;
import static com.codinlog.album.util.WorthStoreUtil.thumbnailPhotoNum;

public class WindowUtil {
    private static DisplayMetrics dm = AlbumApplication.application.getResources().getDisplayMetrics();
    private static int displayWidth = dm.widthPixels;//屏幕宽度
    private static int displayHeight = dm.heightPixels;//屏幕高度
    public static int thumbnailImageSize = Math.min(displayWidth / thumbnailPhotoNum, displayHeight / thumbnailPhotoNum);
    public static int albumItemSize = Math.min(displayWidth / albumItemNum, displayHeight / albumItemNum);
    public static int albumPhotoItemSize = Math.min(displayWidth / albumPhotoItemNum, displayHeight / albumPhotoItemNum);
    public static int gallerySize = displayWidth / 4 ;
}
