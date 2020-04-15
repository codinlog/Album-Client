package com.codinlog.album.controller.fragment.kotlin

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumDisplayRVAdapter
import com.codinlog.album.controller.BaseFragmentController
import com.codinlog.album.controller.activity.PhotoPreviewActivity
import com.codinlog.album.databinding.FragmentAlbumDisplayBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.AlbumPhotoDisplayViewModel
import com.codinlog.album.util.DataStore
import com.codinlog.album.util.WorthStore


class AlbumPhotoDisplayFragment : BaseFragmentController<AlbumPhotoDisplayViewModel, FragmentAlbumDisplayBinding>() {
    private lateinit var albumDisplayRVAdapter: AlbumDisplayRVAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_album_display
    }

    override fun doInitViewData() {
        viewModel = activity?.let { ViewModelProvider(it).get(AlbumPhotoDisplayViewModel::class.java) }
    }

    override fun doInitListener() {
        viewModel.displayData.observe(viewLifecycleOwner, Observer {
            albumDisplayRVAdapter.photoBeans = it
            viewModel.albumPhotoSelectViewModel?.displayData?.value = DataStore.getInstance().allDisplayData.filter { i ->
                !it.contains(i)
            }.toList()
        })
    }

    override fun doInitDisplayData() {
        albumDisplayRVAdapter = AlbumDisplayRVAdapter(CommonListener {
            when (viewModel.albumPreviewViewModel?.currentModel?.value) {
                WorthStore.MODE.MODE_NORMAL -> {
                    val intent = Intent(context, PhotoPreviewActivity::class.java)
                    intent.putExtra("photoBean", viewModel.displayData.value?.get(it as Int))
                    DataStore.getInstance().allDisplayData = viewModel.displayData.value
                    startActivity(intent)
                }
                WorthStore.MODE.MODE_SELECT -> {

                }
            }
        }, CommonListener {

        })
        binding.rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = albumDisplayRVAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumPhotoDisplayFragment()
    }
}
