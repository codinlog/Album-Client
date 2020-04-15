package com.codinlog.album.util;

import android.util.DisplayMetrics;

import com.codinlog.album.application.AlbumApplication;

import static com.codinlog.album.util.WorthStore.albumItemNum;
import static com.codinlog.album.util.WorthStore.albumPhotoItemNum;
import static com.codinlog.album.util.WorthStore.thumbnailPhotoNum;

public class Window {
    public static DisplayMetrics dm = AlbumApplication.application.getResources().getDisplayMetrics();
    public static int displayWidth = dm.widthPixels;//屏幕宽度
    public static int displayHeight = dm.heightPixels;//屏幕高度
    public static int thumbnailImageSize = Math.min(displayWidth / thumbnailPhotoNum, displayHeight / thumbnailPhotoNum);
    public static int albumItemSize = Math.min(displayWidth / albumItemNum, displayHeight / albumItemNum);
    public static int albumPhotoItemSize = Math.min(displayWidth / albumPhotoItemNum, displayHeight / albumPhotoItemNum);
    public static int gallerySize = displayWidth / 4;
    public static int diaryMinSize = (int) (displayWidth * 0.25);
    public static int diaryMaxSize = (int) (displayWidth * 0.75);
}
