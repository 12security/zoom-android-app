package com.google.api.client.googleapis.auth.clientlogin;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponseException.Builder;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Key;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Strings;
import java.io.IOException;

@Beta
public final class ClientLogin {
    @Key
    public String accountType;
    @Key("source")
    public String applicationName;
    @Key("service")
    public String authTokenType;
    @Key("logincaptcha")
    public String captchaAnswer;
    @Key("logintoken")
    public String captchaToken;
    @Key("Passwd")
    public String password;
    public GenericUrl serverUrl = new GenericUrl("https://www.google.com");
    public HttpTransport transport;
    @Key("Email")
    public String username;

    public static final class ErrorInfo {
        @Key("CaptchaToken")
        public String captchaToken;
        @Key("CaptchaUrl")
        public String captchaUrl;
        @Key("Error")
        public String error;
        @Key("Url")
        public String url;
    }

    public static final class Response implements HttpExecuteInterceptor, HttpRequestInitializer {
        @Key("Auth")
        public String auth;

        public String getAuthorizationHeaderValue() {
            return ClientLogin.getAuthorizationHeaderValue(this.auth);
        }

        public void initialize(HttpRequest httpRequest) {
            httpRequest.setInterceptor(this);
        }

        public void intercept(HttpRequest httpRequest) {
            httpRequest.getHeaders().setAuthorization(getAuthorizationHeaderValue());
        }
    }

    public Response authenticate() throws IOException {
        GenericUrl clone = this.serverUrl.clone();
        clone.appendRawPath("/accounts/ClientLogin");
        HttpRequest buildPostRequest = this.transport.createRequestFactory().buildPostRequest(clone, new UrlEncodedContent(this));
        buildPostRequest.setParser(AuthKeyValueParser.INSTANCE);
        buildPostRequest.setContentLoggingLimit(0);
        buildPostRequest.setThrowExceptionOnExecuteError(false);
        HttpResponse execute = buildPostRequest.execute();
        if (execute.isSuccessStatusCode()) {
            return (Response) execute.parseAs(Response.class);
        }
        Builder builder = new Builder(execute.getStatusCode(), execute.getStatusMessage(), execute.getHeaders());
        ErrorInfo errorInfo = (ErrorInfo) execute.parseAs(ErrorInfo.class);
        String obj = errorInfo.toString();
        StringBuilder computeMessageBuffer = HttpResponseException.computeMessageBuffer(execute);
        if (!Strings.isNullOrEmpty(obj)) {
            computeMessageBuffer.append(StringUtils.LINE_SEPARATOR);
            computeMessageBuffer.append(obj);
            builder.setContent(obj);
        }
        builder.setMessage(computeMessageBuffer.toString());
        throw new ClientLoginResponseException(builder, errorInfo);
    }

    public static String getAuthorizationHeaderValue(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("GoogleLogin auth=");
        sb.append(str);
        return sb.toString();
    }
}
