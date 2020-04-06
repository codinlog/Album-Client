package com.codinlog.album.network.kotlin

import com.google.gson.GsonBuilder
import com.microsoft.signalr.HttpHubConnectionBuilder
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder

interface ISignalRFactory {
   fun getHubConnection(url:String): HubConnection
}