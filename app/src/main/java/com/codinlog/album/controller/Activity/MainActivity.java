package com.codinlog.album.controller.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.MainVPAdapter;
import com.codinlog.album.bean.ClassifiedResBean;
import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.controller.Fragment.AlbumFragment;
import com.codinlog.album.controller.Fragment.PhotoFragment;
import com.codinlog.album.controller.Fragment.TimeFragment;
import com.codinlog.album.listener.AlbumDialogBtnCancelListener;
import com.codinlog.album.listener.AlbumDialogBtnOkListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.model.MainViewModel;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.model.TimeViewModel;
import com.codinlog.album.util.ClassifyUtil;
import com.codinlog.album.util.WorthStoreUtil;
import com.codinlog.album.widget.AlbumDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.codinlog.album.util.WorthStoreUtil.MODE.MODE_NORMAL;
import static com.codinlog.album.util.WorthStoreUtil.isFirstScanner;
import static com.codinlog.album.util.WorthStoreUtil.loaderManager_ID;

public class MainActivity extends BaseActivityController<MainViewModel> {
    private ArrayList<FragmentBean> fragmentBeans;
    private MainVPAdapter mainVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void doInitVew() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        viewModel.albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        viewModel.timeViewModel = ViewModelProviders.of(this).get(TimeViewModel.class);
        binding.setData(viewModel);
        fragmentBeans = new ArrayList<>();
        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.bottomNavigation.setVisibility(View.GONE);
    }

    @Override
    protected void doInitData() {
        fragmentBeans.add(new FragmentBean(PhotoFragment.newInstance(), getString(R.string.photo)));
        fragmentBeans.add(new FragmentBean(AlbumFragment.newInstance(), getString(R.string.album)));
        fragmentBeans.add(new FragmentBean(TimeFragment.newInstance(), getString(R.string.time)));
        binding.viewPager.setAdapter(mainVPAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        viewModel.setFragmentMutableLiveData(fragmentBeans);
        loadImageData();
    }

    @Override
    protected void doInitListener() {
        viewModel.getFragmentMutableLiveData().observe(this, new Observer<ArrayList<FragmentBean>>() {
            @Override
            public void onChanged(ArrayList<FragmentBean> fragmentBeans) {
                mainVPAdapter.setList(MainActivity.this.fragmentBeans);
                mainVPAdapter.notifyDataSetChanged();
            }
        });
        viewModel.getPhotoMutableLiveData().observe(this, new Observer<ArrayList<PhotoBean>>() {
            @Override
            public void onChanged(ArrayList<PhotoBean> photoBeans) {
                ClassifiedResBean.getInstance().loadClassifiedRes(photoBeans);
                viewModel.setPhotoViewModelListData(ClassifiedResBean.getInstance().getClassifiedPhotoResList());
                viewModel.setPhotoViewModelMapData(ClassifiedResBean.getInstance().getClassifiedPhotoResMap());
                viewModel.setPhotoViewModelMapNumData(ClassifiedResBean.getInstance().getClassifiedPhotoResNumMap());
            }
        });
        viewModel.getModeMutableLiveData().observe(this, new Observer<WorthStoreUtil.MODE>() {
            @Override
            public void onChanged(WorthStoreUtil.MODE mode) {
                binding.viewPager.setCanScroll(mode == MODE_NORMAL ? true : false);
                binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
                binding.bottomNavigation.setVisibility(mode == MODE_NORMAL ? View.GONE : View.VISIBLE);
                binding.tabLayout.setVisibility(mode == MODE_NORMAL ? View.VISIBLE : View.INVISIBLE);
                binding.topBarSelectNotice.setVisibility(mode == MODE_NORMAL ? View.INVISIBLE : View.VISIBLE);
                binding.btnSearch.setImageDrawable(getDrawable(mode == MODE_NORMAL ? R.drawable.ic_camera_black_24dp : R.drawable.ic_delete_forever_black_24dp));
            }
        });
        viewModel.getIsSelectAllMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.bottomNavigation.getMenu().getItem(2).setTitle(aBoolean ? getString(R.string.btn_all_cancel):getString(R.string.btn_all));
            }
        });
        viewModel.photoViewModel.getSelectedMutableLiveData().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                if (viewModel.getModeMutableLiveData().getValue() == WorthStoreUtil.MODE.MODE_SELECT)
                    binding.topBarSelectNotice.setText(String.format(getString(R.string.top_bar_select_notice), integers == null ? 0 : integers.size()));
            }
        });
        viewModel.getCurrentPagerMutableLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case WorthStoreUtil.photoPager:;break;
                    case WorthStoreUtil.albumPager:break;
                    case WorthStoreUtil.timePager:break;
                }
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModel.setCurrentPagerMutableLiveData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_addto_album:
                        AlbumDialog albumDialog = new AlbumDialog(MainActivity.this)
                                .setBtnOkListener(new AlbumDialogBtnOkListener())
                                .setBtnCancelListener(new AlbumDialogBtnCancelListener())
                                .setTvTitle(getString(R.string.addto_album));
                        albumDialog.show();
                        break;
                    case R.id.menu_cancel:
                        viewModel.setModeMutableLiveData(MODE_NORMAL);
                        break;
                    case R.id.menu_all:
                        viewModel.setIsSelectAllMutableLiveData(!viewModel.getIsSelectAllMutableLiveData().getValue());
                        viewModel.setIsSelectAllToOtherViewModel();
                        break;
                }
                return true;
            }
        });
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
        if (viewModel.getModeMutableLiveData().getValue() == WorthStoreUtil.MODE.MODE_SELECT)
            viewModel.setModeMutableLiveData(MODE_NORMAL);
        else
            super.onBackPressed();
    }

    private void loadImageData() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(loaderManager_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                if (id == loaderManager_ID) {
                    return new CursorLoader(MainActivity.this, WorthStoreUtil.imageUri, WorthStoreUtil.imageProjection, WorthStoreUtil.selectionRule, WorthStoreUtil.selectionArgs, WorthStoreUtil.orderRule);
                }
                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                ArrayList<PhotoBean> photoBeanArrayList = viewModel.getPhotoMutableLiveData().getValue();
                if (isFirstScanner)
                    photoBeanArrayList = new ArrayList<>();
                else
                    ClassifyUtil.removeDeleteImage(photoBeanArrayList, true);
                if (data != null) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[0]));
                        String size = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[1]));
                        String id = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[2]));
                        String tokenData = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[3]));
                        if (isFirstScanner || (ClassifyUtil.isPhotoRepeat(photoBeanArrayList, path) == WorthStoreUtil.photoIsNew)) {
                            PhotoBean photoBean = PhotoBean.newInstance();
                            photoBean.setPath(path).setSize(Long.parseLong(size)).setPhotoId(Integer.parseInt(id)).setTokenDate(Long.parseLong(tokenData));
                            photoBeanArrayList.add(photoBean);
                        }
                    } while (data.moveToNext());
                    if (!isFirstScanner)
                        ClassifyUtil.removeDeleteImage(photoBeanArrayList, false);
                    else
                        isFirstScanner = false;
                    viewModel.setPhotoMutableLiveData(photoBeanArrayList);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }
}
