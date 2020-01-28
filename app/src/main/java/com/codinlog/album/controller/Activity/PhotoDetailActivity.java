package com.codinlog.album.controller.Activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.codinlog.album.R;
import com.codinlog.album.bean.ClassifiedResBean;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.databinding.ActivityPhotoDetailBindingImpl;
import com.codinlog.album.model.PhotoDetailViewModel;

import java.util.ArrayList;

public class PhotoDetailActivity extends BaseActivityController<PhotoDetailViewModel> {
    ActivityPhotoDetailBindingImpl binding;

    @Override
    protected void doInitVew() {
        viewModel = new ViewModelProvider(this).get(PhotoDetailViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setTitle("11/111");
    }

    @Override
    protected void doInitListener() {

    }

    @Override
    protected void showPermissionDialog(ArrayList<Integer> notAllowPermissions) {

    }

    @Override
    protected void doInitData() {
        viewModel.setPhotoMutableLiveData(ClassifiedResBean.getInstance().getClassifiedPhotoBeanResList());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
