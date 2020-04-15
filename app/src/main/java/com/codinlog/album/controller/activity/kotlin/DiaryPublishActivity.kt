package com.codinlog.album.controller.activity.kotlin

import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.DiaryPhotoDisplayRVAdapter
import com.codinlog.album.adapter.kotlin.DiaryPhotoSelectRVAdapter
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityDiaryPublishBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.DiaryPublicViewModel
import com.codinlog.album.util.DataStore
import com.codinlog.album.util.WorthStore
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class DiaryPublishActivity : BaseActivityController<DiaryPublicViewModel, ActivityDiaryPublishBinding>() {
    private lateinit var diaryPhotoSelectRVAdapter: DiaryPhotoSelectRVAdapter
    private lateinit var diaryPhotoDisplayRVAdapter: DiaryPhotoDisplayRVAdapter
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var rv: RecyclerView
    override fun doInitViewData() {
        viewModel = ViewModelProvider(this).get(DiaryPublicViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_publish)
        binding.data = viewModel
        binding.lifecycleOwner = this
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        sheetBehavior.isHideable = true
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        rv = findViewById(R.id.rvBottom)
    }

    override fun doInitListener() {
        binding.floatBtn.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.btnPublish.setOnClickListener {

        }
        viewModel.displayData.observe(this, androidx.lifecycle.Observer {i ->
            diaryPhotoDisplayRVAdapter?.let { t ->
                t.displayData = i
            }
        })
    }

    override fun doInitDisplayData() {
        diaryPhotoSelectRVAdapter = DiaryPhotoSelectRVAdapter(CommonListener {  })
        diaryPhotoDisplayRVAdapter = DiaryPhotoDisplayRVAdapter(CommonListener {  }, CommonListener {  })
        rv.layoutManager = GridLayoutManager(this, WorthStore.thumbnailPhotoNum)
        binding.rv.layoutManager = GridLayoutManager(this, WorthStore.thumbnailPhotoNum)
        rv.adapter = diaryPhotoDisplayRVAdapter
        binding.rv.adapter = diaryPhotoSelectRVAdapter
        viewModel.setDisplayData(DataStore.getInstance().allDisplayData)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {}
}