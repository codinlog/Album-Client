package com.codinlog.album.bean;

import com.codinlog.album.util.ClassifyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ClassifiedResBean {
    private Map<String, List<PhotoBean>> classifiedPhotoResMap;
    private Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap;
    private List<Object> classifiedPhotoResList;

    private enum SingletonEnum{
        INSTANCE;
        ClassifiedResBean classifiedResBean;
        SingletonEnum(){
            classifiedResBean = new ClassifiedResBean();
        }

        public ClassifiedResBean getClassifiedResBean(){
            return classifiedResBean;
        }
    }
    public static ClassifiedResBean getInstance(){
        return SingletonEnum.INSTANCE.getClassifiedResBean();
    }

    public void loadClassifiedRes(List<PhotoBean> photoBeanList){
        ClassifyUtil.PhotoClassification(photoBeanList);
    }

    public Map<String, List<PhotoBean>> getClassifiedPhotoResMap() {
        return classifiedPhotoResMap == null ? new TreeMap<>() : classifiedPhotoResMap;
    }

    public ClassifiedResBean setClassifiedPhotoResMap(Map<String, List<PhotoBean>> classifiedPhotoResMap) {
        this.classifiedPhotoResMap = classifiedPhotoResMap;
        return this;
    }

    public List<Object> getClassifiedPhotoResList() {
        return classifiedPhotoResList == null ? new ArrayList<>() : classifiedPhotoResList;
    }

    public ClassifiedResBean setClassifiedPhotoResList(List<Object> classifiedPhotoResList) {
        this.classifiedPhotoResList = classifiedPhotoResList;
        return this;
    }

    public Map<String, PhotoSelectedNumBean> getClassifiedPhotoResNumMap() {
        return classifiedPhotoResNumMap;
    }

    public ClassifiedResBean setClassifiedPhotoResNumMap(Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap) {
        this.classifiedPhotoResNumMap = classifiedPhotoResNumMap;
        return SingletonEnum.INSTANCE.getClassifiedResBean();
    }
}
