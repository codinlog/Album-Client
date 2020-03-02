package com.codinlog.album.controller.Activity.kotlin

import android.app.AlertDialog
import android.content.Intent
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
import com.codinlog.album.model.kotlin.AlbumDisplayViewModel
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
import com.codinlog.album.model.kotlin.AlbumPreviewViewModel
import java.util.*


class AlbumPreviewActivity : BaseActivityController<AlbumPreviewViewModel>() {
    private lateinit var binding: ActivityAlbumPreviewBinding
    private var menuState: AlbumPreviewViewModel.From = AlbumPreviewViewModel.From.AlbumPreview

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
                    menuState = when (destination.id) {
                        R.id.album_preview -> AlbumPreviewViewModel.From.AlbumPreview
                        R.id.album_photo_select -> AlbumPreviewViewModel.From.SelectPreview
                        else -> AlbumPreviewViewModel.From.AlbumPreview
                    }
                    viewModel.setDisplayTitle(menuState)
                    invalidateOptionsMenu()
                }
            }
        })
        viewModel.titleMutableLiveData.observe(this, androidx.lifecycle.Observer {
            supportActionBar!!.title = it
        })
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {}

    override fun doInitData() {
        when (intent.getStringExtra("from")) {
            "album" -> {
                viewModel.queryAlbumItemByAlbumEntity(intent.getParcelableExtra("albumEntity"))
                viewModel.setDisplayTitle(AlbumPreviewViewModel.From.AlbumPreview)
            }
            "photo" -> {
                viewModel.setDisplayTitle(AlbumPreviewViewModel.From.PhotoPreview)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menuState == AlbumPreviewViewModel.From.AlbumPreview) {
            menu?.findItem(R.id.album_photo_select)?.isVisible = true
            menu?.findItem(R.id.album_slide_play)?.isVisible = true
            menu?.findItem(R.id.album_preview)?.isVisible = false
        } else if (menuState == AlbumPreviewViewModel.From.SelectPreview) {
            menu?.findItem(R.id.album_photo_select)?.isVisible = false
            menu?.findItem(R.id.album_slide_play)?.isVisible = false
            menu?.findItem(R.id.album_preview)?.isVisible = true
        }else if(menuState == AlbumPreviewViewModel.From.PhotoPreview){

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
                R.id.album_preview -> {
                    return onSupportNavigateUp()
                }
                android.R.id.home -> {
                    if (it.currentDestination?.id == R.id.album_preview)
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
        menuInflater.inflate(R.menu.album_preview,menu)
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

    private fun backNotice(it : NavController) {
        if(viewModel.albumPhotoSelectViewModel?.selectData?.value?.size!! > 0){
            var builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.notice))
                    .setCancelable(false)
                    .setMessage(getString(R.string.cancel_select))
                    .setPositiveButton(R.string.btn_ok) { dialog, _ ->
                        dialog.dismiss()
                        it.navigateUp()
                        viewModel.resetSelectData()
                    }
                    .setNegativeButton(R.string.btn_cancel) { dialog, _ ->  dialog.dismiss()}
            builder.show()
        }
        else
            it.navigateUp()
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }
}