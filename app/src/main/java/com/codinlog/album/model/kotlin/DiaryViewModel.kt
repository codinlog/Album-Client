package com.codinlog.album.model.kotlin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.codinlog.album.application.AlbumApplication
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
    private var  user = MutableLiveData<UserModel>()
    private val requestService = RequestService.requestService

    init {
        Login(null)
    }

    fun Login(userModel: UserModel?): Boolean {
        if(userModel != null){
            requestService.Login(userModel, object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    if(response.code() == 200){
                        val userTemp = GsonBuilder().create().fromJson(response.body().toString(),UserModel::class.java)
                        val sharedPreferences = getApplication<AlbumApplication>().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                        val edit = sharedPreferences.edit()
                        edit.putString("username",userTemp.username)
                        edit.putString("password",userTemp.password)
                        edit.putString("email",userTemp.email)
                        edit.putString("accessToken",userTemp.accessToken)
                        edit.apply()
                        user.value = userTemp
                        savedStateHandle.set("user",user)
                    }
                    else if(response.code() == 401)
                        Login(null)
                }
                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {}
            })
        }else{
            user = savedStateHandle.getLiveData("user")
            if(user == null){

            }
        }

        return false
    }
}