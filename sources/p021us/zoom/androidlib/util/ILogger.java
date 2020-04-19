package p021us.zoom.androidlib.util;

/* renamed from: us.zoom.androidlib.util.ILogger */
public interface ILogger {
    public static final int LOG_DEBUG = 0;
    public static final int LOG_ERROR = 3;
    public static final int LOG_ERROR_REPORT = 4;
    public static final int LOG_FATAL = 5;
    public static final int LOG_INFO = 1;
    public static final int LOG_WARNING = 2;

    int getLevel();

    boolean isEnabled();

    void log(int i, String str, String str2, Throwable th);

    boolean needLogThreadId();
}
