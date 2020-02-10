package com.codinlog.album.controller;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.databinding.ActivityMainBinding;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;

import static com.codinlog.album.util.WorthStoreUtil.permission_RequestCode;

public abstract class BaseActivityController<T extends ViewModel> extends AppCompatActivity {
    protected static final String TAG = "BaseActivityController";
    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doInitVew();
        checkPermissions();
    }

    protected void doUpgrade() {
        Log.d(TAG, "doUpgrade: Checking New Version..............");
    }

    protected void checkPermissions() {
        Log.d(TAG, "checkPermissions: ");
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
            doInitData();
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
        }else{
            doInitListener();
            doInitData();
            doUpgrade();
        }
    }

    protected abstract void doInitVew();

    protected abstract void doInitListener();

    protected abstract void showPermissionDialog(ArrayList<Integer> notAllowPermissions);

    protected abstract void doInitData();

}
