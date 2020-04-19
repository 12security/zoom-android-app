package com.zipow.videobox;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.login.AuthFailedDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.UIMgr;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class RCLoginActivity extends ZMActivity implements OnClickListener, OnEditorActionListener, IPTUIListener {
    private static final String TAG = "RCLoginActivity";
    private Button mBtnBack;
    private Button mBtnLoginZoom;
    private Button mBtnSignup;
    private EditText mEdtExtension;
    private EditText mEdtPhoneNumber;
    /* access modifiers changed from: private */
    public EditText mEdtPsw;
    /* access modifiers changed from: private */
    public boolean mIsCachedAccount = false;
    private TextView mLinkForgetPassword;
    private boolean mLoginFailed = false;
    private View mOptionCountry;
    private int mSelectedCountry = 0;
    private TextView mTxtCountry;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static boolean show(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        Intent intent = new Intent(context, RCLoginActivity.class);
        boolean z = context instanceof Activity;
        if (!z) {
            intent.addFlags(268435456);
        }
        ActivityStartHelper.startActivityForeground(context, intent);
        if (z) {
            ((Activity) context).overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
        return true;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (UIUtil.getDisplayMinWidthInDip(this) < 500.0f) {
            setRequestedOrientation(1);
        }
        if (PTApp.getInstance().isWebSignedOn()) {
            IMActivity.show(this);
            finish();
            return;
        }
        setContentView(C4558R.layout.zm_rc_login);
        this.mBtnBack = (Button) findViewById(C4558R.C4560id.btnBack);
        this.mBtnLoginZoom = (Button) findViewById(C4558R.C4560id.btnLoginZoom);
        this.mBtnSignup = (Button) findViewById(C4558R.C4560id.btnSignup);
        this.mLinkForgetPassword = (TextView) findViewById(C4558R.C4560id.linkForgetPassword);
        this.mEdtPhoneNumber = (EditText) findViewById(C4558R.C4560id.edtPhoneNumber);
        this.mEdtExtension = (EditText) findViewById(C4558R.C4560id.edtExtension);
        this.mEdtPsw = (EditText) findViewById(C4558R.C4560id.edtPassword);
        this.mOptionCountry = findViewById(C4558R.C4560id.optionCountry);
        this.mTxtCountry = (TextView) findViewById(C4558R.C4560id.txtCountry);
        this.mEdtPsw.setImeOptions(2);
        this.mEdtPsw.setOnEditorActionListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnLoginZoom.setOnClickListener(this);
        Button button = this.mBtnSignup;
        if (button != null) {
            button.setOnClickListener(this);
        }
        View view = this.mOptionCountry;
        if (view != null) {
            view.setOnClickListener(this);
        }
        this.mLinkForgetPassword.setMovementMethod(LinkMovementMethod.getInstance());
        if (bundle == null) {
            loadCachedAccount();
            this.mSelectedCountry = getDefaultSelectedCountry();
        } else {
            this.mIsCachedAccount = bundle.getBoolean("mIsCachedAccount");
            this.mSelectedCountry = bundle.getInt("mSelectedCountry");
        }
        C21561 r5 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                RCLoginActivity.this.updateLoginZoomButton();
                if (RCLoginActivity.this.mIsCachedAccount) {
                    RCLoginActivity.this.mEdtPsw.setText("");
                }
                RCLoginActivity.this.mIsCachedAccount = false;
            }
        };
        C21572 r0 = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                RCLoginActivity.this.updateLoginZoomButton();
                RCLoginActivity.this.mIsCachedAccount = false;
            }
        };
        this.mEdtPhoneNumber.addTextChangedListener(r5);
        this.mEdtPsw.addTextChangedListener(r0);
        PTUI.getInstance().addPTUIListener(this);
        if (ZMBuildConfig.BUILD_TARGET == 4) {
            this.mSelectedCountry = 2;
        } else if (ZMBuildConfig.BUILD_TARGET == 5) {
            this.mSelectedCountry = 1;
        }
    }

    private int getDefaultSelectedCountry() {
        int RC_getDefaultCountryTypeByName = PTApp.getInstance().RC_getDefaultCountryTypeByName(CompatUtils.getLocalDefault().getCountry().toLowerCase(Locale.US));
        if (RC_getDefaultCountryTypeByName != -1) {
            return RC_getDefaultCountryTypeByName;
        }
        return 0;
    }

    private void loadCachedAccount() {
        String[] strArr = new String[1];
        String[] strArr2 = new String[1];
        PTApp.getInstance().getSavedRingCentralPhoneNumberAndExt(strArr, strArr2);
        if (!StringUtil.isEmptyOrNull(strArr[0])) {
            this.mEdtPhoneNumber.setText(strArr[0]);
            if (strArr2[0] != null) {
                this.mEdtExtension.setText(strArr2[0]);
            }
            this.mEdtPsw.setText("$$$$$$$$$$");
            EditText editText = this.mEdtPhoneNumber;
            editText.setSelection(editText.getText().length(), this.mEdtPhoneNumber.getText().length());
            EditText editText2 = this.mEdtExtension;
            editText2.setSelection(editText2.getText().length(), this.mEdtExtension.getText().length());
            EditText editText3 = this.mEdtPsw;
            editText3.setSelection(editText3.getText().length(), this.mEdtPsw.getText().length());
            this.mIsCachedAccount = true;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onResume() {
        super.onResume();
        updateLoginZoomButton();
        updateSelectedCountry();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putBoolean("mIsCachedAccount", this.mIsCachedAccount);
        bundle.putInt("mSelectedCountry", this.mSelectedCountry);
        super.onSaveInstanceState(bundle);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnLoginZoom) {
            onClickBtnLoginZoom();
        } else if (id == C4558R.C4560id.btnSignup) {
            onClickBtnSignup();
        } else if (id == C4558R.C4560id.optionCountry) {
            onClickOptionCountry();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (!isConnecting()) {
            WelcomeActivity.show(this, true, false);
            finish();
        }
    }

    private void onClickBtnBack() {
        onBackPressed();
    }

    private void onClickBtnLoginZoom() {
        UIUtil.closeSoftKeyboard(this, this.mEdtPhoneNumber);
        String formatPhoneNumber = formatPhoneNumber(this.mEdtPhoneNumber.getText().toString().trim());
        String trim = this.mEdtExtension.getText().toString().trim();
        String obj = this.mEdtPsw.getText().toString();
        if (!isValidPhoneNumber(formatPhoneNumber)) {
            this.mEdtPhoneNumber.requestFocus();
        } else if (StringUtil.isEmptyOrNull(obj)) {
            this.mEdtPsw.requestFocus();
        } else {
            loginZoom(formatPhoneNumber, trim, obj, true, this.mIsCachedAccount, true);
        }
    }

    @NonNull
    private String formatPhoneNumber(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    private boolean isValidPhoneNumber(String str) {
        return str.length() >= 1;
    }

    private void onClickBtnSignup() {
        String uRLByType = PTApp.getInstance().getURLByType(6);
        if (!StringUtil.isEmptyOrNull(uRLByType)) {
            UIUtil.openURL(this, uRLByType);
        }
    }

    private void onClickOptionCountry() {
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(this, false);
        zMMenuAdapter.addItem(new ZMSimpleMenuItem(0, Locale.US.getDisplayCountry()));
        zMMenuAdapter.addItem(new ZMSimpleMenuItem(1, Locale.CANADA.getDisplayCountry()));
        zMMenuAdapter.addItem(new ZMSimpleMenuItem(2, Locale.UK.getDisplayCountry()));
        ZMAlertDialog create = new Builder(this).setTitle(C4558R.string.zm_title_select_country_104883).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMSimpleMenuItem zMSimpleMenuItem = (ZMSimpleMenuItem) zMMenuAdapter.getItem(i);
                if (zMSimpleMenuItem != null) {
                    RCLoginActivity.this.onSelectCountryType(zMSimpleMenuItem.getAction());
                }
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
    }

    /* access modifiers changed from: private */
    public void onSelectCountryType(int i) {
        this.mSelectedCountry = i;
        updateSelectedCountry();
    }

    /* access modifiers changed from: private */
    public void updateLoginZoomButton() {
        this.mBtnLoginZoom.setEnabled(validateZoomAccount());
    }

    private void updateSelectedCountry() {
        if (this.mTxtCountry != null) {
            PTApp.getInstance().RC_setCountryType(this.mSelectedCountry);
            switch (this.mSelectedCountry) {
                case 1:
                    this.mTxtCountry.setText(Locale.CANADA.getDisplayCountry());
                    break;
                case 2:
                    this.mTxtCountry.setText(Locale.UK.getDisplayCountry());
                    break;
                default:
                    this.mTxtCountry.setText(Locale.US.getDisplayCountry());
                    break;
            }
        }
        updateLinkForgotPassword();
    }

    private void updateLinkForgotPassword() {
        String uRLByType = PTApp.getInstance().getURLByType(7);
        if (!StringUtil.isEmptyOrNull(uRLByType)) {
            this.mLinkForgetPassword.setText(Html.fromHtml(getString(C4558R.string.zm_lbl_forget_password_link, new Object[]{uRLByType})));
        }
    }

    private boolean validateZoomAccount() {
        String formatPhoneNumber = formatPhoneNumber(this.mEdtPhoneNumber.getText().toString());
        String obj = this.mEdtPsw.getText().toString();
        if (isValidPhoneNumber(formatPhoneNumber) && obj.length() != 0) {
            return true;
        }
        return false;
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        onClickBtnLoginZoom();
        return true;
    }

    private void loginZoom(String str, String str2, String str3, boolean z, boolean z2, boolean z3) {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            AuthFailedDialog.show(this, getResources().getString(C4558R.string.zm_alert_network_disconnected));
            return;
        }
        showConnecting(true);
        PTApp instance = PTApp.getInstance();
        if (z2) {
            int loginRingCentralWithLocalToken = instance.loginRingCentralWithLocalToken();
            if (loginRingCentralWithLocalToken != 0) {
                showConnecting(false);
                LoginUtil.ShowRestrictedLoginErrorDlg(loginRingCentralWithLocalToken, false);
                return;
            }
        } else {
            instance.loginWithRingCentral(str, str2, str3, this.mSelectedCountry, z);
        }
        this.mLoginFailed = false;
    }

    private void showConnecting(boolean z) {
        if (isConnecting() != z && isActive()) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                if (z) {
                    WaitingDialog.newInstance(C4558R.string.zm_msg_connecting, !UIMgr.isLargeMode(this)).show(supportFragmentManager, "ConnectingDialog");
                } else {
                    WaitingDialog waitingDialog = (WaitingDialog) supportFragmentManager.findFragmentByTag("ConnectingDialog");
                    if (waitingDialog != null) {
                        waitingDialog.dismiss();
                    }
                }
            }
        }
    }

    public boolean isConnecting() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        boolean z = false;
        if (supportFragmentManager == null) {
            return false;
        }
        if (((WaitingDialog) supportFragmentManager.findFragmentByTag("ConnectingDialog")) != null) {
            z = true;
        }
        return z;
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            sinkWebLogin(j);
        } else if (i == 37) {
            sinkWebAccessFail();
        }
    }

    public void sinkWebLogin(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLogin", new EventAction("sinkWebLogin") {
            public void run(@NonNull IUIElement iUIElement) {
                ((RCLoginActivity) iUIElement).handleOnWebLogin(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebLogin(long j) {
        onWebLogin(j);
    }

    private void sinkWebAccessFail() {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebAccessFail", new EventAction("sinkWebAccessFail") {
            public void run(@NonNull IUIElement iUIElement) {
                ((RCLoginActivity) iUIElement).handleOnWebAccessFail();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebAccessFail() {
        onWebAccessFail();
    }

    private void onWebLogin(long j) {
        if (j == 0) {
            showIMActivity();
            return;
        }
        PTApp.getInstance().setRencentJid("");
        showConnecting(false);
        String loginErrorMessage = getLoginErrorMessage(j);
        if (!this.mLoginFailed) {
            this.mLoginFailed = true;
            AuthFailedDialog.show(this, loginErrorMessage);
        }
    }

    private String getLoginErrorMessage(long j) {
        int i = (int) j;
        if (i == 1006) {
            return getResources().getString(C4558R.string.zm_alert_auth_token_failed_msg);
        }
        if (i == 2006) {
            return getResources().getString(C4558R.string.zm_rc_alert_meetings_feature_is_not_enabled);
        }
        switch (i) {
            case 1000:
            case 1001:
            case 1002:
                return getResources().getString(C4558R.string.zm_alert_auth_zoom_failed_msg);
            default:
                return getResources().getString(C4558R.string.zm_alert_auth_error_code_msg, new Object[]{Long.valueOf(j)});
        }
    }

    public void onWebAccessFail() {
        showConnecting(false);
        int i = C4558R.string.zm_alert_connect_zoomus_failed_msg;
        if (!this.mLoginFailed && i != 0) {
            this.mLoginFailed = true;
            AuthFailedDialog.show(this, getResources().getString(i));
        }
    }

    private void showIMActivity() {
        finish();
        IMActivity.show(this);
        overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
    }
}
