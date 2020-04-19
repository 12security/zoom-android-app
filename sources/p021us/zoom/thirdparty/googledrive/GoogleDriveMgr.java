package p021us.zoom.thirdparty.googledrive;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.common.ZMBaseVendorMgr;
import p021us.zoom.thirdparty.googledrive.GoogleDrive.DriveChangeListener;
import p021us.zoom.thirdparty.login.util.GoogleAuthUtil;

/* renamed from: us.zoom.thirdparty.googledrive.GoogleDriveMgr */
public class GoogleDriveMgr extends ZMBaseVendorMgr implements DriveChangeListener {
    private static GoogleDriveMgr INSTANCE = null;
    private static final String TAG = "GoogleDriveMgr";
    private static String mCustomizedClientID;
    private static String mCustomizedRedirectURL;
    public static int mFileIntegrationType;
    private GoogleDrive mGoogleDrive;

    public static synchronized GoogleDriveMgr getInstance() {
        GoogleDriveMgr googleDriveMgr;
        synchronized (GoogleDriveMgr.class) {
            if (INSTANCE == null) {
                INSTANCE = new GoogleDriveMgr();
            }
            googleDriveMgr = INSTANCE;
        }
        return googleDriveMgr;
    }

    public static synchronized void release() {
        synchronized (GoogleDriveMgr.class) {
            if (INSTANCE != null) {
                INSTANCE.close();
                INSTANCE = null;
            }
        }
    }

    public static void setAppInfo(String str, String str2) {
        mCustomizedClientID = str;
        mCustomizedRedirectURL = str2;
    }

    @Nullable
    public String getCustomizedClientID(Context context) {
        if (context == null || isZoomApp()) {
            return null;
        }
        return mCustomizedClientID;
    }

    @Nullable
    public String getRedirectURL(Context context) {
        if (context == null || isZoomApp()) {
            return null;
        }
        return mCustomizedRedirectURL;
    }

    private static String[] getScopes(@NonNull Context context, boolean z) {
        if (z) {
            return getScopes(true);
        }
        return getScopes(false);
    }

    public static String[] getScopes(boolean z) {
        return z ? GoogleAuthUtil.SCOPES : GoogleAuthUtil.GOOGLE_DRIVE_SCOPES;
    }

    public GoogleDrive getGoogleDrive(Context context) {
        if (this.mGoogleDrive == null) {
            checkValid(context);
            this.mGoogleDrive = new GoogleDrive(getScopes(context, isZoomApp()), this, isZoomApp());
        }
        return this.mGoogleDrive;
    }

    private void close() {
        GoogleDrive googleDrive = this.mGoogleDrive;
        if (googleDrive != null) {
            googleDrive.logout();
        }
    }

    public void onLogout(GoogleDrive googleDrive) {
        if (this.mGoogleDrive == googleDrive) {
            this.mGoogleDrive = null;
        }
    }

    public boolean checkValid(Context context) {
        boolean z = true;
        if (isZoomApp()) {
            return true;
        }
        String customizedClientID = getCustomizedClientID(context);
        String redirectURL = getRedirectURL(context);
        if (StringUtil.isEmptyOrNull(customizedClientID) || StringUtil.isEmptyOrNull(redirectURL)) {
            z = false;
        }
        return z;
    }

    public int getmFileIntegrationType() {
        return mFileIntegrationType;
    }
}
