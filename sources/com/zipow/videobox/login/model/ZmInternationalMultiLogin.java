package com.zipow.videobox.login.model;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.dialog.FbConfirmCreateAccountDialog;
import com.zipow.videobox.dialog.IncompatibleClientDialog;
import com.zipow.videobox.login.ForceRedirectLoginDialog;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.FBAuthHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IAuthInternationalHandlerListener;
import com.zipow.videobox.thirdparty.AuthResult;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.ZMUtils;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.cache.IoUtils;
import p021us.zoom.androidlib.util.DeviceInfoUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.thirdparty.login.LoginType;
import p021us.zoom.thirdparty.login.ThirdPartyLoginFactory;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBAppAuthCallBack;
import p021us.zoom.thirdparty.login.facebook.FBAuthUtil;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.thirdparty.login.facebook.FacebookError;
import p021us.zoom.thirdparty.login.facebook.FbUserProfile;
import p021us.zoom.videomeetings.C4558R;

public class ZmInternationalMultiLogin extends AbstractSsoLogin implements IAuthInternationalHandlerListener {
    private static final String TAG = "ZmInternationalMultiLogin";
    private long mAppExpiresInSecond;
    private String mAppToken;
    /* access modifiers changed from: private */
    @Nullable
    public AuthToken mFacebookAuthToken;
    /* access modifiers changed from: private */
    public ZMAsyncTask<String, Void, FbUserProfile> mGetUserInfoZMAsyncTask;

    public /* bridge */ /* synthetic */ void doLoginWithSSO() {
        super.doLoginWithSSO();
    }

    public /* bridge */ /* synthetic */ void loginSSOSite(String str) {
        super.loginSSOSite(str);
    }

    public /* bridge */ /* synthetic */ void onClickBtnLoginSSO() {
        super.onClickBtnLoginSSO();
    }

    public /* bridge */ /* synthetic */ void onQuerySSOVanityURL(String str, int i, String str2) {
        super.onQuerySSOVanityURL(str, i, str2);
    }

    public /* bridge */ /* synthetic */ void onSSOAuthFailed(int i) {
        super.onSSOAuthFailed(i);
    }

    public /* bridge */ /* synthetic */ void onSSOLoginTokenReturn(String str) {
        super.onSSOLoginTokenReturn(str);
    }

    public /* bridge */ /* synthetic */ void onSSOLoginTokenReturnKMS(String str, String str2, String str3) {
        super.onSSOLoginTokenReturnKMS(str, str2, str3);
    }

    @Nullable
    public /* bridge */ /* synthetic */ String querySSODomainByEmail(String str) {
        return super.querySSODomainByEmail(str);
    }

    public /* bridge */ /* synthetic */ void restoreInstanceState(@NonNull Bundle bundle) {
        super.restoreInstanceState(bundle);
    }

    public /* bridge */ /* synthetic */ void saveInstanceState(@NonNull Bundle bundle) {
        super.saveInstanceState(bundle);
    }

    public /* bridge */ /* synthetic */ void setILoginViewListener(@Nullable IMultiLoginListener iMultiLoginListener) {
        super.setILoginViewListener(iMultiLoginListener);
    }

    public /* bridge */ /* synthetic */ void showSSOAuthUI(@NonNull String str) {
        super.showSSOAuthUI(str);
    }

    public /* bridge */ /* synthetic */ void startLoginSSOWithKMSToken(String str, String str2, String str3) {
        super.startLoginSSOWithKMSToken(str, str2, str3);
    }

    public /* bridge */ /* synthetic */ void startLoginSSOWithToken(String str) {
        super.startLoginSSOWithToken(str);
    }

    public void onGoogleAuthReturn(String str, String str2, long j, String str3) {
        PTUI.getInstance().removeAuthInternationalHandler(this);
        if (j == 0) {
            startGoogleIMAuth(str, str2);
        } else {
            showAuthError(C4558R.string.zm_alert_web_auth_failed_33814);
        }
    }

    public void onFacebookAuthReturn(String str, long j, long j2, String str2) {
        PTUI.getInstance().removeAuthInternationalHandler(this);
        if (j2 == 0) {
            startFBIMAuth(str, j);
        } else {
            showAuthError(C4558R.string.zm_alert_web_auth_failed_33814);
        }
    }

