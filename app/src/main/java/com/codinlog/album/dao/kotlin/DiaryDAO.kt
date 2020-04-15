package com.codinlog.album.dao.kotlin

import androidx.lifecycle.LiveData
import androidx.room.*
import com.codinlog.album.entity.kotlin.DiaryEntity

@Dao
interface DiaryDAO {
    @Query("select * from diaryTB order by diaryId desc")
    fun queryAll(): LiveData<DiaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDiary(vararg diaryEntity: DiaryEntity)

    @Delete
    fun deleteDiary(vararg diaryEntity: DiaryEntity)
}