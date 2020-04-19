package com.zipow.videobox;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.dialog.RootedWarningDialog;
import com.zipow.videobox.dialog.RootedWarningDialog.RootedWarningDialogCallback;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.UpgradeUtil;
import com.zipow.videobox.util.ZMUtils;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.RootCheckUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.thirdparty.common.ZMThirdPartyUtils;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.videomeetings.C4558R;

public class LauncherActivity extends ZMActivity implements IPTUIListener, RootedWarningDialogCallback {
    private static final String ACTION_HANDLE_ACTION_SEND;
    private static final String ACTION_HANDLE_URI;
    private static final String ARG_ACTION_FOR_IM_ACTIVITY = "actionForIMActivity";
    private static final String ARG_EXTRAS_FOR_IM_ACTIVITY = "extrasForIMActivity";
    private static final String ARG_LAUNCHED_FROM_ZOOM = "launchedFromZoom";
    private static final int DELAY_WAIT_LOGIN = 5000;
    private static final String EXTRA_ACTION_SEND_INTENT;
    private static final String EXTRA_URI;
    private static final int SPLASH_TIME = 2000;
    private static final String TAG = "LauncherActivity";
    @NonNull
    private Runnable mCheckHandlerUrlRunnable = new Runnable() {
        public void run() {
            LauncherActivity.this.mHandler.removeCallbacksAndMessages(null);
            LauncherActivity launcherActivity = LauncherActivity.this;
            launcherActivity.handleActionURI(launcherActivity.getIntent());
            PTUI.getInstance().removePTUIListener(LauncherActivity.this);
            LauncherActivity.this.finish();
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableShowMainUILater = new Runnable() {
        public void run() {
            if (!LauncherActivity.this.showMainUIIfActive()) {
                LauncherActivity.this.showMainUILaterIfPossible();
            }
        }
    };

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(LauncherActivity.class.getName());
        sb.append(".action.ACTION_HANDLE_URI");
        ACTION_HANDLE_URI = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(LauncherActivity.class.getName());
        sb2.append(".action.ACTION_HANDLE_ACTION_SEND");
        ACTION_HANDLE_ACTION_SEND = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(LauncherActivity.class.getName());
        sb3.append(".extra.URI");
        EXTRA_URI = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(LauncherActivity.class.getName());
        sb4.append(".extra.ACTION_SEND_INTENT");
        EXTRA_ACTION_SEND_INTENT = sb4.toString();
    }

    public static void showLauncherActivity(ZMActivity zMActivity) {
        showLauncherActivity(zMActivity, null, null);
    }

    public static void showLauncherActivity(@Nullable ZMActivity zMActivity, @Nullable String str, @Nullable Bundle bundle) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, LauncherActivity.class);
            intent.addFlags(67108864);
            intent.putExtra(ARG_LAUNCHED_FROM_ZOOM, true);
            if (str != null) {
                intent.putExtra(ARG_ACTION_FOR_IM_ACTIVITY, str);
            }
            if (bundle != null) {
                intent.putExtra(ARG_EXTRAS_FOR_IM_ACTIVITY, bundle);
            }
            ActivityStartHelper.startActivityForeground(zMActivity, intent);
        }
    }

    public static void showLauncherActivityForUri(@Nullable ZMActivity zMActivity, String str) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, LauncherActivity.class);
            intent.setAction(ACTION_HANDLE_URI);
            intent.addFlags(67108864);
            intent.putExtra(EXTRA_URI, str);
            intent.putExtra(ARG_LAUNCHED_FROM_ZOOM, true);
            ActivityStartHelper.startActivityForeground(zMActivity, intent);
        }
    }

    public static void showLauncherActivityForActionSend(@Nullable ZMActivity zMActivity, Intent intent) {
        if (zMActivity != null) {
            Intent intent2 = new Intent(zMActivity, LauncherActivity.class);
            intent2.setAction(ACTION_HANDLE_ACTION_SEND);
            intent2.addFlags(67108864);
            intent2.putExtra(EXTRA_ACTION_SEND_INTENT, intent);
            intent2.putExtra(ARG_LAUNCHED_FROM_ZOOM, true);
            ActivityStartHelper.startActivityForeground(zMActivity, intent2);
        }
    }

    public static void showLauncherActivityAsFromHome(@Nullable Context context) {
        if (context != null) {
            Intent intent = new Intent(context, LauncherActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(270532608);
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        disableFinishActivityByGesture(true);
        if (isLaunchedFromHome() || isLaunchedFromZoom()) {
            if (UIUtil.getDisplayMinWidthInDip(this) < 520.0f) {
                setRequestedOrientation(1);
            }
            if (VideoBoxApplication.getInstance() == null) {
                Context applicationContext = getApplicationContext();
                VideoBoxApplication.initialize(getApplicationContext(), false, 0, null);
                if (!ZMUtils.isZoomApp(applicationContext) && (applicationContext instanceof ZoomApplication)) {
                    ZMThirdPartyUtils.checkShareCloudFileClientInfo(applicationContext, false);
                }
            }
            Mainboard mainboard = Mainboard.getMainboard();
            if (mainboard != null && !mainboard.isInitialized()) {
                setContentView(C4558R.layout.zm_splash);
            }
            return;
        }
        finish();
        showLauncherActivityAsFromHome(this);
    }

    private boolean isLaunchedFromHome() {
        int flags = getIntent().getFlags();
        return ((268435456 & flags) == 0 || (flags & 2097152) == 0) ? false : true;
    }

    private boolean isLaunchedFromZoom() {
        return getIntent().getBooleanExtra(ARG_LAUNCHED_FROM_ZOOM, false);
    }

    public void onResume() {
        super.onResume();
        Context applicationContext = getApplicationContext();
        if (!(applicationContext instanceof ZoomApplication) || !((ZoomApplication) applicationContext).isInitFailed()) {
            if (!RootCheckUtils.isRooted() || RootCheckUtils.continueToUseWhenRooted()) {
                checkAndContinue();
            } else {
                RootedWarningDialog.show(this);
            }
            return;
        }
        showDeviceNotSupported();
    }

    private void checkAndContinue() {
        if (checkSupported()) {
            Mainboard mainboard = Mainboard.getMainboard();
            if (mainboard != null) {
                if (mainboard.isInitialized()) {
                    showMainUIImediately();
                } else {
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            Mainboard mainboard = Mainboard.getMainboard();
                            if (mainboard != null && !mainboard.isInitialized()) {
                                long currentTimeMillis = System.currentTimeMillis();
                                try {
                                    VideoBoxApplication.getNonNullInstance().initPTMainboard();
                                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                                    LauncherActivity.this.autoLoginIfPossible();
                                    long j = 2000 - currentTimeMillis2;
                                    if (j < 0) {
                                        j = 0;
                                    }
                                    LauncherActivity.this.showMainUIDelayed(j);
                                } catch (UnsatisfiedLinkError unused) {
                                    LauncherActivity.this.showDeviceNotSupported();
                                    LauncherActivity.this.mHandler.removeCallbacks(LauncherActivity.this.mRunnableShowMainUILater);
                                }
                            }
                        }
                    }, 200);
                    showMainUILaterIfPossible();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void showDeviceNotSupported() {
        if (ZMUtils.isZoomApp(getApplicationContext())) {
            new Builder(this).setMessage(C4558R.string.zm_alert_link_error_content_106299).setTitle(C4558R.string.zm_alert_link_error_title_106299).setCancelable(false).setVerticalOptionStyle(true).setNegativeButton(C4558R.string.zm_date_time_cancel, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    LauncherActivity.this.finish();
                    VideoBoxApplication.getNonNullInstance().exit();
                }
            }).setPositiveButton(C4558R.string.zm_alert_link_error_btn_106299, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UpgradeUtil.upgradeByUrl(LauncherActivity.this, true);
                    LauncherActivity.this.finish();
                    VideoBoxApplication.getNonNullInstance().exit();
                }
            }).show();
        }
    }

    /* access modifiers changed from: private */
    public void showMainUILaterIfPossible() {
        this.mHandler.postDelayed(this.mRunnableShowMainUILater, 1000);
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this);
        this.mHandler.removeCallbacks(this.mCheckHandlerUrlRunnable);
        this.mHandler.removeCallbacks(this.mRunnableShowMainUILater);
    }

    /* access modifiers changed from: private */
    public void showMainUIDelayed(long j) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                LauncherActivity.this.showMainUIIfActive();
            }
        }, j);
    }

    /* access modifiers changed from: private */
    public boolean showMainUIIfActive() {
        if (!isActive()) {
            return false;
        }
        showMainUIImediately();
        return true;
    }

    /* access modifiers changed from: private */
    public void runOnLogin(@NonNull final Runnable runnable, final long j) {
        if (j <= 0 || !PTApp.getInstance().isAuthenticating()) {
            runnable.run();
            return;
        }
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                LauncherActivity.this.runOnLogin(runnable, j - 200);
            }
        }, 200);
    }

    private void showMainUIImediately() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_HANDLE_URI.equals(action)) {
                boolean z = false;
                String stringExtra = intent.getStringExtra(EXTRA_URI);
                if (!StringUtil.isEmptyOrNull(stringExtra) && PTApp.getInstance().parseZoomAction(stringExtra) == 8) {
                    Mainboard.getMainboard().notifyUrlAction(stringExtra);
                    z = true;
                }
                if (!z) {
                    runOnLogin(this.mCheckHandlerUrlRunnable, 5000);
                }
            } else if (ACTION_HANDLE_ACTION_SEND.equals(action)) {
                handleActionSend(intent);
                finish();
            } else {
                welcomeActivityShow(intent);
            }
        }
    }

    private void welcomeActivityShow(Intent intent) {
        Bundle bundle = null;
        String stringExtra = intent != null ? intent.getStringExtra(ARG_ACTION_FOR_IM_ACTIVITY) : null;
        if (intent != null) {
            bundle = intent.getBundleExtra(ARG_EXTRAS_FOR_IM_ACTIVITY);
        }
        WelcomeActivity.show(this, false, true, stringExtra, bundle);
        overridePendingTransition(0, 0);
        finish();
    }

    /* access modifiers changed from: private */
    public void handleActionURI(Intent intent) {
        PTUI.getInstance().removePTUIListener(this);
        String stringExtra = intent.getStringExtra(EXTRA_URI);
        if (stringExtra != null) {
            Intent intent2 = new Intent(this, JoinByURLActivity.class);
            intent2.setData(Uri.parse(stringExtra));
            ActivityStartHelper.startActivityForeground(this, intent2);
            intent.removeExtra(EXTRA_URI);
        }
    }

    private void handleActionSend(Intent intent) {
        Intent intent2 = (Intent) intent.getParcelableExtra(EXTRA_ACTION_SEND_INTENT);
        if (intent2 != null) {
            WelcomeActivity.showForActionSend(this, intent2);
            intent.removeExtra(EXTRA_ACTION_SEND_INTENT);
        }
    }

    /* access modifiers changed from: private */
    public boolean autoLoginIfPossible() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            return false;
        }
        PTUI.getInstance().addPTUIListener(this);
        if (PTApp.getInstance().getPTLoginType() == 0) {
            AuthToken session = FBSessionStore.getSession(this, FBSessionStore.FACEBOOK_KEY);
            if (session.isSessionValid() && !session.shouldExtendAccessToken()) {
                return PTApp.getInstance().autoSignin();
            }
        } else if (!(PTApp.getInstance().getPTLoginType() == 102 || PTApp.getInstance().getPTLoginType() == 97)) {
            return PTApp.getInstance().autoSignin();
        }
        return false;
    }

    private boolean checkSupported() {
        ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        if (activityManager == null) {
            return false;
        }
        if ((activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 131072) && !Build.CPU_ABI.equals("armeabi") && !Build.CPU_ABI.startsWith("armeabi-v6")) {
            return true;
        }
        new Builder(this).setTitle(C4558R.string.zm_app_name).setMessage(C4558R.string.zm_msg_devices_not_supported).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                LauncherActivity.this.finish();
            }
        }).show();
        return false;
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0 || i == 37) {
            sinkWebLoginResult();
        }
    }

    private void sinkWebLoginResult() {
        getNonNullEventTaskManagerOrThrowException().push("sinkWebLoginResult", new EventAction("sinkWebLoginResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((LauncherActivity) iUIElement).showMainUIIfActive();
            }
        });
    }

    public void onConfirm() {
        RootCheckUtils.setContinueToUseWhenRooted(true);
        checkAndContinue();
    }

    public void onCancel() {
        RootCheckUtils.setContinueToUseWhenRooted(false);
        finish();
        killAllProcessAndExit();
    }

    private void killAllProcessAndExit() {
        VideoBoxApplication.getInstance().killProcess(this, 1);
        VideoBoxApplication.getInstance().killProcess(this, 3);
        VideoBoxApplication.getInstance().killProcess(this, 2);
        VideoBoxApplication.getInstance().killProcess(this, 0);
    }
}
