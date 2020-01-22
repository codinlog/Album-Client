package com.codinlog.album.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AlbumApplication extends Application {
    public static Context mContext;
    public static Application mApplictaion;


    @Override
    public void onCreate() {
        super.onCreate();
        AlbumApplication.mContext = getApplicationContext();
        AlbumApplication.mApplictaion = this;
    }

    public void Log(String TAG, String MSG){
        Log.d(TAG, "Log: " + MSG);
    }
}
