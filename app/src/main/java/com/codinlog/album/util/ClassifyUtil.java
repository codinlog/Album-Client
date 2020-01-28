package com.codinlog.album.util;

import com.codinlog.album.bean.ClassifiedResBean;
import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.codinlog.album.util.WorthStoreUtil.errorCode;
import static com.codinlog.album.util.WorthStoreUtil.photoIsNew;
import static com.codinlog.album.util.WorthStoreUtil.photoIsRepeat;

public class ClassifyUtil {
    public static void PhotoClassification(List<PhotoBean> classifications) {
        if (classifications == null)
            return;
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Object> classifiedPhotoResList = new ArrayList<>();
        Map<String, PhotoSelectedNumBean> classifiedPhotoResNumMap = new HashMap<>();
        Map<String, List<PhotoBean>> classifiedPhotoResMap = new TreeMap<>((o1, o2) -> {
            try {
                if (simpleDateFormat.parse(o1).getTime() > simpleDateFormat.parse(o2).getTime())
                    return -1;
                else
                    return 1;
            } catch (ParseException e) {
                return 1;
            }
        });
        Collections.sort(classifications);
        for (PhotoBean photoBean : classifications) {
            String key = simpleDateFormat.format(photoBean.getTokenDate());
            boolean keyContains = false;
            for (Map.Entry<String, List<PhotoBean>> entry : classifiedPhotoResMap.entrySet()) {
                String tempKey = entry.getKey();
                if (tempKey.equals(key)) {
                    entry.getValue().add(photoBean);
                    keyContains = true;
                    break;
                }
            }
            if (!keyContains) {
                ArrayList<PhotoBean> tempArrayList = new ArrayList<>();
                tempArrayList.add(photoBean);
                classifiedPhotoResMap.put(key, tempArrayList);
            }
        }
        for (Map.Entry<String, List<PhotoBean>> entry : classifiedPhotoResMap.entrySet()) {
            String key = entry.getKey();
            List<PhotoBean> tempArrayList = entry.getValue();
            Collections.sort(tempArrayList);
            classifiedPhotoResList.add(new GroupBean().setGroupId(key));
            classifiedPhotoResNumMap.put(key, PhotoSelectedNumBean.newInstance().setSize(tempArrayList.size()));
            for (PhotoBean photoBean : tempArrayList) {
                classifiedPhotoResList.add(photoBean.setGroupId(key));
            }
        }
        ClassifiedResBean.getInstance()
                .setClassifiedPhotoBeanResList(classifications)
                .setClassifiedPhotoResList(classifiedPhotoResList)
                .setClassifiedPhotoResMap(classifiedPhotoResMap)
                .setClassifiedPhotoResNumMap(classifiedPhotoResNumMap);
    }

    public static int isPhotoRepeat(List<PhotoBean> photoBeans, String path) {
        if (photoBeans == null)
            return errorCode;
        Iterator<PhotoBean> iterator = photoBeans.iterator();
        while (iterator.hasNext()) {
            PhotoBean photoBean = iterator.next();
            if (photoBean.getPath().equals(path)) {
                photoBean.setDelete(false);
                return photoIsRepeat;
            }
        }
        return photoIsNew;
    }

    public static void removeDeleteImage(List<PhotoBean> photoBeans, boolean isResetStatus) {
        if (photoBeans == null)
            return;
        Iterator<PhotoBean> iterator = photoBeans.iterator();
        while (iterator.hasNext()) {
            PhotoBean photoBean = iterator.next();
            if (isResetStatus) {
                photoBean.setDelete(true);
            } else if (photoBean.isDelete()) {
                iterator.remove();
            }
        }
    }
}
