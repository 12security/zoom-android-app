package com.zipow.videobox;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.NotificationMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.thirdparty.login.facebook.FacebookError;
import p021us.zoom.thirdparty.login.facebook.ServiceListener;

public class AutoRecoveryUtil implements IPTUIListener {
    private static final String TAG = "AutoRecoveryUtil";
    @Nullable
    private static AutoRecoveryUtil instance;
    private int loginFailTimes = 0;
    /* access modifiers changed from: private */
    @Nullable
    public AuthToken mFacebookAuthToken;
    @NonNull
    private Handler mHandler = new Handler();

    class ExtendTokenServiceListener implements ServiceListener {
        ExtendTokenServiceListener() {
        }

        public void onComplete(Bundle bundle) {
            if (AutoRecoveryUtil.this.mFacebookAuthToken != null) {
                AutoRecoveryUtil autoRecoveryUtil = AutoRecoveryUtil.this;
                autoRecoveryUtil.onFBTokenExtended(autoRecoveryUtil.mFacebookAuthToken.token);
            }
        }

        public void onFacebookError(@NonNull FacebookError facebookError) {
            AutoRecoveryUtil.this.handleTokenExpired(0);
        }

        public void onError(@NonNull Error error) {
            AutoRecoveryUtil.this.handleTokenExpired(0);
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    @NonNull
    public static synchronized AutoRecoveryUtil getInstance() {
        AutoRecoveryUtil autoRecoveryUtil;
        synchronized (AutoRecoveryUtil.class) {
            if (instance == null) {
                instance = new AutoRecoveryUtil();
            }
            autoRecoveryUtil = instance;
        }
        return autoRecoveryUtil;
    }

    private AutoRecoveryUtil() {
    }

    public void autoRecovery(@NonNull Context context) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null) {
            this.mFacebookAuthToken = FBSessionStore.getSession(context, FBSessionStore.FACEBOOK_KEY);
            if (!mainboard.isInitialized()) {
                if (NetworkUtil.hasDataNetwork(context)) {
                    VideoBoxApplication.getInstance().initPTMainboard();
                    if (ZMActivity.hasActivityCreated() || PTApp.getInstance().isDirectCallAvailable()) {
                        PTUI.getInstance().addPTUIListener(this);
                        autoLogin(context);
                    } else {
                        VideoBoxApplication.getInstance().exit();
                    }
                }
            } else if (ZMActivity.getFrontActivity() == null) {
                PTUI.getInstance().addPTUIListener(this);
                autoLogin(context);
            }
        }
    }

    /* access modifiers changed from: private */
    public void autoLogin(Context context) {
        if (NetworkUtil.hasDataNetwork(context)) {
            PTApp instance2 = PTApp.getInstance();
            if (!instance2.isAuthenticating() && !instance2.isWebSignedOn() && ZMActivity.getFrontActivity() == null) {
                int pTLoginType = instance2.getPTLoginType();
                if (pTLoginType != 0) {
                    if (pTLoginType != 2) {
                        switch (pTLoginType) {
                            case 100:
                            case 101:
                                break;
                            default:
                                if (ZmLoginHelper.isUseZoomLogin(pTLoginType) && !instance2.autoSignin()) {
                                    handleTokenExpired(pTLoginType);
                                    break;
                                }
                        }
                    }
                    if (!instance2.autoSignin()) {
                        handleTokenExpired(pTLoginType);
                    }
                } else {
                    AuthToken authToken = this.mFacebookAuthToken;
                    if (authToken != null) {
                        if (authToken.isSessionValid()) {
                            boolean z = false;
                            if (this.mFacebookAuthToken.shouldExtendAccessToken()) {
                                try {
                                    z = !this.mFacebookAuthToken.extendAccessToken(context, new ExtendTokenServiceListener());
                                } catch (Exception unused) {
                                }
                            } else {
                                z = true;
                            }
                            if (z && !instance2.autoSignin()) {
                                handleTokenExpired(pTLoginType);
                            }
                        } else {
                            handleTokenExpired(pTLoginType);
                        }
                    }
                }
            }
        }
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            sinkWebLogin(j);
        } else if (i == 8) {
            sinkIMLogin(j);
        }
    }

    /* access modifiers changed from: private */
    public void onFBTokenExtended(String str) {
        AuthToken authToken = this.mFacebookAuthToken;
        if (authToken != null) {
            FBSessionStore.save(FBSessionStore.FACEBOOK_KEY, authToken, VideoBoxApplication.getInstance());
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            long currentTimeMillis = (this.mFacebookAuthToken.expires - System.currentTimeMillis()) / 1000;
            PTApp instance2 = PTApp.getInstance();
            instance2.loginXmppServer(str);
            instance2.loginWithFacebook(str, currentTimeMillis);
        }
    }

    private void sinkWebLogin(long j) {
        if (ZMActivity.getFrontActivity() == null) {
            if (j == 0) {
                this.loginFailTimes = 0;
            } else if (j == 1006) {
                handleTokenExpired(PTApp.getInstance().getPTLoginType());
            } else {
                this.loginFailTimes++;
                int i = this.loginFailTimes;
                if (i > 8) {
                    i = 8;
                }
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        PTApp instance = PTApp.getInstance();
                        IMHelper iMHelper = instance.getIMHelper();
                        boolean z = iMHelper != null && iMHelper.isIMSignedOn();
                        if (!instance.isWebSignedOn() && !instance.isAuthenticating() && !z) {
                            AutoRecoveryUtil.this.autoLogin(VideoBoxApplication.getInstance());
                        }
                    }
                }, (long) ((2 << i) * 1000));
            }
        }
    }

    private void sinkIMLogin(long j) {
        if (ZMActivity.getFrontActivity() == null) {
            switch ((int) j) {
                case 2:
                case 3:
                    int pTLoginType = PTApp.getInstance().getPTLoginType();
                    if (pTLoginType != 97) {
                        if (j == 3 && pTLoginType == 0) {
                            FBSessionStore.clear(FBSessionStore.FACEBOOK_KEY, VideoBoxApplication.getInstance());
                        }
                        PTApp.getInstance().setRencentJid("");
                        handleTokenExpired(pTLoginType);
                        break;
                    }
                    break;
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTokenExpired(int i) {
        PTApp.getInstance().setTokenExpired(true);
        NotificationMgr.showLoginExpiredNotification(VideoBoxApplication.getInstance(), i);
    }
}
