package com.codinlog.album.bean;

import com.codinlog.album.util.ClassifyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ClassifiedBean {
    private Map<String, List<PhotoBean>> classifiedPhotoResMap;
    private Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap;
    private List<Object> classifiedPhotoResList;
    private List<PhotoBean> classifiedPhotoBeanResList;

    private enum SingletonEnum{
        INSTANCE;
        ClassifiedBean classifiedBean;
        SingletonEnum(){
            classifiedBean = new ClassifiedBean();
        }

        public ClassifiedBean getClassifiedBean(){
            return classifiedBean;
        }
    }
    public static ClassifiedBean getInstance(){
        return SingletonEnum.INSTANCE.getClassifiedBean();
    }

    public void loadClassifiedRes(List<PhotoBean> photoBeanList){
        ClassifyUtil.PhotoClassification(photoBeanList);
    }

    public Map<String, List<PhotoBean>> getClassifiedPhotoResMap() {
        return classifiedPhotoResMap == null ? new TreeMap<>() : classifiedPhotoResMap;
    }

    public ClassifiedBean setClassifiedPhotoResMap(Map<String, List<PhotoBean>> classifiedPhotoResMap) {
        this.classifiedPhotoResMap = classifiedPhotoResMap;
        return SingletonEnum.INSTANCE.getClassifiedBean();
    }

    public List<Object> getClassifiedPhotoResList() {
        return classifiedPhotoResList == null ? new ArrayList<>() : classifiedPhotoResList;
    }

    public ClassifiedBean setClassifiedPhotoResList(List<Object> classifiedPhotoResList) {
        this.classifiedPhotoResList = classifiedPhotoResList;
        return SingletonEnum.INSTANCE.getClassifiedBean();
    }

    public Map<String, PhotoSelectedNumBean> getClassifiedPhotoResNumMap() {
        return classifiedPhotoResNumMap == null ? new HashMap<>() : classifiedPhotoResNumMap;
    }

    public ClassifiedBean setClassifiedPhotoResNumMap(Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap) {
        this.classifiedPhotoResNumMap = classifiedPhotoResNumMap;
        return SingletonEnum.INSTANCE.getClassifiedBean();
    }

    public List<PhotoBean> getClassifiedPhotoBeanResList() {
        return classifiedPhotoBeanResList;
    }

    public ClassifiedBean setClassifiedPhotoBeanResList(List<PhotoBean> classifiedPhotoBeanResList) {
        this.classifiedPhotoBeanResList = classifiedPhotoBeanResList;
        return SingletonEnum.INSTANCE.getClassifiedBean();
    }
}
