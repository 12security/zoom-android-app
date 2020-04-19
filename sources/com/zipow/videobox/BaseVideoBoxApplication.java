package com.zipow.videobox;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.common.p008pt.ILeaveConfCallBack;
import com.zipow.videobox.common.p008pt.ZMKillConfInPtRunnable;
import com.zipow.videobox.ptapp.PTApp;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class BaseVideoBoxApplication extends ContextWrapper {
    private static final String TAG = "com.zipow.videobox.BaseVideoBoxApplication";
    private static Handler g_handler = new Handler();
    private static ZMKillConfInPtRunnable mKillConfInPt = new ZMKillConfInPtRunnable();
    @Nullable
    protected String mConfProcessExtName = "conf";
    private boolean mDirectKillConfProcess = false;
    protected boolean mIsConfProcessDeathLinked = false;
    protected transient boolean mbSDKMode = false;

    protected class ConfProcessDeathHandler implements DeathRecipient {
        private IBinder mCb;

        public ConfProcessDeathHandler(IBinder iBinder) {
            this.mCb = iBinder;
        }

        public void binderDied() {
            BaseVideoBoxApplication.this.mIsConfProcessDeathLinked = false;
        }

        public IBinder getBinder() {
            return this.mCb;
        }
    }

    public BaseVideoBoxApplication(Context context) {
        super(context);
    }

    public boolean isSDKMode() {
        return this.mbSDKMode;
    }

    public boolean isMultiProcess() {
        return !this.mbSDKMode;
    }

    public void stopConfProcessDirect() {
        this.mDirectKillConfProcess = true;
    }

    public boolean isConfProcessRunning() {
        boolean z = true;
        if (isSDKMode()) {
            if (getConfProcessId() <= 0 && !this.mIsConfProcessDeathLinked) {
                z = false;
            }
            return z;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getPackageName());
        sb.append(":");
        sb.append(this.mConfProcessExtName);
        if (getPidByName(this, sb.toString()) <= 0) {
            z = false;
        }
        return z;
    }

    public int getConfProcessId() {
        FileInputStream fileInputStream;
        File filesDir = getFilesDir();
        if (filesDir == null) {
            return -1;
        }
        String absolutePath = filesDir.getAbsolutePath();
        if (!absolutePath.endsWith("/")) {
            StringBuilder sb = new StringBuilder();
            sb.append(absolutePath);
            sb.append("/");
            absolutePath = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(absolutePath);
        sb2.append("conf_process_id");
        File file = new File(sb2.toString());
        if (!file.exists()) {
            return -1;
        }
        try {
            fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            int parseInt = Integer.parseInt(new String(bArr));
            fileInputStream.close();
            return parseInt;
        } catch (Exception unused) {
            return -1;
        } catch (Throwable th) {
            r2.addSuppressed(th);
        }
        throw th;
    }

    public void killConfInPtForWait(@Nullable ILeaveConfCallBack iLeaveConfCallBack, boolean z) {
        this.mDirectKillConfProcess = false;
        g_handler.removeCallbacks(mKillConfInPt);
        mKillConfInPt.init(iLeaveConfCallBack, z);
        killConfInPt(iLeaveConfCallBack, mKillConfInPt, (long) ZMConfiguration.DURATION_WAIT_KILL_PROCESS, (long) ZMConfiguration.DURATION_WAIT_KILL_PROCESS_INTERVAL);
    }

    /* access modifiers changed from: private */
    public void killConfInPt(@Nullable ILeaveConfCallBack iLeaveConfCallBack, @NonNull Runnable runnable, long j, long j2) {
        if (!isConfProcessRunning()) {
            PTApp.getInstance().dispatchIdleMessage();
            if (iLeaveConfCallBack != null) {
                iLeaveConfCallBack.onLeaveComplete();
            }
            return;
        }
        if (j <= 0) {
            if (this.mDirectKillConfProcess) {
                runnable.run();
            }
        }
        Handler handler = g_handler;
        final ILeaveConfCallBack iLeaveConfCallBack2 = iLeaveConfCallBack;
        final Runnable runnable2 = runnable;
        final long j3 = j;
        final long j4 = j2;
        C18871 r1 = new Runnable() {
            public void run() {
                BaseVideoBoxApplication baseVideoBoxApplication = BaseVideoBoxApplication.this;
                ILeaveConfCallBack iLeaveConfCallBack = iLeaveConfCallBack2;
                Runnable runnable = runnable2;
                long j = j3;
                long j2 = j4;
                baseVideoBoxApplication.killConfInPt(iLeaveConfCallBack, runnable, j - j2, j2);
            }
        };
        handler.postDelayed(r1, j2);
    }

    private static int getPidByName(Context context, @NonNull String str) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        int i = -1;
        if (activityManager != null) {
            List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return 0;
            }
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo != null && str.equals(runningAppProcessInfo.processName)) {
                    i = runningAppProcessInfo.pid;
                }
            }
        }
        return i;
    }
}
