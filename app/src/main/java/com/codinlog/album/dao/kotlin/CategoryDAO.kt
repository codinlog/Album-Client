package com.codinlog.album.dao.kotlin

import androidx.lifecycle.LiveData
import androidx.room.*
import com.codinlog.album.entity.kotlin.CategoryEntity

@Dao
interface CategoryDAO {
    @Query("select * from categoryTB where category != \"\" order by photoId desc")
    fun queryAll(): LiveData<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg categoryEntity: CategoryEntity): List<Long>

    @Delete
    fun delete(vararg categoryEntity: CategoryEntity): Int
}