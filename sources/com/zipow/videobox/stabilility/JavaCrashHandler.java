package com.zipow.videobox.stabilility;

import android.os.Handler;
import androidx.annotation.NonNull;
import java.lang.Thread.UncaughtExceptionHandler;

public class JavaCrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "JavaCrashHandler";
    private static String s_extraInfo = null;
    private static boolean s_hasJavaThreadCrashed = false;
    @NonNull
    private Handler mHandler = new Handler();
    private UncaughtExceptionHandler mNextHandler;

    public static boolean hasJavaThreadCrashed() {
        return s_hasJavaThreadCrashed;
    }

    public static void setJavaThreadCrashed() {
        s_hasJavaThreadCrashed = true;
    }

    public static void setExtraInfo(String str) {
        s_extraInfo = str;
    }

    public JavaCrashHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.mNextHandler = uncaughtExceptionHandler;
    }

    /* JADX WARNING: type inference failed for: r7v3 */
    /* JADX WARNING: type inference failed for: r7v6 */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:55|56|(2:58|59)|60) */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x020f, code lost:
        if (r8 != 0) goto L_0x0224;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0222, code lost:
        if (r8 == 0) goto L_0x0227;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0224, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0284, code lost:
        r11 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:?, code lost:
        r5.flush();
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        r0.delete();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0196, code lost:
        if (r5 != null) goto L_0x0198;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:?, code lost:
        r5.flush();
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x019e, code lost:
        r0 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0193 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0203 A[SYNTHETIC, Splitter:B:107:0x0203] */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x020a A[SYNTHETIC, Splitter:B:111:0x020a] */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x0287 A[SYNTHETIC, Splitter:B:138:0x0287] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01ec A[SYNTHETIC, Splitter:B:93:0x01ec] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01f3 A[SYNTHETIC, Splitter:B:97:0x01f3] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void uncaughtException(java.lang.Thread r11, java.lang.Throwable r12) {
        /*
            r10 = this;
            setJavaThreadCrashed()
            boolean r0 = com.zipow.videobox.mainboard.Mainboard.isNativeCrashed()
            if (r0 == 0) goto L_0x0011
            java.lang.Thread$UncaughtExceptionHandler r0 = r10.mNextHandler
            if (r0 == 0) goto L_0x0010
            r0.uncaughtException(r11, r12)
        L_0x0010:
            return
        L_0x0011:
            boolean r0 = r12 instanceof java.lang.UnsatisfiedLinkError
            if (r0 != 0) goto L_0x028e
            boolean r0 = r12 instanceof android.database.sqlite.SQLiteDiskIOException
            if (r0 == 0) goto L_0x001b
            goto L_0x028e
        L_0x001b:
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r0 = r0.isConfApp()
            if (r0 == 0) goto L_0x0052
            us.zoom.androidlib.app.ZMActivity r0 = p021us.zoom.androidlib.app.ZMActivity.getFrontActivity()
            com.zipow.videobox.confapp.ConfUI r1 = com.zipow.videobox.confapp.ConfUI.getInstance()
            boolean r1 = r1.isLeaveComplete()
            if (r1 != 0) goto L_0x0043
            boolean r1 = r0 instanceof com.zipow.videobox.ConfActivity
            if (r1 == 0) goto L_0x0052
            boolean r1 = r0.isFinishing()
            if (r1 != 0) goto L_0x0043
            boolean r0 = p021us.zoom.androidlib.app.ZMActivity.isActivityDestroyed(r0)
            if (r0 == 0) goto L_0x0052
        L_0x0043:
            java.lang.String r11 = TAG
            java.lang.String r0 = ""
            android.util.Log.e(r11, r0, r12)
            com.zipow.videobox.VideoBoxApplication r11 = com.zipow.videobox.VideoBoxApplication.getInstance()
            r11.stopConfService()
            return
        L_0x0052:
            int r0 = android.os.Process.myPid()
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r1 = r1.isConfApp()
            if (r1 == 0) goto L_0x0063
            java.lang.String r1 = "zVideoApp"
            goto L_0x0065
        L_0x0063:
            java.lang.String r1 = "zChatApp"
        L_0x0065:
            com.zipow.videobox.VideoBoxApplication r2 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r2 = com.zipow.videobox.util.ZMUtils.isItuneApp(r2)
            if (r2 == 0) goto L_0x0080
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            java.lang.String r1 = "-intune"
            r2.append(r1)
            java.lang.String r1 = r2.toString()
        L_0x0080:
            java.lang.String r2 = "java-"
            boolean r3 = r12 instanceof com.zipow.videobox.stabilility.NativeCrashException
            if (r3 == 0) goto L_0x0088
            java.lang.String r2 = "native-zmdump-"
        L_0x0088:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "crash-"
            r4.append(r5)
            r4.append(r2)
            java.lang.String r4 = r4.toString()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "-"
            r5.append(r6)
            r5.append(r1)
            java.lang.String r1 = "-"
            r5.append(r1)
            r5.append(r0)
            java.lang.String r0 = ".log"
            r5.append(r0)
            java.lang.String r0 = r5.toString()
            r1 = 5
            long r5 = java.lang.System.currentTimeMillis()
            java.io.File r0 = com.zipow.videobox.util.LogUtil.getNewLogFile(r4, r0, r1, r5)
            if (r0 != 0) goto L_0x00ca
            java.lang.Thread$UncaughtExceptionHandler r0 = r10.mNextHandler
            if (r0 == 0) goto L_0x00c9
            r0.uncaughtException(r11, r12)
        L_0x00c9:
            return
        L_0x00ca:
            r1 = 0
            boolean r4 = com.zipow.cmmlib.AppContext.BAASecurity_IsEnabled()     // Catch:{ Exception -> 0x0192, all -> 0x018e }
            com.zipow.videobox.VideoBoxApplication r5 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0192, all -> 0x018e }
            boolean r5 = r5.isConfApp()     // Catch:{ Exception -> 0x0192, all -> 0x018e }
            if (r5 == 0) goto L_0x00de
            com.zipow.videobox.stabilility.JavaCrashHandler$1 r5 = new com.zipow.videobox.stabilility.JavaCrashHandler$1     // Catch:{ Exception -> 0x0192, all -> 0x018e }
            r5.<init>()     // Catch:{ Exception -> 0x0192, all -> 0x018e }
        L_0x00de:
            java.io.PrintStream r5 = new java.io.PrintStream     // Catch:{ Exception -> 0x0192, all -> 0x018e }
            r5.<init>(r0)     // Catch:{ Exception -> 0x0192, all -> 0x018e }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0193 }
            r6.<init>()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r7 = "version: "
            r6.append(r7)     // Catch:{ Exception -> 0x0193 }
            com.zipow.videobox.VideoBoxApplication r7 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r7 = r7.getVersionName()     // Catch:{ Exception -> 0x0193 }
            r6.append(r7)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0193 }
            r5.println(r6)     // Catch:{ Exception -> 0x0193 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0193 }
            r6.<init>()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r7 = "Kernel Version: "
            r6.append(r7)     // Catch:{ Exception -> 0x0193 }
            com.zipow.videobox.VideoBoxApplication r7 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r7 = r7.getKernelVersion()     // Catch:{ Exception -> 0x0193 }
            r6.append(r7)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0193 }
            r5.println(r6)     // Catch:{ Exception -> 0x0193 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0193 }
            r6.<init>()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r7 = "OS: "
            r6.append(r7)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r7 = com.zipow.videobox.ptapp.SystemInfoHelper.getOSInfo()     // Catch:{ Exception -> 0x0193 }
            r6.append(r7)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0193 }
            r5.println(r6)     // Catch:{ Exception -> 0x0193 }
            if (r4 != 0) goto L_0x014d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0193 }
            r4.<init>()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = "Hardware: "
            r4.append(r6)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = com.zipow.videobox.ptapp.SystemInfoHelper.getHardwareInfo()     // Catch:{ Exception -> 0x0193 }
            r4.append(r6)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0193 }
            r5.println(r4)     // Catch:{ Exception -> 0x0193 }
        L_0x014d:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0193 }
            r4.<init>()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = "IsProcessAtFront: "
            r4.append(r6)     // Catch:{ Exception -> 0x0193 }
            com.zipow.videobox.VideoBoxApplication r6 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ Exception -> 0x0193 }
            boolean r6 = r6.isAtFront()     // Catch:{ Exception -> 0x0193 }
            r4.append(r6)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0193 }
            r5.println(r4)     // Catch:{ Exception -> 0x0193 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0193 }
            r4.<init>()     // Catch:{ Exception -> 0x0193 }
            java.lang.String r6 = "IsRooted: "
            r4.append(r6)     // Catch:{ Exception -> 0x0193 }
            boolean r6 = p021us.zoom.androidlib.util.RootCheckUtils.isRooted()     // Catch:{ Exception -> 0x0193 }
            r4.append(r6)     // Catch:{ Exception -> 0x0193 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x0193 }
            r5.println(r4)     // Catch:{ Exception -> 0x0193 }
            r5.println()     // Catch:{ Exception -> 0x0193 }
            r12.printStackTrace(r5)     // Catch:{ Exception -> 0x0193 }
            r5.flush()     // Catch:{ Exception -> 0x019f }
            r5.close()     // Catch:{ Exception -> 0x019f }
            goto L_0x019f
        L_0x018e:
            r11 = move-exception
            r5 = r1
            goto L_0x0285
        L_0x0192:
            r5 = r1
        L_0x0193:
            r0.delete()     // Catch:{ all -> 0x0284 }
            if (r5 == 0) goto L_0x019e
            r5.flush()     // Catch:{ Exception -> 0x019e }
            r5.close()     // Catch:{ Exception -> 0x019e }
        L_0x019e:
            r0 = r1
        L_0x019f:
            if (r3 != 0) goto L_0x0227
            us.zoom.androidlib.app.ZMActivity r4 = p021us.zoom.androidlib.app.ZMActivity.getFrontActivity()
            if (r4 == 0) goto L_0x0227
            java.lang.String r5 = "Fragment"
            java.lang.String[] r5 = new java.lang.String[]{r5}
            if (r0 == 0) goto L_0x0212
            java.io.FileWriter r6 = new java.io.FileWriter     // Catch:{ IOException -> 0x01fe, all -> 0x01e6 }
            r7 = 1
            r6.<init>(r0, r7)     // Catch:{ IOException -> 0x01fe, all -> 0x01e6 }
            java.io.BufferedWriter r7 = new java.io.BufferedWriter     // Catch:{ IOException -> 0x01e4, all -> 0x01e1 }
            r7.<init>(r6)     // Catch:{ IOException -> 0x01e4, all -> 0x01e1 }
            java.io.PrintWriter r8 = new java.io.PrintWriter     // Catch:{ IOException -> 0x01df, all -> 0x01dc }
            r8.<init>(r7)     // Catch:{ IOException -> 0x01df, all -> 0x01dc }
            r8.println()     // Catch:{ IOException -> 0x01da, all -> 0x01d8 }
            androidx.fragment.app.FragmentManager r1 = r4.getSupportFragmentManager()     // Catch:{ IOException -> 0x01da, all -> 0x01d8 }
            if (r1 == 0) goto L_0x01d6
            androidx.fragment.app.FragmentManager r1 = r4.getSupportFragmentManager()     // Catch:{ IOException -> 0x01da, all -> 0x01d8 }
            java.lang.String r4 = ""
            java.io.FileDescriptor r9 = new java.io.FileDescriptor     // Catch:{ IOException -> 0x01da, all -> 0x01d8 }
            r9.<init>()     // Catch:{ IOException -> 0x01da, all -> 0x01d8 }
            r1.dump(r4, r9, r8, r5)     // Catch:{ IOException -> 0x01da, all -> 0x01d8 }
        L_0x01d6:
            r1 = r6
            goto L_0x0214
        L_0x01d8:
            r11 = move-exception
            goto L_0x01ea
        L_0x01da:
            goto L_0x0201
        L_0x01dc:
            r11 = move-exception
            r8 = r1
            goto L_0x01ea
        L_0x01df:
            r8 = r1
            goto L_0x0201
        L_0x01e1:
            r11 = move-exception
            r7 = r1
            goto L_0x01e9
        L_0x01e4:
            r7 = r1
            goto L_0x0200
        L_0x01e6:
            r11 = move-exception
            r6 = r1
            r7 = r6
        L_0x01e9:
            r8 = r7
        L_0x01ea:
            if (r6 == 0) goto L_0x01f1
            r6.close()     // Catch:{ IOException -> 0x01f0 }
            goto L_0x01f1
        L_0x01f0:
        L_0x01f1:
            if (r7 == 0) goto L_0x01f8
            r7.close()     // Catch:{ IOException -> 0x01f7 }
            goto L_0x01f8
        L_0x01f7:
        L_0x01f8:
            if (r8 == 0) goto L_0x01fd
            r8.close()
        L_0x01fd:
            throw r11
        L_0x01fe:
            r6 = r1
            r7 = r6
        L_0x0200:
            r8 = r7
        L_0x0201:
            if (r6 == 0) goto L_0x0208
            r6.close()     // Catch:{ IOException -> 0x0207 }
            goto L_0x0208
        L_0x0207:
        L_0x0208:
            if (r7 == 0) goto L_0x020f
            r7.close()     // Catch:{ IOException -> 0x020e }
            goto L_0x020f
        L_0x020e:
        L_0x020f:
            if (r8 == 0) goto L_0x0227
            goto L_0x0224
        L_0x0212:
            r7 = r1
            r8 = r7
        L_0x0214:
            if (r1 == 0) goto L_0x021b
            r1.close()     // Catch:{ IOException -> 0x021a }
            goto L_0x021b
        L_0x021a:
        L_0x021b:
            if (r7 == 0) goto L_0x0222
            r7.close()     // Catch:{ IOException -> 0x0221 }
            goto L_0x0222
        L_0x0221:
        L_0x0222:
            if (r8 == 0) goto L_0x0227
        L_0x0224:
            r8.close()
        L_0x0227:
            if (r0 == 0) goto L_0x0272
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = com.zipow.cmmlib.AppUtil.getLogParentPath()
            r1.append(r4)
            java.lang.String r4 = "/logs"
            r1.append(r4)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "crash-"
            r4.append(r5)
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            boolean r1 = com.zipow.videobox.util.LogUtil.isSameCrashReported(r1, r0, r2)
            if (r1 == 0) goto L_0x0272
            java.io.File r1 = new java.io.File
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = r0.getAbsolutePath()
            r2.append(r4)
            java.lang.String r4 = ".sent"
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            r0.renameTo(r1)
        L_0x0272:
            if (r3 == 0) goto L_0x027c
            com.zipow.videobox.VideoBoxApplication r11 = com.zipow.videobox.VideoBoxApplication.getInstance()
            r11.killCurrentProcess()
            goto L_0x0283
        L_0x027c:
            java.lang.Thread$UncaughtExceptionHandler r0 = r10.mNextHandler
            if (r0 == 0) goto L_0x0283
            r0.uncaughtException(r11, r12)
        L_0x0283:
            return
        L_0x0284:
            r11 = move-exception
        L_0x0285:
            if (r5 == 0) goto L_0x028d
            r5.flush()     // Catch:{ Exception -> 0x028d }
            r5.close()     // Catch:{ Exception -> 0x028d }
        L_0x028d:
            throw r11
        L_0x028e:
            java.lang.Thread$UncaughtExceptionHandler r0 = r10.mNextHandler
            if (r0 == 0) goto L_0x0295
            r0.uncaughtException(r11, r12)
        L_0x0295:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.stabilility.JavaCrashHandler.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }
}
