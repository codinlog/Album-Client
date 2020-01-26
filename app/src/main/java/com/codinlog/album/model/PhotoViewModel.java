package com.codinlog.album.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectNumBean;
import com.codinlog.album.util.WorthStoreUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.codinlog.album.util.WorthStoreUtil.allGroupSelected;
import static com.codinlog.album.util.WorthStoreUtil.groupNotAllSelected;

public class PhotoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<Object>> classifiedResListMutableLiveData; //图片+分组数据
    private MutableLiveData<Map<String, List<PhotoBean>>> classifiedResMapMutableLiveData;//归类的图片数据
    private MutableLiveData<List<Integer>> selectMutableLiveData;//已选择
    private MutableLiveData<Map<String, PhotoSelectNumBean>> classifiedPhotoResNumMapMutableLiveData;//选择数量变化
    private MutableLiveData<Boolean> isSelectAllGroupMutableLiveData;
    private MutableLiveData<Boolean> isSelectGroupAllMutableLiveData;
    public MainViewModel mainViewModel;

    public MutableLiveData<List<Object>> getClassifiedResListMutableLiveData() {
        if (classifiedResListMutableLiveData == null) {
            classifiedResListMutableLiveData = new MutableLiveData<>();
            classifiedResListMutableLiveData.setValue(new ArrayList<Object>());
        }
        return classifiedResListMutableLiveData;
    }

    public void setClassifiedResListMutableLiveData(List<Object> value) {
        getClassifiedResListMutableLiveData().setValue(value);
    }

    public void modeChangeToNormal() {
        if (getSelectMutableLiveData().getValue() == null)
            return;
        Iterator<Object> iterator = getClassifiedResListMutableLiveData().getValue().iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof PhotoBean) {
                PhotoBean photoBean = (PhotoBean) o;
                if (photoBean.isSelected()) {
                    photoBean.setSelected(!photoBean.isSelected());
                }
            } else if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                if (groupBean.isSelected())
                    groupBean.setSelected(!groupBean.isSelected());
            }
        }
        getSelectMutableLiveData().getValue().clear();
    }

    public MutableLiveData<List<Integer>> getSelectMutableLiveData() {
        if (selectMutableLiveData == null) {
            selectMutableLiveData = new MutableLiveData<>();
            selectMutableLiveData.setValue(new ArrayList<Integer>());
        }
        return selectMutableLiveData;
    }

    public void addSelectMutableLiveData(PhotoBean photoBean) {
        Iterator<Integer> iterator = getSelectMutableLiveData().getValue().iterator();
        while (iterator.hasNext()) {
            if (photoBean.getPhotoId() == iterator.next()) {
                return;
            }
        }
        selectChangeCount(photoBean.getGroupId(), true);
        getSelectMutableLiveData().getValue().add(photoBean.getPhotoId());
    }

    public void removeSelectMutableLiveData(PhotoBean photoBean) {
        Iterator<Integer> iterator = getSelectMutableLiveData().getValue().iterator();
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
                    photoBean.setSelected(getIsSelectGroupAllMutableLiveData().getValue());
                else
                    photoBean.setSelected(getIsSelectAllGroupMutableLiveData().getValue());
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
                groupBean.setSelected(!groupBean.isSelected());
                ++position;
                isGroupAll = false;
            }
            if (classifiedResListMutableLiveData.getValue().get(position) instanceof GroupBean && !isAllGroup)
                return;
            changeSelectMutableLiveData(position++, true, isGroupAll, isAllGroup);
        }
        getSelectMutableLiveData().setValue(getSelectMutableLiveData().getValue());
    }

    public void setClassifiedResMapMutableLiveData(Map<String, List<PhotoBean>> value) {
        if (classifiedResMapMutableLiveData == null)
            classifiedResMapMutableLiveData = new MutableLiveData<>();
        classifiedResMapMutableLiveData.setValue(value);
    }

    public MutableLiveData<Map<String, PhotoSelectNumBean>> getClassifiedPhotoResNumMapMutableLiveData() {
        if (classifiedPhotoResNumMapMutableLiveData == null) {
            classifiedPhotoResNumMapMutableLiveData = new MutableLiveData<>();
            classifiedPhotoResNumMapMutableLiveData.setValue(new HashMap<String, PhotoSelectNumBean>());
        }
        return classifiedPhotoResNumMapMutableLiveData;
    }

    public void setClassifiedPhotoResNumMapMutableLiveData(Map<String, PhotoSelectNumBean> value) {
        getClassifiedPhotoResNumMapMutableLiveData().setValue(value);
    }

    public void selectChangeCount(String key, boolean isAdd) {
        if (getClassifiedPhotoResNumMapMutableLiveData().getValue().containsKey(key)) {
            PhotoSelectNumBean photoSelectNumBean = getClassifiedPhotoResNumMapMutableLiveData().getValue().get(key);
            if (isAdd)
                photoSelectNumBean.add();
            else
                photoSelectNumBean.sub();
        }
        Iterator<Object> iterator = getClassifiedResListMutableLiveData().getValue().iterator();
        boolean flag = true;
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (o instanceof GroupBean) {
                GroupBean groupBean = (GroupBean) o;
                if (getClassifiedPhotoResNumMapMutableLiveData().getValue().containsKey(groupBean.getGroupId())) {
                    PhotoSelectNumBean photoSelectNumBean = getClassifiedPhotoResNumMapMutableLiveData().getValue().get(groupBean.getGroupId());
                    if (photoSelectNumBean.getSize() <= photoSelectNumBean.getSelected())
                        groupBean.setSelected(true);
                    else {
                        groupBean.setSelected(false);
                        flag = false;
                    }
                }
            }
        }
        if (flag)
            mainViewModel.setIsSelectAllMutableLiveData(true);
        else
            mainViewModel.setIsSelectAllMutableLiveData(false);
    }

    public void resetSelectChangeCount() {
        if (getClassifiedPhotoResNumMapMutableLiveData().getValue() != null) {
            for (PhotoSelectNumBean photoSelectNumBean : getClassifiedPhotoResNumMapMutableLiveData().getValue().values())
                photoSelectNumBean.setSelected(0);
        }
    }

    public MutableLiveData<Boolean> getIsSelectAllGroupMutableLiveData() {
        if (isSelectAllGroupMutableLiveData == null) {
            isSelectAllGroupMutableLiveData = new MutableLiveData<>();
            isSelectAllGroupMutableLiveData.setValue(false);
        }
        return isSelectAllGroupMutableLiveData;
    }

    public void setIsSelectAllGroupMutableLiveData(boolean value) {
        getIsSelectAllGroupMutableLiveData().setValue(value);
    }

    public void setIsSelectAllGroupFromMainViewMode(boolean value) {
        setIsSelectAllGroupMutableLiveData(value);
        changeSelectMutableLiveData(0, true, false, true);
    }

    public MutableLiveData<Boolean> getIsSelectGroupAllMutableLiveData() {
        if (isSelectGroupAllMutableLiveData == null) {
            isSelectGroupAllMutableLiveData = new MutableLiveData<>();
            isSelectGroupAllMutableLiveData.setValue(false);
        }
        return isSelectGroupAllMutableLiveData;
    }

    public void setIsSelectGroupAllMutableLiveData(boolean value) {
        getIsSelectGroupAllMutableLiveData().setValue(value);
    }
}
