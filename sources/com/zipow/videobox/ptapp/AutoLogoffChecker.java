package com.zipow.videobox.ptapp;

import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTAppProtos.ZoomAccount;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMActivity.GlobalActivityListener;

public class AutoLogoffChecker implements IPTUIListener {
    private static final String TAG = "AutoLogoffChecker";
    private static AutoLogoffChecker instance;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mAutoLogoffTask;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public long mLastCheckTimeStamps;
    /* access modifiers changed from: private */
    public boolean mLogoffWhenFront = false;
    /* access modifiers changed from: private */
    @NonNull
    public List<AutoLogoffInfo> mNoActivityDelayForAutoLogoffInfo = new ArrayList();

    public static class AutoLogoffInfo implements Serializable {
        public static final int TYPE_E2E = 1;
        public static final int TYPE_ONWENLAUNCHEDLOGIN = 4;
        public static final int TYPE_RESTRICTEDLOGINDOMAIN = 3;
        public static final int TYPE_SSO = 2;
        public static final int TYPE_UNKNOW = 0;
        private static final long serialVersionUID = 1;
        public int errorCode;
        public long loginTime;
        public long minutes;
        public int snsType;
        public String ssoVanityURL;
        public int type;
        public String userName;
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    private AutoLogoffChecker() {
        ZMActivity.addGlobalActivityListener(new GlobalActivityListener() {
            public void onUIMoveToBackground() {
            }

            public void onActivityMoveToFront(ZMActivity zMActivity) {
                if (AutoLogoffChecker.this.mLogoffWhenFront && AutoLogoffChecker.this.mAutoLogoffTask != null) {
                    AutoLogoffChecker.this.mAutoLogoffTask.run();
                }
                AutoLogoffChecker.this.mLogoffWhenFront = false;
            }

            public void onUserActivityOnUI() {
                AutoLogoffChecker.this.onUserActivityOnUI();
            }
        });
        CmmSIPCallManager.getInstance().addListener(new SimpleSIPCallEventListener() {
            public void OnNewCallGenerate(String str, int i) {
                super.OnNewCallGenerate(str, i);
                AutoLogoffChecker.this.stopCheck();
            }

            public void OnCallTerminate(String str, int i) {
                super.OnCallTerminate(str, i);
                if (AutoLogoffChecker.this.checkStopAutoLogoff()) {
                    AutoLogoffChecker.this.stopCheck();
                } else {
                    AutoLogoffChecker.this.startChecker();
                }
            }
        });
        ZoomMessengerUI.getInstance().addListener(new SimpleZoomMessengerUIListener() {
            public void NotifyIMWebSettingUpdated(int i) {
                AutoLogoffChecker.this.NotifyIMWebSettingUpdated(i);
            }
        });
    }

    public static AutoLogoffChecker getInstance() {
        if (instance == null) {
            instance = new AutoLogoffChecker();
        }
        return instance;
    }

