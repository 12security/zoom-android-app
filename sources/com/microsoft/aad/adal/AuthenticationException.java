package com.microsoft.aad.adal;

import android.content.Context;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;

public class AuthenticationException extends Exception {
    static final long serialVersionUID = 1;
    private ADALError mCode;
    private HashMap<String, String> mHttpResponseBody;
    private HashMap<String, List<String>> mHttpResponseHeaders;
    private int mServiceStatusCode;

    public AuthenticationException() {
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
    }

    public AuthenticationException(ADALError aDALError) {
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = aDALError;
    }

    public AuthenticationException(ADALError aDALError, String str) {
        super(str);
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = aDALError;
    }

    public AuthenticationException(ADALError aDALError, String str, Throwable th) {
        super(str, th);
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = aDALError;
        if (th != null && (th instanceof AuthenticationException)) {
            AuthenticationException authenticationException = (AuthenticationException) th;
            this.mServiceStatusCode = authenticationException.getServiceStatusCode();
            if (authenticationException.getHttpResponseBody() != null) {
                this.mHttpResponseBody = new HashMap<>(authenticationException.getHttpResponseBody());
            }
            if (authenticationException.getHttpResponseHeaders() != null) {
                this.mHttpResponseHeaders = new HashMap<>(authenticationException.getHttpResponseHeaders());
            }
        }
    }

    public AuthenticationException(ADALError aDALError, String str, HttpWebResponse httpWebResponse) {
        super(str);
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = aDALError;
        setHttpResponse(httpWebResponse);
    }

    public AuthenticationException(ADALError aDALError, String str, HttpWebResponse httpWebResponse, Throwable th) {
        this(aDALError, str, th);
        setHttpResponse(httpWebResponse);
    }

    public ADALError getCode() {
        return this.mCode;
    }

    public String getMessage() {
        return getLocalizedMessage(null);
    }

    public int getServiceStatusCode() {
        return this.mServiceStatusCode;
    }

    public HashMap<String, String> getHttpResponseBody() {
        return this.mHttpResponseBody;
    }

    public HashMap<String, List<String>> getHttpResponseHeaders() {
        return this.mHttpResponseHeaders;
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponseBody(HashMap<String, String> hashMap) {
        this.mHttpResponseBody = hashMap;
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponseHeaders(HashMap<String, List<String>> hashMap) {
        this.mHttpResponseHeaders = hashMap;
    }

    /* access modifiers changed from: 0000 */
    public void setServiceStatusCode(int i) {
        this.mServiceStatusCode = i;
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponse(HttpWebResponse httpWebResponse) {
        if (httpWebResponse != null) {
            this.mServiceStatusCode = httpWebResponse.getStatusCode();
            if (httpWebResponse.getResponseHeaders() != null) {
                this.mHttpResponseHeaders = new HashMap<>(httpWebResponse.getResponseHeaders());
            }
            if (httpWebResponse.getBody() != null) {
                try {
                    this.mHttpResponseBody = new HashMap<>(HashMapExtensions.getJsonResponse(httpWebResponse));
                } catch (JSONException e) {
                    Logger.m231e(AuthenticationException.class.getSimpleName(), "Json exception", ExceptionExtensions.getExceptionMessage(e), ADALError.SERVER_INVALID_JSON_RESPONSE);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponse(AuthenticationResult authenticationResult) {
        if (authenticationResult != null) {
            this.mHttpResponseBody = authenticationResult.getHttpResponseBody();
            this.mHttpResponseHeaders = authenticationResult.getHttpResponseHeaders();
            this.mServiceStatusCode = authenticationResult.getServiceStatusCode();
        }
    }

    public String getLocalizedMessage(Context context) {
        if (!StringExtensions.isNullOrBlank(super.getMessage())) {
            return super.getMessage();
        }
        ADALError aDALError = this.mCode;
        if (aDALError != null) {
            return aDALError.getLocalizedDescription(context);
        }
        return null;
    }
}
