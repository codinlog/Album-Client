package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Object>> classifiedResListMutableLiveData; //图片+分组数据
    private MutableLiveData<Map<String, List<PhotoBean>>> classifiedResMapMutableLiveData;//归类的图片数据
    private MutableLiveData<List<PhotoBean>> selectedPhotoBeanMutableLiveData;//已选择
    private MutableLiveData<Map<String, PhotoSelectedNumBean>> classifiedResNumMapMutableLiveData;//选择数量变化
    private MutableLiveData<Boolean> isSelectedAllGroupMutableLiveData;
    private MutableLiveData<Boolean> isSelectedGroupAllMutableLiveData;
    public MainViewModel mainViewModel;

    public MutableLiveData<List<Object>> getClassifiedResListMutableLiveData() {
        if (classifiedResListMutableLiveData == null) {
            classifiedResListMutableLiveData = new MutableLiveData<>();
            classifiedResListMutableLiveData.setValue(new ArrayList<>());
        }
        return classifiedResListMutableLiveData;
    }

    void setClassifiedResListMutableLiveData(List<Object> value) {
        Iterator<Object> iteratorNewValue = value.iterator();
        Objects.requireNonNull(getSelectedPhotoBeanMutableLiveData().getValue()).clear();
        while (iteratorNewValue.hasNext()) {
            Object o1 = iteratorNewValue.next();
            if (o1 instanceof GroupBean) {
                GroupBean groupBeanNew = (GroupBean) o1;
                for (Object o2 : Objects.requireNonNull(getClassifiedResListMutableLiveData().getValue())) {
                    if (o2 instanceof GroupBean) {
                        GroupBean groupBeanOld = (GroupBean) o2;
                        if (groupBeanNew.getGroupId().equals(groupBeanOld.getGroupId())) {
                            groupBeanNew.setSelected(groupBeanOld.isSelected());
                            break;
                        }
                    }
                }
            } else if (o1 instanceof PhotoBean) {
                PhotoBean photoBean = (PhotoBean) o1;
                if (photoBean.isSelected())
                    getSelectedPhotoBeanMutableLiveData().getValue().add(photoBean);
            }
        }
        getSelectedPhotoBeanMutableLiveData().setValue(getSelectedPhotoBeanMutableLiveData().getValue());
        getClassifiedResListMutableLiveData().setValue(value);
    }

    public void modeChangeToNormal() {
        if (getSelectedPhotoBeanMutableLiveData().getValue() == null)
            return;
        for (Object o : Objects.requireNonNull(getClassifiedResListMutableLiveData().getValue())) {
            if (o instanceof PhotoBean) {
                PhotoBean photoBean = (PhotoBean) o;
                photoBean.setSelected(false);
            } else if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                groupBean.setSelected(false);
            }
        }
        for (Map.Entry<String, PhotoSelectedNumBean> entry : Objects.requireNonNull(getClassifiedResNumMapMutableLiveData().getValue()).entrySet())
            entry.getValue().setSelected(0);
        getSelectedPhotoBeanMutableLiveData().getValue().clear();
        getSelectedPhotoBeanMutableLiveData().setValue(getSelectedPhotoBeanMutableLiveData().getValue());
    }

    public MutableLiveData<List<PhotoBean>> getSelectedPhotoBeanMutableLiveData() {
        if (selectedPhotoBeanMutableLiveData == null) {
            selectedPhotoBeanMutableLiveData = new MutableLiveData<>();
            selectedPhotoBeanMutableLiveData.setValue(new ArrayList<>());
        }
        return selectedPhotoBeanMutableLiveData;
    }

    private void addSelectMutableLiveData(PhotoBean photoBean) {
        for (PhotoBean bean : Objects.requireNonNull(getSelectedPhotoBeanMutableLiveData().getValue())) {
            if (photoBean.getPhotoId() == bean.getPhotoId()) {
                return;
            }
        }
        selectChangeCount(photoBean.getGroupId(), true);
        getSelectedPhotoBeanMutableLiveData().getValue().add(photoBean);
    }

    private void removeSelectMutableLiveData(PhotoBean photoBean) {
        Iterator<PhotoBean> iterator = Objects.requireNonNull(getSelectedPhotoBeanMutableLiveData().getValue()).iterator();
        while (iterator.hasNext()) {
            if (photoBean.getPhotoId() == iterator.next().getPhotoId()) {
                selectChangeCount(photoBean.getGroupId(), false);
                iterator.remove();
                break;
            }
        }
    }

    public void changeSelectMutableLiveData(int position, boolean isRepeat, boolean isGroupAll, boolean isAllGroup) {
        if (position >= Objects.requireNonNull(getClassifiedResListMutableLiveData().getValue()).size())
            return;
        Object o = getClassifiedResListMutableLiveData().getValue().get(position);
        if (o instanceof PhotoBean) {
            PhotoBean photoBean = (PhotoBean) o;
            if (isRepeat)
                if (!isAllGroup)
                    photoBean.setSelected(getIsSelectedGroupAllMutableLiveData().getValue());
                else
                    photoBean.setSelected(getIsSelectedAllGroupMutableLiveData().getValue());
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
                    groupBean.setSelected(getIsSelectedAllGroupMutableLiveData().getValue());
                else
                    groupBean.setSelected(!groupBean.isSelected());
                ++position;
                isGroupAll = false;
            }
            if (Objects.requireNonNull(classifiedResListMutableLiveData.getValue()).get(position) instanceof GroupBean && !isAllGroup)
                return;
            changeSelectMutableLiveData(position++, true, isGroupAll, isAllGroup);
        }
        getSelectedPhotoBeanMutableLiveData().setValue(getSelectedPhotoBeanMutableLiveData().getValue());
    }

    void setClassifiedResMapMutableLiveData(Map<String, List<PhotoBean>> value) {
        if (classifiedResMapMutableLiveData == null)
            classifiedResMapMutableLiveData = new MutableLiveData<>();
        classifiedResMapMutableLiveData.setValue(value);
    }

    private MutableLiveData<Map<String, PhotoSelectedNumBean>> getClassifiedResNumMapMutableLiveData() {
        if (classifiedResNumMapMutableLiveData == null) {
            classifiedResNumMapMutableLiveData = new MutableLiveData<>();
            classifiedResNumMapMutableLiveData.setValue(new HashMap<>());
        }
        return classifiedResNumMapMutableLiveData;
    }

    void setClassifiedResNumMapMutableLiveData(Map<String, PhotoSelectedNumBean> value) {
        Map<String, PhotoSelectedNumBean> oldValue = getClassifiedResNumMapMutableLiveData().getValue();
        for (Map.Entry<String, PhotoSelectedNumBean> entry : value.entrySet()) {
            assert oldValue != null;
            if (oldValue.containsKey(entry.getKey()))
                if (entry.getValue().getSize() < Objects.requireNonNull(oldValue.get(entry.getKey())).getSelected()) {
                    entry.getValue().setSelected(entry.getValue().getSize());
                }
                else {
                    entry.getValue().setSelected(Objects.requireNonNull(oldValue.get(entry.getKey())).getSelected());
                }
        }
        getClassifiedResNumMapMutableLiveData().setValue(value);
        selectChangeCount("null", false);
    }

    private void selectChangeCount(String key, boolean isAdd) {
        if (key != null && Objects.requireNonNull(getClassifiedResNumMapMutableLiveData().getValue()).containsKey(key)) {
            PhotoSelectedNumBean photoSelectedNumBean = getClassifiedResNumMapMutableLiveData().getValue().get(key);
            if (isAdd)
                photoSelectedNumBean.add();
            else
                photoSelectedNumBean.sub();
        }
        Iterator<Object> iterator = getClassifiedResListMutableLiveData().getValue().iterator();
        boolean isAllGroupSelected = true;
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                if (getClassifiedResNumMapMutableLiveData().getValue().containsKey(groupBean.getGroupId())) {
                    PhotoSelectedNumBean photoSelectedNumBean = getClassifiedResNumMapMutableLiveData().getValue().get(groupBean.getGroupId());
                    if (photoSelectedNumBean.getSize() <= photoSelectedNumBean.getSelected())
                        groupBean.setSelected(true);
                    else {
                        groupBean.setSelected(false);
                        isAllGroupSelected = false;
                    }
                }
            }
        }
        if(mainViewModel != null) {
            if (isAllGroupSelected)
                mainViewModel.setIsSelectAllMutableLiveData(true);
            else
                mainViewModel.setIsSelectAllMutableLiveData(false);
        }
    }

    void resetSelectChangeCount() {
        if (getClassifiedResNumMapMutableLiveData().getValue() != null) {
            for (PhotoSelectedNumBean photoSelectedNumBean : getClassifiedResNumMapMutableLiveData().getValue().values())
                photoSelectedNumBean.setSelected(0);
        }
    }

    public MutableLiveData<Boolean> getIsSelectedAllGroupMutableLiveData() {
        if (isSelectedAllGroupMutableLiveData == null) {
            isSelectedAllGroupMutableLiveData = new MutableLiveData<>();
            isSelectedAllGroupMutableLiveData.setValue(false);
        }
        return isSelectedAllGroupMutableLiveData;
    }

    private void setIsSelectedAllGroupMutableLiveData(boolean value) {
        getIsSelectedAllGroupMutableLiveData().setValue(value);
    }

    void setIsSelectAllGroupFromMainViewMode(boolean value) {
        setIsSelectedAllGroupMutableLiveData(value);
        changeSelectMutableLiveData(0, true, false, true);
    }

    private MutableLiveData<Boolean> getIsSelectedGroupAllMutableLiveData() {
        if (isSelectedGroupAllMutableLiveData == null) {
            isSelectedGroupAllMutableLiveData = new MutableLiveData<>();
            isSelectedGroupAllMutableLiveData.setValue(false);
        }
        return isSelectedGroupAllMutableLiveData;
    }

    public void setIsSelectedGroupAllMutableLiveData(boolean value) {
        getIsSelectedGroupAllMutableLiveData().setValue(value);
    }
}
