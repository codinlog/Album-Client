package com.codinlog.album.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AlbumApplication extends Application {
    public  Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public void Log(String TAG, String MSG){
        Log.d(TAG, "Log: " + MSG);
    }
}
