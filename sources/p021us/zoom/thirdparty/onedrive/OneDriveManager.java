package p021us.zoom.thirdparty.onedrive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.onedrive.sdk.authentication.ADALAuthenticator;
import com.onedrive.sdk.authentication.MSAAuthenticator;
import com.onedrive.sdk.core.DefaultClientConfig;
import com.onedrive.sdk.core.IClientConfig;
import com.onedrive.sdk.logger.LoggerLevel;
import java.util.List;
import org.apache.http.cookie.ClientCookie;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.common.ZMBaseVendorMgr;
import p021us.zoom.thirdparty.onedrive.OneDrive.StatusChangedListener;

/* renamed from: us.zoom.thirdparty.onedrive.OneDriveManager */
public class OneDriveManager extends ZMBaseVendorMgr implements StatusChangedListener {
    private static OneDriveManager INSTANCE = null;
    public static final String[] SCOPES = {"wl.signin", "wl.basic", "onedrive.appfolder", "onedrive.readwrite", "wl.offline_access", "wl.skydrive_update", "wl.contacts_create", "wl.photos", "wl.contacts_photos", "wl.contacts_skydrive"};
    private static final int SDK_VERSION = 2;
    private static String mBusinessClientId;
    private static String mBusinessRedirectUrl;
    private static String mClientId;
    public static int mFileIntegrationType;
    private OneDrive mOneDrive = null;
    private OneDrive mOneDriveForBusiness = null;

    private native String getBusinessClientIDImpl();

    private native String getBusinessRedirectURLImpl();

    private native String getClientIDImpl();

    public boolean checkValid(Context context) {
        return false;
    }

    public static synchronized OneDriveManager getInstance() {
        OneDriveManager oneDriveManager;
        synchronized (OneDriveManager.class) {
            if (INSTANCE == null) {
                INSTANCE = new OneDriveManager();
            }
            oneDriveManager = INSTANCE;
        }
        return oneDriveManager;
    }

    public static synchronized void release() {
        synchronized (OneDriveManager.class) {
            if (INSTANCE != null) {
                INSTANCE.close();
                INSTANCE = null;
            }
        }
    }

    private void close() {
        OneDrive oneDrive = this.mOneDrive;
        if (oneDrive != null) {
            oneDrive.logout();
        }
    }

    public OneDrive createOneDrive(boolean z) {
        if (z) {
            this.mOneDriveForBusiness = new OneDrive(this, true);
            return this.mOneDriveForBusiness;
        }
        this.mOneDrive = new OneDrive(this, false);
        return this.mOneDrive;
    }

    public void onLogout(OneDrive oneDrive) {
        if (oneDrive == this.mOneDrive) {
            this.mOneDrive = null;
        }
    }

    public static boolean isLoginSupported(Context context, boolean z) {
        return canAuthViaBrowser(context) || OneDrivePicker.hasPicker(context, z);
    }

    public static boolean canAuthViaBrowser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean z = false;
        if (packageManager == null) {
            return false;
        }
        List queryIntentActivities = packageManager.queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("https://login.live.com/oauth20_authorize.srf")), 65536);
        if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
            z = true;
        }
        return z;
    }

    public boolean checkValid(boolean z) {
        boolean z2 = true;
        if (isZoomApp()) {
            return true;
        }
        if (!z) {
            return StringUtil.isEmptyOrNull(mClientId);
        }
        if (StringUtil.isEmptyOrNull(mBusinessClientId) || StringUtil.isEmptyOrNull(mBusinessRedirectUrl)) {
            z2 = false;
        }
        return z2;
    }

    public static IClientConfig createConfig() {
        IClientConfig createWithAuthenticator = DefaultClientConfig.createWithAuthenticator(new MSAAuthenticator() {
            public String getClientId() {
                return OneDriveManager.getInstance().getClientID();
            }

            public String[] getScopes() {
                return OneDriveManager.SCOPES;
            }
        });
        createWithAuthenticator.getLogger().setLoggingLevel(LoggerLevel.Debug);
        return createWithAuthenticator;
    }

    public static IClientConfig createBusinessConfig() {
        IClientConfig createWithAuthenticator = DefaultClientConfig.createWithAuthenticator(new ADALAuthenticator() {
            public String getClientId() {
                return OneDriveManager.getInstance().getBusinessClientID();
            }

            /* access modifiers changed from: protected */
            public String getRedirectUrl() {
                return OneDriveManager.getInstance().getBusinessRedirectURL();
            }
        });
        createWithAuthenticator.getLogger().setLoggingLevel(LoggerLevel.Debug);
        return createWithAuthenticator;
    }

    public static void setClientID(String str) {
        mClientId = str;
    }

    public static void setBusinessAppInfo(String str, String str2) {
        mBusinessClientId = str;
        mBusinessRedirectUrl = str2;
    }

    /* access modifiers changed from: private */
    public String getClientID() {
        if (isZoomApp()) {
            return getClientIDImpl();
        }
        return mClientId;
    }

    /* access modifiers changed from: private */
    public String getBusinessClientID() {
        if (isZoomApp()) {
            return getBusinessClientIDImpl();
        }
        return mBusinessClientId;
    }

    /* access modifiers changed from: private */
    public String getBusinessRedirectURL() {
        if (isZoomApp()) {
            return getBusinessRedirectURLImpl();
        }
        return mBusinessRedirectUrl;
    }

    public int getmFileIntegrationType() {
        return mFileIntegrationType;
    }

    public Intent createOneDriveIntent(String str, boolean z) {
        Intent intent = new Intent();
        intent.setAction(str);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("appId", z ? getBusinessClientID() : getClientID());
        intent.putExtra(ClientCookie.VERSION_ATTR, 2);
        return intent;
    }
}
