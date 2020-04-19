package com.zipow.videobox.login.model;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.MMSSOLoginFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IAuthSsoHandlerListener;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.ZMUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.thirdparty.login.LoginType;
import p021us.zoom.thirdparty.login.ThirdPartyLoginFactory;
import p021us.zoom.thirdparty.login.sso.SsoUtil;

abstract class AbstractSsoLogin extends AbstractMultiLogin implements IAuthSsoHandlerListener {
    private static final String TAG = "AbstractSsoLogin";
    private String mDomainSearchReqID;

    AbstractSsoLogin() {
    }

    public void onCreate() {
        PTUI.getInstance().addAuthSsoHandler(this);
    }

    public void saveInstanceState(@NonNull Bundle bundle) {
        bundle.putString("mDomainSearchReqID", this.mDomainSearchReqID);
    }

    public void restoreInstanceState(@NonNull Bundle bundle) {
        this.mDomainSearchReqID = bundle.getString("mDomainSearchReqID");
    }

    public void onDestroy() {
        PTUI.getInstance().removeAuthSsoHandler(this);
    }

    public void onSSOLoginTokenReturnKMS(String str, String str2, String str3) {
        PTUI.getInstance().removeAuthSsoHandler(this);
        startLoginSSOWithKMSToken(str, str2, str3);
    }

    public void onSSOLoginTokenReturn(String str) {
        PTUI.getInstance().removeAuthSsoHandler(this);
        startLoginSSOWithToken(str);
    }

    public void onQuerySSOVanityURL(String str, int i, String str2) {
        if (this.mIMultiLoginListener != null && this.mIMultiLoginListener.isUiAuthorizing() && TextUtils.equals(str, this.mDomainSearchReqID)) {
            ZMActivity context = getContext();
            if (context != null) {
                Fragment findFragmentByTag = context.getSupportFragmentManager().findFragmentByTag(MMSSOLoginFragment.class.getName());
                if (findFragmentByTag != null) {
                    if (this.mIMultiLoginListener != null) {
                        this.mIMultiLoginListener.showAuthorizing(false);
                    }
                    if (i != 0 || TextUtils.isEmpty(str2)) {
                        ((MMSSOLoginFragment) findFragmentByTag).onSSOError(i);
                    } else {
                        ((MMSSOLoginFragment) findFragmentByTag).updateSsoCloud();
                        loginSSOSite(str2);
                    }
                }
            }
        }
    }

    public void showSSOAuthUI(@NonNull String str) {
        ZMActivity context = getContext();
        if (context != null) {
            ThirdPartyLoginFactory.build(LoginType.Sso, ThirdPartyLoginFactory.buildSsoBundle(SsoUtil.formatUrl(str, PTApp.getInstance().getZMCID()))).login(context, ZMUtils.getDefaultBrowserPkgName(context));
        }
    }

    public void startLoginSSOWithToken(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            int loginWithSSOToken = PTApp.getInstance().loginWithSSOToken(str);
            if (loginWithSSOToken == 0) {
                if (this.mIMultiLoginListener != null) {
                    this.mIMultiLoginListener.afterCallLoginAPISuccess(101, true);
                }
            } else if (!LoginUtil.ShowRestrictedLoginErrorDlg(loginWithSSOToken, false) && this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.onAuthFailed(null);
            }
        }
    }

    public void startLoginSSOWithKMSToken(String str, String str2, String str3) {
        if (!StringUtil.isEmptyOrNull(str)) {
            int loginWithSSOKMSToken = PTApp.getInstance().loginWithSSOKMSToken(str, str2, str3);
            if (loginWithSSOKMSToken == 0) {
                if (this.mIMultiLoginListener != null) {
                    this.mIMultiLoginListener.afterCallLoginAPISuccess(101, true);
                }
            } else if (!LoginUtil.ShowRestrictedLoginErrorDlg(loginWithSSOKMSToken, false) && this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.onAuthFailed(null);
            }
        }
    }

    public void onClickBtnLoginSSO() {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.beforeShowLoginUI(101, true);
        }
        doLoginWithSSO();
    }

    public void doLoginWithSSO() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            showNetworkError();
            return;
        }
        PTApp instance = PTApp.getInstance();
        int loginSSOWithLocalToken = !instance.isTokenExpired() ? instance.loginSSOWithLocalToken() : 1;
        if (loginSSOWithLocalToken == 0) {
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.afterCallLoginAPISuccess(101, true);
            }
        } else if (!LoginUtil.ShowRestrictedLoginErrorDlg(loginSSOWithLocalToken, false)) {
            ZMActivity context = getContext();
            if (context != null) {
                MMSSOLoginFragment.showAsDialog(context.getSupportFragmentManager());
            }
        }
    }

    public void loginSSOSite(String str) {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            showNetworkError();
        } else {
            showSSOAuthUI(str);
        }
    }

    public void onSSOAuthFailed(int i) {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.showAuthorizing(false);
        }
        ZMActivity context = getContext();
        if (context != null) {
            Fragment findFragmentByTag = context.getSupportFragmentManager().findFragmentByTag(MMSSOLoginFragment.class.getName());
            if (findFragmentByTag != null) {
                ((MMSSOLoginFragment) findFragmentByTag).onSSOError(i);
            }
        }
    }

    @Nullable
    public String querySSODomainByEmail(String str) {
        if (TextUtils.isEmpty(str) || !StringUtil.isValidEmailAddress(str)) {
            return null;
        }
        this.mDomainSearchReqID = PTApp.getInstance().querySSOVanityURL(str);
        if (!TextUtils.isEmpty(this.mDomainSearchReqID) && this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.showAuthorizing(true);
        }
        return this.mDomainSearchReqID;
    }
}
