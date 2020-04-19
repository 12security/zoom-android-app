package com.zipow.videobox.sip.server;

import com.zipow.videobox.VideoBoxApplication;

public class AssistantAppHelper {
    private static final String TAG = "AssistantAppHelper";

    public static boolean isAssistantRunning() {
        return true;
    }

    public static void stopAssistantApp() {
    }

    public static int getAssistantPid() {
        return VideoBoxApplication.getInstance().getPTProcessId();
    }
}
