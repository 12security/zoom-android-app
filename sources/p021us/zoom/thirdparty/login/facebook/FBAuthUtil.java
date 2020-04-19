package p021us.zoom.thirdparty.login.facebook;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.thirdparty.login.facebook.FBAuthUtil */
public class FBAuthUtil {
    private static final String AUTH_TYPE = "rerequest";
    private static final String DIALOG_PARAM_AUTH_TYPE = "auth_type";
    private static final String DIALOG_PARAM_DEFAULT_AUDIENCE = "default_audience";
    private static final String DIALOG_PARAM_LEGACY_OVERRIDE = "legacy_override";
    private static final String DIALOG_PARAM_RESPONSE_TYPE = "response_type";
    private static final String DIALOG_PARAM_RETURN_SCOPES = "return_scopes";
    private static final String DIALOG_RESPONSE_TYPE_TOKEN_AND_SIGNED_REQUEST = "token,signed_request";
    private static final String DIALOG_RETURN_SCOPES_TRUE = "true";
    private static final String FACEBOOK_PROXY_AUTH_ACTIVITY = "com.facebook.katana.ProxyAuth";
    private static final String FACEBOOK_PROXY_AUTH_APP_ID_KEY = "client_id";
    private static final String FACEBOOK_PROXY_AUTH_PACKAGE = "com.facebook.katana";
    private static final String FACEBOOK_PROXY_AUTH_PERMISSIONS_KEY = "scope";
    private static final String FACEBOOK_SDK_VERSION_KEY = "facebook_sdk_version";
    public static final String FB_APP_SIGNATURE = "30820268308201d102044a9c4610300d06092a864886f70d0101040500307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e3020170d3039303833313231353231365a180f32303530303932353231353231365a307a310b3009060355040613025553310b3009060355040813024341311230100603550407130950616c6f20416c746f31183016060355040a130f46616365626f6f6b204d6f62696c653111300f060355040b130846616365626f6f6b311d301b0603550403131446616365626f6f6b20436f72706f726174696f6e30819f300d06092a864886f70d010101050003818d0030818902818100c207d51df8eb8c97d93ba0c8c1002c928fab00dc1b42fca5e66e99cc3023ed2d214d822bc59e8e35ddcf5f44c7ae8ade50d7e0c434f500e6c131f4a2834f987fc46406115de2018ebbb0d5a3c261bd97581ccfef76afc7135a6d59e8855ecd7eacc8f8737e794c60a761c536b72b11fac8e603f5da1a2d54aa103b8a13c0dbc10203010001300d06092a864886f70d0101040500038181005ee9be8bcbb250648d3b741290a82a1c9dc2e76a0af2f2228f1d9f9c4007529c446a70175c5a900d5141812866db46be6559e2141616483998211f4a673149fb2232a10d247663b26a9031e15f84bc1c74d141ff98a02d76f85b2c8ab2571b6469b232d8e768a7f7ca04f7abe4a775615916c07940656b58717457b42bd928a2";
    private static final String GRAPH_API_VERSION = "v3.2";
    private static String GRAPH_BASE_URL = "https://graph.facebook.com/%s/me?access_token=%s&fields=email,name,picture";
    public static final String[] PERMISSIONS = {"email", "public_profile", "user_friends"};
    public static final long REFRESH_TOKEN_BARRIER = 86400000;
    private static final String SDK_VERSION = "4.39.0";
    public static final String SINGLE_SIGN_ON_DISABLED = "service_disabled";

    public static String generateGraphUserUrl(String str) {
        return String.format(GRAPH_BASE_URL, new Object[]{GRAPH_API_VERSION, StringUtil.safeString(str)});
    }

    public static boolean startSingleSignOn(Activity activity, String str, String[] strArr, int i) {
        Intent createNativeAppIntent = createNativeAppIntent(str, strArr);
        if (!validateActivityIntent(activity, createNativeAppIntent)) {
            return false;
        }
        boolean z = true;
        try {
            activity.startActivityForResult(createNativeAppIntent, i);
        } catch (ActivityNotFoundException unused) {
            z = false;
        }
        return z;
    }

    public static boolean hasFacebookApp(Activity activity, String str, String[] strArr) {
        return validateActivityIntent(activity, createNativeAppIntent(str, strArr));
    }

