package com.microsoft.services.msa;

import android.text.TextUtils;
import com.microsoft.services.msa.OAuth.TokenType;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

class OAuthSuccessfulResponse implements OAuthResponse {
    private static final int UNINITIALIZED = -1;
    private final String accessToken;
    private final String authenticationToken;
    private final int expiresIn;
    private final String refreshToken;
    private final String scope;
    private final TokenType tokenType;

    public static class Builder {
        /* access modifiers changed from: private */
        public final String accessToken;
        /* access modifiers changed from: private */
        public String authenticationToken;
        /* access modifiers changed from: private */
        public int expiresIn = -1;
        /* access modifiers changed from: private */
        public String refreshToken;
        /* access modifiers changed from: private */
        public String scope;
        /* access modifiers changed from: private */
        public final TokenType tokenType;

        public Builder(String str, TokenType tokenType2) {
            if (str == null) {
                throw new AssertionError();
            } else if (TextUtils.isEmpty(str)) {
                throw new AssertionError();
            } else if (tokenType2 != null) {
                this.accessToken = str;
                this.tokenType = tokenType2;
            } else {
                throw new AssertionError();
            }
        }

        public Builder authenticationToken(String str) {
            this.authenticationToken = str;
            return this;
        }

        public OAuthSuccessfulResponse build() {
            return new OAuthSuccessfulResponse(this);
        }

        public Builder expiresIn(int i) {
            this.expiresIn = i;
            return this;
        }

        public Builder refreshToken(String str) {
            this.refreshToken = str;
            return this;
        }

        public Builder scope(String str) {
            this.scope = str;
            return this;
        }
    }

    public static OAuthSuccessfulResponse createFromFragment(Map<String, String> map) throws LiveAuthException {
        String str = (String) map.get("access_token");
        String str2 = (String) map.get("token_type");
        if (str == null) {
            throw new AssertionError();
        } else if (str2 != null) {
            try {
                Builder builder = new Builder(str, TokenType.valueOf(str2.toUpperCase()));
                String str3 = (String) map.get(OAuth.AUTHENTICATION_TOKEN);
                if (str3 != null) {
                    builder.authenticationToken(str3);
                }
                String str4 = (String) map.get("expires_in");
                if (str4 != null) {
                    try {
                        builder.expiresIn(Integer.parseInt(str4));
                    } catch (NumberFormatException e) {
                        throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e);
                    }
                }
                String str5 = (String) map.get("scope");
                if (str5 != null) {
                    builder.scope(str5);
                }
                return builder.build();
            } catch (IllegalArgumentException e2) {
                throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e2);
            }
        } else {
            throw new AssertionError();
        }
    }

    public static OAuthSuccessfulResponse createFromJson(JSONObject jSONObject) throws LiveAuthException {
        if (validOAuthSuccessfulResponse(jSONObject)) {
            try {
                try {
                    try {
                        Builder builder = new Builder(jSONObject.getString("access_token"), TokenType.valueOf(jSONObject.getString("token_type").toUpperCase()));
                        if (jSONObject.has(OAuth.AUTHENTICATION_TOKEN)) {
                            try {
                                builder.authenticationToken(jSONObject.getString(OAuth.AUTHENTICATION_TOKEN));
                            } catch (JSONException e) {
                                throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e);
                            }
                        }
                        if (jSONObject.has("refresh_token")) {
                            try {
                                builder.refreshToken(jSONObject.getString("refresh_token"));
                            } catch (JSONException e2) {
                                throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e2);
                            }
                        }
                        if (jSONObject.has("expires_in")) {
                            try {
                                builder.expiresIn(jSONObject.getInt("expires_in"));
                            } catch (JSONException e3) {
                                throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e3);
                            }
                        }
                        if (jSONObject.has("scope")) {
                            try {
                                builder.scope(jSONObject.getString("scope"));
                            } catch (JSONException e4) {
                                throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e4);
                            }
                        }
                        return builder.build();
                    } catch (IllegalArgumentException e5) {
                        throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e5);
                    } catch (NullPointerException e6) {
                        throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e6);
                    }
                } catch (JSONException e7) {
                    throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e7);
                }
            } catch (JSONException e8) {
                throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e8);
            }
        } else {
            throw new AssertionError();
        }
    }

    public static boolean validOAuthSuccessfulResponse(JSONObject jSONObject) {
        return jSONObject.has("access_token") && jSONObject.has("token_type");
    }

    private OAuthSuccessfulResponse(Builder builder) {
        this.accessToken = builder.accessToken;
        this.authenticationToken = builder.authenticationToken;
        this.tokenType = builder.tokenType;
        this.refreshToken = builder.refreshToken;
        this.expiresIn = builder.expiresIn;
        this.scope = builder.scope;
    }

    public void accept(OAuthResponseVisitor oAuthResponseVisitor) {
        oAuthResponseVisitor.visit(this);
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getAuthenticationToken() {
        return this.authenticationToken;
    }

    public int getExpiresIn() {
        return this.expiresIn;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getScope() {
        return this.scope;
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public boolean hasAuthenticationToken() {
        String str = this.authenticationToken;
        return str != null && !TextUtils.isEmpty(str);
    }

    public boolean hasExpiresIn() {
        return this.expiresIn != -1;
    }

    public boolean hasRefreshToken() {
        String str = this.refreshToken;
        return str != null && !TextUtils.isEmpty(str);
    }

    public boolean hasScope() {
        String str = this.scope;
        return str != null && !TextUtils.isEmpty(str);
    }

    public String toString() {
        return String.format("OAuthSuccessfulResponse [accessToken=%s, authenticationToken=%s, tokenType=%s, refreshToken=%s, expiresIn=%s, scope=%s]", new Object[]{this.accessToken, this.authenticationToken, this.tokenType, this.refreshToken, Integer.valueOf(this.expiresIn), this.scope});
    }
}
