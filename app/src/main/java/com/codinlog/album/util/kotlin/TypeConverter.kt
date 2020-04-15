package com.codinlog.album.util.kotlin

import androidx.room.TypeConverter
import com.codinlog.album.bean.PhotoBean
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class AlbumTypeConverter {
    @TypeConverter
    fun revertDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun converterDate(value: Date): Long {
        return value.time
    }
}

class DiaryTypeConverter{
    @TypeConverter
    fun converterList(photoBeans: List<PhotoBean?>?): String {
        return GsonBuilder().create().toJson(photoBeans)
    }

    @TypeConverter
    fun revertList(photoBeans: String?): List<PhotoBean> {
        val type = object : TypeToken<List<PhotoBean?>?>() {}.type
        return GsonBuilder().create().fromJson(photoBeans, type)
    }
}