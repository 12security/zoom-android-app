package com.microsoft.aad.adal;

import android.content.Intent;
import android.text.TextUtils;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class WebviewHelper {
    private static final String TAG = "WebviewHelper";
    private final Oauth2 mOauth = new Oauth2(this.mRequest);
    private final AuthenticationRequest mRequest = getAuthenticationRequestFromIntent(this.mRequestIntent);
    private final Intent mRequestIntent;

    public static class PreKeyAuthInfo {
        private final HashMap<String, String> mHttpHeaders;
        private final String mLoadUrl;

        public PreKeyAuthInfo(HashMap<String, String> hashMap, String str) {
            this.mHttpHeaders = hashMap;
            this.mLoadUrl = str;
        }

        public HashMap<String, String> getHttpHeaders() {
            return this.mHttpHeaders;
        }

        public String getLoadUrl() {
            return this.mLoadUrl;
        }
    }

    public WebviewHelper(Intent intent) {
        this.mRequestIntent = intent;
    }

    public void validateRequestIntent() {
        AuthenticationRequest authenticationRequest = this.mRequest;
        if (authenticationRequest == null) {
            Logger.m236v(TAG, "Request item is null, so it returns to caller");
            throw new IllegalArgumentException("Request is null");
        } else if (TextUtils.isEmpty(authenticationRequest.getAuthority())) {
            throw new IllegalArgumentException("Authority is null");
        } else if (TextUtils.isEmpty(this.mRequest.getResource())) {
            throw new IllegalArgumentException("Resource is null");
        } else if (TextUtils.isEmpty(this.mRequest.getClientId())) {
            throw new IllegalArgumentException("ClientId is null");
        } else if (TextUtils.isEmpty(this.mRequest.getRedirectUri())) {
            throw new IllegalArgumentException("RedirectUri is null");
        }
    }

    public String getStartUrl() throws UnsupportedEncodingException {
        return this.mOauth.getCodeRequestUrl();
    }

    public String getRedirectUrl() {
        return this.mRequest.getRedirectUri();
    }

    public Intent getResultIntent(String str) {
        Intent intent = this.mRequestIntent;
        if (intent != null) {
            AuthenticationRequest authenticationRequestFromIntent = getAuthenticationRequestFromIntent(intent);
            Intent intent2 = new Intent();
            intent2.putExtra(Browser.RESPONSE_FINAL_URL, str);
            intent2.putExtra(Browser.RESPONSE_REQUEST_INFO, authenticationRequestFromIntent);
            intent2.putExtra(Browser.REQUEST_ID, authenticationRequestFromIntent.getRequestId());
            return intent2;
        }
        throw new IllegalArgumentException("requestIntent is null");
    }

    private AuthenticationRequest getAuthenticationRequestFromIntent(Intent intent) {
        Serializable serializableExtra = intent.getSerializableExtra(Browser.REQUEST_MESSAGE);
        if (serializableExtra instanceof AuthenticationRequest) {
            return (AuthenticationRequest) serializableExtra;
        }
        return null;
    }

    public PreKeyAuthInfo getPreKeyAuthInfo(String str) throws UnsupportedEncodingException, AuthenticationException {
        ChallengeResponse challengeResponseFromUri = new ChallengeResponseBuilder(new JWSBuilder()).getChallengeResponseFromUri(str);
        HashMap hashMap = new HashMap();
        hashMap.put("Authorization", challengeResponseFromUri.getAuthorizationHeaderValue());
        String submitUrl = challengeResponseFromUri.getSubmitUrl();
        HashMap urlParameters = StringExtensions.getUrlParameters(challengeResponseFromUri.getSubmitUrl());
        StringBuilder sb = new StringBuilder();
        sb.append("SubmitUrl:");
        sb.append(challengeResponseFromUri.getSubmitUrl());
        Logger.m234i(TAG, "Get submit url. ", sb.toString());
        if (!urlParameters.containsKey("client_id")) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(submitUrl);
            sb2.append("?");
            sb2.append(this.mOauth.getAuthorizationEndpointQueryParameters());
            submitUrl = sb2.toString();
        }
        return new PreKeyAuthInfo(hashMap, submitUrl);
    }
}
