package com.zipow.videobox.util;

import android.content.Context;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ZMNoticeProtocolActionBlockedTask;
import com.zipow.videobox.ptapp.AutoLogoffChecker.AutoLogoffInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.ZoomAccount;
import com.zipow.videobox.ptapp.SSBPTERROR;
import java.util.Locale;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class LoginUtil {
    private static final String TAG = "LoginUtil";

    public static boolean showLoginUI(Context context, boolean z, int i) {
        if (ResourcesUtil.getBoolean(context, C4558R.bool.zm_config_use_zoom_login, true)) {
            return LoginActivity.show(context, z, i);
        }
        String string = ResourcesUtil.getString(context, C4558R.string.zm_config_login_activity);
        if (!StringUtil.isEmptyOrNull(string)) {
            return showLoginActivity(context, string);
        }
        return false;
    }

    private static boolean showLoginActivity(Context context, @NonNull String str) {
        try {
            return ((Boolean) Class.forName(str).getMethod("show", new Class[]{Context.class}).invoke(null, new Object[]{context})).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public static int getDefaultVendor() {
        if (ZMBuildConfig.BUILD_TARGET == 0 && Locale.CHINA.getCountry().equalsIgnoreCase(CompatUtils.getLocalDefault().getCountry())) {
            return 1;
        }
        return 0;
    }

    public static void launchLogin(boolean z, boolean z2) {
        AutoLogoffInfo autoLogoffInfo;
        if (z) {
            ZoomAccount savedZoomAccount = PTApp.getInstance().getSavedZoomAccount();
            autoLogoffInfo = new AutoLogoffInfo();
            if (savedZoomAccount != null) {
                autoLogoffInfo.userName = savedZoomAccount.getUserName();
            }
        } else {
            autoLogoffInfo = null;
        }
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity instanceof LoginActivity) {
            PTApp.getInstance().logout(0);
            return;
        }
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt == null || frontActivity == inProcessActivityInStackAt)) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        PTApp.getInstance().logout(0);
        PTApp.getInstance().setNeedToReturnToMeetingOnResume(z2);
        if (frontActivity != null) {
            LoginActivity.show(frontActivity, false, 0, autoLogoffInfo);
            frontActivity.finish();
        } else {
            LoginActivity.show(VideoBoxApplication.getGlobalContext(), false, 0, autoLogoffInfo);
        }
    }

    public static void launchLogin(String str, boolean z) {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity instanceof LoginActivity) {
            PTApp.getInstance().logout(0);
            return;
        }
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt == null || frontActivity == inProcessActivityInStackAt)) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        PTApp.getInstance().logout(0);
        PTApp.getInstance().setNeedToReturnToMeetingOnResume(z);
        if (frontActivity != null) {
            LoginActivity.showWithPrefillName(frontActivity, false, 0, str);
            frontActivity.finish();
        } else {
            LoginActivity.showWithPrefillName(VideoBoxApplication.getGlobalContext(), false, 0, str);
        }
    }

    public static boolean ShowRestrictedLoginErrorDlg(int i, boolean z) {
        String str = "";
        boolean z2 = true;
        switch (i) {
            case 6001:
                str = getFmtRestrictedLoginDomain();
                break;
            case SSBPTERROR.SSBPT_ERROR_RESTRICTED_LOGIN_TYPE_GG /*6002*/:
            case SSBPTERROR.SSBPT_ERROR_RESTRICTED_LOGIN_TYPE_FB /*6003*/:
                str = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_restricted_login_web_start_129757);
                break;
            case SSBPTERROR.SSBPT_ERROR_RESTRICTED_LOGIN_TYPE_WORKEMAIL /*6005*/:
                str = VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_restricted_email_login_129757);
                break;
            default:
                z2 = false;
                break;
        }
        if (z2 && !z) {
            ForegroundTaskManager.getInstance().runInForeground(new ZMNoticeProtocolActionBlockedTask(ZMNoticeProtocolActionBlockedTask.class.getName(), str));
        }
        return z2;
    }

    public static String getFmtRestrictedLoginDomain() {
        String fmtRestrictedLoginDomain = PTApp.getInstance().getFmtRestrictedLoginDomain();
        if (StringUtil.isEmptyOrNull(fmtRestrictedLoginDomain)) {
            return null;
        }
        String replace = fmtRestrictedLoginDomain.replace("&", PreferencesConstants.COOKIE_DELIMITER);
        return String.format(VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_require_sign_with_company_message_129757), new Object[]{replace});
    }

    public static void autoLogout(int i) {
        AutoLogoffInfo autoLogoffInfo = new AutoLogoffInfo();
        autoLogoffInfo.type = 3;
        autoLogoffInfo.errorCode = i;
        PTApp.getInstance().logout(0);
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i2 = inProcessActivityCountInStack - 1; i2 >= 0; i2--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i2);
                if (!(inProcessActivityInStackAt instanceof ConfActivityNormal) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        LoginActivity.show(VideoBoxApplication.getGlobalContext(), false, -1, autoLogoffInfo);
    }
}
