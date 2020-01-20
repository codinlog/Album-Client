package com.codinlog.album.Controller;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.databinding.ActivityMainBinding;
import com.codinlog.album.util.WorthStore;

import java.util.ArrayList;

import static com.codinlog.album.util.WorthStore.permission_RequestCode;

public abstract class BaseActivityController<T extends ViewModel> extends AppCompatActivity {
    protected static final String TAG = "BaseActivityController";
    protected T viewModel;
    protected ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doInitVew();
        doInitListener();
        checkPermissions();
    }

    protected void doUpgrade() {
        Log.d(TAG, "doUpgrade: Checking New Version..............");
    }

    protected void checkPermissions() {
        Log.d(TAG, "checkPermissions: ");
        ArrayList<String> permissions = new ArrayList<>();
        for (String permission : WorthStore.needPermissions) {
            int res = checkSelfPermission(permission);
            if (res == PackageManager.PERMISSION_DENIED)
                permissions.add(permission);
        }
        if (permissions.size() > 0) {
            requestPermissions(WorthStore.needPermissions, permission_RequestCode);
        } else {
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
                notAllowPermissions.add(WorthStore.permissionsDetails[i]);
        }
        if (notAllowPermissions.size() > 0) {
            showPermissionDialog(notAllowPermissions);
        }else{
            doInitData();
            doUpgrade();
        }
    }

    protected abstract void showPermissionDialog(ArrayList<Integer> notAllowPermissions);

    protected abstract void doInitData();

    protected abstract void doInitVew();

    protected abstract void doInitListener();

}
