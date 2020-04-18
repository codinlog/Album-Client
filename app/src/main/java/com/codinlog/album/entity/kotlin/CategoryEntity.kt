package com.codinlog.album.entity.kotlin

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categoryTB")
class CategoryEntity {
    @PrimaryKey
    @ColumnInfo(name = "photoId")
    var photoId = 0

    @ColumnInfo(name = "category")
    var category = ""
}