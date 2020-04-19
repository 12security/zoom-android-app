package com.zipow.videobox.stabilility;

public class NativeCrashHandler {
    public static void onNativeCrash(String str) {
        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), new NativeCrashException(str));
    }
}
