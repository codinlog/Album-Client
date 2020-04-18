package com.codinlog.album.util.kotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class AlbumNotification(private val context: Context) {
    private var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        fun newInstance(context: Context): AlbumNotification {
            return AlbumNotification(context)
        }
    }

    fun builder(channelId: String, channelName: String, title: String, content: String, smallIcon: Int, autoCancel: Boolean): Notification.Builder {
        val builder = Notification.Builder(context, channelId)
                .setAutoCancel(autoCancel)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIcon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        return builder
    }

    fun notification(id: Int, builder: Notification.Builder) {
        notificationManager.notify(id,builder.build())
    }
}