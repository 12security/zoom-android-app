package com.box.androidsdk.content.models;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.box.androidsdk.content.BoxApiUser;
import com.box.androidsdk.content.BoxConfig;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.BoxException.ErrorType;
import com.box.androidsdk.content.BoxException.RefreshFailure;
import com.box.androidsdk.content.BoxFutureTask;
import com.box.androidsdk.content.auth.BoxAuthentication;
import com.box.androidsdk.content.auth.BoxAuthentication.AuthListener;
import com.box.androidsdk.content.auth.BoxAuthentication.AuthenticationRefreshProvider;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.utils.BoxLogUtils;
import com.box.androidsdk.content.utils.SdkUtils;
import com.box.sdk.android.BuildConfig;
import com.box.sdk.android.C0469R;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import p021us.zoom.androidlib.util.TimeUtil;

public class BoxSession extends BoxObject implements AuthListener, Serializable {
    private static final transient ThreadPoolExecutor AUTH_CREATION_EXECUTOR = SdkUtils.createDefaultThreadPoolExecutor(1, 20, TimeUtil.ONE_HOUR_IN_SECONDS, TimeUnit.SECONDS);
    private static AtomicLong mLastToastTime = new AtomicLong();
    private static final long serialVersionUID = 8122900496609434013L;
    protected String mAccountEmail;
    private transient Context mApplicationContext;
    protected BoxAuthenticationInfo mAuthInfo;
    protected String mClientId;
    protected String mClientRedirectUrl;
    protected String mClientSecret;
    protected String mDeviceId;
    protected String mDeviceName;
    protected boolean mEnableBoxAppAuthentication;
    protected Long mExpiresAt;
    protected BoxMDMData mMDMData;
    protected AuthenticationRefreshProvider mRefreshProvider;
    private String mUserAgent;
    private String mUserId;
    private transient AuthListener sessionAuthListener;

