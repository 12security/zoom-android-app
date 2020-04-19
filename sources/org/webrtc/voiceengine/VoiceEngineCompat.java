package org.webrtc.voiceengine;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.UIUtil;

public class VoiceEngineCompat {
    public static final int AUDIO_DEVICE_API_TYPE_JNI = 1;
    public static final int AUDIO_DEVICE_API_TYPE_OPENSLES = 0;
    private static final String[] SAMSUNG_BluetoothScoDisabledDevices = {"GT-P6200", "GT-P6210", "GT-P6800", "GT-P7500", "GT-P7510", "GT-P7100", "GT-P7110", "GT-P739", "GT-P7300", "GT-P7310", "GT-P1000", "GT-P1010", "GT-I9003"};
    private static final String[] SAMSUNG_ChipAECDisabledNonePhoneDevices = {"GT-P6200", "GT-P6210", "GT-P6800", "GT-P7500", "GT-P7510", "GT-P7100", "GT-P7110", "GT-P739", "GT-P7300", "GT-P7310", "GT-P1000", "GT-P1010"};
    private static boolean g_bBlackListBluetoothSco = false;

    public static boolean isChipAECSupported(@NonNull Context context) {
        if (!Build.BRAND.equals("samsung") || HardwareUtil.getCPUKernalNumbers() < 2) {
            return "Amazon".equals(Build.MANUFACTURER);
        }
        if (Build.MODEL.equals("Galaxy Nexus")) {
            return false;
        }
        if (!isFeatureTelephonySupported(context)) {
            int i = 0;
            while (true) {
                String[] strArr = SAMSUNG_ChipAECDisabledNonePhoneDevices;
                if (i >= strArr.length) {
                    break;
                } else if (strArr[i].equals(Build.MODEL)) {
                    return false;
                } else {
                    i++;
                }
            }
        }
        return true;
    }

    public static void blacklistBluetoothSco(boolean z) {
        g_bBlackListBluetoothSco = z;
    }

    public static boolean isBluetoothScoSupported() {
        boolean z = false;
        if (g_bBlackListBluetoothSco) {
            return false;
        }
        if ("samsung".equals(Build.BRAND)) {
            int i = 0;
            while (true) {
                String[] strArr = SAMSUNG_BluetoothScoDisabledDevices;
                if (i >= strArr.length) {
                    return isBluetoothScoAvailableOffCall();
                }
                if (strArr[i].equals(Build.MODEL)) {
                    return false;
                }
                i++;
            }
        } else if (("realtek".equals(Build.MANUFACTURER) && "phoenix".equals(Build.MODEL)) || selectAudioDeviceAPIType(VideoBoxApplication.getInstance()) != 1) {
            return false;
        } else {
            int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
            if (selectedPlayerStreamType < 0) {
                return isBluetoothScoAvailableOffCall();
            }
            if (selectedPlayerStreamType == 0 && isBluetoothScoAvailableOffCall()) {
                z = true;
            }
            return z;
        }
    }

    private static boolean isBluetoothScoAvailableOffCall() {
        AudioManager audioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        if (audioManager == null) {
            return false;
        }
        return audioManager.isBluetoothScoAvailableOffCall();
    }

    public static boolean isFeatureTelephonySupported(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.telephony");
    }

    public static boolean isTablet(Context context) {
        return UIUtil.getDisplayMinWidthInDip(context) > 500.0f;
    }

    public static boolean isPlayerConfigurationNativeAPIDisabled(@NonNull Context context) {
        if ("GT-I9003".equalsIgnoreCase(Build.MODEL)) {
            return true;
        }
        if (!"Nexus 7".equals(Build.MODEL) && ("Meizu".equals(Build.MANUFACTURER) || !isChipAECSupported(context))) {
            return true;
        }
        return false;
    }

    public static boolean isRecorderConfigurationNativeAPIDisabled(@NonNull Context context) {
        if ("Amazon".equals(Build.MANUFACTURER)) {
            return true ^ isFeatureTelephonySupported(context);
        }
        if (!"Nexus 7".equals(Build.MODEL) && !isChipAECSupported(context)) {
            return true;
        }
        return false;
    }

    public static int selectAudioDeviceAPIType(@Nullable Context context) {
        String readStringValue = PreferenceUtil.readStringValue(ConfigReader.KEY_AUDIO_API_TYPE, null);
        if ("java".equals(readStringValue)) {
            return 1;
        }
        if ("OpenSLES".equals(readStringValue) || "Amazon".equals(Build.MANUFACTURER) || "Meizu".equals(Build.MANUFACTURER)) {
            return 0;
        }
        if ("Motorola".equalsIgnoreCase(Build.MANUFACTURER) && Build.MODEL != null && Build.MODEL.startsWith("XT")) {
            return 0;
        }
        if ("Dell".equalsIgnoreCase(Build.MANUFACTURER) && Build.MODEL != null && Build.MODEL.startsWith("Venue7")) {
            return 0;
        }
        if ("Acer".equalsIgnoreCase(Build.MANUFACTURER) && Build.MODEL != null && Build.MODEL.equals("A1-830")) {
            return 0;
        }
        if (("Lenovo".equalsIgnoreCase(Build.MANUFACTURER) && Build.MODEL != null && Build.MODEL.equals("Lenovo TB-X103F")) || context == null) {
            return 0;
        }
        if (isPlayerConfigurationNativeAPIDisabled(context) || isRecorderConfigurationNativeAPIDisabled(context)) {
            return 1;
        }
        return 0;
    }

    public static boolean isPlayerCommunicationModeAvailable() {
        if ("OpenSLES".equals(PreferenceUtil.readStringValue(ConfigReader.KEY_AUDIO_API_TYPE, null))) {
            return !isPlayerConfigurationNativeAPIDisabled(VideoBoxApplication.getInstance());
        }
        if ("Amazon".equals(Build.MANUFACTURER)) {
            return !isPlayerConfigurationNativeAPIDisabled(VideoBoxApplication.getInstance());
        }
        if ("Meizu".equals(Build.MANUFACTURER)) {
            return !isPlayerConfigurationNativeAPIDisabled(VideoBoxApplication.getInstance());
        }
        return true;
    }
}
