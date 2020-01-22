package com.codinlog.album.util;

import android.Manifest;
import android.net.Uri;
import android.provider.MediaStore;

import com.codinlog.album.R;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class WorthStoreUtil {
    public static String[] needPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};
    public static int[] permissionsDetails = {R.string.permission_storage, R.string.permission_network, R.string.permission_camera};
    public static int permission_RequestCode = 100;
    public static int loaderManager_ID = 200;

    public static Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static String orderRule = MediaStore.Images.Media.DATE_MODIFIED + " desc";
    public static String selectionRule = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "= ?";
    public static String[] imageProjection = {
//            MediaStore.Images.Media._ID,
//            MediaStore.Images.Media.DISPLAY_NAME,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.Media.BUCKET_ID,
//            MediaStore.Images.Media.DATE_TAKEN,
//            MediaStore.Images.Media.DATE_ADDED,
//            MediaStore.Images.Media.HEIGHT,
//            MediaStore.Images.Media.WIDTH,
//            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
    };
    public static String[] selectionArgs = {"image/jpeg", "image/png"};

    public static boolean isFirstScanner = true;
    public static int thumbnailImageNum = 4;
    public static int thumbnailTitleNum = 1;
    public static int no_type = 501;
    public final static int photo_item_type = 501;
    public final static int photo_title_type = 502;
    public final static int photo_isRepeat = 10;
    public final static int photo_isNew = 11;
    public enum MODE{MODE_NORMAL,MODE_SELECT};
}
