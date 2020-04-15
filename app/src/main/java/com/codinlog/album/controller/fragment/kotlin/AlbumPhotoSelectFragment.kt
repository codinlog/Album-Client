package com.codinlog.album.controller.fragment.kotlin

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumPhotoSelectRVAdapter
import com.codinlog.album.controller.BaseFragmentController
import com.codinlog.album.databinding.FragmentPhotoSelectBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.listener.PhotoGroupListener
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
import com.codinlog.album.util.WorthStore


class AlbumPhotoSelectFragment : BaseFragmentController<AlbumPhotoSelectViewModel, FragmentPhotoSelectBinding>() {
    private var clickPosition = 0
    private var albumPhotoSelectRVAdapter: AlbumPhotoSelectRVAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_select
    }

    override fun doInitViewData() {
        viewModel = activity?.let { ViewModelProvider(it).get(AlbumPhotoSelectViewModel::class.java) }
    }

    override fun doInitListener() {
        viewModel.displayData.observe(viewLifecycleOwner, Observer {
            it?.let {
                albumPhotoSelectRVAdapter?.displayData = it
            }
        })
        viewModel.selectData.observe(viewLifecycleOwner, Observer {
            albumPhotoSelectRVAdapter?.let {
                it.notifyItemChanged(clickPosition, "payload")
                viewModel.albumPreviewViewModel?.setDisplayTitle()
            }
        })
    }

    override fun doInitDisplayData() {
        albumPhotoSelectRVAdapter = AlbumPhotoSelectRVAdapter(CommonListener {
            clickPosition = it as Int
            viewModel.addSelectData(clickPosition, null)
        }, object : PhotoGroupListener {
            override fun handleEvent(o: Any) {}
            override fun handleEvent(position: Int, isChecked: Boolean) {
                clickPosition = position
                viewModel.addSelectData(position, isChecked)
            }
        })
        binding.rv.layoutManager = GridLayoutManager(context, WorthStore.thumbnailPhotoNum)
        binding.rv.adapter = albumPhotoSelectRVAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumPhotoSelectFragment()
    }
}
