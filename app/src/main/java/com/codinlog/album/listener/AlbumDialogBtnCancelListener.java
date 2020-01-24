package com.codinlog.album.listener;

import com.codinlog.album.widget.AlbumDialog;

public class AlbumDialogBtnCancelListener extends AlbumDialogBaseListener {
    @Override
    public void handleEvent(Object o) {
        if (o instanceof AlbumDialog){
            AlbumDialog albumDialog = (AlbumDialog) o;
            albumDialog.dismiss();
        }
    }
}
