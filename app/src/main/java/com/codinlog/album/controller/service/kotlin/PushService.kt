package com.codinlog.album.controller.service.kotlin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.codinlog.album.R
import com.codinlog.album.network.kotlin.model.MsgModel
import com.codinlog.album.util.kotlin.AlbumNotification
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PushService : Service() {
    private lateinit var hubConnection: HubConnection
    private lateinit var albumNotification: AlbumNotification
    private lateinit var gson: Gson
    private var isDestroy = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun reconnected() {
        GlobalScope.launch {
            while (true) {
                delay(10000L)
                if (hubConnection.connectionState == HubConnectionState.DISCONNECTED)
                    try {
                        hubConnection.start().blockingAwait()
                    } catch (e: Exception) {

                    }
                else
                    break
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        hubConnection = HubConnectionBuilder.create(connection).build()
        albumNotification = AlbumNotification.newInstance(applicationContext)
        gson = Gson().newBuilder().create()
        initListener()
    }


    private fun initListener() {
        hubConnection.on("pushMsg", { msg: String? ->
            run {
                val msgModel = gson.fromJson(msg, MsgModel::class.java)
                val builder = albumNotification.builder(msgModel.channelId, msgModel.channelName,
                        msgModel.title, msgModel.content, R.drawable.ic_notifications_black_24dp, msgModel.autoCancel)
                albumNotification.notification(msgModel.channelId.hashCode(), builder)
            }
        }, String::class.java)
        hubConnection.onClosed {
            if (!isDestroy) {
                reconnected()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        reconnected()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        isDestroy = true
        hubConnection.stop()
    }

    companion object {
        const val connection = "http://10.0.2.2:5000/hubs/push"
    }
}