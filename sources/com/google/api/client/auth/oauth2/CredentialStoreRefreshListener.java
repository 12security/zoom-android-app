package com.google.api.client.auth.oauth2;

import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

@Beta
@Deprecated
public final class CredentialStoreRefreshListener implements CredentialRefreshListener {
    private final CredentialStore credentialStore;
    private final String userId;

    public CredentialStoreRefreshListener(String str, CredentialStore credentialStore2) {
        this.userId = (String) Preconditions.checkNotNull(str);
        this.credentialStore = (CredentialStore) Preconditions.checkNotNull(credentialStore2);
    }

    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
        makePersistent(credential);
    }

    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
        makePersistent(credential);
    }

    public CredentialStore getCredentialStore() {
        return this.credentialStore;
    }

    public void makePersistent(Credential credential) throws IOException {
        this.credentialStore.store(this.userId, credential);
    }
}
