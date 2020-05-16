package com.codinlog.album.controller.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.MainAdapter;
import com.codinlog.album.application.AlbumApplication;
import com.codinlog.album.bean.FragmentBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.controller.BaseActivityController;
import com.codinlog.album.controller.activity.kotlin.SettingsActivity;
import com.codinlog.album.controller.fragment.AlbumFragment;
import com.codinlog.album.controller.fragment.PhotoFragment;
import com.codinlog.album.controller.fragment.kotlin.DiaryFragment;
import com.codinlog.album.controller.service.kotlin.PushService;
import com.codinlog.album.databinding.ActivityMainBinding;
import com.codinlog.album.entity.AlbumEntity;
import com.codinlog.album.entity.AlbumItemEntity;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.model.AlbumViewModel;
import com.codinlog.album.model.MainViewModel;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.model.kotlin.DiaryLoginViewModel;
import com.codinlog.album.util.Classify;
import com.codinlog.album.util.DataStore;
import com.codinlog.album.util.WorthStore;
import com.codinlog.album.widget.AlbumDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.codinlog.album.util.WorthStore.MODE.MODE_NORMAL;
import static com.codinlog.album.util.WorthStore.REQUEST_TAKE_PHOTO;
import static com.codinlog.album.util.WorthStore.albumPager;
import static com.codinlog.album.util.WorthStore.loaderManagerId;
import static com.codinlog.album.util.WorthStore.photoPager;

public class MainActivity extends BaseActivityController<MainViewModel, ActivityMainBinding> {
    private static final Object lock = new Object();
    private static boolean noOperation = true;
    private ArrayList<FragmentBean> fragmentBeans;
    private MainAdapter mainAdapter;
    private String currentPhotoPath;
    private PopupMenu popupMenuOperation;
    private Handler handler = new Handler();

