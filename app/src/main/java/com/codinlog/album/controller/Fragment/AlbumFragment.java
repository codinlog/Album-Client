package com.codinlog.album.controller.Fragment;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.kotlin.AlbumRVAdapter;
import com.codinlog.album.controller.Activity.kotlin.AlbumPreviewActivity;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.AlbumFragmentBinding;
import com.codinlog.album.listener.kotlin.AlbumItemListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.util.DataStoreUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.Objects;

public class AlbumFragment extends BaseFragmentController<AlbumViewModel,AlbumFragmentBinding> {
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
        viewModel.getAlbumEntityLiveData().observe(getViewLifecycleOwner(), albumEntities -> albumRVAdapter.setAlbumEntities(albumEntities));
    }

    @Override
    public void doInitDisplayData() {
        albumRVAdapter = new AlbumRVAdapter(new AlbumItemListener() {
            @Override
            public void handleEvent(int position) {
                DataStoreUtil.getInstance().setAllDisplayDataList(viewModel.mainViewModel.getPhotoBeansLiveData().getValue());
                Intent intent = new Intent(getContext(), AlbumPreviewActivity.class);
                intent.putExtra("from", "album");
                intent.putExtra("fromValue", viewModel.getAlbumEntityLiveData().getValue().get(position));
                startActivity(intent);
            }
        });
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.albumItemNum));
        binding.rv.setAdapter(albumRVAdapter);
    }
}
