package com.zipow.videobox.util;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class UIMgr {
    private static boolean bDriverModeEnabled = true;
    private static boolean bGlobalDriverModeEnabled = true;
    private static boolean bShowWatermarkOnVideo = false;
    @Nullable
    private static String uiMode;

    public static void initialize(Context context) {
        bShowWatermarkOnVideo = ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_show_water_mark_on_video, bShowWatermarkOnVideo);
    }

    public static boolean getShowWatermarkOnVideo() {
        return bShowWatermarkOnVideo;
    }

    public static boolean isLargeMode(@Nullable Context context) {
        if (context == null) {
            context = VideoBoxApplication.getInstance();
        }
        if (uiMode == null) {
            uiMode = PreferenceUtil.readStringValue(ConfigReader.KEY_UI_MODE, "");
            if (uiMode == null) {
                uiMode = "auto";
            }
        }
        if (uiMode.equals("normal")) {
            return false;
        }
        if (uiMode.equals("large")) {
            return true;
        }
        return ResourcesUtil.getBoolean(context, C4558R.bool.zm_is_large_mode, false);
    }

    public static boolean isDualPaneSupportedInPortraitMode(Context context) {
        return UIUtil.getDisplayMinWidthInDip(context) >= 750.0f;
    }

    public static void setDriverModeEnabled(boolean z) {
        bDriverModeEnabled = z;
    }

    public static void setGlobalDriverModeEnabled(boolean z) {
        bGlobalDriverModeEnabled = z;
    }

    public static boolean isDriverModeEnabled() {
        return bDriverModeEnabled && bGlobalDriverModeEnabled;
    }

    public static boolean isMyNotes(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return false;
        }
        if (!TextUtils.isEmpty(str) && TextUtils.equals(str, myself.getJid())) {
            z = true;
        }
        return z;
    }
}
