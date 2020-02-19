package com.codinlog.album.util;

import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataStoreUtil {
    private Map<String, List<PhotoBean>> classifiedPhotoResMap;//groupId,List<PhotoBean>
    private Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap; //groupId,group include item number
    private List<Object> classifiedPhotoResList;//object {String,PhotoBean}
    private List<PhotoBean> classifiedPhotoBeanResList;//classified all PhotoBean
    private List<PhotoBean> displayDataList;

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

    public void loadClassifiedRes(List<PhotoBean> photoBeanList){
        ClassifyUtil.PhotoClassification(photoBeanList);
    }

    public Map<String, List<PhotoBean>> getClassifiedPhotoResMap() {
        return classifiedPhotoResMap == null ? new TreeMap<>() : classifiedPhotoResMap;
    }

    public DataStoreUtil setClassifiedPhotoResMap(Map<String, List<PhotoBean>> classifiedPhotoResMap) {
        this.classifiedPhotoResMap = classifiedPhotoResMap;
        return SingletonEnum.INSTANCE.getDataStoreUtil();
    }

    public List<Object> getClassifiedPhotoResList() {
        return classifiedPhotoResList == null ? new ArrayList<>() : classifiedPhotoResList;
    }

    public DataStoreUtil setClassifiedPhotoResList(List<Object> classifiedPhotoResList) {
        this.classifiedPhotoResList = classifiedPhotoResList;
        return SingletonEnum.INSTANCE.getDataStoreUtil();
    }

    public Map<String, PhotoSelectedNumBean> getClassifiedPhotoResNumMap() {
        return classifiedPhotoResNumMap == null ? new HashMap<>() : classifiedPhotoResNumMap;
    }

    public DataStoreUtil setClassifiedPhotoResNumMap(Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap) {
        this.classifiedPhotoResNumMap = classifiedPhotoResNumMap;
        return SingletonEnum.INSTANCE.getDataStoreUtil();
    }

    public List<PhotoBean> getClassifiedPhotoBeanResList() {
        return classifiedPhotoBeanResList;
    }

    public DataStoreUtil setClassifiedPhotoBeanResList(List<PhotoBean> classifiedPhotoBeanResList) {
        this.classifiedPhotoBeanResList = classifiedPhotoBeanResList;
        return SingletonEnum.INSTANCE.getDataStoreUtil();
    }

    public List<PhotoBean> getDisplayDataList() {
        return displayDataList;
    }

    public void setDisplayDataList(List<PhotoBean> displayDataList) {
        this.displayDataList = displayDataList;
    }
}
