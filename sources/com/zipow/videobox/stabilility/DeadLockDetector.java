package com.zipow.videobox.stabilility;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.PreferenceUtil;
import java.io.File;
import java.io.PrintStream;
import java.util.Map.Entry;
import p021us.zoom.androidlib.app.ZMActivity;

public class DeadLockDetector {
    private static final long CHECK_PERIOD = 5000;
    private static final long DEADLOCK_CHECK_THRESHOLD = 30000;
    private static final long HEARTBEAT_PERIOD = 3000;
    private final String TAG = DeadLockDetector.class.getSimpleName();
    private long mCurrentTime = 0;
    @Nullable
    private DeadLockDetectorThread mDetectThread;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    @Nullable
    public File mLastDeadlockLogFile;
    /* access modifiers changed from: private */
    public long mLastHeartbeatTime = 0;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mRunnable;
    /* access modifiers changed from: private */
    public boolean mRunning = false;

    class DeadLockDetectorThread extends Thread {
        private long mLastDeadLockStartTime = 0;

        public DeadLockDetectorThread() {
            super("DeadLockDetector");
        }

        public void run() {
            while (DeadLockDetector.this.mRunning) {
                try {
                    sleep(DeadLockDetector.CHECK_PERIOD);
                    DeadLockDetector.this.increaseCurrentTime(DeadLockDetector.CHECK_PERIOD);
                    if (!JavaCrashHandler.hasJavaThreadCrashed()) {
                        if (!Mainboard.isNativeCrashed()) {
                            checkDeadLock();
                        }
                    }
                    DeadLockDetector.this.stop();
                } catch (InterruptedException unused) {
                }
            }
        }

        private boolean isProcessAtFront() {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            return frontActivity != null && frontActivity.isActive();
        }

        private void checkDeadLock() {
            long access$100 = DeadLockDetector.this.getCurrentTime();
            if (!isProcessAtFront()) {
                DeadLockDetector.this.mLastHeartbeatTime = access$100;
                return;
            }
            if (access$100 - DeadLockDetector.this.mLastHeartbeatTime > DeadLockDetector.DEADLOCK_CHECK_THRESHOLD && DeadLockDetector.this.mLastHeartbeatTime != this.mLastDeadLockStartTime) {
                this.mLastDeadLockStartTime = DeadLockDetector.this.mLastHeartbeatTime;
                DeadLockDetector.this.onDeadLock(this);
            }
        }
    }

    public DeadLockDetector() {
        if (Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()) {
            this.mHandler = new Handler();
            this.mRunnable = new Runnable() {
                public void run() {
                    DeadLockDetector deadLockDetector = DeadLockDetector.this;
                    deadLockDetector.mLastHeartbeatTime = deadLockDetector.getCurrentTime();
                    if (DeadLockDetector.this.mLastDeadlockLogFile != null && DeadLockDetector.this.mLastDeadlockLogFile.exists()) {
                        DeadLockDetector.this.mLastDeadlockLogFile.delete();
                        DeadLockDetector.this.mLastDeadlockLogFile = null;
                        PreferenceUtil.removeValue(PreferenceUtil.CAMERA_IS_FREEZED);
                    }
                    if (DeadLockDetector.this.mRunning && DeadLockDetector.this.mRunnable != null) {
                        DeadLockDetector.this.mHandler.postDelayed(DeadLockDetector.this.mRunnable, DeadLockDetector.HEARTBEAT_PERIOD);
                    }
                }
            };
            return;
        }
        throw new RuntimeException("Not called from main thread");
    }

    /* access modifiers changed from: private */
    public long getCurrentTime() {
        return this.mCurrentTime;
    }

    /* access modifiers changed from: private */
    public void increaseCurrentTime(long j) {
        this.mCurrentTime += j;
    }

    public void start() {
        this.mRunning = true;
        this.mLastHeartbeatTime = getCurrentTime();
        Runnable runnable = this.mRunnable;
        if (runnable != null) {
            this.mHandler.postDelayed(runnable, HEARTBEAT_PERIOD);
        }
        this.mDetectThread = new DeadLockDetectorThread();
        this.mDetectThread.start();
    }

