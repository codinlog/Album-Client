package com.codinlog.album.network.kotlin

import com.codinlog.album.network.kotlin.model.UserModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface IService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("account/login")
    fun Login(@Body userModel: UserModel): Call<JsonObject>

    @HEAD("account/verthen")
    fun verifyAuthen(@Header("Authorization") accessToken: String): Call<Void>
}