    @Override
    public void doInitViewData() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.setData(viewModel);
        binding.bottomNavigation.setVisibility(View.GONE);
        viewModel.photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        viewModel.albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        viewModel.diaryLoginViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this)).get(DiaryLoginViewModel.class);
        viewModel.photoViewModel.mainViewModel = viewModel;
        viewModel.albumViewModel.mainViewModel = viewModel;
        viewModel.diaryLoginViewModel.setMainViewModel(viewModel);
        viewModel.setLock(new Object());
        popupMenuOperation = new PopupMenu(this, binding.btnOperation);
        popupMenuOperation.getMenuInflater().inflate(R.menu.album_display, popupMenuOperation.getMenu());
        startService(new Intent(this, PushService.class));
    }

    @Override
    public void doInitDisplayData() {
        mainAdapter = new MainAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentBeans = new ArrayList<>();
        fragmentBeans.add(new FragmentBean(PhotoFragment.newInstance(), getString(R.string.photo)));
        fragmentBeans.add(new FragmentBean(AlbumFragment.newInstance(), getString(R.string.album)));
        fragmentBeans.add(new FragmentBean(DiaryFragment.newInstance(), getString(R.string.time)));
        viewModel.setFragments(fragmentBeans);
        binding.viewPager.setAdapter(mainAdapter);
        binding.viewPager.setOffscreenPageLimit(2);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        loadPhotoData();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void doInitListener() {
        viewModel.getFragments().observe(this, fragmentBeans -> {
            if (mainAdapter == null)
                return;
            mainAdapter.setList(MainActivity.this.fragmentBeans);
            mainAdapter.notifyDataSetChanged();
        });
        viewModel.getPhotoBeans().observe(this, it -> {
            DataStore.getInstance().setAllDisplayData(it);
            viewModel.photoViewModel.setGroupClassifiedData(it);
            viewModel.albumViewModel.setFolderClassifiedData(it);
            viewModel.albumViewModel.setCategoryClassifiedData(it, viewModel.albumViewModel.getCategoryClassifiedData().getValue().getSecond());
        });
        viewModel.getMode().observe(this, mode -> {
            binding.viewPager.setCanScroll(mode == MODE_NORMAL);
            binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
            binding.bottomNavigation.setVisibility(mode == MODE_NORMAL ? View.GONE : View.VISIBLE);
            binding.tabLayout.setVisibility(mode == MODE_NORMAL ? View.VISIBLE : View.INVISIBLE);
            binding.topBarSelectNotice.setVisibility(mode == MODE_NORMAL ? View.INVISIBLE : View.VISIBLE);
            switch (viewModel.getCurrentPager().getValue()) {
                case WorthStore.photoPager:
                    binding.btnOperation.setImageDrawable(getDrawable(mode == MODE_NORMAL ? R.drawable.ic_camera_black_24dp : R.drawable.ic_delete_forever_black_24dp));
                    break;
                case WorthStore.albumPager:
                    binding.btnOperation.setImageDrawable(getDrawable(mode == MODE_NORMAL ? R.drawable.ic_remove_red_eye_black_24dp : R.drawable.ic_delete_forever_black_24dp));
                    break;
                case WorthStore.diaryPager:
                    binding.btnOperation.setImageDrawable(getDrawable(mode == MODE_NORMAL ? R.drawable.ic_filter_list_black_24dp : R.drawable.ic_delete_forever_black_24dp));
                    break;
            }

            viewModel.getIsSelectAll().setValue(false);
            viewModel.modeChanged();
        });
        viewModel.getTitle().observe(this, title -> {
            binding.topBarSelectNotice.setText(title);
        });
        viewModel.getIsSelectAll().observe(this, aBoolean -> binding.bottomNavigation.getMenu().getItem(2).setTitle(aBoolean ? getString(R.string.btn_all_cancel) : getString(R.string.btn_all)));
        viewModel.getCurrentPager().observe(this, integer -> {
            switch (integer) {
                case WorthStore.photoPager:
                    binding.bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.ic_add_black_24dp);
                    binding.bottomNavigation.getMenu().getItem(1).setTitle(getString(R.string.addto_album));
                    binding.btnOperation.setImageDrawable(getDrawable(R.drawable.ic_camera_black_24dp));
                    break;
                case WorthStore.albumPager:
                    binding.bottomNavigation.getMenu().getItem(1).setIcon(R.drawable.ic_call_merge_black_24dp);
                    binding.bottomNavigation.getMenu().getItem(1).setTitle(getString(R.string.merge_album));
                    binding.btnOperation.setImageDrawable(getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                    break;
                case WorthStore.diaryPager:
                    binding.btnOperation.setImageDrawable(getDrawable(R.drawable.ic_filter_list_black_24dp));
                    break;
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewModel.setCurrentPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            if (viewModel.getCurrentPager().getValue() == photoPager) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_func:
                        if (viewModel.photoViewModel.getSelectedData().getValue().size() <= 0) {
                            Toast.makeText(MainActivity.this, getString(R.string.choice_item), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        List<AlbumEntity> albumEntities = viewModel.albumViewModel.getAlbumDisplayData().getValue();
                        List<String> stringList = new ArrayList<>();
                        if (albumEntities != null) {
                            stringList = albumEntities.stream().map(AlbumEntity::getAlbumName).collect(Collectors.toList());
                        }
                        AlbumDialog albumDialog = new AlbumDialog(MainActivity.this)
                                .setBtnCancelListener(new CommonListener() {
                                    @Override
                                    public void handleEvent(Object o) {
                                        AlbumDialog dialog = (AlbumDialog) o;
                                        dialog.dismiss();
                                    }
                                })
                                .setBtnOkListener(o -> {
                                    AlbumDialog dialog = (AlbumDialog) o;
                                    String albumName = dialog.getInputContent().trim();
                                    if ("".equals(albumName))
                                        Toast.makeText(MainActivity.this, getString(R.string.enter_album_name), Toast.LENGTH_SHORT).show();
                                    else {
                                        AlbumEntity albumEntity = new AlbumEntity();
                                        albumEntity.setAlbumName(albumName);
                                        albumEntity.setDate(new Date());
                                        viewModel.photoViewModel.getSelectedData().getValue().forEach(
                                                it -> {
                                                    if (albumEntity.getPhotoBean() == null)
                                                        albumEntity.setPhotoBean(it);
                                                    if (it.getTokenDate() > albumEntity.getPhotoBean().getTokenDate())
                                                        albumEntity.setPhotoBean(it);
                                                }
                                        );
                                        viewModel.albumViewModel.queryAlbumById(albumEntity.hashCode(), o1 -> {
                                            if (o1 == null) {
                                                viewModel.albumViewModel.insertAlbumWithPhotoBeans(albumEntity, viewModel.photoViewModel.getSelectedData().getValue(), o2 -> {
                                                    if (o2 != null && ((List<Long>) o2).size() > 0)
                                                        Toast.makeText(MainActivity.this, getString(R.string.addto_album_success), Toast.LENGTH_SHORT).show();
                                                });
                                            } else {
                                                AlbumEntity opAlbumEntity = (AlbumEntity) o1;
                                                if (opAlbumEntity.getPhotoBean().getTokenDate() < albumEntity.getPhotoBean().getTokenDate())
                                                    opAlbumEntity.setPhotoBean(albumEntity.getPhotoBean());
                                                viewModel.albumViewModel.insertExistAlbumWithPhotoBeans(opAlbumEntity, viewModel.photoViewModel.getSelectedData().getValue(), o3 -> {
                                                    if (o3 != null && ((List<Long>) o3).size() > 0)
                                                        Toast.makeText(MainActivity.this, getString(R.string.addto_album_success), Toast.LENGTH_SHORT).show();
                                                });
                                            }
                                            viewModel.setMode(MODE_NORMAL);
                                            dialog.dismiss();
                                        });
                                    }
                                })
                                .setNoticeAdapterData(stringList)
                                .setTvTitle(getString(R.string.addto_album));
                        albumDialog.show();
                        break;
                    case R.id.menu_cancel:
                        viewModel.setMode(MODE_NORMAL);
                        break;
                    case R.id.menu_all:
                        viewModel.setIsSelectAllToOtherViewModel();
                        break;
                }
            } else if (viewModel.getCurrentPager().getValue() == albumPager) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_func:
                        final List<String> noticeStrings = new ArrayList<>();//albumEntities.stream().map(AlbumEntity::getAlbumName).collect(Collectors.toList());
                        final List<String> invalidStrings = new ArrayList<>();
                        viewModel.albumViewModel.getSelectedData().getValue().forEach(it -> {
                            noticeStrings.add(it.getAlbumName());
                        });
                        viewModel.albumViewModel.getAlbumDisplayData().getValue().forEach(it -> {
                            if (!noticeStrings.contains(it.getAlbumName()))
                                invalidStrings.add(it.getAlbumName());
                        });
                        View view = LayoutInflater.from(this).inflate(R.layout.mergeto_album_dialog, null);
                        AutoCompleteTextView autoTv = view.findViewById(R.id.autoTv);
                        ImageButton imgBtn = view.findViewById(R.id.imgBtn);
                        TextView tv = view.findViewById(R.id.tv);
                        CheckBox cb = view.findViewById(R.id.cb);
                        autoTv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noticeStrings));
                        imgBtn.setOnClickListener(v -> {
                            if (noticeStrings.size() > 0)
                                autoTv.showDropDown();
                            else
                                Toast.makeText(MainActivity.this, R.string.no_notice, Toast.LENGTH_SHORT).show();
                        });
                        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle(R.string.merge_album)
                                .setCancelable(false)
                                .setView(view)
                                .setNegativeButton(R.string.btn_cancel, (dialog, i) -> {
                                    viewModel.setMode(MODE_NORMAL);
                                    dialog.dismiss();
                                })
                                .setPositiveButton(R.string.btn_ok, null).create();
                        alertDialog.setOnShowListener(__ -> {
                            Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            button.setOnClickListener(v -> {
                                if (noOperation)
                                    Toast.makeText(MainActivity.this, R.string.no_operation, Toast.LENGTH_SHORT).show();
                                else {
                                    String albumName = autoTv.getText().toString().trim();
                                    List<AlbumEntity> albumEntities = viewModel.albumViewModel.getSelectedData().getValue();
                                    if (albumEntities.size() <= 1) {
                                        AlbumEntity albumEntity = albumEntities.get(0);
                                        viewModel.albumViewModel.renameAlbum(albumEntity.getAlbumId(), albumName, o -> {
                                            if (o != null && (int) o > 0)
                                                Toast.makeText(MainActivity.this, R.string.rename_success, Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        boolean keepOldAlbum = cb.isChecked();
                                        boolean createNew = !noticeStrings.contains(albumName);
                                        AlbumEntity targetAlbumEntity = null;
                                        List<AlbumEntity> todoAlbumEntities = new ArrayList<>();
                                        for (AlbumEntity e : albumEntities) {
                                            if (e.getAlbumName().equals(albumName))
                                                targetAlbumEntity = e;
                                            else
                                                todoAlbumEntities.add(e);
                                        }
                                        if (!createNew && targetAlbumEntity == null) return;
                                        if (createNew) {
                                            targetAlbumEntity = new AlbumEntity();
                                            targetAlbumEntity.setAlbumName(albumName);
                                            targetAlbumEntity.setDate(new Date());
                                        }
                                        viewModel.albumViewModel.mergeAlbum(targetAlbumEntity, todoAlbumEntities, keepOldAlbum, createNew, o -> {
                                            if (o != null && (boolean) o)
                                                Toast.makeText(MainActivity.this, R.string.merge_success, Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }
                                viewModel.setMode(MODE_NORMAL);
                                alertDialog.dismiss();
                            });
                            autoTv.requestFocus();
                            InputMethodManager inputManager = (InputMethodManager) AlbumApplication.context
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            handler.postDelayed(() -> inputManager.showSoftInput(autoTv, 0), 200);
                        });
                        alertDialog.show();
                        autoTv.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void afterTextChanged(Editable editable) {
                                List<AlbumEntity> value = viewModel.albumViewModel.getSelectedData().getValue();
                                String albumName = editable.toString().trim();
                                cb.setVisibility(View.VISIBLE);
                                cb.setEnabled(true);
                                noOperation = false;
                                if (albumName.isEmpty()) {
                                    tv.setText(R.string.album_name_invalid);
                                    cb.setVisibility(View.INVISIBLE);
                                    noOperation = true;
                                    return;
                                }
                                if (invalidStrings.contains(albumName)) {
                                    tv.setText(R.string.album_exists);
                                    cb.setEnabled(false);
                                    noOperation = true;
                                    return;
                                }
                                if (value.size() == 1) {
                                    cb.setVisibility(View.INVISIBLE);
                                    if (noticeStrings.contains(albumName)) {
                                        tv.setText(R.string.no_operation);
                                        noOperation = true;
                                    } else {
                                        tv.setText(R.string.rename);
                                        noOperation = false;
                                    }
                                } else {
                                    if (noticeStrings.contains(albumName))
                                        tv.setText(R.string.add_to_exists_album);
                                    else
                                        tv.setText(R.string.add_to_new_album);
                                    noOperation = false;
                                }
                            }
                        });
                        break;
                    case R.id.menu_cancel:
                        viewModel.setMode(MODE_NORMAL);
                        break;
                    case R.id.menu_all:
                        viewModel.setIsSelectAllToOtherViewModel();
                        break;
                }
            }
            return true;
        });

        binding.btnOperation.setOnClickListener(v ->
        {
            switch (viewModel.getCurrentPager().getValue()) {
                case photoPager:
                    if (viewModel.getMode().getValue() == MODE_NORMAL) {
                        try {
                            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
                                dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(R.string.notice)
                                .setCancelable(false)
                                .setIcon(R.drawable.ic_priority_high_black_24dp)
                                .setMessage(String.format(getString(R.string.delete_notice), viewModel.photoViewModel.getSelectedData().getValue().size()))
                                .setPositiveButton(R.string.btn_ok, (dialog, which) -> {
                                    new deletePhotoBeansAsyncTask(o -> {
                                        String[] strings = (String[]) o;
                                        if (Arrays.stream(strings).anyMatch(Objects::nonNull)) {
                                            Toast.makeText(this, R.string.delete_not_all, Toast.LENGTH_LONG).show();
                                        }
                                        viewModel.setMode(MODE_NORMAL);
                                        dialog.dismiss();
                                    }).execute(viewModel.photoViewModel.getSelectedData().getValue());
                                })
                                .setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.dismiss()).show();
                    }
                    break;
                case albumPager:
                    if (viewModel.getMode().getValue() == MODE_NORMAL) {
                        popupMenuOperation.show();
                    } else {
                        List<AlbumEntity> albumEntities = viewModel.albumViewModel.getSelectedData().getValue();
                        if (albumEntities.size() <= 0)
                            Toast.makeText(this, R.string.choice_item, Toast.LENGTH_SHORT).show();
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                    .setTitle(R.string.notice)
                                    .setIcon(R.drawable.ic_priority_high_black_24dp)
                                    .setCancelable(false)
                                    .setMessage(getString(R.string.delete_notice, albumEntities.size()))
                                    .setNegativeButton(R.string.btn_cancel, (dialog, i) -> {
                                        dialog.dismiss();
                                    })
                                    .setPositiveButton(R.string.btn_ok, (dialog, i) -> {
                                        viewModel.albumViewModel.deleteAlbum(o -> {
                                            if (o != null && (int) o > 0)
                                                Toast.makeText(MainActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                                            viewModel.setMode(MODE_NORMAL);
                                        }, albumEntities.stream().toArray(AlbumEntity[]::new));
                                    });
                            builder.show();
                        }
                    }
                    break;
            }
        });

        binding.btnMore.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        popupMenuOperation.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.personal:
                    viewModel.albumViewModel.setDisplayAdapter("personal");
                    break;
                case R.id.intellect:
                    viewModel.albumViewModel.setDisplayAdapter("intellect");
                    break;
                case R.id.folder:
                    if (viewModel.albumViewModel.getFolderDisplayData().getValue().size() <= 0) {
                        Toast.makeText(this, R.string.no_folder_view, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    viewModel.albumViewModel.setDisplayOption(BottomSheetBehavior.STATE_COLLAPSED);
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
        if (viewModel.getMode().getValue() == WorthStore.MODE.MODE_SELECT)
            viewModel.setMode(MODE_NORMAL);
        else
            super.onBackPressed();
    }

    private void loadPhotoData() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(loaderManagerId, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                if (id == loaderManagerId)
                    return new CursorLoader(MainActivity.this, WorthStore.imageUri, WorthStore.imageProjection, WorthStore.selectionRule, WorthStore.selectionArgs, WorthStore.orderRule);
                return null;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                List<PhotoBean> photoBeans = viewModel.getPhotoBeans().getValue();
                boolean isFirstScanning = photoBeans.isEmpty();
                boolean isReloadData = false;
                Classify.removeDeletePhotoBeans(photoBeans, true);
                if (data != null && data.getCount() > 0) {
                    data.moveToFirst();
                    do {
                        boolean isContain = false;
                        String path = data.getString(data.getColumnIndexOrThrow(WorthStore.imageProjection[0]));
                        for (String str : WorthStore.disAllowScanning)
                            if (isContain = path.contains(str))
                                break;
                        File file = new File(path);
                        if (file.exists() && !isContain) {
                            if (isFirstScanning || (Classify.isPhotoRepeat(photoBeans, path) == WorthStore.photoIsNew)) {
                                isReloadData = true;
                                String size = data.getString(data.getColumnIndexOrThrow(WorthStore.imageProjection[1]));
                                String id = data.getString(data.getColumnIndexOrThrow(WorthStore.imageProjection[2]));
                                String tokenData = data.getString(data.getColumnIndexOrThrow(WorthStore.imageProjection[3]));
                                String width = data.getString(data.getColumnIndexOrThrow(WorthStore.imageProjection[4]));
                                String height = data.getString(data.getColumnIndexOrThrow(WorthStore.imageProjection[5]));
                                PhotoBean photoBean = PhotoBean.newInstance();
                                photoBean.setPhotoPath(path);
                                photoBean.setDelete(false);
                                photoBean.setPhotoId(Integer.parseInt(id));
                                photoBean.setTokenDate(tokenData == null ? 0 : Long.parseLong(tokenData));
                                photoBean.setWidth(width == null ? 0 : Integer.parseInt(width));
                                photoBean.setHeight(height == null ? 0 : Integer.parseInt(height));
                                photoBean.setPhotoSize(size == null ? 0 : Long.parseLong(size));
                                photoBean.setRotation(getRotation(path));
                                photoBeans.add(photoBean);
                            }
                        }
                    } while (data.moveToNext());
                    isReloadData = Classify.removeDeletePhotoBeans(photoBeans, false) || isReloadData;
                    if (isReloadData) {
                        Collections.sort(photoBeans);
                        viewModel.setPhotoBeans(photoBeans);
                        new Thread(() -> {
                            updateAlbum();
                        }).start();
                    }
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {

            }
        });
    }

    private int getRotation(String path) {
        if ("".equals(path))
            return 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            switch (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    private void updateAlbum() {
        synchronized (lock) {
            List<AlbumEntity> albumEntities = viewModel.albumViewModel.getAlbumDAO().queryAllAlbumWithList();
            if (albumEntities != null) {
                for (AlbumEntity albumEntity : albumEntities) {
                    List<AlbumItemEntity> albumItemEntities = viewModel.albumViewModel.getAlbumItemDAO().queryAllAlbumItem(albumEntity.getAlbumId());
                    if (albumItemEntities != null) {
                        Iterator<AlbumItemEntity> iterator = albumItemEntities.iterator();
                        boolean flag = true;
                        while (iterator.hasNext()) {
                            AlbumItemEntity albumItemEntity = iterator.next();
                            File file = new File(albumItemEntity.getPhotoBean().getPhotoPath());
                            if (file.exists()) {
                                if (flag) {
                                    albumEntity.setPhotoBean(albumItemEntity.getPhotoBean());
                                    flag = false;
                                }
                                iterator.remove();
                            }
                        }
                        if (flag) {
                            viewModel.albumViewModel.deleteAlbum(o -> {
                            }, albumEntity);
                            return;
                        } else {
                            viewModel.albumViewModel.deleteAlbumItem(albumItemEntities.toArray(new AlbumItemEntity[0]));
                            viewModel.albumViewModel.updateAlbum(o -> {
                            }, albumEntity);
                        }
                    }
                }
            }
        }
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

    private String[] deletePhotoBeans(String... filePaths) {
        for (int i = 0; i < filePaths.length; i++) {
            File file = new File(filePaths[i]);
            if (file.exists() && file.isFile() && file.delete()) {
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MediaStore.Images.Media.DATA + "=\"" + filePaths[i] + "\"", null);
                filePaths[i] = null;
            }
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(Environment.getExternalStorageState()));
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        return filePaths;
    }

    class deletePhotoBeansAsyncTask extends AsyncTask<List<PhotoBean>, Integer, String[]> {
        private CommonListener commonListener;

        public deletePhotoBeansAsyncTask(CommonListener commonListener) {
            this.commonListener = commonListener;
        }

        @Override
        protected String[] doInBackground(List<PhotoBean>... photoBeans) {
            String[] filePaths = photoBeans[0].stream().map(PhotoBean::getPhotoPath).collect(Collectors.toList()).toArray(new String[photoBeans[0].size()]);
            return deletePhotoBeans(filePaths);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            commonListener.handleEvent(strings);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

    }
}
