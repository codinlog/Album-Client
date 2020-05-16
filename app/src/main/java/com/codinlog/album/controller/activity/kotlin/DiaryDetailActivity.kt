package com.codinlog.album.controller.activity.kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumDisplayAdapter
import com.codinlog.album.application.AlbumApplication.context
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.controller.activity.PhotoPreviewActivity
import com.codinlog.album.databinding.ActivityDiaryDetailBinding
import com.codinlog.album.entity.kotlin.DiaryEntity
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.DiaryDetailViewModel
import com.codinlog.album.util.DataStore
import java.text.SimpleDateFormat
import java.util.*

class DiaryDetailActivity : BaseActivityController<DiaryDetailViewModel, ActivityDiaryDetailBinding>() {
    private lateinit var albumDisplayAdapter: AlbumDisplayAdapter
    override fun doInitViewData() {
        viewModel = ViewModelProvider(this).get(DiaryDetailViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diary_detail)
    }

    @SuppressLint("SetTextI18n")
    override fun doInitListener() {
        viewModel.displayData.observe(this, androidx.lifecycle.Observer { it ->
            supportActionBar?.title = SimpleDateFormat("yyyy年MM月dd日hh:mm:ss").format(it.diaryId).toString()
            binding.tvTitle.apply {
                text = it.title
                visibility = if("" == it.title) View.GONE else View.VISIBLE
            }
            binding.tvContent.apply {
                text = it.content
                visibility = if("" == it.content) View.GONE else View.VISIBLE
            }
            albumDisplayAdapter = AlbumDisplayAdapter(CommonListener { i ->
                val intent = Intent(context, PhotoPreviewActivity::class.java)
                intent.putExtra("photoBean", viewModel.displayData.value!!.photoBeans[i as Int])
                DataStore.getInstance().allDisplayData = viewModel.displayData.value!!.photoBeans
                startActivity(intent)
            }, CommonListener {

            })
            albumDisplayAdapter.photoBeans = viewModel.displayData.value!!.photoBeans
            binding.rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.rv.adapter = albumDisplayAdapter
        })
    }

    override fun doInitDisplayData() {
        viewModel.displayData.value = intent.getParcelableExtra<DiaryEntity>("diaryEntity")
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

    }
}