package com.dropbox.core.android;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.dropbox.core.DbxRequestUtil;
import com.google.common.primitives.UnsignedBytes;
import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;

public class AuthActivity extends Activity {
    public static final String ACTION_AUTHENTICATE_V1 = "com.dropbox.android.AUTHENTICATE_V1";
    public static final String ACTION_AUTHENTICATE_V2 = "com.dropbox.android.AUTHENTICATE_V2";
    public static final String AUTH_PATH_CONNECT = "/connect";
    public static final int AUTH_VERSION = 1;
    private static final String DEFAULT_WEB_HOST = "www.dropbox.com";
    public static final String EXTRA_ACCESS_SECRET = "ACCESS_SECRET";
    public static final String EXTRA_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String EXTRA_ALREADY_AUTHED_UIDS = "ALREADY_AUTHED_UIDS";
    public static final String EXTRA_AUTH_STATE = "AUTH_STATE";
    public static final String EXTRA_CALLING_CLASS = "CALLING_CLASS";
    public static final String EXTRA_CALLING_PACKAGE = "CALLING_PACKAGE";
    public static final String EXTRA_CONSUMER_KEY = "CONSUMER_KEY";
    public static final String EXTRA_CONSUMER_SIG = "CONSUMER_SIG";
    public static final String EXTRA_DESIRED_UID = "DESIRED_UID";
    public static final String EXTRA_SESSION_ID = "SESSION_ID";
    public static final String EXTRA_UID = "UID";
    private static final String SIS_KEY_AUTH_STATE_NONCE = "SIS_KEY_AUTH_STATE_NONCE";
    /* access modifiers changed from: private */
    public static final String TAG = "com.dropbox.core.android.AuthActivity";
    public static Intent result = null;
    private static String[] sAlreadyAuthedUids;
    private static String sApiType;
    private static String sAppKey;
    private static String sDesiredUid;
    private static SecurityProvider sSecurityProvider = new SecurityProvider() {
        public SecureRandom getSecureRandom() {
            return FixedSecureRandom.get();
        }
    };
    private static final Object sSecurityProviderLock = new Object();
    private static String sSessionId;
    private static String sWebHost = DEFAULT_WEB_HOST;
    private boolean mActivityDispatchHandlerPosted = false;
    private String[] mAlreadyAuthedUids;
    private String mApiType;
    private String mAppKey;
    /* access modifiers changed from: private */
    public String mAuthStateNonce = null;
    private String mDesiredUid;
    private String mSessionId;
    private String mWebHost;

    public interface SecurityProvider {
        SecureRandom getSecureRandom();
    }

    static void setAuthParams(String str, String str2, String[] strArr) {
        setAuthParams(str, str2, strArr, null);
    }

    static void setAuthParams(String str, String str2, String[] strArr, String str3, String str4) {
        setAuthParams(str, str2, strArr, null, null, null);
    }

    static void setAuthParams(String str, String str2, String[] strArr, String str3) {
        setAuthParams(str, str2, strArr, str3, null, null);
    }

    static void setAuthParams(String str, String str2, String[] strArr, String str3, String str4, String str5) {
        sAppKey = str;
        sDesiredUid = str2;
        if (strArr == null) {
            strArr = new String[0];
        }
        sAlreadyAuthedUids = strArr;
        sSessionId = str3;
        if (str4 == null) {
            str4 = DEFAULT_WEB_HOST;
        }
        sWebHost = str4;
        sApiType = str5;
    }

    public static Intent makeIntent(Context context, String str, String str2, String str3) {
        return makeIntent(context, str, null, null, null, str2, str3);
    }

    public static Intent makeIntent(Context context, String str, String str2, String[] strArr, String str3, String str4, String str5) {
        if (str != null) {
            setAuthParams(str, str2, strArr, str3, str4, str5);
            return new Intent(context, AuthActivity.class);
        }
        throw new IllegalArgumentException("'appKey' can't be null");
    }

