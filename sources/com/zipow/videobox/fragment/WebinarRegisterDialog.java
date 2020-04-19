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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class WebinarRegisterDialog extends ZMDialogFragment implements TextWatcher, OnEditorActionListener {
    private static final String ARG_EMAIL = "email";
    private static final String ARG_INSTRUCTION = "instruction";
    private static final String ARG_SCREEN_NAME = "screenName";
    private Button mBtnOK = null;
    private EditText mEdtEmail = null;
    private EditText mEdtScreenName = null;
    private TextView mInstruction;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static void show(FragmentManager fragmentManager, String str, String str2, int i) {
        WebinarRegisterDialog webinarRegisterDialog = new WebinarRegisterDialog();
        Bundle bundle = new Bundle();
        bundle.putString("screenName", str);
        bundle.putString("email", str2);
        bundle.putInt(ARG_INSTRUCTION, i);
        webinarRegisterDialog.setArguments(bundle);
        webinarRegisterDialog.show(fragmentManager, WebinarRegisterDialog.class.getName());
    }

    public WebinarRegisterDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        CharSequence charSequence;
        int i;
        String str = null;
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_webinar_register, null, false);
        this.mInstruction = (TextView) inflate.findViewById(C4558R.C4560id.txtInstructions);
        this.mEdtScreenName = (EditText) inflate.findViewById(C4558R.C4560id.edtScreenName);
        this.mEdtEmail = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        this.mEdtEmail.setImeOptions(2);
        this.mEdtEmail.setOnEditorActionListener(this);
        this.mEdtScreenName.addTextChangedListener(this);
        this.mEdtEmail.addTextChangedListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            str = arguments.getString("screenName");
            charSequence = arguments.getString("email");
            i = arguments.getInt(ARG_INSTRUCTION);
        } else {
            charSequence = null;
            i = 0;
        }
        if (bundle == null) {
            if (str != null) {
                this.mEdtScreenName.setText(str);
            }
            if (charSequence != null) {
                this.mEdtEmail.setText(charSequence);
            }
            if (i != 0) {
                this.mInstruction.setText(i);
            }
        }
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
                    if (WebinarRegisterDialog.this.isValidInput()) {
                        WebinarRegisterDialog.this.onClickBtnOK();
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
            confActivity.confirmWebinarRegisterInfo(null, null, true);
        }
    }

    /* access modifiers changed from: private */
    public void onClickBtnOK() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mBtnOK);
        String trim = this.mEdtEmail.getText().toString().trim();
        String trim2 = this.mEdtScreenName.getText().toString().trim();
        if (trim2.length() == 0) {
            this.mEdtScreenName.requestFocus();
        } else if (trim.length() == 0) {
            this.mEdtEmail.requestFocus();
        } else {
            dismissAllowingStateLoss();
            ConfActivity confActivity = (ConfActivity) getActivity();
            if (confActivity != null) {
                confActivity.confirmWebinarRegisterInfo(trim2, trim, false);
            }
        }
    }

    public void afterTextChanged(Editable editable) {
        updateButtons();
    }

    private void updateButtons() {
        Button button = this.mBtnOK;
        if (button != null) {
            button.setEnabled(isValidInput());
        }
    }

    /* access modifiers changed from: private */
    public boolean isValidInput() {
        return !StringUtil.isEmptyOrNull(this.mEdtScreenName.getText().toString().trim()) && StringUtil.isValidEmailAddress(this.mEdtEmail.getText().toString().trim());
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickBtnOK();
        return true;
    }
}
