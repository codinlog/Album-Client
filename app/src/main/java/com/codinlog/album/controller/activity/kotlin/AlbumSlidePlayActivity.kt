package com.codinlog.album.controller.activity.kotlin

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumSlidePlayRVFullAdapter
import com.codinlog.album.adapter.kotlin.AlbumSlidePlayRVMinAdapter
import com.codinlog.album.controller.BaseActivityController
import com.codinlog.album.databinding.ActivityAlbumSlidePlayBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.AlbumSlidePlayViewModel
import com.codinlog.album.util.DataStore
import com.codinlog.album.util.Window.gallerySize
import kotlinx.android.synthetic.main.activity_album_slide_play.*
import java.util.*
import kotlin.properties.Delegates

class AlbumSlidePlayActivity : BaseActivityController<AlbumSlidePlayViewModel, ActivityAlbumSlidePlayBinding>() {
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    private var currentPosition: Int by Delegates.observable(0) { _, oldPos, newPos ->
        viewModel.displayData.value.let {
            it?.get(oldPos)?.isSelected = false
            it?.get(newPos)?.isSelected = true
            try {
                albumSlidePlayRVMinAdapter.notifyItemChanged(oldPos, "payload")
                albumSlidePlayRVMinAdapter.notifyItemChanged(newPos, "payload")
                binding.vp.currentItem = newPos
                binding.rv.scrollToPosition(newPos)
            } catch (e: IndexOutOfBoundsException) {

            } catch (e: Exception) {

            }
        }
    }
    private lateinit var albumSlidePlayRVFullAdapter: AlbumSlidePlayRVFullAdapter
    private lateinit var albumSlidePlayRVMinAdapter: AlbumSlidePlayRVMinAdapter
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        binding.vp.systemUiVisibility =
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

    override fun doInitViewData() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
        viewModel.displayData.observe(this, Observer {
            albumSlidePlayRVMinAdapter.disPlayData = it.also { it[currentPosition].isSelected = true }
            albumSlidePlayRVFullAdapter.displayData = it
            binding.vp.currentItem = currentPosition
        })
        binding.vp.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            var showToast = false

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                showToast = (state == SCROLL_STATE_DRAGGING)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (showToast && positionOffset <= 0) {
                    viewModel.displayData.value?.let {
                        if (position >= it.size - 1)
                            Toast.makeText(this@AlbumSlidePlayActivity, R.string.last_page, Toast.LENGTH_SHORT).show()
                        else if (position <= 0)
                            Toast.makeText(this@AlbumSlidePlayActivity, R.string.first_page, Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
        albumSlidePlayRVFullAdapter = AlbumSlidePlayRVFullAdapter(CommonListener {
            toggle()
        })
        albumSlidePlayRVMinAdapter = AlbumSlidePlayRVMinAdapter(CommonListener { it ->
            currentPosition = it as Int
        })
        binding.vp.adapter = albumSlidePlayRVFullAdapter
        binding.rv.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        binding.rv.adapter = albumSlidePlayRVMinAdapter
        binding.rv.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                super.getItemOffsets(outRect, itemPosition, parent)
                outRect.left = 5
                outRect.right = 5
            }
        })
        viewModel.setDisplayData(DataStore.getInstance().slidePlayData)
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
        binding.vp.systemUiVisibility =
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
