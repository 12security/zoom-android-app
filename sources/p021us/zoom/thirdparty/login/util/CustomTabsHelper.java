package p021us.zoom.thirdparty.login.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* renamed from: us.zoom.thirdparty.login.util.CustomTabsHelper */
public class CustomTabsHelper {
    private static final String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";
    static final String BETA_PACKAGE = "com.chrome.beta";
    static final String DEV_PACKAGE = "com.chrome.dev";
    static final String LOCAL_PACKAGE = "com.google.android.apps.chrome";
    static final String STABLE_PACKAGE = "com.android.chrome";
    private static final String TAG = "CustomTabsHelper";
    private static String sPackageNameToUse;

    private CustomTabsHelper() {
    }

    public static Intent getCustomTabIntentWithNoSession(Context context, Uri uri) {
        String packageNameToUse = getPackageNameToUse(context);
        if (packageNameToUse != null) {
            return CustomTabsIntent.getViewIntentWithNoSession(packageNameToUse, uri);
        }
        return null;
    }

    public static String getPackageNameToUse(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String str = sPackageNameToUse;
        if (str != null) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
                if (applicationInfo != null && applicationInfo.enabled) {
                    return sPackageNameToUse;
                }
            } catch (NameNotFoundException unused) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getApplicationInfo get a NameNotFoundException packageName=");
                sb.append(sPackageNameToUse);
                Log.e(str2, sb.toString());
            }
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.example.com"));
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 0);
        String str3 = resolveActivity != null ? resolveActivity.activityInfo.packageName : null;
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            Intent intent2 = new Intent();
            intent2.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            intent2.setPackage(resolveInfo.activityInfo.packageName);
            if (packageManager.resolveService(intent2, 0) != null) {
                arrayList.add(resolveInfo.activityInfo.packageName);
            }
        }
        if (arrayList.isEmpty()) {
            sPackageNameToUse = null;
        } else if (arrayList.size() == 1) {
            sPackageNameToUse = (String) arrayList.get(0);
        } else if (!TextUtils.isEmpty(str3) && !hasSpecializedHandlerIntents(context, intent) && arrayList.contains(str3)) {
            sPackageNameToUse = str3;
        } else if (arrayList.contains(STABLE_PACKAGE)) {
            sPackageNameToUse = STABLE_PACKAGE;
        } else if (arrayList.contains(BETA_PACKAGE)) {
            sPackageNameToUse = BETA_PACKAGE;
        } else if (arrayList.contains(DEV_PACKAGE)) {
            sPackageNameToUse = DEV_PACKAGE;
        } else if (arrayList.contains(LOCAL_PACKAGE)) {
            sPackageNameToUse = LOCAL_PACKAGE;
        }
        return sPackageNameToUse;
    }

    private static boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
        try {
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 64);
            if (queryIntentActivities != null) {
                if (queryIntentActivities.size() != 0) {
                    for (ResolveInfo resolveInfo : queryIntentActivities) {
                        IntentFilter intentFilter = resolveInfo.filter;
                        if (intentFilter != null) {
                            if (intentFilter.countDataAuthorities() == 0) {
                                continue;
                            } else if (intentFilter.countDataPaths() != 0) {
                                if (resolveInfo.activityInfo != null) {
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            }
            return false;
        } catch (RuntimeException unused) {
            Log.e(TAG, "Runtime exception while getting specialized handlers");
        }
    }

    public static String[] getPackages() {
        return new String[]{"", STABLE_PACKAGE, BETA_PACKAGE, DEV_PACKAGE, LOCAL_PACKAGE};
    }
}
