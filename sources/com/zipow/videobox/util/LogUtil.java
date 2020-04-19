package com.zipow.videobox.util;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import p021us.zoom.androidlib.util.StringUtil;

public class LogUtil {
    public static final String CRASH_LOG_PREFIX = "crash-";
    public static final String CRASH_LOG_SUFIX = ".log";
    private static final int CRASH_TYPE_JAVA = 0;
    private static final int CRASH_TYPE_NATIVE_NEW = 2;
    private static final int CRASH_TYPE_NATIVE_OLD = 1;
    public static final String FREEZE_LOG_PREFIX = "freeze-";
    public static final String FREEZE_LOG_SUFIX = ".log";
    private static final String JAVA_CRASH_PREFIX = "crash-java-";
    private static final String KEY_VERSION = "version:";
    public static final int MAX_CRASH_LOG_COUNT = 5;
    public static final int MAX_FREEZE_LOG_COUNT = 5;
    private static final String NEW_NATIVE_CRASH_PREFIX = "crash-native-zmdump-";
    private static final String OLD_NATIVE_CRASH_PREFIX = "crash-native-";
    private static final String TAG = "LogUtil";

    public static class CrashInfo {
        public int crashType = -1;
        @NonNull
        public String stackTrace = "";
        @NonNull
        public String version = "";

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof CrashInfo)) {
                return false;
            }
            CrashInfo crashInfo = (CrashInfo) obj;
            if (crashInfo.crashType == this.crashType && StringUtil.isSameString(crashInfo.version, this.version) && StringUtil.isSameString(crashInfo.stackTrace, this.stackTrace)) {
                z = true;
            }
            return z;
        }
    }

    public static File writeCrashLogToFile(String str, String str2, int i, long j, @Nullable String str3, @Nullable String str4, boolean z, @NonNull byte[] bArr) {
        FileOutputStream fileOutputStream;
        PrintStream printStream;
        Throwable th;
        Throwable th2;
        File newLogFile = getNewLogFile(str, str2, i, j);
        if (newLogFile == null) {
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(newLogFile);
            printStream = new PrintStream(fileOutputStream);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(KEY_VERSION);
                sb.append(VideoBoxApplication.getInstance().getVersionName());
                printStream.println(sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Kernel Version: ");
                sb2.append(VideoBoxApplication.getInstance().getKernelVersion());
                printStream.println(sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("OS:");
                sb3.append(SystemInfoHelper.getOSInfo());
                printStream.println(sb3.toString());
                if (!z) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Hardware:");
                    sb4.append(SystemInfoHelper.getHardwareInfo());
                    printStream.println(sb4.toString());
                }
                if (str4 != null) {
                    printStream.println(str4);
                }
                if (!z && str3 != null) {
                    printStream.println(str3);
                }
                printStream.println();
                printStream.println("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***");
                printStream.write(bArr);
                printStream.flush();
                printStream.close();
                fileOutputStream.close();
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r6;
                th2 = th4;
            }
        } catch (Exception unused) {
        } catch (Throwable th5) {
            r3.addSuppressed(th5);
        }
        return newLogFile;
        throw th2;
        if (th != null) {
            try {
                printStream.close();
            } catch (Throwable th6) {
                th.addSuppressed(th6);
            }
        } else {
            printStream.close();
        }
        throw th2;
        throw th;
    }

    public static String getLogFolder() {
        boolean z;
        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getLogParentPath());
        sb.append("/logs");
        String sb2 = sb.toString();
        File file = new File(sb2);
        try {
            z = !file.exists() ? file.mkdirs() : true;
        } catch (Exception unused) {
            z = false;
        }
        if (z) {
            return sb2;
        }
        return null;
    }

    public static String getDeviceInfo() {
        String kernelVersion = VideoBoxApplication.getInstance().getKernelVersion();
        StringBuilder sb = new StringBuilder();
        sb.append(Build.MANUFACTURER);
        sb.append("-");
        sb.append(Build.MODEL);
        String sb2 = sb.toString();
        String str = ZMUtils.isValidSignature(VideoBoxApplication.getInstance()) ? "" : "resigned-";
        StringBuilder sb3 = new StringBuilder();
        sb3.append(kernelVersion);
        sb3.append("-");
        sb3.append(str);
        sb3.append(sb2);
        return sb3.toString();
    }

    public static File getNewLogFile(@Nullable final String str, @Nullable final String str2, int i, long j) {
        if (str == null || str2 == null) {
            return null;
        }
        String logFolder = getLogFolder();
        if (logFolder == null) {
            return null;
        }
        File file = new File(logFolder);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        removeOldestLogFiles(i, file, new FileFilter() {
            /* JADX WARNING: Code restructure failed: missing block: B:5:0x002b, code lost:
                if (r3.endsWith(r0.toString()) != false) goto L_0x002d;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean accept(@androidx.annotation.NonNull java.io.File r3) {
                /*
                    r2 = this;
                    java.lang.String r3 = r3.getName()
                    java.lang.String r0 = r4
                    boolean r0 = r3.startsWith(r0)
                    if (r0 == 0) goto L_0x002f
                    java.lang.String r0 = r5
                    boolean r0 = r3.endsWith(r0)
                    if (r0 != 0) goto L_0x002d
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>()
                    java.lang.String r1 = r5
                    r0.append(r1)
                    java.lang.String r1 = ".sent"
                    r0.append(r1)
                    java.lang.String r0 = r0.toString()
                    boolean r3 = r3.endsWith(r0)
                    if (r3 == 0) goto L_0x002f
                L_0x002d:
                    r3 = 1
                    goto L_0x0030
                L_0x002f:
                    r3 = 0
                L_0x0030:
                    return r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.LogUtil.C33841.accept(java.io.File):boolean");
            }
        });
        String kernelVersion = VideoBoxApplication.getInstance().getKernelVersion();
        StringBuilder sb = new StringBuilder();
        sb.append(Build.MANUFACTURER);
        sb.append("-");
        sb.append(Build.MODEL);
        String sb2 = sb.toString();
        String str3 = ZMUtils.isValidSignature(VideoBoxApplication.getInstance()) ? "" : "resigned-";
        StringBuilder sb3 = new StringBuilder();
        sb3.append(logFolder);
        sb3.append(File.separator);
        sb3.append(str);
        sb3.append(kernelVersion);
        sb3.append("-");
        sb3.append(str3);
        sb3.append(sb2);
        sb3.append("-");
        sb3.append(new SimpleDateFormat("yyyy-MMdd-HHmmss", Locale.US).format(new Date(j)));
        sb3.append(str2);
        return new File(sb3.toString());
    }

    public static void removeOldestLogFiles(int i, File file, FileFilter fileFilter) {
        File[] listFiles = file.listFiles(fileFilter);
        if (listFiles != null && listFiles.length > i) {
            for (int length = listFiles.length; length > i; length--) {
                int i2 = 0;
                File file2 = listFiles[0];
                for (int i3 = 1; i3 < listFiles.length; i3++) {
                    if (file2 == null) {
                        file2 = listFiles[i3];
                        i2 = i3;
                    } else if (listFiles[i3] != null && file2.lastModified() > listFiles[i3].lastModified()) {
                        file2 = listFiles[i3];
                        i2 = i3;
                    }
                }
                listFiles[i2] = null;
                if (file2 != null) {
                    file2.delete();
                }
            }
        }
    }

    public static boolean isSameCrashReported(@Nullable String str, @Nullable final File file, @Nullable final String str2) {
        if (file == null || str == null || str2 == null) {
            return false;
        }
        CrashInfo loadCrashInfo = loadCrashInfo(file);
        File file2 = new File(str);
        if (file2.exists()) {
            File[] listFiles = file2.listFiles(new FileFilter() {
                public boolean accept(@NonNull File file) {
                    if (file.equals(file)) {
                        return false;
                    }
                    return file.getName().startsWith(str2);
                }
            });
            if (listFiles != null && listFiles.length > 0) {
                for (File loadCrashInfo2 : listFiles) {
                    if (isSameCrashInfo(loadCrashInfo, loadCrashInfo(loadCrashInfo2))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSameCrashInfo(@Nullable CrashInfo crashInfo, @Nullable CrashInfo crashInfo2) {
        if (crashInfo == null || crashInfo2 == null) {
            return false;
        }
        return crashInfo.equals(crashInfo2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r3.close();
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.zipow.videobox.util.LogUtil.CrashInfo loadCrashInfo(@androidx.annotation.Nullable java.io.File r9) {
        /*
            r0 = 0
            if (r9 == 0) goto L_0x00da
            boolean r1 = r9.exists()
            if (r1 != 0) goto L_0x000b
            goto L_0x00da
        L_0x000b:
            r1 = -1
            java.lang.String r2 = r9.getName()
            java.lang.String r3 = "crash-java-"
            boolean r3 = r2.contains(r3)
            r4 = 1
            r5 = 0
            if (r3 == 0) goto L_0x001c
            r1 = 0
            goto L_0x002f
        L_0x001c:
            java.lang.String r3 = "crash-native-zmdump-"
            boolean r3 = r2.contains(r3)
            if (r3 == 0) goto L_0x0026
            r1 = 2
            goto L_0x002f
        L_0x0026:
            java.lang.String r3 = "crash-native-"
            boolean r2 = r2.contains(r3)
            if (r2 == 0) goto L_0x002f
            r1 = 1
        L_0x002f:
            if (r1 >= 0) goto L_0x0032
            return r0
        L_0x0032:
            com.zipow.videobox.util.LogUtil$CrashInfo r2 = new com.zipow.videobox.util.LogUtil$CrashInfo
            r2.<init>()
            r2.crashType = r1
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00d9 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00d9 }
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00d9 }
            r7.<init>(r9)     // Catch:{ Exception -> 0x00d9 }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00d9 }
            r3.<init>(r6)     // Catch:{ Exception -> 0x00d9 }
            r9 = 0
        L_0x0049:
            java.lang.String r6 = r3.readLine()     // Catch:{ Throwable -> 0x00c7 }
            if (r6 != 0) goto L_0x0050
            goto L_0x0096
        L_0x0050:
            if (r5 != 0) goto L_0x0068
            java.lang.String r7 = "version:"
            boolean r7 = r6.startsWith(r7)     // Catch:{ Throwable -> 0x00c7 }
            if (r7 == 0) goto L_0x0049
            r5 = 8
            java.lang.String r5 = r6.substring(r5)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r5 = r5.trim()     // Catch:{ Throwable -> 0x00c7 }
            r2.version = r5     // Catch:{ Throwable -> 0x00c7 }
            r5 = 1
            goto L_0x0049
        L_0x0068:
            if (r9 != 0) goto L_0x0090
            boolean r7 = isStackBegin(r1, r6)     // Catch:{ Throwable -> 0x00c7 }
            if (r7 == 0) goto L_0x0049
            if (r1 == 0) goto L_0x008e
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c7 }
            r9.<init>()     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r7 = r2.stackTrace     // Catch:{ Throwable -> 0x00c7 }
            r9.append(r7)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r6 = parseStackTraceLine(r1, r6)     // Catch:{ Throwable -> 0x00c7 }
            r9.append(r6)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r6 = "\n"
            r9.append(r6)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x00c7 }
            r2.stackTrace = r9     // Catch:{ Throwable -> 0x00c7 }
        L_0x008e:
            r9 = 1
            goto L_0x0049
        L_0x0090:
            boolean r7 = isStackEnd(r1, r6)     // Catch:{ Throwable -> 0x00c7 }
            if (r7 == 0) goto L_0x009a
        L_0x0096:
            r3.close()     // Catch:{ Exception -> 0x00d9 }
            goto L_0x00d9
        L_0x009a:
            if (r1 != 0) goto L_0x00a8
            java.lang.String r7 = "Caused by:"
            boolean r7 = r6.startsWith(r7)     // Catch:{ Throwable -> 0x00c7 }
            if (r7 == 0) goto L_0x00a8
            java.lang.String r7 = ""
            r2.stackTrace = r7     // Catch:{ Throwable -> 0x00c7 }
        L_0x00a8:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c7 }
            r7.<init>()     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r8 = r2.stackTrace     // Catch:{ Throwable -> 0x00c7 }
            r7.append(r8)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r6 = parseStackTraceLine(r1, r6)     // Catch:{ Throwable -> 0x00c7 }
            r7.append(r6)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r6 = "\n"
            r7.append(r6)     // Catch:{ Throwable -> 0x00c7 }
            java.lang.String r6 = r7.toString()     // Catch:{ Throwable -> 0x00c7 }
            r2.stackTrace = r6     // Catch:{ Throwable -> 0x00c7 }
            goto L_0x0049
        L_0x00c5:
            r9 = move-exception
            goto L_0x00ca
        L_0x00c7:
            r9 = move-exception
            r0 = r9
            throw r0     // Catch:{ all -> 0x00c5 }
        L_0x00ca:
            if (r0 == 0) goto L_0x00d5
            r3.close()     // Catch:{ Throwable -> 0x00d0 }
            goto L_0x00d8
        L_0x00d0:
            r1 = move-exception
            r0.addSuppressed(r1)     // Catch:{ Exception -> 0x00d9 }
            goto L_0x00d8
        L_0x00d5:
            r3.close()     // Catch:{ Exception -> 0x00d9 }
        L_0x00d8:
            throw r9     // Catch:{ Exception -> 0x00d9 }
        L_0x00d9:
            return r2
        L_0x00da:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.LogUtil.loadCrashInfo(java.io.File):com.zipow.videobox.util.LogUtil$CrashInfo");
    }

    private static boolean isStackBegin(int i, @NonNull String str) {
        switch (i) {
            case 0:
                if (str.length() == 0) {
                    return true;
                }
                break;
            case 1:
            case 2:
                if (str.matches(".+#00  pc .+")) {
                    return true;
                }
                break;
        }
        return false;
    }

    private static boolean isStackEnd(int i, @NonNull String str) {
        switch (i) {
            case 0:
                if (str.length() == 0 || str.startsWith("frag")) {
                    return true;
                }
            case 1:
                if (str.length() == 0 || str.endsWith(" I DEBUG   : ")) {
                    return true;
                }
            case 2:
                if (str.length() == 0) {
                    return true;
                }
                break;
        }
        return false;
    }

    @NonNull
    private static String parseStackTraceLine(int i, @NonNull String str) {
        switch (i) {
            case 0:
                return str;
            case 1:
            case 2:
                int indexOf = str.indexOf(35);
                if (indexOf >= 0) {
                    return str.substring(indexOf);
                }
                break;
        }
        return "";
    }
}
