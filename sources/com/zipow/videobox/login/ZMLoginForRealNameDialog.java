package com.zipow.videobox.login;

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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.login.model.ZmChinaMultiLogin;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ZMVerifyCodeView;
import com.zipow.videobox.view.ZMVerifyCodeView.VerifyCodeCallBack;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZmRegexUtils;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class ZMLoginForRealNameDialog extends ZMDialogFragment implements OnClickListener, VerifyCodeCallBack {
    private static final String ARG_OPEN_ID = "ARG_OPEN_ID";
    private static final String ARG_TOKEN = "ARG_TOKEN";
    private static final String TAG = "com.zipow.videobox.login.ZMLoginForRealNameDialog";
    private Button mBtnBind;
    private EditText mEdtCode;
    private EditText mEdtNumber;
    private boolean mIsBinding = false;
    @Nullable
    private String mOpenId;
    private SimplePTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, long j) {
            if (i == 77) {
                ZMLoginForRealNameDialog.this.sinkSendSmsCode(j);
            }
        }
    };
    @Nullable
    private String mToken;
    private ZMVerifyCodeView mZMVerifyCodeView;

    @Nullable
    public static ZMLoginForRealNameDialog show(@NonNull ZMActivity zMActivity, @Nullable String str, @Nullable String str2) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return null;
        }
        dismiss(supportFragmentManager);
        ZMLoginForRealNameDialog zMLoginForRealNameDialog = new ZMLoginForRealNameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_OPEN_ID, str);
        bundle.putString(ARG_TOKEN, str2);
        zMLoginForRealNameDialog.setArguments(bundle);
        zMLoginForRealNameDialog.show(supportFragmentManager, TAG);
        return zMLoginForRealNameDialog;
    }

    public static boolean isBinding(@Nullable ZMActivity zMActivity) {
        if (zMActivity == null) {
            return false;
        }
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return false;
        }
        ZMLoginForRealNameDialog zMLoginForRealNameDialog = (ZMLoginForRealNameDialog) supportFragmentManager.findFragmentByTag(TAG);
        if (zMLoginForRealNameDialog != null) {
            return zMLoginForRealNameDialog.ismIsBinding();
        }
        return false;
    }

    public static void dismiss(@NonNull ZMActivity zMActivity) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            ZMLoginForRealNameDialog zMLoginForRealNameDialog = (ZMLoginForRealNameDialog) supportFragmentManager.findFragmentByTag(TAG);
            if (zMLoginForRealNameDialog != null) {
                zMLoginForRealNameDialog.dismiss();
            }
        }
    }

    private static void dismiss(FragmentManager fragmentManager) {
        ZMLoginForRealNameDialog zMLoginForRealNameDialog = (ZMLoginForRealNameDialog) fragmentManager.findFragmentByTag(TAG);
        if (zMLoginForRealNameDialog != null) {
            zMLoginForRealNameDialog.dismiss();
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, C4558R.style.ZMDialog_NoTitle);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setCancelable(false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mOpenId = arguments.getString(ARG_OPEN_ID);
            this.mToken = arguments.getString(ARG_TOKEN);
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_dialog_login_realname, null, false);
        this.mZMVerifyCodeView = (ZMVerifyCodeView) inflate.findViewById(C4558R.C4560id.zmVerifyCodeView);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mEdtCode = (EditText) inflate.findViewById(C4558R.C4560id.edtCode);
        this.mBtnBind = (Button) inflate.findViewById(C4558R.C4560id.btnBind);
        this.mBtnBind.setOnClickListener(this);
        View findViewById = inflate.findViewById(C4558R.C4560id.btnSkip);
        ZmChinaMultiLogin zmChinaMultiLogin = ZmComboMultiLogin.getInstance().getmZmChinaMultiLogin();
        if (zmChinaMultiLogin == null || zmChinaMultiLogin.getmZoomSnsType() != 22) {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(this);
        }
        setUpView();
        this.mZMVerifyCodeView.setmVerifyCodeCallBack(this);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public void onDestroyView() {
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
        ZMVerifyCodeView zMVerifyCodeView = this.mZMVerifyCodeView;
        if (zMVerifyCodeView != null) {
            zMVerifyCodeView.setmVerifyCodeCallBack(null);
        }
        super.onDestroyView();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSkip) {
            onClickSkip();
        } else if (id == C4558R.C4560id.btnBind) {
            onClickBind();
        }
    }

    public void onClickSendCode() {
        if (ZmPtUtils.checkNetWork(this)) {
            String phoneNumber = PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
            if (!StringUtil.isEmptyOrNull(phoneNumber)) {
                if (PTApp.getInstance().sendSMSCodeForLogin(CountryCodeUtil.CHINA_COUNTRY_CODE, phoneNumber) == 0) {
                    PTUI.getInstance().addPTUIListener(this.mPTUIListener);
                    showWaiting();
                } else {
                    SimpleMessageDialog.newInstance(C4558R.string.zm_msg_verify_phone_number_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                }
            }
        }
    }

    public boolean ismIsBinding() {
        return this.mIsBinding;
    }

    /* access modifiers changed from: private */
    public void sinkSendSmsCode(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkSendSmsCode") {
            public void run(@NonNull IUIElement iUIElement) {
                ZMLoginForRealNameDialog.this.handleReturnSMSCode(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleReturnSMSCode(long j) {
        dismissWaiting();
        if (j != 0) {
            int i = C4558R.string.zm_msg_verify_send_sms_failed_109213;
            if (j == 3086) {
                i = C4558R.string.zm_msg_verify_invalid_phone_num_109213;
                this.mZMVerifyCodeView.forceEnableSendCode();
            } else if (j == 3088) {
                i = C4558R.string.zm_msg_verify_phone_num_send_too_frequent_109213;
            }
            SimpleMessageDialog.newInstance(i).show(getFragmentManager(), SimpleMessageDialog.class.getName());
        }
    }

    private void onClickSkip() {
        boolean z;
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            UIUtil.closeSoftKeyboard(zMActivity, getView());
        }
        ZmChinaMultiLogin zmChinaMultiLogin = ZmComboMultiLogin.getInstance().getmZmChinaMultiLogin();
        if (zmChinaMultiLogin != null) {
            this.mIsBinding = false;
            if (StringUtil.isEmptyOrNull(this.mToken) || StringUtil.isEmptyOrNull(this.mOpenId)) {
                z = zmChinaMultiLogin.loginLocal();
            } else {
                z = zmChinaMultiLogin.login(this.mOpenId, this.mToken);
            }
            if (!z) {
                SimpleMessageDialog.newInstance(C4558R.string.zm_alert_connect_zoomus_failed_msg).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            }
            dismiss();
        }
    }

    private void onClickBind() {
        boolean z;
        String phoneNumber = PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
        String obj = this.mEdtCode.getText().toString();
        if (!StringUtil.isEmptyOrNull(phoneNumber) && !StringUtil.isEmptyOrNull(obj)) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                UIUtil.closeSoftKeyboard(activity, getView());
            }
            if (ZmPtUtils.checkNetWork(this)) {
                ZmChinaMultiLogin zmChinaMultiLogin = ZmComboMultiLogin.getInstance().getmZmChinaMultiLogin();
                if (zmChinaMultiLogin != null) {
                    this.mIsBinding = true;
                    if (StringUtil.isEmptyOrNull(this.mToken) || StringUtil.isEmptyOrNull(this.mOpenId)) {
                        z = zmChinaMultiLogin.loginLocalWithRealName(CountryCodeUtil.CHINA_COUNTRY_CODE, phoneNumber, obj);
                    } else {
                        z = zmChinaMultiLogin.loginWithRealName(this.mOpenId, this.mToken, CountryCodeUtil.CHINA_COUNTRY_CODE, phoneNumber, obj);
                    }
                    if (!z) {
                        this.mIsBinding = false;
                        SimpleMessageDialog.newInstance(C4558R.string.zm_alert_connect_zoomus_failed_msg).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                    }
                }
            }
        }
    }

    private void showWaiting() {
        WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
    }

    private void dismissWaiting() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
        }
    }

    private void setUpView() {
        this.mEdtNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZMLoginForRealNameDialog.this.onPhoneNumberChanged();
            }
        });
        this.mEdtCode.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZMLoginForRealNameDialog.this.onCodeChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onPhoneNumberChanged() {
        if (this.mEdtCode != null) {
            EditText editText = this.mEdtNumber;
            if (!(editText == null || this.mZMVerifyCodeView == null || this.mBtnBind == null)) {
                String phoneNumber = PhoneNumberUtil.getPhoneNumber(editText.getText().toString());
                String obj = this.mEdtCode.getText().toString();
                boolean isValidForRegex = ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, phoneNumber);
                boolean z = true;
                boolean z2 = obj.length() == 6;
                this.mZMVerifyCodeView.enableSendCode(isValidForRegex);
                Button button = this.mBtnBind;
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
            if (!(editText == null || this.mZMVerifyCodeView == null || this.mBtnBind == null)) {
                this.mBtnBind.setEnabled(ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, PhoneNumberUtil.getPhoneNumber(editText.getText().toString())) && this.mEdtCode.getText().toString().length() == 6);
            }
        }
    }
}
