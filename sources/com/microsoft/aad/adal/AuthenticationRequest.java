package com.microsoft.aad.adal;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.UUID;

class AuthenticationRequest implements Serializable {
    private static final int DELIM_NOT_FOUND = -1;
    private static final String UPN_DOMAIN_SUFFIX_DELIM = "@";
    private static final long serialVersionUID = 1;
    private String mAuthority;
    private String mBrokerAccountName;
    private String mClaimsChallenge;
    private String mClientId;
    private UUID mCorrelationId;
    private String mExtraQueryParamsAuthentication;
    private UserIdentifierType mIdentifierType;
    private transient InstanceDiscoveryMetadata mInstanceDiscoveryMetadata;
    private boolean mIsExtendedLifetimeEnabled;
    private String mLoginHint;
    private PromptBehavior mPrompt;
    private String mRedirectUri;
    private int mRequestId;
    private String mResource;
    private boolean mSilent;
    private String mTelemetryRequestId;
    private String mUserId;
    private String mVersion;

    enum UserIdentifierType {
        UniqueId,
        LoginHint,
        NoUser
    }

    AuthenticationRequest() {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mIdentifierType = UserIdentifierType.NoUser;
    }

    AuthenticationRequest(String str, String str2, String str3, String str4, String str5, PromptBehavior promptBehavior, String str6, UUID uuid, boolean z, String str7) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = str;
        this.mResource = str2;
        this.mClientId = str3;
        this.mRedirectUri = str4;
        this.mLoginHint = str5;
        this.mBrokerAccountName = this.mLoginHint;
        this.mPrompt = promptBehavior;
        this.mExtraQueryParamsAuthentication = str6;
        this.mCorrelationId = uuid;
        this.mIdentifierType = UserIdentifierType.NoUser;
        this.mIsExtendedLifetimeEnabled = z;
        this.mClaimsChallenge = str7;
    }

    AuthenticationRequest(String str, String str2, String str3, String str4, String str5, UUID uuid, boolean z) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = str;
        this.mResource = str2;
        this.mClientId = str3;
        this.mRedirectUri = str4;
        this.mLoginHint = str5;
        this.mBrokerAccountName = this.mLoginHint;
        this.mCorrelationId = uuid;
        this.mIsExtendedLifetimeEnabled = z;
    }

    AuthenticationRequest(String str, String str2, String str3, String str4, String str5, boolean z) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = str;
        this.mResource = str2;
        this.mClientId = str3;
        this.mRedirectUri = str4;
        this.mLoginHint = str5;
        this.mBrokerAccountName = this.mLoginHint;
        this.mIsExtendedLifetimeEnabled = z;
    }

    AuthenticationRequest(String str, String str2, String str3, boolean z) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = str;
        this.mResource = str2;
        this.mClientId = str3;
        this.mIsExtendedLifetimeEnabled = z;
    }

    AuthenticationRequest(String str, String str2, String str3, String str4, UUID uuid, boolean z) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = str;
        this.mResource = str2;
        this.mClientId = str3;
        this.mUserId = str4;
        this.mCorrelationId = uuid;
        this.mIsExtendedLifetimeEnabled = z;
    }

    AuthenticationRequest(String str, String str2, String str3, UUID uuid, boolean z) {
        this.mRequestId = 0;
        this.mAuthority = null;
        this.mRedirectUri = null;
        this.mResource = null;
        this.mClientId = null;
        this.mLoginHint = null;
        this.mUserId = null;
        this.mBrokerAccountName = null;
        this.mSilent = false;
        this.mVersion = null;
        this.mIsExtendedLifetimeEnabled = false;
        this.mAuthority = str;
        this.mClientId = str3;
        this.mResource = str2;
        this.mCorrelationId = uuid;
        this.mIsExtendedLifetimeEnabled = z;
    }

    public String getAuthority() {
        return this.mAuthority;
    }

    public void setAuthority(String str) {
        this.mAuthority = str;
    }

    public String getRedirectUri() {
        return this.mRedirectUri;
    }

    public String getResource() {
        return this.mResource;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public String getLoginHint() {
        return this.mLoginHint;
    }

    public UUID getCorrelationId() {
        return this.mCorrelationId;
    }

    public String getExtraQueryParamsAuthentication() {
        return this.mExtraQueryParamsAuthentication;
    }

    public String getLogInfo() {
        return String.format("Request authority:%s clientid:%s", new Object[]{this.mAuthority, this.mClientId});
    }

    public PromptBehavior getPrompt() {
        return this.mPrompt;
    }

    public void setPrompt(PromptBehavior promptBehavior) {
        this.mPrompt = promptBehavior;
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public void setRequestId(int i) {
        this.mRequestId = i;
    }

    public String getBrokerAccountName() {
        return this.mBrokerAccountName;
    }

    public void setBrokerAccountName(String str) {
        this.mBrokerAccountName = str;
    }

    /* access modifiers changed from: 0000 */
    public void setLoginHint(String str) {
        this.mLoginHint = str;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public void setUserId(String str) {
        this.mUserId = str;
    }

    public boolean isSilent() {
        return this.mSilent;
    }

    public void setSilent(boolean z) {
        this.mSilent = z;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public void setVersion(String str) {
        this.mVersion = str;
    }

    public UserIdentifierType getUserIdentifierType() {
        return this.mIdentifierType;
    }

    public void setUserIdentifierType(UserIdentifierType userIdentifierType) {
        this.mIdentifierType = userIdentifierType;
    }

    public boolean getIsExtendedLifetimeEnabled() {
        return this.mIsExtendedLifetimeEnabled;
    }

    public void setClaimsChallenge(String str) {
        this.mClaimsChallenge = str;
    }

    public String getClaimsChallenge() {
        return this.mClaimsChallenge;
    }

    /* access modifiers changed from: 0000 */
    public String getUserFromRequest() {
        if (UserIdentifierType.LoginHint == this.mIdentifierType) {
            return this.mLoginHint;
        }
        if (UserIdentifierType.UniqueId == this.mIdentifierType) {
            return this.mUserId;
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public String getUpnSuffix() {
        String loginHint = getLoginHint();
        if (loginHint == null) {
            return null;
        }
        int lastIndexOf = loginHint.lastIndexOf(UPN_DOMAIN_SUFFIX_DELIM);
        if (-1 == lastIndexOf) {
            return null;
        }
        return loginHint.substring(lastIndexOf + 1);
    }

    /* access modifiers changed from: 0000 */
    public void setTelemetryRequestId(String str) {
        this.mTelemetryRequestId = str;
    }

    /* access modifiers changed from: 0000 */
    public String getTelemetryRequestId() {
        return this.mTelemetryRequestId;
    }

    /* access modifiers changed from: 0000 */
    public void setInstanceDiscoveryMetadata(InstanceDiscoveryMetadata instanceDiscoveryMetadata) {
        this.mInstanceDiscoveryMetadata = instanceDiscoveryMetadata;
    }

    /* access modifiers changed from: 0000 */
    public InstanceDiscoveryMetadata getInstanceDiscoveryMetadata() {
        return this.mInstanceDiscoveryMetadata;
    }
}
