package com.codinlog.album.network.kotlin

import com.codinlog.album.network.kotlin.model.UserModel
import com.google.gson.JsonObject
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestService {
    private val retrofit = Retrofit.Builder().baseUrl("http://192.168.1.3:5000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val service = retrofit.create(IService::class.java)
    fun Login(userModel: UserModel, callback: Callback<JsonObject>) {
        service.Login(userModel).enqueue(callback)
    }

    fun verifyAuthen(accessToken: String, callback: Callback<Void>) {
        service.verifyAuthen("Bearer$accessToken").enqueue(callback)
    }

    companion object {
        val requestService = RequestService()
    }
}