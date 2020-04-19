package com.microsoft.aad.adal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import com.microsoft.aad.adal.HttpAuthDialog.CancelListener;
import com.microsoft.aad.adal.HttpAuthDialog.OkListener;
import java.util.HashMap;
import java.util.Locale;

abstract class BasicWebViewClient extends WebViewClient {
    public static final String BLANK_PAGE = "about:blank";
    private static final String INSTALL_URL_KEY = "app_link";
    private static final String TAG = "BasicWebViewClient";
    private final Context mCallingContext;
    private final String mRedirect;
    /* access modifiers changed from: private */
    public final AuthenticationRequest mRequest;
    private final UIEvent mUIEvent;

    public abstract void cancelWebViewRequest();

    public abstract void postRunnable(Runnable runnable);

    public abstract void prepareForBrokerResumeRequest();

    public abstract boolean processInvalidUrl(WebView webView, String str);

    public abstract void processRedirectUrl(WebView webView, String str);

    public abstract void sendResponse(int i, Intent intent);

    public abstract void setPKeyAuthStatus(boolean z);

    public abstract void showSpinner(boolean z);

    BasicWebViewClient(Context context, String str, AuthenticationRequest authenticationRequest, UIEvent uIEvent) {
        this.mCallingContext = context;
        this.mRedirect = str;
        this.mRequest = authenticationRequest;
        this.mUIEvent = uIEvent;
    }

    public void onReceivedHttpAuthRequest(WebView webView, final HttpAuthHandler httpAuthHandler, String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Host:");
        sb.append(str);
        Logger.m234i("BasicWebViewClient:onReceivedHttpAuthRequest", "Start. ", sb.toString());
        this.mUIEvent.setNTLM(true);
        HttpAuthDialog httpAuthDialog = new HttpAuthDialog(this.mCallingContext, str, str2);
        httpAuthDialog.setOkListener(new OkListener() {
            public void onOk(String str, String str2, String str3, String str4) {
                StringBuilder sb = new StringBuilder();
                sb.append("Host: ");
                sb.append(str);
                Logger.m234i("BasicWebViewClient:onReceivedHttpAuthRequest", "Handler proceed. ", sb.toString());
                httpAuthHandler.proceed(str3, str4);
            }
        });
        httpAuthDialog.setCancelListener(new CancelListener() {
            public void onCancel() {
                Logger.m234i("BasicWebViewClient:onReceivedHttpAuthRequest", "Handler cancelled", "");
                httpAuthHandler.cancel();
                BasicWebViewClient.this.cancelWebViewRequest();
            }
        });
        Logger.m234i("BasicWebViewClient:onReceivedHttpAuthRequest", "Show dialog. ", "");
        httpAuthDialog.show();
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        super.onReceivedError(webView, i, str, str2);
        showSpinner(false);
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Webview received an error. ErrorCode:");
        sb.append(i);
        Logger.m231e(str3, sb.toString(), str, ADALError.ERROR_WEBVIEW);
        Intent intent = new Intent();
        String str4 = Browser.RESPONSE_ERROR_CODE;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Error Code:");
        sb2.append(i);
        intent.putExtra(str4, sb2.toString());
        intent.putExtra(Browser.RESPONSE_ERROR_MESSAGE, str);
        intent.putExtra(Browser.RESPONSE_REQUEST_INFO, this.mRequest);
        sendResponse(2002, intent);
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        showSpinner(false);
        sslErrorHandler.cancel();
        Logger.m231e(TAG, "Received ssl error. ", "", ADALError.ERROR_FAILED_SSL_HANDSHAKE);
        Intent intent = new Intent();
        intent.putExtra(Browser.RESPONSE_ERROR_CODE, "Code:-11");
        intent.putExtra(Browser.RESPONSE_ERROR_MESSAGE, sslError.toString());
        intent.putExtra(Browser.RESPONSE_REQUEST_INFO, this.mRequest);
        sendResponse(2002, intent);
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        webView.setVisibility(0);
        if (!str.startsWith(BLANK_PAGE)) {
            showSpinner(false);
        }
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        logPageStartLoadingUrl(str);
        super.onPageStarted(webView, str, bitmap);
        showSpinner(true);
    }

