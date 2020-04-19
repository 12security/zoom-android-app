package com.zipow.videobox.login.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.FingerprintOption;
import com.zipow.videobox.ForgetPasswordActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.dialog.FingerprintAuthenticationDialog;
import com.zipow.videobox.fragment.MMSSOLoginFragment;
import com.zipow.videobox.login.AuthFailedDialog;
import com.zipow.videobox.login.ZMLoginForRealNameDialog;
import com.zipow.videobox.login.ZmSmsLoginActivity;
import com.zipow.videobox.login.model.IMultiLoginListener;
import com.zipow.videobox.login.model.ZmChinaMultiLogin;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.login.model.ZmInternationalMultiLogin;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify;
import com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify.CloudSwitchNotifyListener;
import com.zipow.videobox.ptapp.AutoLogoffChecker.AutoLogoffInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.ZoomAccount;
import com.zipow.videobox.ptapp.ZoomProductHelper;
import com.zipow.videobox.util.IAccountNameValidator;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.util.ZoomAccountNameValidator;
import java.util.Arrays;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.EncryptUtils;
import p021us.zoom.androidlib.util.FingerprintUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZmRegexUtils;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class LoginView extends LinearLayout implements OnClickListener, OnEditorActionListener, IMultiLoginListener, CloudSwitchNotifyListener {
    private static final String TAG = "LoginView";
    private IAccountNameValidator mAccountNameValidator = null;
    private IAccountNameValidator mAccountNameValidatorForZoom = new IAccountNameValidator() {
        @Nullable
        public String validate(String str) {
            if (StringUtil.isValidEmailAddress(str) || (ZmLoginHelper.isChinaUserForInternationalSignIn(LoginView.this.mSelectedProductVendor) && ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, str))) {
                return str;
            }
            return null;
        }
    };
    private AutoLogoffInfo mAutologoffInfo;
    private Button mBtnBack;
    private View mBtnLoginZoom;
    private Button mBtnSignup;
    private AbstractLoginPanel mChinaLoginPanel;
    private EditText mEdtUser;
    /* access modifiers changed from: private */
    public EditText mEdtVar2;
    private ZMAlertDialog mForceLogoutDialog;
    private AbstractLoginPanel mInternationalLoginPanel;
    /* access modifiers changed from: private */
    public boolean mIsCachedAccount = false;
    private long mLastLoginStamp;
    private View mLinkForgetPassword;
    @Nullable
    private AbstractLoginPanel mLoginPanel;
    @Nullable
    private RetainedFragment mRetainedFragment;
    private int mSelectedLoginType = -1;
    /* access modifiers changed from: private */
    public int mSelectedProductVendor = 0;
    private ZmSsoCloudSwitchPanel mSsoCloudSwitchPanel;
    @NonNull
    private ZmChinaMultiLogin mZmChinaMultiLogin = new ZmChinaMultiLogin();
    @NonNull
    private ZmInternationalMultiLogin mZmInternationalMultiLogin = new ZmInternationalMultiLogin();

    public static class RetainedFragment extends ZMFragment {
        public boolean loginFailed = false;
        public int signingType = 102;

        public RetainedFragment() {
            setRetainInstance(true);
        }
    }

    public LoginView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public LoginView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ZmComboMultiLogin.getInstance().onResume();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ZmComboMultiLogin.getInstance().onDestroy();
        ZMAlertDialog zMAlertDialog = this.mForceLogoutDialog;
        if (zMAlertDialog != null) {
            zMAlertDialog.dismiss();
            this.mForceLogoutDialog = null;
        }
    }

    public void afterCallLoginAPISuccess(int i, boolean z) {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            retainedFragment.signingType = i;
            if (z) {
                retainedFragment.loginFailed = false;
                showConnecting(true);
            }
        }
    }

    public void beforeShowLoginUI(int i, boolean z) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null && z) {
            UIUtil.closeSoftKeyboard(zMActivity, this.mEdtUser);
        }
    }

    public void onAuthFailed(@Nullable String str) {
        showConnecting(false);
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            AuthFailedDialog.show(zMActivity, str);
        }
    }

    public boolean isUiAuthorizing() {
        return isConnecting();
    }

    public void showAuthorizing(boolean z) {
        showConnecting(z);
    }

    public void checkBeforeShowLogin(@NonNull ZMActivity zMActivity) {
        boolean z;
        AbstractLoginPanel abstractLoginPanel = this.mLoginPanel;
        if (abstractLoginPanel == null || !abstractLoginPanel.isEnableLoginType(101) || !ZMPolicyUIHelper.shouldAutoShowSsoLogin()) {
            z = false;
        } else {
            z = true;
            this.mZmInternationalMultiLogin.onClickBtnLoginSSO();
        }
        if (!z && ZmPtUtils.isSupportFingerprintAndEnableFingerprintWithUserInfo(zMActivity) && OsUtil.isAtLeastM()) {
            FingerprintAuthenticationDialog.show(zMActivity);
        }
    }

    private void initView() {
        if (!isInEditMode()) {
            initRetainedFragment();
        }
        ZmComboMultiLogin.getInstance().onCreate(this.mZmInternationalMultiLogin, this.mZmChinaMultiLogin, this);
        View.inflate(getContext(), C4558R.layout.zm_loginwith, this);
        this.mLinkForgetPassword = findViewById(C4558R.C4560id.linkForgetPassword);
        this.mBtnBack = (Button) findViewById(C4558R.C4560id.btnBack);
        this.mBtnLoginZoom = findViewById(C4558R.C4560id.btnLoginZoom);
        this.mBtnSignup = (Button) findViewById(C4558R.C4560id.btnSignup);
        this.mEdtUser = (EditText) findViewById(C4558R.C4560id.edtUserName);
        this.mEdtVar2 = (EditText) findViewById(C4558R.C4560id.edtPassword);
        if (OsUtil.isAtLeastN() && new FingerprintUtil((ZMActivity) getContext()).isSupportFingerprint()) {
            FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
            if (readFromPreference != null && readFromPreference.isEnableFingerprintWithUserInfo()) {
                this.mEdtUser.setText(readFromPreference.getmUser());
            }
        }
        this.mEdtVar2.setImeOptions(2);
        this.mEdtVar2.setOnEditorActionListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnLoginZoom.setOnClickListener(this);
        this.mBtnSignup.setOnClickListener(this);
        ZmSsoCloudSwitchNotify.getInstance().addCloudSwitchNotifyListener(this);
    }

    public void restoreInstanceState(Bundle bundle) {
        if (bundle == null) {
            this.mIsCachedAccount = true;
            loadCachedAccount();
            this.mZmInternationalMultiLogin.initLoginType(this.mSelectedLoginType);
            this.mZmChinaMultiLogin.initLoginType(this.mSelectedLoginType);
        } else {
            this.mZmInternationalMultiLogin.restoreInstanceState(bundle);
            this.mZmChinaMultiLogin.restoreInstanceState(bundle);
            this.mIsCachedAccount = bundle.getBoolean("mIsCachedAccount");
            this.mAutologoffInfo = (AutoLogoffInfo) bundle.getSerializable("mIsAutoLogff");
            this.mLastLoginStamp = bundle.getLong("mLastLoginStamp", 0);
        }
        initVendorOptions();
        C31662 r4 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                LoginView.this.updateLoginZoomButton();
                if (LoginView.this.mIsCachedAccount) {
                    LoginView.this.mEdtVar2.setText("");
                }
                LoginView.this.mIsCachedAccount = false;
            }
        };
        C31673 r0 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                LoginView.this.updateLoginZoomButton();
                LoginView.this.mIsCachedAccount = false;
            }
        };
        this.mEdtUser.addTextChangedListener(r4);
        this.mEdtVar2.addTextChangedListener(r0);
        updateLoginZoomButton();
        updateAutoLogoffStatus();
        AutoLogoffInfo autoLogoffInfo = this.mAutologoffInfo;
        if (autoLogoffInfo != null && autoLogoffInfo.snsType == 101 && !StringUtil.isEmptyOrNull(this.mAutologoffInfo.ssoVanityURL)) {
            autoLoginSSO();
        }
    }

    private void autoLoginSSO() {
        ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
        if (zmInternationalMultiLogin != null) {
            zmInternationalMultiLogin.loginSSOSite(this.mAutologoffInfo.ssoVanityURL);
        }
    }

    private void initCloudSwitch() {
        if (ZmLoginHelper.isCloudSwitchShow(this.mSelectedProductVendor)) {
            ZmSsoCloudSwitchPanel zmSsoCloudSwitchPanel = this.mSsoCloudSwitchPanel;
            if (zmSsoCloudSwitchPanel == null) {
                this.mSsoCloudSwitchPanel = (ZmSsoCloudSwitchPanel) ((ViewStub) findViewById(C4558R.C4560id.viewCloudSwitch)).inflate().findViewById(C4558R.C4560id.zmSSOCloudSwitch);
            } else {
                zmSsoCloudSwitchPanel.setVisibility(0);
            }
            this.mSsoCloudSwitchPanel.refreshCloudSwitchState();
        }
    }

    private void updateAutoLogoffStatus() {
        if (this.mAutologoffInfo != null) {
            Resources resources = getResources();
            if (resources != null) {
                String str = "";
                switch (this.mAutologoffInfo.type) {
                    case 1:
                        str = resources.getString(C4558R.string.zm_lbl_warn_autologoff, new Object[]{Long.valueOf(this.mAutologoffInfo.minutes)});
                        break;
                    case 2:
                        str = resources.getString(C4558R.string.zm_lbl_warn_autologoff_sso);
                        break;
                    case 3:
                        LoginUtil.ShowRestrictedLoginErrorDlg(this.mAutologoffInfo.errorCode, false);
                        return;
                }
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZMAlertDialog zMAlertDialog = this.mForceLogoutDialog;
                    if (zMAlertDialog != null) {
                        zMAlertDialog.dismiss();
                        this.mForceLogoutDialog = null;
                    }
                    ZMActivity zMActivity = (ZMActivity) getContext();
                    if (zMActivity != null) {
                        this.mForceLogoutDialog = new Builder(zMActivity).setMessage(str).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setCancelable(false).create();
                        this.mForceLogoutDialog.show();
                    }
                }
                this.mEdtUser.setText(this.mAutologoffInfo.userName);
                if (!TextUtils.isEmpty(this.mAutologoffInfo.userName)) {
                    this.mEdtVar2.requestFocus();
                }
            }
        }
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putBoolean("mIsCachedAccount", this.mIsCachedAccount);
        bundle.putSerializable("mIsAutoLogff", this.mAutologoffInfo);
        bundle.putLong("mLastLoginStamp", this.mLastLoginStamp);
        this.mZmInternationalMultiLogin.saveInstanceState(bundle);
        this.mZmChinaMultiLogin.saveInstanceState(bundle);
    }

    public void setPreFillName(@NonNull String str) {
        EditText editText = this.mEdtUser;
        if (editText != null) {
            editText.setText(str);
            this.mEdtUser.clearFocus();
            this.mEdtVar2.requestFocus();
        }
    }

    public void setSelectedProductVendor(int i) {
        this.mSelectedProductVendor = i;
    }

    public void setSelectedLoginType(int i) {
        this.mSelectedLoginType = i;
    }

    public void setAutologoffInfo(AutoLogoffInfo autoLogoffInfo) {
        this.mAutologoffInfo = autoLogoffInfo;
        updateAutoLogoffStatus();
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        if (this.mRetainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
        }
    }

    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }

    public void requestFocusPasswd() {
        this.mEdtVar2.requestFocus();
    }

    public void loginZoomWithFingerprint() {
        FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
        if (readFromPreference != null && readFromPreference.isEnableFingerprintWithUserInfo()) {
            EncryptUtils instance = EncryptUtils.getInstance();
            Context context = getContext();
            if (context != null) {
                Context applicationContext = context.getApplicationContext();
                if (applicationContext != null) {
                    onClickBtnLoginZoom(instance.decryptStringInByte(applicationContext, readFromPreference.getmVar2(), applicationContext.getPackageName()));
                }
            }
        }
    }

    public void loginZoom(String str, byte[] bArr, boolean z, boolean z2, boolean z3) {
        if (System.currentTimeMillis() - this.mLastLoginStamp >= 500) {
            this.mLastLoginStamp = System.currentTimeMillis();
            if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
                AuthFailedDialog.show((ZMActivity) getContext(), getResources().getString(C4558R.string.zm_alert_network_disconnected));
                return;
            }
            int i = ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, str) ? 11 : 100;
            this.mRetainedFragment.signingType = i;
            PTApp instance = PTApp.getInstance();
            if (z2 && instance.getSavedZoomAccount() != null) {
                int loginZoomWithLocalTokenForType = instance.loginZoomWithLocalTokenForType(i);
                if (loginZoomWithLocalTokenForType != 0) {
                    showConnecting(false);
                    LoginUtil.ShowRestrictedLoginErrorDlg(loginZoomWithLocalTokenForType, false);
                    return;
                }
                showConnecting(true);
            } else if (LoginUtil.ShowRestrictedLoginErrorDlg(ZmLoginHelper.loginZoom(str, bArr, z), false)) {
                showConnecting(false);
                return;
            } else {
                showConnecting(true);
            }
            Arrays.fill(bArr, 0);
            RetainedFragment retainedFragment = this.mRetainedFragment;
            retainedFragment.signingType = i;
            retainedFragment.loginFailed = false;
        }
    }

    public void showConnecting(boolean z) {
        if (isConnecting() != z) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null && zMActivity.isActive()) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    if (z) {
                        WaitingDialog.newInstance(C4558R.string.zm_msg_connecting, !UIMgr.isLargeMode(zMActivity)).show(supportFragmentManager, "ConnectingDialog");
                    } else {
                        WaitingDialog waitingDialog = (WaitingDialog) supportFragmentManager.findFragmentByTag("ConnectingDialog");
                        if (waitingDialog != null) {
                            waitingDialog.dismiss();
                        }
                    }
                }
            }
        }
    }

    public boolean isConnecting() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        boolean z = false;
        if (zMActivity == null) {
            return false;
        }
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return false;
        }
        if (((WaitingDialog) supportFragmentManager.findFragmentByTag("ConnectingDialog")) != null) {
            z = true;
        }
        return z;
    }

    public void onIMLogin(long j) {
        if (j == 3 && !PTApp.getInstance().isAuthenticating()) {
            showConnecting(false);
            if (this.mRetainedFragment.signingType == 2) {
                AuthFailedDialog.show((ZMActivity) getContext(), getResources().getString(C4558R.string.zm_alert_web_auth_failed_33814));
            }
        }
        if (ZmLoginHelper.isTypeSupportIM(this.mRetainedFragment.signingType) && j != 0) {
            ZmComboMultiLogin.getInstance().onIMLogin(j, this.mRetainedFragment.signingType);
        }
    }

    private void saveZoomUserInfo() {
        if (OsUtil.isAtLeastN() && new FingerprintUtil((ZMActivity) getContext()).isHardwareDetected()) {
            String obj = this.mEdtUser.getText().toString();
            int length = this.mEdtVar2.length();
            if (!StringUtil.isEmptyOrNull(obj) && length > 0) {
                FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
                if (readFromPreference == null) {
                    readFromPreference = new FingerprintOption();
                }
                Context context = getContext();
                if (context != null) {
                    Context applicationContext = context.getApplicationContext();
                    if (applicationContext != null) {
                        String encryptString = EncryptUtils.getInstance().encryptString(applicationContext, ZmLoginHelper.getEditTextByteArrayWithClearance(this.mEdtVar2), applicationContext.getPackageName());
                        if (!StringUtil.isEmptyOrNull(obj) && !StringUtil.isEmptyOrNull(encryptString)) {
                            readFromPreference.setmUser(obj);
                            readFromPreference.setmVar2(encryptString);
                            readFromPreference.savePreference();
                        }
                    }
                }
            }
        }
    }

    public void onWebLogin(long j) {
        boolean z = true;
        if (j == 0) {
            if (this.mRetainedFragment.signingType != 100) {
                z = false;
            }
            if (z) {
                saveZoomUserInfo();
            }
            PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_TIMED_CHAT, false);
            PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_OUT_OF_STORAGE_ALERT, false);
            ZmLoginHelper.showIMActivityForContext(getContext(), z);
        } else if (!ZmComboMultiLogin.getInstance().onWebLogin(j)) {
            int i = (int) j;
            boolean ShowRestrictedLoginErrorDlg = LoginUtil.ShowRestrictedLoginErrorDlg(i, false);
            if (isConnecting()) {
                int pTLoginType = PTApp.getInstance().getPTLoginType();
                PTApp.getInstance().setRencentJid("");
                if (!ZmLoginHelper.isNeedBindPhone(PTApp.getInstance().getPTLoginType()) || !ZMLoginForRealNameDialog.isBinding((ZMActivity) getContext())) {
                    PTApp.getInstance().logout(0);
                }
                showConnecting(false);
                Fragment findFragmentByTag = ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(MMSSOLoginFragment.class.getName());
                if (findFragmentByTag != null) {
                    ((MMSSOLoginFragment) findFragmentByTag).onSSOError(i);
                } else if (j != 407) {
                    String loginErrorMessage = ZmLoginHelper.getLoginErrorMessage(getContext(), j, pTLoginType == 11);
                    if (!this.mRetainedFragment.loginFailed) {
                        this.mRetainedFragment.loginFailed = true;
                        if (!ZmLoginHelper.isNeedBindPhone(PTApp.getInstance().getPTLoginType()) || !ZMLoginForRealNameDialog.isBinding((ZMActivity) getContext())) {
                            PTApp.getInstance().logout(0);
                        }
                        if (!ShowRestrictedLoginErrorDlg) {
                            AuthFailedDialog.show((ZMActivity) getContext(), loginErrorMessage);
                        }
                    }
                    this.mRetainedFragment.signingType = 102;
                }
            }
        }
    }

    public void onWebAccessFail() {
        showConnecting(false);
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            int loginWebErrorMessage = ZmLoginHelper.getLoginWebErrorMessage(retainedFragment.signingType);
            if (!this.mRetainedFragment.loginFailed && loginWebErrorMessage != 0) {
                this.mRetainedFragment.loginFailed = true;
                AuthFailedDialog.show((ZMActivity) getContext(), getResources().getString(loginWebErrorMessage));
            }
        }
    }

    public void onIMLocalStatusChanged(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                showConnecting(true);
                return;
            default:
                return;
        }
    }

    private void loadCachedAccount() {
        ZoomAccount savedZoomAccount = PTApp.getInstance().getSavedZoomAccount();
        if (savedZoomAccount != null) {
            this.mEdtUser.setText(savedZoomAccount.getUserName());
            EditText editText = this.mEdtUser;
            editText.setSelection(editText.getText().length(), this.mEdtUser.getText().length());
            EditText editText2 = this.mEdtVar2;
            editText2.setSelection(editText2.getText().length(), this.mEdtVar2.getText().length());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0123  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initVendorOptions() {
        /*
            r5 = this;
            int r0 = r5.mSelectedProductVendor
            r1 = 1
            if (r0 < 0) goto L_0x000b
            if (r0 == r1) goto L_0x001b
            int r0 = p021us.zoom.videomeetings.ZMBuildConfig.BUILD_TARGET
            if (r0 != 0) goto L_0x001b
        L_0x000b:
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.ZoomProductHelper r0 = r0.getZoomProductHelper()
            if (r0 == 0) goto L_0x001b
            int r0 = r0.getCurrentVendor()
            r5.mSelectedProductVendor = r0
        L_0x001b:
            int r0 = r5.mSelectedProductVendor
            r2 = 8
            if (r0 == r1) goto L_0x0029
            int r0 = p021us.zoom.videomeetings.ZMBuildConfig.BUILD_TARGET
            if (r0 != 0) goto L_0x0029
            r5.initCloudSwitch()
            goto L_0x0030
        L_0x0029:
            com.zipow.videobox.login.view.ZmSsoCloudSwitchPanel r0 = r5.mSsoCloudSwitchPanel
            if (r0 == 0) goto L_0x0030
            r0.setVisibility(r2)
        L_0x0030:
            int r0 = r5.mSelectedProductVendor
            boolean r0 = com.zipow.videobox.login.model.ZmLoginHelper.isChinaUserForInternationalSignIn(r0)
            r3 = 0
            if (r0 == 0) goto L_0x00d1
            android.widget.EditText r0 = r5.mEdtUser
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_hint_email_phone_137212
            r0.setHint(r1)
            android.view.View r0 = r5.mLinkForgetPassword
            r0.setVisibility(r2)
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.rlCnSignForgotPasswdPanel
            android.view.View r0 = r5.findViewById(r0)
            r0.setVisibility(r3)
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.linkSmsSign
            android.view.View r0 = r5.findViewById(r0)
            r0.setOnClickListener(r5)
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.linkCnForgetPassword
            android.view.View r0 = r5.findViewById(r0)
            r0.setOnClickListener(r5)
            boolean r0 = com.zipow.videobox.login.model.ZmLoginHelper.isEnableChinaThirdpartyLogin()
            if (r0 == 0) goto L_0x0091
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mChinaLoginPanel
            if (r0 != 0) goto L_0x0080
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.viewStubChina
            android.view.View r0 = r5.findViewById(r0)
            android.view.ViewStub r0 = (android.view.ViewStub) r0
            android.view.View r0 = r0.inflate()
            int r1 = p021us.zoom.videomeetings.C4558R.C4560id.zmChinaLoginPanel
            android.view.View r0 = r0.findViewById(r1)
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = (com.zipow.videobox.login.view.AbstractLoginPanel) r0
            r5.mChinaLoginPanel = r0
        L_0x0080:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            if (r0 == 0) goto L_0x0087
            r0.setVisibility(r2)
        L_0x0087:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mChinaLoginPanel
            r0.setVisibility(r3)
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mChinaLoginPanel
            r5.mLoginPanel = r0
            goto L_0x00bb
        L_0x0091:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            if (r0 != 0) goto L_0x00ab
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.viewStubInternational
            android.view.View r0 = r5.findViewById(r0)
            android.view.ViewStub r0 = (android.view.ViewStub) r0
            android.view.View r0 = r0.inflate()
            int r1 = p021us.zoom.videomeetings.C4558R.C4560id.zmInternationalLoginPanel
            android.view.View r0 = r0.findViewById(r1)
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = (com.zipow.videobox.login.view.AbstractLoginPanel) r0
            r5.mInternationalLoginPanel = r0
        L_0x00ab:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mChinaLoginPanel
            if (r0 == 0) goto L_0x00b2
            r0.setVisibility(r2)
        L_0x00b2:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            r0.setVisibility(r3)
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            r5.mLoginPanel = r0
        L_0x00bb:
            int r0 = p021us.zoom.videomeetings.C4558R.bool.zm_config_show_signup_on_login_screen
            boolean r0 = p021us.zoom.androidlib.util.ResourcesUtil.getBoolean(r5, r0, r3)
            if (r0 == 0) goto L_0x00ca
            android.widget.Button r0 = r5.mBtnSignup
            r0.setVisibility(r3)
            goto L_0x014e
        L_0x00ca:
            android.widget.Button r0 = r5.mBtnSignup
            r0.setVisibility(r2)
            goto L_0x014e
        L_0x00d1:
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.rlCnSignForgotPasswdPanel
            android.view.View r0 = r5.findViewById(r0)
            r0.setVisibility(r2)
            android.widget.EditText r0 = r5.mEdtUser
            int r4 = p021us.zoom.videomeetings.C4558R.string.zm_hint_zoom_account
            r0.setHint(r4)
            int r0 = p021us.zoom.videomeetings.ZMBuildConfig.BUILD_TARGET
            if (r0 != 0) goto L_0x0120
            int r0 = r5.mSelectedProductVendor
            if (r0 != r1) goto L_0x00f4
            android.view.View r0 = r5.mLinkForgetPassword
            r0.setVisibility(r2)
            android.widget.Button r0 = r5.mBtnSignup
            r0.setVisibility(r2)
            goto L_0x0120
        L_0x00f4:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            if (r0 != 0) goto L_0x010e
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.viewStubInternational
            android.view.View r0 = r5.findViewById(r0)
            android.view.ViewStub r0 = (android.view.ViewStub) r0
            android.view.View r0 = r0.inflate()
            int r4 = p021us.zoom.videomeetings.C4558R.C4560id.zmInternationalLoginPanel
            android.view.View r0 = r0.findViewById(r4)
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = (com.zipow.videobox.login.view.AbstractLoginPanel) r0
            r5.mInternationalLoginPanel = r0
        L_0x010e:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mChinaLoginPanel
            if (r0 == 0) goto L_0x0115
            r0.setVisibility(r2)
        L_0x0115:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            r0.setVisibility(r3)
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mInternationalLoginPanel
            r5.mLoginPanel = r0
            r0 = 1
            goto L_0x0121
        L_0x0120:
            r0 = 0
        L_0x0121:
            if (r0 == 0) goto L_0x014e
            int r0 = p021us.zoom.videomeetings.C4558R.bool.zm_config_show_forgot_password
            boolean r0 = p021us.zoom.androidlib.util.ResourcesUtil.getBoolean(r5, r0, r1)
            if (r0 == 0) goto L_0x0136
            android.view.View r0 = r5.mLinkForgetPassword
            r0.setVisibility(r3)
            android.view.View r0 = r5.mLinkForgetPassword
            r0.setOnClickListener(r5)
            goto L_0x013b
        L_0x0136:
            android.view.View r0 = r5.mLinkForgetPassword
            r0.setVisibility(r2)
        L_0x013b:
            int r0 = p021us.zoom.videomeetings.C4558R.bool.zm_config_show_signup_on_login_screen
            boolean r0 = p021us.zoom.androidlib.util.ResourcesUtil.getBoolean(r5, r0, r3)
            if (r0 == 0) goto L_0x0149
            android.widget.Button r0 = r5.mBtnSignup
            r0.setVisibility(r3)
            goto L_0x014e
        L_0x0149:
            android.widget.Button r0 = r5.mBtnSignup
            r0.setVisibility(r2)
        L_0x014e:
            com.zipow.videobox.login.view.AbstractLoginPanel r0 = r5.mLoginPanel
            if (r0 == 0) goto L_0x0157
            int r1 = r5.mSelectedProductVendor
            r0.initVendorOptions(r1)
        L_0x0157:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.login.view.LoginView.initVendorOptions():void");
    }

    public void onClick(View view) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null && zMActivity.isActive()) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnBack) {
                onClickBtnBack();
            } else if (id == C4558R.C4560id.btnLoginZoom) {
                onClickBtnLoginZoom(ZmLoginHelper.getEditTextByteArrayWithClearance(this.mEdtVar2));
            } else if (id == C4558R.C4560id.btnSignup) {
                onClickBtnSignup();
            } else if (id == C4558R.C4560id.linkForgetPassword) {
                onClickBtnForgetPassword();
            } else if (id == C4558R.C4560id.linkSmsSign) {
                onClickBtnSmsSign();
            } else if (id == C4558R.C4560id.linkCnForgetPassword) {
                onClickBtnForgetPassword();
            }
        }
    }

    private void onClickBtnBack() {
        LoginActivity loginActivity = (LoginActivity) getContext();
        if (loginActivity != null) {
            if (loginActivity.isShownForTokenExpired()) {
                PTApp.getInstance().logout(0);
            }
            loginActivity.onBackPressed();
        }
    }

    private void onClickBtnLoginZoom(byte[] bArr) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            UIUtil.closeSoftKeyboard(zMActivity, this.mEdtUser);
            String validate = getAccountNameValidator().validate(this.mEdtUser.getText().toString());
            if (StringUtil.isEmptyOrNull(validate)) {
                this.mEdtUser.requestFocus();
            } else if (bArr == null || bArr.length == 0) {
                this.mEdtVar2.requestFocus();
            } else {
                ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
                if (zoomProductHelper != null) {
                    int currentVendor = zoomProductHelper.getCurrentVendor();
                    int i = this.mSelectedProductVendor;
                    if (currentVendor != i) {
                        zoomProductHelper.vendorSwitchTo(i);
                    }
                }
                loginZoom(validate, bArr, true, this.mIsCachedAccount, true);
            }
        }
    }

    private void onClickBtnSignup() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            if (ResourcesUtil.getBoolean((View) this, C4558R.bool.zm_config_show_signup_as_web_url, false)) {
                String uRLByType = PTApp.getInstance().getURLByType(6);
                if (!StringUtil.isEmptyOrNull(uRLByType)) {
                    UIUtil.openURL(zMActivity, uRLByType);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(getZoomScheme());
                sb.append("://client/signup");
                UIUtil.openURL(zMActivity, sb.toString());
            }
        }
    }

    private void onClickBtnForgetPassword() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity == null) {
            return;
        }
        if (ResourcesUtil.getBoolean((View) this, C4558R.bool.zm_config_show_forgot_password_as_web_url, false)) {
            String uRLByType = PTApp.getInstance().getURLByType(7);
            if (uRLByType != null) {
                UIUtil.openURL(zMActivity, uRLByType);
                return;
            }
            return;
        }
        ForgetPasswordActivity.show(zMActivity);
    }

    private void onClickBtnSmsSign() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            ZmSmsLoginActivity.show(zMActivity);
        }
    }

    private String getZoomScheme() {
        Context context = getContext();
        if (context == null) {
            return "";
        }
        return context.getString(C4558R.string.zm_zoom_scheme);
    }

    /* access modifiers changed from: private */
    public void updateLoginZoomButton() {
        this.mBtnLoginZoom.setEnabled(validateZoomAccount());
    }

    private boolean validateZoomAccount() {
        return !StringUtil.isEmptyOrNull(getAccountNameValidator().validate(this.mEdtUser.getText().toString())) && this.mEdtVar2.length() != 0;
    }

    private IAccountNameValidator getAccountNameValidator() {
        if (ZMBuildConfig.BUILD_TARGET == 0) {
            this.mAccountNameValidator = this.mAccountNameValidatorForZoom;
            return this.mAccountNameValidator;
        }
        IAccountNameValidator iAccountNameValidator = this.mAccountNameValidator;
        if (iAccountNameValidator != null) {
            return iAccountNameValidator;
        }
        try {
            this.mAccountNameValidator = (IAccountNameValidator) Class.forName(ResourcesUtil.getString((View) this, C4558R.string.zm_config_account_name_validator)).newInstance();
        } catch (Exception unused) {
        }
        if (this.mAccountNameValidator == null) {
            this.mAccountNameValidator = new ZoomAccountNameValidator();
        }
        return this.mAccountNameValidator;
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickBtnLoginZoom(ZmLoginHelper.getEditTextByteArrayWithClearance(this.mEdtVar2));
        return true;
    }

    public void updateCloudSwitch(int i) {
        ZmSsoCloudSwitchPanel zmSsoCloudSwitchPanel = this.mSsoCloudSwitchPanel;
        if (zmSsoCloudSwitchPanel != null) {
            zmSsoCloudSwitchPanel.refreshCloudSwitchState(i);
        }
        this.mSelectedProductVendor = i;
        initVendorOptions();
    }
}
