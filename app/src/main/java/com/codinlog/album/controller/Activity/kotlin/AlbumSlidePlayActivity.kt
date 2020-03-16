package com.codinlog.album.controller.Activity.kotlin

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumGalleryAdapter
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumSlidePlayBinding
import com.codinlog.album.model.kotlin.AlbumSlidePlayViewModel
import com.codinlog.album.util.DataStoreUtil
import com.codinlog.album.util.WindowUtil.gallerySize
import kotlinx.android.synthetic.main.activity_album_slide_play.*
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AlbumSlidePlayActivity : BaseActivityController<AlbumSlidePlayViewModel, ActivityAlbumSlidePlayBinding>() {
    private lateinit var albumGalleryAdapter: AlbumGalleryAdapter
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        imageSwitcher.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun doInitViewData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_slide_play)
        viewModel = ViewModelProvider(this).get(AlbumSlidePlayViewModel::class.java)
        binding.data = viewModel
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mVisible = true
        imageSwitcher.setFactory {
            val iv = ImageView(this@AlbumSlidePlayActivity)
            iv.scaleType = ImageView.ScaleType.FIT_CENTER
            iv.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
            return@setFactory iv
        }
        gallery.layoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,gallerySize)
        gallery.setSpacing(10)
    }

    override fun doInitListener() {
        viewModel.displayData.observe(this, Observer { i->
            albumGalleryAdapter?.let {t ->
                t.displayData = i
            }
        })
        gallery.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, p3: Long) {
                viewModel.displayData.value?.get(i)?.let {
                    imageSwitcher.setImageURI(Uri.parse(it.photoPath))
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        // Set up the user interaction to manually show or hide the system UI.
        imageSwitcher.setOnClickListener { toggle() }
//        gallery.onItemClickListener{toggle()}

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        //imageSwitcher.setOnTouchListener(mDelayHideTouchListener)
    }

    override fun doInitDisplayData() {
        albumGalleryAdapter = AlbumGalleryAdapter()
        gallery.adapter = albumGalleryAdapter
        viewModel.setDisplayData(DataStoreUtil.getInstance().slidePlayData)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        supportActionBar?.hide()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        gallery.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    override fun showPermissionDialog(notAllowPermissions: ArrayList<Int>?) {}

    companion object {
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

}
