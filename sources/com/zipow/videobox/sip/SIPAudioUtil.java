package com.zipow.videobox.sip;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.util.OsUtil;

public class SIPAudioUtil {
    private static final int SYSTEM_SETTING_ON = 1;
    private static final String TAG = "SIPAudioUtil";
    private static final String VIBRATE_WHEN_RINGING_MIUI = "vibrate_in_normal";
    private static final String VIBRATE_WHEN_RINGING_SMARTISAN = "telephony_vibration_enabled";

    public static boolean willRing(@NonNull Context context) {
        int i;
        boolean z = false;
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            if (audioManager == null) {
                return false;
            }
            i = audioManager.getRingerMode();
            if (i == 2) {
                z = true;
            }
            return z;
        } catch (Exception unused) {
            i = 2;
        }
    }

    public static boolean willVibrate(@NonNull Context context) {
        int i;
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            if (audioManager == null) {
                return false;
            }
            i = audioManager.getRingerMode();
            boolean z = true;
            if (!(i == 1 || (i == 2 && (1 == System.getInt(context.getContentResolver(), "vibrate_when_ringing", 0) || 1 == System.getInt(context.getContentResolver(), VIBRATE_WHEN_RINGING_MIUI, 0) || (OsUtil.isAtLeastJB_MR1() && 1 == Global.getInt(context.getContentResolver(), VIBRATE_WHEN_RINGING_SMARTISAN, 0)))))) {
                z = false;
            }
            return z;
        } catch (Exception unused) {
            i = 2;
        }
    }
}
