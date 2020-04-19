package com.google.api.client.auth.oauth2;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class TokenErrorResponse extends GenericJson {
    @Key
    private String error;
    @Key("error_description")
    private String errorDescription;
    @Key("error_uri")
    private String errorUri;

    public final String getError() {
        return this.error;
    }

    public TokenErrorResponse setError(String str) {
        this.error = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public final String getErrorDescription() {
        return this.errorDescription;
    }

    public TokenErrorResponse setErrorDescription(String str) {
        this.errorDescription = str;
        return this;
    }

    public final String getErrorUri() {
        return this.errorUri;
    }

    public TokenErrorResponse setErrorUri(String str) {
        this.errorUri = str;
        return this;
    }

    public TokenErrorResponse set(String str, Object obj) {
        return (TokenErrorResponse) super.set(str, obj);
    }

    public TokenErrorResponse clone() {
        return (TokenErrorResponse) super.clone();
    }
}