    /* renamed from: com.box.androidsdk.content.models.BoxSession$4 */
    static /* synthetic */ class C04574 {
        static final /* synthetic */ int[] $SwitchMap$com$box$androidsdk$content$BoxException$ErrorType = new int[ErrorType.values().length];

        static {
            try {
                $SwitchMap$com$box$androidsdk$content$BoxException$ErrorType[ErrorType.NETWORK_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static class BoxSessionAuthCreationRequest extends BoxRequest<BoxSession, BoxSessionAuthCreationRequest> implements AuthListener {
        private CountDownLatch authLatch;
        /* access modifiers changed from: private */
        public final BoxSession mSession;

        public void onLoggedOut(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        }

        public void onRefreshed(BoxAuthenticationInfo boxAuthenticationInfo) {
        }

        public BoxSessionAuthCreationRequest(BoxSession boxSession, boolean z) {
            super(null, OAuth.SCOPE_DELIMITER, null);
            this.mSession = boxSession;
        }

        public BoxSession send() throws BoxException {
            synchronized (this.mSession) {
                if (this.mSession.getUser() == null) {
                    if (this.mSession.getAuthInfo() != null && !SdkUtils.isBlank(this.mSession.getAuthInfo().accessToken())) {
                        try {
                            BoxUser boxUser = (BoxUser) new BoxApiUser(this.mSession).getCurrentUserInfoRequest().send();
                            this.mSession.setUserId(boxUser.getId());
                            this.mSession.getAuthInfo().setUser(boxUser);
                            this.mSession.onAuthCreated(this.mSession.getAuthInfo());
                            BoxSession boxSession = this.mSession;
                            return boxSession;
                        } catch (BoxException e) {
                            BoxLogUtils.m12e("BoxSession", "Unable to repair user", e);
                            if ((e instanceof RefreshFailure) && ((RefreshFailure) e).isErrorFatal()) {
                                BoxSession.toastString(this.mSession.getApplicationContext(), C0469R.string.boxsdk_error_fatal_refresh);
                            } else if (e.getErrorType() == ErrorType.TERMS_OF_SERVICE_REQUIRED) {
                                BoxSession.toastString(this.mSession.getApplicationContext(), C0469R.string.boxsdk_error_terms_of_service);
                            } else {
                                this.mSession.onAuthFailure(null, e);
                                throw e;
                            }
                        }
                    }
                    BoxAuthentication.getInstance().addListener(this);
                    launchAuthUI();
                    BoxSession boxSession2 = this.mSession;
                    return boxSession2;
                }
                BoxAuthenticationInfo authInfo = BoxAuthentication.getInstance().getAuthInfo(this.mSession.getUserId(), this.mSession.getApplicationContext());
                if (authInfo != null) {
                    BoxAuthenticationInfo.cloneInfo(this.mSession.mAuthInfo, authInfo);
                    this.mSession.onAuthCreated(this.mSession.getAuthInfo());
                } else {
                    this.mSession.mAuthInfo.setUser(null);
                    launchAuthUI();
                }
                BoxSession boxSession3 = this.mSession;
                return boxSession3;
            }
        }

        private void launchAuthUI() {
            this.authLatch = new CountDownLatch(1);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    if (BoxSessionAuthCreationRequest.this.mSession.getRefreshProvider() == null || !BoxSessionAuthCreationRequest.this.mSession.getRefreshProvider().launchAuthUi(BoxSessionAuthCreationRequest.this.mSession.getUserId(), BoxSessionAuthCreationRequest.this.mSession)) {
                        BoxSessionAuthCreationRequest.this.mSession.startAuthenticationUI();
                    }
                }
            });
            try {
                this.authLatch.await();
            } catch (InterruptedException unused) {
                this.authLatch.countDown();
            }
        }

        public void onAuthCreated(BoxAuthenticationInfo boxAuthenticationInfo) {
            BoxAuthenticationInfo.cloneInfo(this.mSession.mAuthInfo, boxAuthenticationInfo);
            this.mSession.setUserId(boxAuthenticationInfo.getUser().getId());
            this.mSession.onAuthCreated(boxAuthenticationInfo);
            this.authLatch.countDown();
        }

        public void onAuthFailure(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
            this.authLatch.countDown();
        }
    }

    private static class BoxSessionLogoutRequest extends BoxRequest<BoxSession, BoxSessionLogoutRequest> {
        private BoxSession mSession;

        public BoxSessionLogoutRequest(BoxSession boxSession) {
            super(null, OAuth.SCOPE_DELIMITER, null);
            this.mSession = boxSession;
        }

        public BoxSession send() throws BoxException {
            synchronized (this.mSession) {
                if (this.mSession.getUser() != null) {
                    BoxAuthentication.getInstance().logout(this.mSession);
                    this.mSession.getAuthInfo().wipeOutAuth();
                    this.mSession.setUserId(null);
                }
            }
            return this.mSession;
        }
    }

    private static class BoxSessionRefreshRequest extends BoxRequest<BoxSession, BoxSessionRefreshRequest> {
        private BoxSession mSession;

        public BoxSessionRefreshRequest(BoxSession boxSession) {
            super(null, OAuth.SCOPE_DELIMITER, null);
            this.mSession = boxSession;
        }

        public BoxSession send() throws BoxException {
            try {
                BoxAuthenticationInfo boxAuthenticationInfo = (BoxAuthenticationInfo) BoxAuthentication.getInstance().refresh(this.mSession).get();
                BoxAuthenticationInfo.cloneInfo(this.mSession.mAuthInfo, BoxAuthentication.getInstance().getAuthInfo(this.mSession.getUserId(), this.mSession.getApplicationContext()));
                return this.mSession;
            } catch (Exception e) {
                BoxException boxException = (BoxException) e.getCause();
                if (e.getCause() instanceof BoxException) {
                    throw ((BoxException) e.getCause());
                }
                throw new BoxException("BoxSessionRefreshRequest failed", (Throwable) e);
            }
        }
    }

    public BoxSession(Context context) {
        this(context, getBestStoredUserId(context));
    }

    private static String getBestStoredUserId(Context context) {
        String lastAuthenticatedUserId = BoxAuthentication.getInstance().getLastAuthenticatedUserId(context);
        Map storedAuthInfo = BoxAuthentication.getInstance().getStoredAuthInfo(context);
        if (storedAuthInfo != null) {
            if (!SdkUtils.isEmptyString(lastAuthenticatedUserId) && storedAuthInfo.get(lastAuthenticatedUserId) != null) {
                return lastAuthenticatedUserId;
            }
            if (storedAuthInfo.size() == 1) {
                Iterator it = storedAuthInfo.keySet().iterator();
                if (it.hasNext()) {
                    return (String) it.next();
                }
            }
        }
        return null;
    }

    public BoxSession(Context context, String str) {
        this(context, str, BoxConfig.CLIENT_ID, BoxConfig.CLIENT_SECRET, BoxConfig.REDIRECT_URL);
        if (!SdkUtils.isEmptyString(BoxConfig.DEVICE_NAME)) {
            setDeviceName(BoxConfig.DEVICE_NAME);
        }
        if (!SdkUtils.isEmptyString(BoxConfig.DEVICE_ID)) {
            setDeviceName(BoxConfig.DEVICE_ID);
        }
    }

    public BoxSession(Context context, String str, String str2, String str3, String str4) {
        this.mUserAgent = BuildConfig.APPLICATION_ID;
        this.mEnableBoxAppAuthentication = BoxConfig.ENABLE_BOX_APP_AUTHENTICATION;
        this.mClientId = str2;
        this.mClientSecret = str3;
        this.mClientRedirectUrl = str4;
        if (SdkUtils.isEmptyString(this.mClientId) || SdkUtils.isEmptyString(this.mClientSecret)) {
            throw new RuntimeException("Session must have a valid client id and client secret specified.");
        }
        this.mApplicationContext = context.getApplicationContext();
        if (!SdkUtils.isEmptyString(str)) {
            this.mAuthInfo = BoxAuthentication.getInstance().getAuthInfo(str, context);
            this.mUserId = str;
        }
        if (this.mAuthInfo == null) {
            this.mUserId = str;
            this.mAuthInfo = new BoxAuthenticationInfo();
        }
        this.mAuthInfo.setClientId(this.mClientId);
        setupSession();
    }

    protected BoxSession(BoxSession boxSession) {
        this.mUserAgent = BuildConfig.APPLICATION_ID;
        this.mEnableBoxAppAuthentication = BoxConfig.ENABLE_BOX_APP_AUTHENTICATION;
        this.mApplicationContext = boxSession.mApplicationContext;
        setAuthInfo(boxSession.getAuthInfo());
        setupSession();
    }

    public BoxSession(Context context, BoxAuthenticationInfo boxAuthenticationInfo, AuthenticationRefreshProvider authenticationRefreshProvider) {
        this.mUserAgent = BuildConfig.APPLICATION_ID;
        this.mEnableBoxAppAuthentication = BoxConfig.ENABLE_BOX_APP_AUTHENTICATION;
        this.mApplicationContext = context.getApplicationContext();
        setAuthInfo(boxAuthenticationInfo);
        this.mRefreshProvider = authenticationRefreshProvider;
        setupSession();
    }

    /* access modifiers changed from: protected */
    public void setAuthInfo(BoxAuthenticationInfo boxAuthenticationInfo) {
        if (boxAuthenticationInfo != null) {
            this.mAuthInfo = boxAuthenticationInfo;
            if (boxAuthenticationInfo.getUser() != null && !SdkUtils.isBlank(boxAuthenticationInfo.getUser().getId())) {
                setUserId(boxAuthenticationInfo.getUser().getId());
            }
        }
    }

    public BoxSession(Context context, String str, AuthenticationRefreshProvider authenticationRefreshProvider) {
        this(context, createSimpleBoxAuthenticationInfo(str), authenticationRefreshProvider);
    }

    private static BoxAuthenticationInfo createSimpleBoxAuthenticationInfo(String str) {
        BoxAuthenticationInfo boxAuthenticationInfo = new BoxAuthenticationInfo();
        boxAuthenticationInfo.setAccessToken(str);
        return boxAuthenticationInfo;
    }

    public void setEnableBoxAppAuthentication(boolean z) {
        this.mEnableBoxAppAuthentication = z;
    }

    public boolean isEnabledBoxAppAuthentication() {
        return this.mEnableBoxAppAuthentication;
    }

    public void setApplicationContext(Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }

    public Context getApplicationContext() {
        return this.mApplicationContext;
    }

    public void setSessionAuthListener(AuthListener authListener) {
        this.sessionAuthListener = authListener;
    }

    /* access modifiers changed from: protected */
    public void setupSession() {
        boolean z = false;
        try {
            if (!(this.mApplicationContext == null || this.mApplicationContext.getPackageManager() == null || (this.mApplicationContext.getPackageManager().getPackageInfo(this.mApplicationContext.getPackageName(), 0).applicationInfo.flags & 2) == 0)) {
                z = true;
            }
        } catch (NameNotFoundException unused) {
        }
        BoxConfig.IS_DEBUG = z;
        BoxAuthentication.getInstance().addListener(this);
    }

    public BoxUser getUser() {
        return this.mAuthInfo.getUser();
    }

    public String getUserId() {
        return this.mUserId;
    }

    /* access modifiers changed from: protected */
    public void setUserId(String str) {
        this.mUserId = str;
    }

    public BoxAuthenticationInfo getAuthInfo() {
        return this.mAuthInfo;
    }

    public AuthenticationRefreshProvider getRefreshProvider() {
        return this.mRefreshProvider;
    }

    public void setDeviceId(String str) {
        this.mDeviceId = str;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public void setDeviceName(String str) {
        this.mDeviceName = str;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public String getUserAgent() {
        return this.mUserAgent;
    }

    public void setManagementData(BoxMDMData boxMDMData) {
        this.mMDMData = boxMDMData;
    }

    public BoxMDMData getManagementData() {
        return this.mMDMData;
    }

    public void setRefreshTokenExpiresAt(long j) {
        this.mExpiresAt = Long.valueOf(j);
    }

    public Long getRefreshTokenExpiresAt() {
        return this.mExpiresAt;
    }

    public void setBoxAccountEmail(String str) {
        this.mAccountEmail = str;
    }

    public String getBoxAccountEmail() {
        return this.mAccountEmail;
    }

    public BoxFutureTask<BoxSession> authenticate() {
        BoxFutureTask<BoxSession> task = new BoxSessionAuthCreationRequest(this, this.mEnableBoxAppAuthentication).toTask();
        AUTH_CREATION_EXECUTOR.submit(task);
        return task;
    }

    public BoxFutureTask<BoxSession> logout() {
        final BoxFutureTask<BoxSession> task = new BoxSessionLogoutRequest(this).toTask();
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                task.run();
                return null;
            }
        }.execute(new Void[0]);
        return task;
    }

    public BoxFutureTask<BoxSession> refresh() {
        final BoxFutureTask<BoxSession> task = new BoxSessionRefreshRequest(this).toTask();
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                task.run();
                return null;
            }
        }.execute(new Void[0]);
        return task;
    }

    public BoxSharedLinkSession getSharedLinkSession(String str) {
        return new BoxSharedLinkSession(str, this);
    }

    public File getCacheDir() {
        return new File(getApplicationContext().getFilesDir(), getUserId());
    }

    public void onRefreshed(BoxAuthenticationInfo boxAuthenticationInfo) {
        if (sameUser(boxAuthenticationInfo)) {
            BoxAuthenticationInfo.cloneInfo(this.mAuthInfo, boxAuthenticationInfo);
            AuthListener authListener = this.sessionAuthListener;
            if (authListener != null) {
                authListener.onRefreshed(boxAuthenticationInfo);
            }
        }
    }

    public void onAuthCreated(BoxAuthenticationInfo boxAuthenticationInfo) {
        if (sameUser(boxAuthenticationInfo)) {
            BoxAuthenticationInfo.cloneInfo(this.mAuthInfo, boxAuthenticationInfo);
            AuthListener authListener = this.sessionAuthListener;
            if (authListener != null) {
                authListener.onAuthCreated(boxAuthenticationInfo);
            }
        }
    }

    public void onAuthFailure(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        if (sameUser(boxAuthenticationInfo) || (boxAuthenticationInfo == null && getUserId() == null)) {
            AuthListener authListener = this.sessionAuthListener;
            if (authListener != null) {
                authListener.onAuthFailure(boxAuthenticationInfo, exc);
            }
            if (exc instanceof BoxException) {
                if (C04574.$SwitchMap$com$box$androidsdk$content$BoxException$ErrorType[((BoxException) exc).getErrorType().ordinal()] == 1) {
                    toastString(this.mApplicationContext, C0469R.string.boxsdk_error_network_connection);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startAuthenticationUI() {
        BoxAuthentication.getInstance().startAuthenticationUI(this);
    }

    /* access modifiers changed from: private */
    public static void toastString(final Context context, final int i) {
        Handler handler = new Handler(Looper.getMainLooper());
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - 3000 >= mLastToastTime.get()) {
            mLastToastTime.set(currentTimeMillis);
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, i, 0).show();
                }
            });
        }
    }

    public void onLoggedOut(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        if (sameUser(boxAuthenticationInfo)) {
            boxAuthenticationInfo.wipeOutAuth();
            getAuthInfo().wipeOutAuth();
            setUserId(null);
            AuthListener authListener = this.sessionAuthListener;
            if (authListener != null) {
                authListener.onLoggedOut(boxAuthenticationInfo, exc);
            }
        }
    }

    public String getClientId() {
        return this.mClientId;
    }

    public String getClientSecret() {
        return this.mClientSecret;
    }

    public String getRedirectUrl() {
        return this.mClientRedirectUrl;
    }

    private boolean sameUser(BoxAuthenticationInfo boxAuthenticationInfo) {
        return (boxAuthenticationInfo == null || boxAuthenticationInfo.getUser() == null || getUserId() == null || !getUserId().equals(boxAuthenticationInfo.getUser().getId())) ? false : true;
    }
}
