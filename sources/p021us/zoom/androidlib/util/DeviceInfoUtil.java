package p021us.zoom.androidlib.util;

import android.os.Build;
import java.util.HashMap;

/* renamed from: us.zoom.androidlib.util.DeviceInfoUtil */
public class DeviceInfoUtil {
    private static final String TAG = "DeviceInfoUtil";
    private static HashMap<String, Boolean> android_o_front_service_whitelist = new HashMap<>();
    private static HashMap<String, Boolean> ar_headset_whitelist = new HashMap<>();
    private static HashMap<String, Boolean> auth_null_state_whitelist = new HashMap<>();
    private static HashMap<String, Boolean> pop_up_camera_whitelist = new HashMap<>();
    private static HashMap<String, Boolean> ringer_mode_whitelist = new HashMap<>();

    public static String getDeviceInfo() {
        return "";
    }

    static {
        ringer_mode_whitelist.put("google", Boolean.TRUE);
        ringer_mode_whitelist.put("oneplus", Boolean.TRUE);
        auth_null_state_whitelist.put("samsung j6primelte".toLowerCase(), Boolean.TRUE);
        auth_null_state_whitelist.put("samsung j4primelte".toLowerCase(), Boolean.TRUE);
        auth_null_state_whitelist.put("samsung a10".toLowerCase(), Boolean.TRUE);
        ar_headset_whitelist.put("RealWear inc. T1100G".toLowerCase(), Boolean.TRUE);
        android_o_front_service_whitelist.put("google", Boolean.TRUE);
        android_o_front_service_whitelist.put("huawei", Boolean.TRUE);
        android_o_front_service_whitelist.put("oneplus", Boolean.TRUE);
        pop_up_camera_whitelist.put("vivo vivo NEX A".toLowerCase(), Boolean.TRUE);
        pop_up_camera_whitelist.put("OnePlus GM1910".toLowerCase(), Boolean.TRUE);
    }

    public static boolean isInRingerModeWhiteList() {
        Boolean bool = (Boolean) ringer_mode_whitelist.get(StringUtil.safeString(Build.MANUFACTURER).trim().toLowerCase());
        return bool != null && bool.booleanValue();
    }

    public static boolean isSamsungSpecificDevice() {
        String trim = StringUtil.safeString(Build.MANUFACTURER).trim();
        String trim2 = StringUtil.safeString(Build.DEVICE).trim();
        StringBuilder sb = new StringBuilder();
        sb.append(trim);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(trim2);
        Boolean bool = (Boolean) auth_null_state_whitelist.get(sb.toString().trim().toLowerCase());
        return bool != null && bool.booleanValue();
    }

    public static boolean isARHeadset() {
        String trim = StringUtil.safeString(Build.MANUFACTURER).trim();
        String trim2 = StringUtil.safeString(Build.MODEL).trim();
        StringBuilder sb = new StringBuilder();
        sb.append(trim);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(trim2);
        Boolean bool = (Boolean) ar_headset_whitelist.get(sb.toString().trim().toLowerCase());
        return bool != null && bool.booleanValue();
    }

    public static boolean isInAndroidOFrontServiceWhiteList() {
        Boolean bool = (Boolean) android_o_front_service_whitelist.get(StringUtil.safeString(Build.MANUFACTURER).trim().toLowerCase());
        return bool != null && bool.booleanValue();
    }

    public static boolean isInPopUpCameraWhiteList() {
        String trim = StringUtil.safeString(Build.MANUFACTURER).trim();
        String trim2 = StringUtil.safeString(Build.MODEL).trim();
        StringBuilder sb = new StringBuilder();
        sb.append(trim);
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(trim2);
        Boolean bool = (Boolean) pop_up_camera_whitelist.get(sb.toString().trim().toLowerCase());
        return bool != null && bool.booleanValue();
    }
}
