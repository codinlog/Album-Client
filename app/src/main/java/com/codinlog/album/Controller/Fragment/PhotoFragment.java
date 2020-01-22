package com.codinlog.album.Controller.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codinlog.album.Controller.BaseFragmentController;
import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoRecyclerViewAdpater;
import com.codinlog.album.databinding.PhotoFragmentBinding;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.util.WindowUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;

public class PhotoFragment extends BaseFragmentController<PhotoViewModel> {
    private PhotoRecyclerViewAdpater photoRecyclerViewAdpater;

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.photo_fragment;
    }

    @Override
    protected void doInitListener() {
        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Object>>() {
            @Override
            public void onChanged(ArrayList<Object> objects) {
                photoRecyclerViewAdpater.setData(objects);
            }
        });
    }

    @Override
    protected void doInitView() {
        viewModel = ViewModelProviders.of(getActivity()).get(PhotoViewModel.class);
        photoRecyclerViewAdpater = new PhotoRecyclerViewAdpater();
    }

    @Override
    protected void doInitData() {
        PhotoFragmentBinding photoFragmentBinding = (PhotoFragmentBinding) binding;
        photoFragmentBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.thumbnailImageNum));
        photoFragmentBinding.rv.setAdapter(photoRecyclerViewAdpater);
    }
}
