package com.codinlog.album.network.kotlin.model

import com.google.gson.annotations.SerializedName

data class UserModel(@SerializedName("UserName")  val username: String,
                     @SerializedName("Email")  val email:String?,
                     @SerializedName("Password")  val password: String,
                     @SerializedName("AccessToken")  val accessToken: String?)