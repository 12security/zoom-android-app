package com.zipow.cmmlib;

public class CmmTime {
    private static native long getMMNowImpl();

    public static long getMMNow() {
        return getMMNowImpl();
    }
}
