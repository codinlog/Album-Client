package com.codinlog.album.util;

import com.codinlog.album.bean.PhotoBean;

import java.util.List;

public class DataStoreUtil {
    private List<PhotoBean> allDisplayDataList;
    private List<PhotoBean> displayDataList;
    private List<PhotoBean> slidePlayDataList;

    private enum SingletonEnum{
        INSTANCE;
        DataStoreUtil dataStoreUtil;
        SingletonEnum(){
            dataStoreUtil = new DataStoreUtil();
        }
        public DataStoreUtil getDataStoreUtil(){
            return dataStoreUtil;
        }
    }
    public static DataStoreUtil getInstance(){
        return SingletonEnum.INSTANCE.getDataStoreUtil();
    }

    public List<PhotoBean> getAllDisplayDataList() {
        return allDisplayDataList;
    }

    public void setAllDisplayDataList(List<PhotoBean> allDisplayDataList) {
        this.allDisplayDataList = allDisplayDataList;
    }

    public List<PhotoBean> getSlidePlayDataList() {
        return slidePlayDataList;
    }

    public void setSlidePlayDataList(List<PhotoBean> slidePlayDataList) {
        this.slidePlayDataList = slidePlayDataList;
    }

    public List<PhotoBean> getDisplayDataList() {
        return displayDataList;
    }

    public void setDisplayDataList(List<PhotoBean> displayDataList) {
        this.displayDataList = displayDataList;
    }
}
