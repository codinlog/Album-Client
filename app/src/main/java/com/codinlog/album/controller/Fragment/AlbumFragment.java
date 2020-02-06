package com.codinlog.album.controller.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import com.codinlog.album.R;
import com.codinlog.album.adapter.kotlin.AlbumRVAdapter;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.AlbumFragmentBinding;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.listener.kotlin.AlbumItemListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.List;

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
        viewModel.getAlbumEntityLiveData().observe(getViewLifecycleOwner(), new Observer<List<AlbumEntity>>() {
            @Override
            public void onChanged(List<AlbumEntity> albumEntities) {
                for (AlbumEntity albumEntity : albumEntities) {
                    Log.d("album", "onChanged: " + albumEntity.toString());
                }
                albumRVAdapter.setAlbumEntities(albumEntities);
                viewModel.mainViewModel.setModeMutableLiveData(WorthStoreUtil.MODE.MODE_NORMAL);
                Toast.makeText(getContext(),"创建成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void doInitData() {
        albumRVAdapter = new AlbumRVAdapter(new AlbumItemListener() {
            @Override
            public void handleEvent(int position) {
                Log.d("album", "handleEvent: " + position);
            }
        });
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(),WorthStoreUtil.albumItemNum));
        binding.rv.setAdapter(albumRVAdapter);
    }
}
