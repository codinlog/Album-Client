package com.codinlog.album.util;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.codinlog.album.R;

@RequiresApi(api = Build.VERSION_CODES.Q)
public final class WorthStore {
    public final static int photoPager = 0;
    public final static int albumPager = 1;
    public final static int diaryPager = 2;
    public final static int thumbnailGroupNum = 1;
    public final static int thumbnailPhotoNum = 4;
    public final static int viewHolderNoType = 501;
    public final static int photoItemType = 501;
    public final static int photoGroupType = 502;
    public final static int photoIsRepeat = 10;
    public final static int photoIsNew = 11;
    public final static int errorCode = 404;
    public final static int albumItemNum = 3;
    public final static int REQUEST_TAKE_PHOTO = 101;
    public final static int albumPhotoItemNum = 2;
    public final static String[] needPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE};
    public final static int[] permissionsDetails = {R.string.permission_storage_read, R.string.permission_storage_write, R.string.permission_network, R.string.permission_camera,R.string.permission_access_network_state};
    public final static int permission_RequestCode = 100;
    public final static int loaderManagerId = 200;
    public final static Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public final static String orderRule = MediaStore.Images.Media.DATE_TAKEN + " desc";
    public final static String selectionRule = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "= ?";
    public final static String[] imageProjection = {
//            MediaStore.Images.Media.DISPLAY_NAME,
//            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.Media.BUCKET_ID,
//            MediaStore.Images.Media.DATE_ADDED,
//            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    };
    public final static String[] selectionArgs = {"image/jpeg", "image/png"};
    public final static String[] disAllowScanning = {"amap/data/",};
    public enum MODE {MODE_NORMAL, MODE_SELECT}
}
