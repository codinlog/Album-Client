package com.codinlog.album.util;

import com.codinlog.album.bean.ImageBean;

import java.util.ArrayList;
import java.util.Iterator;

public class ClassifyUtil {
    public static ArrayList<Object> PhotoClassification(ArrayList<ImageBean> classifications){
        ArrayList<Object> classified = new ArrayList<>();
        Iterator<ImageBean> imageBeanIterator = classifications.iterator();
        int  i = 0;
        classified.add(new String("aaaaaaaa"));
        while (imageBeanIterator.hasNext()){
            classified.add(imageBeanIterator.next());
            i++;
            if (i %20 == 0)
                classified.add(new String("aaaaaaaa"));
        }
        return classified;
    }
}
