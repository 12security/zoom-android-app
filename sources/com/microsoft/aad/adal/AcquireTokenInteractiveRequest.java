package com.microsoft.aad.adal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager.BadTokenException;
import android.webkit.ClientCertRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

final class AcquireTokenInteractiveRequest {
    /* access modifiers changed from: private */
    public static final String TAG = "AcquireTokenInteractiveRequest";
    /* access modifiers changed from: private */
    public final AcquireTokenRequest mAcquireTokenRequest;
    /* access modifiers changed from: private */
    public final AuthenticationRequest mAuthRequest;
    private final Context mContext;
    private final TokenCacheAccessor mTokenCacheAccessor;

    private class OAuthDialog extends Dialog implements OnCancelListener {
        private WebView mWebView;

        class DialogWebViewClient extends BasicWebViewClient {
            public void postRunnable(Runnable runnable) {
            }

            public void prepareForBrokerResumeRequest() {
            }

            public boolean processInvalidUrl(WebView webView, String str) {
                return false;
            }

            public void setPKeyAuthStatus(boolean z) {
            }

            public void showSpinner(boolean z) {
            }

            DialogWebViewClient(Context context, String str, AuthenticationRequest authenticationRequest) {
                super(context, str, authenticationRequest, null);
            }

            public void sendResponse(int i, Intent intent) {
                OAuthDialog.this.dismiss();
                if (!intent.hasExtra(Browser.REQUEST_ID)) {
                    intent.putExtra(Browser.REQUEST_ID, AcquireTokenInteractiveRequest.this.mAuthRequest.getRequestId());
                }
                AcquireTokenInteractiveRequest.this.mAcquireTokenRequest.onActivityResult(1001, i, intent);
            }

            public void processRedirectUrl(WebView webView, String str) {
                Intent intent = new Intent();
                intent.putExtra(Browser.RESPONSE_FINAL_URL, str);
                intent.putExtra(Browser.RESPONSE_REQUEST_INFO, AcquireTokenInteractiveRequest.this.mAuthRequest);
                intent.putExtra(Browser.REQUEST_ID, AcquireTokenInteractiveRequest.this.mAuthRequest.getRequestId());
                sendResponse(2003, intent);
                webView.stopLoading();
            }

            public void cancelWebViewRequest() {
                OAuthDialog.this.cancelFlow();
            }

            @TargetApi(21)
            public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
                Logger.m234i(AcquireTokenInteractiveRequest.TAG, "onReceivedClientCertRequest", "");
                if (VERSION.SDK_INT >= 21) {
                    Activity ownerActivity = OAuthDialog.this.getOwnerActivity();
                    if (ownerActivity != null) {
                        AcquireTokenInteractiveRequest.this.mAcquireTokenRequest.processCertification(ownerActivity, clientCertRequest, ":onReceivedClientCertRequest", this);
                    }
                }
            }
        }

        public OAuthDialog(Activity activity) {
            super(activity, 16973840);
            setOwnerActivity(activity);
        }

