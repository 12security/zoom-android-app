package com.onedrive.sdk.authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.microsoft.onedrivesdk.BuildConfig;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.concurrency.SimpleWaiter;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;
import java.security.InvalidParameterException;
import java.util.concurrent.atomic.AtomicReference;

public class DisambiguationAuthenticator implements IAuthenticator {
    public static final String ACCOUNT_TYPE_KEY = "accountType";
    private static final String DISAMBIGUATION_AUTHENTICATOR_PREFS = "DisambiguationAuthenticatorPrefs";
    public static final String VERSION_CODE_KEY = "versionCode";
    private final ADALAuthenticator mADALAuthenticator;
    private final AtomicReference<IAccountInfo> mAccountInfo = new AtomicReference<>();
    private Activity mActivity;
    /* access modifiers changed from: private */
    public IExecutors mExecutors;
    private boolean mInitialized;
    /* access modifiers changed from: private */
    public ILogger mLogger;
    private final MSAAuthenticator mMSAAuthenticator;

    public DisambiguationAuthenticator(MSAAuthenticator mSAAuthenticator, ADALAuthenticator aDALAuthenticator) {
        this.mMSAAuthenticator = mSAAuthenticator;
        this.mADALAuthenticator = aDALAuthenticator;
    }

    public synchronized void init(IExecutors iExecutors, IHttpProvider iHttpProvider, Activity activity, ILogger iLogger) {
        if (!this.mInitialized) {
            this.mExecutors = iExecutors;
            this.mActivity = activity;
            this.mLogger = iLogger;
            this.mLogger.logDebug("Initializing MSA and ADAL authenticators");
            this.mMSAAuthenticator.init(iExecutors, iHttpProvider, activity, iLogger);
            this.mADALAuthenticator.init(iExecutors, iHttpProvider, activity, iLogger);
            this.mInitialized = true;
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
                        DisambiguationAuthenticator.this.mExecutors.performOnForeground(DisambiguationAuthenticator.this.login(str), iCallback);
                    } catch (ClientException e) {
                        DisambiguationAuthenticator.this.mExecutors.performOnForeground(e, iCallback);
                    }
                }
            });
        } else {
            throw new InvalidParameterException("loginCallback");
        }
    }

    public synchronized IAccountInfo login(String str) throws ClientException {
        IAccountInfo iAccountInfo;
        this.mLogger.logDebug("Starting login");
        final SimpleWaiter simpleWaiter = new SimpleWaiter();
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        C17702 r2 = new ICallback<DisambiguationResponse>() {
            public void success(DisambiguationResponse disambiguationResponse) {
                DisambiguationAuthenticator.this.mLogger.logDebug(String.format("Successfully disambiguated '%s' as account type '%s'", new Object[]{disambiguationResponse.getAccount(), disambiguationResponse.getAccountType()}));
                atomicReference.set(disambiguationResponse);
                simpleWaiter.signal();
            }

            public void failure(ClientException clientException) {
                atomicReference2.set(new ClientAuthenticatorException("Unable to disambiguate account type", OneDriveErrorCodes.AuthenticationFailure));
                DisambiguationAuthenticator.this.mLogger.logError(((ClientException) atomicReference2.get()).getMessage(), (Throwable) atomicReference2.get());
                simpleWaiter.signal();
            }
        };
        AccountType accountTypeInPreferences = getAccountTypeInPreferences();
        String str2 = null;
        if (accountTypeInPreferences != null) {
            this.mLogger.logDebug(String.format("Found saved account information %s type of account", new Object[]{accountTypeInPreferences}));
        } else {
            this.mLogger.logDebug("Creating disambiguation ui, waiting for user to sign in");
            new DisambiguationRequest(this.mActivity, r2, this.mLogger).execute();
            simpleWaiter.waitForSignal();
            if (atomicReference2.get() == null) {
                DisambiguationResponse disambiguationResponse = (DisambiguationResponse) atomicReference.get();
                accountTypeInPreferences = disambiguationResponse.getAccountType();
                str2 = disambiguationResponse.getAccount();
            } else {
                throw ((ClientException) atomicReference2.get());
            }
        }
        switch (accountTypeInPreferences) {
            case ActiveDirectory:
                iAccountInfo = this.mADALAuthenticator.login(str2);
                break;
            case MicrosoftAccount:
                iAccountInfo = this.mMSAAuthenticator.login(str2);
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unrecognized account type ");
                sb.append(accountTypeInPreferences);
                UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException(sb.toString());
                this.mLogger.logError("Unrecognized account type", unsupportedOperationException);
                throw unsupportedOperationException;
        }
        setAccountTypeInPreferences(accountTypeInPreferences);
        this.mAccountInfo.set(iAccountInfo);
        return (IAccountInfo) this.mAccountInfo.get();
    }

    public void loginSilent(final ICallback<IAccountInfo> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting login silent async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        DisambiguationAuthenticator.this.mExecutors.performOnForeground(DisambiguationAuthenticator.this.loginSilent(), iCallback);
                    } catch (ClientException e) {
                        DisambiguationAuthenticator.this.mExecutors.performOnForeground(e, iCallback);
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
            AccountType accountTypeInPreferences = getAccountTypeInPreferences();
            if (accountTypeInPreferences != null) {
                this.mLogger.logDebug(String.format("Expecting %s type of account", new Object[]{accountTypeInPreferences}));
            }
            this.mLogger.logDebug("Checking MSA");
            IAccountInfo loginSilent = this.mMSAAuthenticator.loginSilent();
            if (loginSilent != null) {
                this.mLogger.logDebug("Found account info in MSA");
                setAccountTypeInPreferences(accountTypeInPreferences);
                this.mAccountInfo.set(loginSilent);
                return loginSilent;
            }
            this.mLogger.logDebug("Checking ADAL");
            IAccountInfo loginSilent2 = this.mADALAuthenticator.loginSilent();
            this.mAccountInfo.set(loginSilent2);
            if (loginSilent2 != null) {
                this.mLogger.logDebug("Found account info in ADAL");
                setAccountTypeInPreferences(accountTypeInPreferences);
            }
            return (IAccountInfo) this.mAccountInfo.get();
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
                    DisambiguationAuthenticator.this.logout();
                    DisambiguationAuthenticator.this.mExecutors.performOnForeground(null, iCallback);
                }
            });
        } else {
            throw new InvalidParameterException("logoutCallback");
        }
    }

    public synchronized void logout() throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting logout");
            if (this.mMSAAuthenticator.getAccountInfo() != null) {
                this.mLogger.logDebug("Starting logout of MSA account");
                this.mMSAAuthenticator.logout();
            }
            if (this.mADALAuthenticator.getAccountInfo() != null) {
                this.mLogger.logDebug("Starting logout of ADAL account");
                this.mADALAuthenticator.logout();
            }
            getSharedPreferences().edit().clear().putInt("versionCode", BuildConfig.VERSION_CODE).commit();
        } else {
            throw new IllegalStateException("init must be called");
        }
    }

    public IAccountInfo getAccountInfo() {
        return (IAccountInfo) this.mAccountInfo.get();
    }

    private SharedPreferences getSharedPreferences() {
        return this.mActivity.getSharedPreferences(DISAMBIGUATION_AUTHENTICATOR_PREFS, 0);
    }

    private void setAccountTypeInPreferences(@Nullable AccountType accountType) {
        if (accountType != null) {
            getSharedPreferences().edit().putString(ACCOUNT_TYPE_KEY, accountType.toString()).putInt("versionCode", BuildConfig.VERSION_CODE).commit();
        }
    }

    @Nullable
    private AccountType getAccountTypeInPreferences() {
        String string = getSharedPreferences().getString(ACCOUNT_TYPE_KEY, null);
        if (string == null) {
            return null;
        }
        return AccountType.valueOf(string);
    }
}
