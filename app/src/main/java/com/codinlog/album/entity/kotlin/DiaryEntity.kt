package com.codinlog.album.entity.kotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.util.kotlin.DiaryTypeConverter
import java.util.*

@TypeConverters(DiaryTypeConverter::class)
@Entity(tableName = "diaryTB")
class DiaryEntity {
    @PrimaryKey
    @ColumnInfo(name = "diaryId")
    var diaryId = Date().time

    @ColumnInfo(name = "title")
    var title = ""

    @ColumnInfo(name = "content")
    var content = ""

    @ColumnInfo(name = "photoBeans")
    var photoBeans = listOf<PhotoBean>()
}