package com.zipow.videobox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.dialog.DialogActionCallBack;
import com.zipow.videobox.dialog.FbConfirmCreateAccountDialog;
import com.zipow.videobox.dialog.FingerprintAuthenticationDialog.FingerprintAuthCallBack;
import com.zipow.videobox.dialog.ZMGDPRConfirmDialog;
import com.zipow.videobox.login.AuthFailedDialog;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.login.model.ZmInternationalMultiLogin;
import com.zipow.videobox.login.model.ZmSsoCloudSwitchNotify;
import com.zipow.videobox.login.view.LoginView;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.AutoLogoffChecker.AutoLogoffInfo;
import com.zipow.videobox.ptapp.IAgeGatingCallback;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IGDPRListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.RevokeTokenAutoLogoffChecker.RovokeTokenDialog;
import com.zipow.videobox.ptapp.SimpleIMListener;
import com.zipow.videobox.thirdparty.AuthResult;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.NotificationMgr.NotificationType;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMServiceHelper;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class LoginActivity extends ZMActivity implements IPTUIListener, DialogActionCallBack, IGDPRListener, FingerprintAuthCallBack, IAgeGatingCallback {
    public static final String ACTION_THIRD_LOGIN;
    private static final String ARG_AUTH_LAST_TIME = "googleAuthLastTime";
    private static final String ARG_AUTO_LOGOFF_INFO = "autoLogoffInfo";
    private static final String ARG_LOGIN_TYPE = "loginType";
    private static final String ARG_PRE_FILL_NAME = "prefill_name";
    private static final String ARG_PRODUCT_VENDOR = "productVendor";
    private static final String ARG_REVOKE_TOKEN = "ARG_REVOKE_TOKEN";
    private static final String ARG_SHOW_FOR_TOKEN_EXPIRED = "showForTokenExpired";
    private static final String ARG_THIRD_LOGIN = "ARG_THIRD_LOGIN";
    private static final int REQUEST_DIALOG_GDPR = 1000;
    public static final int REQUEST_FACEBOOK_AUTH = 100;
    private static final String TAG = "LoginActivity";
    public static long mLastAuthTime = -1;
    private SimpleIMListener mIMListener = new SimpleIMListener() {
        public void onIMLocalStatusChanged(final int i) {
            LoginActivity.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onIMLocalStatusChanged") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((LoginActivity) iUIElement).handleOnIMLocalStatusChanged(i);
                }
            });
        }
    };
    private LoginView mLoginView;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(LoginActivity.class.getName());
        sb.append(".action.ACTION_THIRD_LOGIN");
        ACTION_THIRD_LOGIN = sb.toString();
    }

    public static boolean show(Context context) {
        return show(context, false, 100);
    }

    public static boolean show(ZMActivity zMActivity, boolean z) {
        return show((Context) zMActivity, z, 100);
    }

    public static boolean show(Context context, boolean z, boolean z2) {
        return show(context, z, 100, null, z2);
    }

    public static boolean show(Context context, boolean z, int i) {
        return show(context, z, i, null);
    }

    public static boolean showWithPrefillName(Context context, boolean z, int i, @Nullable String str) {
        if (context == null) {
            return false;
        }
        if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance()) || !(context instanceof ZMActivity)) {
            Intent intent = new Intent(context, LoginActivity.class);
            boolean z2 = context instanceof Activity;
            if (!z2) {
                intent.addFlags(268435456);
            }
            intent.putExtra(ARG_PRODUCT_VENDOR, i);
            if (!StringUtil.isEmptyOrNull(str)) {
                intent.putExtra(ARG_PRE_FILL_NAME, str);
            }
            if (z) {
                intent.putExtra("loginType", PTApp.getInstance().getPTLoginType());
            }
            if (PTApp.getInstance().isTokenExpired()) {
                intent.putExtra(ARG_SHOW_FOR_TOKEN_EXPIRED, true);
            }
            ActivityStartHelper.startActivityForeground(context, intent);
            if (z2) {
                ((Activity) context).overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
            return true;
        }
        AuthFailedDialog.show((ZMActivity) context, context.getResources().getString(C4558R.string.zm_alert_network_disconnected));
        return false;
    }

    public static boolean show(Context context, boolean z, int i, AutoLogoffInfo autoLogoffInfo) {
        return show(context, z, i, autoLogoffInfo, false);
    }

    private static boolean show(@Nullable Context context, boolean z, int i, AutoLogoffInfo autoLogoffInfo, boolean z2) {
        if (context == null) {
            return false;
        }
        if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance()) || !(context instanceof ZMActivity)) {
            Intent intent = new Intent(context, LoginActivity.class);
            boolean z3 = context instanceof Activity;
            if (!z3) {
                intent.addFlags(268435456);
            }
            intent.putExtra(ARG_PRODUCT_VENDOR, i);
            intent.putExtra(ARG_AUTO_LOGOFF_INFO, autoLogoffInfo);
            if (z) {
                intent.putExtra("loginType", PTApp.getInstance().getPTLoginType());
            }
            if (PTApp.getInstance().isTokenExpired()) {
                intent.putExtra(ARG_SHOW_FOR_TOKEN_EXPIRED, true);
            }
            intent.putExtra(ARG_REVOKE_TOKEN, z2);
            ActivityStartHelper.startActivity(context, intent, NotificationType.LOGIN_NOTIFICATION.name(), null);
            if (z3) {
                ((Activity) context).overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
            return true;
        }
        AuthFailedDialog.show((ZMActivity) context, context.getResources().getString(C4558R.string.zm_alert_network_disconnected));
        return false;
    }

    public static boolean showForAuthUI(@Nullable ZMActivity zMActivity, @NonNull AuthResult authResult) {
        if (zMActivity == null) {
            return false;
        }
        Intent intent = new Intent(zMActivity, LoginActivity.class);
        intent.setAction(ACTION_THIRD_LOGIN);
        intent.putExtra(ARG_THIRD_LOGIN, authResult);
        intent.putExtra(ARG_AUTH_LAST_TIME, System.currentTimeMillis());
        ActivityStartHelper.startActivity(zMActivity, intent, NotificationType.LOGIN_NOTIFICATION.name(), null);
        return true;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(Bundle bundle) {
        AutoLogoffInfo autoLogoffInfo;
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(getApplicationContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
        }
        ZmComboMultiLogin.getInstance().setContext(this);
        disableFinishActivityByGesture(true);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        if (UIMgr.isLargeMode(this) && !UIMgr.isDualPaneSupportedInPortraitMode(this)) {
            setRequestedOrientation(0);
        } else if (!UIMgr.isLargeMode(this) && UIUtil.getDisplayMinWidthInDip(this) < 500.0f) {
            setRequestedOrientation(1);
        }
        if (PTApp.getInstance().isWebSignedOn()) {
            IMActivity.show(this);
            finish();
            overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            return;
        }
        int i = 100;
        Intent intent = getIntent();
        String str = null;
        if (intent != null) {
            i = intent.getIntExtra(ARG_PRODUCT_VENDOR, 100);
            AutoLogoffInfo autoLogoffInfo2 = (AutoLogoffInfo) intent.getSerializableExtra(ARG_AUTO_LOGOFF_INFO);
            intent.putExtra("loginType", -1);
            autoLogoffInfo = autoLogoffInfo2;
            str = intent.getStringExtra(ARG_PRE_FILL_NAME);
        } else {
            autoLogoffInfo = null;
        }
        this.mLoginView = new LoginView(this);
        this.mLoginView.setId(C4558R.C4560id.viewLogin);
        this.mLoginView.setSelectedProductVendor(i);
        if (!StringUtil.isEmptyOrNull(str)) {
            this.mLoginView.setPreFillName(str);
        }
        if (intent != null) {
            this.mLoginView.setSelectedLoginType(intent.getIntExtra("loginType", -1));
        }
        if (autoLogoffInfo != null) {
            this.mLoginView.setAutologoffInfo(autoLogoffInfo);
        }
        setContentView(this.mLoginView);
        this.mLoginView.restoreInstanceState(bundle);
        PTUI.getInstance().addPTUIListener(this);
        PTUI.getInstance().addIMListener(this.mIMListener);
        PTUI.getInstance().addGDPRListener(this);
        this.mLoginView.checkBeforeShowLogin(this);
        if (intent != null && intent.getBooleanExtra(ARG_REVOKE_TOKEN, false)) {
            RovokeTokenDialog.show(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        LoginView loginView = this.mLoginView;
        if (loginView != null) {
            loginView.saveInstanceState(bundle);
        }
        super.onSaveInstanceState(bundle);
    }

    public void onResume() {
        super.onResume();
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (PTApp.getInstance().isWebSignedOn() || (iMHelper != null && iMHelper.isIMSignedOn())) {
            IMActivity.show(this);
            finish();
            return;
        }
        ZMServiceHelper.doServiceActionInFront(PTService.ACTION_STOP_FOREGROUND, PTService.class);
        Intent intent = getIntent();
        if (ACTION_THIRD_LOGIN.equals(intent.getAction())) {
            long longExtra = intent.getLongExtra(ARG_AUTH_LAST_TIME, 0);
            if (mLastAuthTime != longExtra) {
                mLastAuthTime = longExtra;
                intent.setAction(null);
                setIntent(intent);
                ZmComboMultiLogin.getInstance().onAuthResult((AuthResult) intent.getParcelableExtra(ARG_THIRD_LOGIN));
                return;
            }
            return;
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && !mainboard.isInitialized()) {
            showLauncherActivity();
        }
    }

    public boolean isShownForTokenExpired() {
        Intent intent = getIntent();
        if (intent == null || !intent.getBooleanExtra(ARG_SHOW_FOR_TOKEN_EXPIRED, false)) {
            return false;
        }
        return true;
    }

    private void showLauncherActivity() {
        LauncherActivity.showLauncherActivity(this);
        finish();
    }

    public void onBackPressed() {
        if (isShownForTokenExpired()) {
            PTApp.getInstance().logout(0);
        }
        WelcomeActivity.show(this, true, false);
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
        ZmComboMultiLogin.getInstance().setContext(null);
        ZmComboMultiLogin.getInstance().onDestroy();
        PTUI.getInstance().removePTUIListener(this);
        PTUI.getInstance().removeIMListener(this.mIMListener);
        PTUI.getInstance().removeGDPRListener(this);
        if (this.mLoginView != null) {
            ZmSsoCloudSwitchNotify.getInstance().removeCloudSwitchNotifyListener(this.mLoginView);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ZmComboMultiLogin.getInstance().onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 8) {
            sinkIMLogin(j);
        } else if (i != 37) {
            switch (i) {
                case 0:
                    sinkWebLogin(j);
                    return;
                case 1:
                    sinkWebLogout(j);
                    return;
                default:
                    return;
            }
        } else {
            sinkWebAccessFail();
        }
    }

    public void sinkIMLogin(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkIMLogin", new EventAction("sinkIMLogin") {
            public void run(@NonNull IUIElement iUIElement) {
                ((LoginActivity) iUIElement).handleOnIMLogin(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnIMLogin(long j) {
        this.mLoginView.onIMLogin(j);
    }

    private void sinkWebLogout(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLogout", new EventAction("sinkWebLogout") {
            public void run(@NonNull IUIElement iUIElement) {
                ((LoginActivity) iUIElement).handleOnWebLogout(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebLogout(long j) {
        this.mLoginView.showConnecting(false);
    }

    public void sinkWebLogin(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLogin", new EventAction("sinkWebLogin") {
            public void run(@NonNull IUIElement iUIElement) {
                ((LoginActivity) iUIElement).handleOnWebLogin(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebLogin(long j) {
        this.mLoginView.onWebLogin(j);
    }

    private void sinkWebAccessFail() {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebAccessFail", new EventAction("sinkWebAccessFail") {
            public void run(@NonNull IUIElement iUIElement) {
                ((LoginActivity) iUIElement).handleOnWebAccessFail();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnWebAccessFail() {
        this.mLoginView.onWebAccessFail();
    }

    /* access modifiers changed from: private */
    public void handleOnIMLocalStatusChanged(int i) {
        this.mLoginView.onIMLocalStatusChanged(i);
    }

    public void performDialogAction(int i, int i2, Bundle bundle) {
        if (i == 1000) {
            if (i2 == -1) {
                PTUI.getInstance().ClearGDPRConfirmFlag();
                PTApp.getInstance().confirmGDPR(true);
            } else if (i2 == -2) {
                PTApp.getInstance().confirmGDPR(false);
            } else if (i2 == 1) {
                PTApp.getInstance().confirmGDPR(false);
            }
        } else if (i2 == FbConfirmCreateAccountDialog.ACTION_LOGIN_FB_FIRST) {
            ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
            if (zmInternationalMultiLogin != null) {
                zmInternationalMultiLogin.loginFacebookWithAcceptedTos();
            }
        }
    }

    public void OnShowPrivacyDialog(@Nullable String str, @Nullable String str2) {
        showGDPRConfirmDialog(str, str2);
    }

    public void NotifyUIToLogOut() {
        LogoutHandler.getInstance().startLogout();
        WelcomeActivity.show(this, false, false);
        finish();
    }

    private void showGDPRConfirmDialog(@Nullable String str, @Nullable String str2) {
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            ZMGDPRConfirmDialog findFragment = ZMGDPRConfirmDialog.findFragment(getSupportFragmentManager());
            if (findFragment != null) {
                findFragment.dismiss();
            }
            ZMGDPRConfirmDialog.showDialog(this, 1000, 1, str2, str);
        }
    }

    public void onAuthenticateSucceeded(AuthenticationResult authenticationResult) {
        this.mLoginView.loginZoomWithFingerprint();
    }

    public void onEnterPasswd() {
        this.mLoginView.requestFocusPasswd();
    }

    public void onCancelAgeGating() {
        LoginView loginView = this.mLoginView;
        if (loginView != null) {
            loginView.showConnecting(false);
        }
    }

    public void onConfirmAgeFailed(int i) {
        LoginView loginView = this.mLoginView;
        if (loginView != null) {
            loginView.showConnecting(false);
        }
    }
}
