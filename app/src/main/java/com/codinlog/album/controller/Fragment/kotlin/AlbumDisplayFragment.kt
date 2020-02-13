package com.codinlog.album.controller.Fragment.kotlin

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codinlog.album.R
import com.codinlog.album.adapter.PhotoRVAdpater
import com.codinlog.album.adapter.kotlin.AlbumDisplayRVAdapter
import com.codinlog.album.controller.BaseFragmentController
import com.codinlog.album.databinding.FragmentAlbumDisplayBinding
import com.codinlog.album.listener.PhotoItemListener
import com.codinlog.album.model.kotlin.AlbumDisplayViewModel


class AlbumDisplayFragment : BaseFragmentController<AlbumDisplayViewModel>() {
    private lateinit var fragmentAlbumDisplayBinding: FragmentAlbumDisplayBinding
    private lateinit var albumDisplayRVAdpater: AlbumDisplayRVAdapter

    companion object {
        @JvmStatic
        fun newInstance() = AlbumDisplayFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_album_display
    }

    override fun doInitView() {
        fragmentAlbumDisplayBinding = super.binding as FragmentAlbumDisplayBinding
        viewModel = activity?.let { ViewModelProvider(it).get(AlbumDisplayViewModel::class.java) }
    }

    override fun doInitListener() {
        viewModel.displayData.observe(viewLifecycleOwner, Observer {
                albumDisplayRVAdpater?.photoBeans = it
        })
    }

    override fun doInitData() {
        albumDisplayRVAdpater = AlbumDisplayRVAdapter(object : PhotoItemListener(){
            override fun handleEvent(position: Int) {
            }

        },object : PhotoItemListener(){
            override fun handleEvent(position: Int) {
            }
        })
        fragmentAlbumDisplayBinding.rv.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        fragmentAlbumDisplayBinding.rv.adapter = albumDisplayRVAdpater
    }
}
