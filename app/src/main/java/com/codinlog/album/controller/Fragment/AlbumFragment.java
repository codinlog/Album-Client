package com.codinlog.album.controller.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.kotlin.AlbumRVAdapter;
import com.codinlog.album.controller.Activity.kotlin.AlbumPreviewActivity;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.AlbumFragmentBinding;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.listener.kotlin.AlbumItemListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.util.WorthStoreUtil;

public class AlbumFragment extends BaseFragmentController<AlbumViewModel> {
    private AlbumFragmentBinding binding;
    private AlbumRVAdapter albumRVAdapter;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.album_fragment;
    }

    @Override
    protected void doInitView() {
        viewModel = new ViewModelProvider(getActivity()).get(AlbumViewModel.class);
        binding = (AlbumFragmentBinding) super.binding;
    }

    @Override
    protected void doInitListener() {
        viewModel.getAlbumEntityLiveData().observe(getViewLifecycleOwner(), albumEntities -> albumRVAdapter.setAlbumEntities(albumEntities));
    }

    @Override
    protected void doInitData() {
        albumRVAdapter = new AlbumRVAdapter(new AlbumItemListener() {
            @Override
            public void handleEvent(int position) {
                Log.d("album", "handleEvent: " + position);
                Intent intent = new Intent(getContext(),AlbumPreviewActivity.class);
                intent.putExtra("albumId",viewModel.getAlbumEntityLiveData().getValue().get(position).getAlbumId());
                intent.putExtra("albumName",viewModel.getAlbumEntityLiveData().getValue().get(position).getAlbumName());
                startActivity(intent);
            }
        });
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.albumItemNum));
        binding.rv.setAdapter(albumRVAdapter);
    }
}
