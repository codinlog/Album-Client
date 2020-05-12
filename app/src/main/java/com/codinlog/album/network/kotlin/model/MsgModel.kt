package com.codinlog.album.network.kotlin.model

import com.google.gson.annotations.SerializedName

data class MsgModel(@SerializedName("ChannelId") val channelId:String, @SerializedName("ChannelName") val channelName:String,
                    @SerializedName("Title") val title:String, @SerializedName("Content") val content :String,
                    @SerializedName("SmallIcon") val smallIcon:String, @SerializedName("AutoCancel") val autoCancel:Boolean)