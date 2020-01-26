package com.codinlog.album.util;

import android.Manifest;
import android.net.Uri;
import android.provider.MediaStore;

import com.codinlog.album.R;

public class WorthStoreUtil {
    public static String[] needPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};
    public static int[] permissionsDetails = {R.string.permission_storage, R.string.permission_network, R.string.permission_camera};
    public static int permission_RequestCode = 100;
    public static int loaderManager_ID = 200;

    public static Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static String orderRule = MediaStore.Images.Media.DATE_MODIFIED + " desc";
    public static String selectionRule = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "= ?";
    public static String[] imageProjection = {
//            MediaStore.Images.Media.DISPLAY_NAME,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.Media.BUCKET_ID,
//            MediaStore.Images.Media.DATE_ADDED,
//            MediaStore.Images.Media.HEIGHT,
//            MediaStore.Images.Media.WIDTH,
//            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,

    };
    public static String[] selectionArgs = {"image/jpeg", "image/png"};
    public static boolean isFirstScanner = true;
    public final static int photoPager = 0;
    public final static int albumPager = 1;
    public final static int timePager = 2;
    public  final static int thumbnailGroupNum = 1;
    public final static int thumbnailPhotoNum = 4;
    public final static int viewHolderNoType = 501;
    public final static int photoItemType = 501;
    public final static int photoGroupType = 502;
    public final static int photoIsRepeat = 10;
    public final static int photoIsNew = 11;
    public final static int errorCode = 404;
    public final static int groupAllSelected = 20;
    public final static int groupNotAllSelected = 30;
    public final static int allGroupSelected = 40;

    public enum MODE {MODE_NORMAL, MODE_SELECT}
}
