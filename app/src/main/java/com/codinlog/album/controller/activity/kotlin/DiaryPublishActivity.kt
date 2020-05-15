package com.codinlog.album.controller.activity.kotlin

import android.content.Context
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.DiaryPhotoDisplayAdapter
import com.codinlog.album.adapter.kotlin.DiaryPhotoSelectAdapter
import com.codinlog.album.bean.PhotoBean
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityDiaryPublishBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.DiaryPublicViewModel
import com.codinlog.album.util.DataStore
import com.codinlog.album.util.WorthStore
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class DiaryPublishActivity : BaseActivityController<DiaryPublicViewModel, ActivityDiaryPublishBinding>() {
    private lateinit var diaryPhotoSelectAdapter: DiaryPhotoSelectAdapter
    private lateinit var diaryPhotoDisplayAdapter: DiaryPhotoDisplayAdapter
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private val handle = Handler()
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
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.layout.windowToken, 0)
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.btnPublish.setOnClickListener {
            viewModel.selectData.value?.let {
                if (it.size <= 0)
                    Toast.makeText(this, R.string.must_add_photo, Toast.LENGTH_SHORT).show()
                else {
                    val title = binding.etTitle.text.toString()
                    val content = binding.etContent.text.toString()
                    viewModel.publishDiary(title, content)
                    finish()
                }
            }
        }
        viewModel.displayData.observe(this, androidx.lifecycle.Observer { i ->
            diaryPhotoDisplayAdapter.displayData = i
        })
        viewModel.selectData.observe(this, androidx.lifecycle.Observer { i ->
            diaryPhotoSelectAdapter.displayData = i
        })
    }

    override fun doInitDisplayData() {
        diaryPhotoSelectAdapter = DiaryPhotoSelectAdapter(CommonListener {
            val pairs = it as Pair<Int, PhotoBean>
            val position = pairs.first
            val photoBean = pairs.second
            photoBean.isSelected = false
            viewModel.addSelectData(photoBean)
            diaryPhotoSelectAdapter.notifyItemRemoved(position)
            viewModel.selectData.value?.let { t ->
                diaryPhotoSelectAdapter.notifyItemRangeChanged(position, t.size - position,"payload")
            }
            viewModel.displayData.value?.let { i ->
                diaryPhotoDisplayAdapter.notifyItemChanged(i.indexOf(photoBean), "payload")
            }
        })
        diaryPhotoDisplayAdapter = DiaryPhotoDisplayAdapter(CommonListener { i ->
            val position = i as Int
            viewModel.displayData.value?.get(position)?.let { t ->
                t.isSelected = !t.isSelected
                viewModel.addSelectData(t)
                diaryPhotoDisplayAdapter.notifyItemChanged(position, "payload")
            }
            viewModel.selectData.value = viewModel.selectData.value
        }, CommonListener { i ->
            val pairs = i as Pair<Int, Boolean>
            val photoBean = viewModel.displayData.value?.get(pairs.first)
            val isClicked = pairs.second
            photoBean?.let { t ->
                t.isSelected = isClicked
                viewModel.addSelectData(t)
                diaryPhotoDisplayAdapter.notifyItemChanged(pairs.first, "payload")
            }
            viewModel.selectData.value = viewModel.selectData.value
        })
        rv.layoutManager = GridLayoutManager(this, WorthStore.thumbnailPhotoNum)
        binding.rv.layoutManager = GridLayoutManager(this, WorthStore.thumbnailPhotoNum)
        rv.adapter = diaryPhotoDisplayAdapter
        binding.rv.adapter = diaryPhotoSelectAdapter
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