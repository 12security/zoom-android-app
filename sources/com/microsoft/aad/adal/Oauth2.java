package com.microsoft.aad.adal;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Base64;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

class Oauth2 {
    private static final String DEFAULT_AUTHORIZE_ENDPOINT = "/oauth2/authorize";
    private static final String DEFAULT_TOKEN_ENDPOINT = "/oauth2/token";
    private static final int DELAY_TIME_PERIOD = 1000;
    private static final String HTTPS_PROTOCOL_STRING = "https";
    private static final int MAX_RESILIENCY_ERROR_CODE = 599;
    private static final String TAG = "Oauth";
    private IJWSBuilder mJWSBuilder = new JWSBuilder();
    private AuthenticationRequest mRequest;
    private boolean mRetryOnce = true;
    private String mTokenEndpoint;
    private IWebRequestHandler mWebRequestHandler;

    Oauth2(AuthenticationRequest authenticationRequest) {
        this.mRequest = authenticationRequest;
        this.mWebRequestHandler = null;
        this.mJWSBuilder = null;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append(DEFAULT_TOKEN_ENDPOINT);
        setTokenEndpoint(sb.toString());
    }

    Oauth2(AuthenticationRequest authenticationRequest, IWebRequestHandler iWebRequestHandler) {
        this.mRequest = authenticationRequest;
        this.mWebRequestHandler = iWebRequestHandler;
        this.mJWSBuilder = null;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append(DEFAULT_TOKEN_ENDPOINT);
        setTokenEndpoint(sb.toString());
    }

    Oauth2(AuthenticationRequest authenticationRequest, IWebRequestHandler iWebRequestHandler, IJWSBuilder iJWSBuilder) {
        this.mRequest = authenticationRequest;
        this.mWebRequestHandler = iWebRequestHandler;
        this.mJWSBuilder = iJWSBuilder;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append(DEFAULT_TOKEN_ENDPOINT);
        setTokenEndpoint(sb.toString());
    }