    public void startChecker() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null && instance2.isPTApp()) {
            PTUI.getInstance().addPTUIListener(this);
            if (PTApp.getInstance().isWebSignedOn()) {
                startTask();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onUserActivityOnUI() {
        if (this.mAutoLogoffTask != null && this.mNoActivityDelayForAutoLogoffInfo.size() > 0) {
            this.mLastCheckTimeStamps = CmmTime.getMMNow();
            if (!hasSSOForceLogoff()) {
                this.mHandler.removeCallbacks(this.mAutoLogoffTask);
                this.mHandler.postDelayed(this.mAutoLogoffTask, 60000);
            }
        }
    }

    private boolean hasSSOForceLogoff() {
        boolean z = false;
        if (this.mNoActivityDelayForAutoLogoffInfo.isEmpty()) {
            return false;
        }
        Iterator it = this.mNoActivityDelayForAutoLogoffInfo.iterator();
        while (true) {
            if (it.hasNext()) {
                if (((AutoLogoffInfo) it.next()).type == 2) {
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void stopCheck() {
        Runnable runnable = this.mAutoLogoffTask;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        this.mAutoLogoffTask = null;
    }

    private void startTask() {
        Runnable runnable = this.mAutoLogoffTask;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        this.mNoActivityDelayForAutoLogoffInfo.clear();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            this.mLastCheckTimeStamps = CmmTime.getMMNow();
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                int sSOEnforceLogoutTimeInMins = currentUserProfile.getSSOEnforceLogoutTimeInMins();
                long sSOLoginWithPasswordTime = currentUserProfile.getSSOLoginWithPasswordTime();
                if (sSOEnforceLogoutTimeInMins != 0) {
                    AutoLogoffInfo autoLogoffInfo = new AutoLogoffInfo();
                    autoLogoffInfo.type = 2;
                    autoLogoffInfo.minutes = (long) sSOEnforceLogoutTimeInMins;
                    autoLogoffInfo.loginTime = sSOLoginWithPasswordTime;
                    this.mNoActivityDelayForAutoLogoffInfo.add(autoLogoffInfo);
                }
                int e2eGetAutologoffMinutes = zoomMessenger.e2eGetAutologoffMinutes();
                if (e2eGetAutologoffMinutes != 0) {
                    AutoLogoffInfo autoLogoffInfo2 = new AutoLogoffInfo();
                    autoLogoffInfo2.type = 1;
                    autoLogoffInfo2.minutes = (long) e2eGetAutologoffMinutes;
                    this.mNoActivityDelayForAutoLogoffInfo.add(autoLogoffInfo2);
                }
                if (this.mNoActivityDelayForAutoLogoffInfo.size() > 0) {
                    this.mAutoLogoffTask = new Runnable() {
                        public void run() {
                            AutoLogoffInfo autoLogoffInfo;
                            if (AutoLogoffChecker.this.mNoActivityDelayForAutoLogoffInfo.size() != 0) {
                                if (AutoLogoffChecker.this.isFileTransferring()) {
                                    AutoLogoffChecker.this.mHandler.postDelayed(AutoLogoffChecker.this.mAutoLogoffTask, 60000);
                                    return;
                                }
                                Iterator it = AutoLogoffChecker.this.mNoActivityDelayForAutoLogoffInfo.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        autoLogoffInfo = null;
                                        break;
                                    }
                                    autoLogoffInfo = (AutoLogoffInfo) it.next();
                                    long j = autoLogoffInfo.minutes * 60000;
                                    long mMNow = CmmTime.getMMNow();
                                    if (autoLogoffInfo.type != 2 || autoLogoffInfo.loginTime == 0 || mMNow - autoLogoffInfo.loginTime <= j) {
                                        if (mMNow - AutoLogoffChecker.this.mLastCheckTimeStamps > j) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (autoLogoffInfo == null) {
                                    AutoLogoffChecker.this.mHandler.postDelayed(AutoLogoffChecker.this.mAutoLogoffTask, 60000);
                                    return;
                                }
                                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                                if (frontActivity == null || !frontActivity.isActive()) {
                                    AutoLogoffChecker.this.mLogoffWhenFront = true;
                                    return;
                                }
                                ZoomAccount savedZoomAccount = PTApp.getInstance().getSavedZoomAccount();
                                if (savedZoomAccount != null) {
                                    autoLogoffInfo.userName = savedZoomAccount.getUserName();
                                }
                                PTApp.getInstance().logout(0);
                                int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
                                if (inProcessActivityCountInStack > 0) {
                                    for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                                        ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                                        if (!(inProcessActivityInStackAt instanceof ConfActivityNormal) && inProcessActivityInStackAt != null) {
                                            inProcessActivityInStackAt.finish();
                                        }
                                    }
                                }
                                LoginActivity.show(VideoBoxApplication.getGlobalContext(), false, -1, autoLogoffInfo);
                                AutoLogoffChecker.this.mHandler.removeCallbacks(AutoLogoffChecker.this.mAutoLogoffTask);
                                AutoLogoffChecker.this.mAutoLogoffTask = null;
                            }
                        }
                    };
                    this.mHandler.postDelayed(this.mAutoLogoffTask, 60000);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isFileTransferring() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return zoomMessenger != null && zoomMessenger.hasUpOrDownloadingFileRequest();
    }

    public void onPTAppEvent(int i, long j) {
        if (i != 22) {
            switch (i) {
                case 0:
                    if (j == 0) {
                        startTask();
                        return;
                    }
                    return;
                case 1:
                    stopCheck();
                    return;
                default:
                    return;
            }
        } else if (checkStopAutoLogoff()) {
            stopCheck();
        } else {
            startChecker();
        }
    }

    /* access modifiers changed from: private */
    public boolean checkStopAutoLogoff() {
        return (PTApp.getInstance().hasActiveCall() && VideoBoxApplication.getInstance().isConfProcessRunning()) || CmmSIPCallManager.getInstance().hasSipCallsInCache();
    }

    /* access modifiers changed from: private */
    public void NotifyIMWebSettingUpdated(int i) {
        stopCheck();
        startChecker();
    }
}
