package com.microsoft.services.msa;

import android.text.TextUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

abstract class TokenRequest {
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";
    protected final HttpClient client;
    protected final String clientId;
    protected final OAuthConfig mOAuthConfig;

    /* access modifiers changed from: protected */
    public abstract void constructBody(List<NameValuePair> list);

    public TokenRequest(HttpClient httpClient, String str, OAuthConfig oAuthConfig) {
        if (httpClient == null) {
            throw new AssertionError();
        } else if (str == null) {
            throw new AssertionError();
        } else if (!TextUtils.isEmpty(str)) {
            this.client = httpClient;
            this.clientId = str;
            this.mOAuthConfig = oAuthConfig;
        } else {
            throw new AssertionError();
        }
    }

    public OAuthResponse execute() throws LiveAuthException {
        HttpPost httpPost = new HttpPost(this.mOAuthConfig.getTokenUri().toString());
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("client_id", this.clientId));
        constructBody(arrayList);
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity((List<? extends NameValuePair>) arrayList, "UTF-8");
            urlEncodedFormEntity.setContentType(CONTENT_TYPE);
            httpPost.setEntity(urlEncodedFormEntity);
            try {
                try {
                    try {
                        JSONObject jSONObject = new JSONObject(EntityUtils.toString(this.client.execute(httpPost).getEntity()));
                        if (OAuthErrorResponse.validOAuthErrorResponse(jSONObject)) {
                            return OAuthErrorResponse.createFromJson(jSONObject);
                        }
                        if (OAuthSuccessfulResponse.validOAuthSuccessfulResponse(jSONObject)) {
                            return OAuthSuccessfulResponse.createFromJson(jSONObject);
                        }
                        throw new LiveAuthException(ErrorMessages.SERVER_ERROR);
                    } catch (JSONException e) {
                        throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e);
                    }
                } catch (IOException e2) {
                    throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e2);
                }
            } catch (ClientProtocolException e3) {
                throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e3);
            } catch (IOException e4) {
                throw new LiveAuthException(ErrorMessages.SERVER_ERROR, e4);
            }
        } catch (UnsupportedEncodingException e5) {
            throw new LiveAuthException(ErrorMessages.CLIENT_ERROR, e5);
        }
    }
}