    public void onCreate() {
        super.onCreate();
        ZMActivity context = getContext();
        if (context != null && this.mFacebookAuthToken == null) {
            this.mFacebookAuthToken = FBSessionStore.getSession(context, FBSessionStore.FACEBOOK_KEY);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ZMAsyncTask<String, Void, FbUserProfile> zMAsyncTask = this.mGetUserInfoZMAsyncTask;
        if (zMAsyncTask != null) {
            if (!zMAsyncTask.isCancelled()) {
                this.mGetUserInfoZMAsyncTask.cancel(true);
            }
            this.mGetUserInfoZMAsyncTask = null;
        }
    }

    public boolean onIMLogin(long j, int i) {
        if (j != 3) {
            return j == 2 || j == 1;
        }
        if (!PTApp.getInstance().isAuthenticating()) {
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.showAuthorizing(false);
            }
            if (i == 2) {
                showGoogleAuthUI();
                return true;
            } else if (i == 0) {
                showFBAuthUI();
                return true;
            }
        }
    }

    public boolean onWebLogin(long j) {
        if (!super.onWebLogin(j)) {
            if (j == 2012) {
                if (this.mIMultiLoginListener == null || !this.mIMultiLoginListener.isUiAuthorizing()) {
                    return true;
                }
                PTApp.getInstance().logout(0);
                this.mIMultiLoginListener.showAuthorizing(false);
                ZMActivity context = getContext();
                if (context == null) {
                    return true;
                }
                ForceRedirectLoginDialog.newInstance(1).show(context.getSupportFragmentManager(), ForceRedirectLoginDialog.class.getName());
                return true;
            } else if (j == 1133) {
                if (this.mIMultiLoginListener == null || !this.mIMultiLoginListener.isUiAuthorizing()) {
                    return true;
                }
                PTApp.getInstance().setRencentJid("");
                PTApp.getInstance().logout(0);
                pullUserProfile();
                return true;
            } else if (j == 1037 || j == 1038) {
                if (this.mIMultiLoginListener == null || !this.mIMultiLoginListener.isUiAuthorizing()) {
                    return true;
                }
                PTApp.getInstance().setRencentJid("");
                PTApp.getInstance().logout(0);
                this.mIMultiLoginListener.showAuthorizing(false);
                ZMActivity context2 = getContext();
                if (context2 != null) {
                    IncompatibleClientDialog.showDialog(context2.getSupportFragmentManager(), j);
                }
                return true;
            }
        }
        return false;
    }

    public void initLoginType(int i) {
        if (i == 0) {
            onClickBtnLoginFacebook();
        } else if (i == 2) {
            onClickBtnLoginGoogle();
        } else if (i == 101) {
            onClickBtnLoginSSO();
        }
    }

    public boolean onActivityResult(int i, int i2, Intent intent) {
        if (i != 100) {
            return false;
        }
        AuthToken authToken = this.mFacebookAuthToken;
        if (authToken != null) {
            FBAuthUtil.authorizeCallback(authToken, i2, intent, new FBAppAuthCallBack() {
                public void forceAuthByBrowser() {
                }

                public void onCancelAuth() {
                    ZmInternationalMultiLogin.this.onFBAuthCancel();
                }

                public void onFaceBookError(@NonNull FacebookError facebookError) {
                    ZmInternationalMultiLogin.this.onFBAuthFailed(facebookError);
                }

                public void onError(String str) {
                    ZmInternationalMultiLogin.this.onFBAuthFailed(str);
                }

                public void onComplete(Bundle bundle) {
                    ZmInternationalMultiLogin zmInternationalMultiLogin = ZmInternationalMultiLogin.this;
                    zmInternationalMultiLogin.onFBAppAuthSuccess(zmInternationalMultiLogin.mFacebookAuthToken);
                }
            });
        }
        return true;
    }

