package com.zipow.videobox;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.common.ZMNoticeChooseDomainTask;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.OnButtonClickListener;
import com.zipow.videobox.dialog.ZMJoinConfirmDialog;
import com.zipow.videobox.fragment.CreateProfileFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.UrlActionData;
import com.zipow.videobox.ptapp.ShareScreenDialogHelper;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.thirdparty.ThirdPartyHandler;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.IClientUriHandler;
import com.zipow.videobox.util.ZmPtUtils;
import java.net.URLDecoder;
import org.apache.http.HttpHost;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.videomeetings.C4558R;

public class JoinByURLActivity extends ZMActivity {
    public static final String ACTION_SWITCH_CALL;
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final String ARG_URL_ACTION = "urlAction";
    private static final String TAG = JoinByURLActivity.class.getSimpleName();

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(JoinByURLActivity.class.getName());
        sb.append(".action.SWITCH_CALL");
        ACTION_SWITCH_CALL = sb.toString();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ZMActivity frontActivity = getFrontActivity();
        if ((frontActivity instanceof SignupActivity) || (frontActivity instanceof ForgetPasswordActivity)) {
            frontActivity.finish();
        }
    }

    public void onResume() {
        super.onResume();
        if (ForegroundTaskManager.getInstance().containsTask(ZMNoticeChooseDomainTask.class.getName())) {
            PTApp.getInstance().onUserSkipSignToJoinOption();
        }
        if (!PTApp.getInstance().isWebSignedOn()) {
            PTApp.getInstance().setCurrentUIFlag();
        }
        Intent intent = getIntent();
        if (intent == null || UIUtil.isScreenLocked(this)) {
            finish();
        } else if (checkSupported()) {
            Uri data = intent.getData();
            if (data != null && "geo".equalsIgnoreCase(data.getScheme())) {
                String encodedQuery = data.getEncodedQuery();
                if (StringUtil.isEmptyOrNull(encodedQuery)) {
                    finish();
                    return;
                }
                int indexOf = encodedQuery.indexOf(HttpHost.DEFAULT_SCHEME_NAME);
                if (indexOf == -1) {
                    finish();
                    return;
                }
                try {
                    String decode = URLDecoder.decode(encodedQuery.substring(indexOf), "UTF-8");
                    if (StringUtil.isEmptyOrNull(decode)) {
                        finish();
                        return;
                    }
                    data = Uri.parse(decode);
                } catch (Exception unused) {
                    finish();
                    return;
                }
            }
            Mainboard mainboard = Mainboard.getMainboard();
            String str = null;
            if (mainboard == null || !mainboard.isInitialized()) {
                finish();
                if (data != null) {
                    LauncherActivity.showLauncherActivityForUri(this, data.toString());
                } else {
                    LauncherActivity.showLauncherActivity(this, null, null);
                }
            } else if (data != null && "ZoomPhoneCall".equalsIgnoreCase(data.getScheme())) {
                if (NetworkUtil.hasDataNetwork(this)) {
                    CmmSIPCallManager.getInstance().setPendingNumber(data.getSchemeSpecificPart());
                }
                if (PTApp.getInstance().isWebSignedOn()) {
                    IMActivity.show(this);
                } else {
                    showWelcomeUI();
                }
                finish();
            } else if (ACTION_SWITCH_CALL.equals(intent.getAction())) {
                PTApp.getInstance().forceSyncLeaveCurrentCall();
                PTApp.getInstance().dispatchIdleMessage();
                String stringExtra = intent.getStringExtra("urlAction");
                String stringExtra2 = intent.getStringExtra("screenName");
                UrlActionData parseURLActionData = PTApp.getInstance().parseURLActionData(stringExtra);
                String confno = parseURLActionData != null ? parseURLActionData.getConfno() : null;
                if (parseURLActionData != null) {
                    str = parseURLActionData.getConfid();
                }
                ZmPtUtils.switchToVendor(ZmPtUtils.parseVendorFromUrl(stringExtra));
                if (parseURLActionData != null && parseURLActionData.getAction() == 1) {
                    VideoBoxApplication.getNonNullInstance().setConfUIPreloaded(false);
                    mainboard.notifyUrlAction(stringExtra);
                } else if (StringUtil.isEmptyOrNull(confno) && StringUtil.isEmptyOrNull(str)) {
                    mainboard.notifyUrlAction(stringExtra);
                } else if (stringExtra != null) {
                    joinByUrlAction(parseURLActionData, stringExtra, stringExtra2);
                }
                finish();
            } else if (data == null) {
                finish();
            } else if (data.getPathSegments() == null) {
                finish();
            } else if (!handleClientURI(data) && !handleSetPwdURI(data)) {
                ZmPtUtils.switchToVendor(ZmPtUtils.parseVendorFromUrl(data.toString()));
                parseURLActionData(data);
            }
        }
    }

    private void parseURLActionData(@NonNull Uri uri) {
        UrlActionData parseURLActionData = PTApp.getInstance().parseURLActionData(uri.toString());
        boolean z = true;
        if (!PTApp.getInstance().isWebSignedOn() && parseURLActionData != null && parseURLActionData.getAction() == 1 && "0".equals(parseURLActionData.getSnsType())) {
            String snsToken = parseURLActionData.getSnsToken();
            if (!StringUtil.isEmptyOrNull(snsToken)) {
                AuthToken authToken = new AuthToken();
                authToken.setAccessToken(snsToken);
                authToken.setExpires(2147483647L);
                FBSessionStore.save(FBSessionStore.FACEBOOK_KEY, authToken, this);
            }
        }
        if (parseURLActionData != null) {
            int action = parseURLActionData.getAction();
            if (action == 2) {
                z = checkPBXToConf(uri, false);
            } else if (action == 1) {
                z = checkPBXToConf(uri, true);
            } else if (action == 3) {
                Mainboard.getMainboard().notifyUrlAction(uri.toString());
                if (PTApp.getInstance().isWebSignedOn()) {
                    IMActivity.show(this);
                } else {
                    showWelcomeUI();
                }
            } else if (!ThirdPartyHandler.parseAuthResult(this, action, uri)) {
                z = parseZoomAction(uri);
            }
        } else {
            z = parseZoomAction(uri);
        }
        if (z) {
            finish();
        }
    }

    private boolean parseZoomAction(@NonNull Uri uri) {
        int parseZoomAction = PTApp.getInstance().parseZoomAction(uri.toString());
        if (parseZoomAction == 1) {
            ShareScreenDialogHelper.getInstance().showShareScreen(this, true);
            return false;
        } else if (parseZoomAction == 2 || parseZoomAction == 3) {
            Mainboard mainboard = Mainboard.getMainboard();
            if (mainboard != null) {
                mainboard.notifyUrlAction(uri.toString());
            }
            if (PTApp.getInstance().isWebSignedOn()) {
                return true;
            }
            showWelcomeUI();
            return true;
        } else {
            VideoBoxApplication.getNonNullInstance().setConfUIPreloaded(false);
            Mainboard mainboard2 = Mainboard.getMainboard();
            if (mainboard2 == null) {
                return true;
            }
            mainboard2.notifyUrlAction(uri.toString());
            return true;
        }
    }

    private boolean checkSupported() {
        ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        if ((activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 131072) && !Build.CPU_ABI.equals("armeabi") && !Build.CPU_ABI.startsWith("armeabi-v6")) {
            return true;
        }
        new Builder(this).setTitle(C4558R.string.zm_app_name).setMessage(C4558R.string.zm_msg_devices_not_supported).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                JoinByURLActivity.this.finish();
            }
        }).show();
        return false;
    }

    /* access modifiers changed from: private */
    public boolean joinByUrl(@Nullable Uri uri) {
        String str;
        if (uri != null && ZMPolicyUIHelper.isShowConfirmDialogWhenWebJoin()) {
            return ZMJoinConfirmDialog.show(this, uri);
        }
        if (uri == null) {
            str = "";
        } else {
            str = uri.toString();
        }
        return ConfActivity.joinByUrl((Context) this, str);
    }

    private void joinByUrlAction(@Nullable UrlActionData urlActionData, @NonNull String str, String str2) {
        if (urlActionData == null || !ZMPolicyUIHelper.isShowConfirmDialogWhenWebJoin()) {
            ConfActivity.joinByUrl(this, str, str2);
        } else {
            ZMJoinConfirmDialog.show(this, urlActionData, str, str2);
        }
    }

    /* access modifiers changed from: private */
    public boolean webStart(Uri uri) {
        return ConfActivity.webStart(this, uri.toString());
    }

    private boolean checkPBXToConf(@NonNull final Uri uri, final boolean z) {
        if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            MeetingInSipCallConfirmDialog.show(this, new OnButtonClickListener() {
                public void onPositiveClick() {
                    if (z) {
                        JoinByURLActivity.this.webStart(uri);
                    } else {
                        JoinByURLActivity.this.joinByUrl(uri);
                    }
                    JoinByURLActivity.this.finish();
                }

                public void onNegativeClick() {
                    JoinByURLActivity.this.finish();
                }
            });
            return false;
        } else if (z) {
            return webStart(uri);
        } else {
            return joinByUrl(uri);
        }
    }

    public static void switchCallTo(@NonNull Context context, String str, String str2) {
        Intent intent = new Intent(context, JoinByURLActivity.class);
        intent.setFlags(268435456);
        intent.setAction(ACTION_SWITCH_CALL);
        intent.putExtra("urlAction", str);
        intent.putExtra("screenName", str2);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    private boolean handleClientURI(Uri uri) {
        if (!getZoomScheme().equals(uri.getScheme())) {
            return false;
        }
        if (!handleClientURIWithExtHandler(uri)) {
            if ("client".equals(uri.getHost())) {
                String path = uri.getPath();
                if ("/signup".equals(path)) {
                    showSignup();
                } else if ("/forgetpwd".equals(path)) {
                    showForgetPassowrd();
                }
            } else if (!"open".equals(uri.getHost()) && !"".equals(uri.getHost())) {
                return false;
            } else {
                showMainUI();
            }
        }
        finish();
        return true;
    }

    private boolean handleClientURIWithExtHandler(Uri uri) {
        String string = ResourcesUtil.getString((Context) this, C4558R.string.zm_config_ext_client_uri_handler);
        if (!StringUtil.isEmptyOrNull(string)) {
            try {
                return ((IClientUriHandler) Class.forName(string).newInstance()).handleUri(this, uri);
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private void showMainUI() {
        if (PTApp.getInstance().hasActiveCall()) {
            ConfLocalHelper.returnToConf(this);
        } else {
            showWelcomeUI();
        }
    }

    public void showWelcomeUI() {
        WelcomeActivity.show(this, false, true, null, null);
        overridePendingTransition(0, 0);
    }

    @NonNull
    private String getZoomScheme() {
        String string = getString(C4558R.string.zm_zoom_scheme);
        return StringUtil.isEmptyOrNull(string) ? "zoomus" : string;
    }

    private void showSignup() {
        ForegroundTaskManager.getInstance().runInForeground(new ZMNoticeOnShowAgeGatingTask(ZMNoticeOnShowAgeGatingTask.class.getName()));
    }

    private void showForgetPassowrd() {
        ForgetPasswordActivity.show(this);
    }

    private boolean handleSetPwdURI(Uri uri) {
        if (!getZoomScheme().equals(uri.getScheme())) {
            return false;
        }
        if (!"/setpwd".equals(uri.getPath())) {
            return false;
        }
        String queryParameter = uri.getQueryParameter("type");
        String queryParameter2 = uri.getQueryParameter("email");
        String queryParameter3 = uri.getQueryParameter("uname");
        String queryParameter4 = uri.getQueryParameter("code");
        String queryParameter5 = uri.getQueryParameter("fname");
        String queryParameter6 = uri.getQueryParameter("lname");
        if ("set".equals(queryParameter)) {
            showSetPassword(queryParameter2, queryParameter5, queryParameter6, queryParameter4);
        } else if ("reset".equals(queryParameter)) {
            showResetPassword(queryParameter2, queryParameter3, queryParameter4);
        }
        finish();
        return true;
    }

    private void showSetPassword(String str, String str2, String str3, String str4) {
        CreateProfileFragment.showAsActivity((ZMActivity) this, str2, str3, str, str4);
    }

    private void showResetPassword(String str, String str2, String str3) {
        SetPasswordActivity.show(this, str2, str, str3);
    }
}
