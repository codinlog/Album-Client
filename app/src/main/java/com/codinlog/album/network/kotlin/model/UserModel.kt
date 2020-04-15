package com.codinlog.album.network.kotlin.model

import com.google.gson.annotations.SerializedName

data class UserModel(@SerializedName("userName") val username: String?,
                     @SerializedName("email") val email: String?,
                     @SerializedName("password") val password: String?,
                     @SerializedName("accessToken") val accessToken: String?)