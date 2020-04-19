package p021us.zoom.thirdparty.login.google;

import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.googledrive.GoogleDriveMgr;

/* renamed from: us.zoom.thirdparty.login.google.GoogleAuthActivity */
public class GoogleAuthActivity extends ZmBaseGoogleAuthActivity {
    /* access modifiers changed from: protected */
    public void setAuthInfo(String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            GoogleDriveMgr.getInstance().getGoogleDrive(this).setAuthCode(str);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            GoogleDriveMgr.getInstance().getGoogleDrive(this).setAuthErrorMsg(str2);
        }
    }
}