    public void stop() {
        this.mRunning = false;
        if (this.mDetectThread.isAlive()) {
            this.mDetectThread.interrupt();
        }
        this.mDetectThread = null;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x013a A[SYNTHETIC, Splitter:B:34:0x013a] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0146 A[SYNTHETIC, Splitter:B:42:0x0146] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onDeadLock(@androidx.annotation.NonNull java.lang.Thread r10) {
        /*
            r9 = this;
            boolean r0 = r9.isCameraAPIFreezed()
            if (r0 == 0) goto L_0x0016
            java.lang.String r0 = "camera_is_freezed"
            r1 = 0
            boolean r0 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r0, r1)
            if (r0 == 0) goto L_0x0010
            return
        L_0x0010:
            java.lang.String r0 = "camera_is_freezed"
            r1 = 1
            com.zipow.videobox.util.PreferenceUtil.saveBooleanValue(r0, r1)
        L_0x0016:
            int r0 = android.os.Process.myPid()
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r1 = r1.isConfApp()
            if (r1 == 0) goto L_0x0027
            java.lang.String r1 = "zVideoApp"
            goto L_0x0029
        L_0x0027:
            java.lang.String r1 = "zChatApp"
        L_0x0029:
            java.lang.String r2 = "freeze-"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "-"
            r3.append(r4)
            r3.append(r1)
            java.lang.String r1 = "-"
            r3.append(r1)
            r3.append(r0)
            java.lang.String r0 = ".log"
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r1 = 5
            long r3 = java.lang.System.currentTimeMillis()
            java.io.File r0 = com.zipow.videobox.util.LogUtil.getNewLogFile(r2, r0, r1, r3)
            if (r0 != 0) goto L_0x0055
            return
        L_0x0055:
            r1 = 0
            java.io.PrintStream r2 = new java.io.PrintStream     // Catch:{ Exception -> 0x0143, all -> 0x0136 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0143, all -> 0x0136 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.<init>()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "version: "
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            com.zipow.videobox.VideoBoxApplication r3 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = r3.getVersionName()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r2.println(r1)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.<init>()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "Kernel Version: "
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            com.zipow.videobox.VideoBoxApplication r3 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = r3.getKernelVersion()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r2.println(r1)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.<init>()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "OS: "
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = com.zipow.videobox.ptapp.SystemInfoHelper.getOSInfo()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r2.println(r1)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.<init>()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "Hardware: "
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = com.zipow.videobox.ptapp.SystemInfoHelper.getHardwareInfo()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r2.println(r1)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            boolean r1 = r1.isConfApp()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            if (r1 == 0) goto L_0x011e
            com.zipow.videobox.confapp.ConfMgr r1 = com.zipow.videobox.confapp.ConfMgr.getInstance()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            com.zipow.videobox.confapp.CmmConfContext r1 = r1.getConfContext()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            if (r1 == 0) goto L_0x011e
            java.lang.String r3 = r1.getMeetingId()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            long r4 = r1.getConfNumber()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            com.zipow.videobox.confapp.ConfMgr r1 = com.zipow.videobox.confapp.ConfMgr.getInstance()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            com.zipow.videobox.confapp.CmmUser r1 = r1.getMyself()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            if (r1 == 0) goto L_0x00ee
            long r6 = r1.getNodeId()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            goto L_0x00f0
        L_0x00ee:
            r6 = 0
        L_0x00f0:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.<init>()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r8 = "MeetingId:"
            r1.append(r8)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "; "
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "MeetingNo:"
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r4)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "; "
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r3 = "NodeId:"
            r1.append(r3)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r1.append(r6)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r2.println(r1)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
        L_0x011e:
            r2.println()     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r1 = "=====================Start to print dead lock stacks==================="
            r2.println(r1)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r9.printThreadsStacks(r10, r2)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            java.lang.String r10 = "=======================Dead lock stacks till here======================"
            r2.println(r10)     // Catch:{ Exception -> 0x0134, all -> 0x0132 }
            r2.flush()     // Catch:{ Exception -> 0x014c }
            goto L_0x0149
        L_0x0132:
            r10 = move-exception
            goto L_0x0138
        L_0x0134:
            goto L_0x0144
        L_0x0136:
            r10 = move-exception
            r2 = r1
        L_0x0138:
            if (r2 == 0) goto L_0x0140
            r2.flush()     // Catch:{ Exception -> 0x0140 }
            r2.close()     // Catch:{ Exception -> 0x0140 }
        L_0x0140:
            r9.mLastDeadlockLogFile = r0
            throw r10
        L_0x0143:
            r2 = r1
        L_0x0144:
            if (r2 == 0) goto L_0x014c
            r2.flush()     // Catch:{ Exception -> 0x014c }
        L_0x0149:
            r2.close()     // Catch:{ Exception -> 0x014c }
        L_0x014c:
            r9.mLastDeadlockLogFile = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.stabilility.DeadLockDetector.onDeadLock(java.lang.Thread):void");
    }

    private boolean isCameraAPIFreezed() {
        for (Entry value : Thread.getAllStackTraces().entrySet()) {
            StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) value.getValue();
            if (stackTraceElementArr != null && stackTraceElementArr.length >= 1 && "android.hardware.Camera".equals(stackTraceElementArr[0].getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void printThreadsStacks(@NonNull Thread thread, @NonNull PrintStream printStream) {
        for (Entry entry : Thread.getAllStackTraces().entrySet()) {
            Thread thread2 = (Thread) entry.getKey();
            if (thread2.getId() != thread.getId()) {
                printThreadStack(thread2, (StackTraceElement[]) entry.getValue(), printStream);
            }
        }
    }

    private void printThreadStack(Thread thread, StackTraceElement[] stackTraceElementArr, PrintStream printStream) {
        printStream.println(thread.toString());
        for (StackTraceElement stackTraceElement : stackTraceElementArr) {
            printStream.println(stackTraceElement.toString());
        }
        printStream.println();
    }
}
