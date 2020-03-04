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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Handler handler = new Handler();
    private MutableLiveData<List<Object>> displayData; //图片+分组数据
    private MutableLiveData<List<PhotoBean>> selectedPhotoBean;//已选择
    private MutableLiveData<Map<String, PhotoSelectedNumBean>> photoGroupNum;//选择数量变化
    private MutableLiveData<Map<GroupBean, List<PhotoBean>>> classifiedDisplayDataMap;
    private MutableLiveData<Boolean> isSelectedAllGroup;
    private MutableLiveData<Boolean> isSelectedGroupAll;
    public MainViewModel mainViewModel;
    private Runnable classifiedRunnable;

    public MutableLiveData<List<Object>> getDisplayData() {
        if (displayData == null) {
            displayData = new MutableLiveData<>();
            displayData.setValue(new ArrayList<>());
        }
        return displayData;
    }

    public void setDisplayData() {
        Log.d("hi", "running here");
        getDisplayData().getValue().clear();
        getSelectedPhotoBean().getValue().clear();
        getClassifiedDisplayDataMap().getValue().forEach((k, v) -> {
            getDisplayData().getValue().add(k);
            v.forEach(it -> {
                getDisplayData().getValue().add(it);
                if (it.isSelected())
                    getSelectedPhotoBean().getValue().add(it);
            });
        });
        getSelectedPhotoBean().setValue(getSelectedPhotoBean().getValue());
        getDisplayData().setValue(getDisplayData().getValue());
    }

    public void modeChangeToNormal() {
        if (getSelectedPhotoBean().getValue() == null)
            return;
        for (Object o : Objects.requireNonNull(getDisplayData().getValue())) {
            if (o instanceof PhotoBean) {
                PhotoBean photoBean = (PhotoBean) o;
                photoBean.setSelected(false);
            } else if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                groupBean.setSelected(false);
            }
        }
        for (Map.Entry<String, PhotoSelectedNumBean> entry : Objects.requireNonNull(getPhotoGroupNum().getValue()).entrySet())
            entry.getValue().setSelected(0);
        getSelectedPhotoBean().getValue().clear();
        getSelectedPhotoBean().setValue(getSelectedPhotoBean().getValue());
    }

    public MutableLiveData<List<PhotoBean>> getSelectedPhotoBean() {
        if (selectedPhotoBean == null) {
            selectedPhotoBean = new MutableLiveData<>();
            selectedPhotoBean.setValue(new ArrayList<>());
        }
        return selectedPhotoBean;
    }

    private void addSelectMutableLiveData(PhotoBean photoBean) {
        for (PhotoBean bean : Objects.requireNonNull(getSelectedPhotoBean().getValue())) {
            if (photoBean.getPhotoId() == bean.getPhotoId()) {
                return;
            }
        }
        selectChangeCount(photoBean.getGroupId(), true);
        getSelectedPhotoBean().getValue().add(photoBean);
    }

    private void removeSelectMutableLiveData(PhotoBean photoBean) {
        Iterator<PhotoBean> iterator = Objects.requireNonNull(getSelectedPhotoBean().getValue()).iterator();
        while (iterator.hasNext()) {
            if (photoBean.getPhotoId() == iterator.next().getPhotoId()) {
                selectChangeCount(photoBean.getGroupId(), false);
                iterator.remove();
                break;
            }
        }
    }

    public void changeSelectMutableLiveData(int position, boolean isRepeat, boolean isGroupAll, boolean isAllGroup) {
        if (position >= Objects.requireNonNull(getDisplayData().getValue()).size())
            return;
        Object o = getDisplayData().getValue().get(position);
        if (o instanceof PhotoBean) {
            PhotoBean photoBean = (PhotoBean) o;
            if (isRepeat)
                if (!isAllGroup)
                    photoBean.setSelected(getIsSelectedGroupAll().getValue());
                else
                    photoBean.setSelected(getIsSelectedAllGroup().getValue());
            else
                photoBean.setSelected(!photoBean.isSelected());
            if (photoBean.isSelected())
                addSelectMutableLiveData(photoBean);
            else
                removeSelectMutableLiveData(photoBean);
            if (isRepeat)
                changeSelectMutableLiveData(++position, isRepeat, isGroupAll, isAllGroup);
        } else if (o instanceof GroupBean) {
            if (isGroupAll || isAllGroup) {
                GroupBean groupBean = (GroupBean) o;
                if (isAllGroup)
                    groupBean.setSelected(getIsSelectedAllGroup().getValue());
                else
                    groupBean.setSelected(!groupBean.isSelected());
                ++position;
                isGroupAll = false;
            }
            if (Objects.requireNonNull(displayData.getValue()).get(position) instanceof GroupBean && !isAllGroup)
                return;
            changeSelectMutableLiveData(position++, true, isGroupAll, isAllGroup);
        }
        getSelectedPhotoBean().setValue(getSelectedPhotoBean().getValue());
    }

    private MutableLiveData<Map<String, PhotoSelectedNumBean>> getPhotoGroupNum() {
        if (photoGroupNum == null) {
            photoGroupNum = new MutableLiveData<>();
            photoGroupNum.setValue(new HashMap<>());
        }
        return photoGroupNum;
    }

    void setPhotoGroupNum(Map<String, PhotoSelectedNumBean> value) {
        Map<String, PhotoSelectedNumBean> oldValue = getPhotoGroupNum().getValue();
        for (Map.Entry<String, PhotoSelectedNumBean> entry : value.entrySet()) {
            assert oldValue != null;
            if (oldValue.containsKey(entry.getKey()))
                if (entry.getValue().getSize() < Objects.requireNonNull(oldValue.get(entry.getKey())).getSelected()) {
                    entry.getValue().setSelected(entry.getValue().getSize());
                } else {
                    entry.getValue().setSelected(Objects.requireNonNull(oldValue.get(entry.getKey())).getSelected());
                }
        }
        getPhotoGroupNum().setValue(value);
        selectChangeCount("null", false);
    }

    private void selectChangeCount(String key, boolean isAdd) {
        if (key != null && Objects.requireNonNull(getPhotoGroupNum().getValue()).containsKey(key)) {
            PhotoSelectedNumBean photoSelectedNumBean = getPhotoGroupNum().getValue().get(key);
            if (isAdd)
                photoSelectedNumBean.add();
            else
                photoSelectedNumBean.sub();
        }
        Iterator<Object> iterator = getDisplayData().getValue().iterator();
        boolean isAllGroupSelected = true;
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                if (getPhotoGroupNum().getValue().containsKey(groupBean.getGroupId())) {
                    PhotoSelectedNumBean photoSelectedNumBean = getPhotoGroupNum().getValue().get(groupBean.getGroupId());
                    if (photoSelectedNumBean.getSize() <= photoSelectedNumBean.getSelected())
                        groupBean.setSelected(true);
                    else {
                        groupBean.setSelected(false);
                        isAllGroupSelected = false;
                    }
                }
            }
        }
        if (mainViewModel != null) {
            if (isAllGroupSelected)
                mainViewModel.setIsSelectAllLiveData(true);
            else
                mainViewModel.setIsSelectAllLiveData(false);
        }
    }

    void resetSelectChangeCount() {
        if (getPhotoGroupNum().getValue() != null) {
            for (PhotoSelectedNumBean photoSelectedNumBean : getPhotoGroupNum().getValue().values())
                photoSelectedNumBean.setSelected(0);
        }
    }

    public MutableLiveData<Boolean> getIsSelectedAllGroup() {
        if (isSelectedAllGroup == null) {
            isSelectedAllGroup = new MutableLiveData<>();
            isSelectedAllGroup.setValue(false);
        }
        return isSelectedAllGroup;
    }

    private void setIsSelectedAllGroup(boolean value) {
        getIsSelectedAllGroup().setValue(value);
    }

    void setIsSelectAllGroupFromMainViewMode(boolean value) {
        setIsSelectedAllGroup(value);
        changeSelectMutableLiveData(0, true, false, true);
    }

    private MutableLiveData<Boolean> getIsSelectedGroupAll() {
        if (isSelectedGroupAll == null) {
            isSelectedGroupAll = new MutableLiveData<>();
            isSelectedGroupAll.setValue(false);
        }
        return isSelectedGroupAll;
    }

    public void setIsSelectedGroupAll(boolean value) {
        getIsSelectedGroupAll().setValue(value);
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
        Log.d("hi", "it:" +it.size());
        classifiedRunnable = () -> {
            setClassifiedDisplayDataMap(ClassifyUtil.PhotoBeansClassify(it, getClassifiedDisplayDataMap().getValue()));
        };
        handler.post(classifiedRunnable);
    }
}
