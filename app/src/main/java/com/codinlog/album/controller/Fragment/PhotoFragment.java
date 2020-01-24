package com.codinlog.album.controller.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.GridLayoutManager;

import com.codinlog.album.controller.BaseFragmentController;
import com.codinlog.album.R;
import com.codinlog.album.adapter.PhotoRecyclerViewAdpater;
import com.codinlog.album.bean.ImageBean;
import com.codinlog.album.databinding.PhotoFragmentBinding;
import com.codinlog.album.listener.PhotoItemCheckBoxListener;
import com.codinlog.album.listener.PhotoItemOnClickListener;
import com.codinlog.album.listener.PhotoItemOnLongClickListenser;
import com.codinlog.album.model.PhotoViewModel;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class PhotoFragment extends BaseFragmentController<PhotoViewModel> {
    private PhotoRecyclerViewAdpater photoRecyclerViewAdpater;
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
        photoRecyclerViewAdpater = new PhotoRecyclerViewAdpater(new PhotoItemOnLongClickListenser() {
            @Override
            public void handleEvent(int position) {
                if (viewModel.getModeMutableLiveData().getValue() != WorthStoreUtil.MODE.MODE_SELECT)
                    viewModel.setModeMutableLiveData(WorthStoreUtil.MODE.MODE_SELECT);
                selectPhotoChanged(position);
            }
        }, new PhotoItemOnClickListener() {
            @Override
            public void handleEvent(int position) {
                selectPhotoChanged(position);
            }
        }, new PhotoItemCheckBoxListener() {
            @Override
            public void handleEvent(int position) {
                selectPhotoChanged(position);
            }
        });
        photoFragmentBinding = (PhotoFragmentBinding) binding;
    }

    @Override
    protected void doInitListener() {
        viewModel.getObjectMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Object>>() {
            @Override
            public void onChanged(ArrayList<Object> objects) {
                photoRecyclerViewAdpater.setData(objects);
            }
        });

        viewModel.getModeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<WorthStoreUtil.MODE>() {
            @Override
            public void onChanged(WorthStoreUtil.MODE mode) {
                if (viewModel.getModeMutableLiveData().getValue() == WorthStoreUtil.MODE.MODE_NORMAL && viewModel.getSelectMutableLiveData().getValue() != null) {
                    ArrayList<Integer> selectList = viewModel.getSelectMutableLiveData().getValue();
                    ArrayList<Object> objectArrayList = viewModel.getObjectMutableLiveData().getValue();
                    Iterator<Integer> iterator = selectList.iterator();
                    while (iterator.hasNext()) {
                        Integer id = iterator.next();
                        for (Object o : objectArrayList) {
                            if (o instanceof ImageBean) {
                                ImageBean imageBean = (ImageBean) o;
                                if (imageBean.getImageId() == id) {
                                    imageBean.setSelected(!imageBean.isSelected());
                                    break;
                                }
                            }
                        }
                    }
                    selectList.clear();
                }
                photoRecyclerViewAdpater.setMode(mode);
            }
        });
    }

    @Override
    protected void doInitData() {
        photoFragmentBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), WorthStoreUtil.thumbnailImageNum));
        photoFragmentBinding.rv.setAdapter(photoRecyclerViewAdpater);
    }

    private void selectPhotoChanged(int position) {
        ArrayList<Object> objectArrayList = viewModel.getObjectMutableLiveData().getValue();
        Object o = objectArrayList.get(position);
        if (o instanceof ImageBean) {
            ImageBean imageBean = (ImageBean) o;
            imageBean.setSelected(!imageBean.isSelected());
            if (imageBean.isSelected()) {
                viewModel.addSelectMutableLiveData(imageBean.getImageId());
            } else {
                viewModel.removeSelectMutableLiveData(imageBean.getImageId());
            }
            photoRecyclerViewAdpater.notifyItemChanged(position, "payload");
        }
    }

}
