package com.zipow.videobox.common;

import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.LauncherActivity;
import com.zipow.videobox.dialog.ZMNoticeChoiceDomainDialog;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;

public class ZMNoticeChooseDomainTask extends ForegroundTask {
    private String accountName;
    private boolean couldSkip;
    private String detailLinkUrl;
    private String emailDomain;

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMNoticeChooseDomainTask(String str, String str2, boolean z, String str3, String str4) {
        super(str);
        this.detailLinkUrl = str2;
        this.couldSkip = z;
        this.accountName = str3;
        this.emailDomain = str4;
    }

    public boolean isValidActivity(String str) {
        return !LauncherActivity.class.getName().equals(str) && !JoinByURLActivity.class.getName().equals(str);
    }

    public void run(ZMActivity zMActivity) {
        if (!PTApp.getInstance().isWebSignedOn()) {
            ZMNoticeChoiceDomainDialog.show(zMActivity.getSupportFragmentManager(), ZMNoticeChoiceDomainDialog.class.getName(), this.detailLinkUrl, this.couldSkip, this.accountName, this.emailDomain);
        }
    }
}
