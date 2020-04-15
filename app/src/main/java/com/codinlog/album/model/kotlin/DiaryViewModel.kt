package com.codinlog.album.model.kotlin

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.model.MainViewModel
import com.codinlog.album.network.kotlin.RequestService
import com.codinlog.album.network.kotlin.model.UserModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    var mainViewModel: MainViewModel? = null
    val diaryDAO = AlbumDatabase.getInstance().diaryDAO
    var loginByUser = false
    var user = MutableLiveData<UserModel>()
    private val requestService = RequestService.requestService

    fun Login(userModel: UserModel?): Unit {
        if (userModel != null) {
            requestService.Login(userModel, object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    if (response.code() == 200) {
                        Log.d("msg", response.body().toString())
                        val userInfo = GsonBuilder().create().fromJson(response.body().toString(), UserModel::class.java)
                        val sharedPreferences = getApplication<AlbumApplication>().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                        val edit = sharedPreferences.edit()
                        edit.putString("username", userInfo.username)
                        edit.putString("password", userInfo.password)
                        edit.putString("email", userInfo.email)
                        edit.putString("accessToken", userInfo.accessToken)
                        edit.apply()
                        user.value = userInfo
                        savedStateHandle.set("userInfo", GsonBuilder().create().toJson(userInfo))
                    } else if (response.code() == 404) {
                        Log.d("msg", "404")
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Log.d("msg", "failure")
                }
            })
        } else {
            var userInfo = GsonBuilder().create().fromJson(savedStateHandle.get<String>("userInfo"), UserModel::class.java)
            if (userInfo == null) {
                val sharedPreferences = getApplication<AlbumApplication>().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                val username = sharedPreferences.getString("username", null)
                val email = sharedPreferences.getString("email", null)
                val password = sharedPreferences.getString("password", null)
                val accessToken = sharedPreferences.getString("accessToken", null)
                userInfo = UserModel(username, email, password, accessToken)
            }
            userInfo.accessToken?.let {
                requestService.verifyAuthen(it, object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {}
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.code() == 200) {
                            user.value = userInfo
                        } else if (response.code() == 401) {
                            Login(userInfo)
                        }
                    }
                })
            }
        }
    }
}