package p021us.zoom.thirdparty.box;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.box.Box.BoxChangeListener;
import p021us.zoom.thirdparty.common.ZMBaseVendorMgr;

/* renamed from: us.zoom.thirdparty.box.BoxMgr */
public class BoxMgr extends ZMBaseVendorMgr implements BoxChangeListener {
    private static BoxMgr INSTANCE = null;
    public static final String ROOT_FOLDER_ID = "0";
    private static String mCustomerAppKey;
    private static String mCustomerAppSecret;
    private static String mCustomerRedirectUrl;
    public static int mFileIntegrationType;
    private Box mBox;

    public static synchronized BoxMgr getInstance() {
        BoxMgr boxMgr;
        synchronized (BoxMgr.class) {
            if (INSTANCE == null) {
                INSTANCE = new BoxMgr();
            }
            boxMgr = INSTANCE;
        }
        return boxMgr;
    }

    public static synchronized void release() {
        synchronized (BoxMgr.class) {
            if (INSTANCE != null) {
                INSTANCE.close();
                INSTANCE = null;
            }
        }
    }

    private void close() {
        Box box = this.mBox;
        if (box != null) {
            box.logout();
        }
    }

    public static void setAppKeyPair(String str, String str2, String str3) {
        mCustomerAppKey = str;
        mCustomerAppSecret = str2;
        mCustomerRedirectUrl = str3;
    }

    public static String getCustomizedAppKey(@NonNull Context context) {
        if (getInstance().isZoomApp()) {
            return null;
        }
        return mCustomerAppKey;
    }

    @Nullable
    public static String getCustomizedAppRedirectUrl(Context context) {
        if (context == null || getInstance().isZoomApp()) {
            return null;
        }
        return mCustomerRedirectUrl;
    }

    @Nullable
    public static String getCustomizedAppSecret(Context context) {
        if (context == null || getInstance().isZoomApp()) {
            return null;
        }
        return mCustomerAppSecret;
    }

    public Box getBox() {
        if (this.mBox == null) {
            this.mBox = new Box(this);
        }
        return this.mBox;
    }

    public void onLogout(Box box) {
        if (this.mBox == box) {
            this.mBox = null;
        }
    }

    public boolean checkValid(Context context) {
        if (context == null) {
            return false;
        }
        if (isZoomApp()) {
            return true;
        }
        getInstance();
        String customizedAppKey = getCustomizedAppKey(context);
        getInstance();
        return !StringUtil.isEmptyOrNull(customizedAppKey) && !StringUtil.isEmptyOrNull(getCustomizedAppSecret(context));
    }

    public int getmFileIntegrationType() {
        return mFileIntegrationType;
    }
}
