package com.codinlog.album.model;

import android.os.Handler;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.util.Classify;
import com.codinlog.album.util.WorthStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhotoViewModel extends ViewModel {
    public MainViewModel mainViewModel;
    public Object lock;
    private MutableLiveData<List<Object>> displayData; //图片+分组数据
    private MutableLiveData<List<PhotoBean>> selectedData;//已选择
    private MutableLiveData<WorthStore.MODE> mode;
    private MutableLiveData<Map<GroupBean, List<PhotoBean>>> classifiedDisplayData;
    private Handler handler = new Handler();
    private Runnable classifiedRunnable;

    public MutableLiveData<WorthStore.MODE> getMode() {
        if (mode == null) {
            mode = new MediatorLiveData<>();
            mode.setValue(WorthStore.MODE.MODE_NORMAL);
        }
        return mode;
    }

    public void setMode(WorthStore.MODE mode) {
        getMode().setValue(mode);
    }

    public MutableLiveData<List<Object>> getDisplayData() {
        if (displayData == null) {
            displayData = new MutableLiveData<>();
            displayData.setValue(new ArrayList<>());
        }
        return displayData;
    }

    public MutableLiveData<List<PhotoBean>> getSelectedData() {
        if (selectedData == null) {
            selectedData = new MutableLiveData<>();
            selectedData.setValue(new ArrayList<>());
        }
        return selectedData;
    }

    public MutableLiveData<Map<GroupBean, List<PhotoBean>>> getClassifiedDisplayData() {
        if (classifiedDisplayData == null) {
            classifiedDisplayData = new MutableLiveData<>();
            classifiedDisplayData.setValue(new LinkedHashMap<>());
        }
        return classifiedDisplayData;
    }

    public void setClassifiedDisplayData(Map<GroupBean, List<PhotoBean>> value) {
        getClassifiedDisplayData().setValue(value);
    }

    public void setGroupClassifiedData(List<PhotoBean> it) {
        synchronized (lock) {
            if (classifiedRunnable == null)
                classifiedRunnable = () -> setClassifiedDisplayData(Classify.PhotoBeansClassify(it, getClassifiedDisplayData().getValue()));
            handler.removeCallbacks(classifiedRunnable);
            handler.post(classifiedRunnable);
        }
    }

    public void setDisplayData() {
        getDisplayData().getValue().clear();
        getSelectedData().getValue().clear();
        List<GroupBean> groupBeans = new ArrayList<>(getClassifiedDisplayData().getValue().keySet());
        Collections.sort(groupBeans);
        groupBeans.forEach(it -> {
            getDisplayData().getValue().add(it);
            getClassifiedDisplayData().getValue().get(it).forEach(in -> {
                getDisplayData().getValue().add(in);
                if (in.isSelected())
                    getSelectedData().getValue().add(in);
            });
        });
        setMainViewModelIsSelectAll();
        getSelectedData().setValue(getSelectedData().getValue());
        getDisplayData().setValue(getDisplayData().getValue());
    }

    public void changeSelectLiveData(int position, boolean isGroupAll) {
        Object o = getDisplayData().getValue().get(position);
        if (o instanceof PhotoBean) {
            PhotoBean photoBean = (PhotoBean) o;
            photoBean.setSelected(!photoBean.isSelected());
            for (GroupBean groupBean : getClassifiedDisplayData().getValue().keySet()) {
                if (groupBean.getGroupId().equals(photoBean.getGroupId())) {
                    groupBean.setSelectNum(getClassifiedDisplayData().getValue().get(groupBean).
                            stream().filter(PhotoBean::isSelected).collect(Collectors.toList()).size());
                    groupBean.setSelected(groupBean.getHaveNum() <= groupBean.getSelectNum());
                    break;
                }
            }
        } else if (o instanceof GroupBean) {
            for (GroupBean groupBean : getClassifiedDisplayData().getValue().keySet()) {
                if (groupBean.getGroupId().equals(((GroupBean) o).getGroupId())) {
                    getClassifiedDisplayData().getValue().get(groupBean).forEach(it -> {
                        it.setSelected(isGroupAll);
                    });
                    groupBean.setSelectNum(getClassifiedDisplayData().getValue().get(groupBean).
                            stream().filter(it -> it.isSelected()).collect(Collectors.toList()).size());
                    groupBean.setSelected(groupBean.getHaveNum() <= groupBean.getSelectNum());
                    break;
                }
            }
        }
        getSelectedData().setValue(getDisplayData().getValue().stream()
                .filter(it -> ((it instanceof PhotoBean) && ((PhotoBean) it).isSelected()))
                .map(it -> (PhotoBean) it).collect(Collectors.toList()));
        setMainViewModelIsSelectAll();
    }


    public void resetDisplayData() {
        getClassifiedDisplayData().getValue().forEach((k, v) -> {
            k.setSelectNum(0);
            k.setSelected(false);
            v.forEach(it -> it.setSelected(false));
        });
        getSelectedData().setValue(new ArrayList<>());
    }

    public void selectAllGroup(boolean isAllGroup) {
        if (isAllGroup) {
            List<PhotoBean> photoBeans = new ArrayList<>();
            getClassifiedDisplayData().getValue().forEach((k, v) -> {
                v.forEach(it -> it.setSelected(isAllGroup));
                List<PhotoBean> photoBeansTemp = v.stream().filter(it -> it.isSelected()).collect(Collectors.toList());
                photoBeans.addAll(photoBeansTemp);
                k.setSelectNum(photoBeansTemp.size());
                k.setSelected(k.getHaveNum() <= k.getSelectNum());
            });
            getSelectedData().setValue(photoBeans);
        } else resetDisplayData();
        mainViewModel.setIsSelectAll(isAllGroup);
    }

    private void setMainViewModelIsSelectAll() {
        for (GroupBean groupBean : getClassifiedDisplayData().getValue().keySet()) {
            if (!groupBean.isSelected()) {
                mainViewModel.setIsSelectAll(false);
                return;
            }
        }
        mainViewModel.setIsSelectAll(true);
    }
}
