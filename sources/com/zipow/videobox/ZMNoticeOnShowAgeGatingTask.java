package com.zipow.videobox;

import com.zipow.videobox.login.ConfirmAgeFragment;
import com.zipow.videobox.login.ZmSmsLoginActivity;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;

public class ZMNoticeOnShowAgeGatingTask extends ForegroundTask {
    public boolean isMultipleInstancesAllowed() {
        return false;
    }

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMNoticeOnShowAgeGatingTask(String str) {
        super(str);
    }

    public boolean isValidActivity(String str) {
        return WelcomeActivity.class.getName().equals(str) || LoginActivity.class.getName().equals(str) || ZmSmsLoginActivity.class.getName().equals(str);
    }

    public void run(ZMActivity zMActivity) {
        ConfirmAgeFragment.show(zMActivity.getSupportFragmentManager());
    }
}
