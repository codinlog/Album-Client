package com.codinlog.album.controller.Activity.kotlin

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.codinlog.album.R
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumPreviewBinding
import com.codinlog.album.model.kotlin.AlbumDisplayViewModel
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
import com.codinlog.album.model.kotlin.AlbumPreviewViewModel
import java.util.*


class AlbumPreviewActivity : BaseActivityController<AlbumPreviewViewModel>() {
    private lateinit var binding: ActivityAlbumPreviewBinding
    private var menuState: AlbumPreviewViewModel.FromTo = AlbumPreviewViewModel.FromTo.album_preview

    override fun onStart() {
        super.onStart()
        if (viewModel.navControllerMutableLiveData.value == null) {
            viewModel.setNavController(Navigation.findNavController(binding.fragment))
        }
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.navControllerMutableLiveData.value?.let {
            return it.navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    override fun doInitVew() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_preview)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(AlbumPreviewViewModel::class.java)
        viewModel.albumDisplayViewModel = ViewModelProvider(this).get(AlbumDisplayViewModel::class.java)
        viewModel.albumPhotoSelectViewModel = ViewModelProvider(this).get(AlbumPhotoSelectViewModel::class.java)
        viewModel.albumDisplayViewModel?.albumPreviewViewModel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }


    override fun doInitListener() {
        viewModel.navControllerMutableLiveData.observe(this, androidx.lifecycle.Observer {
            it?.let {
                it.addOnDestinationChangedListener { _, destination, _ ->
                    menuState = when (destination.id) {
                        R.id.album_preview -> AlbumPreviewViewModel.FromTo.album_preview
                        R.id.album_photo_select -> AlbumPreviewViewModel.FromTo.select_preview
                        else -> AlbumPreviewViewModel.FromTo.album_preview
                    }
                    viewModel.setDisplayTitle(menuState)
                    supportActionBar?.title = viewModel.title
                    invalidateOptionsMenu()
                }
            }
        })
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {}



    override fun doInitData() {
        when (intent.getStringExtra("from")) {
            "album" -> {
                viewModel.queryAlbumItemByAlbumEntity(intent.getParcelableExtra("albumEntity"))
                viewModel.setDisplayTitle(AlbumPreviewViewModel.FromTo.album_preview)
            }
            "photo" -> {
                viewModel.setDisplayTitle(AlbumPreviewViewModel.FromTo.photo_preivew)
            }
        }
        supportActionBar?.title = viewModel.title
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menuState == AlbumPreviewViewModel.FromTo.album_preview) {
            menu?.findItem(R.id.album_photo_select)?.isVisible = true
            menu?.findItem(R.id.album_slide_play)?.isVisible = true
            menu?.findItem(R.id.finish)?.isVisible = false
        } else if (menuState == AlbumPreviewViewModel.FromTo.select_preview) {
            menu?.findItem(R.id.album_photo_select)?.isVisible = false
            menu?.findItem(R.id.album_slide_play)?.isVisible = false
            menu?.findItem(R.id.finish)?.isVisible = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.navControllerMutableLiveData.value?.let {
            when (item.itemId) {
                R.id.album_photo_select -> return NavigationUI.onNavDestinationSelected(item, it)
                R.id.album_slide_play -> {
                    val intent = Intent(this, AlbumSlidePlayActivity::class.java)
                    startActivity(intent)
                }
                android.R.id.home -> {
                    if (it.currentDestination?.id == R.id.album_preview)
                        finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.album_preview,menu)
        return true
    }

    override fun onBackPressed() {
        viewModel.navControllerMutableLiveData.value?.let {
            if (it.currentDestination?.id == R.id.album_photo_select) {
                it.navigateUp()
                return
            }
        }
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }
}