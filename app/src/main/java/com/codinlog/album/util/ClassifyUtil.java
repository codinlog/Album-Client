package com.codinlog.album.util;

import android.annotation.SuppressLint;

import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.codinlog.album.util.WorthStoreUtil.errorCode;
import static com.codinlog.album.util.WorthStoreUtil.photoIsNew;
import static com.codinlog.album.util.WorthStoreUtil.photoIsRepeat;

public class ClassifyUtil {
    private final static SimpleDateFormat simpleDateFormat;
    private static boolean isFirstScanning;

    static {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        isFirstScanning = true;
    }

    public static  Map<GroupBean, List<PhotoBean>> PhotoBeansClassify(List<PhotoBean> photoBeans, Map<GroupBean, List<PhotoBean>> classifiedMap) {
        if (photoBeans == null || photoBeans.isEmpty())
            return classifiedMap;
        classifiedMap.forEach((k, v) -> {
            ClassifyUtil.removeDeletePhotoBeans(v, true);
        });
        for (PhotoBean photoBean : photoBeans) {
            GroupBean groupBean = GroupBean.newInstance(simpleDateFormat.format(photoBean.getTokenDate()));
            if (classifiedMap.containsKey(groupBean)) {
                boolean flag = false;
                for (PhotoBean bean : classifiedMap.get(groupBean)) {
                    if (flag = photoBean.equals(bean)) {
                        bean.setDelete(false);
                        break;
                    }
                }
                if (!flag) {
                    photoBean.setDelete(false);
                    classifiedMap.get(groupBean).add(photoBean);
                }
            } else {
                List<PhotoBean> photoBeanList = new ArrayList<>();
                photoBean.setDelete(false);
                photoBeanList.add(photoBean);
                classifiedMap.put(groupBean, photoBeanList);
            }
        }
        classifiedMap.forEach((k, v) -> {
            ClassifyUtil.removeDeletePhotoBeans(v, false);
        });
        classifiedMap.forEach((k,v)->{
            k.setHaveNum(v.size());
            k.setSelectNum(v.stream().filter(PhotoBean::isSelected).collect(Collectors.toList()).size());
            k.setSelected(k.getHaveNum() <= k.getSelectNum());
            v.forEach(it -> it.setGroupId(k.getGroupId()));
            Collections.sort(v);
        });
        Iterator<GroupBean> iterator = classifiedMap.keySet().iterator();
        while (iterator.hasNext()){
            GroupBean groupBean = iterator.next();
            if(groupBean.getHaveNum() <= 0)
                iterator.remove();
        }
        return classifiedMap;
    }

    public static int isPhotoRepeat(List<PhotoBean> photoBeans, String path) {
        if (photoBeans == null || photoBeans.isEmpty())
            return errorCode;
        for (PhotoBean photoBean : photoBeans) {
            if (photoBean.getPhotoPath().equals(path)) {
                photoBean.setDelete(false);
                return photoIsRepeat;
            }
        }
        return photoIsNew;
    }

    public static boolean removeDeletePhotoBeans(List<PhotoBean> photoBeans, boolean isResetStatus) {
        if (photoBeans == null || photoBeans.isEmpty())
            return false;
        Iterator<PhotoBean> iterator = photoBeans.iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            PhotoBean photoBean = iterator.next();
            if (isResetStatus) {
                photoBean.setDelete(true);
            } else if (photoBean.isDelete()) {
                iterator.remove();
                flag = true;
            }
        }
        return flag;
    }
}
