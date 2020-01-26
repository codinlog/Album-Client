package com.codinlog.album.controller.Fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codinlog.album.R;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.model.PhotoDetailViewModel;

public class PhotoDetailFragment extends BaseFragmentController<PhotoDetailViewModel> {

    private PhotoDetailViewModel mViewModel;

    public static PhotoDetailFragment newInstance() {
        return new PhotoDetailFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.photo_detail_fragment;
    }

    @Override
    protected void doInitView() {

    }

    @Override
    protected void doInitListener() {

    }

    @Override
    protected void doInitData() {

    }
}
