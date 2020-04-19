package com.zipow.videobox;

import com.zipow.videobox.dialog.ZMCrashReportDialog;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;

public class ZMNoticeOnShowCrashReport extends ForegroundTask {
    public boolean isMultipleInstancesAllowed() {
        return false;
    }

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMNoticeOnShowCrashReport(String str) {
        super(str);
    }

    public boolean isValidActivity(String str) {
        return !LauncherActivity.class.getName().equals(str) && !IntegrationActivity.class.getName().equals(str) && !JoinByURLActivity.class.getName().equals(str);
    }

    public void run(ZMActivity zMActivity) {
        ZMCrashReportDialog.showDialog(zMActivity);
    }
}
