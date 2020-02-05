package com.codinlog.album.listener;

public abstract class PhotoGroupListener extends CommonListener {

    @Override
    public void handleEvent(Object o) {}

    public abstract void handleEvent(int position, boolean isChecked);
}