    public static boolean checkAppBeforeAuth(Context context, String str, boolean z) {
        Intent intent = new Intent("android.intent.action.VIEW");
        StringBuilder sb = new StringBuilder();
        sb.append("db-");
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("://");
        sb3.append(1);
        sb3.append(AUTH_PATH_CONNECT);
        intent.setData(Uri.parse(sb3.toString()));
        List queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.size() == 0) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("URI scheme in your app's manifest is not set up correctly. You should have a ");
            sb4.append(AuthActivity.class.getName());
            sb4.append(" with the scheme: ");
            sb4.append(sb2);
            throw new IllegalStateException(sb4.toString());
        } else if (queryIntentActivities.size() > 1) {
            if (z) {
                Builder builder = new Builder(context);
                builder.setTitle("Security alert");
                builder.setMessage("Another app on your phone may be trying to pose as the app you are currently using. The malicious app can't access your account, but linking to Dropbox has been disabled as a precaution. Please contact support@dropbox.com.");
                builder.setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            } else {
                String str2 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("There are multiple apps registered for the AuthActivity URI scheme (");
                sb5.append(sb2);
                sb5.append(").  Another app may be trying to  impersonate this app, so authentication will be disabled.");
                Log.w(str2, sb5.toString());
            }
            return false;
        } else {
            ResolveInfo resolveInfo = (ResolveInfo) queryIntentActivities.get(0);
            if (resolveInfo != null && resolveInfo.activityInfo != null && context.getPackageName().equals(resolveInfo.activityInfo.packageName)) {
                return true;
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("There must be a ");
            sb6.append(AuthActivity.class.getName());
            sb6.append(" within your app's package registered for your URI scheme (");
            sb6.append(sb2);
            sb6.append("). However, it appears that an activity in a different package is registered for that scheme instead. If you have multiple apps that all want to use the same accesstoken pair, designate one of them to do authentication and have the other apps launch it and then retrieve the token pair from it.");
            throw new IllegalStateException(sb6.toString());
        }
    }

    public static void setSecurityProvider(SecurityProvider securityProvider) {
        synchronized (sSecurityProviderLock) {
            sSecurityProvider = securityProvider;
        }
    }

    private static SecurityProvider getSecurityProvider() {
        SecurityProvider securityProvider;
        synchronized (sSecurityProviderLock) {
            securityProvider = sSecurityProvider;
        }
        return securityProvider;
    }

    private static SecureRandom getSecureRandom() {
        SecurityProvider securityProvider = getSecurityProvider();
        if (securityProvider != null) {
            return securityProvider.getSecureRandom();
        }
        return new SecureRandom();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        this.mAppKey = sAppKey;
        this.mWebHost = sWebHost;
        this.mApiType = sApiType;
        this.mDesiredUid = sDesiredUid;
        this.mAlreadyAuthedUids = sAlreadyAuthedUids;
        this.mSessionId = sSessionId;
        if (bundle == null) {
            result = null;
            this.mAuthStateNonce = null;
        } else {
            this.mAuthStateNonce = bundle.getString(SIS_KEY_AUTH_STATE_NONCE);
        }
        setTheme(16973840);
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(SIS_KEY_AUTH_STATE_NONCE, this.mAuthStateNonce);
    }

    static Intent getOfficialAuthIntent() {
        Intent intent = new Intent(ACTION_AUTHENTICATE_V2);
        intent.setPackage("com.dropbox.android");
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!isFinishing()) {
            if (this.mAuthStateNonce != null || this.mAppKey == null) {
                authFinished(null);
                return;
            }
            result = null;
            if (this.mActivityDispatchHandlerPosted) {
                Log.w(TAG, "onResume called again before Handler run");
                return;
            }
            final String createStateNonce = createStateNonce();
            final Intent officialAuthIntent = getOfficialAuthIntent();
            officialAuthIntent.putExtra(EXTRA_CONSUMER_KEY, this.mAppKey);
            officialAuthIntent.putExtra(EXTRA_CONSUMER_SIG, "");
            officialAuthIntent.putExtra(EXTRA_DESIRED_UID, this.mDesiredUid);
            officialAuthIntent.putExtra(EXTRA_ALREADY_AUTHED_UIDS, this.mAlreadyAuthedUids);
            officialAuthIntent.putExtra(EXTRA_SESSION_ID, this.mSessionId);
            officialAuthIntent.putExtra(EXTRA_CALLING_PACKAGE, getPackageName());
            officialAuthIntent.putExtra(EXTRA_CALLING_CLASS, getClass().getName());
            officialAuthIntent.putExtra(EXTRA_AUTH_STATE, createStateNonce);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Log.d(AuthActivity.TAG, "running startActivity in handler");
                    try {
                        if (DbxOfficialAppConnector.getDropboxAppPackage(AuthActivity.this, officialAuthIntent) != null) {
                            AuthActivity.this.startActivity(officialAuthIntent);
                        } else {
                            AuthActivity.this.startWebAuth(createStateNonce);
                        }
                        AuthActivity.this.mAuthStateNonce = createStateNonce;
                        AuthActivity.setAuthParams(null, null, null);
                    } catch (ActivityNotFoundException e) {
                        Log.e(AuthActivity.TAG, "Could not launch intent. User may have restricted profile", e);
                        AuthActivity.this.finish();
                    }
                }
            });
            this.mActivityDispatchHandlerPosted = true;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0096  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNewIntent(android.content.Intent r6) {
        /*
            r5 = this;
            java.lang.String r0 = r5.mAuthStateNonce
            r1 = 0
            if (r0 != 0) goto L_0x0009
            r5.authFinished(r1)
            return
        L_0x0009:
            java.lang.String r0 = "ACCESS_TOKEN"
            boolean r0 = r6.hasExtra(r0)
            if (r0 == 0) goto L_0x002a
            java.lang.String r0 = "ACCESS_TOKEN"
            java.lang.String r0 = r6.getStringExtra(r0)
            java.lang.String r2 = "ACCESS_SECRET"
            java.lang.String r2 = r6.getStringExtra(r2)
            java.lang.String r3 = "UID"
            java.lang.String r3 = r6.getStringExtra(r3)
            java.lang.String r4 = "AUTH_STATE"
            java.lang.String r6 = r6.getStringExtra(r4)
            goto L_0x0062
        L_0x002a:
            android.net.Uri r6 = r6.getData()
            if (r6 == 0) goto L_0x005e
            java.lang.String r0 = r6.getPath()
            java.lang.String r2 = "/connect"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x005e
            java.lang.String r0 = "oauth_token"
            java.lang.String r0 = r6.getQueryParameter(r0)     // Catch:{ UnsupportedOperationException -> 0x0059 }
            java.lang.String r2 = "oauth_token_secret"
            java.lang.String r2 = r6.getQueryParameter(r2)     // Catch:{ UnsupportedOperationException -> 0x0057 }
            java.lang.String r3 = "uid"
            java.lang.String r3 = r6.getQueryParameter(r3)     // Catch:{ UnsupportedOperationException -> 0x0055 }
            java.lang.String r4 = "state"
            java.lang.String r6 = r6.getQueryParameter(r4)     // Catch:{ UnsupportedOperationException -> 0x005c }
            goto L_0x0062
        L_0x0055:
            r3 = r1
            goto L_0x005c
        L_0x0057:
            r2 = r1
            goto L_0x005b
        L_0x0059:
            r0 = r1
            r2 = r0
        L_0x005b:
            r3 = r2
        L_0x005c:
            r6 = r1
            goto L_0x0062
        L_0x005e:
            r6 = r1
            r0 = r6
            r2 = r0
            r3 = r2
        L_0x0062:
            if (r0 == 0) goto L_0x00aa
            java.lang.String r4 = ""
            boolean r4 = r0.equals(r4)
            if (r4 != 0) goto L_0x00aa
            if (r2 == 0) goto L_0x00aa
            java.lang.String r4 = ""
            boolean r4 = r2.equals(r4)
            if (r4 != 0) goto L_0x00aa
            if (r3 == 0) goto L_0x00aa
            java.lang.String r4 = ""
            boolean r4 = r3.equals(r4)
            if (r4 != 0) goto L_0x00aa
            if (r6 == 0) goto L_0x00aa
            java.lang.String r4 = ""
            boolean r4 = r6.equals(r4)
            if (r4 != 0) goto L_0x00aa
            java.lang.String r4 = r5.mAuthStateNonce
            boolean r6 = r4.equals(r6)
            if (r6 != 0) goto L_0x0096
            r5.authFinished(r1)
            return
        L_0x0096:
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            java.lang.String r6 = "ACCESS_TOKEN"
            r1.putExtra(r6, r0)
            java.lang.String r6 = "ACCESS_SECRET"
            r1.putExtra(r6, r2)
            java.lang.String r6 = "UID"
            r1.putExtra(r6, r3)
        L_0x00aa:
            r5.authFinished(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.android.AuthActivity.onNewIntent(android.content.Intent):void");
    }

    private void authFinished(Intent intent) {
        result = intent;
        this.mAuthStateNonce = null;
        setAuthParams(null, null, null);
        finish();
    }

    /* access modifiers changed from: private */
    public void startWebAuth(String str) {
        String str2 = "1/connect";
        Locale locale = Locale.getDefault();
        Locale locale2 = new Locale(locale.getLanguage(), locale.getCountry());
        String[] strArr = this.mAlreadyAuthedUids;
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(DbxRequestUtil.buildUrlWithParams(locale2.toString(), this.mWebHost, str2, new String[]{"k", this.mAppKey, "n", strArr.length > 0 ? strArr[0] : "0", "api", this.mApiType, "state", str}))));
    }

    private String createStateNonce() {
        byte[] bArr = new byte[16];
        getSecureRandom().nextBytes(bArr);
        StringBuilder sb = new StringBuilder();
        sb.append("oauth2:");
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & UnsignedBytes.MAX_VALUE)}));
        }
        return sb.toString();
    }
}
