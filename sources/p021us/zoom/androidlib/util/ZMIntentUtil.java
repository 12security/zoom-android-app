package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import com.zipow.videobox.util.ZMDomainUtil;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.utils.ZmAppUtils;

/* renamed from: us.zoom.androidlib.util.ZMIntentUtil */
public class ZMIntentUtil {
    private static final String TAG = "us.zoom.androidlib.util.ZMIntentUtil";

    @RequiresPermission("android.permission.CALL_PHONE")
    public static void callNumber(@NonNull ZMActivity zMActivity, String str) {
        Uri uri;
        if (Build.MANUFACTURER == null || !Build.MANUFACTURER.toLowerCase().contains("moto")) {
            StringBuilder sb = new StringBuilder();
            sb.append("tel:");
            sb.append(URLEncoder.encode(str));
            uri = Uri.parse(sb.toString());
        } else {
            String replaceAll = str.replaceAll("#", URLEncoder.encode("#").replaceAll(PreferencesConstants.COOKIE_DELIMITER, URLEncoder.encode(PreferencesConstants.COOKIE_DELIMITER)));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("tel:");
            sb2.append(replaceAll);
            uri = Uri.parse(sb2.toString());
        }
        try {
            zMActivity.startActivity(new Intent("android.intent.action.CALL", uri));
        } catch (Exception unused) {
        }
    }

    public static List<ResolveInfo> queryBrowserActivities(Context context) {
        List<ResolveInfo> list;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setData(Uri.parse(ZMDomainUtil.ZM_URL_HTTP));
        if (OsUtil.isAtLeastM()) {
            list = packageManager.queryIntentActivities(intent, 131072);
        } else {
            list = packageManager.queryIntentActivities(intent, 65536);
        }
        return list == null ? new ArrayList() : list;
    }

    @NonNull
    public static List<ResolveInfo> queryAllActivitiesForIntent(@NonNull Context context, @NonNull Intent intent) {
        List<ResolveInfo> list;
        PackageManager packageManager = context.getPackageManager();
        try {
            list = OsUtil.isAtLeastM() ? packageManager.queryIntentActivities(intent, 131072) : packageManager.queryIntentActivities(intent, 65536);
        } catch (Exception unused) {
            list = null;
        }
        if (list == null) {
            ZMLog.m278d(TAG, "queryAllActivitiesForIntent list is null", new Object[0]);
            list = new ArrayList<>();
        }
        for (ResolveInfo resolveInfo : list) {
        }
        return list;
    }

    public static boolean hasAppStore(Context context) {
        return queryAllActivitiesForIntent(context, new Intent("android.intent.action.VIEW", Uri.parse("market://details"))).size() > 0;
    }

    public static boolean isInstalled(@NonNull Context context, @NonNull Uri uri) {
        return new Intent("android.intent.action.VIEW", uri).resolveActivity(context.getPackageManager()) != null;
    }

    public static boolean isSupportShareScreen(@NonNull Context context) {
        if (!OsUtil.isAtLeastL()) {
            return false;
        }
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService("media_projection");
        Intent intent = null;
        if (mediaProjectionManager != null) {
            intent = mediaProjectionManager.createScreenCaptureIntent();
        }
        if (intent == null || !AndroidAppUtil.hasActivityForIntent(context, intent)) {
            return false;
        }
        if (OsUtil.isAtLeastN() && !Settings.canDrawOverlays(context)) {
            StringBuilder sb = new StringBuilder();
            sb.append("package:");
            sb.append(ZmAppUtils.getHostPackageName(context));
            if (!AndroidAppUtil.hasActivityForIntent(context, new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())))) {
                return false;
            }
        }
        return true;
    }
}
