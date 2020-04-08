package com.codinlog.album.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.codinlog.album.network.kotlin.RequestService;
import com.codinlog.album.network.kotlin.model.UserModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumApplication extends Application{
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
