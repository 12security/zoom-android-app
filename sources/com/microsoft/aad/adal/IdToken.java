package com.microsoft.aad.adal;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;

class IdToken {
    private static final String TAG = "IdToken";
    private String mEmail;
    private String mFamilyName;
    private String mGivenName;
    private String mIdentityProvider;
    private String mObjectId;
    private String mPasswordChangeUrl;
    private long mPasswordExpiration;
    private String mSubject;
    private String mTenantId;
    private String mUpn;

    IdToken(String str) throws AuthenticationException {
        Map parseJWT = parseJWT(str);
        if (parseJWT != null && !parseJWT.isEmpty()) {
            this.mSubject = (String) parseJWT.get("sub");
            this.mTenantId = (String) parseJWT.get("tid");
            this.mUpn = (String) parseJWT.get("upn");
            this.mEmail = (String) parseJWT.get("email");
            this.mGivenName = (String) parseJWT.get("given_name");
            this.mFamilyName = (String) parseJWT.get("family_name");
            this.mIdentityProvider = (String) parseJWT.get("idp");
            this.mObjectId = (String) parseJWT.get("oid");
            String str2 = (String) parseJWT.get("pwd_exp");
            if (!StringExtensions.isNullOrBlank(str2)) {
                this.mPasswordExpiration = Long.parseLong(str2);
            }
            this.mPasswordChangeUrl = (String) parseJWT.get("pwd_url");
        }
    }

    public String getSubject() {
        return this.mSubject;
    }

    public String getTenantId() {
        return this.mTenantId;
    }

    public String getUpn() {
        return this.mUpn;
    }

    public String getGivenName() {
        return this.mGivenName;
    }

    public String getFamilyName() {
        return this.mFamilyName;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public String getIdentityProvider() {
        return this.mIdentityProvider;
    }

    public String getObjectId() {
        return this.mObjectId;
    }

    public long getPasswordExpiration() {
        return this.mPasswordExpiration;
    }

    public String getPasswordChangeUrl() {
        return this.mPasswordChangeUrl;
    }

    private Map<String, String> parseJWT(String str) throws AuthenticationException {
        try {
            return HashMapExtensions.jsonStringAsMap(new String(Base64.decode(extractJWTBody(str), 8), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.m232e("IdToken:parseJWT", "The encoding is not supported.", "", ADALError.ENCODING_IS_NOT_SUPPORTED, e);
            throw new AuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED, e.getMessage(), (Throwable) e);
        } catch (JSONException e2) {
            Logger.m232e("IdToken:parseJWT", "Failed to parse the decoded body into JsonObject.", "", ADALError.JSON_PARSE_ERROR, e2);
            throw new AuthenticationException(ADALError.JSON_PARSE_ERROR, e2.getMessage(), (Throwable) e2);
        }
    }

    private String extractJWTBody(String str) throws AuthenticationException {
        int indexOf = str.indexOf(46);
        int i = indexOf + 1;
        int indexOf2 = str.indexOf(46, i);
        if (str.indexOf(46, indexOf2 + 1) == -1 && indexOf > 0 && indexOf2 > 0) {
            return str.substring(i, indexOf2);
        }
        throw new AuthenticationException(ADALError.IDTOKEN_PARSING_FAILURE, "Failed to extract the ClientID");
    }
}
