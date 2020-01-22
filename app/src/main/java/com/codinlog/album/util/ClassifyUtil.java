package com.codinlog.album.util;

import com.codinlog.album.bean.ImageBean;

import java.util.ArrayList;
import java.util.Iterator;

import static com.codinlog.album.util.WorthStoreUtil.photo_isNew;
import static com.codinlog.album.util.WorthStoreUtil.photo_isRepeat;

public class ClassifyUtil {
    public static ArrayList<Object> PhotoClassification(ArrayList<ImageBean> classifications) {
        ArrayList<Object> classified = new ArrayList<>();
        Iterator<ImageBean> imageBeanIterator = classifications.iterator();
        int i = 0;
        classified.add(new String("aaaaaaaa"));
        while (imageBeanIterator.hasNext()) {
            classified.add(imageBeanIterator.next());
            i++;
            if (i % 20 == 0)
                classified.add(new String("aaaaaaaa"));
        }
        return classified;
    }

    public static int isPhotoRepeat(ArrayList<ImageBean> objects, String path) {
        Iterator<ImageBean> iterator = objects.iterator();
        while (iterator.hasNext()) {
            ImageBean imageBean = iterator.next();
            if (imageBean.getPath().equals(path))
                return photo_isRepeat;
        }
        return photo_isNew;
    }

}
