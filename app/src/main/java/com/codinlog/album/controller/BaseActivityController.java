package com.codinlog.album.controller;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;

import static com.codinlog.album.util.WorthStoreUtil.permission_RequestCode;

public abstract class BaseActivityController<T extends ViewModel, V extends ViewDataBinding> extends AppCompatActivity implements IBaseController {
    protected static final String TAG = "BaseActivityController";
    protected T viewModel;
    protected V binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doInitViewData();
        checkPermissions();
    }

    protected void doUpgrade() {
    }

    protected void checkPermissions() {
        ArrayList<String> permissions = new ArrayList<>();
        for (String permission : WorthStoreUtil.needPermissions) {
            int res = checkSelfPermission(permission);
            if (res == PackageManager.PERMISSION_DENIED)
                permissions.add(permission);
        }
        if (permissions.size() > 0) {
            requestPermissions(WorthStoreUtil.needPermissions, permission_RequestCode);
        } else {
            doInitListener();
            doInitDisplayData();
            doUpgrade();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<Integer> notAllowPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                notAllowPermissions.add(WorthStoreUtil.permissionsDetails[i]);
        }
        if (notAllowPermissions.size() > 0) {
            showPermissionDialog(notAllowPermissions);
        } else {
            doInitListener();
            doInitDisplayData();
            doUpgrade();
        }
    }


    protected abstract void showPermissionDialog(ArrayList<Integer> notAllowPermissions);

}
