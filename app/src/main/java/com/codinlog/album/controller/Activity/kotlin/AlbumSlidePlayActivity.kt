package com.codinlog.album.controller.Activity.kotlin

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumSlidePlayRVAdapter
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumSlidePlayBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.AlbumSlidePlayViewModel
import com.codinlog.album.util.DataStoreUtil
import com.codinlog.album.util.WindowUtil.gallerySize
import kotlinx.android.synthetic.main.activity_album_slide_play.*
import java.lang.Exception
import java.util.ArrayList
import kotlin.properties.Delegates

class AlbumSlidePlayActivity : BaseActivityController<AlbumSlidePlayViewModel, ActivityAlbumSlidePlayBinding>() {
    private var currentPosition: Int by Delegates.observable(0) { _, oldPos, newPos ->
        viewModel.displayData.value.let {
            it?.get(oldPos)?.isSelected = false
            it?.get(newPos)?.isSelected = true
            try {
                albumSlidePlayRVAdapter?.notifyItemChanged(oldPos, "payload")
                albumSlidePlayRVAdapter?.notifyItemChanged(newPos, "payload")
            } catch (e: IndexOutOfBoundsException) {

            } catch (e: Exception) {

            }
        }
    }
    private lateinit var albumSlidePlayRVAdapter: AlbumSlidePlayRVAdapter
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        fullscreen_content.systemUiVisibility =
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
    override fun doInitViewData() {
        viewModel = ViewModelProvider(this).get(AlbumSlidePlayViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_slide_play)
        binding.lifecycleOwner = this
        binding.data = viewModel
        binding.rv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, gallerySize)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.album_slide_play)
        }
        mVisible = true
    }

    override fun doInitListener() {
        fullscreen_content.setOnClickListener { toggle() }
        viewModel.displayData.observe(this, Observer {
            albumSlidePlayRVAdapter.disPlayData = it.also { it[currentPosition].isSelected = true }
        })
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                if (firstVisibleItemPosition > currentPosition)
                    currentPosition = firstVisibleItemPosition
                else if (lastVisibleItemPosition < currentPosition)
                    currentPosition = lastVisibleItemPosition
            }
        })
    }

    override fun doInitDisplayData() {
        albumSlidePlayRVAdapter = AlbumSlidePlayRVAdapter(CommonListener { it ->
            currentPosition = it as Int
        })
        binding.rv.layoutManager = LinearLayoutManager(this, HORIZONTAL, false);
        binding.rv.adapter = albumSlidePlayRVAdapter
        binding.rv.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                super.getItemOffsets(outRect, itemPosition, parent)
                outRect.left = 5
                outRect.right = 5
            }
        })
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
        fullscreen_content.systemUiVisibility =
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
        private const val UI_ANIMATION_DELAY = 300
    }
}
