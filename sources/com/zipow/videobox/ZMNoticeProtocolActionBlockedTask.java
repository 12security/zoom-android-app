package com.zipow.videobox;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMNoticeProtocolActionBlockedTask extends ForegroundTask {
    private static String mContent;

    public boolean isMultipleInstancesAllowed() {
        return false;
    }

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMNoticeProtocolActionBlockedTask(String str, String str2) {
        super(str);
        mContent = str2;
    }

    public boolean isValidActivity(String str) {
        return !LauncherActivity.class.getName().equals(str) && !JoinByURLActivity.class.getName().equals(str) && !IntegrationActivity.class.getName().equals(str);
    }

    public void run(ZMActivity zMActivity) {
        showDomainDialog(zMActivity);
    }

    private void showDomainDialog(ZMActivity zMActivity) {
        if (!StringUtil.isEmptyOrNull(mContent)) {
            ZMAlertDialog create = new Builder(zMActivity).setMessage(mContent).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
            create.setCancelable(true);
            create.setCanceledOnTouchOutside(false);
            create.show();
        }
    }
}
