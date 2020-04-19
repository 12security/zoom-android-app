package com.zipow.cmmlib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CmmTimer {
    private static final String TAG = "CmmTimer";
    private static ScheduledExecutorService sExecutorService;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mNativeCaller = new Runnable() {
        public void run() {
            System.currentTimeMillis();
            if (CmmTimer.this.mNativeTimerProc != 0) {
                CmmTimer cmmTimer = CmmTimer.this;
                cmmTimer.callNativeTimerProc(cmmTimer.mNativeTimerProc, CmmTimer.this);
                System.currentTimeMillis();
            }
        }
    };
    /* access modifiers changed from: private */
    public long mNativeTimerProc = 0;
    @Nullable
    private ScheduledFuture<?> mTimer = null;

    /* access modifiers changed from: private */
    public native void callNativeTimerProc(long j, CmmTimer cmmTimer);

    public void setTimer(long j, long j2) {
        synchronized (CmmTimer.class) {
            if (sExecutorService == null) {
                sExecutorService = Executors.newScheduledThreadPool(1);
            }
        }
        if (this.mTimer == null) {
            this.mNativeTimerProc = j2;
            this.mTimer = sExecutorService.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    VideoBoxApplication.getInstance().runOnMainThread(CmmTimer.this.mNativeCaller);
                }
            }, j, j, TimeUnit.MILLISECONDS);
        }
    }

    public void killTimer() {
        ScheduledFuture<?> scheduledFuture = this.mTimer;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            this.mTimer = null;
            this.mNativeTimerProc = 0;
        }
    }
}
