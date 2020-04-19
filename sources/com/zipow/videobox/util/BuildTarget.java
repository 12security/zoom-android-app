package com.zipow.videobox.util;

public class BuildTarget {
    public static final int TARGET_ATT = 1;
    public static final int TARGET_BIZCONF = 7;
    public static final int TARGET_BROADVIEW = 3;
    public static final int TARGET_BT = 4;
    public static final int TARGET_HUIHUI = 8;
    public static final int TARGET_RINGCENTRAL = 2;
    public static final int TARGET_TELUS = 5;
    public static final int TARGET_ZHUMU = 6;
    public static final int TARGET_ZOOM = 0;

    public static boolean isRingCentralLogin(int i) {
        switch (i) {
            case 1:
            case 2:
            case 4:
            case 5:
                return true;
            default:
                return false;
        }
    }
}
