package com.zipow.videobox.common.p008pt;

import androidx.annotation.NonNull;
import com.zipow.videobox.util.ZMDomainUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.common.pt.ZMNativeSsoCloudInfo */
public class ZMNativeSsoCloudInfo {
    private String mPost_fix;
    private String mPre_fix;
    private int mSsoCloud = 0;
    private String mSsoUrl;
    private boolean mbLocked;

    public ZMNativeSsoCloudInfo() {
    }

    public ZMNativeSsoCloudInfo(String str, String str2, String str3, int i, boolean z) {
        this.mSsoUrl = str;
        this.mPre_fix = str2;
        this.mPost_fix = str3;
        this.mbLocked = z;
        this.mSsoCloud = i;
        if (this.mSsoCloud == 2 && !StringUtil.isEmptyOrNull(this.mSsoUrl)) {
            if (StringUtil.isEmptyOrNull(this.mPre_fix)) {
                this.mPre_fix = ZMDomainUtil.getPreFixForGov(this.mSsoUrl);
            }
            if (StringUtil.isEmptyOrNull(this.mPost_fix)) {
                this.mPost_fix = ZMDomainUtil.getPostFixForGov();
            }
        }
    }

    public String getmSsoUrl() {
        return this.mSsoUrl;
    }

    public String getmPre_fix() {
        return this.mPre_fix;
    }

    public String getmPost_fix() {
        return this.mPost_fix;
    }

    public boolean isMbLocked() {
        return this.mbLocked;
    }

    public int getmSsoCloud() {
        return this.mSsoCloud;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ZMNativeSsoCloudInfo{mSsoUrl='");
        sb.append(this.mSsoUrl);
        sb.append('\'');
        sb.append("mPre_fix='");
        sb.append(this.mPre_fix);
        sb.append('\'');
        sb.append(", mPost_fix='");
        sb.append(this.mPost_fix);
        sb.append('\'');
        sb.append(", mbLocked=");
        sb.append(this.mbLocked);
        sb.append(", mSsoCloud=");
        sb.append(this.mSsoCloud);
        sb.append('}');
        return sb.toString();
    }
}
