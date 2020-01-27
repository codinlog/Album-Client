package com.codinlog.album.controller.Activity;

import android.app.AlertDialog;
import android.view.TextureView;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.ViewModelProviders;

import com.codinlog.album.R;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.model.CamearaXViewModel;

import java.util.ArrayList;
import java.util.Iterator;

public class CameraXActivity extends BaseActivityController<CamearaXViewModel> {
    private ImageCaptureConfig config;
    private PreviewConfig previewConfig;
    private TextureView textureView;

    @Override
    protected void showPermissionDialog(final ArrayList notAllowPermissions) {
        final Iterator<Integer> iterator = notAllowPermissions.iterator();
        if (iterator.hasNext()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.notice)
                    .setMessage(iterator.next())
                    .setPositiveButton(R.string.certain, (dialog, which) -> {
                        dialog.dismiss();
                        iterator.remove();
                        showPermissionDialog(notAllowPermissions);
                    }).show();
        }
    }

    @Override
    protected void doInitData() {
        previewConfig = new PreviewConfig.Builder()
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                        .build();
        Preview preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(
                previewOutput -> {
                    // Your code here. For example, use previewOutput.getSurfaceTexture()
                    // and post to a GL renderer.
                    textureView.setSurfaceTexture(previewOutput.getSurfaceTexture());
                });

        CameraX.bindToLifecycle(this, preview);
    }

    @Override
    protected void doInitVew() {
        setContentView(R.layout.activity_camera);
        textureView = findViewById(R.id.textureView);
        viewModel = ViewModelProviders.of(this).get(CamearaXViewModel.class);
    }

    @Override
    protected void doInitListener() {

    }
}
