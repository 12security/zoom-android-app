package com.microsoft.services.msa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.microsoft.services.msa.OAuth.ResponseType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.http.client.HttpClient;

class AuthorizationRequest implements ObservableOAuthRequest, OAuthRequestObserver {
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    /* access modifiers changed from: private */
    public final Activity activity;
    private final HttpClient client;
    private final String clientId;
    private final String loginHint;
    /* access modifiers changed from: private */
    public final OAuthConfig mOAuthConfig;
    private final DefaultObservableOAuthRequest observable;
    private final String scope;

    private class OAuthDialog extends Dialog implements OnCancelListener {
        private final Uri requestUri;
        private WebView webView;

        private class AuthorizationWebViewClient extends WebViewClient {
            private final Set<String> cookieKeys = new HashSet();
            private final CookieManager cookieManager = CookieManager.getInstance();

            public AuthorizationWebViewClient() {
                CookieSyncManager.createInstance(OAuthDialog.this.getContext());
            }

            public void onPageFinished(WebView webView, String str) {
                Uri parse = Uri.parse(str);
                if (AuthorizationRequest.this.mOAuthConfig.getLogoutUri().getHost().equals(parse.getHost())) {
                    saveCookiesInMemory(this.cookieManager.getCookie(str));
                }
                if (UriComparator.INSTANCE.compare(parse, AuthorizationRequest.this.mOAuthConfig.getDesktopUri()) == 0) {
                    saveCookiesToPreferences();
                    AuthorizationRequest.this.onEndUri(parse);
                    dismissDialog();
                }
            }

            private void dismissDialog() {
                if (OAuthDialog.this.isShowing() && AuthorizationRequest.this.activity != null && !AuthorizationRequest.this.activity.isFinishing()) {
                    OAuthDialog.this.dismiss();
                }
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                if (i != -10) {
                    AuthorizationRequest.this.onError("", str, str2);
                    dismissDialog();
                }
            }

            private void saveCookiesInMemory(String str) {
                String[] split;
                if (!TextUtils.isEmpty(str)) {
                    for (String str2 : TextUtils.split(str, "; ")) {
                        this.cookieKeys.add(str2.substring(0, str2.indexOf(AuthorizationRequest.EQUALS)));
                    }
                }
            }

            private void saveCookiesToPreferences() {
                SharedPreferences sharedPreferences = OAuthDialog.this.getContext().getSharedPreferences(PreferencesConstants.FILE_NAME, 0);
                this.cookieKeys.addAll(Arrays.asList(TextUtils.split(sharedPreferences.getString(PreferencesConstants.COOKIES_KEY, ""), PreferencesConstants.COOKIE_DELIMITER)));
                Editor edit = sharedPreferences.edit();
                edit.putString(PreferencesConstants.COOKIES_KEY, TextUtils.join(PreferencesConstants.COOKIE_DELIMITER, this.cookieKeys));
                edit.commit();
                this.cookieKeys.clear();
            }
        }

        public OAuthDialog(Uri uri) {
            super(AuthorizationRequest.this.activity, 16973840);
            setOwnerActivity(AuthorizationRequest.this.activity);
            if (uri != null) {
                this.requestUri = uri;
                return;
            }
            throw new AssertionError();
        }

        public void onCancel(DialogInterface dialogInterface) {
            AuthorizationRequest.this.onException(new LiveAuthException(ErrorMessages.SIGNIN_CANCEL));
        }

