package com.codinlog.album.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codinlog.album.R;
import com.codinlog.album.listener.CommonListener;

import java.util.List;

public class AlbumDialog extends Dialog {
    private TextView tvTitle;
    private Button btn_cancel,btn_ok;
    private AutoCompleteTextView autoCompleteTextView;
    private String title;
    private CommonListener btnOkListener;
    private CommonListener btnCancelListener;
    private TextWatcher watcher;
    private ArrayAdapter<String> arrayAdapter;

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
        autoCompleteTextView = findViewById(R.id.editTextAlbum);
        doInit();
    }

    private void doInit() {
        btn_ok.setOnClickListener(v -> btnOkListener.handleEvent(AlbumDialog.this));
        btn_cancel.setOnClickListener(v -> btnCancelListener.handleEvent(AlbumDialog.this));
        //autoCompleteTextView.addTextChangedListener(watcher);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        if (title != null)
            tvTitle.setText(title);
    }

    public AlbumDialog setBtnOkListener(CommonListener listener) {
        this.btnOkListener = listener;
        return this;
    }

    public AlbumDialog setBtnCancelListener(CommonListener listener) {
        this.btnCancelListener = listener;
        return this;
    }

    public AlbumDialog setEditTextInputChangeListener(TextWatcher watcher) {
        this.watcher = watcher;
        return this;
    }

    public AlbumDialog setTvTitle(String title) {
        this.title = title;
        return this;
    }

    public AlbumDialog setNoticeAdapterData(List<String> data){
        arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,data);
        return this;
    }

    public String getInputContent(){
        return autoCompleteTextView.getText().toString();
    }

    class OnKeyDownListener implements DialogInterface.OnKeyListener {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                dialog.dismiss();
                return true;
            }
            return false;
        }
    }
}
