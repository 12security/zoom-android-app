package com.dropbox.core;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.p005v2.callbacks.DbxGlobalCallbackFactory;
import com.dropbox.core.p005v2.common.PathRoot;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.LangUtil;
import com.dropbox.core.util.StringUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public final class DbxRequestUtil {
    private static final Random RAND = new Random();
    public static DbxGlobalCallbackFactory sharedCallbackFactory;

    public static abstract class RequestMaker<T, E extends Throwable> {
        public abstract T run() throws DbxException, Throwable;
    }

    public static abstract class ResponseHandler<T> {
        public abstract T handle(Response response) throws DbxException;
    }

    public static String encodeUrlParam(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw LangUtil.mkAssert("UTF-8 should always be supported", e);
        }
    }

    public static String buildUrlWithParams(String str, String str2, String str3, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildUri(str2, str3));
        sb.append("?");
        sb.append(encodeUrlParams(str, strArr));
        return sb.toString();
    }

    static String[] toParamsArray(Map<String, String> map) {
        String[] strArr = new String[(map.size() * 2)];
        int i = 0;
        for (Entry entry : map.entrySet()) {
            strArr[i] = (String) entry.getKey();
            strArr[i + 1] = (String) entry.getValue();
            i += 2;
        }
        return strArr;
    }

    public static String buildUri(String str, String str2) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("/");
            sb.append(str2);
            return new URI("https", str, sb.toString(), null).toASCIIString();
        } catch (URISyntaxException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("URI creation failed, host=");
            sb2.append(StringUtil.m33jq(str));
            sb2.append(", path=");
            sb2.append(StringUtil.m33jq(str2));
            throw LangUtil.mkAssert(sb2.toString(), e);
        }
    }

    private static String encodeUrlParams(String str, String[] strArr) {
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (str != null) {
            sb.append("locale=");
            sb.append(encodeUrlParam(str));
            str2 = "&";
        }
        if (strArr != null) {
            if (strArr.length % 2 == 0) {
                int i = 0;
                while (i < strArr.length) {
                    String str3 = strArr[i];
                    String str4 = strArr[i + 1];
                    if (str3 != null) {
                        if (str4 != null) {
                            sb.append(str2);
                            str2 = "&";
                            sb.append(encodeUrlParam(str3));
                            sb.append("=");
                            sb.append(encodeUrlParam(str4));
                        }
                        i += 2;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("params[");
                        sb2.append(i);
                        sb2.append("] is null");
                        throw new IllegalArgumentException(sb2.toString());
                    }
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("'params.length' is ");
                sb3.append(strArr.length);
                sb3.append("; expecting a multiple of two");
                throw new IllegalArgumentException(sb3.toString());
            }
        }
        return sb.toString();
    }

    public static List<Header> addAuthHeader(List<Header> list, String str) {
        if (str != null) {
            if (list == null) {
                list = new ArrayList<>();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Bearer ");
            sb.append(str);
            list.add(new Header("Authorization", sb.toString()));
            return list;
        }
        throw new NullPointerException("accessToken");
    }

    public static List<Header> addSelectUserHeader(List<Header> list, String str) {
        if (str != null) {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(new Header("Dropbox-API-Select-User", str));
            return list;
        }
        throw new NullPointerException("memberId");
    }

    public static List<Header> addSelectAdminHeader(List<Header> list, String str) {
        if (str != null) {
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(new Header("Dropbox-API-Select-Admin", str));
            return list;
        }
        throw new NullPointerException("adminId");
    }

    public static List<Header> addBasicAuthHeader(List<Header> list, String str, String str2) {
        if (str == null) {
            throw new NullPointerException(OAuth.USER_NAME);
        } else if (str2 != null) {
            if (list == null) {
                list = new ArrayList<>();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(":");
            sb.append(str2);
            String base64Encode = StringUtil.base64Encode(StringUtil.stringToUtf8(sb.toString()));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Basic ");
            sb2.append(base64Encode);
            list.add(new Header("Authorization", sb2.toString()));
            return list;
        } else {
            throw new NullPointerException(BoxSharedLink.FIELD_PASSWORD);
        }
    }

    public static List<Header> addUserAgentHeader(List<Header> list, DbxRequestConfig dbxRequestConfig, String str) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(buildUserAgentHeader(dbxRequestConfig, str));
        return list;
    }

    public static List<Header> addUserLocaleHeader(List<Header> list, DbxRequestConfig dbxRequestConfig) {
        if (dbxRequestConfig.getUserLocale() == null) {
            return list;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new Header("Dropbox-API-User-Locale", dbxRequestConfig.getUserLocale()));
        return list;
    }

    public static Header buildUserAgentHeader(DbxRequestConfig dbxRequestConfig, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(dbxRequestConfig.getClientIdentifier());
        sb.append(OAuth.SCOPE_DELIMITER);
        sb.append(str);
        sb.append("/");
        sb.append(DbxSdkVersion.Version);
        return new Header("User-Agent", sb.toString());
    }

    public static List<Header> addPathRootHeader(List<Header> list, PathRoot pathRoot) {
        if (pathRoot == null) {
            return list;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new Header("Dropbox-API-Path-Root", pathRoot.toString()));
        return list;
    }

    public static Response startGet(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, String str4, String[] strArr, List<Header> list) throws NetworkIOException {
        List addAuthHeader = addAuthHeader(addUserAgentHeader(copyHeaders(list), dbxRequestConfig, str2), str);
        try {
            return dbxRequestConfig.getHttpRequestor().doGet(buildUrlWithParams(dbxRequestConfig.getUserLocale(), str3, str4, strArr), addAuthHeader);
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
    }

    public static Uploader startPut(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, String str4, String[] strArr, List<Header> list) throws NetworkIOException {
        List addAuthHeader = addAuthHeader(addUserAgentHeader(copyHeaders(list), dbxRequestConfig, str2), str);
        try {
            return dbxRequestConfig.getHttpRequestor().startPut(buildUrlWithParams(dbxRequestConfig.getUserLocale(), str3, str4, strArr), addAuthHeader);
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
    }

    public static Response startPostNoAuth(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, String[] strArr, List<Header> list) throws NetworkIOException {
        byte[] stringToUtf8 = StringUtil.stringToUtf8(encodeUrlParams(dbxRequestConfig.getUserLocale(), strArr));
        List copyHeaders = copyHeaders(list);
        copyHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
        return startPostRaw(dbxRequestConfig, str, str2, str3, stringToUtf8, copyHeaders);
    }

    public static Response startPostRaw(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, byte[] bArr, List<Header> list) throws NetworkIOException {
        Uploader startPost;
        String buildUri = buildUri(str2, str3);
        List addUserAgentHeader = addUserAgentHeader(copyHeaders(list), dbxRequestConfig, str);
        addUserAgentHeader.add(new Header("Content-Length", Integer.toString(bArr.length)));
        try {
            startPost = dbxRequestConfig.getHttpRequestor().startPost(buildUri, addUserAgentHeader);
            startPost.upload(bArr);
            Response finish = startPost.finish();
            startPost.close();
            return finish;
        } catch (IOException e) {
            throw new NetworkIOException(e);
        } catch (Throwable th) {
            startPost.close();
            throw th;
        }
    }

    private static List<Header> copyHeaders(List<Header> list) {
        if (list == null) {
            return new ArrayList();
        }
        return new ArrayList(list);
    }

    public static byte[] loadErrorBody(Response response) throws NetworkIOException {
        if (response.getBody() == null) {
            return new byte[0];
        }
        try {
            return IOUtil.slurp(response.getBody(), 4096);
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
    }

    public static String parseErrorBody(String str, int i, byte[] bArr) throws BadResponseException {
        try {
            return StringUtil.utf8ToString(bArr);
        } catch (CharacterCodingException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Got non-UTF8 response body: ");
            sb.append(i);
            sb.append(": ");
            sb.append(e.getMessage());
            throw new BadResponseException(str, sb.toString());
        }
    }

    public static DbxException unexpectedStatus(Response response) throws NetworkIOException, BadResponseException {
        return unexpectedStatus(response, null);
    }

    /* JADX WARNING: type inference failed for: r0v1, types: [com.dropbox.core.DbxException] */
    /* JADX WARNING: type inference failed for: r0v6, types: [com.dropbox.core.AccessErrorException] */
    /* JADX WARNING: type inference failed for: r0v12, types: [com.dropbox.core.PathRootErrorException] */
    /* JADX WARNING: type inference failed for: r0v15, types: [com.dropbox.core.BadResponseException] */
    /* JADX WARNING: type inference failed for: r0v17 */
    /* JADX WARNING: type inference failed for: r0v18, types: [com.dropbox.core.ServerException] */
    /* JADX WARNING: type inference failed for: r0v20, types: [com.dropbox.core.RetryException] */
    /* JADX WARNING: type inference failed for: r0v21, types: [com.dropbox.core.BadResponseException] */
    /* JADX WARNING: type inference failed for: r0v25 */
    /* JADX WARNING: type inference failed for: r0v26, types: [com.dropbox.core.BadRequestException] */
    /* JADX WARNING: type inference failed for: r0v27, types: [com.dropbox.core.InvalidAccessTokenException] */
    /* JADX WARNING: type inference failed for: r0v28, types: [com.dropbox.core.BadResponseCodeException] */
    /* JADX WARNING: type inference failed for: r0v29 */
    /* JADX WARNING: type inference failed for: r0v30 */
    /* JADX WARNING: type inference failed for: r0v31 */
    /* JADX WARNING: type inference failed for: r0v32 */
    /* JADX WARNING: type inference failed for: r0v33 */
    /* JADX WARNING: type inference failed for: r0v34 */
    /* JADX WARNING: type inference failed for: r0v35 */
    /* JADX WARNING: type inference failed for: r0v36 */
    /* JADX WARNING: type inference failed for: r0v37 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.dropbox.core.DbxException unexpectedStatus(com.dropbox.core.http.HttpRequestor.Response r8, java.lang.String r9) throws com.dropbox.core.NetworkIOException, com.dropbox.core.BadResponseException {
        /*
            java.lang.String r6 = getRequestId(r8)
            int r0 = r8.getStatusCode()
            r1 = 403(0x193, float:5.65E-43)
            r2 = 0
            if (r0 == r1) goto L_0x0107
            r1 = 422(0x1a6, float:5.91E-43)
            if (r0 == r1) goto L_0x00b9
            r1 = 429(0x1ad, float:6.01E-43)
            if (r0 == r1) goto L_0x0099
            r1 = 500(0x1f4, float:7.0E-43)
            if (r0 == r1) goto L_0x0092
            r1 = 503(0x1f7, float:7.05E-43)
            if (r0 == r1) goto L_0x005e
            switch(r0) {
                case 400: goto L_0x0053;
                case 401: goto L_0x0048;
                default: goto L_0x0020;
            }
        L_0x0020:
            com.dropbox.core.BadResponseCodeException r0 = new com.dropbox.core.BadResponseCodeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "unexpected HTTP status code: "
            r1.append(r3)
            int r3 = r8.getStatusCode()
            r1.append(r3)
            java.lang.String r3 = ": "
            r1.append(r3)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            int r8 = r8.getStatusCode()
            r0.<init>(r6, r1, r8)
            goto L_0x0131
        L_0x0048:
            java.lang.String r8 = messageFromResponse(r8, r6)
            com.dropbox.core.InvalidAccessTokenException r0 = new com.dropbox.core.InvalidAccessTokenException
            r0.<init>(r6, r8)
            goto L_0x0131
        L_0x0053:
            java.lang.String r8 = messageFromResponse(r8, r6)
            com.dropbox.core.BadRequestException r0 = new com.dropbox.core.BadRequestException
            r0.<init>(r6, r8)
            goto L_0x0131
        L_0x005e:
            java.lang.String r0 = "Retry-After"
            java.lang.String r8 = getFirstHeaderMaybe(r8, r0)
            if (r8 == 0) goto L_0x0082
            java.lang.String r0 = r8.trim()     // Catch:{ NumberFormatException -> 0x0089 }
            boolean r0 = r0.isEmpty()     // Catch:{ NumberFormatException -> 0x0089 }
            if (r0 != 0) goto L_0x0082
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ NumberFormatException -> 0x0089 }
            com.dropbox.core.RetryException r7 = new com.dropbox.core.RetryException     // Catch:{ NumberFormatException -> 0x0089 }
            long r3 = (long) r8     // Catch:{ NumberFormatException -> 0x0089 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ NumberFormatException -> 0x0089 }
            r2 = 0
            r0 = r7
            r1 = r6
            r0.<init>(r1, r2, r3, r5)     // Catch:{ NumberFormatException -> 0x0089 }
            r0 = r7
            goto L_0x0131
        L_0x0082:
            com.dropbox.core.RetryException r0 = new com.dropbox.core.RetryException     // Catch:{ NumberFormatException -> 0x0089 }
            r0.<init>(r6, r2)     // Catch:{ NumberFormatException -> 0x0089 }
            goto L_0x0131
        L_0x0089:
            com.dropbox.core.BadResponseException r0 = new com.dropbox.core.BadResponseException
            java.lang.String r8 = "Invalid value for HTTP header: \"Retry-After\""
            r0.<init>(r6, r8)
            goto L_0x0131
        L_0x0092:
            com.dropbox.core.ServerException r0 = new com.dropbox.core.ServerException
            r0.<init>(r6, r2)
            goto L_0x0131
        L_0x0099:
            java.lang.String r0 = "Retry-After"
            java.lang.String r8 = getFirstHeader(r8, r0)     // Catch:{ NumberFormatException -> 0x00b1 }
            int r8 = java.lang.Integer.parseInt(r8)     // Catch:{ NumberFormatException -> 0x00b1 }
            com.dropbox.core.RateLimitException r7 = new com.dropbox.core.RateLimitException     // Catch:{ NumberFormatException -> 0x00b1 }
            long r3 = (long) r8     // Catch:{ NumberFormatException -> 0x00b1 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ NumberFormatException -> 0x00b1 }
            r2 = 0
            r0 = r7
            r1 = r6
            r0.<init>(r1, r2, r3, r5)     // Catch:{ NumberFormatException -> 0x00b1 }
            r0 = r7
            goto L_0x0131
        L_0x00b1:
            com.dropbox.core.BadResponseException r0 = new com.dropbox.core.BadResponseException
            java.lang.String r8 = "Invalid value for HTTP header: \"Retry-After\""
            r0.<init>(r6, r8)
            goto L_0x0131
        L_0x00b9:
            com.dropbox.core.ApiErrorResponse$Serializer r0 = new com.dropbox.core.ApiErrorResponse$Serializer     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            com.dropbox.core.v2.common.PathRootError$Serializer r1 = com.dropbox.core.p005v2.common.PathRootError.Serializer.INSTANCE     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            r0.<init>(r1)     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            java.io.InputStream r8 = r8.getBody()     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            java.lang.Object r8 = r0.deserialize(r8)     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            com.dropbox.core.ApiErrorResponse r8 = (com.dropbox.core.ApiErrorResponse) r8     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            com.dropbox.core.LocalizedText r0 = r8.getUserMessage()     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            if (r0 == 0) goto L_0x00d8
            com.dropbox.core.LocalizedText r0 = r8.getUserMessage()     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            java.lang.String r2 = r0.toString()     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
        L_0x00d8:
            java.lang.Object r8 = r8.getError()     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            com.dropbox.core.v2.common.PathRootError r8 = (com.dropbox.core.p005v2.common.PathRootError) r8     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            com.dropbox.core.PathRootErrorException r0 = new com.dropbox.core.PathRootErrorException     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            r0.<init>(r6, r2, r8)     // Catch:{ JsonProcessingException -> 0x00eb, IOException -> 0x00e4 }
            goto L_0x0131
        L_0x00e4:
            r8 = move-exception
            com.dropbox.core.NetworkIOException r9 = new com.dropbox.core.NetworkIOException
            r9.<init>(r8)
            throw r9
        L_0x00eb:
            r8 = move-exception
            com.dropbox.core.BadResponseException r9 = new com.dropbox.core.BadResponseException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Bad JSON: "
            r0.append(r1)
            java.lang.String r1 = r8.getMessage()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9.<init>(r6, r0, r8)
            throw r9
        L_0x0107:
            com.dropbox.core.ApiErrorResponse$Serializer r0 = new com.dropbox.core.ApiErrorResponse$Serializer     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            com.dropbox.core.v2.auth.AccessError$Serializer r1 = com.dropbox.core.p005v2.auth.AccessError.Serializer.INSTANCE     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            r0.<init>(r1)     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            java.io.InputStream r8 = r8.getBody()     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            java.lang.Object r8 = r0.deserialize(r8)     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            com.dropbox.core.ApiErrorResponse r8 = (com.dropbox.core.ApiErrorResponse) r8     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            com.dropbox.core.LocalizedText r0 = r8.getUserMessage()     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            if (r0 == 0) goto L_0x0126
            com.dropbox.core.LocalizedText r0 = r8.getUserMessage()     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            java.lang.String r2 = r0.toString()     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
        L_0x0126:
            java.lang.Object r8 = r8.getError()     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            com.dropbox.core.v2.auth.AccessError r8 = (com.dropbox.core.p005v2.auth.AccessError) r8     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            com.dropbox.core.AccessErrorException r0 = new com.dropbox.core.AccessErrorException     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
            r0.<init>(r6, r2, r8)     // Catch:{ JsonProcessingException -> 0x0144, IOException -> 0x013d }
        L_0x0131:
            com.dropbox.core.v2.callbacks.DbxGlobalCallbackFactory r8 = sharedCallbackFactory
            if (r8 == 0) goto L_0x013c
            com.dropbox.core.v2.callbacks.DbxNetworkErrorCallback r8 = r8.createNetworkErrorCallback(r9)
            r8.onNetworkError(r0)
        L_0x013c:
            return r0
        L_0x013d:
            r8 = move-exception
            com.dropbox.core.NetworkIOException r9 = new com.dropbox.core.NetworkIOException
            r9.<init>(r8)
            throw r9
        L_0x0144:
            r8 = move-exception
            com.dropbox.core.BadResponseException r9 = new com.dropbox.core.BadResponseException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Bad JSON: "
            r0.append(r1)
            java.lang.String r1 = r8.getMessage()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9.<init>(r6, r0, r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.DbxRequestUtil.unexpectedStatus(com.dropbox.core.http.HttpRequestor$Response, java.lang.String):com.dropbox.core.DbxException");
    }

    private static String messageFromResponse(Response response, String str) throws NetworkIOException, BadResponseException {
        return parseErrorBody(str, response.getStatusCode(), loadErrorBody(response));
    }

    public static <T> T readJsonFromResponse(JsonReader<T> jsonReader, Response response) throws BadResponseException, NetworkIOException {
        try {
            return jsonReader.readFully(response.getBody());
        } catch (JsonReadException e) {
            String requestId = getRequestId(response);
            StringBuilder sb = new StringBuilder();
            sb.append("error in response JSON: ");
            sb.append(e.getMessage());
            throw new BadResponseException(requestId, sb.toString(), e);
        } catch (IOException e2) {
            throw new NetworkIOException(e2);
        }
    }

    public static <T> T doGet(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, String str4, String[] strArr, List<Header> list, ResponseHandler<T> responseHandler) throws DbxException {
        int maxRetries = dbxRequestConfig.getMaxRetries();
        final DbxRequestConfig dbxRequestConfig2 = dbxRequestConfig;
        final String str5 = str;
        final String str6 = str2;
        final String str7 = str3;
        final String str8 = str4;
        final String[] strArr2 = strArr;
        final List<Header> list2 = list;
        final ResponseHandler<T> responseHandler2 = responseHandler;
        C05481 r1 = new RequestMaker<T, DbxException>() {
            public T run() throws DbxException {
                Response startGet = DbxRequestUtil.startGet(dbxRequestConfig2, str5, str6, str7, str8, strArr2, list2);
                try {
                    T handle = responseHandler2.handle(startGet);
                    try {
                        return handle;
                    } catch (IOException e) {
                        throw new NetworkIOException(e);
                    }
                } finally {
                    try {
                        startGet.getBody().close();
                    } catch (IOException e2) {
                        throw new NetworkIOException(e2);
                    }
                }
            }
        };
        return runAndRetry(maxRetries, r1);
    }

    public static <T> T doPost(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, String str4, String[] strArr, List<Header> list, ResponseHandler<T> responseHandler) throws DbxException {
        return doPostNoAuth(dbxRequestConfig, str2, str3, str4, strArr, addAuthHeader(copyHeaders(list), str), responseHandler);
    }

    public static <T> T doPostNoAuth(DbxRequestConfig dbxRequestConfig, String str, String str2, String str3, String[] strArr, List<Header> list, ResponseHandler<T> responseHandler) throws DbxException {
        int maxRetries = dbxRequestConfig.getMaxRetries();
        final DbxRequestConfig dbxRequestConfig2 = dbxRequestConfig;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final String[] strArr2 = strArr;
        final List<Header> list2 = list;
        final ResponseHandler<T> responseHandler2 = responseHandler;
        C05492 r1 = new RequestMaker<T, DbxException>() {
            public T run() throws DbxException {
                return DbxRequestUtil.finishResponse(DbxRequestUtil.startPostNoAuth(dbxRequestConfig2, str4, str5, str6, strArr2, list2), responseHandler2);
            }
        };
        return runAndRetry(maxRetries, r1);
    }

    public static <T> T finishResponse(Response response, ResponseHandler<T> responseHandler) throws DbxException {
        try {
            T handle = responseHandler.handle(response);
            return handle;
        } finally {
            IOUtil.closeInput(response.getBody());
        }
    }

    public static String getFirstHeader(Response response, String str) throws BadResponseException {
        List list = (List) response.getHeaders().get(str);
        if (list != null && !list.isEmpty()) {
            return (String) list.get(0);
        }
        String requestId = getRequestId(response);
        StringBuilder sb = new StringBuilder();
        sb.append("missing HTTP header \"");
        sb.append(str);
        sb.append("\"");
        throw new BadResponseException(requestId, sb.toString());
    }

    public static String getFirstHeaderMaybe(Response response, String str) {
        List list = (List) response.getHeaders().get(str);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (String) list.get(0);
    }

    public static String getRequestId(Response response) {
        return getFirstHeaderMaybe(response, "X-Dropbox-Request-Id");
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0012  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002e A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T, E extends java.lang.Throwable> T runAndRetry(int r8, com.dropbox.core.DbxRequestUtil.RequestMaker<T, E> r9) throws com.dropbox.core.DbxException, java.lang.Throwable {
        /*
            r0 = 0
        L_0x0001:
            r1 = 0
            java.lang.Object r8 = r9.run()     // Catch:{ RetryException -> 0x000b, ServerException -> 0x0008 }
            return r8
        L_0x0008:
            r3 = move-exception
            r4 = r1
            goto L_0x0010
        L_0x000b:
            r3 = move-exception
            long r4 = r3.getBackoffMillis()
        L_0x0010:
            if (r0 >= r8) goto L_0x002e
            java.util.Random r3 = RAND
            r6 = 1000(0x3e8, float:1.401E-42)
            int r3 = r3.nextInt(r6)
            long r6 = (long) r3
            long r4 = r4 + r6
            int r1 = (r4 > r1 ? 1 : (r4 == r1 ? 0 : -1))
            if (r1 <= 0) goto L_0x002b
            java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x0024 }
            goto L_0x002b
        L_0x0024:
            java.lang.Thread r1 = java.lang.Thread.currentThread()
            r1.interrupt()
        L_0x002b:
            int r0 = r0 + 1
            goto L_0x0001
        L_0x002e:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.DbxRequestUtil.runAndRetry(int, com.dropbox.core.DbxRequestUtil$RequestMaker):java.lang.Object");
    }
}
