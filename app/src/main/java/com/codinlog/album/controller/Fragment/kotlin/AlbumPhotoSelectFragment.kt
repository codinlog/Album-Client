package com.codinlog.album.controller.Fragment.kotlin

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.codinlog.album.R
import com.codinlog.album.adapter.kotlin.AlbumPhotoSelectRVAdapter
import com.codinlog.album.controller.BaseFragmentController
import com.codinlog.album.databinding.FragmentPhotoSelectBinding
import com.codinlog.album.listener.CommonListener
import com.codinlog.album.model.kotlin.AlbumPhotoSelectViewModel
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

    private lateinit var navController: NavController

    private lateinit var fragmentPhotoSelectBinding : FragmentPhotoSelectBinding
    private var albumPhotoSelectRVAdapter : AlbumPhotoSelectRVAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_select
    }

    override fun doInitView() {
        fragmentPhotoSelectBinding = super.binding as FragmentPhotoSelectBinding
        viewModel = activity?.let { ViewModelProvider(it).get(AlbumPhotoSelectViewModel::class.java) }
        navController = NavHostFragment.findNavController(this)
    }

    override fun doInitListener() {
        viewModel.disPlayData.observe(viewLifecycleOwner, Observer {
            it?.let {
                albumPhotoSelectRVAdapter?.displayData = it
            }
        })
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.album_photo_select)
                destination.label = viewModel.selectData.value?.size.toString() + "/" + viewModel.disPlayData.value?.size.toString()
        }
    }

    override fun doInitData() {
        albumPhotoSelectRVAdapter = AlbumPhotoSelectRVAdapter(object : CommonListener(){
            override fun handleEvent(o: Any?) {

            }
        },object : CommonListener(){
            override fun handleEvent(o: Any?) {

            }
        })
        fragmentPhotoSelectBinding.rv.layoutManager = GridLayoutManager(context, WorthStoreUtil.thumbnailPhotoNum)
        fragmentPhotoSelectBinding.rv.adapter = albumPhotoSelectRVAdapter
    }
}
