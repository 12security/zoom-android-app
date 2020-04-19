package com.microsoft.services.msa;

import com.microsoft.services.msa.OAuth.ErrorType;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

class OAuthErrorResponse implements OAuthResponse {
    private final ErrorType error;
    private final String errorDescription;
    private final String errorUri;

    public static class Builder {
        /* access modifiers changed from: private */
        public final ErrorType error;
        /* access modifiers changed from: private */
        public String errorDescription;
        /* access modifiers changed from: private */
        public String errorUri;

        public Builder(ErrorType errorType) {
            if (errorType != null) {
                this.error = errorType;
                return;
            }
            throw new AssertionError();
        }

        public OAuthErrorResponse build() {
            return new OAuthErrorResponse(this);
        }

        public Builder errorDescription(String str) {
            this.errorDescription = str;
            return this;
        }

        public Builder errorUri(String str) {
            this.errorUri = str;
            return this;
        }
    }

    public static OAuthErrorResponse createFromJson(JSONObject jSONObject) throws LiveAuthException {
        try {
            try {
                Builder builder = new Builder(ErrorType.valueOf(jSONObject.getString("error").toUpperCase()));
                if (jSONObject.has("error_description")) {
                    try {
                        builder.errorDescription(jSONObject.getString("error_description"));
                    } catch (JSONException e) {
                        throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e);
                    }
                }
                if (jSONObject.has(OAuth.ERROR_URI)) {
                    try {
                        builder.errorUri(jSONObject.getString(OAuth.ERROR_URI));
                    } catch (JSONException e2) {
                        throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e2);
                    }
                }
                return builder.build();
            } catch (IllegalArgumentException e3) {
                throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e3);
            } catch (NullPointerException e4) {
                throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e4);
            }
        } catch (JSONException e5) {
            throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e5);
        }
    }

    public static boolean validOAuthErrorResponse(JSONObject jSONObject) {
        return jSONObject.has("error");
    }

    private OAuthErrorResponse(Builder builder) {
        this.error = builder.error;
        this.errorDescription = builder.errorDescription;
        this.errorUri = builder.errorUri;
    }

    public void accept(OAuthResponseVisitor oAuthResponseVisitor) {
        oAuthResponseVisitor.visit(this);
    }

    public ErrorType getError() {
        return this.error;
    }

    public String getErrorDescription() {
        return this.errorDescription;
    }

    public String getErrorUri() {
        return this.errorUri;
    }

    public String toString() {
        return String.format("OAuthErrorResponse [error=%s, errorDescription=%s, errorUri=%s]", new Object[]{this.error.toString().toLowerCase(Locale.US), this.errorDescription, this.errorUri});
    }
}
