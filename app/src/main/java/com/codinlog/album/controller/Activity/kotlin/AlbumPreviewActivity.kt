package com.codinlog.album.controller.Activity.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.codinlog.album.R
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumPreviewBinding
import com.codinlog.album.model.AlbumViewModel
import com.codinlog.album.model.kotlin.AlbumDisplayViewModel
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
import com.codinlog.album.model.kotlin.AlbumPreviewViewModel
import com.codinlog.album.util.DataStoreUtil
import java.util.*


class AlbumPreviewActivity : BaseActivityController<AlbumPreviewViewModel, ActivityAlbumPreviewBinding>() {
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

    override fun doInitViewData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_preview)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(AlbumPreviewViewModel::class.java)
        viewModel.albumDisplayViewModel = ViewModelProvider(this).get(AlbumDisplayViewModel::class.java)
        viewModel.albumPhotoSelectViewModel = ViewModelProvider(this).get(AlbumPhotoSelectViewModel::class.java)
        viewModel.albumDisplayViewModel?.albumPreviewViewModel = viewModel
        viewModel.albumPhotoSelectViewModel?.albumPreviewViewModel = viewModel
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
                    if(viewModel.fromWhere != AlbumPreviewViewModel.FromWhere.PhotoPreview){
                        viewModel.fromWhere = when (destination.id) {
                            R.id.album_photo_preview -> AlbumPreviewViewModel.FromWhere.AlbumPreview
                            R.id.album_photo_select -> AlbumPreviewViewModel.FromWhere.SelectPreview
                            else -> AlbumPreviewViewModel.FromWhere.None
                        }
                    }
                    viewModel.setDisplayTitle()
                    invalidateOptionsMenu()
                }
            }
        })
        viewModel.titleMutableLiveData.observe(this, androidx.lifecycle.Observer {
            supportActionBar!!.title = it
        })
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {}

    override fun doInitDisplayData() {
        val from = when (intent.getStringExtra("from")) {
            "album" -> From(intent.getParcelableExtra("fromValue"), AlbumPreviewViewModel.FromWhere.AlbumPreview)
            "photo" -> From(intent.getStringExtra("fromValue"), AlbumPreviewViewModel.FromWhere.PhotoPreview)
            else -> From(intent.getStringExtra("fromValue"), AlbumPreviewViewModel.FromWhere.None)
        }
        viewModel.handOutData(from.fromValue,from.fromWhere)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (viewModel.fromWhere) {
            AlbumPreviewViewModel.FromWhere.AlbumPreview -> {
                menu?.findItem(R.id.album_photo_select)?.isVisible = true
                menu?.findItem(R.id.album_slide_play)?.isVisible = true
                menu?.findItem(R.id.album_photo_preview)?.isVisible = false
            }
            AlbumPreviewViewModel.FromWhere.SelectPreview -> {
                menu?.findItem(R.id.album_photo_select)?.isVisible = false
                menu?.findItem(R.id.album_slide_play)?.isVisible = false
                menu?.findItem(R.id.album_photo_preview)?.isVisible = true
            }
            AlbumPreviewViewModel.FromWhere.PhotoPreview -> {
                menu?.findItem(R.id.album_photo_select)?.isVisible = false
                menu?.findItem(R.id.album_slide_play)?.isVisible = true
                menu?.findItem(R.id.album_photo_preview)?.isVisible = false
            }
            else -> {
                menu?.findItem(R.id.album_photo_select)?.isVisible = false
                menu?.findItem(R.id.album_slide_play)?.isVisible = false
                menu?.findItem(R.id.album_photo_preview)?.isVisible = false
            }
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
                R.id.album_photo_preview -> {
                    return onSupportNavigateUp()
                }
                android.R.id.home -> {
                    if (it.currentDestination?.id == R.id.album_photo_preview)
                        finish()
                    backNotice(it)
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.album_preview, menu)
        return true
    }

    override fun onBackPressed() {
        viewModel.navControllerMutableLiveData.value?.let {
            if (it.currentDestination?.id == R.id.album_photo_select) {
                backNotice(it)
                return
            }
        }
        finish()
    }

    private fun backNotice(it: NavController) {
        if (viewModel.albumPhotoSelectViewModel?.selectData?.value?.size!! > 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.notice))
                    .setCancelable(false)
                    .setMessage(getString(R.string.cancel_select))
                    .setPositiveButton(R.string.btn_ok) { dialog, _ ->
                        dialog.dismiss()
                        it.navigateUp()
                        viewModel.resetSelectData()
                    }
                    .setNegativeButton(R.string.btn_cancel) { dialog, _ -> dialog.dismiss() }
            builder.show()
        } else
            it.navigateUp()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }
}

data class From(val fromValue: Any, val fromWhere: AlbumPreviewViewModel.FromWhere)