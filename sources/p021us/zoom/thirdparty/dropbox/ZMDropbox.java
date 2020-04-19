package p021us.zoom.thirdparty.dropbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dropbox.core.android.Auth;
import java.util.List;
import p021us.zoom.androidlib.app.IZMAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.common.ZMBaseVendorMgr;
import p021us.zoom.thirdparty.dropbox.ZMDropboxClient.DropboxListener;

/* renamed from: us.zoom.thirdparty.dropbox.ZMDropbox */
public class ZMDropbox extends ZMBaseVendorMgr {
    private static final String ACTION_AUTHENTICATE_V2 = "com.dropbox.android.AUTHENTICATE_V2";
    private static final String[] DROPBOX_APP_SIGNATURES = {"308202223082018b02044bd207bd300d06092a864886f70d01010405003058310b3009060355040613025553310b3009060355040813024341311630140603550407130d53616e204672616e636973636f3110300e060355040a130744726f70626f783112301006035504031309546f6d204d65796572301e170d3130303432333230343930315a170d3430303431353230343930315a3058310b3009060355040613025553310b3009060355040813024341311630140603550407130d53616e204672616e636973636f3110300e060355040a130744726f70626f783112301006035504031309546f6d204d6579657230819f300d06092a864886f70d010101050003818d0030818902818100ac1595d0ab278a9577f0ca5a14144f96eccde75f5616f36172c562fab0e98c48ad7d64f1091c6cc11ce084a4313d522f899378d312e112a748827545146a779defa7c31d8c00c2ed73135802f6952f59798579859e0214d4e9c0554b53b26032a4d2dfc2f62540d776df2ea70e2a6152945fb53fef5bac5344251595b729d4810203010001300d06092a864886f70d01010405000381810055c425d94d036153203dc0bbeb3516f94563b102fff39c3d4ed91278db24fc4424a244c2e59f03bbfea59404512b8bf74662f2a32e37eafa2ac904c31f99cfc21c9ff375c977c432d3b6ec22776f28767d0f292144884538c3d5669b568e4254e4ed75d9054f75229ac9d4ccd0b7c3c74a34f07b7657083b2aa76225c0c56ffc", "308201e53082014ea00302010202044e17e115300d06092a864886f70d01010505003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3131303730393035303331375a170d3431303730313035303331375a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730819f300d06092a864886f70d010101050003818d003081890281810096759fe5abea6a0757039b92adc68d672efa84732c3f959408e12efa264545c61f23141026a6d01eceeeaa13ec7087087e5894a3363da8bf5c69ed93657a6890738a80998e4ca22dc94848f30e2d0e1890000ae2cddf543b20c0c3828deca6c7944b5ecd21a9d18c988b2b3e54517dafbc34b48e801bb1321e0fa49e4d575d7f0203010001300d06092a864886f70d0101050500038181002b6d4b65bcfa6ec7bac97ae6d878064d47b3f9f8da654995b8ef4c385bc4fbfbb7a987f60783ef0348760c0708acd4b7e63f0235c35a4fbcd5ec41b3b4cb295feaa7d5c27fa562a02562b7e1f4776b85147be3e295714986c4a9a07183f48ea09ae4d3ea31b88d0016c65b93526b9c45f2967c3d28dee1aff5a5b29b9c2c8639"};
    private static final String DROPBOX_PREFS_NAME = "dropbox_prefs";
    private static ZMDropbox INSTANCE = null;
    private static final String PREF_KEY_TOKEN = "ACCESS-TOKEN";
    private static String mCustomerAppKey;
    private static String mCustomerAppSecret;
    public static int mFileIntegrationType;
    private ZMDropboxClient mDropboxClient;

    private native void InitAuthImpl(Context context, boolean z);

    public static synchronized ZMDropbox getInstance() {
        ZMDropbox zMDropbox;
        synchronized (ZMDropbox.class) {
            if (INSTANCE == null) {
                INSTANCE = new ZMDropbox();
            }
            zMDropbox = INSTANCE;
        }
        return zMDropbox;
    }

