package com.codinlog.album.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codinlog.album.R;
import com.codinlog.album.listener.AlbumDialogBaseListener;

public class AlbumDialog extends Dialog {
    private TextView tvTitle;
    private Button btn_cancel;
    private Button btn_ok;
    private String title;
    AlbumDialogBaseListener albumDialogBaseListener_btnOk;
    AlbumDialogBaseListener albumDialogBaseListener_btnCancel;

    public AlbumDialog(@NonNull Context context) {
        super(context);
    }

    public AlbumDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AlbumDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addto_album_dialog);
        setCancelable(false);
        setOnKeyListener(new OnKeyDownListener());
        tvTitle = findViewById(R.id.tvTitle);
        btn_cancel = findViewById(R.id.btnCancel);
        btn_ok = findViewById(R.id.btnOk);
        doInit();
    }

    private void doInit() {
        btn_ok.setOnClickListener(v -> albumDialogBaseListener_btnOk.handleEvent(AlbumDialog.this.getOwnerActivity()));

        btn_cancel.setOnClickListener(v -> albumDialogBaseListener_btnCancel.handleEvent(AlbumDialog.this));
        if(title != null)
            tvTitle.setText(title);
    }

    public AlbumDialog setBtnOkListener(AlbumDialogBaseListener albumDialogBaseListener) {
        this.albumDialogBaseListener_btnOk = albumDialogBaseListener;
        return this;
    }

    public AlbumDialog setBtnCancelListener(AlbumDialogBaseListener albumDialogBaseListener) {
        this.albumDialogBaseListener_btnCancel = albumDialogBaseListener;
        return this;
    }

    public AlbumDialog setTvTitle(String title) {
           this.title = title;
           return this;
    }

    class OnKeyDownListener implements DialogInterface.OnKeyListener{
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
                dialog.dismiss();
                return true;
            }
            return false;
        }
    }

}
