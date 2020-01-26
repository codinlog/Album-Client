package com.codinlog.album.controller.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoRVAdpater;
import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.databinding.PhotoFragmentBinding;
import com.codinlog.album.listener.BaseListener;
import com.codinlog.album.listener.PhotoGroupListener;
import com.codinlog.album.model.MainViewModel;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.List;

public class PhotoFragment extends BaseFragmentController<PhotoViewModel> {
    private PhotoRVAdpater photoRVAdpater;
    private PhotoFragmentBinding photoFragmentBinding;

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.photo_fragment;
    }

    @Override
    protected void doInitView() {
        viewModel = ViewModelProviders.of(getActivity()).get(PhotoViewModel.class);
        viewModel.mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        photoFragmentBinding = (PhotoFragmentBinding) binding;
        photoRVAdpater = new PhotoRVAdpater(new BaseListener() {
            @Override
            public void handleEvent(int position) {
                if (viewModel.mainViewModel.getModeMutableLiveData().getValue() != WorthStoreUtil.MODE.MODE_SELECT)
                    viewModel.mainViewModel.setModeMutableLiveData(WorthStoreUtil.MODE.MODE_SELECT);
                selectPhotoChanged(position, false, false, false);
            }
        }, new BaseListener() {
            @Override
            public void handleEvent(int position) {
                selectPhotoChanged(position, false, false, false);
            }
        }, new BaseListener() {
            @Override
            public void handleEvent(int position) {
                selectPhotoChanged(position, false, false, false);
            }
        }, new PhotoGroupListener() {
            @Override
            public void handleEvent(int position, boolean isChecked) {
                viewModel.setIsSelectGroupAllMutableLiveData(isChecked);
                selectPhotoChanged(position, true, true, false);
            }
        }, new PhotoGroupListener() {
            @Override
            public void handleEvent(int position, boolean isChecked) {
                if (viewModel.mainViewModel.getModeMutableLiveData().getValue() != WorthStoreUtil.MODE.MODE_SELECT)
                    viewModel.mainViewModel.setModeMutableLiveData(WorthStoreUtil.MODE.MODE_SELECT);
                viewModel.setIsSelectGroupAllMutableLiveData(!isChecked);
                selectPhotoChanged(position, true, true, false);
            }
        });
    }

    @Override
    protected void doInitListener() {
        viewModel.getClassifiedResListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Object>>() {
            @Override
            public void onChanged(List<Object> objects) {
                photoRVAdpater.setData(objects);
            }
        });

        viewModel.mainViewModel.getModeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<WorthStoreUtil.MODE>() {
            @Override
            public void onChanged(WorthStoreUtil.MODE mode) {
                if (viewModel.mainViewModel.getModeMutableLiveData().getValue() == WorthStoreUtil.MODE.MODE_NORMAL)
                    viewModel.modeChangeToNormal();
                photoRVAdpater.setMode(mode);
            }
        });
        viewModel.getSelectMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                photoRVAdpater.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void doInitData() {
        photoFragmentBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.thumbnailPhotoNum));
        photoFragmentBinding.rv.setAdapter(photoRVAdpater);
    }

    private void selectPhotoChanged(int position, boolean isRepeat, boolean isGroupAll, boolean isAllGroup) {
        viewModel.changeSelectMutableLiveData(position, isRepeat, isGroupAll, isAllGroup);
    }

}
