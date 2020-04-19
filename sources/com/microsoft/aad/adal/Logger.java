package com.microsoft.aad.adal;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Logger {
    private static final String CUSTOM_LOG_ERROR = "Custom log failed to log message:%s";
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static Logger sINSTANCE = new Logger();
    private boolean mAndroidLogEnabled = false;
    private String mCorrelationId = null;
    private boolean mEnablePII = false;
    private ILogger mExternalLogger = null;
    private LogLevel mLogLevel = LogLevel.Verbose;

    /* renamed from: com.microsoft.aad.adal.Logger$1 */
    static /* synthetic */ class C17181 {
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel = new int[LogLevel.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                com.microsoft.aad.adal.Logger$LogLevel[] r0 = com.microsoft.aad.adal.Logger.LogLevel.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel = r0
                int[] r0 = $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel     // Catch:{ NoSuchFieldError -> 0x0014 }
                com.microsoft.aad.adal.Logger$LogLevel r1 = com.microsoft.aad.adal.Logger.LogLevel.Error     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel     // Catch:{ NoSuchFieldError -> 0x001f }
                com.microsoft.aad.adal.Logger$LogLevel r1 = com.microsoft.aad.adal.Logger.LogLevel.Warn     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel     // Catch:{ NoSuchFieldError -> 0x002a }
                com.microsoft.aad.adal.Logger$LogLevel r1 = com.microsoft.aad.adal.Logger.LogLevel.Info     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel     // Catch:{ NoSuchFieldError -> 0x0035 }
                com.microsoft.aad.adal.Logger$LogLevel r1 = com.microsoft.aad.adal.Logger.LogLevel.Verbose     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$com$microsoft$aad$adal$Logger$LogLevel     // Catch:{ NoSuchFieldError -> 0x0040 }
                com.microsoft.aad.adal.Logger$LogLevel r1 = com.microsoft.aad.adal.Logger.LogLevel.Debug     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.microsoft.aad.adal.Logger.C17181.<clinit>():void");
        }
    }

    public interface ILogger {
        void Log(String str, String str2, String str3, LogLevel logLevel, ADALError aDALError);
    }

    public enum LogLevel {
        Error(0),
        Warn(1),
        Info(2),
        Verbose(3),
        Debug(4);
        
        private int mValue;

        private LogLevel(int i) {
            this.mValue = i;
        }
    }

    private void log(String str, String str2, String str3, LogLevel logLevel, ADALError aDALError, Throwable th) {
    }

    private void sendLogcatLogs(String str, LogLevel logLevel, String str2) {
    }

    public static Logger getInstance() {
        return sINSTANCE;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.mLogLevel = logLevel;
    }

    public synchronized void setExternalLogger(ILogger iLogger) {
        this.mExternalLogger = iLogger;
    }

    public void setAndroidLogEnabled(boolean z) {
        this.mAndroidLogEnabled = z;
    }

    public void setEnablePII(boolean z) {
        this.mEnablePII = z;
    }

    private static String addMoreInfo(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(getUTCDateTimeAsString());
            sb.append("-");
            sb.append(getInstance().mCorrelationId);
            sb.append("-");
            sb.append(str);
            sb.append(" ver:");
            sb.append(AuthenticationContext.getVersionName());
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getUTCDateTimeAsString());
        sb2.append("-");
        sb2.append(getInstance().mCorrelationId);
        sb2.append("- ver:");
        sb2.append(AuthenticationContext.getVersionName());
        return sb2.toString();
    }

    /* renamed from: d */
    public static void m230d(String str, String str2) {
        if (!StringExtensions.isNullOrBlank(str2)) {
            getInstance().log(str, str2, null, LogLevel.Debug, null, null);
        }
    }

    /* renamed from: i */
    public static void m234i(String str, String str2, String str3) {
        getInstance().log(str, str2, str3, LogLevel.Info, null, null);
    }

    /* renamed from: i */
    public static void m235i(String str, String str2, String str3, ADALError aDALError) {
        getInstance().log(str, str2, str3, LogLevel.Info, aDALError, null);
    }

    /* renamed from: v */
    public static void m236v(String str, String str2) {
        getInstance().log(str, str2, null, LogLevel.Verbose, null, null);
    }

    /* renamed from: v */
    public static void m237v(String str, String str2, String str3, ADALError aDALError) {
        getInstance().log(str, str2, str3, LogLevel.Verbose, aDALError, null);
    }

    /* renamed from: w */
    public static void m239w(String str, String str2, String str3, ADALError aDALError) {
        getInstance().log(str, str2, str3, LogLevel.Warn, aDALError, null);
    }

    /* renamed from: w */
    public static void m238w(String str, String str2) {
        getInstance().log(str, str2, null, LogLevel.Warn, null, null);
    }

    /* renamed from: e */
    public static void m231e(String str, String str2, String str3, ADALError aDALError) {
        getInstance().log(str, str2, str3, LogLevel.Error, aDALError, null);
    }

    /* renamed from: e */
    public static void m232e(String str, String str2, String str3, ADALError aDALError, Throwable th) {
        getInstance().log(str, str2, str3, LogLevel.Error, aDALError, th);
    }

    /* renamed from: e */
    public static void m233e(String str, String str2, Throwable th) {
        getInstance().log(str, str2, null, LogLevel.Error, null, th);
    }

    public static void setCorrelationId(UUID uuid) {
        getInstance().mCorrelationId = "";
        if (uuid != null) {
            getInstance().mCorrelationId = uuid.toString();
        }
    }

    private static String getCodeName(ADALError aDALError) {
        return aDALError != null ? aDALError.name() : "";
    }

    @SuppressLint({"SimpleDateFormat"})
    private static String getUTCDateTimeAsString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date());
    }

    public String getCorrelationId() {
        return this.mCorrelationId;
    }
}
