package com.codinlog.album.util.kotlin

import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.bean.kotlin.CategoryBean
import com.codinlog.album.entity.kotlin.CategoryEntity

class Classify {
    companion object {
        fun PhotoBeansCategoryClassify(pair: Pair<List<PhotoBean>, List<CategoryEntity>>): Map<CategoryBean, List<PhotoBean>>? {
            if (pair.first == null || pair.second == null)
                return null
            val map =  pair.second.mapNotNull { i ->
                val photoBean = pair.first.find { t -> t.photoId == i.photoId }
                if (photoBean != null)
                    photoBean.category = i.category
                photoBean
            }.sortedBy { it.tokenDate }.groupBy { it.category }.map {
                mapOf(Pair(CategoryBean(it.value.first(),it.key),it.value))
            }
            return map.first()
        }
    }
}