    public String getAuthorizationEndpoint() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mRequest.getAuthority());
        sb.append(DEFAULT_AUTHORIZE_ENDPOINT);
        return sb.toString();
    }

    public String getTokenEndpoint() {
        return this.mTokenEndpoint;
    }

    public String getAuthorizationEndpointQueryParameters() throws UnsupportedEncodingException {
        Builder builder = new Builder();
        builder.appendQueryParameter("response_type", "code").appendQueryParameter("client_id", URLEncoder.encode(this.mRequest.getClientId(), "UTF_8")).appendQueryParameter(AAD.RESOURCE, URLEncoder.encode(this.mRequest.getResource(), "UTF_8")).appendQueryParameter("redirect_uri", URLEncoder.encode(this.mRequest.getRedirectUri(), "UTF_8")).appendQueryParameter("state", encodeProtocolState());
        if (!StringExtensions.isNullOrBlank(this.mRequest.getLoginHint())) {
            builder.appendQueryParameter("login_hint", URLEncoder.encode(this.mRequest.getLoginHint(), "UTF_8"));
        }
        builder.appendQueryParameter(AAD.ADAL_ID_PLATFORM, AAD.ADAL_ID_PLATFORM_VALUE).appendQueryParameter(AAD.ADAL_ID_VERSION, URLEncoder.encode(AuthenticationContext.getVersionName(), "UTF_8")).appendQueryParameter(AAD.ADAL_ID_OS_VER, URLEncoder.encode(String.valueOf(VERSION.SDK_INT), "UTF_8")).appendQueryParameter(AAD.ADAL_ID_DM, URLEncoder.encode(Build.MODEL, "UTF_8"));
        if (this.mRequest.getCorrelationId() != null) {
            builder.appendQueryParameter(AAD.CLIENT_REQUEST_ID, URLEncoder.encode(this.mRequest.getCorrelationId().toString(), "UTF_8"));
        }
        if (this.mRequest.getPrompt() == PromptBehavior.Always) {
            builder.appendQueryParameter(AAD.QUERY_PROMPT, URLEncoder.encode("login", "UTF_8"));
        } else if (this.mRequest.getPrompt() == PromptBehavior.REFRESH_SESSION) {
            builder.appendQueryParameter(AAD.QUERY_PROMPT, URLEncoder.encode(AAD.QUERY_PROMPT_REFRESH_SESSION_VALUE, "UTF_8"));
        }
        String extraQueryParamsAuthentication = this.mRequest.getExtraQueryParamsAuthentication();
        if (StringExtensions.isNullOrBlank(extraQueryParamsAuthentication) || !extraQueryParamsAuthentication.contains("haschrome")) {
            builder.appendQueryParameter("haschrome", "1");
        }
        if (!StringExtensions.isNullOrBlank(this.mRequest.getClaimsChallenge())) {
            builder.appendQueryParameter("claims", this.mRequest.getClaimsChallenge());
        }
        String query = builder.build().getQuery();
        if (StringExtensions.isNullOrBlank(extraQueryParamsAuthentication)) {
            return query;
        }
        if (!extraQueryParamsAuthentication.startsWith("&")) {
            StringBuilder sb = new StringBuilder();
            sb.append("&");
            sb.append(extraQueryParamsAuthentication);
            extraQueryParamsAuthentication = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(query);
        sb2.append(extraQueryParamsAuthentication);
        return sb2.toString();
    }

    public String getCodeRequestUrl() throws UnsupportedEncodingException {
        return String.format("%s?%s", new Object[]{getAuthorizationEndpoint(), getAuthorizationEndpointQueryParameters()});
    }

    public String buildTokenRequestMessage(String str) throws UnsupportedEncodingException {
        Logger.m236v(TAG, "Building request message for redeeming token with auth code.");
        return String.format("%s=%s&%s=%s&%s=%s&%s=%s", new Object[]{"grant_type", StringExtensions.urlFormEncode(OAuth2.AUTHORIZATION_CODE), "code", StringExtensions.urlFormEncode(str), "client_id", StringExtensions.urlFormEncode(this.mRequest.getClientId()), "redirect_uri", StringExtensions.urlFormEncode(this.mRequest.getRedirectUri())});
    }

    public String buildRefreshTokenRequestMessage(String str) throws UnsupportedEncodingException {
        Logger.m236v(TAG, "Building request message for redeeming token with refresh token.");
        String format = String.format("%s=%s&%s=%s&%s=%s", new Object[]{"grant_type", StringExtensions.urlFormEncode("refresh_token"), "refresh_token", StringExtensions.urlFormEncode(str), "client_id", StringExtensions.urlFormEncode(this.mRequest.getClientId())});
        if (StringExtensions.isNullOrBlank(this.mRequest.getResource())) {
            return format;
        }
        return String.format("%s&%s=%s", new Object[]{format, AAD.RESOURCE, StringExtensions.urlFormEncode(this.mRequest.getResource())});
    }

    public AuthenticationResult processUIResponseParams(Map<String, String> map) throws AuthenticationException {
        String str;
        String str2;
        UserInfo userInfo;
        Map<String, String> map2 = map;
        String str3 = null;
        if (map2.containsKey("error")) {
            String str4 = (String) map2.get(AAD.CORRELATION_ID);
            if (!StringExtensions.isNullOrBlank(str4)) {
                try {
                    Logger.setCorrelationId(UUID.fromString(str4));
                } catch (IllegalArgumentException unused) {
                    String str5 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("CorrelationId is malformed: ");
                    sb.append(str4);
                    Logger.m231e(str5, sb.toString(), "", ADALError.CORRELATION_ID_FORMAT);
                }
            }
            String str6 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("OAuth2 error:");
            sb2.append((String) map2.get("error"));
            String sb3 = sb2.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(" Description:");
            sb4.append((String) map2.get("error_description"));
            Logger.m234i(str6, sb3, sb4.toString());
            return new AuthenticationResult((String) map2.get("error"), (String) map2.get("error_description"), (String) map2.get(OAuth2.ERROR_CODES));
        } else if (map2.containsKey("code")) {
            AuthenticationResult authenticationResult = new AuthenticationResult((String) map2.get("code"));
            String str7 = (String) map2.get("cloud_instance_host_name");
            if (StringExtensions.isNullOrBlank(str7)) {
                return authenticationResult;
            }
            String uri = new Builder().scheme(HTTPS_PROTOCOL_STRING).authority(str7).path(StringExtensions.getUrl(this.mRequest.getAuthority()).getPath()).build().toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append(uri);
            sb5.append(DEFAULT_TOKEN_ENDPOINT);
            setTokenEndpoint(sb5.toString());
            authenticationResult.setAuthority(uri);
            return authenticationResult;
        } else if (!map2.containsKey("access_token")) {
            return null;
        } else {
            String str8 = (String) map2.get("expires_in");
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            int i = AuthenticationConstants.DEFAULT_EXPIRATION_TIME_SEC;
            gregorianCalendar.add(13, (str8 == null || str8.isEmpty()) ? AuthenticationConstants.DEFAULT_EXPIRATION_TIME_SEC : Integer.parseInt(str8));
            String str9 = (String) map2.get("refresh_token");
            boolean z = map2.containsKey(AAD.RESOURCE) && !StringExtensions.isNullOrBlank(str9);
            if (map2.containsKey("id_token")) {
                String str10 = (String) map2.get("id_token");
                if (!StringExtensions.isNullOrBlank(str10)) {
                    Logger.m236v(TAG, "Id token was returned, parsing id token.");
                    IdToken idToken = new IdToken(str10);
                    str = str10;
                    str2 = idToken.getTenantId();
                    userInfo = new UserInfo(idToken);
                } else {
                    Logger.m236v(TAG, "IdToken was not returned from token request.");
                    str = str10;
                    userInfo = null;
                    str2 = null;
                }
            } else {
                userInfo = null;
                str2 = null;
                str = null;
            }
            if (map2.containsKey("foci")) {
                str3 = (String) map2.get("foci");
            }
            AuthenticationResult authenticationResult2 = new AuthenticationResult((String) map2.get("access_token"), str9, gregorianCalendar.getTime(), z, userInfo, str2, str, null);
            if (map2.containsKey("ext_expires_in")) {
                String str11 = (String) map2.get("ext_expires_in");
                GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
                if (!StringExtensions.isNullOrBlank(str11)) {
                    i = Integer.parseInt(str11);
                }
                gregorianCalendar2.add(13, i);
                authenticationResult2.setExtendedExpiresOn(gregorianCalendar2.getTime());
            }
            authenticationResult2.setFamilyClientId(str3);
            return authenticationResult2;
        }
    }

    private static void extractJsonObjects(Map<String, String> map, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str2 = (String) keys.next();
            map.put(str2, jSONObject.getString(str2));
        }
    }

    public AuthenticationResult refreshToken(String str) throws IOException, AuthenticationException {
        if (this.mWebRequestHandler != null) {
            try {
                String buildRefreshTokenRequestMessage = buildRefreshTokenRequestMessage(str);
                Map requestHeaders = getRequestHeaders();
                requestHeaders.put(Broker.CHALLENGE_TLS_INCAPABLE, "1.0");
                Logger.m236v(TAG, "Sending request to redeem token with refresh token.");
                return postMessage(buildRefreshTokenRequestMessage, requestHeaders);
            } catch (UnsupportedEncodingException e) {
                Logger.m232e(TAG, ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), e.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, e);
                return null;
            }
        } else {
            Logger.m236v(TAG, "Web request is not set correctly.");
            throw new IllegalArgumentException("webRequestHandler is null.");
        }
    }

    public AuthenticationResult getToken(String str) throws IOException, AuthenticationException {
        if (!StringExtensions.isNullOrBlank(str)) {
            HashMap urlParameters = StringExtensions.getUrlParameters(str);
            String decodeProtocolState = decodeProtocolState((String) urlParameters.get("state"));
            if (!StringExtensions.isNullOrBlank(decodeProtocolState)) {
                StringBuilder sb = new StringBuilder();
                sb.append("http://state/path?");
                sb.append(decodeProtocolState);
                Uri parse = Uri.parse(sb.toString());
                String queryParameter = parse.getQueryParameter("a");
                String queryParameter2 = parse.getQueryParameter("r");
                if (StringExtensions.isNullOrBlank(queryParameter) || StringExtensions.isNullOrBlank(queryParameter2) || !queryParameter2.equalsIgnoreCase(this.mRequest.getResource())) {
                    throw new AuthenticationException(ADALError.AUTH_FAILED_BAD_STATE);
                }
                AuthenticationResult processUIResponseParams = processUIResponseParams(urlParameters);
                if (processUIResponseParams == null || processUIResponseParams.getCode() == null || processUIResponseParams.getCode().isEmpty()) {
                    return processUIResponseParams;
                }
                AuthenticationResult tokenForCode = getTokenForCode(processUIResponseParams.getCode());
                if (!StringExtensions.isNullOrBlank(processUIResponseParams.getAuthority())) {
                    tokenForCode.setAuthority(processUIResponseParams.getAuthority());
                } else {
                    tokenForCode.setAuthority(this.mRequest.getAuthority());
                }
                return tokenForCode;
            }
            throw new AuthenticationException(ADALError.AUTH_FAILED_NO_STATE);
        }
        throw new IllegalArgumentException("authorizationUrl");
    }

    public AuthenticationResult getTokenForCode(String str) throws IOException, AuthenticationException {
        if (this.mWebRequestHandler != null) {
            try {
                String buildTokenRequestMessage = buildTokenRequestMessage(str);
                Map requestHeaders = getRequestHeaders();
                Logger.m236v("Oauth:getTokenForCode", "Sending request to redeem token with auth code.");
                return postMessage(buildTokenRequestMessage, requestHeaders);
            } catch (UnsupportedEncodingException e) {
                Logger.m232e("Oauth:getTokenForCode", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), e.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, e);
                return null;
            }
        } else {
            throw new IllegalArgumentException("webRequestHandler");
        }
    }

    private AuthenticationResult postMessage(String str, Map<String, String> map) throws IOException, AuthenticationException {
        HttpWebResponse sendPost;
        AuthenticationResult authenticationResult;
        String str2;
        HttpEvent startHttpEvent = startHttpEvent();
        URL url = StringExtensions.getUrl(getTokenEndpoint());
        if (url != null) {
            startHttpEvent.setHttpPath(url);
            try {
                this.mWebRequestHandler.setRequestCorrelationId(this.mRequest.getCorrelationId());
                ClientMetrics.INSTANCE.beginClientMetricsRecord(url, this.mRequest.getCorrelationId(), map);
                sendPost = this.mWebRequestHandler.sendPost(url, map, str.getBytes("UTF_8"), "application/x-www-form-urlencoded");
                startHttpEvent.setResponseCode(sendPost.getStatusCode());
                startHttpEvent.setCorrelationId(this.mRequest.getCorrelationId().toString());
                stopHttpEvent(startHttpEvent);
                if (sendPost.getStatusCode() == 401) {
                    if (sendPost.getResponseHeaders() == null || !sendPost.getResponseHeaders().containsKey("WWW-Authenticate")) {
                        Logger.m236v("Oauth:postMessage", "401 http status code is returned without authorization header.");
                    } else {
                        String str3 = (String) ((List) sendPost.getResponseHeaders().get("WWW-Authenticate")).get(0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("Challenge header: ");
                        sb.append(str3);
                        Logger.m234i("Oauth:postMessage", "Device certificate challenge request. ", sb.toString());
                        if (StringExtensions.isNullOrBlank(str3)) {
                            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Challenge header is empty", sendPost);
                        } else if (StringExtensions.hasPrefixInHeader(str3, Broker.CHALLENGE_RESPONSE_TYPE)) {
                            HttpEvent startHttpEvent2 = startHttpEvent();
                            startHttpEvent2.setHttpPath(url);
                            Logger.m236v("Oauth:postMessage", "Received pkeyAuth device challenge.");
                            ChallengeResponseBuilder challengeResponseBuilder = new ChallengeResponseBuilder(this.mJWSBuilder);
                            Logger.m236v("Oauth:postMessage", "Processing device challenge.");
                            map.put("Authorization", challengeResponseBuilder.getChallengeResponseFromHeader(str3, url.toString()).getAuthorizationHeaderValue());
                            Logger.m236v("Oauth:postMessage", "Sending request with challenge response.");
                            HttpWebResponse sendPost2 = this.mWebRequestHandler.sendPost(url, map, str.getBytes("UTF_8"), "application/x-www-form-urlencoded");
                            startHttpEvent2.setResponseCode(sendPost2.getStatusCode());
                            startHttpEvent2.setCorrelationId(this.mRequest.getCorrelationId().toString());
                            stopHttpEvent(startHttpEvent2);
                            sendPost = sendPost2;
                        }
                    }
                }
                boolean isEmpty = TextUtils.isEmpty(sendPost.getBody());
                if (!isEmpty) {
                    Logger.m236v("Oauth:postMessage", "Token request does not have exception.");
                    authenticationResult = processTokenResponse(sendPost, startHttpEvent);
                    ClientMetrics.INSTANCE.setLastError(null);
                } else {
                    authenticationResult = null;
                }
                if (authenticationResult == null) {
                    if (isEmpty) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Status code:");
                        sb2.append(sendPost.getStatusCode());
                        str2 = sb2.toString();
                    } else {
                        str2 = sendPost.getBody();
                    }
                    Logger.m231e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), str2, ADALError.SERVER_ERROR);
                    throw new AuthenticationException(ADALError.SERVER_ERROR, str2, sendPost);
                }
                ClientMetrics.INSTANCE.setLastErrorCodes(authenticationResult.getErrorCodes());
                ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                return authenticationResult;
            } catch (ServerRespondingWithRetryableException e) {
                AuthenticationResult retry = retry(str, map);
                if (retry != null) {
                    ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                    return retry;
                } else if (this.mRequest.getIsExtendedLifetimeEnabled()) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("WebResponse is not a success due to: ");
                    sb3.append(sendPost.getStatusCode());
                    Logger.m236v("Oauth:postMessage", sb3.toString());
                    throw e;
                } else {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("WebResponse is not a success due to: ");
                    sb4.append(sendPost.getStatusCode());
                    Logger.m236v("Oauth:postMessage", sb4.toString());
                    ADALError aDALError = ADALError.SERVER_ERROR;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("WebResponse is not a success due to: ");
                    sb5.append(sendPost.getStatusCode());
                    throw new AuthenticationException(aDALError, sb5.toString(), sendPost);
                }
            } catch (UnsupportedEncodingException e2) {
                ClientMetrics.INSTANCE.setLastError(null);
                Logger.m232e("Oauth:postMessage", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), e2.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, e2);
                throw e2;
            } catch (SocketTimeoutException e3) {
                AuthenticationResult retry2 = retry(str, map);
                if (retry2 != null) {
                    ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                    return retry2;
                }
                ClientMetrics.INSTANCE.setLastError(null);
                if (this.mRequest.getIsExtendedLifetimeEnabled()) {
                    Logger.m232e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), e3.getMessage(), ADALError.SERVER_ERROR, e3);
                    throw new ServerRespondingWithRetryableException(e3.getMessage(), (Throwable) e3);
                }
                Logger.m232e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), e3.getMessage(), ADALError.SERVER_ERROR, e3);
                throw e3;
            } catch (IOException e4) {
                try {
                    ClientMetrics.INSTANCE.setLastError(null);
                    Logger.m232e("Oauth:postMessage", ADALError.SERVER_ERROR.getDescription(), e4.getMessage(), ADALError.SERVER_ERROR, e4);
                    throw e4;
                } catch (Throwable th) {
                    ClientMetrics.INSTANCE.endClientMetricsRecord("token", this.mRequest.getCorrelationId());
                    throw th;
                }
            }
        } else {
            stopHttpEvent(startHttpEvent);
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL);
        }
    }

    private AuthenticationResult retry(String str, Map<String, String> map) throws IOException, AuthenticationException {
        if (!this.mRetryOnce) {
            return null;
        }
        this.mRetryOnce = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException unused) {
            Logger.m236v("Oauth:retry", "The thread is interrupted while it is sleeping. ");
        }
        Logger.m236v("Oauth:retry", "Try again...");
        return postMessage(str, map);
    }

    public static String decodeProtocolState(String str) throws UnsupportedEncodingException {
        if (!StringExtensions.isNullOrBlank(str)) {
            return new String(Base64.decode(str, 9), "UTF-8");
        }
        return null;
    }

    public String encodeProtocolState() throws UnsupportedEncodingException {
        return Base64.encodeToString(String.format("a=%s&r=%s", new Object[]{this.mRequest.getAuthority(), this.mRequest.getResource()}).getBytes("UTF-8"), 9);
    }

    private Map<String, String> getRequestHeaders() {
        HashMap hashMap = new HashMap();
        hashMap.put("Accept", WebRequestHandler.HEADER_ACCEPT_JSON);
        return hashMap;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.microsoft.aad.adal.AuthenticationResult processTokenResponse(com.microsoft.aad.adal.HttpWebResponse r8, com.microsoft.aad.adal.HttpEvent r9) throws com.microsoft.aad.adal.AuthenticationException {
        /*
            r7 = this;
            java.util.Map r0 = r8.getResponseHeaders()
            r1 = 0
            if (r0 == 0) goto L_0x00b3
            java.util.Map r0 = r8.getResponseHeaders()
            java.lang.String r2 = "client-request-id"
            boolean r0 = r0.containsKey(r2)
            r2 = 0
            if (r0 == 0) goto L_0x002f
            java.util.Map r0 = r8.getResponseHeaders()
            java.lang.String r3 = "client-request-id"
            java.lang.Object r0 = r0.get(r3)
            java.util.List r0 = (java.util.List) r0
            if (r0 == 0) goto L_0x002f
            int r3 = r0.size()
            if (r3 <= 0) goto L_0x002f
            java.lang.Object r0 = r0.get(r2)
            java.lang.String r0 = (java.lang.String) r0
            goto L_0x0030
        L_0x002f:
            r0 = r1
        L_0x0030:
            java.util.Map r3 = r8.getResponseHeaders()
            java.lang.String r4 = "x-ms-request-id"
            boolean r3 = r3.containsKey(r4)
            if (r3 == 0) goto L_0x0075
            java.util.Map r3 = r8.getResponseHeaders()
            java.lang.String r4 = "x-ms-request-id"
            java.lang.Object r3 = r3.get(r4)
            java.util.List r3 = (java.util.List) r3
            if (r3 == 0) goto L_0x0075
            int r4 = r3.size()
            if (r4 <= 0) goto L_0x0075
            java.lang.String r4 = "Oauth:processTokenResponse"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Set request id header. x-ms-request-id: "
            r5.append(r6)
            java.lang.Object r6 = r3.get(r2)
            java.lang.String r6 = (java.lang.String) r6
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.microsoft.aad.adal.Logger.m236v(r4, r5)
            java.lang.Object r3 = r3.get(r2)
            java.lang.String r3 = (java.lang.String) r3
            r9.setRequestIdHeader(r3)
        L_0x0075:
            java.util.Map r3 = r8.getResponseHeaders()
            java.lang.String r4 = "x-ms-clitelem"
            java.lang.Object r3 = r3.get(r4)
            if (r3 == 0) goto L_0x00b4
            java.util.Map r3 = r8.getResponseHeaders()
            java.lang.String r4 = "x-ms-clitelem"
            java.lang.Object r3 = r3.get(r4)
            java.util.List r3 = (java.util.List) r3
            boolean r3 = r3.isEmpty()
            if (r3 != 0) goto L_0x00b4
            java.util.Map r3 = r8.getResponseHeaders()
            java.lang.String r4 = "x-ms-clitelem"
            java.lang.Object r3 = r3.get(r4)
            java.util.List r3 = (java.util.List) r3
            java.lang.Object r2 = r3.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            com.microsoft.aad.adal.TelemetryUtils$CliTelemInfo r2 = com.microsoft.aad.adal.TelemetryUtils.parseXMsCliTelemHeader(r2)
            if (r2 == 0) goto L_0x00b4
            r9.setXMsCliTelemData(r2)
            java.lang.String r1 = r2.getSpeRing()
            goto L_0x00b4
        L_0x00b3:
            r0 = r1
        L_0x00b4:
            int r2 = r8.getStatusCode()
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 == r3) goto L_0x0115
            r3 = 400(0x190, float:5.6E-43)
            if (r2 == r3) goto L_0x0115
            r3 = 401(0x191, float:5.62E-43)
            if (r2 != r3) goto L_0x00c5
            goto L_0x0115
        L_0x00c5:
            r9 = 500(0x1f4, float:7.0E-43)
            if (r2 < r9) goto L_0x00f0
            r9 = 599(0x257, float:8.4E-43)
            if (r2 > r9) goto L_0x00f0
            com.microsoft.aad.adal.ServerRespondingWithRetryableException r9 = new com.microsoft.aad.adal.ServerRespondingWithRetryableException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Server Error "
            r0.append(r1)
            r0.append(r2)
            java.lang.String r1 = " "
            r0.append(r1)
            java.lang.String r1 = r8.getBody()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9.<init>(r0, r8)
            throw r9
        L_0x00f0:
            com.microsoft.aad.adal.AuthenticationException r9 = new com.microsoft.aad.adal.AuthenticationException
            com.microsoft.aad.adal.ADALError r0 = com.microsoft.aad.adal.ADALError.SERVER_ERROR
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Unexpected server response "
            r1.append(r3)
            r1.append(r2)
            java.lang.String r2 = " "
            r1.append(r2)
            java.lang.String r2 = r8.getBody()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r9.<init>(r0, r1, r8)
            throw r9
        L_0x0115:
            java.lang.String r2 = r8.getBody()     // Catch:{ JSONException -> 0x0190 }
            com.microsoft.aad.adal.AuthenticationResult r2 = r7.parseJsonResponse(r2)     // Catch:{ JSONException -> 0x0190 }
            if (r2 == 0) goto L_0x013a
            java.lang.String r3 = r2.getErrorCode()     // Catch:{ JSONException -> 0x0190 }
            if (r3 == 0) goto L_0x0128
            r2.setHttpResponse(r8)     // Catch:{ JSONException -> 0x0190 }
        L_0x0128:
            com.microsoft.aad.adal.TelemetryUtils$CliTelemInfo r3 = new com.microsoft.aad.adal.TelemetryUtils$CliTelemInfo     // Catch:{ JSONException -> 0x0190 }
            r3.<init>()     // Catch:{ JSONException -> 0x0190 }
            r3.setSpeRing(r1)     // Catch:{ JSONException -> 0x0190 }
            r2.setCliTelemInfo(r3)     // Catch:{ JSONException -> 0x0190 }
            java.lang.String r1 = r2.getErrorCode()     // Catch:{ JSONException -> 0x0190 }
            r9.setOauthErrorCode(r1)     // Catch:{ JSONException -> 0x0190 }
        L_0x013a:
            if (r0 == 0) goto L_0x018f
            boolean r8 = r0.isEmpty()
            if (r8 != 0) goto L_0x018f
            java.util.UUID r8 = java.util.UUID.fromString(r0)     // Catch:{ IllegalArgumentException -> 0x0174 }
            com.microsoft.aad.adal.AuthenticationRequest r9 = r7.mRequest     // Catch:{ IllegalArgumentException -> 0x0174 }
            java.util.UUID r9 = r9.getCorrelationId()     // Catch:{ IllegalArgumentException -> 0x0174 }
            boolean r8 = r8.equals(r9)     // Catch:{ IllegalArgumentException -> 0x0174 }
            if (r8 != 0) goto L_0x015d
            java.lang.String r8 = "Oauth:processTokenResponse"
            java.lang.String r9 = "CorrelationId is not matching"
            java.lang.String r1 = ""
            com.microsoft.aad.adal.ADALError r3 = com.microsoft.aad.adal.ADALError.CORRELATION_ID_NOT_MATCHING_REQUEST_RESPONSE     // Catch:{ IllegalArgumentException -> 0x0174 }
            com.microsoft.aad.adal.Logger.m239w(r8, r9, r1, r3)     // Catch:{ IllegalArgumentException -> 0x0174 }
        L_0x015d:
            java.lang.String r8 = "Oauth:processTokenResponse"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IllegalArgumentException -> 0x0174 }
            r9.<init>()     // Catch:{ IllegalArgumentException -> 0x0174 }
            java.lang.String r1 = "Response correlationId:"
            r9.append(r1)     // Catch:{ IllegalArgumentException -> 0x0174 }
            r9.append(r0)     // Catch:{ IllegalArgumentException -> 0x0174 }
            java.lang.String r9 = r9.toString()     // Catch:{ IllegalArgumentException -> 0x0174 }
            com.microsoft.aad.adal.Logger.m236v(r8, r9)     // Catch:{ IllegalArgumentException -> 0x0174 }
            goto L_0x018f
        L_0x0174:
            r8 = move-exception
            java.lang.String r9 = "Oauth:processTokenResponse"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Wrong format of the correlation ID:"
            r1.append(r3)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            java.lang.String r1 = ""
            com.microsoft.aad.adal.ADALError r3 = com.microsoft.aad.adal.ADALError.CORRELATION_ID_FORMAT
            com.microsoft.aad.adal.Logger.m232e(r9, r0, r1, r3, r8)
        L_0x018f:
            return r2
        L_0x0190:
            r9 = move-exception
            com.microsoft.aad.adal.AuthenticationException r0 = new com.microsoft.aad.adal.AuthenticationException
            com.microsoft.aad.adal.ADALError r1 = com.microsoft.aad.adal.ADALError.SERVER_INVALID_JSON_RESPONSE
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Can't parse server response. "
            r2.append(r3)
            java.lang.String r3 = r8.getBody()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r1, r2, r8, r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.aad.adal.Oauth2.processTokenResponse(com.microsoft.aad.adal.HttpWebResponse, com.microsoft.aad.adal.HttpEvent):com.microsoft.aad.adal.AuthenticationResult");
    }

    private AuthenticationResult parseJsonResponse(String str) throws JSONException, AuthenticationException {
        HashMap hashMap = new HashMap();
        extractJsonObjects(hashMap, str);
        return processUIResponseParams(hashMap);
    }

    private HttpEvent startHttpEvent() {
        HttpEvent httpEvent = new HttpEvent("Microsoft.ADAL.http_event");
        httpEvent.setRequestId(this.mRequest.getTelemetryRequestId());
        httpEvent.setMethod("Microsoft.ADAL.post");
        Telemetry.getInstance().startEvent(this.mRequest.getTelemetryRequestId(), "Microsoft.ADAL.http_event");
        return httpEvent;
    }

    private void stopHttpEvent(HttpEvent httpEvent) {
        Telemetry.getInstance().stopEvent(this.mRequest.getTelemetryRequestId(), httpEvent, "Microsoft.ADAL.http_event");
    }

    private void setTokenEndpoint(String str) {
        this.mTokenEndpoint = str;
    }
}
