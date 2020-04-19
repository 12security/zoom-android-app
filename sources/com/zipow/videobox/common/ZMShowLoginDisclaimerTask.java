package com.zipow.videobox.common;

import androidx.annotation.NonNull;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.LauncherActivity;
import com.zipow.videobox.WelcomeActivity;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.dialog.ZMNetErrorDialog;
import com.zipow.videobox.fragment.PrivacyDisclaimerFragment;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;

public class ZMShowLoginDisclaimerTask extends ForegroundTask {
    private static final String TAG = "ZMShowLoginDisclaimerTask";
    private final CustomizeInfo customizeInfo;

    public boolean isMultipleInstancesAllowed() {
        return false;
    }

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMShowLoginDisclaimerTask(String str, @NonNull CustomizeInfo customizeInfo2) {
        super(str);
        this.customizeInfo = customizeInfo2;
    }

    public boolean isValidActivity(String str) {
        return !LauncherActivity.class.getName().equals(str) && !IntegrationActivity.class.getName().equals(str) && !JoinByURLActivity.class.getName().equals(str) && !WelcomeActivity.class.getName().equals(str);
    }

    public void run(ZMActivity zMActivity) {
        CustomizeInfo customizeInfo2 = this.customizeInfo;
        if (customizeInfo2 == null || customizeInfo2.isEmpty()) {
            ZMNetErrorDialog.showDialog(zMActivity, 1);
            return;
        }
        this.customizeInfo.setType(1);
        PrivacyDisclaimerFragment.showDialog(zMActivity, this.customizeInfo);
    }
}
