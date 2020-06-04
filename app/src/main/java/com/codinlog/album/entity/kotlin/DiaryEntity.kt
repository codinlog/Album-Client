package com.codinlog.album.entity.kotlin

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.util.kotlin.DiaryTypeConverter
import java.util.*

@TypeConverters(DiaryTypeConverter::class)
@Entity(tableName = "diaryTB")
class DiaryEntity() :Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "diaryId")
    var diaryId = Date().time

    @ColumnInfo(name = "title")
    var title = ""

    @ColumnInfo(name = "content")
    var content = ""

    @ColumnInfo(name = "photoBeans")
    var photoBeans = listOf<PhotoBean>()

    @Ignore
    var isExpand = false

    constructor(parcel: Parcel) : this() {
        diaryId = parcel.readLong()
        title = parcel.readString().toString()
        content = parcel.readString().toString()
        photoBeans = parcel.createTypedArrayList(PhotoBean.CREATOR)!!
    }

    override fun toString(): String {
        return "title:$title,content:$content"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(diaryId)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeTypedList(photoBeans)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiaryEntity> {
        override fun createFromParcel(parcel: Parcel): DiaryEntity {
            return DiaryEntity(parcel)
        }

        override fun newArray(size: Int): Array<DiaryEntity?> {
            return arrayOfNulls(size)
        }
    }
}