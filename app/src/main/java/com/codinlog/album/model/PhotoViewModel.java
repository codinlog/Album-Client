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

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Object>> classifiedResListMutableLiveData; //图片+分组数据
    private MutableLiveData<Map<String, List<PhotoBean>>> classifiedResMapMutableLiveData;//归类的图片数据
    private MutableLiveData<List<Integer>> selectedMutableLiveData;//已选择
    private MutableLiveData<Map<String, PhotoSelectedNumBean>> classifiedPhotoResNumMapMutableLiveData;//选择数量变化
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

    public void setClassifiedResListMutableLiveData(List<Object> value) {
        Iterator<Object> iteratorNewValue = value.iterator();
        getSelectedMutableLiveData().getValue().clear();
        while (iteratorNewValue.hasNext()) {
            Object o1 = iteratorNewValue.next();
            if (o1 instanceof GroupBean) {
                GroupBean groupBeanNew = (GroupBean) o1;
                Iterator<Object> iteratorOldValue = getClassifiedResListMutableLiveData().getValue().iterator();
                while (iteratorOldValue.hasNext()) {
                    Object o2 = iteratorOldValue.next();
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
                    getSelectedMutableLiveData().getValue().add(photoBean.getPhotoId());
            }
        }
        getSelectedMutableLiveData().setValue(getSelectedMutableLiveData().getValue());
        getClassifiedResListMutableLiveData().setValue(value);
    }

    public void modeChangeToNormal() {
        if (getSelectedMutableLiveData().getValue() == null)
            return;
        Iterator<Object> iterator = getClassifiedResListMutableLiveData().getValue().iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof PhotoBean) {
                PhotoBean photoBean = (PhotoBean) o;
                photoBean.setSelected(false);
            } else if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                groupBean.setSelected(false);
            }
        }
        for (Map.Entry<String, PhotoSelectedNumBean> entry : getClassifiedPhotoResNumMapMutableLiveData().getValue().entrySet())
            entry.getValue().setSelected(0);
        getSelectedMutableLiveData().getValue().clear();
        getSelectedMutableLiveData().setValue(getSelectedMutableLiveData().getValue());
    }

    public MutableLiveData<List<Integer>> getSelectedMutableLiveData() {
        if (selectedMutableLiveData == null) {
            selectedMutableLiveData = new MutableLiveData<>();
            selectedMutableLiveData.setValue(new ArrayList<>());
        }
        return selectedMutableLiveData;
    }

    public void addSelectMutableLiveData(PhotoBean photoBean) {
        Iterator<Integer> iterator = getSelectedMutableLiveData().getValue().iterator();
        while (iterator.hasNext()) {
            if (photoBean.getPhotoId() == iterator.next()) {
                return;
            }
        }
        selectChangeCount(photoBean.getGroupId(), true);
        getSelectedMutableLiveData().getValue().add(photoBean.getPhotoId());
    }

    public void removeSelectMutableLiveData(PhotoBean photoBean) {
        Iterator<Integer> iterator = getSelectedMutableLiveData().getValue().iterator();
        while (iterator.hasNext()) {
            if (photoBean.getPhotoId() == iterator.next()) {
                selectChangeCount(photoBean.getGroupId(), false);
                iterator.remove();
                break;
            }
        }
    }

    public void changeSelectMutableLiveData(int position, boolean isRepeat, boolean isGroupAll, boolean isAllGroup) {
        if (position >= getClassifiedResListMutableLiveData().getValue().size())
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
            if (classifiedResListMutableLiveData.getValue().get(position) instanceof GroupBean && !isAllGroup)
                return;
            changeSelectMutableLiveData(position++, true, isGroupAll, isAllGroup);
        }
        getSelectedMutableLiveData().setValue(getSelectedMutableLiveData().getValue());
    }

    public void setClassifiedResMapMutableLiveData(Map<String, List<PhotoBean>> value) {
        if (classifiedResMapMutableLiveData == null)
            classifiedResMapMutableLiveData = new MutableLiveData<>();
        classifiedResMapMutableLiveData.setValue(value);
    }

    public MutableLiveData<Map<String, PhotoSelectedNumBean>> getClassifiedPhotoResNumMapMutableLiveData() {
        if (classifiedPhotoResNumMapMutableLiveData == null) {
            classifiedPhotoResNumMapMutableLiveData = new MutableLiveData<>();
            classifiedPhotoResNumMapMutableLiveData.setValue(new HashMap<>());
        }
        return classifiedPhotoResNumMapMutableLiveData;
    }

    public void setClassifiedPhotoResNumMapMutableLiveData(Map<String, PhotoSelectedNumBean> value) {
        Map<String, PhotoSelectedNumBean> oldValue = getClassifiedPhotoResNumMapMutableLiveData().getValue();
        int countFlag = 0;
        for (Map.Entry<String, PhotoSelectedNumBean> entry : value.entrySet()) {
            if (oldValue.containsKey(entry.getKey()))
                if (entry.getValue().getSize() < oldValue.get(entry.getKey()).getSelected()) {
                    entry.getValue().setSelected(entry.getValue().getSize());
                    countFlag += entry.getValue().getSize();
                }
                else {
                    entry.getValue().setSelected(oldValue.get(entry.getKey()).getSelected());
                    countFlag += oldValue.get(entry.getKey()).getSelected();
                }
        }
        getClassifiedPhotoResNumMapMutableLiveData().setValue(value);
        selectChangeCount("null", false);
    }

    public void selectChangeCount(String key, boolean isAdd) {
        if (key != null && getClassifiedPhotoResNumMapMutableLiveData().getValue().containsKey(key)) {
            PhotoSelectedNumBean photoSelectedNumBean = getClassifiedPhotoResNumMapMutableLiveData().getValue().get(key);
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
                if (getClassifiedPhotoResNumMapMutableLiveData().getValue().containsKey(groupBean.getGroupId())) {
                    PhotoSelectedNumBean photoSelectedNumBean = getClassifiedPhotoResNumMapMutableLiveData().getValue().get(groupBean.getGroupId());
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

    public void resetSelectChangeCount() {
        if (getClassifiedPhotoResNumMapMutableLiveData().getValue() != null) {
            for (PhotoSelectedNumBean photoSelectedNumBean : getClassifiedPhotoResNumMapMutableLiveData().getValue().values())
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

    public void setIsSelectedAllGroupMutableLiveData(boolean value) {
        getIsSelectedAllGroupMutableLiveData().setValue(value);
    }

    public void setIsSelectAllGroupFromMainViewMode(boolean value) {
        setIsSelectedAllGroupMutableLiveData(value);
        changeSelectMutableLiveData(0, true, false, true);
    }

    public MutableLiveData<Boolean> getIsSelectedGroupAllMutableLiveData() {
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
