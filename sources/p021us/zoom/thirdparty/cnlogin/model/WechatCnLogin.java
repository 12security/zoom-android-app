package p021us.zoom.thirdparty.cnlogin.model;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.p022cn.login.CnLoginCallBack;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.thirdparty.cnlogin.CnLoginConstants;
import p021us.zoom.thirdparty.cnlogin.CnLoginProxy;
import p021us.zoom.videomeetings.wxapi.ZmWxApi;

/* renamed from: us.zoom.thirdparty.cnlogin.model.WechatCnLogin */
public class WechatCnLogin extends AbstractCnLogin {
    private static final String LOGIN_STATE = "WX_LOGIN_STATE";
    private static final String SCOPE = "snsapi_userinfo";
    private String TAG = WechatCnLogin.class.getSimpleName();
    @Nullable
    private WeakReference<CnLoginCallBack> mCnLoginCallBackWR;
    @Nullable
    private ZmWxApi mZmWxApi;

    public void init(@NonNull Activity activity) {
        if (this.mZmWxApi == null) {
            this.mZmWxApi = new ZmWxApi(activity.getApplicationContext());
        }
    }

    public void registerApp() {
        ZmWxApi zmWxApi = this.mZmWxApi;
        if (zmWxApi != null) {
            zmWxApi.registerApp(CnLoginProxy.WECHAT_CURRENT_APPID);
        }
    }

    public void login(@NonNull CnLoginCallBack cnLoginCallBack) {
        this.mCnLoginCallBackWR = new WeakReference<>(cnLoginCallBack);
        ZmWxApi zmWxApi = this.mZmWxApi;
        if (zmWxApi == null) {
            cnLoginCallBack.onLoginFail(CnLoginType.Wechat, -1, "");
        } else if (!zmWxApi.isWxAppInstalled()) {
            cnLoginCallBack.onNotInstalled(CnLoginType.Wechat, CnLoginConstants.PACKAGE_NAME_WECHAT);
        } else {
            try {
                if (!this.mZmWxApi.sendReq(SCOPE, LOGIN_STATE)) {
                    cnLoginCallBack.onLoginFail(CnLoginType.Wechat, -1, "");
                }
            } catch (Exception unused) {
                cnLoginCallBack.onLoginFail(CnLoginType.Wechat, -1, "");
            }
        }
    }

    public void onResultCallBack(@NonNull ZmCnAuthResult zmCnAuthResult) {
        CnLoginCallBack cnLoginCallBack = getCnLoginCallBack();
        if (cnLoginCallBack != null) {
            if (zmCnAuthResult.getErrorCode() == 0) {
                WeChatData weChatData = (WeChatData) zmCnAuthResult.getData();
                if (TextUtils.isEmpty(weChatData.getmCode()) || !LOGIN_STATE.equals(weChatData.getmState())) {
                    cnLoginCallBack.onLoginFail(CnLoginType.Wechat, -10, "");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(CnLoginConstants.KEY_LOGIN_RESULT_CODE, weChatData.getmCode());
                    cnLoginCallBack.onLoginSuccess(CnLoginType.Wechat, bundle);
                }
            } else if (zmCnAuthResult.getErrorCode() == -2 || zmCnAuthResult.getErrorCode() == -4) {
                cnLoginCallBack.onLoginCancel(CnLoginType.Wechat);
            } else {
                cnLoginCallBack.onLoginFail(CnLoginType.Wechat, -11, zmCnAuthResult.getErrStr());
            }
        }
    }

    public void unInit() {
        ZmWxApi zmWxApi = this.mZmWxApi;
        if (zmWxApi != null) {
            zmWxApi.unregisterApp();
            this.mZmWxApi = null;
        }
        WeakReference<CnLoginCallBack> weakReference = this.mCnLoginCallBackWR;
        if (weakReference != null) {
            weakReference.clear();
            this.mCnLoginCallBackWR = null;
        }
    }

    @Nullable
    private CnLoginCallBack getCnLoginCallBack() {
        WeakReference<CnLoginCallBack> weakReference = this.mCnLoginCallBackWR;
        if (weakReference != null) {
            return (CnLoginCallBack) weakReference.get();
        }
        return null;
    }
}
