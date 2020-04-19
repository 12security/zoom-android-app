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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class CreateProfileFragment extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    private static final String ARG_CODE = "code";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_FIRST_NAME = "firstName";
    private static final String ARG_LAST_NAME = "lastName";
    private Button mBtnBack;
    private Button mBtnOK;
    @Nullable
    private String mCode = null;
    private EditText mEdtFirstName;
    private EditText mEdtLastName;
    private EditText mEdtPassword;
    private EditText mEdtVerifyPassword;
    @Nullable
    private String mEmail = null;
    @Nullable
    private String mFirstName = null;
    @Nullable
    private String mLastName = null;
    /* access modifiers changed from: private */
    public TextView mTxtError;
    /* access modifiers changed from: private */
    public boolean mVerifyFailed = false;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, String str, String str2, String str3, String str4) {
        SimpleActivity.show(zMActivity, CreateProfileFragment.class.getName(), buildArguments(str, str2, str3, str4), 0);
    }

    public static void showAsActivity(Fragment fragment, String str, String str2, String str3, String str4) {
        SimpleActivity.show(fragment, CreateProfileFragment.class.getName(), buildArguments(str, str2, str3, str4), 0);
    }

    @NonNull
    private static Bundle buildArguments(String str, String str2, String str3, String str4) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FIRST_NAME, str);
        bundle.putString(ARG_LAST_NAME, str2);
        bundle.putString("email", str3);
        bundle.putString("code", str4);
        return bundle;
    }

    public CreateProfileFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_create_profile, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnOK = (Button) inflate.findViewById(C4558R.C4560id.btnOK);
        this.mTxtError = (TextView) inflate.findViewById(C4558R.C4560id.txtError);
        this.mEdtPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtPassword);
        this.mEdtVerifyPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtVerifyPassword);
        EditText editText = (EditText) inflate.findViewById(C4558R.C4560id.edtEmail);
        this.mEdtFirstName = (EditText) inflate.findViewById(C4558R.C4560id.edtFirstName);
        this.mEdtLastName = (EditText) inflate.findViewById(C4558R.C4560id.edtLastName);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mEmail = arguments.getString("email");
            this.mCode = arguments.getString("code");
            this.mFirstName = arguments.getString(ARG_FIRST_NAME);
            this.mLastName = arguments.getString(ARG_LAST_NAME);
        }
        if (bundle == null) {
            if (editText != null) {
                String str = this.mEmail;
                if (str != null) {
                    editText.setText(str);
                }
            }
            EditText editText2 = this.mEdtFirstName;
            if (editText2 != null) {
                String str2 = this.mFirstName;
                if (str2 != null) {
                    editText2.setText(str2);
                }
            }
            EditText editText3 = this.mEdtLastName;
            if (editText3 != null) {
                String str3 = this.mLastName;
                if (str3 != null) {
                    editText3.setText(str3);
                }
            }
        } else {
            this.mVerifyFailed = bundle.getBoolean("mVerifyFailed");
        }
        this.mBtnBack.setOnClickListener(this);
        this.mBtnOK.setOnClickListener(this);
        C25181 r4 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                CreateProfileFragment.this.updateOKButton();
                CreateProfileFragment.this.mVerifyFailed = false;
                CreateProfileFragment.this.mTxtError.setVisibility(4);
            }
        };
        this.mEdtPassword.addTextChangedListener(r4);
        this.mEdtVerifyPassword.addTextChangedListener(r4);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateUI();
        PTUI.getInstance().addPTUIListener(this);
    }

    public void onPause() {
        super.onPause();
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
        if (validateInput()) {
            String obj = this.mEdtPassword.getText().toString();
            String obj2 = this.mEdtVerifyPassword.getText().toString();
            String trim = this.mEdtFirstName.getText().toString().trim();
            String trim2 = this.mEdtLastName.getText().toString().trim();
            if (!obj.equals(obj2)) {
                this.mVerifyFailed = true;
                this.mTxtError.setVisibility(0);
                return;
            }
            if (PTApp.getInstance().setPassword(false, this.mEmail, obj, this.mCode, trim, trim2)) {
                WaitingDialog.newInstance(C4558R.string.zm_msg_requesting_setpwd).show(getFragmentManager(), WaitingDialog.class.getName());
            } else {
                showSetPwdErrorDialog();
            }
        }
    }

    private void showSetPwdErrorDialog() {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_activate_account_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
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
        String trim = this.mEdtFirstName.getText().toString().trim();
        String trim2 = this.mEdtLastName.getText().toString().trim();
        if (obj.length() == 0 || obj2.length() == 0 || trim.length() == 0 || trim2.length() == 0) {
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

    private void onSetPasswordRet(long j) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            if (((int) j) != 0) {
                showSetPwdErrorDialog();
            } else {
                autoLogin();
            }
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
}
