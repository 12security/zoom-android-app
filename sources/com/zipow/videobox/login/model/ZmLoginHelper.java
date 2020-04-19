package com.zipow.videobox.login.model;

import android.content.Context;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.common.p008pt.ZMNativeSsoCloudInfo;
import com.zipow.videobox.login.ZMLoginForRealNameDialog;
import com.zipow.videobox.ptapp.AutoLogoffChecker.AutoLogoffInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.WebLaunchedToLoginParam;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.SBWebServiceErrorCode;
import com.zipow.videobox.ptapp.ZoomProductHelper;
import com.zipow.videobox.util.ZMDomainUtil;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZmRegexUtils;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class ZmLoginHelper {
    private static String TAG = "com.zipow.videobox.login.model.ZmLoginHelper";

    public static boolean isExcludeModifyPasswd(int i) {
        return i == 11 || i == 22 || i == 21 || i == 23;
    }

    public static boolean isNeedBindPhone(int i) {
        return i == 22 || i == 21 || i == 23;
    }

    public static boolean isNormalTypeLogin(int i) {
        return i == 100 || i == 0 || i == 2 || i == 101 || i == 11 || i == 22 || i == 21 || i == 23;
    }

    public static boolean isTypeSupportFavoriteContacts(int i) {
        switch (i) {
            case 100:
            case 101:
                return true;
            default:
                return false;
        }
    }

    public static boolean isTypeSupportIM(int i) {
        return false;
    }

    public static boolean isTypeSupportMyMeetings(int i) {
        if (!(i == 0 || i == 2 || i == 11 || i == 98)) {
            switch (i) {
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    switch (i) {
                        case 100:
                        case 101:
                            break;
                        default:
                            return false;
                    }
            }
        }
        return true;
    }

    public static boolean isTypeSupportMyProfileWebPage(int i) {
        if (!(i == 0 || i == 2 || i == 11)) {
            switch (i) {
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    switch (i) {
                        case 100:
                        case 101:
                            break;
                        default:
                            return false;
                    }
            }
        }
        return true;
    }

    public static boolean isUseZoomLogin(int i) {
        return i == 100 || i == 11 || i == 22 || i == 21 || i == 23;
    }

    public static int loginZoom(String str, byte[] bArr, boolean z) {
        if (ZmRegexUtils.isValidForRegex(ZmRegexUtils.CHINA_MOBILE_NUMBER, str)) {
            return PTApp.getInstance().loginWithPhonePasswd(CountryCodeUtil.CHINA_COUNTRY_CODE, str, bArr, z);
        }
        return PTApp.getInstance().loginZoom(str, bArr, z);
    }

    public static boolean isChinaUserForInternationalSignIn(int i) {
        boolean z = true;
        if (ZMBuildConfig.BUILD_TARGET != 0 || i == 1 || i == 2) {
            z = false;
        }
        return z ? AndroidAppUtil.isChinaUser(VideoBoxApplication.getNonNullInstance()) : z;
    }

    public static boolean isEnableChinaThirdpartyLogin() {
        return VideoBoxApplication.getNonNullInstance().getResources().getBoolean(C4558R.bool.zm_vendor_config_is_china);
    }

    @Nullable
    public static byte[] getEditTextByteArrayWithClearance(@Nullable EditText editText) {
        if (editText == null || editText.length() == 0) {
            return null;
        }
        char[] cArr = new char[editText.length()];
        editText.getText().getChars(0, editText.length(), cArr, 0);
        return StringUtil.charArray2UTF8ByteArrayWithClearance(cArr);
    }

    public static void showIMActivityForContext(@Nullable Context context, boolean z) {
        ZMActivity zMActivity = (ZMActivity) context;
        if (zMActivity != null) {
            showIMActivity(zMActivity, z);
        }
    }

    private static void showIMActivity(@NonNull ZMActivity zMActivity, boolean z) {
        ZMLoginForRealNameDialog.dismiss(zMActivity);
        if (z) {
            IMActivity.showWithPasswd(zMActivity);
        } else {
            IMActivity.show(zMActivity);
        }
        zMActivity.finish();
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
    }

    public static int getLoginWebErrorMessage(int i) {
        if (i == 0) {
            return C4558R.string.zm_alert_connect_facebook_failed_msg;
        }
        if (i == 2) {
            return C4558R.string.zm_alert_connect_google_failed_msg;
        }
        if (i != 11) {
            switch (i) {
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    switch (i) {
                        case 100:
                        case 101:
                            break;
                        default:
                            return 0;
                    }
            }
        }
        return C4558R.string.zm_alert_connect_zoomus_failed_msg;
    }

    public static String getLoginErrorMessage(@NonNull Context context, long j, boolean z) {
        int i = (int) j;
        if (i == 1006) {
            return context.getResources().getString(C4558R.string.zm_alert_auth_token_failed_msg);
        }
        if (i == 1019) {
            return context.getResources().getString(C4558R.string.zm_alert_account_locked);
        }
        if (i == 1041) {
            return context.getResources().getString(C4558R.string.zm_input_illegal_sign_in_msg_148333);
        }
        if (i == 2029) {
            return context.getResources().getString(C4558R.string.zm_alert_disable_signed_in_142165);
        }
        switch (i) {
            case 1001:
            case 1002:
                if (z) {
                    return context.getResources().getString(C4558R.string.zm_alert_auth_zoom_phone_failed_msg_137212);
                }
                return context.getResources().getString(C4558R.string.zm_alert_auth_zoom_failed_msg);
            case 1003:
                return context.getResources().getString(C4558R.string.zm_alert_account_inactive_or_locked_126436);
            default:
                switch (i) {
                    case SBWebServiceErrorCode.SB_ERROR_GOOGLE_DISABLED /*2025*/:
                    case SBWebServiceErrorCode.SB_ERROR_FACEBOOK_DISABLED /*2026*/:
                        return context.getResources().getString(C4558R.string.zm_alert_login_disable_19086);
                    default:
                        return context.getResources().getString(C4558R.string.zm_alert_auth_error_code_msg, new Object[]{Long.valueOf(j)});
                }
        }
    }

    public static String getShowEmail(int i) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return "";
        }
        if (i == 23 || i == 21 || i == 22) {
            return StringUtil.safeString(currentUserProfile.getOauthNickname());
        }
        return StringUtil.safeString(currentUserProfile.getEmail());
    }

    public static void setSSODomain(String str, int i) {
        ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
        if (zoomProductHelper != null) {
            zoomProductHelper.setCurrentVendor(i);
            ZMNativeSsoCloudInfo sSOCloudInfo = PTApp.getInstance().getSSOCloudInfo();
            if (sSOCloudInfo == null || i != sSOCloudInfo.getmSsoCloud()) {
                PTApp.getInstance().setSSOURL(getSsoUrl(str, true, i), i);
            }
        }
    }

    public static String getSsoUrl(String str, boolean z, int i) {
        if (str.trim().length() == 0 && !z) {
            return "";
        }
        if (str.startsWith("https://")) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(getRealZmUrlSSOCloud(i));
            return sb.toString();
        } else if (str.startsWith(ZMDomainUtil.ZM_URL_HTTP)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://");
            sb2.append(str.substring(7));
            sb2.append(getRealZmUrlSSOCloud(i));
            return sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("https://");
            sb3.append(str);
            sb3.append(getRealZmUrlSSOCloud(i));
            return sb3.toString();
        }
    }

    @NonNull
    public static String getRealZmUrlSSOCloud(int i) {
        ZMNativeSsoCloudInfo sSOCloudInfo = PTApp.getInstance().getSSOCloudInfo();
        if (i == 2) {
            return ZMDomainUtil.getPostFixForGov();
        }
        if (sSOCloudInfo == null || i != sSOCloudInfo.getmSsoCloud()) {
            return ZMDomainUtil.getPostFixForVendor(i);
        }
        String str = sSOCloudInfo.getmPost_fix();
        return StringUtil.isEmptyOrNull(str) ? ZMDomainUtil.getPostFixForVendor(i) : str;
    }

    public static int getZoom3rdPartyVendor() {
        ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
        if (zoomProductHelper != null) {
            return zoomProductHelper.getCurrentVendor();
        }
        return 0;
    }

    public static int getSelectVendorByDomain(String str) {
        return str.equals(ZMDomainUtil.getPostFixForGov()) ? 2 : 0;
    }

    public static void goLoginActivity(WebLaunchedToLoginParam webLaunchedToLoginParam) {
        AutoLogoffInfo autoLogoffInfo = new AutoLogoffInfo();
        autoLogoffInfo.type = 4;
        autoLogoffInfo.ssoVanityURL = webLaunchedToLoginParam.getSsoVanityURL();
        autoLogoffInfo.snsType = webLaunchedToLoginParam.getSnsType();
        finishActivity();
        LoginActivity.show(VideoBoxApplication.getGlobalContext(), false, -1, autoLogoffInfo);
    }

    private static void finishActivity() {
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt instanceof ConfActivityNormal) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
    }

    public static boolean isCloudSwitchShow(int i) {
        boolean z = false;
        if (i == 1 || ZMBuildConfig.BUILD_TARGET != 0) {
            return false;
        }
        PTApp instance = PTApp.getInstance();
        ZMNativeSsoCloudInfo sSOCloudInfo = instance.getSSOCloudInfo();
        boolean z2 = instance.isEnableCloudSwitch() && (sSOCloudInfo == null || !sSOCloudInfo.isMbLocked());
        if (z2) {
            ArrayList arrayList = new ArrayList();
            if (instance.getCloudSwitchList(arrayList) && arrayList.size() > 0) {
                z = true;
            }
        } else {
            z = z2;
        }
        return z;
    }

    public static boolean isMeetingProcessRun() {
        return PTApp.getInstance().hasActiveCall() && VideoBoxApplication.getNonNullInstance().isConfProcessRunning();
    }

    public static void setSSOUrl(String str, int i) {
        ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
        if (zoomProductHelper != null) {
            zoomProductHelper.setCurrentVendor(i);
            PTApp.getInstance().setSSOURL(str, i);
        }
    }
}
