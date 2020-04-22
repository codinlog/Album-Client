package com.codinlog.album.model.kotlin

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.database.AlbumDatabase
import com.codinlog.album.entity.kotlin.DiaryEntity
import com.codinlog.album.model.MainViewModel
import com.codinlog.album.network.kotlin.RequestService
import com.codinlog.album.network.kotlin.model.UserModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryViewModel(application: Application, private val savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val diaryDAO = AlbumDatabase.getInstance().diaryDAO
    private val requestService = RequestService.requestService
    var user = MutableLiveData<UserModel>()
    var mainViewModel: MainViewModel? = null
    val displayData = diaryDAO.queryAll()
    var loginByUser = false

    fun login(userModel: UserModel?): Unit {
        if (userModel != null) {
            requestService.Login(userModel, object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    if (response.code() == 200) {
                        val userInfo = GsonBuilder().create().fromJson(response.body().toString(), UserModel::class.java)
                        val sharedPreferences = getApplication<AlbumApplication>().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                        sharedPreferences.edit{
                            putString("username", userInfo.username)
                            putString("password", userInfo.password)
                            putString("email", userInfo.email)
                            putString("accessToken", userInfo.accessToken)
                        }
                        user.value = userInfo
                        savedStateHandle.set("userInfo", GsonBuilder().create().toJson(userInfo))
                    } else if (response.code() == 404) {
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {

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
                            login(userInfo)
                        }
                    }
                })
            }
        }
    }

    fun deleteDiary(diaryEntity: DiaryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDAO.deleteDiary(diaryEntity)
        }
    }
}