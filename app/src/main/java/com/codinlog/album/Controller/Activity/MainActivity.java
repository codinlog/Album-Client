package com.codinlog.album.Controller.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.codinlog.album.Controller.BaseActivityController;
import com.codinlog.album.Controller.Fragment.AlbumFragment;
import com.codinlog.album.Controller.Fragment.PhotoFragment;
import com.codinlog.album.Controller.Fragment.TimeFragment;
import com.codinlog.album.R;
import com.codinlog.album.adapter.ViewPagerAdapter;
import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.ImageBean;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.model.MainViewModel;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.model.TimeViewModel;
import com.codinlog.album.util.ClassifyUtil;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.Iterator;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.codinlog.album.util.WorthStoreUtil.isFirstScanner;
import static com.codinlog.album.util.WorthStoreUtil.loaderManager_ID;

public class MainActivity extends BaseActivityController<MainViewModel> {
    private ArrayList<FragmentBean> fragmentBeans;
    private ViewPagerAdapter viewPagerAdapter;
    private PhotoViewModel photoViewModel;
    private AlbumViewModel albumViewModel;
    private TimeViewModel timeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void doInitVew() {
        Log.d(TAG, "doInitVew: ");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        timeViewModel = ViewModelProviders.of(this).get(TimeViewModel.class);
        binding.setData(viewModel);
        fragmentBeans = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    protected void doInitData() {
        Log.d(TAG, "doInitData: ");
        fragmentBeans.add(new FragmentBean(PhotoFragment.newInstance(), getString(R.string.photo)));
        fragmentBeans.add(new FragmentBean(AlbumFragment.newInstance(), getString(R.string.album)));
        fragmentBeans.add(new FragmentBean(TimeFragment.newInstance(), getString(R.string.time)));
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        viewModel.setFragmentBeans(fragmentBeans);
    }

    @Override
    protected void doInitListener() {
        Log.d(TAG, "doInitListener: ");
        viewModel.getFragmentBeans().observe(this, new Observer<ArrayList<FragmentBean>>() {
            @Override
            public void onChanged(ArrayList<FragmentBean> fragmentBeans) {
                viewPagerAdapter.setList(MainActivity.this.fragmentBeans);
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getImageBeans().observe(this, new Observer<ArrayList<ImageBean>>() {
            @Override
            public void onChanged(ArrayList<ImageBean> imageBeans) {
                ArrayList<Object> classified = ClassifyUtil.PhotoClassification(imageBeans);
                photoViewModel.setObjectMutableLiveData(classified);
            }
        });
        loadImageData();
    }

    @Override
    protected void showPermissionDialog(final ArrayList<Integer> notAllowPermissions) {
        final Iterator<Integer> iterator = notAllowPermissions.iterator();
        if (iterator.hasNext()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.notice)
                    .setMessage(iterator.next())
                    .setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            iterator.remove();
                            showPermissionDialog(notAllowPermissions);
                        }
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(photoViewModel.getModeMutableLiveData().getValue() == WorthStoreUtil.MODE.MODE_SELECT)
            photoViewModel.setModeMutableLiveData(WorthStoreUtil.MODE.MODE_NORMAL);
        else
            super.onBackPressed();
    }

    private void loadImageData(){
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(loaderManager_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                if (id == loaderManager_ID) {
                    return new CursorLoader(MainActivity.this, WorthStoreUtil.imageUri, WorthStoreUtil.imageProjection, WorthStoreUtil.selectionRule, WorthStoreUtil.selectionArgs, WorthStoreUtil.orderRule);
//                    return new CursorLoader(MainActivity.this,
//                            WorthStoreUtil.imageUri,WorthStoreUtil.imageProjection,
//                            null,null,WorthStoreUtil.orderRule);
                }
                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                ArrayList<ImageBean> imageBeanArrayList = viewModel.getImageBeans().getValue();
                if(isFirstScanner)
                    imageBeanArrayList = new ArrayList<>();
                if (data != null) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[0]));
                        String size = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[1]));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[2])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[3])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[4])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[5])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[6])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[7])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[8])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[9])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[10])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[11])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[12])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[13])));
//                        Log.d(TAG,data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[14])));
                        if(isFirstScanner || (ClassifyUtil.isPhotoRepeat(imageBeanArrayList,path) == WorthStoreUtil.photo_isNew)){
                            ImageBean imageBean = ImageBean.newInstance();
                            imageBean.setPath(path);
                            imageBean.setSize(Long.parseLong(size));
                            imageBeanArrayList.add(imageBean);
                        }
                    } while (data.moveToNext());
                    if(isFirstScanner)
                        isFirstScanner = false;
                    viewModel.setImageBeans(imageBeanArrayList);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }
}
