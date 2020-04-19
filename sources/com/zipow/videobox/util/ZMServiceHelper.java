package com.zipow.videobox.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfService;
import com.zipow.videobox.PTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ZMBaseService;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.DeviceInfoUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;

public class ZMServiceHelper {
    private static final String TAG = "ZMServiceHelper";

    public static void doServiceActionInFront(String str, @NonNull Class<? extends ZMBaseService> cls) {
        doServiceActionInFront(str, null, cls);
    }

    public static void doServiceActionInFront(final String str, @Nullable final Bundle bundle, @NonNull final Class<? extends ZMBaseService> cls) {
        if (!OsUtil.isAtLeastO() || VideoBoxApplication.getInstance() == null || VideoBoxApplication.getInstance().isSDKMode()) {
            doServiceAction(str, bundle, cls);
        } else {
            ForegroundTaskManager.getInstance().runInForeground(new ForegroundTask(StringUtil.isEmptyOrNull(str) ? cls.getName() : str) {
                public boolean isOtherProcessSupported() {
                    return false;
                }

                public boolean isValidActivity(String str) {
                    return true;
                }

                public void run(ZMActivity zMActivity) {
                    if (DeviceInfoUtil.isInAndroidOFrontServiceWhiteList()) {
                        ZMServiceHelper.doServiceActionForceInFront(str, bundle, cls);
                    } else {
                        ZMServiceHelper.doServiceAction(str, bundle, cls);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void doServiceAction(@Nullable String str, @Nullable Bundle bundle, @NonNull Class<? extends ZMBaseService> cls) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            Intent intent = new Intent();
            if (str != null) {
                intent.setAction(str);
            }
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.setClassName(instance.getPackageName(), cls.getName());
            CompatUtils.startService(instance, intent, !instance.isAtFront(), instance.isMultiProcess());
        }
    }

    /* access modifiers changed from: private */
    public static void doServiceActionForceInFront(@Nullable String str, @Nullable Bundle bundle, @NonNull Class<? extends ZMBaseService> cls) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            Intent intent = new Intent();
            if (str != null) {
                intent.setAction(str);
            }
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.setClassName(instance.getPackageName(), cls.getName());
            CompatUtils.startService(instance, intent, false, instance.isMultiProcess());
        }
    }

    public static void doServiceAction(@Nullable String str, String str2, @Nullable Bundle bundle, @NonNull Class<? extends ZMBaseService> cls) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("SDK model no service.  doServiceAction : ");
            sb.append(str);
            ZMLog.m286i(str3, sb.toString(), new Object[0]);
            if (VideoBoxApplication.getInstance().isSDKMode()) {
                if (PTService.ACTION_SHOW_CONF_NOTIFICATION.equals(str)) {
                    NotificationMgr.showConfNotificationForSDK(instance);
                    return;
                } else if (PTService.ACTION_REMOVE_CONF_NOTIFICATION.equals(str) || PTService.ACTION_STOP_FOREGROUND.equals(str)) {
                    NotificationMgr.removeConfNotification(instance);
                    return;
                } else if (cls == PTService.class || cls == ConfService.class) {
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SDK model no service.  doServiceAction : ");
                    sb2.append(str);
                    ZMLog.m280e(str4, sb2.toString(), new Object[0]);
                    return;
                }
            }
            Intent intent = new Intent();
            if (str != null) {
                intent.setAction(str);
            }
            if (!StringUtil.isEmptyOrNull(str2) && bundle != null) {
                intent.putExtra(str2, bundle);
            }
            intent.setClassName(instance.getPackageName(), cls.getName());
            CompatUtils.startService(instance, intent, !instance.isAtFront(), instance.isMultiProcess());
        }
    }

    public static void stopService(@NonNull Context context, @NonNull String str, @NonNull String str2) {
        Intent intent = new Intent();
        intent.setClassName(str, str2);
        try {
            context.stopService(intent);
        } catch (Exception unused) {
        }
    }
}
