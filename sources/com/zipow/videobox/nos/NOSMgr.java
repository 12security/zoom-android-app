package com.zipow.videobox.nos;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.zipow.videobox.config.ConfigReader;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.Arrays;
import java.util.List;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class NOSMgr {
    private static final List<String> GCM_NOT_SUPPORT_COUNTRY_CODE = Arrays.asList(new String[]{CountryCodeUtil.CN_ISO_COUNTRY_CODE});
    private static final int GCM_REGISTER_TIMEOUT = 10000;
    private static final String NEED_UNREGISTER_C2DM = "need_unregister_c2dm";
    private static final String TAG = "NOSMgr";
    public static final int XMPP_MSG_TYPE_AUDIO = 4;
    public static final int XMPP_MSG_TYPE_AUDIOCALL = 8;
    public static final int XMPP_MSG_TYPE_FILE = 5;
    public static final int XMPP_MSG_TYPE_IMG = 3;
    public static final int XMPP_MSG_TYPE_SCREENSHARECALL = 7;
    public static final int XMPP_MSG_TYPE_TXT = 1;
    public static final int XMPP_MSG_TYPE_VIDEO = 2;
    public static final int XMPP_MSG_TYPE_VIDEOCALL = 6;
    @Nullable
    private static NOSMgr instance = null;
    /* access modifiers changed from: private */
    public Context mContext;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mbGCMRegistered = false;
    private boolean mbRegistering = false;
    private boolean mbUnregistering = false;

    @NonNull
    public static synchronized NOSMgr getInstance() {
        NOSMgr nOSMgr;
        synchronized (NOSMgr.class) {
            if (instance == null) {
                instance = new NOSMgr();
            }
            nOSMgr = instance;
        }
        return nOSMgr;
    }

    private NOSMgr() {
    }

    public void initialize(Context context) {
        this.mContext = context;
    }

    public boolean isGCMRegistered() {
        return this.mbGCMRegistered;
    }

    public void register() {
        if (isC2DMCapable(this.mContext)) {
            String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.FCM_REGISTRATION_TOKEN, null);
            int readIntValue = PreferenceUtil.readIntValue(PreferenceUtil.FCM_REGISTRATION_VERSION_CODE, 0);
            if (StringUtil.isEmptyOrNull(readStringValue) || readIntValue != AndroidAppUtil.getAppVersionCode(this.mContext)) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            InstanceIdResult instanceIdResult = (InstanceIdResult) task.getResult();
                            if (instanceIdResult != null) {
                                String token = instanceIdResult.getToken();
                                if (!TextUtils.isEmpty(token)) {
                                    PreferenceUtil.saveStringValue(PreferenceUtil.FCM_REGISTRATION_TOKEN, token);
                                    PreferenceUtil.saveIntValue(PreferenceUtil.FCM_REGISTRATION_VERSION_CODE, AndroidAppUtil.getAppVersionCode(NOSMgr.this.mContext));
                                    PTApp.getInstance().nos_SetDeviceToken(token, SystemInfoHelper.getDeviceId());
                                }
                            }
                        }
                    }
                });
            } else {
                PTApp.getInstance().nos_SetDeviceToken(readStringValue, SystemInfoHelper.getDeviceId());
            }
        } else {
            PreferenceUtil.saveStringValue(PreferenceUtil.FCM_REGISTRATION_TOKEN, null);
            PreferenceUtil.saveIntValue(PreferenceUtil.FCM_REGISTRATION_VERSION_CODE, 0);
        }
    }

    public void unregister() {
        unregisterC2DM();
    }

    public void onQueryIPLocation(IPLocationInfo iPLocationInfo) {
        if (!isC2DMCapable(this.mContext) && isGCMRegistered()) {
            this.mbGCMRegistered = false;
        }
    }

    public void onXMPPConnectSuccess() {
        PTApp.getInstance().getIPLocation(true);
    }

    private void unregisterC2DM() {
        Context context = this.mContext;
        if (context != null && isC2DMCapable(context)) {
            this.mbUnregistering = true;
            this.mbGCMRegistered = false;
            PreferenceUtil.saveStringValue(PreferenceUtil.GCM_REGISTRATION_ID, null);
            PreferenceUtil.saveLongValue(PreferenceUtil.GCM_REGISTRATION_ID_TIMESTAMP, 0);
        }
    }

    private boolean isC2DMCapable(Context context) {
        return PreferenceUtil.readBooleanValue(ConfigReader.KEY_GCM_ALWAYS, false) ? true : true;
    }
}
