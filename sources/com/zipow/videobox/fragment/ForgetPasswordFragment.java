package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class ForgetPasswordFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private Button mBtnBack;
    private Button mBtnSendEmail;
    private EditText mEdtEmail;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void show(ZMActivity zMActivity) {
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, new ForgetPasswordFragment(), ForgetPasswordFragment.class.getName()).commit();
    }

    @Nullable
    public static ForgetPasswordFragment getForgetPasswordFragment(FragmentManager fragmentManager) {
        return (ForgetPasswordFragment) fragmentManager.findFragmentByTag(ForgetPasswordFragment.class.getName());
    }

    public ForgetPasswordFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_forgetpwd, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSendEmail = (Button) inflate.findViewById(C4558R.C4560id.btnSendEmail);
        this.mEdtEmail = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSendEmail.setOnClickListener(this);
        this.mEdtEmail.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ForgetPasswordFragment.this.updateSendEmailButton();
            }
        });
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateSendEmailButton();
        PTUI.getInstance().addPTUIListener(this);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSendEmail) {
            onClickSendEmailButton();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickSendEmailButton() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtEmail);
        if (validateInput()) {
            if (PTApp.getInstance().forgotPassword(this.mEdtEmail.getText().toString())) {
                WaitingDialog.newInstance(C4558R.string.zm_msg_requesting_forgot_pwd).show(getFragmentManager(), WaitingDialog.class.getName());
            } else {
                showForgotPwdErrorDialog();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateSendEmailButton() {
        this.mBtnSendEmail.setEnabled(validateInput());
    }

    private boolean validateInput() {
        return StringUtil.isValidEmailAddress(this.mEdtEmail.getText().toString());
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 42) {
            onForgotPasswordRet(j);
        }
    }

    private void onForgotPasswordRet(long j) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            int i = (int) j;
            if (i == 0 || i == 1001) {
                showSendEmailResultDialog();
            } else {
                showForgotPwdErrorDialog();
            }
        }
    }

    private void showForgotPwdErrorDialog() {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_resetpwd_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    private void showSendEmailResultDialog() {
        String obj = this.mEdtEmail.getText().toString();
        SimpleMessageDialog.newInstance(getString(C4558R.string.zm_msg_reset_pwd_email_sent_ret_52083, obj), getString(C4558R.string.zm_msg_resetpwd_email_sent_title), true).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }
}
