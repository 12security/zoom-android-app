package p021us.zoom.androidlib.app;

import android.app.AppOpsManager;
import android.app.AppOpsManager.OnOpChangedListener;
import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.app.ZMAdapterOsBugHelper */
public class ZMAdapterOsBugHelper {
    private static final ZMAdapterOsBugHelper ourInstance = new ZMAdapterOsBugHelper();
    /* access modifiers changed from: private */
    public boolean mCanDraw;
    private OnOpChangedListener mOnOpChangedListener = null;

    private ZMAdapterOsBugHelper() {
    }

    public static ZMAdapterOsBugHelper getInstance() {
        return ourInstance;
    }

    public boolean isNeedListenOverlayPermissionChanged() {
        return OsUtil.isAtLeastM() && (VERSION.SDK_INT == 26 || VERSION.SDK_INT == 27);
    }

    @RequiresApi(api = 23)
    public void startListenOverlayPermissionChange(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
        if (appOpsManager != null) {
            this.mCanDraw = Settings.canDrawOverlays(context);
            final String packageName = context.getPackageName();
            if (!StringUtil.isEmptyOrNull(packageName)) {
                this.mOnOpChangedListener = new OnOpChangedListener() {
                    public void onOpChanged(String str, String str2) {
                        if (packageName.equals(str2) && "android:system_alert_window".equals(str)) {
                            ZMAdapterOsBugHelper zMAdapterOsBugHelper = ZMAdapterOsBugHelper.this;
                            zMAdapterOsBugHelper.mCanDraw = !zMAdapterOsBugHelper.mCanDraw;
                        }
                    }
                };
                appOpsManager.startWatchingMode("android:system_alert_window", null, this.mOnOpChangedListener);
            }
        }
    }

    @RequiresApi(api = 23)
    public void stopListenOverlayPermissionChange(@NonNull Context context) {
        if (this.mOnOpChangedListener != null) {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
            if (appOpsManager != null) {
                appOpsManager.stopWatchingMode(this.mOnOpChangedListener);
                this.mOnOpChangedListener = null;
            }
        }
    }

    public boolean ismCanDraw() {
        return this.mCanDraw;
    }
}
