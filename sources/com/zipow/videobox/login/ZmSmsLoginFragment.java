package com.zipow.videobox.login;

import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ZMVerifyCodeView;
import com.zipow.videobox.view.ZMVerifyCodeView.VerifyCodeCallBack;
import java.util.Arrays;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZmRegexUtils;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class ZmSmsLoginFragment extends ZMFragment implements OnClickListener, VerifyCodeCallBack {
    private static final String TAG = "com.zipow.videobox.login.ZmSmsLoginFragment";
    private Button mBtnSignIn;
    private EditText mEdtCode;
    private EditText mEdtNumber;
    private boolean mIsSignIng = false;
    private SimplePTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, long j) {
            if (i == 8) {
                ZmSmsLoginFragment.this.sinkIMLogin(j);
            } else if (i == 37) {
                ZmSmsLoginFragment.this.sinkWebAccessFail(j);
            } else if (i != 77) {
                switch (i) {
                    case 0:
                        ZmSmsLoginFragment.this.sinkWebLogin(j);
                        return;
                    case 1:
                        ZmSmsLoginFragment.this.sinkWebLogout(j);
                        return;
                    default:
                        return;
                }
            } else {
                ZmSmsLoginFragment.this.sinkSendSmsCode(j);
            }
        }
    };
    private TextView mTxtPrivacy;
    private ZMVerifyCodeView mZMVerifyCodeView;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (bundle != null && bundle.containsKey("mIsSignIng")) {
            this.mIsSignIng = bundle.getBoolean("mIsSignIng");
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_fragment_sms_login, null, false);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mZMVerifyCodeView = (ZMVerifyCodeView) inflate.findViewById(C4558R.C4560id.zmVerifyCodeView);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mEdtCode = (EditText) inflate.findViewById(C4558R.C4560id.edtCode);
        this.mBtnSignIn = (Button) inflate.findViewById(C4558R.C4560id.btnSignIn);
        this.mBtnSignIn.setOnClickListener(this);
        this.mTxtPrivacy = (TextView) inflate.findViewById(C4558R.C4560id.txtCnPrivacy);
        setUpView();
        this.mZMVerifyCodeView.setmVerifyCodeCallBack(this);
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mIsSignIng", this.mIsSignIng);
    }

    public void onDestroyView() {
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
        ZMVerifyCodeView zMVerifyCodeView = this.mZMVerifyCodeView;
        if (zMVerifyCodeView != null) {
            zMVerifyCodeView.setmVerifyCodeCallBack(null);
        }
        super.onDestroyView();
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBack();
        } else if (id == C4558R.C4560id.btnSignIn) {
            onClickSignIn();
        }
    }

    public void onClickSendCode() {
        if (ZmPtUtils.checkNetWork(this)) {
            String phoneNumber = PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
            if (!StringUtil.isEmptyOrNull(phoneNumber)) {
                int sendSMSCodeForLogin = PTApp.getInstance().sendSMSCodeForLogin(CountryCodeUtil.CHINA_COUNTRY_CODE, phoneNumber);
                this.mIsSignIng = false;
                if (sendSMSCodeForLogin == 0) {
                    WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
                } else {
                    SimpleMessageDialog.newInstance(C4558R.string.zm_msg_verify_phone_number_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sinkIMLogin(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkIMLogin", new EventAction("sinkIMLogin") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmSmsLoginFragment.this.handleOnIMLogin(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnIMLogin(long j) {
        if (j == 3 && !PTApp.getInstance().isAuthenticating()) {
            dismissWaiting();
        }
        if (ZmLoginHelper.isTypeSupportIM(11)) {
        }
    }

    /* access modifiers changed from: private */
    public void sinkWebLogout(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLogout", new EventAction("sinkWebLogout") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmSmsLoginFragment.this.handleOnWebLogout(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebLogout(long j) {
        dismissWaiting();
    }

    /* access modifiers changed from: private */
    public void sinkWebLogin(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLogin", new EventAction("sinkWebLogin") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmSmsLoginFragment.this.handleOnWebLogin(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebLogin(long j) {
        if (j == 0) {
            PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_TIMED_CHAT, false);
            PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_OUT_OF_STORAGE_ALERT, false);
            ZmLoginHelper.showIMActivityForContext(getContext(), false);
        } else {
            dismissWaiting();
            PTApp.getInstance().setRencentJid("");
            PTApp.getInstance().logout(0);
            if (j != 407) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    AuthFailedDialog.show(zMActivity, ZmLoginHelper.getLoginErrorMessage(zMActivity, j, false));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sinkWebAccessFail(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebAccessFail", new EventAction("sinkWebAccessFail") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmSmsLoginFragment.this.handleOnWebAccessFail(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebAccessFail(long j) {
        dismissWaiting();
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            AuthFailedDialog.show(zMActivity, getString(C4558R.string.zm_alert_connect_zoomus_failed_msg));
        }
    }

    /* access modifiers changed from: private */
    public void sinkSendSmsCode(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkSendSmsCode") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmSmsLoginFragment.this.handleReturnSMSCode(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleReturnSMSCode(long j) {
        String str;
        dismissWaiting();
        if (j != 0) {
            if (this.mIsSignIng) {
                if (j == 3086) {
                    str = getString(C4558R.string.zm_msg_verify_invalid_phone_num_109213);
                    this.mZMVerifyCodeView.forceEnableSendCode();
                } else if (j == 3084) {
                    str = getString(C4558R.string.zm_msg_error_verification_code_109213);
                } else if (j == 3085) {
                    str = getString(C4558R.string.zm_msg_expired_verification_code_109213);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(C4558R.string.zm_alert_connect_zoomus_failed_msg));
                    sb.append(getString(C4558R.string.zm_lbl_unknow_error, Long.valueOf(j)));
                    str = sb.toString();
                }
            } else if (j == 3086) {
                str = getString(C4558R.string.zm_msg_verify_invalid_phone_num_109213);
                this.mZMVerifyCodeView.forceEnableSendCode();
            } else if (j == 3088) {
                str = getString(C4558R.string.zm_msg_verify_phone_num_send_too_frequent_109213);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getString(C4558R.string.zm_msg_verify_send_sms_failed_109213));
                sb2.append(getString(C4558R.string.zm_lbl_unknow_error, Long.valueOf(j)));
                str = sb2.toString();
            }
            SimpleMessageDialog.newInstance(str).show(getFragmentManager(), SimpleMessageDialog.class.getName());
        }
        this.mIsSignIng = false;
    }

    private void showWaiting() {
        WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
    }

    public void dismissWaiting() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
        }
    }

    private void onClickSignIn() {
        String phoneNumber = PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
        byte[] editTextByteArrayWithClearance = ZmLoginHelper.getEditTextByteArrayWithClearance(this.mEdtCode);
        if (!StringUtil.isEmptyOrNull(phoneNumber) && editTextByteArrayWithClearance != null && editTextByteArrayWithClearance.length != 0) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                UIUtil.closeSoftKeyboard(activity, getView());
            }
            if (ZmPtUtils.checkNetWork(this)) {
                int loginWithPhoneSms = PTApp.getInstance().loginWithPhoneSms(CountryCodeUtil.CHINA_COUNTRY_CODE, phoneNumber, editTextByteArrayWithClearance, true);
                Arrays.fill(editTextByteArrayWithClearance, 0);
                if (loginWithPhoneSms == 0) {
                    this.mIsSignIng = true;
                    showWaiting();
                }
            }
        }
    }

    private void onClickBack() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            UIUtil.closeSoftKeyboard(zMActivity, getView());
            zMActivity.finish();
        }
    }

    private void setUpView() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ZmPtUtils.initPrivacyAndTerms(zMActivity, this.mTxtPrivacy);
        }
        this.mEdtNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZmSmsLoginFragment.this.onPhoneNumberChanged();
            }
        });
        this.mEdtCode.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZmSmsLoginFragment.this.onCodeChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onPhoneNumberChanged() {
        if (this.mEdtCode != null) {
            EditText editText = this.mEdtNumber;
            if (!(editText == null || this.mZMVerifyCodeView == null || this.mBtnSignIn == null)) {
                String phoneNumber = PhoneNumberUtil.getPhoneNumber(editText.getText().toString());
                String obj = this.mEdtCode.getText().toString();
                boolean isValidForRegex = ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, phoneNumber);
                boolean z = true;
                boolean z2 = obj.length() == 6;
                this.mZMVerifyCodeView.enableSendCode(isValidForRegex);
                Button button = this.mBtnSignIn;
                if (!isValidForRegex || !z2) {
                    z = false;
                }
                button.setEnabled(z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onCodeChanged() {
        if (this.mEdtCode != null) {
            EditText editText = this.mEdtNumber;
            if (!(editText == null || this.mZMVerifyCodeView == null || this.mBtnSignIn == null)) {
                this.mBtnSignIn.setEnabled(ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, PhoneNumberUtil.getPhoneNumber(editText.getText().toString())) && this.mEdtCode.getText().toString().length() == 6);
            }
        }
    }
}
