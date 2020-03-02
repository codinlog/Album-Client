package com.codinlog.album.controller.Fragment.kotlin

import android.os.Bundle
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
import com.codinlog.album.model.kotlin.AlbumPreviewViewModel
import com.codinlog.album.util.WorthStoreUtil


class AlbumPhotoSelectFragment : BaseFragmentController<AlbumPhotoSelectViewModel>() {
    companion object {
        @JvmStatic
        fun newInstance() =
                AlbumPhotoSelectFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    private var currentPosition = 0

    private lateinit var fragmentPhotoSelectBinding : FragmentPhotoSelectBinding
    private var albumPhotoSelectRVAdapter : AlbumPhotoSelectRVAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_select
    }

    override fun doInitView() {
        fragmentPhotoSelectBinding = super.binding as FragmentPhotoSelectBinding
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
                it.notifyItemChanged(currentPosition,"payload")
                viewModel.albumPreviewViewModel?.setDisplayTitle(AlbumPreviewViewModel.From.SelectPreview)
            }
        })
    }

    override fun doInitData() {
        albumPhotoSelectRVAdapter = AlbumPhotoSelectRVAdapter(CommonListener {
            var position = it as Int
            currentPosition = position
            viewModel.addSelectData(position,null)
        }, object : PhotoGroupListener {
            override fun handleEvent(o: Any) {}
            override fun handleEvent(position: Int, isChecked: Boolean) {
                currentPosition = position
                viewModel.addSelectData(position,isChecked)
            }
        })
        fragmentPhotoSelectBinding.rv.layoutManager = GridLayoutManager(context, WorthStoreUtil.thumbnailPhotoNum)
        fragmentPhotoSelectBinding.rv.adapter = albumPhotoSelectRVAdapter
    }
}
