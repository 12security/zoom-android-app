package com.box.androidsdk.content.requests;

import android.text.TextUtils;
import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.BoxException.RateLimitAttemptsExceeded;
import com.box.androidsdk.content.BoxException.RefreshFailure;
import com.box.androidsdk.content.BoxFutureTask;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.listeners.ProgressListener;
import com.box.androidsdk.content.models.BoxArray;
import com.box.androidsdk.content.models.BoxJsonObject;
import com.box.androidsdk.content.models.BoxObject;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxSharedLinkSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.utils.BoxLogUtils;
import com.box.androidsdk.content.utils.SdkUtils;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.microsoft.aad.adal.WebRequestHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

public abstract class BoxRequest<T extends BoxObject, R extends BoxRequest<T, R>> {
    public static final String JSON_OBJECT = "json_object";
    protected LinkedHashMap<String, Object> mBodyMap = new LinkedHashMap<>();
    Class<T> mClazz;
    protected ContentTypes mContentType = ContentTypes.JSON;
    protected LinkedHashMap<String, String> mHeaderMap = new LinkedHashMap<>();
    private String mIfMatchEtag;
    private String mIfNoneMatchEtag;
    protected ProgressListener mListener;
    protected HashMap<String, String> mQueryMap = new HashMap<>();
    BoxRequestHandler mRequestHandler;
    protected Methods mRequestMethod;
    protected String mRequestUrlString;
    protected BoxSession mSession;
    private String mStringBody;
    private int mTimeout;

    public static class BoxRequestHandler<R extends BoxRequest> {
        protected static final int DEFAULT_NUM_RETRIES = 1;
        protected static final int DEFAULT_RATE_LIMIT_WAIT = 20;
        public static final String OAUTH_ERROR_HEADER = "error";
        public static final String OAUTH_INVALID_TOKEN = "invalid_token";
        public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
        protected int mNumRateLimitRetries = 0;
        protected R mRequest;

        public BoxRequestHandler(R r) {
            this.mRequest = r;
        }

        public boolean isResponseSuccess(BoxHttpResponse boxHttpResponse) {
            int responseCode = boxHttpResponse.getResponseCode();
            return (responseCode >= 200 && responseCode < 300) || responseCode == 429;
        }

        public <T extends BoxObject> T onResponse(Class<T> cls, BoxHttpResponse boxHttpResponse) throws IllegalAccessException, InstantiationException, BoxException {
            if (boxHttpResponse.getResponseCode() == 429) {
                return retryRateLimited(boxHttpResponse);
            }
            String contentType = boxHttpResponse.getContentType();
            T t = (BoxObject) cls.newInstance();
            if ((t instanceof BoxJsonObject) && contentType.contains(ContentTypes.JSON.toString())) {
                ((BoxJsonObject) t).createFromJson(boxHttpResponse.getStringBody());
            }
            return t;
        }

        /* access modifiers changed from: protected */
        public <T extends BoxObject> T retryRateLimited(BoxHttpResponse boxHttpResponse) throws BoxException {
            int i = this.mNumRateLimitRetries;
            if (i < 1) {
                this.mNumRateLimitRetries = i + 1;
                try {
                    Thread.sleep((long) getRetryAfterFromResponse(boxHttpResponse, ((int) (new SecureRandom().nextDouble() * 10.0d)) + 20));
                    return this.mRequest.send();
                } catch (InterruptedException e) {
                    throw new BoxException(e.getMessage(), (Throwable) e);
                }
            } else {
                throw new RateLimitAttemptsExceeded("Max attempts exceeded", i, boxHttpResponse);
            }
        }

        public boolean onException(BoxRequest boxRequest, BoxHttpResponse boxHttpResponse, BoxException boxException) throws RefreshFailure {
            BoxSession session = boxRequest.getSession();
            boolean z = true;
            if (oauthExpired(boxHttpResponse)) {
                try {
                    BoxResponse boxResponse = (BoxResponse) session.refresh().get();
                    if (boxResponse.isSuccess()) {
                        return true;
                    }
                    if (boxResponse.getException() == null || !(boxResponse.getException() instanceof RefreshFailure)) {
                        return false;
                    }
                    throw ((RefreshFailure) boxResponse.getException());
                } catch (InterruptedException e) {
                    BoxLogUtils.m12e("oauthRefresh", "Interrupted Exception", e);
                } catch (ExecutionException e2) {
                    BoxLogUtils.m12e("oauthRefresh", "Interrupted Exception", e2);
                }
            } else if (authFailed(boxHttpResponse)) {
                session.getAuthInfo().setUser(null);
                try {
                    session.authenticate().get();
                    if (session.getUser() == null) {
                        z = false;
                    }
                    return z;
                } catch (Exception unused) {
                }
            }
            return false;
        }

