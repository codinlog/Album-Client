package com.codinlog.album.util.kotlin

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.dao.kotlin.CategoryDAO
import com.codinlog.album.entity.kotlin.CategoryEntity
import com.codinlog.album.util.tflite.Classifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumCategory(private val pair: Pair<List<PhotoBean>, List<CategoryEntity>>, private val activity: Activity, private val categoryDAO: CategoryDAO) {
    private val model = Classifier.Model.QUANTIZED
    private val device = Classifier.Device.CPU
    private var classifier: Classifier? = null

    init {
        try {
            classifier?.close()
            classifier = Classifier.create(activity, model, device, 4)
        } catch (ex: Exception) {
            Log.d("err", "create failure")
        }
    }

    fun categoryClassify() {
        if (classifier == null || pair.first == null || pair.second == null)
            return
        GlobalScope.launch {
            val todoList = pair.first.filter { p -> pair.second.all { c -> c.photoId != p.photoId } }.toList()
            val removeList = pair.second.filter { c -> pair.first.all { p -> c.photoId != p.photoId } }.toList()
            val classifiedList = mutableListOf<CategoryEntity>()
            todoList.forEach {
                val bitmap = BitmapFactory.decodeFile(it.photoPath).copy(Bitmap.Config.ARGB_8888, true)
                if (bitmap != null) {
                    val recognitions = classifier!!.recognizeImage(bitmap, 0)
                    if (recognitions[0] != null) {
                        val categoryEntity = CategoryEntity()
                        categoryEntity.photoId = it.photoId
                        if (recognitions[0].confidence >= 0.7)
                            categoryEntity.category = recognitions[0].title
                        else
                            categoryEntity.category = ""
                        classifiedList.add(categoryEntity)
                    }
                }
            }
            withContext(Dispatchers.IO) {
                if (classifiedList.isNotEmpty())
                    categoryDAO.insert(*classifiedList.toTypedArray())
                if (removeList.isNotEmpty())
                    categoryDAO.delete(*removeList.toTypedArray())
            }
        }
    }
}