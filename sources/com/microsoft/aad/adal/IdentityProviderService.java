package com.microsoft.aad.adal;

import com.google.gson.annotations.SerializedName;

final class IdentityProviderService {
    @SerializedName("PassiveAuthEndpoint")
    private String mPassiveAuthEndpoint;

    IdentityProviderService() {
    }

    /* access modifiers changed from: 0000 */
    public String getPassiveAuthEndpoint() {
        return this.mPassiveAuthEndpoint;
    }

    /* access modifiers changed from: 0000 */
    public void setPassiveAuthEndpoint(String str) {
        this.mPassiveAuthEndpoint = str;
    }
}
