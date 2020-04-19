package com.zipow.videobox;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.annotation.NonNull;
import com.zipow.videobox.common.p008pt.ZMNativeSsoCloudInfo;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.WebLaunchedToLoginParam;
import com.zipow.videobox.util.ZMDomainUtil;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMNoticeOnWebLoginTask extends ForegroundTask {
    private WebLaunchedToLoginParam mWebLaunchToLogin;

    public boolean isMultipleInstancesAllowed() {
        return false;
    }

    public boolean isOtherProcessSupported() {
        return false;
    }

    public ZMNoticeOnWebLoginTask(String str, @NonNull WebLaunchedToLoginParam webLaunchedToLoginParam) {
        super(str);
        this.mWebLaunchToLogin = webLaunchedToLoginParam;
    }

    public boolean isValidActivity(String str) {
        return !LauncherActivity.class.getName().equals(str) && !IntegrationActivity.class.getName().equals(str) && !JoinByURLActivity.class.getName().equals(str) && !WelcomeActivity.class.getName().equals(str);
    }

    public void run(ZMActivity zMActivity) {
        onWebLaunchedToLogin(zMActivity);
    }

    private void onWebLaunchedToLogin(ZMActivity zMActivity) {
        ZMAlertDialog create = new Builder(zMActivity).setTitle(C4558R.string.zm_sign_in_gov_title_130953).setMessage(C4558R.string.zm_sign_in_gov_msg_130953).setPositiveButton(C4558R.string.zm_login_to_start_conf, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMNoticeOnWebLoginTask.this.onClickSignIn();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCancelable(true);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    /* access modifiers changed from: private */
    public void onClickSignIn() {
        String str;
        String str2 = "";
        if (this.mWebLaunchToLogin.getSnsType() == 101) {
            str = StringUtil.safeString(this.mWebLaunchToLogin.getSsoVanityURL());
        } else {
            ZMNativeSsoCloudInfo sSOCloudInfo = PTApp.getInstance().getSSOCloudInfo();
            if (sSOCloudInfo != null) {
                str2 = sSOCloudInfo.getmPre_fix();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(ZMDomainUtil.getPostFixForGov());
            str = sb.toString();
        }
        ZmLoginHelper.setSSOUrl(str, 2);
        PTApp.getInstance().logout(0);
        ZmLoginHelper.goLoginActivity(this.mWebLaunchToLogin);
    }
}
