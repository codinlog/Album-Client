package com.codinlog.album.bean;

import androidx.annotation.Nullable;

public class GroupBean implements Comparable {
    private String groupId;
    private boolean isSelected = false;
    private int haveNum = 0;
    private int selectNum = 0;

    private GroupBean(String groupId) {
        this.groupId = groupId;
        this.isSelected = false;
    }

    public static GroupBean newInstance(String groupId) {
        return new GroupBean(groupId);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getHaveNum() {
        return haveNum;
    }

    public void setHaveNum(int haveNum) {
        this.haveNum = haveNum;
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    @Override
    public int hashCode() {
        if (groupId != null)
            return groupId.hashCode();
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof GroupBean) {
            GroupBean groupBean = (GroupBean) obj;
            return this.groupId.equals(groupBean.getGroupId());
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof GroupBean) {
            GroupBean groupBean = (GroupBean) o;
            return groupBean.getGroupId().compareTo(groupId);
        }
        return 0;
    }
}
