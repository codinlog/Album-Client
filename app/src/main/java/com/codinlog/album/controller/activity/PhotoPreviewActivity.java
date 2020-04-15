package com.codinlog.album.controller.activity;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoPreviewVPAdapter;
import com.codinlog.album.anim.ZoomOutPageTransformer;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.databinding.ActivityPhotoPreviewBinding;
import com.codinlog.album.model.PhotoPreviewViewModel;
import com.codinlog.album.util.DataStore;

import java.util.ArrayList;

public class PhotoPreviewActivity extends BaseActivityController<PhotoPreviewViewModel, ActivityPhotoPreviewBinding> {
    private static boolean isShowAppBar = false;
    private ActivityPhotoPreviewBinding binding;
    private PhotoPreviewVPAdapter photoPreviewVPAdapter;
    private TranslateAnimation animation;

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    @Override
    public void doInitViewData() {
        viewModel = new ViewModelProvider(this).get(PhotoPreviewViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_preview);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setTitle("../...");
    }

    @Override
    public void doInitListener() {
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                getSupportActionBar().setTitle(String.format(getString(R.string.photo_detail_title), position + 1, viewModel.getDisplayData().getValue().size()));
            }
        });
        viewModel.getDisplayData().observe(this, photoBeans -> {
            photoPreviewVPAdapter.setData(photoBeans);
            viewModel.setCurrentPosition(photoBeans.indexOf(getIntent().getParcelableExtra("photoBean")));
        });
        viewModel.getCurrentPosition().observe(this, integer -> binding.viewPager.setCurrentItem(integer, false));
    }

    @Override
    protected void showPermissionDialog(ArrayList<Integer> notAllowPermissions) {

    }

    @Override
    public void doInitDisplayData() {
        photoPreviewVPAdapter = new PhotoPreviewVPAdapter(o -> {
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
        });
        binding.viewPager.setPageTransformer(new ZoomOutPageTransformer());
        binding.viewPager.setAdapter(photoPreviewVPAdapter);
        viewModel.setDisplayData(DataStore.getInstance().getAllDisplayData());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
