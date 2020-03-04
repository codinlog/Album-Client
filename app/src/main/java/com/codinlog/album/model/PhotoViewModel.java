package com.codinlog.album.model;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;
import com.codinlog.album.util.ClassifyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Handler handler = new Handler();
    private MutableLiveData<List<Object>> displayData; //图片+分组数据
    private MutableLiveData<List<PhotoBean>> selectedPhotoBeans;//已选择
    private MutableLiveData<Map<GroupBean, List<PhotoBean>>> classifiedDisplayDataMap;
    private Runnable classifiedRunnable;
    public MainViewModel mainViewModel;

    public MutableLiveData<List<Object>> getDisplayData() {
        if (displayData == null) {
            displayData = new MutableLiveData<>();
            displayData.setValue(new ArrayList<>());
        }
        return displayData;
    }

    public MutableLiveData<List<PhotoBean>> getSelectedPhotoBeans() {
        if (selectedPhotoBeans == null) {
            selectedPhotoBeans = new MutableLiveData<>();
            selectedPhotoBeans.setValue(new ArrayList<>());
        }
        return selectedPhotoBeans;
    }

    public MutableLiveData<Map<GroupBean, List<PhotoBean>>> getClassifiedDisplayDataMap() {
        if (classifiedDisplayDataMap == null) {
            classifiedDisplayDataMap = new MutableLiveData<>();
            classifiedDisplayDataMap.setValue(new LinkedHashMap<>());
        }
        return classifiedDisplayDataMap;
    }

    public void setClassifiedDisplayDataMap(Map<GroupBean, List<PhotoBean>> value) {
        getClassifiedDisplayDataMap().setValue(value);
    }

    public void setClassifiedData(List<PhotoBean> it) {
        if (classifiedRunnable != null)
            handler.removeCallbacks(classifiedRunnable);
        else
            classifiedRunnable = () -> {
                setClassifiedDisplayDataMap(ClassifyUtil.PhotoBeansClassify(it, getClassifiedDisplayDataMap().getValue()));
            };
        handler.post(classifiedRunnable);
    }

    public void setDisplayData() {
        getDisplayData().getValue().clear();
        getSelectedPhotoBeans().getValue().clear();
        List<GroupBean> groupBeans = new ArrayList<>(getClassifiedDisplayDataMap().getValue().keySet());
        Collections.sort(groupBeans);
        groupBeans.forEach(it -> {
            getDisplayData().getValue().add(it);
            getClassifiedDisplayDataMap().getValue().get(it).forEach(in -> {
                getDisplayData().getValue().add(in);
                if (in.isSelected())
                    getSelectedPhotoBeans().getValue().add(in);
            });
        });
        setMainViewModelIsSelectAll();
        getSelectedPhotoBeans().setValue(getSelectedPhotoBeans().getValue());
        getDisplayData().setValue(getDisplayData().getValue());
    }

    public void changeSelectLiveData(int position, boolean isGroupAll) {
        Object o = getDisplayData().getValue().get(position);
        if (o instanceof PhotoBean) {
            PhotoBean photoBean = (PhotoBean) o;
            photoBean.setSelected(!photoBean.isSelected());
            for (GroupBean groupBean : getClassifiedDisplayDataMap().getValue().keySet()) {
                if (groupBean.getGroupId().equals(photoBean.getGroupId())) {
                    groupBean.setSelectNum(getClassifiedDisplayDataMap().getValue().get(groupBean).
                            stream().filter(PhotoBean::isSelected).collect(Collectors.toList()).size());
                    groupBean.setSelected(groupBean.getHaveNum() <= groupBean.getSelectNum());
                    break;
                }
            }
        } else if (o instanceof GroupBean) {
            for (GroupBean groupBean : getClassifiedDisplayDataMap().getValue().keySet()) {
                if (groupBean.getGroupId().equals(((GroupBean) o).getGroupId())) {
                    getClassifiedDisplayDataMap().getValue().get(groupBean).forEach(it -> {
                        it.setSelected(isGroupAll);
                    });
                    groupBean.setSelectNum(getClassifiedDisplayDataMap().getValue().get(groupBean).
                            stream().filter(it -> it.isSelected()).collect(Collectors.toList()).size());
                    groupBean.setSelected(groupBean.getHaveNum() <= groupBean.getSelectNum());
                    break;
                }
            }
        }
        getSelectedPhotoBeans().setValue(getDisplayData().getValue().stream()
                .filter(it -> ((it instanceof PhotoBean) && ((PhotoBean) it).isSelected()))
                .map(it -> (PhotoBean) it).collect(Collectors.toList()));
        setMainViewModelIsSelectAll();
    }


    public void resetSelectLiveData() {
        getClassifiedDisplayDataMap().getValue().forEach((k, v) -> {
            k.setSelectNum(0);
            k.setSelected(false);
            v.forEach(it -> it.setSelected(false));
        });
        getSelectedPhotoBeans().setValue(new ArrayList<>());
    }

    public void selectAllGroup(boolean isAllGroup) {
        if (isAllGroup) {
            List<PhotoBean> photoBeans = new ArrayList<>();
            getClassifiedDisplayDataMap().getValue().forEach((k, v) -> {
                v.forEach(it -> it.setSelected(isAllGroup));
                List<PhotoBean> photoBeansTemp = v.stream().filter(it -> it.isSelected()).collect(Collectors.toList());
                photoBeans.addAll(photoBeansTemp);
                k.setSelectNum(photoBeansTemp.size());
                k.setSelected(k.getHaveNum() <= k.getSelectNum());
            });
            getSelectedPhotoBeans().setValue(photoBeans);
        } else resetSelectLiveData();
        mainViewModel.setIsSelectAllLiveData(isAllGroup);
    }

    private void setMainViewModelIsSelectAll() {
        for (GroupBean groupBean : getClassifiedDisplayDataMap().getValue().keySet()) {
            if (!groupBean.isSelected()) {
                mainViewModel.setIsSelectAllLiveData(false);
                return;
            }
        }
        mainViewModel.setIsSelectAllLiveData(true);
    }
}
