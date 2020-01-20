package com.codinlog.album.util;

import android.Manifest;

import com.codinlog.album.R;

public class WorthStore {
    public static String[] needPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.CAMERA};
    public static int[] permissionsDetails = {R.string.permission_storage,R.string.permission_network,R.string.permission_camera};
    public static int permission_RequestCode = 100;
}
