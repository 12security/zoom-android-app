package com.zipow.videobox.login.model;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.login.AuthFailedDialog;
import com.zipow.videobox.login.ZMLoginForRealNameDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.thirdparty.AuthResult;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.p022cn.login.CnLoginCallBack;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.thirdparty.cnlogin.CnLoginConstants;
import p021us.zoom.thirdparty.cnlogin.CnLoginProxy;
import p021us.zoom.videomeetings.C4558R;

public class ZmChinaMultiLogin extends AbstractMultiLogin implements CnLoginCallBack {
    private static final String TAG = "com.zipow.videobox.login.model.ZmChinaMultiLogin";
    private SimplePTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, long j) {
            if (i == 78) {
                ZmChinaMultiLogin.this.sinkGetAuthResult(j);
            }
        }
    };
    private int mZoomSnsType = 102;

    public boolean onActivityResult(int i, int i2, Intent intent) {
        return false;
    }

    public boolean onAuthResult(AuthResult authResult) {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean onWebLogin(long j) {
        return super.onWebLogin(j);
    }

    public /* bridge */ /* synthetic */ void setILoginViewListener(@Nullable IMultiLoginListener iMultiLoginListener) {
        super.setILoginViewListener(iMultiLoginListener);
    }

    public boolean onIMLogin(long j, int i) {
        if (!ZmLoginHelper.isEnableChinaThirdpartyLogin()) {
            return false;
        }
        if (j != 3) {
            return j == 2 || j == 1;
        }
        if (!PTApp.getInstance().isAuthenticating()) {
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.showAuthorizing(false);
            }
            if (i == 21) {
                CnLoginProxy.getInstance().requestAuth(CnLoginType.Wechat, this);
                return true;
            } else if (i == 22) {
                CnLoginProxy.getInstance().requestAuth(CnLoginType.QQ, this);
                return true;
            } else if (i == 23) {
                CnLoginProxy.getInstance().requestAuth(CnLoginType.Alipay, this);
                return true;
            }
        }
    }

    public void initLoginType(int i) {
        switch (i) {
            case 21:
                onClickBtnLoginWeChat();
                return;
            case 22:
                onClickBtnLoginQQ();
                return;
            case 23:
                onClickBtnLoginAliPay();
                return;
            default:
                return;
        }
    }

    public void onCreate() {
        if (ZmLoginHelper.isEnableChinaThirdpartyLogin()) {
            if (getContext() != null) {
                CnLoginProxy.getInstance().init(true, getContext());
            }
            PTUI.getInstance().addPTUIListener(this.mPTUIListener);
        }
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt("mZoomSnsType", this.mZoomSnsType);
    }

    public void onResume() {
        if (ZmLoginHelper.isEnableChinaThirdpartyLogin() && getContext() != null) {
            CnLoginProxy.getInstance().registerApp();
        }
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        this.mZoomSnsType = bundle.getInt("mZoomSnsType");
    }

    public void onDestroy() {
        if (ZmLoginHelper.isEnableChinaThirdpartyLogin()) {
            CnLoginProxy.getInstance().unInit();
            PTUI.getInstance().removePTUIListener(this.mPTUIListener);
        }
    }

    public int getmZoomSnsType() {
        return this.mZoomSnsType;
    }

    public void onClickBtnLoginWeChat() {
        if (ZmLoginHelper.isEnableChinaThirdpartyLogin()) {
            if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
                showNetworkError();
                return;
            }
            this.mZoomSnsType = 21;
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.beforeShowLoginUI(this.mZoomSnsType, true);
            }
            CnLoginProxy.getInstance().requestAuth(CnLoginType.Wechat, this);
        }
    }

    public void onClickBtnLoginQQ() {
        if (ZmLoginHelper.isEnableChinaThirdpartyLogin()) {
            if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
                showNetworkError();
                return;
            }
            this.mZoomSnsType = 22;
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.beforeShowLoginUI(22, true);
            }
            CnLoginProxy.getInstance().requestAuth(CnLoginType.QQ, this);
        }
    }

    public void onClickBtnLoginAliPay() {
        if (ZmLoginHelper.isEnableChinaThirdpartyLogin()) {
            if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
                showNetworkError();
                return;
            }
            this.mZoomSnsType = 23;
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.beforeShowLoginUI(23, true);
            }
            CnLoginProxy.getInstance().requestAuth(CnLoginType.Alipay, this);
        }
    }

    public void onLoginSuccess(final CnLoginType cnLoginType, final Bundle bundle) {
        ZMActivity context = getContext();
        if (context != null) {
            context.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onLoginSuccess") {
                public void run(@NonNull IUIElement iUIElement) {
                    ZmChinaMultiLogin.this.handleLoginSuccess(cnLoginType, bundle);
                }
            });
        }
    }

    public void onLoginFail(CnLoginType cnLoginType, int i, String str) {
        ZMActivity context = getContext();
        if (context != null) {
            EventTaskManager nonNullEventTaskManagerOrThrowException = context.getNonNullEventTaskManagerOrThrowException();
            final CnLoginType cnLoginType2 = cnLoginType;
            final int i2 = i;
            final String str2 = str;
            C31583 r1 = new EventAction("onLoginFail") {
                public void run(@NonNull IUIElement iUIElement) {
                    ZmChinaMultiLogin.this.handleLoginFail(cnLoginType2, i2, str2);
                }
            };
            nonNullEventTaskManagerOrThrowException.push(r1);
        }
    }

    public void onLoginCancel(final CnLoginType cnLoginType) {
        ZMActivity context = getContext();
        if (context != null) {
            context.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onLoginCancel") {
                public void run(@NonNull IUIElement iUIElement) {
                    ZmChinaMultiLogin.this.handleLoginCancel(cnLoginType);
                }
            });
        }
    }

    public void onNotInstalled(final CnLoginType cnLoginType, final String str) {
        ZMActivity context = getContext();
        if (context != null) {
            context.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onNotInstalled") {
                public void run(@NonNull IUIElement iUIElement) {
                    ZmChinaMultiLogin.this.handleNotInstalled(cnLoginType, str);
                }
            });
        }
    }

    public boolean login(@NonNull String str, @NonNull String str2) {
        if (PTApp.getInstance().loginWithOAuthToken(this.mZoomSnsType, getAppId(), str, CompatUtils.getStardardCharSetUTF8().encode(str2).array()) != 0) {
            return false;
        }
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(this.mZoomSnsType, true);
        }
        return true;
    }

    public boolean loginWithRealName(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5) {
        if (PTApp.getInstance().loginWithOAuthTokenForRealName(this.mZoomSnsType, getAppId(), str, CompatUtils.getStardardCharSetUTF8().encode(str2).array(), str3, str4, str5) != 0) {
            return false;
        }
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(this.mZoomSnsType, true);
        }
        return true;
    }

    public boolean loginLocal() {
        if (PTApp.getInstance().loginWithLocalOAuthToken(this.mZoomSnsType, getAppId(), PTApp.getInstance().getLoginAuthOpenId()) != 0) {
            return false;
        }
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(this.mZoomSnsType, true);
        }
        return true;
    }

    public boolean loginLocalWithRealName(@NonNull String str, @NonNull String str2, @NonNull String str3) {
        if (PTApp.getInstance().loginWithLocalOAuthTokenForRealName(this.mZoomSnsType, getAppId(), PTApp.getInstance().getLoginAuthOpenId(), str, str2, str3) != 0) {
            return false;
        }
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(this.mZoomSnsType, true);
        }
        return true;
    }

    private void showBindDialog(String str, String str2) {
        ZMActivity context = getContext();
        if (context != null) {
            ZMLoginForRealNameDialog.show(context, str, str2);
        }
    }

    private void showAuthFail(@Nullable String str) {
        ZMActivity context = getContext();
        if (context != null) {
            if (StringUtil.isEmptyOrNull(str)) {
                ZMToast.show(context, context.getString(C4558R.string.zm_alert_auth_token_failed_msg), 1, 17, 0);
            } else {
                ZMToast.show(context, str, 1, 17, 0);
            }
        }
    }

    private void showAuthFail(long j) {
        ZMActivity context = getContext();
        if (context != null) {
            AuthFailedDialog.show(context, context.getResources().getString(C4558R.string.zm_alert_auth_error_code_msg, new Object[]{Long.valueOf(j)}));
        }
    }

    /* access modifiers changed from: private */
    public void sinkGetAuthResult(final long j) {
        ZMActivity context = getContext();
        if (context != null) {
            context.getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkGetAuthResult") {
                public void run(@NonNull IUIElement iUIElement) {
                    ZmChinaMultiLogin.this.handleGetAuthResult(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleGetAuthResult(long j) {
        if (j != 0) {
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.showAuthorizing(false);
            }
            showAuthFail(j);
        } else if (PTApp.getInstance().needRealNameAuth()) {
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.showAuthorizing(false);
            }
            showBindDialog(null, null);
        } else {
            loginLocal();
        }
    }

    @Nullable
    private String getAppId() {
        int i = this.mZoomSnsType;
        if (i == 22) {
            return CnLoginProxy.QQ_CURRENT_APPID;
        }
        if (i == 21) {
            return CnLoginProxy.WECHAT_CURRENT_APPID;
        }
        if (i == 23) {
            return CnLoginProxy.ALIPAY_CURRENT_APPID;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void handleLoginSuccess(CnLoginType cnLoginType, Bundle bundle) {
        if (bundle != null) {
            boolean z = false;
            if (cnLoginType == CnLoginType.Wechat) {
                String string = bundle.getString(CnLoginConstants.KEY_LOGIN_RESULT_CODE);
                if (!StringUtil.isEmptyOrNull(string) && PTApp.getInstance().requestOAuthTokenWithCode(21, CnLoginProxy.WECHAT_CURRENT_APPID, string) == 0) {
                    z = true;
                }
                if (!z) {
                    showAuthFail((String) null);
                } else if (this.mIMultiLoginListener != null) {
                    this.mIMultiLoginListener.afterCallLoginAPISuccess(this.mZoomSnsType, true);
                }
            } else if (cnLoginType == CnLoginType.QQ) {
                String string2 = bundle.getString(CnLoginConstants.KEY_LOGIN_RESULT_ACCESS_TOKEN);
                String string3 = bundle.getString(CnLoginConstants.KEY_LOGIN_RESULT_OPEN_ID);
                if (StringUtil.isEmptyOrNull(string2) || StringUtil.isEmptyOrNull(string3)) {
                    showAuthFail((String) null);
                } else {
                    showBindDialog(string3, string2);
                }
            } else if (cnLoginType == CnLoginType.Alipay) {
                String string4 = bundle.getString(CnLoginConstants.KEY_LOGIN_RESULT_CODE);
                if (!StringUtil.isEmptyOrNull(string4) && PTApp.getInstance().requestOAuthTokenWithCode(23, CnLoginProxy.ALIPAY_CURRENT_APPID, string4) == 0) {
                    z = true;
                }
                if (!z) {
                    showAuthFail((String) null);
                } else if (this.mIMultiLoginListener != null) {
                    this.mIMultiLoginListener.afterCallLoginAPISuccess(this.mZoomSnsType, true);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleLoginFail(CnLoginType cnLoginType, int i, String str) {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.showAuthorizing(false);
        }
        showAuthFail(str);
    }

    /* access modifiers changed from: private */
    public void handleLoginCancel(CnLoginType cnLoginType) {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.showAuthorizing(false);
        }
        ZMActivity context = getContext();
        if (context != null) {
            ZMToast.show(context, context.getString(C4558R.string.zm_alert_auth_token_failed_msg), 1, 17, 0);
        }
    }

    /* access modifiers changed from: private */
    public void handleNotInstalled(CnLoginType cnLoginType, String str) {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.showAuthorizing(false);
        }
        ZMActivity context = getContext();
        if (context != null) {
            String str2 = null;
            if (cnLoginType == CnLoginType.Alipay) {
                str2 = context.getString(C4558R.string.zm_description_login_with_alipay_137212);
            } else if (cnLoginType == CnLoginType.Wechat) {
                str2 = context.getString(C4558R.string.zm_description_login_with_wechat_137212);
            } else if (cnLoginType == CnLoginType.QQ) {
                str2 = context.getString(C4558R.string.zm_description_login_with_qq_137212);
            }
            if (!StringUtil.isEmptyOrNull(str2)) {
                ZMToast.show(context, context.getString(C4558R.string.zm_msg_install_app_137212, new Object[]{str2}), 1, 17, 0);
            }
        }
    }
}
