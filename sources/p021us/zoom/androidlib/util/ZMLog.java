package p021us.zoom.androidlib.util;

/* renamed from: us.zoom.androidlib.util.ZMLog */
public class ZMLog {
    private static ILogger logger = new ILogger() {
        public int getLevel() {
            return 1;
        }

        public boolean isEnabled() {
            return true;
        }

        public void log(int i, String str, String str2, Throwable th) {
        }

        public boolean needLogThreadId() {
            return false;
        }
    };

    public static void setLogger(ILogger iLogger) {
        logger = iLogger;
    }

    public static boolean isLogEnabled() {
        ILogger iLogger = logger;
        if (iLogger != null) {
            return iLogger.isEnabled();
        }
        return false;
    }

    /* renamed from: d */
    public static void m278d(String str, String str2, Object... objArr) {
        m286i(str, str2, objArr);
    }

    /* renamed from: d */
    public static void m279d(String str, Throwable th, String str2, Object... objArr) {
        m287i(str, th, str2, objArr);
    }

    /* renamed from: i */
    public static void m286i(String str, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 1) {
            m287i(str, null, formatMsg(str2, objArr), new Object[0]);
        }
    }

    /* renamed from: i */
    public static void m287i(String str, Throwable th, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPrefix());
            sb.append(formatMsg(str2, objArr));
            writeLog(1, str, sb.toString(), th);
        }
    }

    /* renamed from: w */
    public static void m288w(String str, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 2) {
            m289w(str, null, formatMsg(str2, objArr), new Object[0]);
        }
    }

    /* renamed from: w */
    public static void m289w(String str, Throwable th, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPrefix());
            sb.append(formatMsg(str2, objArr));
            writeLog(2, str, sb.toString(), th);
        }
    }

    /* renamed from: e */
    public static void m280e(String str, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 3) {
            m281e(str, null, formatMsg(str2, objArr), new Object[0]);
        }
    }

    /* renamed from: e */
    public static void m281e(String str, Throwable th, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 3) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPrefix());
            sb.append(formatMsg(str2, objArr));
            writeLog(3, str, sb.toString(), th);
        }
    }

    /* renamed from: er */
    public static void m282er(String str, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 4) {
            m283er(str, null, formatMsg(str2, objArr), new Object[0]);
        }
    }

    /* renamed from: er */
    public static void m283er(String str, Throwable th, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 4) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPrefix());
            sb.append(formatMsg(str2, objArr));
            writeLog(4, str, sb.toString(), th);
        }
    }

    /* renamed from: f */
    public static void m284f(String str, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 5) {
            m285f(str, null, formatMsg(str2, objArr), new Object[0]);
        }
    }

    /* renamed from: f */
    public static void m285f(String str, Throwable th, String str2, Object... objArr) {
        ILogger iLogger = logger;
        if (iLogger != null && iLogger.isEnabled() && logger.getLevel() <= 5) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPrefix());
            sb.append(formatMsg(str2, objArr));
            writeLog(5, str, sb.toString(), th);
        }
    }

    private static String formatMsg(String str, Object... objArr) {
        if (str == null) {
            return "";
        }
        if (objArr != null && objArr.length > 0) {
            str = String.format(str, objArr);
        }
        return str;
    }

    private static String getPrefix() {
        String str;
        ILogger iLogger = logger;
        if (iLogger == null) {
            return null;
        }
        if (!iLogger.needLogThreadId()) {
            str = "";
        } else {
            str = String.format("[T:%d]", new Object[]{Long.valueOf(getThreadId())});
        }
        return str;
    }

    private static long getThreadId() {
        return Thread.currentThread().getId();
    }

    private static void writeLog(int i, String str, String str2, Throwable th) {
        ILogger iLogger = logger;
        if (iLogger != null) {
            iLogger.log(i, str, str2, th);
        }
    }
}
