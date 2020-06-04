package com.codinlog.album.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoPreviewAdapter;
import com.codinlog.album.anim.ZoomOutPageTransformer;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.databinding.ActivityPhotoPreviewBinding;
import com.codinlog.album.model.PhotoPreviewViewModel;
import com.codinlog.album.util.DataStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.minetsh.imaging.IMGEditActivity;

public class PhotoPreviewActivity extends BaseActivityController<PhotoPreviewViewModel, ActivityPhotoPreviewBinding> {
    private static boolean isShowAppBar = false;
    private ActivityPhotoPreviewBinding binding;
    private PhotoPreviewAdapter photoPreviewAdapter;
    private TranslateAnimation animation;
    private final static int REQUEST_CODE = 1;
    private String storagePath;
    private static int currentPosition = 0;
    private boolean showEdit;


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
                currentPosition = position;
                getSupportActionBar().setTitle(String.format(getString(R.string.photo_detail_title), position + 1, viewModel.getDisplayData().getValue().size()));
            }
        });
        viewModel.getDisplayData().observe(this, photoBeans -> {
            photoPreviewAdapter.setData(photoBeans);
            viewModel.setCurrentPosition(photoBeans.indexOf(getIntent().getParcelableExtra("photoBean")));
        });
        viewModel.getCurrentPosition().observe(this, integer -> {
            binding.viewPager.setCurrentItem(integer, false);
        });
    }

    @Override
    protected void showPermissionDialog(ArrayList<Integer> notAllowPermissions) {

    }

    @Override
    public void doInitDisplayData() {
        photoPreviewAdapter = new PhotoPreviewAdapter(o -> {
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
        binding.viewPager.setAdapter(photoPreviewAdapter);
        viewModel.setDisplayData(DataStore.getInstance().getAllDisplayData());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                try {
                    editPhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    private void editPhoto() throws IOException {
        PhotoBean photoBean = viewModel.getDisplayData().getValue().get(currentPosition);
        Uri from = Uri.fromFile(new File(photoBean.getPhotoPath()));
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "MyAlbum/Pictures");
        if (!storageDir.exists())
            storageDir.mkdirs();
        File photoFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        storagePath = photoFile.getAbsolutePath();
        Intent intent = new Intent(this, IMGEditActivity.class);
        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI, from);
        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, storagePath);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(storagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getBooleanExtra("showEdit", false))
            getMenuInflater().inflate(R.menu
                    .photo_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
