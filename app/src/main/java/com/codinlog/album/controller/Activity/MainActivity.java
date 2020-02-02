package com.codinlog.album.controller.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
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
import com.codinlog.album.databinding.ActivityMainBinding;
import com.codinlog.album.listener.AlbumDialogBtnCancelListener;
import com.codinlog.album.listener.AlbumDialogBtnOkListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.model.MainViewModel;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.model.TimeViewModel;
import com.codinlog.album.util.ClassifyUtil;
import com.codinlog.album.util.WorthStoreUtil;
import com.codinlog.album.widget.AlbumDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.codinlog.album.util.WorthStoreUtil.MODE.MODE_NORMAL;
import static com.codinlog.album.util.WorthStoreUtil.REQUEST_TAKE_PHOTO;
import static com.codinlog.album.util.WorthStoreUtil.isFirstScanner;
import static com.codinlog.album.util.WorthStoreUtil.loaderManager_ID;
import static com.codinlog.album.util.WorthStoreUtil.photoPager;

public class MainActivity extends BaseActivityController<MainViewModel> {
    private ArrayList<FragmentBean> fragmentBeans;
    private MainVPAdapter mainVPAdapter;
    private ActivityMainBinding binding;
    private String currentPhotoPath;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void doInitVew() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.setData(viewModel);
        binding.bottomNavigation.setVisibility(View.GONE);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        viewModel.albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        viewModel.timeViewModel = ViewModelProviders.of(this).get(TimeViewModel.class);
        popupMenu = new PopupMenu(this, binding.btnMore);
        popupMenu.getMenuInflater().inflate(R.menu.top_menu, popupMenu.getMenu());
        fragmentBeans = new ArrayList<>();
        mainVPAdapter = new MainVPAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
        viewModel.getFragmentMutableLiveData().observe(this, fragmentBeans -> {
            mainVPAdapter.setList(MainActivity.this.fragmentBeans);
            mainVPAdapter.notifyDataSetChanged();
        });
        viewModel.getClassifiedPhotoBeanMutableLiveData().observe(this, photoBeans -> {
            viewModel.setPhotoViewModelListData(ClassifiedResBean.getInstance().getClassifiedPhotoResList());
            viewModel.setPhotoViewModelMapData(ClassifiedResBean.getInstance().getClassifiedPhotoResMap());
            viewModel.setPhotoViewModelMapNumData(ClassifiedResBean.getInstance().getClassifiedPhotoResNumMap());
        });
        viewModel.getModeMutableLiveData().observe(this, mode -> {
            binding.viewPager.setCanScroll(mode == MODE_NORMAL ? true : false);
            binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
            binding.bottomNavigation.setVisibility(mode == MODE_NORMAL ? View.GONE : View.VISIBLE);
            binding.tabLayout.setVisibility(mode == MODE_NORMAL ? View.VISIBLE : View.INVISIBLE);
            binding.topBarSelectNotice.setVisibility(mode == MODE_NORMAL ? View.INVISIBLE : View.VISIBLE);
            binding.btnOperation.setImageDrawable(getDrawable(mode == MODE_NORMAL ? R.drawable.ic_camera_black_24dp : R.drawable.ic_delete_forever_black_24dp));
        });
        viewModel.getIsSelectAllMutableLiveData().observe(this, aBoolean -> binding.bottomNavigation.getMenu().getItem(2).setTitle(aBoolean ? getString(R.string.btn_all_cancel) : getString(R.string.btn_all)));
        viewModel.photoViewModel.getSelectedMutableLiveData().observe(this, integers -> {
            if (viewModel.getModeMutableLiveData().getValue() == WorthStoreUtil.MODE.MODE_SELECT)
                binding.topBarSelectNotice.setText(String.format(getString(R.string.top_bar_select_notice), integers == null ? 0 : integers.size()));
//            int countFlag = 0;
//            for(PhotoSelectedNumBean photoSelectedNumBean:viewModel.photoViewModel.getClassifiedResNumMapMutableLiveData().getValue().values()){
//                countFlag = countFlag + photoSelectedNumBean.getSize();
//            }
//            if(integers.size() == countFlag)
//                viewModel.setIsSelectAllMutableLiveData(true);
//            else
//                viewModel.setIsSelectAllMutableLiveData(false);
        });
        viewModel.getCurrentPagerMutableLiveData().observe(this, integer -> {
            switch (integer) {
                case WorthStoreUtil.photoPager:
                    break;
                case WorthStoreUtil.albumPager:
                    break;
                case WorthStoreUtil.timePager:
                    break;
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
        binding.bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
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
        });

        binding.btnOperation.setOnClickListener(v -> {
            switch (viewModel.getCurrentPagerMutableLiveData().getValue()) {
                case photoPager:
                    if (viewModel.getModeMutableLiveData().getValue() == MODE_NORMAL) {
                        try {
                            dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                    break;
            }
        });

        binding.btnMore.setOnClickListener(v -> popupMenu.show());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.setting_1:
                    break;
            }
            return false;
        });
    }

    @Override
    protected void showPermissionDialog(final ArrayList<Integer> notAllowPermissions) {
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
                List<PhotoBean> photoBeans = viewModel.getClassifiedPhotoBeanMutableLiveData().getValue();
                if (isFirstScanner)
                    photoBeans = new ArrayList<>();
                else
                    ClassifyUtil.removeDeleteImage(photoBeans, true);
                if (data != null) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[0]));
                        boolean isContinue = true;
                        for (String str:WorthStoreUtil.disAllowScanning)
                            if(path.contains(str)){
                                isContinue = false;
                                break;
                            }
                        File file = new File(path);
                        if(file.exists() && isContinue) {
                            String size = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[1]));
                            String id = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[2]));
                            String tokenData = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[3]));
                            String width = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[4]));
                            String height = data.getString(data.getColumnIndexOrThrow(WorthStoreUtil.imageProjection[5]));
                            if (isFirstScanner || (ClassifyUtil.isPhotoRepeat(photoBeans, path) == WorthStoreUtil.photoIsNew)) {
                                PhotoBean photoBean = PhotoBean.newInstance();
                                photoBean.setPath(path).setSize(Long.parseLong(size)).setPhotoId(Integer.parseInt(id)).setTokenDate(Long.parseLong(tokenData))
                                        .setWidth(width == null ? 0 : Integer.parseInt(width))
                                        .setHeight(height == null ? 0 : Integer.parseInt(height));
                                photoBeans.add(photoBean);
                            }
                        }
                    } while (data.moveToNext());
                    if (!isFirstScanner)
                        ClassifyUtil.removeDeleteImage(photoBeans, false);
                    else
                        isFirstScanner = false;
                    ClassifiedResBean.getInstance().loadClassifiedRes(photoBeans);
                    viewModel.setClassifiedPhotoBeanMutableLiveData(ClassifiedResBean.getInstance().getClassifiedPhotoBeanResList());
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }
    }


    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.codinlog.album.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
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
        currentPhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    private boolean deletePhotos(ArrayList<String> filePaths) {
        final Iterator<String> iterator = filePaths.iterator();
        while (iterator.hasNext()) {
            String path = iterator.next();
            File file = new File(path);
            if (file.exists())
                if (file.isFile() & file.delete())
                    iterator.remove();
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(Environment.getExternalStorageState()));
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        return false;
    }
}
