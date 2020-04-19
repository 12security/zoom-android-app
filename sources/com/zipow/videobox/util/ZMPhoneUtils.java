package com.zipow.videobox.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.RemoteException;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxUser;
import com.zipow.videobox.CallingActivity;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CloudPBX;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.ZMPhoneNumberHelper;
import com.zipow.videobox.view.sip.SipInCallActivity;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMPhoneUtils {
    private static final String TAG = "ZMPhoneUtils";

    public static boolean isPBXFeatureOptionChanged(List<CmmPBXFeatureOptionBit> list, long j) {
        boolean z = false;
        if (list == null || list.size() == 0) {
            return false;
        }
        Iterator it = list.iterator();
        while (true) {
            if (it.hasNext()) {
                if (j == ((CmmPBXFeatureOptionBit) it.next()).getBit()) {
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        return z;
    }

    public static CmmPBXFeatureOptionBit getPBXFeatureOptionBit(List<CmmPBXFeatureOptionBit> list, long j) {
        CmmPBXFeatureOptionBit cmmPBXFeatureOptionBit = null;
        if (list == null || list.size() == 0) {
            return null;
        }
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            CmmPBXFeatureOptionBit cmmPBXFeatureOptionBit2 = (CmmPBXFeatureOptionBit) it.next();
            if (j == cmmPBXFeatureOptionBit2.getBit()) {
                cmmPBXFeatureOptionBit = cmmPBXFeatureOptionBit2;
                break;
            }
        }
        return cmmPBXFeatureOptionBit;
    }

    @Nullable
    public static String dialNumberFilter(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        return zMPhoneNumberHelper != null ? zMPhoneNumberHelper.formatCalloutPeerUriVanityNumber(str) : str;
    }

    public static void callSip(@NonNull String str, @Nullable String str2) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        CmmSIPCallManager instance2 = CmmSIPCallManager.getInstance();
        if (instance2.checkNetwork(instance) && instance2.checkIsPbxActive(instance)) {
            instance2.callPeer(str, str2);
        }
    }

    public static String getZoomPhonePreferenceName() {
        return PreferenceUtil.getPreferenceName(PreferenceUtil.ZOOM_PHONE_PREFERENCE_NAME);
    }

    public static void saveBooleanValue(@NonNull String str, boolean z) {
        PreferenceUtil.saveBooleanValue(getZoomPhonePreferenceName(), str, z);
    }

    public static boolean readBooleanValue(@Nullable String str, boolean z) {
        return PreferenceUtil.readBooleanValue(getZoomPhonePreferenceName(), str, z);
    }

    public static void saveIntValue(@NonNull String str, int i) {
        PreferenceUtil.saveIntValue(getZoomPhonePreferenceName(), str, i);
    }

    public static int readIntValue(@Nullable String str, int i) {
        return PreferenceUtil.readIntValue(getZoomPhonePreferenceName(), str, i);
    }

    public static void saveStringValue(@NonNull String str, String str2) {
        PreferenceUtil.saveStringValue(getZoomPhonePreferenceName(), str, str2);
    }

    public static String readStringValue(@Nullable String str, String str2) {
        return PreferenceUtil.readStringValue(getZoomPhonePreferenceName(), str, str2);
    }

    public static void upgradeZoomPhonePreference(@Nullable Context context) {
        if (context != null) {
            Editor edit = context.getSharedPreferences(getZoomPhonePreferenceName(), 0).edit();
            int readIntValue = PreferenceUtil.readIntValue(PreferenceUtil.PBX_FRAGMENT_INDEX, 0);
            if (readIntValue != 0) {
                edit.putInt(PreferenceUtil.PBX_FRAGMENT_INDEX, readIntValue);
            }
            if (!PreferenceUtil.readBooleanValue(PreferenceUtil.PBX_SLA_FIRST_USE, true)) {
                edit.putBoolean(PreferenceUtil.PBX_SLA_FIRST_USE, false);
            }
            if (!PreferenceUtil.readBooleanValue(PreferenceUtil.PBX_FIRST_IGNORE, true)) {
                edit.putBoolean(PreferenceUtil.PBX_FIRST_IGNORE, false);
            }
            if (PreferenceUtil.readBooleanValue(PreferenceUtil.PBX_AD_HOC_RECORDING, false)) {
                edit.putBoolean(PreferenceUtil.PBX_AD_HOC_RECORDING, true);
            }
            String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.PBX_SIP_SWITCH_TO_CARRIER_NUMBER, null);
            if (!StringUtil.isEmptyOrNull(readStringValue)) {
                edit.putString(PreferenceUtil.PBX_SIP_SWITCH_TO_CARRIER_NUMBER, readStringValue);
            }
            edit.commit();
            PreferenceUtil.clearKeys(PreferenceUtil.PBX_FRAGMENT_INDEX, PreferenceUtil.PBX_SLA_FIRST_USE, PreferenceUtil.PBX_FIRST_IGNORE, PreferenceUtil.PBX_SIP_SWITCH_TO_CARRIER_NUMBER, PreferenceUtil.PBX_AD_HOC_RECORDING);
        }
    }

    public static boolean isInSipInCallUI() {
        return isUIInFront(SipInCallActivity.class.getName());
    }

    public static boolean isInConfCallingUI() {
        return isUIInFront(CallingActivity.class.getName());
    }

    private static boolean isUIInFront(String str) {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity != null && frontActivity.getClass().getName().equals(str)) {
            return frontActivity.isActive();
        }
        return false;
    }

    public static boolean isInMeetingUI() {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if ((frontActivity instanceof CallingActivity) && frontActivity.isActive()) {
            return true;
        }
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService == null) {
            return false;
        }
        try {
            return confService.isConfAppAtFront();
        } catch (RemoteException unused) {
            return false;
        }
    }

    public static boolean isValidPhoneNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper != null) {
            return zMPhoneNumberHelper.isValidPhoneNumber(str);
        }
        return TextUtils.isDigitsOnly(str);
    }

    public static boolean isValidPhoneNumbers(List<String> list) {
        if (CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        for (String isValidPhoneNumber : list) {
            if (!isValidPhoneNumber(isValidPhoneNumber)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isExtensionNumber(String str) {
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper != null) {
            return zMPhoneNumberHelper.isExtension(str);
        }
        boolean z = false;
        if (str.startsWith("+")) {
            return false;
        }
        try {
            if (Long.parseLong(str) >= 10) {
                z = true;
            }
            return z;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public static boolean isE164Format(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper != null) {
            return zMPhoneNumberHelper.isE164Format(str);
        }
        if (str.startsWith("+") && str.length() > 6) {
            z = true;
        }
        return z;
    }

    public static String formatPhoneNumberAsE164(String str) {
        if (ZMPhoneNumberHelper.isInvalidPhoneNumberLength(str)) {
            return str;
        }
        CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
        if (cloudPBXInfo == null) {
            return str;
        }
        return formatPhoneNumberAsE164(str, cloudPBXInfo.getCountryCode(), cloudPBXInfo.getAreaCode());
    }

    public static String formatPhoneNumberAsE164(String str, String str2, String str3) {
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(str2);
            sb.append(str3);
            sb.append(str);
            return sb.toString();
        }
        String formatPhoneNumberAsE164 = zMPhoneNumberHelper.formatPhoneNumberAsE164(str, str2, str3);
        return TextUtils.isEmpty(formatPhoneNumberAsE164) ? str : formatPhoneNumberAsE164;
    }

    public static String formatPhoneNumber(@Nullable String str) {
        return formatPhoneNumber(str, false);
    }

    public static String formatPhoneNumber(@Nullable String str, boolean z) {
        CloudPBX cloudPBXInfo = CmmSIPCallManager.getInstance().getCloudPBXInfo();
        if (cloudPBXInfo != null) {
            return formatPhoneNumber(str, cloudPBXInfo.getCountryCode(), cloudPBXInfo.getAreaCode(), z);
        }
        if (str == null) {
            str = "";
        }
        return str;
    }

    public static String formatPhoneNumber(@Nullable String str, String str2, String str3, boolean z) {
        if (str == null) {
            return "";
        }
        if (ZMPhoneNumberHelper.isInvalidPhoneNumberLength(str)) {
            return str;
        }
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper == null) {
            return str;
        }
        String formatPhoneNumber = zMPhoneNumberHelper.formatPhoneNumber(str, str2, str3, z);
        return TextUtils.isEmpty(formatPhoneNumber) ? str : formatPhoneNumber;
    }

    public static int getSipNumberType(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if ((str.startsWith("+") || str.length() > 6) && !isExtensionNumber(str)) {
            return 2;
        }
        return 1;
    }

    public static boolean isNumberMatched(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        if (str.equals(str2)) {
            return true;
        }
        ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
        if (zMPhoneNumberHelper != null) {
            return zMPhoneNumberHelper.isNumberMatched(str, str2);
        }
        return false;
    }

    public static void addNumberToPhoneContact(Context context, String str, boolean z) {
        if (!StringUtil.isEmptyOrNull(str) && context != null) {
            Intent intent = new Intent();
            intent.putExtra(BoxUser.FIELD_PHONE, str);
            intent.putExtra("phone_type", 2);
            if (z) {
                intent.setAction("android.intent.action.INSERT_OR_EDIT");
                intent.setType("vnd.android.cursor.item/contact");
                ZoomLogEventTracking.eventTrackAddToContactsList();
            } else {
                intent.setAction("android.intent.action.INSERT");
                intent.setType("vnd.android.cursor.dir/raw_contact");
            }
            try {
                ActivityStartHelper.startActivityForeground(context, intent);
            } catch (Exception unused) {
            }
        }
    }
}
