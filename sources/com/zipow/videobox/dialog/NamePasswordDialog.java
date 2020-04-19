package com.zipow.videobox.dialog;

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
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class NamePasswordDialog extends ZMDialogFragment implements TextWatcher, OnEditorActionListener {
    public static final String ARG_INCORRECT_PSW = "passwordError";
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final String ARG_SHOW_PSW = "showPassword";
    public static final String ARG_SHOW_SCREEN_NAME = "showScreenName";
    private Button mBtnOK = null;
    private EditText mEdtPassword = null;
    private EditText mEdtScreenName = null;
    private boolean mbShowPassword = true;
    private boolean mbShowScreenName = true;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public NamePasswordDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        boolean z;
        String str = "";
        Bundle arguments = getArguments();
        if (arguments != null) {
            str = arguments.getString("screenName");
            z = arguments.getBoolean(ARG_INCORRECT_PSW, false);
            this.mbShowScreenName = arguments.getBoolean(ARG_SHOW_SCREEN_NAME, true);
            this.mbShowPassword = arguments.getBoolean(ARG_SHOW_PSW, true);
        } else {
            z = false;
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_name_password, null, false);
        this.mEdtScreenName = (EditText) inflate.findViewById(C4558R.C4560id.edtScreenName);
        this.mEdtPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtPassword);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtInstructions);
        View findViewById = inflate.findViewById(C4558R.C4560id.panelScreenName);
        View findViewById2 = inflate.findViewById(C4558R.C4560id.panelPassword);
        if (!this.mbShowScreenName) {
            findViewById.setVisibility(8);
        } else {
            this.mEdtScreenName.setText(str);
        }
        if (!this.mbShowPassword) {
            findViewById2.setVisibility(8);
        }
        if (z) {
            textView.setText(C4558R.string.zm_lbl_incorrect_meeting_password);
        } else if (this.mbShowScreenName && this.mbShowPassword) {
            textView.setText(C4558R.string.zm_lbl_name_password_instructions);
        } else if (this.mbShowScreenName) {
            textView.setText(C4558R.string.zm_lbl_name_instructions);
        } else if (this.mbShowPassword) {
            textView.setText(C4558R.string.zm_lbl_password_instructions);
        }
        if (this.mbShowScreenName) {
            this.mEdtScreenName.setImeOptions(2);
            this.mEdtScreenName.setOnEditorActionListener(this);
        }
        if (this.mbShowPassword && (!this.mbShowScreenName || !StringUtil.isEmptyOrNull(str))) {
            this.mEdtPassword.setImeOptions(2);
            this.mEdtPassword.setOnEditorActionListener(this);
        }
        this.mEdtScreenName.addTextChangedListener(this);
        this.mEdtPassword.addTextChangedListener(this);
        return new Builder(getActivity()).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
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
                    if (NamePasswordDialog.this.checkInput()) {
                        NamePasswordDialog.this.onClickBtnOK();
                    }
                }
            });
        }
        updateButtons();
    }

    public void onCancel(DialogInterface dialogInterface) {
        UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            ConfMgr.getInstance().onUserInputPassword("", "", true);
            ConfLocalHelper.leaveCall(confActivity);
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnOK() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
        String obj = this.mEdtPassword.getText().toString();
        String trim = this.mEdtScreenName.getText().toString().trim();
        if (this.mbShowPassword && obj.length() == 0) {
            this.mEdtPassword.requestFocus();
        } else if (!this.mbShowScreenName || trim.length() != 0) {
            dismissAllowingStateLoss();
            ConfActivity confActivity = (ConfActivity) getActivity();
            if (confActivity != null) {
                ConfLocalHelper.confirmNamePassword(confActivity, obj, trim);
            }
        } else {
            this.mEdtScreenName.requestFocus();
        }
    }

    public void afterTextChanged(Editable editable) {
        updateButtons();
    }

    private void updateButtons() {
        if (this.mBtnOK == null) {
            return;
        }
        if ((!this.mbShowScreenName || !StringUtil.isEmptyOrNull(this.mEdtScreenName.getText().toString())) && (!this.mbShowPassword || !StringUtil.isEmptyOrNull(this.mEdtPassword.getText().toString()))) {
            this.mBtnOK.setEnabled(true);
        } else {
            this.mBtnOK.setEnabled(false);
        }
    }

    /* access modifiers changed from: private */
    public boolean checkInput() {
        return (!this.mbShowScreenName || !StringUtil.isEmptyOrNull(this.mEdtScreenName.getText().toString())) && (!this.mbShowPassword || !StringUtil.isEmptyOrNull(this.mEdtPassword.getText().toString()));
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickBtnOK();
        return true;
    }
}
