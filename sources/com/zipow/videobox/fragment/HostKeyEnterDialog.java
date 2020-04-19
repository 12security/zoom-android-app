package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class HostKeyEnterDialog extends ZMDialogFragment implements OnEditorActionListener, TextWatcher {
    @NonNull
    public static String ARGS_HOST_NAME = "arg_host_name";
    private Button mBtnOK = null;
    private EditText mEditHostKey;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static void show(FragmentManager fragmentManager, String str, String str2) {
        Bundle bundle = new Bundle();
        HostKeyEnterDialog hostKeyEnterDialog = new HostKeyEnterDialog();
        bundle.putString(ARGS_HOST_NAME, str);
        hostKeyEnterDialog.setArguments(bundle);
        hostKeyEnterDialog.show(fragmentManager, str2);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_enter_hostkey, null, false);
        this.mEditHostKey = (EditText) inflate.findViewById(C4558R.C4560id.txtHostKey);
        EditText editText = this.mEditHostKey;
        if (editText != null) {
            editText.setImeOptions(2);
            this.mEditHostKey.setOnEditorActionListener(this);
            this.mEditHostKey.addTextChangedListener(this);
            this.mEditHostKey.requestFocus();
        }
        return new Builder(getActivity()).setCancelable(true).setTitle(C4558R.string.zm_title_enter_hostkey).setView(inflate).setPositiveButton(C4558R.string.zm_btn_claim, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        this.mBtnOK = ((ZMAlertDialog) getDialog()).getButton(-1);
        Button button = this.mBtnOK;
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (HostKeyEnterDialog.this.checkInput()) {
                        HostKeyEnterDialog.this.onClickBtnOK();
                    }
                }
            });
        }
        updateButtons();
    }

    private void updateButtons() {
        if (checkInput()) {
            this.mBtnOK.setEnabled(true);
        } else {
            this.mBtnOK.setEnabled(false);
        }
    }

    /* access modifiers changed from: private */
    public boolean checkInput() {
        EditText editText = this.mEditHostKey;
        if (editText == null) {
            return false;
        }
        String trim = editText.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(trim) || trim.length() < 6 || trim.length() > 10) {
            return false;
        }
        try {
            Long.parseLong(trim);
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnOK() {
        String obj = this.mEditHostKey.getText().toString();
        if (!StringUtil.isEmptyOrNull(obj)) {
            dismissAllowingStateLoss();
            if (!ConfMgr.getInstance().verifyHostKey(obj)) {
                HostKeyErrorDialog.show(getFragmentManager());
                return;
            }
            ConfActivityNormal confActivityNormal = (ConfActivityNormal) getActivity();
            if (confActivityNormal != null) {
                confActivityNormal.showVerifyHostKeyDialog();
            }
        }
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickBtnOK();
        return true;
    }

    public void afterTextChanged(Editable editable) {
        updateButtons();
    }
}
