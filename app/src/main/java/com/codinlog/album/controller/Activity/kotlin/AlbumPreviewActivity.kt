package com.codinlog.album.controller.Activity.kotlin

import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.codinlog.album.R
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumPreviewBinding
import com.codinlog.album.model.kotlin.AlbumDisplayViewModel
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
import com.codinlog.album.model.kotlin.AlbumPreviewViewModel
import java.util.*

class AlbumPreviewActivity : BaseActivityController<AlbumPreviewViewModel>(){
    private lateinit var binding: ActivityAlbumPreviewBinding

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }

    override fun doInitVew() {
        viewModel = ViewModelProvider(this).get(AlbumPreviewViewModel::class.java)
        viewModel.albumDisplayViewModel = ViewModelProvider(this).get(AlbumDisplayViewModel::class.java)
        viewModel.albumPhotoSelectViewModel = ViewModelProvider(this).get(AlbumPhotoSelectViewModel::class.java)
        viewModel.albumDisplayViewModel?.albumPreviewViewModel = viewModel
        binding = DataBindingUtil.setContentView<ActivityAlbumPreviewBinding>(this, R.layout.activity_album_preview)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //添加默认的返回图标
        supportActionBar!!.setHomeButtonEnabled(true) //设置返回键可用
    }

    override fun doInitListener() {

    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {

    }

    override fun doInitData() {
        var albumId = intent.getIntExtra("albumId",0)
        supportActionBar!!.title = intent.getStringExtra("albumName")
        viewModel.queryAlbumItemByAlbumId(albumId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.album_add_photo ->{

            }
            R.id.album_slide_play ->{

            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.album_preview,menu)
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }

}