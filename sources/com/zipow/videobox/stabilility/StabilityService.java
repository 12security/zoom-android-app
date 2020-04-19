package com.zipow.videobox.stabilility;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.IPTService.Stub;
import com.zipow.videobox.PTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ZMBaseService;
import com.zipow.videobox.mainboard.Mainboard;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

public class StabilityService extends ZMBaseService {
    public static final String ACTION_LOG_CRASH;
    public static final String ACTION_NEW_CRASH_INFO;
    public static final String ACTION_PROTECT_PT;
    public static final String ARG_BAA_SECURITY_ENABLED = "BAASecurityEnabled";
    public static final String ARG_MEETING_INFO = "meetingInfo";
    public static final String ARG_MEM_CPU = "memCpu";
    public static final String ARG_PID = "pid";
    private static final String TAG = "StabilityService";
    /* access modifiers changed from: private */
    public boolean mLastCrashInfo_BAASecurityEnabled = false;
    @Nullable
    private String mLastCrashedInfo_meetingInfo = null;
    @Nullable
    private String mLastCrashedInfo_memCpu = null;
    private int mLastCrashedInfo_pid = 0;
    private LogMonitorThread mLogMonitorThread;
    @Nullable
    private IPTService mPTService;
    @Nullable
    private ServiceConnection mPTServiceConnection;
    private boolean mbNeedProtectPT = false;

    static class LogMonitorThread extends Thread {
        @NonNull
        private String crashLogKeyword;
        @Nullable
        private StabilityService mService = null;
        private boolean mStopped = false;

        public LogMonitorThread(@Nullable StabilityService stabilityService) {
            super(LogMonitorThread.class.getSimpleName());
            StringBuilder sb = new StringBuilder();
            sb.append(">>> ");
            sb.append(AppUtil.getAppPackageName());
            this.crashLogKeyword = sb.toString();
            this.mService = stabilityService;
        }

        public void stopMonitor() {
            this.mStopped = true;
            interrupt();
        }