    public boolean onAuthResult(AuthResult authResult) {
        if (VERSION.SDK_INT == 28 && DeviceInfoUtil.isSamsungSpecificDevice() && authResult == null) {
            return true;
        }
        int action = authResult.getAction();
        if (action == 8 || action == 10) {
            PTUI.getInstance().addAuthInternationalHandler(this);
            Mainboard.getMainboard().notifyUrlAction(authResult.getUrl());
            return true;
        } else if (action != 11) {
            return false;
        } else {
            if (authResult.isValid()) {
                PTUI.getInstance().addAuthSsoHandler(this);
                Mainboard.getMainboard().notifyUrlAction(authResult.getUrl());
            } else {
                onSSOAuthFailed(authResult.getErrorCode());
            }
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void onFBAppAuthSuccess(@NonNull final AuthToken authToken) {
        ZMActivity context = getContext();
        if (context != null) {
            context.getNonNullEventTaskManagerOrThrowException().push("onFBAuthSuccess", new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ZmInternationalMultiLogin.this.handleOnFBAppAuthSuccess(authToken);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnFBAppAuthSuccess(@NonNull AuthToken authToken) {
        ZMActivity context = getContext();
        if (context != null) {
            FBSessionStore.save(FBSessionStore.FACEBOOK_KEY, authToken, context);
            startFBAppIMAuth(authToken);
        }
    }

    public void loginFacebookWithAcceptedTos() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            showNetworkError();
            return;
        }
        PTApp instance = PTApp.getInstance();
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(0, true);
        }
        instance.loginWithFacebook(this.mAppToken, this.mAppExpiresInSecond, true);
    }

    public void doLoginWithFacebook() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            showNetworkError();
            return;
        }
        PTApp instance = PTApp.getInstance();
        int loginFacebookWithLocalToken = !instance.isTokenExpired() ? instance.loginFacebookWithLocalToken() : 1;
        if (loginFacebookWithLocalToken != 0) {
            PTApp.getInstance().logout(0);
            if (!LoginUtil.ShowRestrictedLoginErrorDlg(loginFacebookWithLocalToken, false)) {
                showFBAuthUI();
            }
        } else if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(0, true);
        }
    }

    public void doLoginWithGoogle() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            showNetworkError();
            return;
        }
        PTApp instance = PTApp.getInstance();
        int loginGoogleWithLocalToken = !instance.isTokenExpired() ? instance.loginGoogleWithLocalToken() : 1;
        if (loginGoogleWithLocalToken != 0) {
            PTApp.getInstance().logout(0);
            if (!LoginUtil.ShowRestrictedLoginErrorDlg(loginGoogleWithLocalToken, false)) {
                showGoogleAuthUI();
            }
        } else if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.afterCallLoginAPISuccess(2, true);
        }
    }

    private void showGoogleAuthUI() {
        FBAuthHelper fBAuthHelper = PTApp.getInstance().getFBAuthHelper();
        if (fBAuthHelper != null) {
            String generateGoogleLoginURL = fBAuthHelper.generateGoogleLoginURL();
            ZMActivity context = getContext();
            if (generateGoogleLoginURL != null && context != null) {
                ThirdPartyLoginFactory.build(LoginType.Google, ThirdPartyLoginFactory.buildGoogleBundle(generateGoogleLoginURL)).login(context, ZMUtils.getDefaultBrowserPkgName(context));
            }
        }
    }

    private void showFBAuthUI() {
        FBAuthHelper fBAuthHelper = PTApp.getInstance().getFBAuthHelper();
        if (fBAuthHelper != null) {
            String generateFBLoginURL = fBAuthHelper.generateFBLoginURL();
            ZMActivity context = getContext();
            if (generateFBLoginURL != null && context != null) {
                ThirdPartyLoginFactory.build(LoginType.Facebook, ThirdPartyLoginFactory.buildFacebookBundle(generateFBLoginURL, 100)).login(context, ZMUtils.getDefaultBrowserPkgName(context));
            }
        }
    }

    public void onFBAuthFailed(String str) {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.onAuthFailed(str);
        }
    }

    public void onFBAuthFailed(FacebookError facebookError) {
        String message = facebookError.getMessage();
        if (message != null && message.indexOf("invalid_key:Android key mismatch") >= 0) {
            message = "Invalid debug build to integrate with Facebook App. Please contact Reed Yang to get debug keystore.";
        }
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.onAuthFailed(message);
        }
    }

    public void onFBAuthCancel() {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.showAuthorizing(false);
        }
        if (PTApp.getInstance().getPTLoginType() == 0) {
            PTApp.getInstance().logout(0);
        }
    }

    public void startFBAppIMAuth(AuthToken authToken) {
        if (!StringUtil.isEmptyOrNull(authToken.token)) {
            long currentTimeMillis = (authToken.expires - System.currentTimeMillis()) / 1000;
            this.mAppToken = authToken.token;
            this.mAppExpiresInSecond = currentTimeMillis;
            PTApp instance = PTApp.getInstance();
            instance.loginXmppServer(authToken.token);
            instance.loginWithFacebook(authToken.token, currentTimeMillis, false);
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.afterCallLoginAPISuccess(0, true);
            }
        }
    }

    public void startFBIMAuth(String str, long j) {
        if (!StringUtil.isEmptyOrNull(str)) {
            PTApp instance = PTApp.getInstance();
            instance.loginXmppServer(str);
            instance.loginWithFacebookWithToken(str, j);
            if (this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.afterCallLoginAPISuccess(0, true);
            }
        }
    }

    public void startGoogleIMAuth(String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            int loginGoogleWithCodes = PTApp.getInstance().loginGoogleWithCodes(str, str2);
            if (loginGoogleWithCodes == 0) {
                if (this.mIMultiLoginListener != null) {
                    this.mIMultiLoginListener.afterCallLoginAPISuccess(2, true);
                }
            } else if (loginGoogleWithCodes == 6000) {
                ZMActivity context = getContext();
                if (!(context == null || this.mIMultiLoginListener == null)) {
                    this.mIMultiLoginListener.onAuthFailed(context.getResources().getString(C4558R.string.zm_alert_web_auth_failed_33814));
                }
            } else if (!LoginUtil.ShowRestrictedLoginErrorDlg(loginGoogleWithCodes, false) && this.mIMultiLoginListener != null) {
                this.mIMultiLoginListener.onAuthFailed(null);
            }
        }
    }

    public void onClickBtnLoginFacebook() {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.beforeShowLoginUI(0, true);
        }
        doLoginWithFacebook();
    }

    public void onClickBtnLoginGoogle() {
        if (this.mIMultiLoginListener != null) {
            this.mIMultiLoginListener.beforeShowLoginUI(2, true);
        }
        doLoginWithGoogle();
    }

    private void pullUserProfile() {
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            showNetworkError();
            return;
        }
        if (!StringUtil.isEmptyOrNull(this.mAppToken)) {
            this.mGetUserInfoZMAsyncTask = new ZMAsyncTask<String, Void, FbUserProfile>() {
                /* access modifiers changed from: protected */
                @Nullable
                public FbUserProfile doInBackground(String... strArr) {
                    InputStream inputStream;
                    InputStream inputStream2 = null;
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                        if (httpURLConnection.getResponseCode() != 200) {
                            inputStream = httpURLConnection.getErrorStream();
                        } else {
                            inputStream = httpURLConnection.getInputStream();
                        }
                        try {
                            FbUserProfile parse = FbUserProfile.parse(inputStream);
                            IoUtils.closeSilently(inputStream);
                            if (isCancelled()) {
                                return null;
                            }
                            return parse;
                        } catch (Exception unused) {
                            IoUtils.closeSilently(inputStream);
                            return null;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            inputStream2 = inputStream;
                            th = th2;
                            IoUtils.closeSilently(inputStream2);
                            throw th;
                        }
                    } catch (Exception unused2) {
                        inputStream = null;
                        IoUtils.closeSilently(inputStream);
                        return null;
                    } catch (Throwable th3) {
                        th = th3;
                        IoUtils.closeSilently(inputStream2);
                        throw th;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(FbUserProfile fbUserProfile) {
                    ZmInternationalMultiLogin.this.mGetUserInfoZMAsyncTask = null;
                    ZMActivity context = ZmInternationalMultiLogin.this.getContext();
                    if (context != null && !context.isDestroyed()) {
                        if (ZmInternationalMultiLogin.this.mIMultiLoginListener != null) {
                            ZmInternationalMultiLogin.this.mIMultiLoginListener.showAuthorizing(false);
                        }
                        if (fbUserProfile == null) {
                            ZmInternationalMultiLogin.this.showNetworkError();
                        } else if (!StringUtil.isEmptyOrNull(fbUserProfile.getErrorMsg())) {
                            if (ZmInternationalMultiLogin.this.mIMultiLoginListener != null) {
                                ZmInternationalMultiLogin.this.mIMultiLoginListener.onAuthFailed(fbUserProfile.getErrorMsg());
                            }
                        } else {
                            FbConfirmCreateAccountDialog.show(context, fbUserProfile);
                        }
                    }
                }
            };
            this.mGetUserInfoZMAsyncTask.execute((Params[]) new String[]{FBAuthUtil.generateGraphUserUrl(this.mAppToken)});
        }
    }
}
