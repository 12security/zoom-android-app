package com.zipow.videobox.common;

import p021us.zoom.androidlib.util.DeviceModelRank;
import p021us.zoom.androidlib.util.HardwareUtil;

public class ZMConfiguration {
    public static int CONF_CACHE_USER_NUMBERS = 300;
    public static int CONF_CLOSE_CAPTION_DURATION = 10000;
    public static int CONF_FRENQUENCE_EVENT_DELAY = 300;
    public static final int CONF_TOOLBAR_TIMEOUT = 5000;
    public static int DURATION_NORMAL_TIP = 3000;
    public static int DURATION_WAIT_KILL_PROCESS = 2000;
    public static int DURATION_WAIT_KILL_PROCESS_INTERVAL = 100;
    public static int FIRST_AUTO_FOCUS_DELAY = 1000;
    public static int INTERVAL_PLIST_FILTER = 200;
    private static boolean IS_ALREADY_INIT_CONSTANT_BY_DEVICERANK = false;
    private static final int MAX_LOW_PLIST_ATTENDEES_CHANGE_SORT = 50;
    private static final int MAX_LOW_PLIST_REFRESH_NOW_USER_COUNT = 100;
    public static int MAX_PLIST_ATTENDEES_CHANGE_SORT = 50;
    public static int MAX_PLIST_REFRESH_NOW_USER_COUNT = 100;
    public static final long PERMISSION_NEVER_ASK_AGAIN_SLOP = 1000;
    public static final int REQUEST_PERMISSION_DELAY_TIME = 500;

    public static void initConstantByDeviceRank() {
        if (!IS_ALREADY_INIT_CONSTANT_BY_DEVICERANK) {
            DeviceModelRank deviceModelRank = HardwareUtil.getDeviceModelRank();
            if (deviceModelRank == DeviceModelRank.High) {
                MAX_PLIST_ATTENDEES_CHANGE_SORT = 100;
                MAX_PLIST_REFRESH_NOW_USER_COUNT = 200;
            } else if (deviceModelRank == DeviceModelRank.Medium) {
                MAX_PLIST_ATTENDEES_CHANGE_SORT = 75;
                MAX_PLIST_REFRESH_NOW_USER_COUNT = 150;
            } else {
                MAX_PLIST_ATTENDEES_CHANGE_SORT = 50;
                MAX_PLIST_REFRESH_NOW_USER_COUNT = 100;
            }
            IS_ALREADY_INIT_CONSTANT_BY_DEVICERANK = true;
        }
    }
}
