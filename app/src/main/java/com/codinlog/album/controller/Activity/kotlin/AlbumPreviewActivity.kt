package com.codinlog.album.controller.Activity.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.codinlog.album.R
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumPreviewBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.AlbumPhotoDisplayViewModel
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
import com.codinlog.album.model.kotlin.AlbumPreviewViewModel
import com.codinlog.album.model.kotlin.FromWhere
import com.codinlog.album.util.DataStoreUtil
import java.util.*


class AlbumPreviewActivity : BaseActivityController<AlbumPreviewViewModel, ActivityAlbumPreviewBinding>() {
    data class From(val fromValue: Any, val fromWhere: FromWhere)

    override fun onStart() {
        super.onStart()
        if (viewModel.navController.value == null) {
            viewModel.setNavController(Navigation.findNavController(binding.fragment))
        }
        overridePendingTransition(R.anim.actitivtyin, R.anim.activityout)
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.navController.value?.let {
            return it.navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    override fun doInitViewData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_preview)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(AlbumPreviewViewModel::class.java)
        viewModel.albumPhotoDisplayViewModel = ViewModelProvider(this).get(AlbumPhotoDisplayViewModel::class.java)
        viewModel.albumPhotoSelectViewModel = ViewModelProvider(this).get(AlbumPhotoSelectViewModel::class.java)
        viewModel.albumPhotoDisplayViewModel?.albumPhotoSelectViewModel = viewModel.albumPhotoSelectViewModel
        viewModel.albumPhotoDisplayViewModel?.albumPreviewViewModel = viewModel
        viewModel.albumPhotoSelectViewModel?.albumPreviewViewModel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }


    override fun doInitListener() {
        viewModel.navController.observe(this, androidx.lifecycle.Observer {
            it?.let {
                it.addOnDestinationChangedListener { _, destination, _ ->
                    if (viewModel.fromWhere != FromWhere.PhotoPreview && viewModel.fromWhere != FromWhere.AlbumFolderPreview) {
                        viewModel.fromWhere = when (destination.id) {
                            R.id.album_confirm -> FromWhere.AlbumPreview
                            R.id.album_add -> FromWhere.SelectPreview
                            else -> FromWhere.None
                        }
                    }
                    viewModel.setDisplayTitle()
                    invalidateOptionsMenu()
                }
            }
        })
        viewModel.title.observe(this, androidx.lifecycle.Observer {
            supportActionBar!!.title = it
        })
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {}

    override fun doInitDisplayData() {
        val from = when (intent.getStringExtra("from")) {
            "album" -> From(intent.getParcelableExtra("fromValue"), FromWhere.AlbumPreview)
            "albumFolder" -> From(intent.getStringExtra("fromValue"), FromWhere.AlbumFolderPreview)
            "photo" -> From(intent.getStringExtra("fromValue"), FromWhere.PhotoPreview)
            else -> From(intent.getStringExtra("fromValue"), FromWhere.None)
        }
        viewModel.handOutData(from.fromValue, from.fromWhere)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (viewModel.fromWhere) {
            FromWhere.AlbumPreview -> {
                menu?.findItem(R.id.album_add)?.isVisible = true
                menu?.findItem(R.id.album_play)?.isVisible = true
                menu?.findItem(R.id.album_confirm)?.isVisible = false
            }
            FromWhere.SelectPreview -> {
                menu?.findItem(R.id.album_add)?.isVisible = false
                menu?.findItem(R.id.album_play)?.isVisible = false
                menu?.findItem(R.id.album_confirm)?.isVisible = true
            }
            FromWhere.PhotoPreview, FromWhere.AlbumFolderPreview -> {
                menu?.findItem(R.id.album_add)?.isVisible = false
                menu?.findItem(R.id.album_play)?.isVisible = true
                menu?.findItem(R.id.album_confirm)?.isVisible = false
            }
            else -> {
                menu?.findItem(R.id.album_add)?.isVisible = false
                menu?.findItem(R.id.album_play)?.isVisible = false
                menu?.findItem(R.id.album_confirm)?.isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.navController.value?.let { it ->
            when (item.itemId) {
                R.id.album_add -> return NavigationUI.onNavDestinationSelected(item, it)
                R.id.album_play -> {
                    DataStoreUtil.getInstance().slidePlayData = viewModel.albumPhotoDisplayViewModel?.displayData?.value
                            ?: listOf()
                    val intent = Intent(this, AlbumSlidePlayActivity::class.java)
                    startActivity(intent)
                }
                R.id.album_confirm -> {
                    viewModel.insertExistAlbumWithPhotoBeans(CommonListener { i ->
                        viewModel.resetSelectData()
                        if ((i as List<Long>).size > 0)
                            Toast.makeText(this@AlbumPreviewActivity, getString(R.string.addto_album_success), Toast.LENGTH_SHORT).show()
                    })
                    return onSupportNavigateUp()
                }
                android.R.id.home -> {
                    if (it.currentDestination?.id == R.id.album_confirm)
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
        viewModel.navController.value?.let {
            if (it.currentDestination?.id == R.id.album_add) {
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