    private void logPageStartLoadingUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            Logger.m236v("BasicWebViewClient:logPageStartLoadingUrl", "onPageStarted: Null url for page to load.");
            return;
        }
        Uri parse = Uri.parse(str);
        if (parse.isOpaque()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Url: ");
            sb.append(str);
            Logger.m237v("BasicWebViewClient:logPageStartLoadingUrl", "onPageStarted: Non-hierarchical loading uri. ", sb.toString(), null);
            return;
        }
        if (StringExtensions.isNullOrBlank(parse.getQueryParameter("code"))) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" Host: ");
            sb2.append(parse.getHost());
            sb2.append(" Path: ");
            sb2.append(parse.getPath());
            sb2.append(" Full loading url is: ");
            sb2.append(str);
            Logger.m237v("BasicWebViewClient:logPageStartLoadingUrl", "Webview starts loading. ", sb2.toString(), null);
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(" Host: ");
            sb3.append(parse.getHost());
            sb3.append(" Path: ");
            sb3.append(parse.getPath());
            sb3.append(" Auth code is returned for the loading url.");
            Logger.m237v("BasicWebViewClient:logPageStartLoadingUrl", "Webview starts loading. ", sb3.toString(), null);
        }
    }

    public boolean shouldOverrideUrlLoading(final WebView webView, final String str) {
        Logger.m236v("BasicWebViewClient:shouldOverrideUrlLoading", "Navigation is detected");
        if (str.startsWith(Broker.PKEYAUTH_REDIRECT)) {
            Logger.m236v("BasicWebViewClient:shouldOverrideUrlLoading", "Webview detected request for pkeyauth challenge.");
            webView.stopLoading();
            setPKeyAuthStatus(true);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        final ChallengeResponse challengeResponseFromUri = new ChallengeResponseBuilder(new JWSBuilder()).getChallengeResponseFromUri(str);
                        final HashMap hashMap = new HashMap();
                        hashMap.put("Authorization", challengeResponseFromUri.getAuthorizationHeaderValue());
                        BasicWebViewClient.this.postRunnable(new Runnable() {
                            public void run() {
                                String submitUrl = challengeResponseFromUri.getSubmitUrl();
                                StringBuilder sb = new StringBuilder();
                                sb.append("Challenge submit url:");
                                sb.append(challengeResponseFromUri.getSubmitUrl());
                                Logger.m237v("BasicWebViewClient:shouldOverrideUrlLoading", "Respond to pkeyAuth challenge", sb.toString(), null);
                                webView.loadUrl(submitUrl, hashMap);
                            }
                        });
                    } catch (AuthenticationServerProtocolException e) {
                        Logger.m232e("BasicWebViewClient:shouldOverrideUrlLoading", "Argument exception. ", e.getMessage(), ADALError.ARGUMENT_EXCEPTION, e);
                        Intent intent = new Intent();
                        intent.putExtra(Browser.RESPONSE_AUTHENTICATION_EXCEPTION, e);
                        if (BasicWebViewClient.this.mRequest != null) {
                            intent.putExtra(Browser.RESPONSE_REQUEST_INFO, BasicWebViewClient.this.mRequest);
                        }
                        BasicWebViewClient.this.sendResponse(2005, intent);
                    } catch (AuthenticationException e2) {
                        Logger.m232e("BasicWebViewClient:shouldOverrideUrlLoading", "It is failed to create device certificate response", e2.getMessage(), ADALError.DEVICE_CERTIFICATE_RESPONSE_FAILED, e2);
                        Intent intent2 = new Intent();
                        intent2.putExtra(Browser.RESPONSE_AUTHENTICATION_EXCEPTION, e2);
                        if (BasicWebViewClient.this.mRequest != null) {
                            intent2.putExtra(Browser.RESPONSE_REQUEST_INFO, BasicWebViewClient.this.mRequest);
                        }
                        BasicWebViewClient.this.sendResponse(2005, intent2);
                    }
                }
            }).start();
            return true;
        } else if (str.toLowerCase(Locale.US).startsWith(this.mRedirect.toLowerCase(Locale.US))) {
            Logger.m236v("BasicWebViewClient:shouldOverrideUrlLoading", "Navigation starts with the redirect uri.");
            if (hasCancelError(str)) {
                Logger.m234i("BasicWebViewClient:shouldOverrideUrlLoading", "Sending intent to cancel authentication activity", "");
                webView.stopLoading();
                cancelWebViewRequest();
                return true;
            }
            processRedirectUrl(webView, str);
            return true;
        } else if (str.startsWith(Broker.BROWSER_EXT_PREFIX)) {
            Logger.m236v("BasicWebViewClient:shouldOverrideUrlLoading", "It is an external website request");
            openLinkInBrowser(str);
            webView.stopLoading();
            cancelWebViewRequest();
            return true;
        } else if (!str.startsWith(Broker.BROWSER_EXT_INSTALL_PREFIX)) {
            return processInvalidUrl(webView, str);
        } else {
            Logger.m236v("BasicWebViewClient:shouldOverrideUrlLoading", "It is an install request");
            HashMap urlParameters = StringExtensions.getUrlParameters(str);
            prepareForBrokerResumeRequest();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException unused) {
                Logger.m236v("BasicWebViewClient:shouldOverrideUrlLoading", "Error occurred when having thread sleeping for 1 second.");
            }
            openLinkInBrowser((String) urlParameters.get(INSTALL_URL_KEY));
            webView.stopLoading();
            return true;
        }
    }

    /* access modifiers changed from: 0000 */
    public final Context getCallingContext() {
        return this.mCallingContext;
    }

    /* access modifiers changed from: protected */
    public void openLinkInBrowser(String str) {
        this.mCallingContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str.replace(Broker.BROWSER_EXT_PREFIX, "https://"))));
    }

    private boolean hasCancelError(String str) {
        HashMap urlParameters = StringExtensions.getUrlParameters(str);
        String str2 = (String) urlParameters.get("error");
        String str3 = (String) urlParameters.get("error_description");
        if (StringExtensions.isNullOrBlank(str2)) {
            return false;
        }
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Cancel error: ");
        sb.append(str2);
        Logger.m239w(str4, sb.toString(), str3, null);
        return true;
    }
}
