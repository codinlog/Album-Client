package com.codinlog.album.bean;

public class PhotoSelectedNumBean {
    private int size = 0;
    private int selected = 0;

    public static PhotoSelectedNumBean newInstance() {
        return new PhotoSelectedNumBean();
    }

    public int getSize() {
        return size;
    }

    public PhotoSelectedNumBean setSize(int size) {
        this.size = size;
        return this;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void add() {
        selected++;
    }

    public void sub() {
        if (selected > 0)
            selected--;
    }
}
