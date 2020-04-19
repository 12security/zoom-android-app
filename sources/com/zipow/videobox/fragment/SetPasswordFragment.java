package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class SetPasswordFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private static final String ARG_CODE = "code";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_UNAME = "uname";
    private Button mBtnBack;
    private Button mBtnOK;
    @Nullable
    private String mCode = null;
    private EditText mEdtPassword;
    private EditText mEdtVerifyPassword;
    @Nullable
    private String mEmail = null;
    /* access modifiers changed from: private */
    public TextView mTxtError;
    /* access modifiers changed from: private */
    public boolean mVerifyFailed = false;
    @Nullable
    private ProgressDialog mWaitingDialog;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void show(ZMActivity zMActivity, String str, String str2, String str3) {
        SetPasswordFragment setPasswordFragment = new SetPasswordFragment();
        setPasswordFragment.setArguments(buildArguments(str, str2, str3));
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, setPasswordFragment, SetPasswordFragment.class.getName()).commit();
    }

    @NonNull
    private static Bundle buildArguments(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_UNAME, str);
        bundle.putString("email", str2);
        bundle.putString("code", str3);
        return bundle;
    }

    public SetPasswordFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mEmail = arguments.getString("email");
            this.mCode = arguments.getString("code");
        }
        View inflate = layoutInflater.inflate(C4558R.layout.zm_resetpwd, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnOK = (Button) inflate.findViewById(C4558R.C4560id.btnOK);
        this.mTxtError = (TextView) inflate.findViewById(C4558R.C4560id.txtError);
        this.mEdtPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtPassword);
        this.mEdtVerifyPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtVerifyPassword);
        EditText editText = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        if (bundle != null) {
            this.mVerifyFailed = bundle.getBoolean("mVerifyFailed");
        } else if (editText != null) {
            String str = this.mEmail;
            if (str != null) {
                editText.setText(str);
            }
        }
        this.mBtnBack.setOnClickListener(this);
        this.mBtnOK.setOnClickListener(this);
        C29841 r3 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SetPasswordFragment.this.updateOKButton();
                SetPasswordFragment.this.mVerifyFailed = false;
                SetPasswordFragment.this.mTxtError.setVisibility(4);
            }
        };
        this.mEdtPassword.addTextChangedListener(r3);
        this.mEdtVerifyPassword.addTextChangedListener(r3);
        PTUI.getInstance().addPTUIListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mVerifyFailed", this.mVerifyFailed);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnOK) {
            onClickBtnOK();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickBtnOK() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        String obj = this.mEdtPassword.getText().toString();
        String obj2 = this.mEdtVerifyPassword.getText().toString();
        if (validateInput()) {
            if (!obj.equals(obj2)) {
                this.mVerifyFailed = true;
                this.mTxtError.setVisibility(0);
                return;
            }
            if (PTApp.getInstance().setPassword(true, this.mEmail, obj, this.mCode)) {
                showWaitingDialog(C4558R.string.zm_msg_requesting_setpwd);
            } else {
                showSetPwdErrorDialog();
            }
        }
    }

    private void showSetPwdErrorDialog() {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_resetpwd_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    private void updateUI() {
        if (this.mVerifyFailed) {
            this.mTxtError.setVisibility(0);
        } else {
            this.mTxtError.setVisibility(4);
        }
        updateOKButton();
    }

    /* access modifiers changed from: private */
    public void updateOKButton() {
        this.mBtnOK.setEnabled(validateInput());
    }

    private boolean validateInput() {
        String obj = this.mEdtPassword.getText().toString();
        String obj2 = this.mEdtVerifyPassword.getText().toString();
        if (obj.length() == 0 || obj2.length() == 0) {
            return false;
        }
        return true;
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 43) {
            onSetPasswordRet(j);
        }
    }

    private void onSetPasswordRet(final long j) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((SetPasswordFragment) iUIElement).handleOnSetPasswordRet(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnSetPasswordRet(long j) {
        dismissWaitingDialog();
        if (((int) j) != 0) {
            showSetPwdErrorDialog();
        } else {
            autoLogin();
        }
    }

    private void autoLogin() {
        dismiss();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            LoginActivity.show(zMActivity, false);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    private void showWaitingDialog(int i) {
        if (this.mWaitingDialog == null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                this.mWaitingDialog = UIUtil.showSimpleWaitingDialog((Activity) activity, i);
            }
        }
    }

    private void dismissWaitingDialog() {
        ProgressDialog progressDialog = this.mWaitingDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mWaitingDialog = null;
        }
    }
}