        /* access modifiers changed from: protected */
        @SuppressLint({"SetJavaScriptEnabled"})
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setOnCancelListener(this);
            LinearLayout linearLayout = new LinearLayout(getContext());
            if (this.webView == null) {
                this.webView = new WebView(getContext());
                this.webView.setWebViewClient(new AuthorizationWebViewClient());
                this.webView.getSettings().setJavaScriptEnabled(true);
                this.webView.loadUrl(this.requestUri.toString());
                this.webView.setLayoutParams(new LayoutParams(-1, -1));
                this.webView.setVisibility(0);
            }
            linearLayout.addView(this.webView);
            linearLayout.setVisibility(0);
            linearLayout.forceLayout();
            addContentView(linearLayout, new LayoutParams(-1, -1));
        }
    }

    private enum UriComparator implements Comparator<Uri> {
        INSTANCE;

        public int compare(Uri uri, Uri uri2) {
            String[] strArr = {uri.getScheme(), uri.getAuthority(), uri.getPath()};
            String[] strArr2 = {uri2.getScheme(), uri2.getAuthority(), uri2.getPath()};
            if (strArr.length == strArr2.length) {
                for (int i = 0; i < strArr.length; i++) {
                    if (strArr[i] == null && strArr2[i] == null) {
                        return 0;
                    }
                    int compareTo = strArr[i].compareTo(strArr2[i]);
                    if (compareTo != 0) {
                        return compareTo;
                    }
                }
                return 0;
            }
            throw new AssertionError();
        }
    }

    private static Map<String, String> getFragmentParametersMap(Uri uri) {
        String[] split = TextUtils.split(uri.getFragment(), AMPERSAND);
        HashMap hashMap = new HashMap();
        for (String str : split) {
            int indexOf = str.indexOf(EQUALS);
            hashMap.put(str.substring(0, indexOf), str.substring(indexOf + 1));
        }
        return hashMap;
    }

    public AuthorizationRequest(Activity activity2, HttpClient httpClient, String str, String str2, String str3, OAuthConfig oAuthConfig) {
        if (activity2 == null) {
            throw new AssertionError();
        } else if (httpClient == null) {
            throw new AssertionError();
        } else if (TextUtils.isEmpty(str)) {
            throw new AssertionError();
        } else if (!TextUtils.isEmpty(str2)) {
            this.activity = activity2;
            this.client = httpClient;
            this.clientId = str;
            this.mOAuthConfig = oAuthConfig;
            this.observable = new DefaultObservableOAuthRequest();
            this.scope = str2;
            this.loginHint = str3;
        } else {
            throw new AssertionError();
        }
    }

    public void addObserver(OAuthRequestObserver oAuthRequestObserver) {
        this.observable.addObserver(oAuthRequestObserver);
    }

    public void execute() {
        String displayParameter = getDisplayParameter();
        String lowerCase = ResponseType.CODE.toString().toLowerCase(Locale.US);
        Locale.getDefault().toString();
        Builder appendQueryParameter = this.mOAuthConfig.getAuthorizeUri().buildUpon().appendQueryParameter("client_id", this.clientId).appendQueryParameter("scope", this.scope).appendQueryParameter(OAuth.DISPLAY, displayParameter).appendQueryParameter("response_type", lowerCase).appendQueryParameter("redirect_uri", this.mOAuthConfig.getDesktopUri().toString());
        String str = this.loginHint;
        if (str != null) {
            appendQueryParameter.appendQueryParameter("login_hint", str);
            appendQueryParameter.appendQueryParameter(OAuth.USER_NAME, this.loginHint);
        }
        new OAuthDialog(appendQueryParameter.build()).show();
    }

    public void onException(LiveAuthException liveAuthException) {
        this.observable.notifyObservers(liveAuthException);
    }

    public void onResponse(OAuthResponse oAuthResponse) {
        this.observable.notifyObservers(oAuthResponse);
    }

    public boolean removeObserver(OAuthRequestObserver oAuthRequestObserver) {
        return this.observable.removeObserver(oAuthRequestObserver);
    }

    private String getDisplayParameter() {
        return ScreenSize.determineScreenSize(this.activity).getDeviceType().getDisplayParameter().toString().toLowerCase(Locale.US);
    }

    private void onAccessTokenResponse(Map<String, String> map) {
        if (map != null) {
            try {
                onResponse(OAuthSuccessfulResponse.createFromFragment(map));
            } catch (LiveAuthException e) {
                onException(e);
            }
        } else {
            throw new AssertionError();
        }
    }

    private void onAuthorizationResponse(String str) {
        if (!TextUtils.isEmpty(str)) {
            TokenRequestAsync tokenRequestAsync = new TokenRequestAsync(new AccessTokenRequest(this.client, this.clientId, str, this.mOAuthConfig));
            tokenRequestAsync.addObserver(this);
            tokenRequestAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: private */
    public void onEndUri(Uri uri) {
        boolean z = uri.getFragment() != null;
        boolean z2 = uri.getQuery() != null;
        boolean z3 = !z && !z2;
        boolean isHierarchical = uri.isHierarchical();
        if (z3) {
            onInvalidUri();
            return;
        }
        if (z) {
            Map fragmentParametersMap = getFragmentParametersMap(uri);
            if (fragmentParametersMap.containsKey("access_token") && fragmentParametersMap.containsKey("token_type")) {
                onAccessTokenResponse(fragmentParametersMap);
                return;
            }
            String str = (String) fragmentParametersMap.get("error");
            if (str != null) {
                onError(str, (String) fragmentParametersMap.get("error_description"), (String) fragmentParametersMap.get(OAuth.ERROR_URI));
                return;
            }
        }
        if (z2 && isHierarchical) {
            String queryParameter = uri.getQueryParameter("code");
            if (queryParameter != null) {
                onAuthorizationResponse(queryParameter);
                return;
            }
            String queryParameter2 = uri.getQueryParameter("error");
            if (queryParameter2 != null) {
                onError(queryParameter2, uri.getQueryParameter("error_description"), uri.getQueryParameter(OAuth.ERROR_URI));
                return;
            }
        }
        if (z2 && !isHierarchical) {
            String[] split = uri.getQuery().split("&|=");
            for (int i = 0; i < split.length; i = 2) {
                if (split[i].equals("code")) {
                    onAuthorizationResponse(split[i + 1]);
                    return;
                }
            }
        }
        onInvalidUri();
    }

    /* access modifiers changed from: private */
    public void onError(String str, String str2, String str3) {
        onException(new LiveAuthException(str, str2, str3));
    }

    private void onInvalidUri() {
        onException(new LiveAuthException(ErrorMessages.SERVER_ERROR));
    }
}