        public void onCancel(DialogInterface dialogInterface) {
            cancelFlow();
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"SetJavaScriptEnabled"})
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setOnCancelListener(this);
            LinearLayout linearLayout = new LinearLayout(getContext());
            if (this.mWebView == null) {
                this.mWebView = new WebView(getContext());
                this.mWebView.setWebViewClient(new DialogWebViewClient(getContext(), AcquireTokenInteractiveRequest.this.mAuthRequest.getRedirectUri(), AcquireTokenInteractiveRequest.this.mAuthRequest));
                this.mWebView.getSettings().setJavaScriptEnabled(true);
                try {
                    this.mWebView.loadUrl(new Oauth2(AcquireTokenInteractiveRequest.this.mAuthRequest).getCodeRequestUrl());
                    this.mWebView.setLayoutParams(new LayoutParams(-1, -1));
                    this.mWebView.setVisibility(0);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            linearLayout.addView(this.mWebView);
            linearLayout.setVisibility(0);
            linearLayout.forceLayout();
            addContentView(linearLayout, new LayoutParams(-1, -1));
        }

        /* access modifiers changed from: private */
        public void cancelFlow() {
            Logger.m234i(AcquireTokenInteractiveRequest.TAG, "Cancelling dialog", "");
            Intent intent = new Intent();
            intent.putExtra(Browser.REQUEST_ID, AcquireTokenInteractiveRequest.this.mAuthRequest.getRequestId());
            AcquireTokenInteractiveRequest.this.mAcquireTokenRequest.onActivityResult(1001, 2001, intent);
            if (isShowing()) {
                dismiss();
            }
        }
    }

    AcquireTokenInteractiveRequest(Context context, AuthenticationRequest authenticationRequest, TokenCacheAccessor tokenCacheAccessor, AcquireTokenRequest acquireTokenRequest) {
        this.mContext = context;
        this.mTokenCacheAccessor = tokenCacheAccessor;
        this.mAuthRequest = authenticationRequest;
        this.mAcquireTokenRequest = acquireTokenRequest;
    }

    /* access modifiers changed from: 0000 */
    public void acquireToken(IWindowComponent iWindowComponent, boolean z) throws AuthenticationException {
        HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
        if (PromptBehavior.FORCE_PROMPT == this.mAuthRequest.getPrompt()) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":acquireToken");
            Logger.m236v(sb.toString(), "FORCE_PROMPT is set for embedded flow, reset it as Always.");
            this.mAuthRequest.setPrompt(PromptBehavior.Always);
        }
        if (z) {
            final Activity activity = (Activity) this.mContext;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    OAuthDialog oAuthDialog = new OAuthDialog(activity);
                    Activity activity = activity;
                    if (activity != null && !activity.isFinishing()) {
                        try {
                            oAuthDialog.show();
                        } catch (BadTokenException e) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(AcquireTokenInteractiveRequest.TAG);
                            sb.append(":acquireToken");
                            Logger.m233e(sb.toString(), "OAuthDialog show WindowManager.BadTokenException", e);
                        }
                    }
                }
            });
        } else if (!startAuthenticationActivity(iWindowComponent)) {
            throw new AuthenticationException(ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED);
        }
    }

    /* access modifiers changed from: 0000 */
    public AuthenticationResult acquireTokenWithAuthCode(String str) throws AuthenticationException {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":acquireTokenWithAuthCode");
        Logger.m237v(sb.toString(), "Start token acquisition with auth code.", this.mAuthRequest.getLogInfo(), null);
        try {
            AuthenticationResult token = new Oauth2(this.mAuthRequest, new WebRequestHandler()).getToken(str);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":acquireTokenWithAuthCode");
            Logger.m236v(sb2.toString(), "OnActivityResult processed the result.");
            if (token == null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(TAG);
                sb3.append(":acquireTokenWithAuthCode");
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Returned result with exchanging auth code for token is null");
                sb5.append(getCorrelationInfo());
                Logger.m231e(sb4, sb5.toString(), "", ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN);
                throw new AuthenticationException(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, getCorrelationInfo());
            } else if (StringExtensions.isNullOrBlank(token.getErrorCode())) {
                if (!StringExtensions.isNullOrBlank(token.getAccessToken())) {
                    TokenCacheAccessor tokenCacheAccessor = this.mTokenCacheAccessor;
                    if (tokenCacheAccessor != null) {
                        try {
                            tokenCacheAccessor.updateTokenCache(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), token);
                        } catch (MalformedURLException e) {
                            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
                        }
                    }
                }
                return token;
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(TAG);
                sb6.append(":acquireTokenWithAuthCode");
                String sb7 = sb6.toString();
                StringBuilder sb8 = new StringBuilder();
                sb8.append(" ErrorCode:");
                sb8.append(token.getErrorCode());
                String sb9 = sb8.toString();
                StringBuilder sb10 = new StringBuilder();
                sb10.append(" ErrorDescription:");
                sb10.append(token.getErrorDescription());
                Logger.m231e(sb7, sb9, sb10.toString(), ADALError.AUTH_FAILED);
                ADALError aDALError = ADALError.AUTH_FAILED;
                StringBuilder sb11 = new StringBuilder();
                sb11.append(" ErrorCode:");
                sb11.append(token.getErrorCode());
                throw new AuthenticationException(aDALError, sb11.toString());
            }
        } catch (AuthenticationException | IOException e2) {
            StringBuilder sb12 = new StringBuilder();
            sb12.append("Error in processing code to get token. ");
            sb12.append(getCorrelationInfo());
            throw new AuthenticationException(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, sb12.toString(), e2);
        }
    }

    private boolean startAuthenticationActivity(IWindowComponent iWindowComponent) {
        Intent authenticationActivityIntent = getAuthenticationActivityIntent();
        if (!resolveIntent(authenticationActivityIntent)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":startAuthenticationActivity");
            Logger.m231e(sb.toString(), "Intent is not resolved", "", ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED);
            return false;
        }
        try {
            iWindowComponent.startActivityForResult(authenticationActivityIntent, 1001);
            return true;
        } catch (ActivityNotFoundException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":startAuthenticationActivity");
            Logger.m232e(sb2.toString(), "Activity login is not found after resolving intent", "", ADALError.DEVELOPER_ACTIVITY_IS_NOT_RESOLVED, e);
            return false;
        }
    }

    private Intent getAuthenticationActivityIntent() {
        Intent intent = new Intent();
        if (AuthenticationSettings.INSTANCE.getActivityPackageName() != null) {
            intent.setClassName(AuthenticationSettings.INSTANCE.getActivityPackageName(), AuthenticationActivity.class.getName());
        } else {
            intent.setClass(this.mContext, AuthenticationActivity.class);
        }
        intent.putExtra(Browser.REQUEST_MESSAGE, this.mAuthRequest);
        return intent;
    }

    private boolean resolveIntent(Intent intent) {
        return this.mContext.getPackageManager().resolveActivity(intent, 0) != null;
    }

    private String getCorrelationInfo() {
        return String.format(" CorrelationId: %s", new Object[]{this.mAuthRequest.getCorrelationId().toString()});
    }
}
