package com.codinlog.album.util;

import com.codinlog.album.bean.PhotoBean;

import java.util.ArrayList;
import java.util.List;

public class DataStoreUtil {
    private List<PhotoBean> allDisplayData;
    private List<PhotoBean> displayData;
    private List<PhotoBean> slidePlayData;
    private List<PhotoBean> folderDisplayData;

    public static DataStoreUtil getInstance() {
        return SingletonEnum.INSTANCE.getDataStoreUtil();
    }

    public List<PhotoBean> getAllDisplayData() {
        return allDisplayData;
    }

    public void setAllDisplayData(List<PhotoBean> allDisplayData) {
        this.allDisplayData = allDisplayData;
    }

    public List<PhotoBean> getSlidePlayData() {
        return slidePlayData;
    }

    public void setSlidePlayData(List<PhotoBean> slidePlayData) {
        this.slidePlayData = slidePlayData;
    }

    public List<PhotoBean> getDisplayData() {
        return displayData;
    }

    public void setDisplayData(List<PhotoBean> displayData) {
        this.displayData = displayData;
    }

    public List<PhotoBean> getFolderDisplayData() {
        if (folderDisplayData == null)
            folderDisplayData = new ArrayList<>();
        return folderDisplayData;
    }

    public void setFolderDisplayData(List<PhotoBean> photoBeans) {
        folderDisplayData = photoBeans;
    }

    private enum SingletonEnum {
        INSTANCE;
        DataStoreUtil dataStoreUtil;

        SingletonEnum() {
            dataStoreUtil = new DataStoreUtil();
        }

        public DataStoreUtil getDataStoreUtil() {
            return dataStoreUtil;
        }
    }

}
