package com.zipow.videobox.util;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings.Secure;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppContext;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.MinVersionForceUpdateActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.fragment.NewVersionDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.upgrade.UpgradeMgr;
import java.util.Locale;
import org.apache.http.HttpHost;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class UpgradeUtil {
    private static final String TAG = "UpgradeUtil";

    @NonNull
    private static String getWebServer() {
        String zoomDomain = PTApp.getInstance().getZoomDomain();
        if (StringUtil.isEmptyOrNull(zoomDomain)) {
            try {
                zoomDomain = new AppContext(AppContext.PREFER_NAME_CHAT).queryWithKey(ConfigReader.KEY_WEBSERVER, AppContext.APP_NAME_CHAT);
            } catch (Error | Exception unused) {
            }
            if (StringUtil.isEmptyOrNull(zoomDomain)) {
                zoomDomain = ZMDomainUtil.getDefaultWebDomain();
            }
        }
        return zoomDomain.replaceFirst("https", HttpHost.DEFAULT_SCHEME_NAME);
    }

    public static void upgrade(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            if (VideoBoxApplication.getNonNullInstance().isConfApp()) {
                IMActivity.show(zMActivity, true, IMActivity.ACTION_SHOW_AND_UPGRADE);
                return;
            }
            if (!needUpdateLocal(zMActivity)) {
                upgradeByUrl(zMActivity);
            } else if (VideoBoxApplication.getInstance().isPTApp()) {
                String zoomAPKDownloadUrl = UpgradeMgr.getZoomAPKDownloadUrl();
                if (UpgradeMgr.getInstance(zMActivity).installZoomByAPK(zMActivity, zoomAPKDownloadUrl)) {
                    zMActivity.getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                        public void run(IUIElement iUIElement) {
                            UpgradeUtil.showDownloading((ZMActivity) iUIElement);
                        }
                    });
                } else if (!StringUtil.isEmptyOrNull(zoomAPKDownloadUrl) || !(zMActivity instanceof MinVersionForceUpdateActivity)) {
                    UIUtil.openURL(zMActivity, zoomAPKDownloadUrl);
                } else {
                    upgradeByUrl(zMActivity);
                    zMActivity.finish();
                }
            }
        }
    }

    public static void upgradeByUrl(@Nullable ZMActivity zMActivity) {
        upgradeByUrl(zMActivity, false);
    }

    public static void upgradeByUrl(@Nullable ZMActivity zMActivity, boolean z) {
        if (zMActivity != null) {
            if (z) {
                ZMDomainUtil.initMainDomain(AndroidAppUtil.isLocaleCN(VideoBoxApplication.getNonNullInstance()) ? 1 : 0);
            }
            UIUtil.openURL(zMActivity, zMActivity.getResources().getString(C4558R.string.zm_url_update, new Object[]{getWebServer()}));
        }
    }

    /* access modifiers changed from: private */
    public static void showDownloading(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            NewVersionDialog.showDownloading(zMActivity, null);
        }
    }

    private static boolean needUpdateLocal(@NonNull Context context) {
        boolean z;
        boolean z2;
        if (!ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_enable_self_ugrade, false)) {
            return false;
        }
        boolean z3 = true;
        try {
            z = Secure.getInt(context.getContentResolver(), "install_non_market_apps") == 1;
        } catch (Exception unused) {
            z = true;
        }
        try {
            z2 = Environment.getExternalStorageState().equals("mounted");
        } catch (Exception unused2) {
            z2 = false;
        }
        if (ZMBuildConfig.BUILD_TARGET == 0) {
            if (!z || !z2 || !Locale.CHINA.getCountry().equalsIgnoreCase(CountryCodeUtil.getIsoCountryCode(context))) {
                z3 = false;
            }
            return z3;
        }
        if (!z || !z2) {
            z3 = false;
        }
        return z3;
    }
}
