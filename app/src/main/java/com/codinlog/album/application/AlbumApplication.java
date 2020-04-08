package com.codinlog.album.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class AlbumApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = getApplicationContext();
    }
}
