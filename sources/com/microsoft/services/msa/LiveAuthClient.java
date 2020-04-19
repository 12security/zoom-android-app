package com.microsoft.services.msa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.microsoft.services.msa.OAuth.ErrorType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class LiveAuthClient {
    private static final LiveAuthListener NULL_LISTENER = new LiveAuthListener() {
        public void onAuthComplete(LiveStatus liveStatus, LiveConnectSession liveConnectSession, Object obj) {
        }

        public void onAuthError(LiveAuthException liveAuthException, Object obj) {
        }
    };
    private static final String TAG = "LiveAuthClient";
    /* access modifiers changed from: private */
    public final Context applicationContext;
    private Set<String> baseScopes;
    private final String clientId;
    /* access modifiers changed from: private */
    public boolean hasPendingLoginRequest;
    private HttpClient httpClient;
    private final OAuthConfig mOAuthConfig;
    /* access modifiers changed from: private */
    public final LiveConnectSession session;

    private static class AuthCompleteRunnable extends AuthListenerCaller implements Runnable {
        private final LiveConnectSession session;
        private final LiveStatus status;

        public AuthCompleteRunnable(LiveAuthListener liveAuthListener, Object obj, LiveStatus liveStatus, LiveConnectSession liveConnectSession) {
            super(liveAuthListener, obj);
            this.status = liveStatus;
            this.session = liveConnectSession;
        }

        public void run() {
            this.listener.onAuthComplete(this.status, this.session, this.userState);
        }
    }

    private static class AuthErrorRunnable extends AuthListenerCaller implements Runnable {
        private final LiveAuthException exception;

        public AuthErrorRunnable(LiveAuthListener liveAuthListener, Object obj, LiveAuthException liveAuthException) {
            super(liveAuthListener, obj);
            this.exception = liveAuthException;
        }

        public void run() {
            this.listener.onAuthError(this.exception, this.userState);
        }
    }

    private static abstract class AuthListenerCaller {
        protected final LiveAuthListener listener;
        protected final Object userState;

        public AuthListenerCaller(LiveAuthListener liveAuthListener, Object obj) {
            this.listener = liveAuthListener;
            this.userState = obj;
        }
    }

    private class ListenerCallerObserver extends AuthListenerCaller implements OAuthRequestObserver, OAuthResponseVisitor {
        public ListenerCallerObserver(LiveAuthListener liveAuthListener, Object obj) {
            super(liveAuthListener, obj);
        }

        public void onException(LiveAuthException liveAuthException) {
            new AuthErrorRunnable(this.listener, this.userState, liveAuthException).run();
        }

        public void onResponse(OAuthResponse oAuthResponse) {
            oAuthResponse.accept(this);
        }

        public void visit(OAuthErrorResponse oAuthErrorResponse) {
            new AuthErrorRunnable(this.listener, this.userState, new LiveAuthException(oAuthErrorResponse.getError().toString().toLowerCase(Locale.US), oAuthErrorResponse.getErrorDescription(), oAuthErrorResponse.getErrorUri())).run();
        }

        public void visit(OAuthSuccessfulResponse oAuthSuccessfulResponse) {
            LiveAuthClient.this.session.loadFromOAuthResponse(oAuthSuccessfulResponse);
            new AuthCompleteRunnable(this.listener, this.userState, LiveStatus.CONNECTED, LiveAuthClient.this.session).run();
        }
    }

    private class RefreshTokenWriter implements OAuthRequestObserver, OAuthResponseVisitor {
        public void onException(LiveAuthException liveAuthException) {
        }

        private RefreshTokenWriter() {
        }

        public void onResponse(OAuthResponse oAuthResponse) {
            oAuthResponse.accept(this);
        }

        public void visit(OAuthErrorResponse oAuthErrorResponse) {
            if (oAuthErrorResponse.getError() == ErrorType.INVALID_GRANT) {
                LiveAuthClient.this.clearRefreshTokenFromPreferences();
            }
        }

        public void visit(OAuthSuccessfulResponse oAuthSuccessfulResponse) {
            String refreshToken = oAuthSuccessfulResponse.getRefreshToken();
            if (!TextUtils.isEmpty(refreshToken)) {
                saveRefreshTokenToPreferences(refreshToken);
            }
        }

        private boolean saveRefreshTokenToPreferences(String str) {
            if (!TextUtils.isEmpty(str)) {
                Editor edit = LiveAuthClient.this.applicationContext.getSharedPreferences(PreferencesConstants.FILE_NAME, 0).edit();
                edit.putString("refresh_token", str);
                return edit.commit();
            }
            throw new AssertionError();
        }
    }

    private static class SessionRefresher implements OAuthResponseVisitor {
        private final LiveConnectSession session;
        private boolean visitedSuccessfulResponse;

        public SessionRefresher(LiveConnectSession liveConnectSession) {
            if (liveConnectSession != null) {
                this.session = liveConnectSession;
                this.visitedSuccessfulResponse = false;
                return;
            }
            throw new AssertionError();
        }

        public void visit(OAuthErrorResponse oAuthErrorResponse) {
            this.visitedSuccessfulResponse = false;
        }

        public void visit(OAuthSuccessfulResponse oAuthSuccessfulResponse) {
            this.session.loadFromOAuthResponse(oAuthSuccessfulResponse);
            this.visitedSuccessfulResponse = true;
        }

        public boolean visitedSuccessfulResponse() {
            return this.visitedSuccessfulResponse;
        }
    }

    public LiveAuthClient(Context context, String str, Iterable<String> iterable, OAuthConfig oAuthConfig) {
        this.httpClient = new DefaultHttpClient();
        this.hasPendingLoginRequest = false;
        this.session = new LiveConnectSession(this);
        LiveConnectUtils.assertNotNull(context, "context");
        LiveConnectUtils.assertNotNullOrEmpty(str, "clientId");
        this.applicationContext = context.getApplicationContext();
        this.clientId = str;
        if (oAuthConfig == null) {
            this.mOAuthConfig = MicrosoftOAuthConfig.getInstance();
        } else {
            this.mOAuthConfig = oAuthConfig;
        }
        if (iterable == null) {
            iterable = Arrays.asList(new String[0]);
        }
        this.baseScopes = new HashSet();
        for (String add : iterable) {
            this.baseScopes.add(add);
        }
        this.baseScopes = Collections.unmodifiableSet(this.baseScopes);
        String refreshTokenFromPreferences = getRefreshTokenFromPreferences();
        if (!TextUtils.isEmpty(refreshTokenFromPreferences)) {
            RefreshAccessTokenRequest refreshAccessTokenRequest = new RefreshAccessTokenRequest(this.httpClient, this.clientId, refreshTokenFromPreferences, TextUtils.join(OAuth.SCOPE_DELIMITER, this.baseScopes), this.mOAuthConfig);
            TokenRequestAsync tokenRequestAsync = new TokenRequestAsync(refreshAccessTokenRequest);
            tokenRequestAsync.addObserver(new RefreshTokenWriter());
            tokenRequestAsync.execute(new Void[0]);
        }
    }

    public LiveAuthClient(Context context, String str) {
        this(context, str, null);
    }

    public LiveAuthClient(Context context, String str, Iterable<String> iterable) {
        this(context, str, iterable, null);
    }

    public String getClientId() {
        return this.clientId;
    }

    public void login(Activity activity, LiveAuthListener liveAuthListener) {
        login(activity, null, null, liveAuthListener);
    }

    public void login(Activity activity, Iterable<String> iterable, LiveAuthListener liveAuthListener) {
        login(activity, iterable, null, null, liveAuthListener);
    }

    public void login(Activity activity, Iterable<String> iterable, Object obj, LiveAuthListener liveAuthListener) {
        login(activity, iterable, obj, null, liveAuthListener);
    }

    public void login(Activity activity, Iterable<String> iterable, Object obj, String str, LiveAuthListener liveAuthListener) {
        LiveConnectUtils.assertNotNull(activity, "activity");
        if (liveAuthListener == null) {
            liveAuthListener = NULL_LISTENER;
        }
        if (!this.hasPendingLoginRequest) {
            if (iterable == null) {
                iterable = this.baseScopes;
                if (iterable == null) {
                    iterable = Arrays.asList(new String[0]);
                }
            }
            if (loginSilent(iterable, obj, liveAuthListener).booleanValue()) {
                Log.i(TAG, "Interactive login not required.");
                return;
            }
            Activity activity2 = activity;
            AuthorizationRequest authorizationRequest = new AuthorizationRequest(activity2, this.httpClient, this.clientId, TextUtils.join(OAuth.SCOPE_DELIMITER, iterable), str, this.mOAuthConfig);
            authorizationRequest.addObserver(new ListenerCallerObserver(liveAuthListener, obj));
            authorizationRequest.addObserver(new RefreshTokenWriter());
            authorizationRequest.addObserver(new OAuthRequestObserver() {
                public void onException(LiveAuthException liveAuthException) {
                    LiveAuthClient.this.hasPendingLoginRequest = false;
                }

                public void onResponse(OAuthResponse oAuthResponse) {
                    LiveAuthClient.this.hasPendingLoginRequest = false;
                }
            });
            this.hasPendingLoginRequest = true;
            authorizationRequest.execute();
            return;
        }
        throw new IllegalStateException(ErrorMessages.LOGIN_IN_PROGRESS);
    }

    public Boolean loginSilent(LiveAuthListener liveAuthListener) {
        return loginSilent(null, null, liveAuthListener);
    }

    public Boolean loginSilent(Iterable<String> iterable, LiveAuthListener liveAuthListener) {
        return loginSilent(iterable, null, liveAuthListener);
    }

    public Boolean loginSilent(Object obj, LiveAuthListener liveAuthListener) {
        return loginSilent(null, obj, liveAuthListener);
    }

    public Boolean loginSilent(Iterable<String> iterable, Object obj, LiveAuthListener liveAuthListener) {
        final Iterable<String> iterable2;
        if (!this.hasPendingLoginRequest) {
            if (iterable == null) {
                Set<String> set = this.baseScopes;
                iterable2 = set == null ? Arrays.asList(new String[0]) : set;
            } else {
                iterable2 = iterable;
            }
            if (TextUtils.isEmpty(this.session.getRefreshToken())) {
                this.session.setRefreshToken(getRefreshTokenFromPreferences());
            }
            final boolean z = this.session.isExpired() || !this.session.contains(iterable2);
            boolean isEmpty = TextUtils.isEmpty(this.session.getRefreshToken());
            final LiveAuthListener liveAuthListener2 = liveAuthListener;
            final Object obj2 = obj;
            C17273 r1 = new AsyncTask<Void, Void, Void>() {
                /* access modifiers changed from: protected */
                public Void doInBackground(Void... voidArr) {
                    if (!z) {
                        Log.i(LiveAuthClient.TAG, "Access token still valid, so using it.");
                        liveAuthListener2.onAuthComplete(LiveStatus.CONNECTED, LiveAuthClient.this.session, obj2);
                    } else if (LiveAuthClient.this.tryRefresh(iterable2).booleanValue()) {
                        Log.i(LiveAuthClient.TAG, "Used refresh token to refresh access and refresh tokens.");
                        liveAuthListener2.onAuthComplete(LiveStatus.CONNECTED, LiveAuthClient.this.session, obj2);
                    } else {
                        Log.i(LiveAuthClient.TAG, "All tokens expired, you need to call login() to initiate interactive logon");
                        liveAuthListener2.onAuthComplete(LiveStatus.NOT_CONNECTED, LiveAuthClient.this.getSession(), obj2);
                    }
                    return null;
                }
            };
            r1.execute(new Void[0]);
            return Boolean.valueOf(!isEmpty);
        }
        throw new IllegalStateException(ErrorMessages.LOGIN_IN_PROGRESS);
    }

    public void logout(LiveAuthListener liveAuthListener) {
        logout(null, liveAuthListener);
    }

    public void logout(Object obj, LiveAuthListener liveAuthListener) {
        if (liveAuthListener == null) {
            liveAuthListener = NULL_LISTENER;
        }
        this.session.setAccessToken(null);
        this.session.setAuthenticationToken(null);
        this.session.setRefreshToken(null);
        this.session.setScopes(null);
        this.session.setTokenType(null);
        clearRefreshTokenFromPreferences();
        CookieSyncManager createInstance = CookieSyncManager.createInstance(this.applicationContext);
        CookieManager instance = CookieManager.getInstance();
        if (VERSION.SDK_INT >= 21) {
            instance.removeAllCookies(null);
        } else {
            instance.removeAllCookie();
        }
        createInstance.sync();
        liveAuthListener.onAuthComplete(LiveStatus.UNKNOWN, null, obj);
    }

    /* access modifiers changed from: 0000 */
    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public LiveConnectSession getSession() {
        return this.session;
    }

    /* access modifiers changed from: 0000 */
    public Boolean tryRefresh(Iterable<String> iterable) {
        String join = TextUtils.join(OAuth.SCOPE_DELIMITER, iterable);
        String refreshToken = this.session.getRefreshToken();
        if (TextUtils.isEmpty(refreshToken)) {
            Log.i(TAG, "No refresh token available, sorry!");
            return Boolean.valueOf(false);
        }
        Log.i(TAG, "Refresh token found, attempting to refresh access and refresh tokens.");
        RefreshAccessTokenRequest refreshAccessTokenRequest = new RefreshAccessTokenRequest(this.httpClient, this.clientId, refreshToken, join, this.mOAuthConfig);
        try {
            OAuthResponse execute = refreshAccessTokenRequest.execute();
            SessionRefresher sessionRefresher = new SessionRefresher(this.session);
            execute.accept(sessionRefresher);
            execute.accept(new RefreshTokenWriter());
            return Boolean.valueOf(sessionRefresher.visitedSuccessfulResponse());
        } catch (LiveAuthException unused) {
            return Boolean.valueOf(false);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setHttpClient(HttpClient httpClient2) {
        if (httpClient2 != null) {
            this.httpClient = httpClient2;
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: private */
    public boolean clearRefreshTokenFromPreferences() {
        Editor edit = getSharedPreferences().edit();
        edit.remove("refresh_token");
        return edit.commit();
    }

    private SharedPreferences getSharedPreferences() {
        return this.applicationContext.getSharedPreferences(PreferencesConstants.FILE_NAME, 0);
    }

    private List<String> getCookieKeysFromPreferences() {
        return Arrays.asList(TextUtils.split(getSharedPreferences().getString(PreferencesConstants.COOKIES_KEY, ""), PreferencesConstants.COOKIE_DELIMITER));
    }

    private String getRefreshTokenFromPreferences() {
        return getSharedPreferences().getString("refresh_token", null);
    }
}
