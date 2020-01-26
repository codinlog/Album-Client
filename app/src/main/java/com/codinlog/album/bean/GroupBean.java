package com.codinlog.album.bean;

public class GroupBean {
    private String groupId;
    private boolean isSelected = false;

    public String getGroupId() {
        return groupId;
    }

    public GroupBean setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
