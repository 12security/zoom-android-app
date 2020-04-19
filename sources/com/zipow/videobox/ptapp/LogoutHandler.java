package com.zipow.videobox.ptapp;

import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class LogoutHandler {
    private static final long BEFORE_LOGOUT_TIME_OUT = 3000;
    private static LogoutHandler mInstance;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mLogoutRunnable;
    @NonNull
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, long j) {
            if (i == 66) {
                LogoutHandler.this.doLogout();
                if (LogoutHandler.this.mLogoutRunnable != null) {
                    LogoutHandler.this.mHandler.removeCallbacks(LogoutHandler.this.mLogoutRunnable);
                    LogoutHandler.this.mLogoutRunnable = null;
                }
            }
        }
    };

    public interface IListener {
        void afterLogout();
    }

    public static LogoutHandler getInstance() {
        if (mInstance == null) {
            mInstance = new LogoutHandler();
        }
        return mInstance;
    }

    private LogoutHandler() {
    }

    private void showWaitingDialog(FragmentManager fragmentManager, boolean z) {
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(z);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    /* access modifiers changed from: private */
    public void dismissWaitingDialog(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void startLogout(final ZMActivity zMActivity, final IListener iListener) {
        if (PTApp.getInstance().beforeLogout()) {
            PTUI.getInstance().addPTUIListener(this.mPTUIListener);
            Runnable runnable = this.mLogoutRunnable;
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
            }
            if (zMActivity != null) {
                showWaitingDialog(zMActivity.getSupportFragmentManager(), true);
            }
            this.mLogoutRunnable = new Runnable() {
                public void run() {
                    ZMActivity zMActivity = zMActivity;
                    if (zMActivity != null) {
                        LogoutHandler.this.dismissWaitingDialog(zMActivity.getSupportFragmentManager());
                    }
                    if (PTApp.getInstance().isWebSignedOn() || PTApp.getInstance().isAuthenticating()) {
                        LogoutHandler.this.doLogout();
                        IListener iListener = iListener;
                        if (iListener != null) {
                            iListener.afterLogout();
                        }
                    }
                }
            };
            this.mHandler.postDelayed(this.mLogoutRunnable, BEFORE_LOGOUT_TIME_OUT);
            return;
        }
        doLogout();
        if (iListener != null) {
            iListener.afterLogout();
        }
    }

    public void startLogout() {
        startLogout(null, null);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0035  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void doLogout() {
        /*
            r4 = this;
            com.zipow.videobox.ptapp.PTUI r0 = com.zipow.videobox.ptapp.PTUI.getInstance()
            com.zipow.videobox.ptapp.PTUI$IPTUIListener r1 = r4.mPTUIListener
            r0.removePTUIListener(r1)
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            if (r0 != 0) goto L_0x0010
            return
        L_0x0010:
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            int r1 = r1.getPTLoginType()
            r2 = 101(0x65, float:1.42E-43)
            r3 = 0
            if (r1 != r2) goto L_0x002b
            android.webkit.WebViewDatabase r1 = android.webkit.WebViewDatabase.getInstance(r0)
            if (r1 == 0) goto L_0x002b
            boolean r1 = r1.hasHttpAuthUsernamePassword()
            if (r1 == 0) goto L_0x002b
            r1 = 1
            goto L_0x002c
        L_0x002b:
            r1 = 0
        L_0x002c:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            r2.logout(r3)
            if (r1 == 0) goto L_0x0050
            us.zoom.thirdparty.login.LoginType r1 = p021us.zoom.thirdparty.login.LoginType.Sso
            android.os.Bundle r2 = p021us.zoom.thirdparty.login.ThirdPartyLoginFactory.buildEmptySsoBundle()
            us.zoom.thirdparty.login.ThirdPartyLogin r1 = p021us.zoom.thirdparty.login.ThirdPartyLoginFactory.build(r1, r2)
            com.zipow.videobox.VideoBoxApplication r2 = com.zipow.videobox.VideoBoxApplication.getInstance()
            r1.logout(r2)
            com.zipow.videobox.LauncherActivity.showLauncherActivityAsFromHome(r0)
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            r0.exit()
        L_0x0050:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.LogoutHandler.doLogout():void");
    }
}
