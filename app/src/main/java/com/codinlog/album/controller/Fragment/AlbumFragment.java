package com.codinlog.album.controller.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.kotlin.AlbumRVAdapter;
import com.codinlog.album.controller.Activity.kotlin.AlbumPreviewActivity;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.AlbumFragmentBinding;
import com.codinlog.album.listener.kotlin.AlbumItemListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.Objects;

public class AlbumFragment extends BaseFragmentController<AlbumViewModel> {
    private AlbumFragmentBinding albumFragmentBinding;
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
        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(AlbumViewModel.class);
        albumFragmentBinding = (AlbumFragmentBinding) super.binding;
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
                Intent intent = new Intent(getContext(),AlbumPreviewActivity.class);
                intent.putExtra("from", "album");
                intent.putExtra("albumEntity", viewModel.getAlbumEntityLiveData().getValue().get(position));
                startActivity(intent);
            }
        });
        albumFragmentBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.albumItemNum));
        albumFragmentBinding.rv.setAdapter(albumRVAdapter);
    }
}