        protected static int getRetryAfterFromResponse(BoxHttpResponse boxHttpResponse, int i) {
            String headerField = boxHttpResponse.getHttpURLConnection().getHeaderField("Retry-After");
            if (!SdkUtils.isBlank(headerField)) {
                try {
                    i = Integer.parseInt(headerField);
                } catch (NumberFormatException unused) {
                }
                if (i <= 0) {
                    i = 1;
                }
            }
            return i * 1000;
        }

        private boolean authFailed(BoxHttpResponse boxHttpResponse) {
            return boxHttpResponse != null && boxHttpResponse.getResponseCode() == 401;
        }

        private boolean oauthExpired(BoxHttpResponse boxHttpResponse) {
            if (boxHttpResponse == null || 401 != boxHttpResponse.getResponseCode()) {
                return false;
            }
            String headerField = boxHttpResponse.mConnection.getHeaderField("WWW-Authenticate");
            if (!SdkUtils.isEmptyString(headerField)) {
                for (String isInvalidTokenError : headerField.split(PreferencesConstants.COOKIE_DELIMITER)) {
                    if (isInvalidTokenError(isInvalidTokenError)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isInvalidTokenError(String str) {
            String[] split = str.split("=");
            return split.length == 2 && split[0] != null && split[1] != null && "error".equalsIgnoreCase(split[0].trim()) && OAUTH_INVALID_TOKEN.equalsIgnoreCase(split[1].replace("\"", "").trim());
        }
    }

    public enum ContentTypes {
        JSON(WebRequestHandler.HEADER_ACCEPT_JSON),
        URL_ENCODED("application/x-www-form-urlencoded"),
        JSON_PATCH("application/json-patch+json");
        
        private String mName;

        private ContentTypes(String str) {
            this.mName = str;
        }

        public String toString() {
            return this.mName;
        }
    }

    public enum Methods {
        GET,
        POST,
        PUT,
        DELETE,
        OPTIONS
    }

    public BoxRequest(Class<T> cls, String str, BoxSession boxSession) {
        this.mClazz = cls;
        this.mRequestUrlString = str;
        this.mSession = boxSession;
        setRequestHandler(new BoxRequestHandler(this));
    }

    protected BoxRequest(BoxRequest boxRequest) {
        this.mSession = boxRequest.getSession();
        this.mClazz = boxRequest.mClazz;
        this.mRequestHandler = boxRequest.getRequestHandler();
        this.mRequestMethod = boxRequest.mRequestMethod;
        this.mContentType = boxRequest.mContentType;
        this.mIfMatchEtag = boxRequest.getIfMatchEtag();
        this.mListener = boxRequest.mListener;
        this.mRequestUrlString = boxRequest.mRequestUrlString;
        this.mIfNoneMatchEtag = boxRequest.getIfNoneMatchEtag();
        this.mTimeout = boxRequest.mTimeout;
        this.mStringBody = boxRequest.mStringBody;
        importRequestContentMapsFrom(boxRequest);
    }

    /* access modifiers changed from: protected */
    public void importRequestContentMapsFrom(BoxRequest boxRequest) {
        this.mQueryMap = new HashMap<>(boxRequest.mQueryMap);
        this.mBodyMap = new LinkedHashMap<>(boxRequest.mBodyMap);
    }

    public BoxSession getSession() {
        return this.mSession;
    }

    public BoxRequestHandler getRequestHandler() {
        return this.mRequestHandler;
    }

    public R setRequestHandler(BoxRequestHandler boxRequestHandler) {
        this.mRequestHandler = boxRequestHandler;
        return this;
    }

    public R setContentType(ContentTypes contentTypes) {
        this.mContentType = contentTypes;
        return this;
    }

    public R setTimeOut(int i) {
        this.mTimeout = i;
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x009b  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:48:0x0080=Splitter:B:48:0x0080, B:41:0x0072=Splitter:B:41:0x0072, B:34:0x0064=Splitter:B:34:0x0064, B:55:0x008e=Splitter:B:55:0x008e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public T send() throws com.box.androidsdk.content.BoxException {
        /*
            r6 = this;
            com.box.androidsdk.content.requests.BoxRequest$BoxRequestHandler r0 = r6.getRequestHandler()
            r1 = 0
            com.box.androidsdk.content.requests.BoxHttpRequest r2 = r6.createHttpRequest()     // Catch:{ IOException -> 0x008a, InstantiationException -> 0x007c, IllegalAccessException -> 0x006e, BoxException -> 0x0060, all -> 0x005d }
            java.net.HttpURLConnection r2 = r2.getUrlConnection()     // Catch:{ IOException -> 0x008a, InstantiationException -> 0x007c, IllegalAccessException -> 0x006e, BoxException -> 0x0060, all -> 0x005d }
            int r3 = r6.mTimeout     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
            if (r3 <= 0) goto L_0x001b
            int r3 = r6.mTimeout     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
            r2.setConnectTimeout(r3)     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
            int r3 = r6.mTimeout     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
            r2.setReadTimeout(r3)     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
        L_0x001b:
            com.box.androidsdk.content.requests.BoxHttpResponse r3 = new com.box.androidsdk.content.requests.BoxHttpResponse     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
            r3.<init>(r2)     // Catch:{ IOException -> 0x0058, InstantiationException -> 0x0053, IllegalAccessException -> 0x004e, BoxException -> 0x0049 }
            r3.open()     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            r6.logDebug(r3)     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            boolean r1 = r0.isResponseSuccess(r3)     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            if (r1 == 0) goto L_0x0038
            java.lang.Class<T> r1 = r6.mClazz     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            com.box.androidsdk.content.models.BoxObject r0 = r0.onResponse(r1, r3)     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            if (r2 == 0) goto L_0x0037
            r2.disconnect()
        L_0x0037:
            return r0
        L_0x0038:
            com.box.androidsdk.content.BoxException r1 = new com.box.androidsdk.content.BoxException     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            java.lang.String r4 = "An error occurred while sending the request"
            r1.<init>(r4, r3)     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
            throw r1     // Catch:{ IOException -> 0x0046, InstantiationException -> 0x0044, IllegalAccessException -> 0x0042, BoxException -> 0x0040 }
        L_0x0040:
            r1 = move-exception
            goto L_0x0064
        L_0x0042:
            r1 = move-exception
            goto L_0x0072
        L_0x0044:
            r1 = move-exception
            goto L_0x0080
        L_0x0046:
            r1 = move-exception
            goto L_0x008e
        L_0x0049:
            r3 = move-exception
            r5 = r3
            r3 = r1
            r1 = r5
            goto L_0x0064
        L_0x004e:
            r3 = move-exception
            r5 = r3
            r3 = r1
            r1 = r5
            goto L_0x0072
        L_0x0053:
            r3 = move-exception
            r5 = r3
            r3 = r1
            r1 = r5
            goto L_0x0080
        L_0x0058:
            r3 = move-exception
            r5 = r3
            r3 = r1
            r1 = r5
            goto L_0x008e
        L_0x005d:
            r0 = move-exception
            r2 = r1
            goto L_0x0099
        L_0x0060:
            r3 = move-exception
            r2 = r1
            r1 = r3
            r3 = r2
        L_0x0064:
            com.box.androidsdk.content.models.BoxObject r0 = r6.handleSendException(r0, r3, r1)     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x006d
            r2.disconnect()
        L_0x006d:
            return r0
        L_0x006e:
            r3 = move-exception
            r2 = r1
            r1 = r3
            r3 = r2
        L_0x0072:
            com.box.androidsdk.content.models.BoxObject r0 = r6.handleSendException(r0, r3, r1)     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x007b
            r2.disconnect()
        L_0x007b:
            return r0
        L_0x007c:
            r3 = move-exception
            r2 = r1
            r1 = r3
            r3 = r2
        L_0x0080:
            com.box.androidsdk.content.models.BoxObject r0 = r6.handleSendException(r0, r3, r1)     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0089
            r2.disconnect()
        L_0x0089:
            return r0
        L_0x008a:
            r3 = move-exception
            r2 = r1
            r1 = r3
            r3 = r2
        L_0x008e:
            com.box.androidsdk.content.models.BoxObject r0 = r6.handleSendException(r0, r3, r1)     // Catch:{ all -> 0x0098 }
            if (r2 == 0) goto L_0x0097
            r2.disconnect()
        L_0x0097:
            return r0
        L_0x0098:
            r0 = move-exception
        L_0x0099:
            if (r2 == 0) goto L_0x009e
            r2.disconnect()
        L_0x009e:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.requests.BoxRequest.send():com.box.androidsdk.content.models.BoxObject");
    }

    private T handleSendException(BoxRequestHandler boxRequestHandler, BoxHttpResponse boxHttpResponse, Exception exc) throws BoxException {
        if (exc instanceof BoxException) {
            BoxException boxException = (BoxException) exc;
            if (boxRequestHandler.onException(this, boxHttpResponse, boxException)) {
                return send();
            }
            throw boxException;
        }
        BoxException boxException2 = new BoxException("Couldn't connect to the Box API due to a network error.", (Throwable) exc);
        boxRequestHandler.onException(this, boxHttpResponse, boxException2);
        throw boxException2;
    }

    public BoxFutureTask<T> toTask() {
        return new BoxFutureTask<>(this.mClazz, this);
    }

    /* access modifiers changed from: protected */
    public BoxHttpRequest createHttpRequest() throws IOException, BoxException {
        BoxHttpRequest boxHttpRequest = new BoxHttpRequest(buildUrl(), this.mRequestMethod, this.mListener);
        setHeaders(boxHttpRequest);
        setBody(boxHttpRequest);
        return boxHttpRequest;
    }

    /* access modifiers changed from: protected */
    public URL buildUrl() throws MalformedURLException, UnsupportedEncodingException {
        String createQuery = createQuery(this.mQueryMap);
        if (TextUtils.isEmpty(createQuery)) {
            return new URL(this.mRequestUrlString);
        }
        return new URL(String.format(Locale.ENGLISH, "%s?%s", new Object[]{this.mRequestUrlString, createQuery}));
    }

    /* access modifiers changed from: protected */
    public String createQuery(Map<String, String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        String str = "%s=%s";
        boolean z = true;
        for (Entry entry : map.entrySet()) {
            sb.append(String.format(Locale.ENGLISH, str, new Object[]{URLEncoder.encode((String) entry.getKey(), "UTF-8"), URLEncoder.encode((String) entry.getValue(), "UTF-8")}));
            if (z) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("&");
                sb2.append(str);
                str = sb2.toString();
                z = false;
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void setHeaders(BoxHttpRequest boxHttpRequest) {
        String str;
        this.mHeaderMap.clear();
        BoxAuthenticationInfo authInfo = this.mSession.getAuthInfo();
        if (authInfo == null) {
            str = null;
        } else {
            str = authInfo.accessToken();
        }
        if (!SdkUtils.isEmptyString(str)) {
            this.mHeaderMap.put("Authorization", String.format(Locale.ENGLISH, "Bearer %s", new Object[]{str}));
        }
        this.mHeaderMap.put("User-Agent", this.mSession.getUserAgent());
        this.mHeaderMap.put("Accept-Encoding", "gzip");
        this.mHeaderMap.put("Accept-Charset", "utf-8");
        this.mHeaderMap.put("Content-Type", this.mContentType.toString());
        String str2 = this.mIfMatchEtag;
        if (str2 != null) {
            this.mHeaderMap.put("If-Match", str2);
        }
        String str3 = this.mIfNoneMatchEtag;
        if (str3 != null) {
            this.mHeaderMap.put("If-None-Match", str3);
        }
        BoxSession boxSession = this.mSession;
        if (boxSession instanceof BoxSharedLinkSession) {
            BoxSharedLinkSession boxSharedLinkSession = (BoxSharedLinkSession) boxSession;
            String format = String.format(Locale.ENGLISH, "shared_link=%s", new Object[]{boxSharedLinkSession.getSharedLink()});
            if (boxSharedLinkSession.getPassword() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(format);
                sb.append(String.format(Locale.ENGLISH, "&shared_link_password=%s", new Object[]{boxSharedLinkSession.getPassword()}));
                format = sb.toString();
            }
            this.mHeaderMap.put("BoxApi", format);
        }
        for (Entry entry : this.mHeaderMap.entrySet()) {
            boxHttpRequest.addHeader((String) entry.getKey(), (String) entry.getValue());
        }
    }

    /* access modifiers changed from: protected */
    public R setIfMatchEtag(String str) {
        this.mIfMatchEtag = str;
        return this;
    }

    /* access modifiers changed from: protected */
    public String getIfMatchEtag() {
        return this.mIfMatchEtag;
    }

    /* access modifiers changed from: protected */
    public R setIfNoneMatchEtag(String str) {
        this.mIfNoneMatchEtag = str;
        return this;
    }

    /* access modifiers changed from: protected */
    public String getIfNoneMatchEtag() {
        return this.mIfNoneMatchEtag;
    }

    /* access modifiers changed from: protected */
    public void setBody(BoxHttpRequest boxHttpRequest) throws IOException {
        if (!this.mBodyMap.isEmpty()) {
            boxHttpRequest.setBody(new ByteArrayInputStream(getStringBody().getBytes("UTF-8")));
        }
    }

    public String getStringBody() throws UnsupportedEncodingException {
        String str = this.mStringBody;
        if (str != null) {
            return str;
        }
        switch (this.mContentType) {
            case JSON:
                JsonObject jsonObject = new JsonObject();
                for (Entry parseHashMapEntry : this.mBodyMap.entrySet()) {
                    parseHashMapEntry(jsonObject, parseHashMapEntry);
                }
                this.mStringBody = jsonObject.toString();
                break;
            case URL_ENCODED:
                HashMap hashMap = new HashMap();
                for (Entry entry : this.mBodyMap.entrySet()) {
                    hashMap.put(entry.getKey(), (String) entry.getValue());
                }
                this.mStringBody = createQuery(hashMap);
                break;
            case JSON_PATCH:
                this.mStringBody = ((BoxArray) this.mBodyMap.get(JSON_OBJECT)).toJson();
                break;
        }
        return this.mStringBody;
    }

    /* access modifiers changed from: protected */
    public void parseHashMapEntry(JsonObject jsonObject, Entry<String, Object> entry) {
        Object value = entry.getValue();
        if (value instanceof BoxJsonObject) {
            jsonObject.add((String) entry.getKey(), parseJsonObject(value));
        } else if (value instanceof Double) {
            jsonObject.add((String) entry.getKey(), Double.toString(((Double) value).doubleValue()));
        } else if ((value instanceof Enum) || (value instanceof Boolean)) {
            jsonObject.add((String) entry.getKey(), value.toString());
        } else if (value instanceof JsonArray) {
            jsonObject.add((String) entry.getKey(), (JsonValue) (JsonArray) value);
        } else {
            jsonObject.add((String) entry.getKey(), (String) entry.getValue());
        }
    }

    /* access modifiers changed from: protected */
    public JsonValue parseJsonObject(Object obj) {
        return JsonValue.readFrom(((BoxJsonObject) obj).toJson());
    }

    /* access modifiers changed from: protected */
    public void logDebug(BoxHttpResponse boxHttpResponse) throws BoxException {
        logRequest();
        BoxLogUtils.m13i(BoxConstants.TAG, String.format(Locale.ENGLISH, "Response (%s):  %s", new Object[]{Integer.valueOf(boxHttpResponse.getResponseCode()), boxHttpResponse.getStringBody()}));
    }

    /* access modifiers changed from: protected */
    public void logRequest() {
        String str;
        String str2;
        try {
            str = createQuery(this.mQueryMap);
        } catch (UnsupportedEncodingException unused) {
            str = null;
        }
        if (!SdkUtils.isBlank(str)) {
            str2 = String.format(Locale.ENGLISH, "%s?%s", new Object[]{this.mRequestUrlString, str});
        } else {
            str2 = this.mRequestUrlString;
        }
        BoxLogUtils.m13i(BoxConstants.TAG, String.format(Locale.ENGLISH, "Request (%s):  %s", new Object[]{this.mRequestMethod, str2}));
        BoxLogUtils.m14i(BoxConstants.TAG, "Request Header", this.mHeaderMap);
        switch (this.mContentType) {
            case JSON:
            case JSON_PATCH:
                if (!SdkUtils.isBlank(this.mStringBody)) {
                    BoxLogUtils.m13i(BoxConstants.TAG, String.format(Locale.ENGLISH, "Request JSON:  %s", new Object[]{this.mStringBody}));
                    return;
                }
                return;
            case URL_ENCODED:
                HashMap hashMap = new HashMap();
                for (Entry entry : this.mBodyMap.entrySet()) {
                    hashMap.put(entry.getKey(), (String) entry.getValue());
                }
                BoxLogUtils.m14i(BoxConstants.TAG, "Request Form Data", hashMap);
                return;
            default:
                return;
        }
    }
}
