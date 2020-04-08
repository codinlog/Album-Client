package com.codinlog.album.listener;

public interface PhotoGroupListener extends CommonListener {
    void handleEvent(Object o);

    void handleEvent(int position, boolean isChecked);
}
