package com.codinlog.album.listener;

public abstract class PhotoGroupListener extends BaseListener {

    @Override
    public void handleEvent(int position) {

    }

    public abstract void handleEvent(int position, boolean isChecked);
}
