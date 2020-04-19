package com.zipow.videobox.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.zipow.cmmlib.AppContext;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.BuildTarget;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class ConfigReader extends BroadcastReceiver {
    public static final String ACTION_READ_CONFIG = "us.zoom.videomeetings.intent.action.READ_CONFIG";
    public static final String ACTION_ZOOM_CONFIG = "us.zoom.videomeetings.intent.action.ZOOM_CONFIG";
    public static final String KEY_ADDRESS_BOOK_ENABLED = "AddressBookEnabled";
    public static final String KEY_AUDIO_API_TYPE = "audioAPIType";
    public static final String KEY_CONF_SERVER_RINGCENTRAL_API = "conf.server.ringcentralapi";
    public static final String KEY_CRASH_DUMP_USER_INFO = "Crash.DumpUserInfor";
    public static final String KEY_DB_SDK = "dbSDK";
    public static final String KEY_DISABLE_DEADLOCK_DETECT = "com.zoom.test.disable_deadlock_detect";
    public static final String KEY_DISABLE_UTIL_LOG = "DisableUtilLog";
    public static final String KEY_ENABLELOG = "enableLog";
    public static final String KEY_ENABLE_MZM_LOG = "enableMzmLog";
    public static final String KEY_FORCE_DISABLE_GCM = "forceDisableGCM";
    public static final String KEY_GCM_ALWAYS = "gcmAlways";
    public static final String KEY_GCM_CAPABLE = "gcmCapable";
    public static final String KEY_LOG_LEVEL = "logLevel";
    public static final String KEY_UI_MODE = "UIMode";
    public static final String KEY_WEBSERVER = "conf.webserver";
    public static final String KEY_WEBSERVER_BEFORE_CN = "conf.webserver.before.cn";
    private static final String TAG = "ConfigReader";

    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if (ACTION_READ_CONFIG.equals(intent.getAction())) {
            broadcastConfigInfo(context);
        }
    }

    public static void broadcastConfigInfo(@NonNull Context context) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null) {
            if (!mainboard.isInitialized()) {
                VideoBoxApplication.getInstance().initPTMainboard();
            }
            AppContext appContext = new AppContext(AppContext.PREFER_NAME_CHAT);
            String queryWithKey = appContext.queryWithKey(KEY_WEBSERVER, AppContext.APP_NAME_CHAT);
            if (StringUtil.isEmptyOrNull(queryWithKey)) {
                queryWithKey = PTApp.getInstance().getZoomDomain();
            }
            boolean equals = "true".equals(appContext.queryWithKey(KEY_ENABLELOG, AppContext.APP_NAME_CHAT));
            boolean equals2 = "true".equals(appContext.queryWithKey(KEY_ENABLE_MZM_LOG, AppContext.APP_NAME_CHAT));
            String queryWithKey2 = appContext.queryWithKey(KEY_LOG_LEVEL, AppContext.APP_NAME_CHAT);
            boolean equals3 = "true".equals(appContext.queryWithKey(KEY_DISABLE_UTIL_LOG, AppContext.APP_NAME_CHAT));
            String queryWithKey3 = appContext.queryWithKey(KEY_DISABLE_DEADLOCK_DETECT, AppContext.APP_NAME_CHAT);
            String queryWithKey4 = appContext.queryWithKey(KEY_CRASH_DUMP_USER_INFO, AppContext.APP_NAME_CHAT);
            String queryWithKey5 = appContext.queryWithKey(KEY_CONF_SERVER_RINGCENTRAL_API, AppContext.APP_NAME_CHAT);
            String readStringValue = PreferenceUtil.readStringValue(KEY_UI_MODE, null);
            String readStringValue2 = PreferenceUtil.readStringValue(KEY_ADDRESS_BOOK_ENABLED, null);
            boolean readBooleanValue = PreferenceUtil.readBooleanValue(KEY_FORCE_DISABLE_GCM, false);
            String readStringValue3 = PreferenceUtil.readStringValue(KEY_AUDIO_API_TYPE, null);
            boolean readBooleanValue2 = PreferenceUtil.readBooleanValue(KEY_GCM_ALWAYS, false);
            boolean readBooleanValue3 = PreferenceUtil.readBooleanValue(KEY_DB_SDK, false);
            Intent intent = new Intent();
            intent.setAction(ACTION_ZOOM_CONFIG);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.putExtra(KEY_WEBSERVER, queryWithKey);
            intent.putExtra(KEY_ENABLELOG, equals);
            intent.putExtra(KEY_ENABLE_MZM_LOG, equals2);
            intent.putExtra(KEY_LOG_LEVEL, queryWithKey2);
            intent.putExtra(KEY_DISABLE_UTIL_LOG, equals3);
            intent.putExtra(KEY_DISABLE_DEADLOCK_DETECT, queryWithKey3);
            intent.putExtra(KEY_CRASH_DUMP_USER_INFO, queryWithKey4);
            intent.putExtra(KEY_UI_MODE, readStringValue);
            intent.putExtra(KEY_ADDRESS_BOOK_ENABLED, readStringValue2);
            intent.putExtra(KEY_FORCE_DISABLE_GCM, readBooleanValue);
            intent.putExtra(KEY_AUDIO_API_TYPE, readStringValue3);
            intent.putExtra(KEY_GCM_CAPABLE, AndroidAppUtil.isC2DMCapable(context));
            intent.putExtra(KEY_GCM_ALWAYS, readBooleanValue2);
            intent.putExtra(KEY_DB_SDK, readBooleanValue3);
            if (BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET)) {
                intent.putExtra(KEY_CONF_SERVER_RINGCENTRAL_API, queryWithKey5);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(".permission.READ_CONFIG");
            context.sendBroadcast(intent, sb.toString());
        }
    }
}
