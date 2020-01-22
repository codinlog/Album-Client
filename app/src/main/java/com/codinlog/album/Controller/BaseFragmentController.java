package com.codinlog.album.Controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.codinlog.album.databinding.PhotoFragmentBinding;

public abstract class BaseFragmentController<T extends ViewModel> extends Fragment {
    protected T viewModel;
    protected ViewDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doInitView();
        doInitListener();
        doInitData();
    }
    protected abstract int getLayoutId();
    protected abstract void doInitListener();
    protected abstract void doInitView();
    protected abstract void doInitData();
}
