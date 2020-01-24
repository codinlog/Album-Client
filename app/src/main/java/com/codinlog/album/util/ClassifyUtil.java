package com.codinlog.album.util;

import com.codinlog.album.bean.ImageBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.codinlog.album.util.WorthStoreUtil.photo_isNew;
import static com.codinlog.album.util.WorthStoreUtil.photo_isRepeat;

public class ClassifyUtil {
    public static ArrayList<Object> PhotoClassification(ArrayList<ImageBean> classifications) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Object> classified = new ArrayList<>();
        Map<String, ArrayList<ImageBean>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    if (simpleDateFormat.parse(o1).getTime() > simpleDateFormat.parse(o2).getTime())
                        return -1;
                    else
                        return 1;
                } catch (ParseException e) {
                    return 1;
                }
            }
        });
        Iterator<ImageBean> iterator = classifications.iterator();
        while (iterator.hasNext()) {
            ImageBean imageBean = iterator.next();
            String key = simpleDateFormat.format(imageBean.getTokenDate());
            boolean keyContains = false;
            for (Map.Entry<String, ArrayList<ImageBean>> entry : map.entrySet()) {
                String tempKey = entry.getKey();
                if (tempKey.equals(key)) {
                    entry.getValue().add(imageBean);
                    keyContains = true;
                    break;
                }
            }
            if(!keyContains) {
                ArrayList<ImageBean> tempArrayList = new ArrayList<>();
                tempArrayList.add(imageBean);
                map.put(key, tempArrayList);
            }
        }
        for (Map.Entry<String, ArrayList<ImageBean>> entry : map.entrySet()) {
            String key = entry.getKey();
            ArrayList<ImageBean> tempArrayList = entry.getValue();
            Collections.sort(tempArrayList);
            classified.add(key);
            for (ImageBean imageBean : tempArrayList) {
                classified.add(imageBean);
            }
        }
        return classified;
    }

    public static int isPhotoRepeat(ArrayList<ImageBean> imageBeans, String path) {
        Iterator<ImageBean> iterator = imageBeans.iterator();
        while (iterator.hasNext()) {
            ImageBean imageBean = iterator.next();
            if (imageBean.getPath().equals(path)) {
                imageBean.setDelete(false);
                return photo_isRepeat;
            }
        }
        return photo_isNew;
    }

    public static void removeDeleteImage(ArrayList<ImageBean> imageBeans, boolean isResetStatus) {
        Iterator<ImageBean> iterator = imageBeans.iterator();
        while (iterator.hasNext()) {
            ImageBean imageBean = iterator.next();
            if (isResetStatus) {
                imageBean.setDelete(true);
            } else if (imageBean.isDelete()) {
                iterator.remove();
            }
        }
    }

}
