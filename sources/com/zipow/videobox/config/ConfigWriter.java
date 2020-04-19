package com.zipow.videobox.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.zipow.cmmlib.AppContext;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMDomainUtil;
import java.io.File;
import p021us.zoom.androidlib.util.StringUtil;

public class ConfigWriter extends BroadcastReceiver {
    public static final String ACTION_CHANGE_CONFIG = "us.zoom.videomeetings.intent.action.CHANGE_CONFIG";
    public static final String ACTION_RESET_CONFIG = "us.zoom.videomeetings.intent.action.RESET_CONFIG";
    private static final String TAG = "ConfigWriter";

    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if (VideoBoxApplication.getInstance() == null) {
            VideoBoxApplication.initialize(context.getApplicationContext(), false, 0, null);
        }
        if (ACTION_CHANGE_CONFIG.equals(intent.getAction())) {
            changeConfig(context, intent);
        } else if (ACTION_RESET_CONFIG.equals(intent.getAction())) {
            resetConfig(context, intent);
        }
    }

    private void changeConfig(@NonNull Context context, Intent intent) {
        String str;
        boolean z;
        boolean z2;
        Intent intent2 = intent;
        AppContext appContext = new AppContext(AppContext.PREFER_NAME_CHAT);
        appContext.beginTransaction();
        boolean equals = "true".equals(appContext.queryWithKey(ConfigReader.KEY_ENABLELOG, AppContext.APP_NAME_CHAT));
        boolean equals2 = "true".equals(appContext.queryWithKey(ConfigReader.KEY_ENABLE_MZM_LOG, AppContext.APP_NAME_CHAT));
        boolean equals3 = "true".equals(appContext.queryWithKey(ConfigReader.KEY_DISABLE_UTIL_LOG, AppContext.APP_NAME_CHAT));
        String queryWithKey = appContext.queryWithKey(ConfigReader.KEY_WEBSERVER, AppContext.APP_NAME_CHAT);
        String readStringValue = PreferenceUtil.readStringValue(ConfigReader.KEY_UI_MODE, null);
        String readStringValue2 = PreferenceUtil.readStringValue(ConfigReader.KEY_ADDRESS_BOOK_ENABLED, null);
        boolean readBooleanValue = PreferenceUtil.readBooleanValue(ConfigReader.KEY_FORCE_DISABLE_GCM, false);
        String readStringValue3 = PreferenceUtil.readStringValue(ConfigReader.KEY_AUDIO_API_TYPE, null);
        boolean readBooleanValue2 = PreferenceUtil.readBooleanValue(ConfigReader.KEY_GCM_ALWAYS, false);
        boolean readBooleanValue3 = PreferenceUtil.readBooleanValue(ConfigReader.KEY_DB_SDK, false);
        String stringExtra = intent2.getStringExtra(ConfigReader.KEY_WEBSERVER);
        String stringExtra2 = intent2.getStringExtra(ConfigReader.KEY_CONF_SERVER_RINGCENTRAL_API);
        String stringExtra3 = intent2.getStringExtra(ConfigReader.KEY_LOG_LEVEL);
        boolean booleanExtra = intent2.getBooleanExtra(ConfigReader.KEY_ENABLELOG, equals);
        boolean booleanExtra2 = intent2.getBooleanExtra(ConfigReader.KEY_ENABLE_MZM_LOG, equals2);
        boolean booleanExtra3 = intent2.getBooleanExtra(ConfigReader.KEY_DISABLE_UTIL_LOG, equals3);
        String stringExtra4 = intent2.getStringExtra(ConfigReader.KEY_DISABLE_DEADLOCK_DETECT);
        String str2 = readStringValue;
        String stringExtra5 = intent2.getStringExtra(ConfigReader.KEY_CRASH_DUMP_USER_INFO);
        String str3 = readStringValue2;
        boolean booleanExtra4 = intent2.getBooleanExtra(ConfigReader.KEY_GCM_ALWAYS, readBooleanValue2);
        boolean booleanExtra5 = intent2.getBooleanExtra(ConfigReader.KEY_DB_SDK, readBooleanValue3);
        String stringExtra6 = intent2.getStringExtra(ConfigReader.KEY_UI_MODE);
        if (stringExtra6 == null) {
            stringExtra6 = str2;
            str = readStringValue3;
        } else {
            str = readStringValue3;
        }
        String stringExtra7 = intent2.getStringExtra(ConfigReader.KEY_ADDRESS_BOOK_ENABLED);
        if (stringExtra7 == null) {
            stringExtra7 = str3;
            z = booleanExtra5;
        } else {
            z = booleanExtra5;
        }
        boolean booleanExtra6 = intent2.getBooleanExtra(ConfigReader.KEY_FORCE_DISABLE_GCM, readBooleanValue);
        String stringExtra8 = intent2.getStringExtra(ConfigReader.KEY_AUDIO_API_TYPE);
        if (stringExtra8 == null) {
            stringExtra8 = str;
        }
        if (stringExtra == null || stringExtra.length() <= 0 || stringExtra.equals(queryWithKey)) {
            if (StringUtil.isEmptyOrNull(stringExtra)) {
                appContext.setKeyValue(ConfigReader.KEY_WEBSERVER, null, AppContext.APP_NAME_CHAT);
            }
            z2 = false;
        } else {
            if (stringExtra.indexOf(ZMDomainUtil.ZM_URL_WEB_SERVER_DOMAIN) > 0) {
                stringExtra = stringExtra.replace(ZMDomainUtil.ZM_URL_WEB_SERVER_DOMAIN, ZMDomainUtil.getMainDomain());
            }
            appContext.setKeyValue(ConfigReader.KEY_WEBSERVER, stringExtra, AppContext.APP_NAME_CHAT);
            StringBuilder sb = new StringBuilder();
            sb.append(AppUtil.getDataPath());
            sb.append("/ZMPreSchedule");
            File file = new File(sb.toString());
            if (file.exists()) {
                file.delete();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(AppUtil.getDataPath());
            sb2.append("/ZMMeetingTrash");
            File file2 = new File(sb2.toString());
            if (file2.exists()) {
                file2.delete();
            }
            z2 = true;
        }
        appContext.setKeyValue(ConfigReader.KEY_CONF_SERVER_RINGCENTRAL_API, stringExtra2, AppContext.APP_NAME_CHAT);
        if (stringExtra3 != null && (stringExtra3.equals("info") || stringExtra3.equals("warning"))) {
            appContext.setKeyValue(ConfigReader.KEY_LOG_LEVEL, stringExtra3, AppContext.APP_NAME_CHAT);
        }
        if (stringExtra4 != null && stringExtra4.length() > 0) {
            appContext.setKeyValue(ConfigReader.KEY_DISABLE_DEADLOCK_DETECT, stringExtra4, AppContext.APP_NAME_CHAT);
        }
        if (stringExtra5 != null && stringExtra5.length() > 0) {
            appContext.setKeyValue(ConfigReader.KEY_CRASH_DUMP_USER_INFO, stringExtra5, AppContext.APP_NAME_CHAT);
        }
        appContext.setKeyValue(ConfigReader.KEY_ENABLELOG, String.valueOf(booleanExtra), AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_ENABLE_MZM_LOG, String.valueOf(booleanExtra2), AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_DISABLE_UTIL_LOG, String.valueOf(booleanExtra3), AppContext.APP_NAME_CHAT);
        if (stringExtra6 != null && (stringExtra6.equals("auto") || stringExtra6.equals("large") || stringExtra6.equals("normal"))) {
            PreferenceUtil.saveStringValue(ConfigReader.KEY_UI_MODE, stringExtra6);
        }
        if (stringExtra7 != null && (stringExtra7.equals("auto") || stringExtra7.equals("yes") || stringExtra7.equals("no"))) {
            PreferenceUtil.saveStringValue(ConfigReader.KEY_ADDRESS_BOOK_ENABLED, stringExtra7);
        }
        PreferenceUtil.saveBooleanValue(ConfigReader.KEY_FORCE_DISABLE_GCM, booleanExtra6);
        if (booleanExtra6) {
            PreferenceUtil.saveStringValue(PreferenceUtil.GCM_REGISTRATION_ID, null);
            PreferenceUtil.saveLongValue(PreferenceUtil.GCM_REGISTRATION_ID_TIMESTAMP, 0);
        }
        PreferenceUtil.saveBooleanValue(ConfigReader.KEY_GCM_ALWAYS, booleanExtra4);
        PreferenceUtil.saveBooleanValue(ConfigReader.KEY_DB_SDK, z);
        if (stringExtra8 != null && (stringExtra8.equals("auto") || stringExtra8.equals("java") || stringExtra8.equals("OpenSLES"))) {
            PreferenceUtil.saveStringValue(ConfigReader.KEY_AUDIO_API_TYPE, stringExtra8);
        }
        appContext.endTransaction();
        if (z2) {
            PTApp.getInstance().logout(0);
        }
        applyNewConfig(context);
    }

    private void resetConfig(@NonNull Context context, Intent intent) {
        AppContext appContext = new AppContext(AppContext.PREFER_NAME_CHAT);
        appContext.beginTransaction();
        appContext.setKeyValue(ConfigReader.KEY_WEBSERVER, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_LOG_LEVEL, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_ENABLELOG, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_ENABLE_MZM_LOG, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_DISABLE_UTIL_LOG, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_DISABLE_DEADLOCK_DETECT, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_CRASH_DUMP_USER_INFO, null, AppContext.APP_NAME_CHAT);
        appContext.setKeyValue(ConfigReader.KEY_CONF_SERVER_RINGCENTRAL_API, null, AppContext.APP_NAME_CHAT);
        appContext.endTransaction();
        PreferenceUtil.saveStringValue(ConfigReader.KEY_UI_MODE, "auto");
        PreferenceUtil.saveStringValue(ConfigReader.KEY_ADDRESS_BOOK_ENABLED, "auto");
        PreferenceUtil.saveBooleanValue(ConfigReader.KEY_FORCE_DISABLE_GCM, false);
        PreferenceUtil.saveStringValue(ConfigReader.KEY_AUDIO_API_TYPE, "auto");
        PreferenceUtil.saveBooleanValue(ConfigReader.KEY_GCM_ALWAYS, false);
        PreferenceUtil.saveBooleanValue(ConfigReader.KEY_DB_SDK, false);
        applyNewConfig(context);
    }

    private void applyNewConfig(@NonNull Context context) {
        ConfigReader.broadcastConfigInfo(context);
        VideoBoxApplication.getInstance().restart();
    }
}
