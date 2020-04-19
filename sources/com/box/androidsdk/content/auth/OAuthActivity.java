package com.box.androidsdk.content.auth;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;
import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.auth.ChooseAuthenticationFragment.OnAuthenticationChosen;
import com.box.androidsdk.content.auth.OAuthWebView.AuthFailure;
import com.box.androidsdk.content.auth.OAuthWebView.OAuthWebViewClient;
import com.box.androidsdk.content.auth.OAuthWebView.OAuthWebViewClient.WebEventListener;
import com.box.androidsdk.content.auth.OAuthWebView.OnPageFinishedListener;
import com.box.androidsdk.content.models.BoxError;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.utils.SdkUtils;
import com.box.sdk.android.C0469R;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.HttpHost;

public class OAuthActivity extends Activity implements OnAuthenticationChosen, WebEventListener, OnPageFinishedListener {
    public static final String AUTH_CODE = "authcode";
    public static final String AUTH_INFO = "authinfo";
    public static final int AUTH_TYPE_APP = 1;
    public static final int AUTH_TYPE_WEBVIEW = 0;
    private static final String CHOOSE_AUTH_TAG = "choose_auth";
    public static final String EXTRA_DISABLE_ACCOUNT_CHOOSING = "disableAccountChoosing";
    public static final String EXTRA_SESSION = "session";
    public static final String EXTRA_USER_ID_RESTRICTION = "restrictToUserId";
    protected static final String LOGIN_VIA_BOX_APP = "loginviaboxapp";
    public static final int REQUEST_BOX_APP_FOR_AUTH_CODE = 1;
    public static final String USER_ID = "userId";
    private static Dialog dialog;
    private AtomicBoolean apiCallStarted = new AtomicBoolean(false);
    private int authType = 0;
    /* access modifiers changed from: private */
    public boolean mAuthWasSuccessful = false;
    private String mClientId;
    private String mClientSecret;
    private BroadcastReceiver mConnectedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") && SdkUtils.isInternetAvailable(context) && OAuthActivity.this.oauthView != null) {
                String url = OAuthActivity.this.oauthView.getUrl();
                if (url != null && !url.startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
                    OAuthActivity.this.startOAuth();
                }
            }
        }
    };
    private String mDeviceId;
    private String mDeviceName;
    private ProgressDialog mProgresDialog;
    private String mRedirectUrl;
    /* access modifiers changed from: private */
    public BoxSession mSession;
    protected OAuthWebViewClient oauthClient;
    protected OAuthWebView oauthView;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getContentView());
        registerReceiver(this.mConnectedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        Intent intent = getIntent();
        this.mClientId = intent.getStringExtra("client_id");
        this.mClientSecret = intent.getStringExtra(BoxConstants.KEY_CLIENT_SECRET);
        this.mDeviceId = intent.getStringExtra(BoxConstants.KEY_BOX_DEVICE_ID);
        this.mDeviceName = intent.getStringExtra(BoxConstants.KEY_BOX_DEVICE_NAME);
        this.mRedirectUrl = intent.getStringExtra("redirect_uri");
        this.authType = intent.getBooleanExtra(LOGIN_VIA_BOX_APP, false) ? 1 : 0;
        this.apiCallStarted.getAndSet(false);
        this.mSession = (BoxSession) intent.getSerializableExtra("session");
        BoxSession boxSession = this.mSession;
        if (boxSession != null) {
            boxSession.setApplicationContext(getApplicationContext());
            return;
        }
        BoxSession boxSession2 = new BoxSession(this, null, this.mClientId, this.mClientSecret, this.mRedirectUrl);
        this.mSession = boxSession2;
        this.mSession.setDeviceId(this.mDeviceId);
        this.mSession.setDeviceName(this.mDeviceName);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        OAuthWebView oAuthWebView = this.oauthView;
        if (oAuthWebView == null || !oAuthWebView.getUrl().startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
            startOAuth();
        }
    }

    public void onReceivedAuthCode(String str) {
        onReceivedAuthCode(str, null);
    }

    public void onReceivedAuthCode(String str, String str2) {
        if (this.authType == 0) {
            this.oauthView.setVisibility(4);
        }
        ProgressDialog progressDialog = this.mProgresDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            this.mProgresDialog = ProgressDialog.show(this, null, getString(C0469R.string.zm_msg_loading), false, true);
        }
        startMakingOAuthAPICall(str, str2);
    }

    public void finish() {
        clearCachedAuthenticationData();
        if (!this.mAuthWasSuccessful) {
            BoxAuthentication.getInstance().onAuthenticationFailure(null, null);
        }
        super.finish();
    }

    public void onPageFinished(WebView webView, String str) {
        dismissSpinner();
    }

    public boolean onAuthFailure(AuthFailure authFailure) {
        if (authFailure.type == 2) {
            if (authFailure.mWebException.getErrorCode() == -6 || authFailure.mWebException.getErrorCode() == -2 || authFailure.mWebException.getErrorCode() == -8) {
                return false;
            }
            Resources resources = getResources();
            StringBuilder sb = new StringBuilder();
            sb.append(authFailure.mWebException.getErrorCode());
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(authFailure.mWebException.getDescription());
            Toast.makeText(this, String.format("%s\n%s: %s", new Object[]{resources.getString(C0469R.string.boxsdk_Authentication_fail), resources.getString(C0469R.string.boxsdk_details), sb.toString()}), 1).show();
        } else if (SdkUtils.isEmptyString(authFailure.message)) {
            Toast.makeText(this, C0469R.string.boxsdk_Authentication_fail, 1).show();
        } else if (authFailure.type != 1) {
            Toast.makeText(this, C0469R.string.boxsdk_Authentication_fail, 1).show();
        } else {
            Resources resources2 = getResources();
            Toast.makeText(this, String.format("%s\n%s: %s", new Object[]{resources2.getString(C0469R.string.boxsdk_Authentication_fail), resources2.getString(C0469R.string.boxsdk_details), resources2.getString(C0469R.string.boxsdk_Authentication_fail_url_mismatch)}), 1).show();
        }
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public int getContentView() {
        return C0469R.layout.boxsdk_activity_oauth;
    }

    /* access modifiers changed from: protected */
    public void startOAuth() {
        if (this.authType != 1 && !getIntent().getBooleanExtra(EXTRA_DISABLE_ACCOUNT_CHOOSING, false) && getFragmentManager().findFragmentByTag(CHOOSE_AUTH_TAG) == null) {
            Map storedAuthInfo = BoxAuthentication.getInstance().getStoredAuthInfo(this);
            if (SdkUtils.isEmptyString(getIntent().getStringExtra(EXTRA_USER_ID_RESTRICTION)) && storedAuthInfo != null && storedAuthInfo.size() > 0) {
                FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.replace(C0469R.C0471id.oauth_container, ChooseAuthenticationFragment.createAuthenticationActivity(this), CHOOSE_AUTH_TAG);
                beginTransaction.addToBackStack(CHOOSE_AUTH_TAG);
                beginTransaction.commit();
            }
        }
        switch (this.authType) {
            case 0:
                break;
            case 1:
                Intent boxAuthApp = getBoxAuthApp();
                if (boxAuthApp != null) {
                    boxAuthApp.putExtra("client_id", this.mClientId);
                    boxAuthApp.putExtra("redirect_uri", this.mRedirectUrl);
                    if (!SdkUtils.isEmptyString(getIntent().getStringExtra(EXTRA_USER_ID_RESTRICTION))) {
                        boxAuthApp.putExtra(EXTRA_USER_ID_RESTRICTION, getIntent().getStringExtra(EXTRA_USER_ID_RESTRICTION));
                    }
                    startActivityForResult(boxAuthApp, 1);
                    return;
                }
                break;
            default:
                return;
        }
        showSpinner();
        this.oauthView = createOAuthView();
        this.oauthClient = createOAuthWebViewClient(this.oauthView.getStateString());
        this.oauthClient.setOnPageFinishedListener(this);
        this.oauthView.setWebViewClient(this.oauthClient);
        if (this.mSession.getBoxAccountEmail() != null) {
            this.oauthView.setBoxAccountEmail(this.mSession.getBoxAccountEmail());
        }
        this.oauthView.authenticate(this.mClientId, this.mRedirectUrl);
    }

    /* access modifiers changed from: protected */
    public Intent getBoxAuthApp() {
        Intent intent = new Intent(BoxConstants.REQUEST_BOX_APP_FOR_AUTH_INTENT_ACTION);
        List<ResolveInfo> queryIntentActivities = getPackageManager().queryIntentActivities(intent, 65600);
        if (queryIntentActivities == null || queryIntentActivities.size() < 1) {
            return null;
        }
        String string = getResources().getString(C0469R.string.boxsdk_box_app_signature);
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            try {
                if (string.equals(getPackageManager().getPackageInfo(resolveInfo.activityInfo.packageName, 64).signatures[0].toCharsString())) {
                    intent.setPackage(resolveInfo.activityInfo.packageName);
                    Map storedAuthInfo = BoxAuthentication.getInstance().getStoredAuthInfo(this);
                    if (storedAuthInfo != null && storedAuthInfo.size() > 0) {
                        ArrayList arrayList = new ArrayList(storedAuthInfo.size());
                        for (Entry entry : storedAuthInfo.entrySet()) {
                            if (((BoxAuthenticationInfo) entry.getValue()).getUser() != null) {
                                arrayList.add(((BoxAuthenticationInfo) entry.getValue()).getUser().toJson());
                            }
                        }
                        if (arrayList.size() > 0) {
                            intent.putStringArrayListExtra(BoxConstants.KEY_BOX_USERS, arrayList);
                        }
                    }
                    return intent;
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(CHOOSE_AUTH_TAG) != null) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void onAuthenticationChosen(BoxAuthenticationInfo boxAuthenticationInfo) {
        if (boxAuthenticationInfo != null) {
            BoxAuthentication.getInstance().onAuthenticated(boxAuthenticationInfo, this);
            dismissSpinnerAndFinishAuthenticate(boxAuthenticationInfo);
        }
    }

    public void onDifferentAuthenticationChosen() {
        if (getFragmentManager().findFragmentByTag(CHOOSE_AUTH_TAG) != null) {
            getFragmentManager().popBackStack();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (-1 == i2 && 1 == i) {
            String stringExtra = intent.getStringExtra("userId");
            String stringExtra2 = intent.getStringExtra(AUTH_CODE);
            if (SdkUtils.isBlank(stringExtra2) && !SdkUtils.isBlank(stringExtra)) {
                BoxAuthenticationInfo boxAuthenticationInfo = (BoxAuthenticationInfo) BoxAuthentication.getInstance().getStoredAuthInfo(this).get(stringExtra);
                if (boxAuthenticationInfo != null) {
                    onAuthenticationChosen(boxAuthenticationInfo);
                } else {
                    onAuthFailure(new AuthFailure(0, ""));
                }
            } else if (!SdkUtils.isBlank(stringExtra2)) {
                startMakingOAuthAPICall(stringExtra2, null);
            }
        } else if (i2 == 0) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void startMakingOAuthAPICall(final String str, String str2) {
        if (!this.apiCallStarted.getAndSet(true)) {
            showSpinner();
            this.mSession.getAuthInfo().setBaseDomain(str2);
            new Thread() {
                public void run() {
                    try {
                        BoxAuthenticationInfo boxAuthenticationInfo = (BoxAuthenticationInfo) BoxAuthentication.getInstance().create(OAuthActivity.this.mSession, str).get();
                        String stringExtra = OAuthActivity.this.getIntent().getStringExtra(OAuthActivity.EXTRA_USER_ID_RESTRICTION);
                        if (!SdkUtils.isEmptyString(stringExtra)) {
                            if (!boxAuthenticationInfo.getUser().getId().equals(stringExtra)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Unexpected user logged in. Expected ");
                                sb.append(stringExtra);
                                sb.append(" received ");
                                sb.append(boxAuthenticationInfo.getUser().getId());
                                throw new RuntimeException(sb.toString());
                            }
                        }
                        OAuthActivity.this.dismissSpinnerAndFinishAuthenticate(boxAuthenticationInfo);
                    } catch (Exception e) {
                        OAuthActivity oAuthActivity = OAuthActivity.this;
                        oAuthActivity.dismissSpinnerAndFailAuthenticate(oAuthActivity.getAuthCreationErrorString(e));
                    }
                }
            }.start();
        }
    }

    /* access modifiers changed from: protected */
    public void dismissSpinnerAndFinishAuthenticate(final BoxAuthenticationInfo boxAuthenticationInfo) {
        runOnUiThread(new Runnable() {
            public void run() {
                OAuthActivity.this.dismissSpinner();
                Intent intent = new Intent();
                intent.putExtra(OAuthActivity.AUTH_INFO, boxAuthenticationInfo);
                OAuthActivity.this.setResult(-1, intent);
                OAuthActivity.this.mAuthWasSuccessful = true;
                OAuthActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void dismissSpinnerAndFailAuthenticate(final String str) {
        runOnUiThread(new Runnable() {
            public void run() {
                OAuthActivity.this.dismissSpinner();
                Toast.makeText(OAuthActivity.this, str, 1).show();
                OAuthActivity.this.setResult(0);
                OAuthActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public OAuthWebView createOAuthView() {
        OAuthWebView oAuthWebView = (OAuthWebView) findViewById(getOAuthWebViewRId());
        oAuthWebView.setVisibility(0);
        oAuthWebView.getSettings().setJavaScriptEnabled(true);
        return oAuthWebView;
    }

    /* access modifiers changed from: protected */
    public OAuthWebViewClient createOAuthWebViewClient(String str) {
        return new OAuthWebViewClient(this, this.mRedirectUrl, str);
    }

    /* access modifiers changed from: protected */
    public int getOAuthWebViewRId() {
        return C0469R.C0471id.oauthview;
    }

    /* access modifiers changed from: protected */
    public Dialog showDialogWhileWaitingForAuthenticationAPICall() {
        return ProgressDialog.show(this, getText(C0469R.string.boxsdk_Authenticating), getText(C0469R.string.boxsdk_Please_wait), false, true, new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                OAuthActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void showSpinner() {
        try {
            dialog = showDialogWhileWaitingForAuthenticationAPICall();
        } catch (Exception unused) {
            dialog = null;
        }
    }

    /* access modifiers changed from: protected */
    public void dismissSpinner() {
        Dialog dialog2 = dialog;
        if (dialog2 != null && dialog2.isShowing()) {
            try {
                dialog.dismiss();
            } catch (IllegalArgumentException unused) {
            }
            dialog = null;
        }
    }

    public void onDestroy() {
        ProgressDialog progressDialog = this.mProgresDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        unregisterReceiver(this.mConnectedReceiver);
        this.apiCallStarted.set(false);
        dismissSpinner();
        super.onDestroy();
    }

    public static Intent createOAuthActivityIntent(Context context, String str, String str2, String str3, boolean z) {
        Intent intent = new Intent(context, OAuthActivity.class);
        intent.putExtra("client_id", str);
        intent.putExtra(BoxConstants.KEY_CLIENT_SECRET, str2);
        if (!SdkUtils.isEmptyString(str3)) {
            intent.putExtra("redirect_uri", str3);
        }
        intent.putExtra(LOGIN_VIA_BOX_APP, z);
        return intent;
    }

    public static Intent createOAuthActivityIntent(Context context, BoxSession boxSession, boolean z) {
        Intent createOAuthActivityIntent = createOAuthActivityIntent(context, boxSession.getClientId(), boxSession.getClientSecret(), boxSession.getRedirectUrl(), z);
        createOAuthActivityIntent.putExtra("session", boxSession);
        if (!SdkUtils.isEmptyString(boxSession.getUserId())) {
            createOAuthActivityIntent.putExtra(EXTRA_USER_ID_RESTRICTION, boxSession.getUserId());
        }
        return createOAuthActivityIntent;
    }

    /* access modifiers changed from: private */
    public String getAuthCreationErrorString(Exception exc) {
        String str;
        String string = getString(C0469R.string.boxsdk_Authentication_fail);
        if (exc != null) {
            if (exc instanceof BoxException) {
                BoxException boxException = (BoxException) exc;
                BoxError asBoxError = boxException.getAsBoxError();
                if (asBoxError != null) {
                    if (boxException.getResponseCode() == 403 || boxException.getResponseCode() == 401 || asBoxError.getError().equals("unauthorized_device")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(":");
                        sb.append(getResources().getText(C0469R.string.boxsdk_Authentication_fail_forbidden));
                        sb.append(FontStyleHelper.SPLITOR);
                        str = sb.toString();
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(string);
                        sb2.append(":");
                        str = sb2.toString();
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(asBoxError.getErrorDescription());
                    return sb3.toString();
                }
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(string);
            sb4.append(":");
            sb4.append(exc);
            string = sb4.toString();
        }
        return string;
    }

    private void clearCachedAuthenticationData() {
        OAuthWebView oAuthWebView = this.oauthView;
        if (oAuthWebView != null) {
            oAuthWebView.clearCache(true);
            this.oauthView.clearFormData();
            this.oauthView.clearHistory();
        }
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
        File cacheDir = getCacheDir();
        SdkUtils.deleteFolderRecursive(cacheDir);
        cacheDir.mkdir();
    }
}
