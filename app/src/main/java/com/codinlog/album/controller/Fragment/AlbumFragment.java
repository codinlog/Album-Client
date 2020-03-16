package com.codinlog.album.controller.Fragment;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.kotlin.AlbumRVAdapter;
import com.codinlog.album.controller.Activity.kotlin.AlbumPreviewActivity;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.AlbumFragmentBinding;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.listener.kotlin.AlbumItemListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.util.DataStoreUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlbumFragment extends BaseFragmentController<AlbumViewModel, AlbumFragmentBinding> {
    private AlbumRVAdapter albumRVAdapter;
    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.album_fragment;
    }

    @Override
    public void doInitViewData() {
        viewModel = new ViewModelProvider(getActivity()).get(AlbumViewModel.class);
    }

    @Override
    public void doInitListener() {
        viewModel.getDisplayData().observe(getViewLifecycleOwner(), displayData -> {
            albumRVAdapter.setDisplayData(displayData);
        });
        viewModel.getSelectedData().observe(getViewLifecycleOwner(), o -> {
            viewModel.mainViewModel.setTitle();
        });
        viewModel.getMode().observe(getViewLifecycleOwner(), mode -> {
            if (albumRVAdapter != null)
                albumRVAdapter.setMode(mode);
            if (mode == WorthStoreUtil.MODE.MODE_NORMAL)
                viewModel.resetSelectData();

        });
    }

    @Override
    public void doInitDisplayData() {
        albumRVAdapter = new AlbumRVAdapter(new AlbumItemListener() {
            @Override
            public void handleEvent(int position) {
                if (viewModel.getMode().getValue() == WorthStoreUtil.MODE.MODE_NORMAL) {
                    DataStoreUtil.getInstance().setAllDisplayData(viewModel.mainViewModel.getPhotoBeans().getValue());
                    Intent intent = new Intent(getContext(), AlbumPreviewActivity.class);
                    intent.putExtra("from", "album");
                    intent.putExtra("fromValue", viewModel.getDisplayData().getValue().get(position));
                    startActivity(intent);
                } else {
                    viewModel.setSelectedData(position);
                    albumRVAdapter.notifyItemChanged(position, "payload");
                }
            }
        }, position -> {
            if (viewModel.getMode().getValue() == WorthStoreUtil.MODE.MODE_NORMAL)
                viewModel.mainViewModel.setMode(WorthStoreUtil.MODE.MODE_SELECT);
            viewModel.setSelectedData((int) position);
            albumRVAdapter.notifyItemChanged((int) position, "payload");
        });
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.albumItemNum));
        binding.rv.setAdapter(albumRVAdapter);
    }
}