    public static synchronized void release() {
        synchronized (ZMDropbox.class) {
            if (INSTANCE != null) {
                INSTANCE.logout();
                INSTANCE = null;
            }
        }
    }

    public void login(Context context) {
        if (context != null && checkValid(context)) {
            InitAuthImpl(context, isZoomApp());
        }
    }

    public void logout() {
        this.mDropboxClient = null;
        IZMAppUtil iZMAppUtil = getInstance().getmIZMAppUtil();
        if (iZMAppUtil != null) {
            Context appContext = iZMAppUtil.getAppContext();
            if (appContext != null) {
                Editor edit = appContext.getSharedPreferences(DROPBOX_PREFS_NAME, 0).edit();
                edit.putString(PREF_KEY_TOKEN, null);
                edit.commit();
            }
        }
    }

    public boolean onResume(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DROPBOX_PREFS_NAME, 0);
        String string = sharedPreferences.getString(PREF_KEY_TOKEN, null);
        String oAuth2Token = Auth.getOAuth2Token();
        Editor edit = sharedPreferences.edit();
        if (!StringUtil.isEmptyOrNull(string) || !StringUtil.isEmptyOrNull(oAuth2Token)) {
            if (StringUtil.isEmptyOrNull(oAuth2Token) || (!StringUtil.isEmptyOrNull(string) && oAuth2Token.equals(string))) {
                initClient(string);
            } else {
                edit.putString(PREF_KEY_TOKEN, oAuth2Token);
                edit.commit();
                initClient(oAuth2Token);
            }
            return true;
        }
        initClient(null);
        return false;
    }

    private void initClient(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            logout();
            return;
        }
        ZMDropboxClient zMDropboxClient = this.mDropboxClient;
        if (zMDropboxClient == null || !str.equals(zMDropboxClient.getToken())) {
            this.mDropboxClient = new ZMDropboxClient(str);
        }
    }

    public boolean hasLogin() {
        ZMDropboxClient zMDropboxClient = this.mDropboxClient;
        return zMDropboxClient != null && !StringUtil.isEmptyOrNull(zMDropboxClient.getToken());
    }

    public ZMDropboxClient getClient() {
        return this.mDropboxClient;
    }

    public void setListener(DropboxListener dropboxListener) {
        ZMDropboxClient zMDropboxClient = this.mDropboxClient;
        if (zMDropboxClient != null) {
            zMDropboxClient.setListener(dropboxListener);
        }
    }

    public boolean checkValid(Context context) {
        return isZoomApp() || getCustomizedAppKeyPair(context) != null;
    }

    public static void setAppKeyPair(@NonNull Context context, String str, String str2) {
        mCustomerAppKey = str;
        mCustomerAppSecret = str2;
    }

    @Nullable
    public static ZMAppKeyPair getCustomizedAppKeyPair(Context context) {
        if (context == null || getInstance().isZoomApp()) {
            return null;
        }
        return new ZMAppKeyPair(mCustomerAppKey, mCustomerAppSecret);
    }

    public boolean isDropboxLoginSupported(Context context) {
        return canAuthViaBrowser(context) || hasDropboxApp(context);
    }

    private boolean canAuthViaBrowser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean z = false;
        if (packageManager == null) {
            return false;
        }
        List queryIntentActivities = packageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("https://www.dropbox.com:443")), 65536);
        if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
            z = true;
        }
        return z;
    }

    private boolean hasDropboxApp(Context context) {
        Signature[] signatureArr;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setPackage("com.dropbox.android");
        intent.setAction("com.dropbox.android.AUTHENTICATE_V2");
        List queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || 1 != queryIntentActivities.size()) {
            return false;
        }
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 0);
        if (resolveActivity == null) {
            return false;
        }
        try {
            for (Signature signature : packageManager.getPackageInfo(resolveActivity.activityInfo.packageName, 64).signatures) {
                for (String equals : DROPBOX_APP_SIGNATURES) {
                    if (equals.equals(signature.toCharsString())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }

    public int getmFileIntegrationType() {
        return mFileIntegrationType;
    }
}
