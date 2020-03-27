package com.codinlog.album.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.codinlog.album.bean.GroupBean;
import com.codinlog.album.bean.PhotoBean;
import com.codinlog.album.bean.PhotoSelectedNumBean;
import com.codinlog.album.bean.kotlin.FolderBean;

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

    public static Map<GroupBean, List<PhotoBean>> PhotoBeansClassify(List<PhotoBean> photoBeans, Map<GroupBean, List<PhotoBean>> classifiedMap) {
        if (photoBeans == null || photoBeans.isEmpty())
            return classifiedMap;
        classifiedMap.forEach((k, v) -> {
            ClassifyUtil.removeDeletePhotoBeans(v, true);
        });
        for (PhotoBean photoBean : photoBeans) {
            photoBean.setDelete(false);
            GroupBean groupBean = GroupBean.newInstance(simpleDateFormat.format(photoBean.getTokenDate()));
            if (classifiedMap.containsKey(groupBean)) {
                if (!classifiedMap.get(groupBean).contains(photoBean))
                    classifiedMap.get(groupBean).add(photoBean);
            } else {
                List<PhotoBean> photoBeanList = new ArrayList<>();
                photoBeanList.add(photoBean);
                classifiedMap.put(groupBean, photoBeanList);
            }
        }
        classifiedMap.forEach((k, v) -> {
            ClassifyUtil.removeDeletePhotoBeans(v, false);
        });
        classifiedMap.forEach((k, v) -> {
            k.setHaveNum(v.size());
            k.setSelectNum(v.stream().filter(PhotoBean::isSelected).collect(Collectors.toList()).size());
            k.setSelected(k.getHaveNum() <= k.getSelectNum());
            v.forEach(it -> it.setGroupId(k.getGroupId()));
            Collections.sort(v);
        });
        Iterator<GroupBean> iterator = classifiedMap.keySet().iterator();
        while (iterator.hasNext()) {
            GroupBean groupBean = iterator.next();
            if (groupBean.getHaveNum() <= 0)
                iterator.remove();
        }
        return classifiedMap;
    }

    public static Map<FolderBean, List<PhotoBean>> PhotoBeansFolderClassify(List<PhotoBean> photoBeans, Map<FolderBean, List<PhotoBean>> classifiedFolderMap) {
        if (classifiedFolderMap == null)
            classifiedFolderMap = new LinkedHashMap<>();
        classifiedFolderMap.forEach((k, v) -> {
            ClassifyUtil.removeDeletePhotoBeans(v, true);
        });
        for (PhotoBean photoBean : photoBeans) {
            photoBean.setDelete(false);
            String folder = getFolderString(photoBean.getPhotoPath().toCharArray(), '/');
            FolderBean folderBean = new FolderBean(folder, 0, null);
            if (classifiedFolderMap.containsKey(folderBean)) {
                if (!classifiedFolderMap.get(folderBean).contains(photoBean)) {
                    classifiedFolderMap.get(folderBean).add(photoBean);
                }
            } else {
                List<PhotoBean> tempList = new ArrayList<>();
                tempList.add(photoBean);
                classifiedFolderMap.put(folderBean, tempList);
            }
        }
        classifiedFolderMap.forEach((k, v) -> {
            ClassifyUtil.removeDeletePhotoBeans(v, false);
            Collections.sort(v);
            k.setFolderNum(v.size());
            if (v.size() > 0)
                k.setPhotoBean(v.get(0));
        });
        Iterator<FolderBean> iterator = classifiedFolderMap.keySet().iterator();
        while (iterator.hasNext()) {
            FolderBean folderBean = iterator.next();
            if (folderBean.getFolderNum() <= 0)
                iterator.remove();
        }
        return classifiedFolderMap;
    }

    public static String getFolderString(char[] str, char split) {
        int length = str.length;
        int firstIndex = -1, lastIndex = -1;
        StringBuilder folder = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            if (str[i] == split) {
                if (lastIndex < 0)
                    lastIndex = i;
                else
                    firstIndex = i;
            }
            if (firstIndex > 0)
                break;
        }
        for (int i = firstIndex + 1; i < lastIndex; i++)
            folder.append(str[i]);
        return folder.toString();
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
