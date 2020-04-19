package com.onedrive.sdk.authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import com.microsoft.onedrivesdk.BuildConfig;
import com.microsoft.services.msa.LiveAuthClient;
import com.microsoft.services.msa.LiveAuthException;
import com.microsoft.services.msa.LiveAuthListener;
import com.microsoft.services.msa.LiveConnectSession;
import com.microsoft.services.msa.LiveStatus;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.concurrency.SimpleWaiter;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MSAAuthenticator implements IAuthenticator {
    private static final String DEFAULT_USER_ID = "@@defaultUser";
    private static final String MSA_AUTHENTICATOR_PREFS = "MSAAuthenticatorPrefs";
    private static final String SIGN_IN_CANCELLED_MESSAGE = "The user cancelled the login operation.";
    private static final String USER_ID_KEY = "userId";
    public static final String VERSION_CODE_KEY = "versionCode";
    /* access modifiers changed from: private */
    public Activity mActivity;
    /* access modifiers changed from: private */
    public LiveAuthClient mAuthClient;
    /* access modifiers changed from: private */
    public IExecutors mExecutors;
    private boolean mInitialized;
    /* access modifiers changed from: private */
    public ILogger mLogger;
    private final AtomicReference<String> mUserId = new AtomicReference<>();

    public abstract String getClientId();

    public abstract String[] getScopes();

    public synchronized void init(IExecutors iExecutors, IHttpProvider iHttpProvider, Activity activity, ILogger iLogger) {
        if (!this.mInitialized) {
            this.mExecutors = iExecutors;
            this.mActivity = activity;
            this.mLogger = iLogger;
            this.mInitialized = true;
            this.mAuthClient = new LiveAuthClient(activity, getClientId(), Arrays.asList(getScopes()));
            this.mUserId.set(getSharedPreferences().getString("userId", null));
        }
    }

    public void login(final String str, final ICallback<IAccountInfo> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting login async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        MSAAuthenticator.this.mExecutors.performOnForeground(MSAAuthenticator.this.login(str), iCallback);
                    } catch (ClientException e) {
                        MSAAuthenticator.this.mExecutors.performOnForeground(e, iCallback);
                    }
                }
            });
        } else {
            throw new InvalidParameterException("loginCallback");
        }
    }

    public synchronized IAccountInfo login(final String str) throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting login");
            final AtomicReference atomicReference = new AtomicReference();
            final SimpleWaiter simpleWaiter = new SimpleWaiter();
            final C17762 r2 = new LiveAuthListener() {
                public void onAuthComplete(LiveStatus liveStatus, LiveConnectSession liveConnectSession, Object obj) {
                    if (liveStatus == LiveStatus.NOT_CONNECTED) {
                        MSAAuthenticator.this.mLogger.logDebug("Received invalid login failure from silent authentication with MSA, ignoring.");
                        return;
                    }
                    MSAAuthenticator.this.mLogger.logDebug("Successful interactive login");
                    simpleWaiter.signal();
                }

                public void onAuthError(LiveAuthException liveAuthException, Object obj) {
                    OneDriveErrorCodes oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationFailure;
                    if (liveAuthException.getError().equals("The user cancelled the login operation.")) {
                        oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationCancelled;
                    }
                    atomicReference.set(new ClientAuthenticatorException("Unable to login with MSA", liveAuthException, oneDriveErrorCodes));
                    MSAAuthenticator.this.mLogger.logError(((ClientException) atomicReference.get()).getMessage(), (Throwable) atomicReference.get());
                    simpleWaiter.signal();
                }
            };
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    MSAAuthenticator.this.mAuthClient.login(MSAAuthenticator.this.mActivity, null, null, str, r2);
                }
            });
            this.mLogger.logDebug("Waiting for MSA callback");
            simpleWaiter.waitForSignal();
            ClientException clientException = (ClientException) atomicReference.get();
            if (clientException == null) {
                if (str == null) {
                    str = DEFAULT_USER_ID;
                }
                this.mUserId.set(str);
                getSharedPreferences().edit().putString("userId", (String) this.mUserId.get()).putInt("versionCode", BuildConfig.VERSION_CODE).apply();
            } else {
                throw clientException;
            }
        } else {
            throw new IllegalStateException("init must be called");
        }
        return getAccountInfo();
    }

    public void loginSilent(final ICallback<IAccountInfo> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting login silent async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        MSAAuthenticator.this.mExecutors.performOnForeground(MSAAuthenticator.this.loginSilent(), iCallback);
                    } catch (ClientException e) {
                        MSAAuthenticator.this.mExecutors.performOnForeground(e, iCallback);
                    }
                }
            });
        } else {
            throw new InvalidParameterException("loginCallback");
        }
    }

    public synchronized IAccountInfo loginSilent() throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting login silent");
            if (getSharedPreferences().getInt("versionCode", 0) < 10112 || this.mUserId.get() != null) {
                final SimpleWaiter simpleWaiter = new SimpleWaiter();
                final AtomicReference atomicReference = new AtomicReference();
                if (!this.mAuthClient.loginSilent(new LiveAuthListener() {
                    public void onAuthComplete(LiveStatus liveStatus, LiveConnectSession liveConnectSession, Object obj) {
                        if (liveStatus == LiveStatus.NOT_CONNECTED) {
                            atomicReference.set(new ClientAuthenticatorException("Failed silent login, interactive login required", OneDriveErrorCodes.AuthenticationFailure));
                            MSAAuthenticator.this.mLogger.logError(((ClientException) atomicReference.get()).getMessage(), (Throwable) atomicReference.get());
                        } else {
                            MSAAuthenticator.this.mLogger.logDebug("Successful silent login");
                        }
                        simpleWaiter.signal();
                    }

                    public void onAuthError(LiveAuthException liveAuthException, Object obj) {
                        OneDriveErrorCodes oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationFailure;
                        if (liveAuthException.getError().equals("The user cancelled the login operation.")) {
                            oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationCancelled;
                        }
                        atomicReference.set(new ClientAuthenticatorException("Login silent authentication error", liveAuthException, oneDriveErrorCodes));
                        MSAAuthenticator.this.mLogger.logError(((ClientException) atomicReference.get()).getMessage(), (Throwable) atomicReference.get());
                        simpleWaiter.signal();
                    }
                }).booleanValue()) {
                    this.mLogger.logDebug("MSA silent auth fast-failed");
                    return null;
                }
                this.mLogger.logDebug("Waiting for MSA callback");
                simpleWaiter.waitForSignal();
                ClientException clientException = (ClientException) atomicReference.get();
                if (clientException == null) {
                    return getAccountInfo();
                }
                throw clientException;
            }
            this.mLogger.logDebug("No login information found for silent authentication");
            return null;
        }
        throw new IllegalStateException("init must be called");
    }

    public void logout(final ICallback<Void> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting logout async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        MSAAuthenticator.this.logout();
                        MSAAuthenticator.this.mExecutors.performOnForeground(null, iCallback);
                    } catch (ClientException e) {
                        MSAAuthenticator.this.mExecutors.performOnForeground(e, iCallback);
                    }
                }
            });
        } else {
            throw new InvalidParameterException("logoutCallback");
        }
    }

    public synchronized void logout() throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting logout");
            final SimpleWaiter simpleWaiter = new SimpleWaiter();
            final AtomicReference atomicReference = new AtomicReference();
            this.mAuthClient.logout(new LiveAuthListener() {
                public void onAuthComplete(LiveStatus liveStatus, LiveConnectSession liveConnectSession, Object obj) {
                    MSAAuthenticator.this.mLogger.logDebug("Logout completed");
                    simpleWaiter.signal();
                }

                public void onAuthError(LiveAuthException liveAuthException, Object obj) {
                    atomicReference.set(new ClientAuthenticatorException("MSA Logout failed", liveAuthException, OneDriveErrorCodes.AuthenticationFailure));
                    MSAAuthenticator.this.mLogger.logError(((ClientException) atomicReference.get()).getMessage(), (Throwable) atomicReference.get());
                    simpleWaiter.signal();
                }
            });
            this.mLogger.logDebug("Waiting for logout to complete");
            simpleWaiter.waitForSignal();
            this.mLogger.logDebug("Clearing all MSA Authenticator shared preferences");
            getSharedPreferences().edit().clear().putInt("versionCode", BuildConfig.VERSION_CODE).apply();
            this.mUserId.set(null);
            ClientException clientException = (ClientException) atomicReference.get();
            if (clientException != null) {
                throw clientException;
            }
        } else {
            throw new IllegalStateException("init must be called");
        }
    }

    public IAccountInfo getAccountInfo() {
        LiveConnectSession session = this.mAuthClient.getSession();
        if (session == null) {
            return null;
        }
        return new MSAAccountInfo(this, session, this.mLogger);
    }

    private SharedPreferences getSharedPreferences() {
        return this.mActivity.getSharedPreferences(MSA_AUTHENTICATOR_PREFS, 0);
    }
}
