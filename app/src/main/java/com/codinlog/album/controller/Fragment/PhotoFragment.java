package com.codinlog.album.controller.Fragment;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoRVAdpater;
import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.controller.Activity.PhotoPreviewActivity;
import com.codinlog.album.controller.Activity.kotlin.AlbumPreviewActivity;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.PhotoFragmentBinding;
import com.codinlog.album.listener.CommonListener;
import com.codinlog.album.listener.PhotoGroupListener;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.util.DataStoreUtil;
import com.codinlog.album.util.WorthStoreUtil;

public class PhotoFragment extends BaseFragmentController<PhotoViewModel,PhotoFragmentBinding> {
    private PhotoRVAdpater photoRVAdpater;

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.photo_fragment;
    }

    @Override
    public void doInitViewData() {
        viewModel = new ViewModelProvider(getActivity()).get(PhotoViewModel.class);
    }

    @Override
    public void doInitListener() {
        viewModel.getDisplayData().observe(getViewLifecycleOwner(), v -> photoRVAdpater.setData(v));
        viewModel.getClassifiedDisplayDataMap().observe(getViewLifecycleOwner(), v -> viewModel.setDisplayData());
        viewModel.getSelectedPhotoBeans().observe(getViewLifecycleOwner(), integers -> photoRVAdpater.notifyChange(null, true));
        viewModel.mainViewModel.getModeLiveData().observe(getViewLifecycleOwner(), mode -> {
            if (viewModel.mainViewModel.getModeLiveData().getValue() == WorthStoreUtil.MODE.MODE_NORMAL)
                viewModel.resetSelectLiveData();
            photoRVAdpater.setMode(mode);
        });
    }

    @Override
    public void doInitDisplayData() {
        photoRVAdpater = new PhotoRVAdpater(position -> {
            if (viewModel.mainViewModel.getModeLiveData().getValue() != WorthStoreUtil.MODE.MODE_SELECT)
                viewModel.mainViewModel.setModeLiveData(WorthStoreUtil.MODE.MODE_SELECT);
            selectPhotoChanged((int) position, false);
        }, position -> {
            if (viewModel.mainViewModel.getModeLiveData().getValue() == WorthStoreUtil.MODE.MODE_SELECT) {
                selectPhotoChanged((int) position, false);
            } else {
                Intent intent = new Intent(getContext(), PhotoPreviewActivity.class);
                PhotoBean photoBean = (PhotoBean) viewModel.getDisplayData().getValue().get((int) position);
                int currentPosition = 0;
                for (PhotoBean p : viewModel.mainViewModel.getPhotoBeansLiveData().getValue()) {
                    if (p.getPhotoId() == photoBean.getPhotoId()) {
                        intent.putExtra("currentPosition", currentPosition);
                        break;
                    }
                    currentPosition++;
                }
                DataStoreUtil.getInstance().setAllDisplayDataList(viewModel.mainViewModel.getPhotoBeansLiveData().getValue());
                startActivity(intent);
            }
        }, position -> selectPhotoChanged((int) position, false), new PhotoGroupListener() {
            @Override
            public void handleEvent(Object o) {
            }

            @Override
            public void handleEvent(int position, boolean isChecked) {
                selectPhotoChanged(position, isChecked);
            }
        }, new PhotoGroupListener() {
            @Override
            public void handleEvent(Object o) {
            }

            @Override
            public void handleEvent(int position, boolean isChecked) {
                if (viewModel.mainViewModel.getModeLiveData().getValue() != WorthStoreUtil.MODE.MODE_SELECT)
                    viewModel.mainViewModel.setModeLiveData(WorthStoreUtil.MODE.MODE_SELECT);
                selectPhotoChanged(position, !isChecked);
            }
        }, position -> {
            Object o = viewModel.getDisplayData().getValue().get((int)position);
            if(o instanceof GroupBean){
                GroupBean groupBean = (GroupBean) o;
                DataStoreUtil.getInstance().setDisplayDataList(viewModel.getClassifiedDisplayDataMap().getValue().get(groupBean));
                Intent intent = new Intent(getContext(), AlbumPreviewActivity.class);
                intent.putExtra("from", "photo");
                intent.putExtra("fromValue", groupBean.getGroupId());
                startActivity(intent);
            }
        });
        binding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.thumbnailPhotoNum));
        binding.rv.setAdapter(photoRVAdpater);
    }

    private void selectPhotoChanged(int position, boolean isGroupAll) {
        viewModel.changeSelectLiveData(position,isGroupAll);
    }
}
