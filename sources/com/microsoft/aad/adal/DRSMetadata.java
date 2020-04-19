package com.microsoft.aad.adal;

import com.google.gson.annotations.SerializedName;

final class DRSMetadata {
    @SerializedName("IdentityProviderService")
    private IdentityProviderService mIdentityProviderService;

    DRSMetadata() {
    }

    /* access modifiers changed from: 0000 */
    public IdentityProviderService getIdentityProviderService() {
        return this.mIdentityProviderService;
    }

    /* access modifiers changed from: 0000 */
    public void setIdentityProviderService(IdentityProviderService identityProviderService) {
        this.mIdentityProviderService = identityProviderService;
    }
}