        public void run() {
            while (!this.mStopped) {
                monitor();
                try {
                    sleep(2000);
                } catch (InterruptedException unused) {
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00ce, code lost:
            r0 = th;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x00ac */
        /* JADX WARNING: Removed duplicated region for block: B:58:0x00ce A[ExcHandler: all (th java.lang.Throwable), Splitter:B:29:0x006e] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void monitor() {
            /*
                r14 = this;
                r0 = 0
                java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                java.lang.String r2 = "logcat"
                java.lang.String r3 = "-c"
                java.lang.String[] r2 = new java.lang.String[]{r2, r3}     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                r1.exec(r2)     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                r1 = 1000(0x3e8, double:4.94E-321)
                sleep(r1)     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                java.lang.String r2 = "logcat"
                java.lang.String r3 = "-vthreadtime"
                java.lang.String r4 = "DEBUG:I *:S"
                java.lang.String[] r2 = new java.lang.String[]{r2, r3, r4}     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                java.lang.Process r1 = r1.exec(r2)     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                r2 = 1
                if (r1 != 0) goto L_0x0033
                r14.mStopped = r2     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
                return
            L_0x0033:
                java.io.InputStream r1 = r1.getInputStream()     // Catch:{ Exception -> 0x00e1, all -> 0x00d6 }
                if (r1 != 0) goto L_0x0042
                r14.mStopped = r2     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r0)
                return
            L_0x0042:
                java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
                java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
                r4.<init>(r1)     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
                r3.<init>(r4)     // Catch:{ Exception -> 0x00d4, all -> 0x00d0 }
                r4 = 0
                r6 = r0
                r5 = 0
            L_0x004f:
                java.lang.String r7 = r3.readLine()     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r7 != 0) goto L_0x0062
                r1.close()     // Catch:{ Exception -> 0x005b, all -> 0x00ce }
                r3.close()     // Catch:{ Exception -> 0x005b, all -> 0x00ce }
            L_0x005b:
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r3)
                return
            L_0x0062:
                java.lang.String r8 = r14.crashLogKeyword     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                int r8 = r7.indexOf(r8)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r8 <= 0) goto L_0x00b9
                java.lang.String r5 = r14.parsePid(r7)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                int r9 = java.lang.Integer.parseInt(r5)     // Catch:{ Exception -> 0x00ac, all -> 0x00ce }
                java.lang.String r10 = r14.parseProcessType(r7)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                java.lang.String r5 = "zVideoApp"
                boolean r5 = r5.equals(r10)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r5 == 0) goto L_0x008a
                com.zipow.videobox.stabilility.StabilityService r5 = r14.mService     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r5 == 0) goto L_0x008a
                com.zipow.videobox.stabilility.StabilityService r5 = r14.mService     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                java.lang.String r5 = r5.getZoomProcessMeetingInfoInString(r9)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                r11 = r5
                goto L_0x008b
            L_0x008a:
                r11 = r0
            L_0x008b:
                com.zipow.videobox.stabilility.StabilityService r5 = r14.mService     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r5 == 0) goto L_0x009e
                com.zipow.videobox.stabilility.StabilityService r5 = r14.mService     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                java.lang.String r5 = r5.getZoomProcessMemInfoInString(r9)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                com.zipow.videobox.stabilility.StabilityService r6 = r14.mService     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                boolean r6 = r6.mLastCrashInfo_BAASecurityEnabled     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                r12 = r5
                r13 = r6
                goto L_0x00a0
            L_0x009e:
                r12 = r0
                r13 = 0
            L_0x00a0:
                com.zipow.videobox.stabilility.StabilityService$WriteLogFileThread r5 = new com.zipow.videobox.stabilility.StabilityService$WriteLogFileThread     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                r8 = r5
                r8.<init>(r9, r10, r11, r12, r13)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                r5.start()     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                r6 = r5
                r5 = 1
                goto L_0x00b9
            L_0x00ac:
                r1.close()     // Catch:{ Exception -> 0x00b2, all -> 0x00ce }
                r3.close()     // Catch:{ Exception -> 0x00b2, all -> 0x00ce }
            L_0x00b2:
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r3)
                return
            L_0x00b9:
                if (r5 == 0) goto L_0x00c9
                if (r6 == 0) goto L_0x00c9
                boolean r8 = r6.isStopped()     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r8 == 0) goto L_0x00c6
                r6 = r0
                r5 = 0
                goto L_0x00c9
            L_0x00c6:
                r6.writeLine(r7)     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
            L_0x00c9:
                boolean r7 = r14.mStopped     // Catch:{ Exception -> 0x00e3, all -> 0x00ce }
                if (r7 == 0) goto L_0x004f
                goto L_0x00e3
            L_0x00ce:
                r0 = move-exception
                goto L_0x00da
            L_0x00d0:
                r2 = move-exception
                r3 = r0
                r0 = r2
                goto L_0x00da
            L_0x00d4:
                r3 = r0
                goto L_0x00e3
            L_0x00d6:
                r1 = move-exception
                r3 = r0
                r0 = r1
                r1 = r3
            L_0x00da:
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r3)
                throw r0
            L_0x00e1:
                r1 = r0
                r3 = r1
            L_0x00e3:
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r1)
                p021us.zoom.androidlib.cache.IoUtils.closeSilently(r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.stabilility.StabilityService.LogMonitorThread.monitor():void");
        }

        @NonNull
        private String parsePid(@Nullable String str) {
            if (str == null) {
                return "0";
            }
            int indexOf = str.indexOf("pid:");
            if (indexOf < 0) {
                return "0";
            }
            int i = indexOf + 4;
            int indexOf2 = str.indexOf(PreferencesConstants.COOKIE_DELIMITER, i);
            if (indexOf2 < 0) {
                indexOf2 = str.indexOf(this.crashLogKeyword);
            }
            if (indexOf2 < 0) {
                return "0";
            }
            return str.substring(i, indexOf2).trim();
        }

        private String parseProcessType(@Nullable String str) {
            if (str == null) {
                return "";
            }
            int indexOf = str.indexOf(">>>");
            if (indexOf < 0) {
                return "";
            }
            int i = indexOf + 4;
            int indexOf2 = str.indexOf("<<<", i);
            if (indexOf2 < 0) {
                return "";
            }
            return str.substring(i, indexOf2).trim().endsWith(":conf") ? Mainboard.CONF_MAINBOARD_NAME : Mainboard.PT_MAINBOARD_NAME;
        }
    }

    static class WriteLogFileThread extends Thread {
        @Nullable
        private ByteArrayOutputStream mBos = null;
        private boolean mHasStackInfo = false;
        private boolean mIsBAASecurityEnabled = false;
        @Nullable
        private String mMeetingInfo = null;
        private String mMemInfo;
        private int mPid = 0;
        private String mProcessType = "";
        private boolean mStackEnd = false;
        private long mStartTime = 0;
        private boolean mStopped = false;
        @Nullable
        private BufferedWriter mWriter = null;

        public WriteLogFileThread(int i, String str, @Nullable String str2, String str3, boolean z) {
            super(WriteLogFileThread.class.getSimpleName());
            this.mPid = i;
            this.mProcessType = str;
            this.mStartTime = System.currentTimeMillis();
            this.mMeetingInfo = str2;
            this.mMemInfo = str3;
            this.mIsBAASecurityEnabled = z;
            this.mBos = new ByteArrayOutputStream();
            this.mWriter = new BufferedWriter(new OutputStreamWriter(this.mBos));
        }

        public synchronized void writeLine(@NonNull String str) {
            if (this.mWriter != null) {
                if (!this.mHasStackInfo && str.indexOf("#00  pc") > 0) {
                    this.mHasStackInfo = true;
                }
                if (!this.mStackEnd && str.indexOf("code around pc:") > 0) {
                    this.mStackEnd = true;
                }
                try {
                    this.mWriter.write(str);
                    this.mWriter.write(10);
                } catch (Exception unused) {
                }
            }
        }

        public boolean isStopped() {
            return this.mStopped;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(8:14|15|(2:17|18)|19|20|21|22|(2:24|25)(10:26|(1:28)(1:29)|30|31|(1:33)|34|(1:36)|37|38|39)) */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0085, code lost:
            if (r0 == null) goto L_0x00c1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0087, code lost:
            r1 = new java.lang.StringBuilder();
            r1.append(com.zipow.cmmlib.AppUtil.getLogParentPath());
            r1.append("/logs");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x00a2, code lost:
            if (com.zipow.videobox.util.LogUtil.isSameCrashReported(r1.toString(), r0, "crash-native-") == false) goto L_0x00c1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00a4, code lost:
            r3 = new java.lang.StringBuilder();
            r3.append(r0.getAbsolutePath());
            r3.append(".sent");
            r0.renameTo(new java.io.File(r3.toString()));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x00c1, code lost:
            r12.mStopped = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c3, code lost:
            return;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x001b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0020 */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0024 A[Catch:{ Exception -> 0x0081, all -> 0x007b }, DONT_GENERATE] */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x0026 A[Catch:{ Exception -> 0x0081, all -> 0x007b }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r12 = this;
                r0 = 0
            L_0x0001:
                r1 = 1000(0x3e8, double:4.94E-321)
                sleep(r1)     // Catch:{ InterruptedException -> 0x0006 }
            L_0x0006:
                monitor-enter(r12)
                boolean r1 = r12.mStackEnd     // Catch:{ all -> 0x00c7 }
                monitor-exit(r12)     // Catch:{ all -> 0x00c7 }
                r2 = 1
                if (r1 != 0) goto L_0x0011
                int r0 = r0 + r2
                r1 = 3
                if (r0 < r1) goto L_0x0001
            L_0x0011:
                monitor-enter(r12)
                int r0 = r12.mPid     // Catch:{ all -> 0x00c4 }
                if (r0 <= 0) goto L_0x001b
                int r0 = r12.mPid     // Catch:{ Exception -> 0x001b }
                android.os.Process.killProcess(r0)     // Catch:{ Exception -> 0x001b }
            L_0x001b:
                java.io.BufferedWriter r0 = r12.mWriter     // Catch:{ Exception -> 0x0020 }
                r0.flush()     // Catch:{ Exception -> 0x0020 }
            L_0x0020:
                boolean r0 = r12.mHasStackInfo     // Catch:{ all -> 0x00c4 }
                if (r0 != 0) goto L_0x0026
                monitor-exit(r12)     // Catch:{ all -> 0x00c4 }
                return
            L_0x0026:
                java.io.ByteArrayOutputStream r0 = r12.mBos     // Catch:{ all -> 0x00c4 }
                r1 = 0
                if (r0 == 0) goto L_0x0063
                java.lang.String r3 = "crash-native-"
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c4 }
                r0.<init>()     // Catch:{ all -> 0x00c4 }
                java.lang.String r4 = "-"
                r0.append(r4)     // Catch:{ all -> 0x00c4 }
                java.lang.String r4 = r12.mProcessType     // Catch:{ all -> 0x00c4 }
                r0.append(r4)     // Catch:{ all -> 0x00c4 }
                java.lang.String r4 = "-"
                r0.append(r4)     // Catch:{ all -> 0x00c4 }
                int r4 = r12.mPid     // Catch:{ all -> 0x00c4 }
                r0.append(r4)     // Catch:{ all -> 0x00c4 }
                java.lang.String r4 = ".log"
                r0.append(r4)     // Catch:{ all -> 0x00c4 }
                java.lang.String r4 = r0.toString()     // Catch:{ all -> 0x00c4 }
                r5 = 4
                long r6 = r12.mStartTime     // Catch:{ all -> 0x00c4 }
                java.lang.String r8 = r12.mMeetingInfo     // Catch:{ all -> 0x00c4 }
                java.lang.String r9 = r12.mMemInfo     // Catch:{ all -> 0x00c4 }
                boolean r10 = r12.mIsBAASecurityEnabled     // Catch:{ all -> 0x00c4 }
                java.io.ByteArrayOutputStream r0 = r12.mBos     // Catch:{ all -> 0x00c4 }
                byte[] r11 = r0.toByteArray()     // Catch:{ all -> 0x00c4 }
                java.io.File r0 = com.zipow.videobox.util.LogUtil.writeCrashLogToFile(r3, r4, r5, r6, r8, r9, r10, r11)     // Catch:{ all -> 0x00c4 }
                goto L_0x0064
            L_0x0063:
                r0 = r1
            L_0x0064:
                java.io.ByteArrayOutputStream r3 = r12.mBos     // Catch:{ Exception -> 0x0081, all -> 0x007b }
                if (r3 == 0) goto L_0x006d
                java.io.ByteArrayOutputStream r3 = r12.mBos     // Catch:{ Exception -> 0x0081, all -> 0x007b }
                r3.close()     // Catch:{ Exception -> 0x0081, all -> 0x007b }
            L_0x006d:
                java.io.BufferedWriter r3 = r12.mWriter     // Catch:{ Exception -> 0x0081, all -> 0x007b }
                if (r3 == 0) goto L_0x0076
                java.io.BufferedWriter r3 = r12.mWriter     // Catch:{ Exception -> 0x0081, all -> 0x007b }
                r3.close()     // Catch:{ Exception -> 0x0081, all -> 0x007b }
            L_0x0076:
                r12.mBos = r1     // Catch:{ all -> 0x00c4 }
            L_0x0078:
                r12.mWriter = r1     // Catch:{ all -> 0x00c4 }
                goto L_0x0084
            L_0x007b:
                r0 = move-exception
                r12.mBos = r1     // Catch:{ all -> 0x00c4 }
                r12.mWriter = r1     // Catch:{ all -> 0x00c4 }
                throw r0     // Catch:{ all -> 0x00c4 }
            L_0x0081:
                r12.mBos = r1     // Catch:{ all -> 0x00c4 }
                goto L_0x0078
            L_0x0084:
                monitor-exit(r12)     // Catch:{ all -> 0x00c4 }
                if (r0 == 0) goto L_0x00c1
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r3 = com.zipow.cmmlib.AppUtil.getLogParentPath()
                r1.append(r3)
                java.lang.String r3 = "/logs"
                r1.append(r3)
                java.lang.String r1 = r1.toString()
                java.lang.String r3 = "crash-native-"
                boolean r1 = com.zipow.videobox.util.LogUtil.isSameCrashReported(r1, r0, r3)
                if (r1 == 0) goto L_0x00c1
                java.io.File r1 = new java.io.File
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = r0.getAbsolutePath()
                r3.append(r4)
                java.lang.String r4 = ".sent"
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                r1.<init>(r3)
                r0.renameTo(r1)
            L_0x00c1:
                r12.mStopped = r2
                return
            L_0x00c4:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x00c4 }
                throw r0
            L_0x00c7:
                r0 = move-exception
                monitor-exit(r12)     // Catch:{ all -> 0x00c7 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.stabilility.StabilityService.WriteLogFileThread.run():void");
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(StabilityService.class.getName());
        sb.append(".ACTION_LOG_CRASH");
        ACTION_LOG_CRASH = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(StabilityService.class.getName());
        sb2.append(".ACTION_NEW_CRASH_INFO");
        ACTION_NEW_CRASH_INFO = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(StabilityService.class.getName());
        sb3.append(".ACTION_PROTECT_PT");
        ACTION_PROTECT_PT = sb3.toString();
    }

    public void onCreate() {
        super.onCreate();
        if (VideoBoxApplication.getInstance() == null) {
            VideoBoxApplication.initialize(getApplicationContext(), false, 2, null);
        }
    }

    @SuppressLint({"NewApi"})
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        if (!this.mbNeedProtectPT) {
            disconnectPTService();
            stopSelf();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (!this.mbNeedProtectPT) {
            disconnectPTService();
            Process.killProcess(Process.myPid());
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        VideoBoxApplication.getInstance();
        super.onStartCommand(intent, i, i2);
        return doCommand(intent);
    }

    private int doCommand(@Nullable Intent intent) {
        int i = 2;
        if (intent == null) {
            return 2;
        }
        String action = intent.getAction();
        if (ACTION_LOG_CRASH.equals(action)) {
            startCrashLogger();
        } else if (ACTION_NEW_CRASH_INFO.equals(action)) {
            this.mLastCrashedInfo_memCpu = intent.getStringExtra(ARG_MEM_CPU);
            this.mLastCrashedInfo_meetingInfo = intent.getStringExtra(ARG_MEETING_INFO);
            this.mLastCrashedInfo_pid = intent.getIntExtra(ARG_PID, 0);
            this.mLastCrashInfo_BAASecurityEnabled = intent.getBooleanExtra(ARG_BAA_SECURITY_ENABLED, this.mLastCrashInfo_BAASecurityEnabled);
            startCrashLogger();
        } else if (ACTION_PROTECT_PT.equals(action)) {
            this.mbNeedProtectPT = true;
            i = 1;
        }
        connectPTService();
        return i;
    }

    private void connectPTService() {
        if (this.mPTServiceConnection == null) {
            this.mPTServiceConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    StabilityService.this.onPTServiceConnected(Stub.asInterface(iBinder));
                }

                public void onServiceDisconnected(ComponentName componentName) {
                    StabilityService.this.onPTServiceDisconnected();
                }
            };
        }
        int i = 0;
        if (this.mbNeedProtectPT) {
            i = 1;
        }
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), PTService.class.getName());
        bindService(intent, this.mPTServiceConnection, i);
    }

    private void disconnectPTService() {
        ServiceConnection serviceConnection = this.mPTServiceConnection;
        if (serviceConnection != null) {
            try {
                unbindService(serviceConnection);
            } catch (Exception unused) {
            }
            this.mPTServiceConnection = null;
            this.mPTService = null;
        }
    }

    /* access modifiers changed from: private */
    public void onPTServiceDisconnected() {
        this.mPTService = null;
    }

    /* access modifiers changed from: private */
    public void onPTServiceConnected(IPTService iPTService) {
        this.mPTService = iPTService;
    }

    private boolean startCrashLogger() {
        if (checkCallingPermission("android.permission.READ_LOGS") != 0) {
            return false;
        }
        LogMonitorThread logMonitorThread = this.mLogMonitorThread;
        if (logMonitorThread == null || !logMonitorThread.isAlive()) {
            this.mLogMonitorThread = new LogMonitorThread(this);
            this.mLogMonitorThread.start();
        }
        return true;
    }

    @Nullable
    public String getZoomProcessMemInfoInString(int i) {
        if (i <= 0 || i != this.mLastCrashedInfo_pid) {
            return null;
        }
        return this.mLastCrashedInfo_memCpu;
    }

    @Nullable
    public String getZoomProcessMeetingInfoInString(int i) {
        if (i <= 0 || i != this.mLastCrashedInfo_pid) {
            return null;
        }
        return this.mLastCrashedInfo_meetingInfo;
    }
}
