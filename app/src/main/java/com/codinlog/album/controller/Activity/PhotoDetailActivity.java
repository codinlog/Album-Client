package com.codinlog.album.controller.Activity;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoDetailVPAdapter;
import com.codinlog.album.bean.ClassifiedResBean;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.databinding.ActivityPhotoDetailBindingImpl;
import com.codinlog.album.listener.BaseListener;
import com.codinlog.album.model.PhotoDetailViewModel;

import java.util.ArrayList;

public class PhotoDetailActivity extends BaseActivityController<PhotoDetailViewModel> {
    ActivityPhotoDetailBindingImpl binding;
    private PhotoDetailVPAdapter photoDetailVPAdapter;
    private static boolean isShowAppBar = false;
    private TranslateAnimation animation;

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    protected void doInitVew() {
        viewModel = new ViewModelProvider(this).get(PhotoDetailViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setTitle("../...");
    }

    @Override
    protected void doInitListener() {
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                getSupportActionBar().setTitle(String.format(getString(R.string.photo_detail_title), position + 1, viewModel.getClassifiedPhotoBeanResListMutableLiveData().getValue().size()));
            }
        });
        viewModel.getClassifiedPhotoBeanResListMutableLiveData().observe(this, photoBeans -> {
            photoDetailVPAdapter.setData(photoBeans);
            viewModel.setCurrentPositionMutableLiveData(getIntent().getIntExtra("currentPosition", 1));
        });
        viewModel.getCurrentPositionMutableLiveData().observe(this, integer -> binding.viewPager.setCurrentItem(integer, false));
    }

    @Override
    protected void showPermissionDialog(ArrayList<Integer> notAllowPermissions) {

    }

    @Override
    protected void doInitData() {
        photoDetailVPAdapter = new PhotoDetailVPAdapter(new BaseListener() {
            @Override
            public void handleEvent(int position) {
                if (animation == null || animation.hasEnded()) {
                    animation = isShowAppBar ?
                            new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                    -1.0f, Animation.RELATIVE_TO_SELF, 0.0f) :
                            new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                    -1.0f);
                    animation.setDuration(500);
                    binding.appBarLayout.startAnimation(animation);
                    binding.appBarLayout.setVisibility(isShowAppBar ? View.VISIBLE : View.GONE);
                    isShowAppBar = !isShowAppBar;
                }
            }
        });
        binding.viewPager.setAdapter(photoDetailVPAdapter);
        viewModel.setClassifiedPhotoBeanResListMutableLiveData(ClassifiedResBean.getInstance().getClassifiedPhotoBeanResList());
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
