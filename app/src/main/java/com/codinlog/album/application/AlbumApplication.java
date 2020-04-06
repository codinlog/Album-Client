package com.codinlog.album.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.codinlog.album.network.kotlin.ISignalRFactory;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import org.jetbrains.annotations.NotNull;

public class AlbumApplication extends Application implements ISignalRFactory {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static Application application;


    @Override
    public void onCreate() {
        super.onCreate();
        AlbumApplication.context = getApplicationContext();
        AlbumApplication.application = this;
    }

    @NotNull
    @Override
    public HubConnection getHubConnection(@NotNull String url) {
        return HubConnectionBuilder.create(url).build();
    }
}
