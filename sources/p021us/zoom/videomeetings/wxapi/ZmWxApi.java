package p021us.zoom.videomeetings.wxapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.thirdparty.cnlogin.CnLoginConstants;

/* renamed from: us.zoom.videomeetings.wxapi.ZmWxApi */
public class ZmWxApi {
    private static final String TAG = "ZmWxApi";
    @Nullable
    private String mAppid;
    @Nullable
    private Context mContext;

    public ZmWxApi(@NonNull Context context) {
        this.mContext = context;
    }

    public boolean registerApp(@NonNull String str) {
        Context context = this.mContext;
        if (context == null || !ZmWxApiUtil.validateAppSignatureForPackage(context, CnLoginConstants.PACKAGE_NAME_WECHAT)) {
            return false;
        }
        this.mAppid = str;
        StringBuilder sb = new StringBuilder();
        sb.append("weixin://registerapp?appid=");
        sb.append(this.mAppid);
        String sb2 = sb.toString();
        Intent intent = new Intent();
        intent.setAction("com.tencent.mm.plugin.openapi.Intent.ACTION_HANDLE_APP_REGISTER");
        intent.putExtra("_mmessage_sdkVersion", 621086720);
        intent.putExtra("_mmessage_appPackage", this.mContext.getPackageName());
        intent.putExtra("_mmessage_content", sb2);
        intent.putExtra("_mmessage_support_content_type", 0);
        intent.putExtra("_mmessage_checksum", ZmWxApiUtil.checkSum(sb2, 621086720, this.mContext.getPackageName()));
        this.mContext.sendBroadcast(intent, "com.tencent.mm.permission.MM_MESSAGE");
        return true;
    }

    public void unregisterApp() {
        Context context = this.mContext;
        if (context != null && ZmWxApiUtil.validateAppSignatureForPackage(context, CnLoginConstants.PACKAGE_NAME_WECHAT)) {
            if (!TextUtils.isEmpty(this.mAppid)) {
                StringBuilder sb = new StringBuilder();
                sb.append("weixin://unregisterapp?appid=");
                sb.append(this.mAppid);
                String sb2 = sb.toString();
                String packageName = this.mContext.getPackageName();
                Intent intent = new Intent("com.tencent.mm.plugin.openapi.Intent.ACTION_HANDLE_APP_UNREGISTER");
                intent.putExtra("_mmessage_sdkVersion", 621086720);
                intent.putExtra("_mmessage_appPackage", packageName);
                intent.putExtra("_mmessage_content", sb2);
                intent.putExtra("_mmessage_support_content_type", 0);
                intent.putExtra("_mmessage_checksum", ZmWxApiUtil.checkSum(sb2, 621086720, packageName));
                this.mContext.sendBroadcast(intent, "com.tencent.mm.permission.MM_MESSAGE");
            } else {
                ZMLog.m280e(TAG, "unregisterApp fail, appId is empty", new Object[0]);
            }
        }
    }

    public boolean sendReq(@NonNull String str, @NonNull String str2) {
        Context context = this.mContext;
        if (context == null || this.mAppid == null || !ZmWxApiUtil.validateAppSignatureForPackage(context, CnLoginConstants.PACKAGE_NAME_WECHAT) || !ZmWxApiUtil.checkArgs(str, str2)) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("_wxapi_command_type", 1);
        bundle.putString("_wxapi_sendauth_req_scope", str);
        bundle.putString("_wxapi_sendauth_req_state", str2);
        StringBuilder sb = new StringBuilder();
        sb.append("weixin://sendreq?appid=");
        sb.append(this.mAppid);
        String sb2 = sb.toString();
        Intent intent = new Intent();
        intent.setClassName(CnLoginConstants.PACKAGE_NAME_WECHAT, "com.tencent.mm.plugin.base.stub.WXEntryActivity");
        intent.putExtras(bundle);
        intent.putExtra("_mmessage_sdkVersion", 621086720);
        intent.putExtra("_mmessage_appPackage", this.mContext.getPackageName());
        intent.putExtra("_mmessage_content", sb2);
        intent.putExtra("_mmessage_checksum", ZmWxApiUtil.checkSum(sb2, 621086720, this.mContext.getPackageName()));
        intent.addFlags(268435456);
        intent.addFlags(134217728);
        try {
            this.mContext.startActivity(intent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean isWxAppInstalled() {
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(CnLoginConstants.PACKAGE_NAME_WECHAT, 64);
            if (packageInfo != null) {
                if (packageInfo.signatures != null) {
                    return ZmWxApiUtil.validateAppSignature(packageInfo.signatures);
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