    private static Intent createNativeAppIntent(String str, String[] strArr) {
        Intent intent = new Intent();
        intent.setClassName(FACEBOOK_PROXY_AUTH_PACKAGE, FACEBOOK_PROXY_AUTH_ACTIVITY);
        intent.putExtra("client_id", str);
        intent.putExtra(FACEBOOK_SDK_VERSION_KEY, SDK_VERSION);
        if (strArr != null && strArr.length > 0) {
            intent.putExtra("scope", TextUtils.join(PreferencesConstants.COOKIE_DELIMITER, strArr));
        }
        intent.putExtra("response_type", DIALOG_RESPONSE_TYPE_TOKEN_AND_SIGNED_REQUEST);
        intent.putExtra(DIALOG_PARAM_RETURN_SCOPES, DIALOG_RETURN_SCOPES_TRUE);
        intent.putExtra(DIALOG_PARAM_DEFAULT_AUDIENCE, null);
        intent.putExtra(DIALOG_PARAM_LEGACY_OVERRIDE, GRAPH_API_VERSION);
        intent.putExtra(DIALOG_PARAM_AUTH_TYPE, AUTH_TYPE);
        return intent;
    }

    public static boolean validateActivityIntent(Context context, Intent intent) {
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null) {
            return false;
        }
        return validateAppSignatureForPackage(context, resolveActivity.activityInfo.packageName);
    }

    private static boolean validateAppSignatureForPackage(Context context, String str) {
        try {
            for (Signature charsString : context.getPackageManager().getPackageInfo(str, 64).signatures) {
                if (charsString.toCharsString().equals(FB_APP_SIGNATURE)) {
                    return true;
                }
            }
            return false;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean validateServiceIntent(Context context, Intent intent) {
        ResolveInfo resolveService = context.getPackageManager().resolveService(intent, 0);
        if (resolveService == null) {
            return false;
        }
        return validateAppSignatureForPackage(context, resolveService.serviceInfo.packageName);
    }

    public static void authorizeCallback(@NonNull AuthToken authToken, int i, Intent intent, @NonNull FBAppAuthCallBack fBAppAuthCallBack) {
        if (i == -1) {
            String stringExtra = intent.getStringExtra("error");
            if (stringExtra == null) {
                stringExtra = intent.getStringExtra("error_type");
            }
            if (stringExtra == null) {
                authToken.setAccessToken(intent.getStringExtra("access_token"));
                authToken.setAccessExpiresIn(intent.getStringExtra("expires_in"));
                if (authToken.isSessionValid()) {
                    fBAppAuthCallBack.onComplete(intent.getExtras());
                } else {
                    fBAppAuthCallBack.onFaceBookError(new FacebookError("Failed to receive access token."));
                }
            } else if (stringExtra.equals(SINGLE_SIGN_ON_DISABLED) || stringExtra.equals("AndroidAuthKillSwitchException")) {
                fBAppAuthCallBack.forceAuthByBrowser();
            } else if (stringExtra.equals(AAD.WEB_UI_CANCEL) || stringExtra.equals("OAuthAccessDeniedException")) {
                fBAppAuthCallBack.onCancelAuth();
            } else {
                String stringExtra2 = intent.getStringExtra("error_description");
                if (stringExtra2 != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(stringExtra);
                    sb.append(":");
                    sb.append(stringExtra2);
                    stringExtra = sb.toString();
                }
                fBAppAuthCallBack.onFaceBookError(new FacebookError(stringExtra));
            }
        } else if (i != 0) {
        } else {
            if (intent != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Login failed: ");
                sb2.append(intent.getStringExtra("error"));
                Util.logd("Facebook-authorize", sb2.toString());
                fBAppAuthCallBack.onError(intent.getStringExtra("error"));
                return;
            }
            Util.logd("Facebook-authorize", "Login canceled by user.");
            fBAppAuthCallBack.onCancelAuth();
        }
    }

    public static void logout(Context context, @NonNull AuthToken authToken) {
        FBSessionStore.clear(FBSessionStore.FACEBOOK_KEY, context);
        authToken.setAccessToken(null);
        authToken.setExpires(0);
    }
}
