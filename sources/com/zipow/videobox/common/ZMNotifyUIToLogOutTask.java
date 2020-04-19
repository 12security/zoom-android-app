package com.zipow.videobox.common;

import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.LauncherActivity;
import com.zipow.videobox.WelcomeActivity;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.LogoutHandler.IListener;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;

public class ZMNotifyUIToLogOutTask extends ForegroundTask {
    private static final String TAG = "ZMNotifyUIToLogOutTask";

    public boolean isMultipleInstancesAllowed() {
        return false;
    }

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMNotifyUIToLogOutTask(String str) {
        super(str);
    }

    public boolean isValidActivity(String str) {
        return !LauncherActivity.class.getName().equals(str) && !IntegrationActivity.class.getName().equals(str) && !JoinByURLActivity.class.getName().equals(str) && !WelcomeActivity.class.getName().equals(str);
    }

    public void run(final ZMActivity zMActivity) {
        LogoutHandler.getInstance().startLogout(zMActivity, new IListener() {
            public void afterLogout() {
                WelcomeActivity.show(zMActivity, false, false);
                zMActivity.finish();
            }
        });
    }
}
