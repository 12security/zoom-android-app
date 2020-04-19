package com.zipow.cmmlib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.ILogger;

public class Logger implements ILogger {
    @NonNull
    private static Logger instance = new Logger();
    private boolean mIsEnabled = false;
    private int mLevel = 2;
    private boolean mUseNativeLog = false;

    private void logWithAndroidJavaLogger(int i, String str, String str2, Throwable th) {
    }

    private static native void writeLogImpl(int i, String str, String str2);

    private Logger() {
    }

    @NonNull
    public static Logger getInstance() {
        return instance;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public void setEnabled(boolean z) {
        this.mIsEnabled = z;
    }

    public void setLevel(int i) {
        this.mLevel = i;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public boolean needLogThreadId() {
        return !useNativeLog();
    }

    private boolean useNativeLog() {
        return this.mUseNativeLog;
    }

    public void startNativeLog(boolean z) {
        this.mUseNativeLog = z;
    }

    public void log(int i, String str, String str2, Throwable th) {
        if (!useNativeLog()) {
            logWithAndroidJavaLogger(i, str, str2, th);
            return;
        }
        try {
            logWithNativeLogger(i, str, str2, th);
        } catch (UnsatisfiedLinkError unused) {
            logWithAndroidJavaLogger(i, str, str2, th);
        }
    }

    private void logWithNativeLogger(int i, String str, String str2, @Nullable Throwable th) {
        if (th == null) {
            writeLogImpl(i, str, str2);
            return;
        }
        writeLogImpl(i, str, str2);
        if (th.getMessage() != null) {
            writeLogImpl(i, str, th.getMessage());
        }
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            writeLogImpl(i, str, stackTraceElement.toString());
        }
    }
}
