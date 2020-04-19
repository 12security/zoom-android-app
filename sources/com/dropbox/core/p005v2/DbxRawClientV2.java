package com.dropbox.core.p005v2;

import com.dropbox.core.BadResponseException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.p005v2.common.PathRoot;
import com.dropbox.core.stone.StoneSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* renamed from: com.dropbox.core.v2.DbxRawClientV2 */
public abstract class DbxRawClientV2 {
    private static final JsonFactory JSON = new JsonFactory();
    private static final Random RAND = new Random();
    public static final String USER_AGENT_ID = "OfficialDropboxJavaSDKv2";
    private final DbxHost host;
    private final PathRoot pathRoot;
    /* access modifiers changed from: private */
    public final DbxRequestConfig requestConfig;
    private final String userId;

    /* renamed from: com.dropbox.core.v2.DbxRawClientV2$RetriableExecution */
    private interface RetriableExecution<T> {
        T execute() throws DbxWrappedException, DbxException;
    }

    /* access modifiers changed from: protected */
    public abstract void addAuthHeaders(List<Header> list);

    /* access modifiers changed from: protected */
    public abstract DbxRawClientV2 withPathRoot(PathRoot pathRoot2);

    protected DbxRawClientV2(DbxRequestConfig dbxRequestConfig, DbxHost dbxHost, String str, PathRoot pathRoot2) {
        if (dbxRequestConfig == null) {
            throw new NullPointerException("requestConfig");
        } else if (dbxHost != null) {
            this.requestConfig = dbxRequestConfig;
            this.host = dbxHost;
            this.userId = str;
            this.pathRoot = pathRoot2;
        } else {
            throw new NullPointerException("host");
        }
    }

    public <ArgT, ResT, ErrT> ResT rpcStyle(String str, String str2, ArgT argt, boolean z, StoneSerializer<ArgT> stoneSerializer, StoneSerializer<ResT> stoneSerializer2, StoneSerializer<ErrT> stoneSerializer3) throws DbxWrappedException, DbxException {
        final byte[] writeAsBytes = writeAsBytes(stoneSerializer, argt);
        final ArrayList arrayList = new ArrayList();
        if (!z) {
            addAuthHeaders(arrayList);
        }
        if (!this.host.getNotify().equals(str)) {
            DbxRequestUtil.addUserLocaleHeader(arrayList, this.requestConfig);
            DbxRequestUtil.addPathRootHeader(arrayList, this.pathRoot);
        }
        arrayList.add(new Header("Content-Type", "application/json; charset=utf-8"));
        int maxRetries = this.requestConfig.getMaxRetries();
        final String str3 = str;
        final String str4 = str2;
        final StoneSerializer<ResT> stoneSerializer4 = stoneSerializer2;
        final StoneSerializer<ErrT> stoneSerializer5 = stoneSerializer3;
        C06201 r0 = new RetriableExecution<ResT>() {
            private String userIdAnon;

            public ResT execute() throws DbxWrappedException, DbxException {
                Response startPostRaw = DbxRequestUtil.startPostRaw(DbxRawClientV2.this.requestConfig, DbxRawClientV2.USER_AGENT_ID, str3, str4, writeAsBytes, arrayList);
                try {
                    int statusCode = startPostRaw.getStatusCode();
                    if (statusCode == 200) {
                        return stoneSerializer4.deserialize(startPostRaw.getBody());
                    }
                    if (statusCode != 409) {
                        throw DbxRequestUtil.unexpectedStatus(startPostRaw, this.userIdAnon);
                    }
                    throw DbxWrappedException.fromResponse(stoneSerializer5, startPostRaw, this.userIdAnon);
                } catch (JsonProcessingException e) {
                    String requestId = DbxRequestUtil.getRequestId(startPostRaw);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bad JSON: ");
                    sb.append(e.getMessage());
                    throw new BadResponseException(requestId, sb.toString(), e);
                } catch (IOException e2) {
                    throw new NetworkIOException(e2);
                }
            }

            /* access modifiers changed from: private */
            public RetriableExecution<ResT> init(String str) {
                this.userIdAnon = str;
                return this;
            }
        };
        return executeRetriable(maxRetries, r0.init(this.userId));
    }

    public <ArgT, ResT, ErrT> DbxDownloader<ResT> downloadStyle(String str, String str2, ArgT argt, boolean z, List<Header> list, StoneSerializer<ArgT> stoneSerializer, StoneSerializer<ResT> stoneSerializer2, StoneSerializer<ErrT> stoneSerializer3) throws DbxWrappedException, DbxException {
        final ArrayList arrayList = new ArrayList(list);
        if (!z) {
            addAuthHeaders(arrayList);
        }
        DbxRequestUtil.addUserLocaleHeader(arrayList, this.requestConfig);
        DbxRequestUtil.addPathRootHeader(arrayList, this.pathRoot);
        ArgT argt2 = argt;
        arrayList.add(new Header("Dropbox-API-Arg", headerSafeJson(stoneSerializer, argt)));
        arrayList.add(new Header("Content-Type", ""));
        final byte[] bArr = new byte[0];
        int maxRetries = this.requestConfig.getMaxRetries();
        final String str3 = str;
        final String str4 = str2;
        final StoneSerializer<ResT> stoneSerializer4 = stoneSerializer2;
        final StoneSerializer<ErrT> stoneSerializer5 = stoneSerializer3;
        C06212 r0 = new RetriableExecution<DbxDownloader<ResT>>() {
            private String userIdAnon;

            public DbxDownloader<ResT> execute() throws DbxWrappedException, DbxException {
                Response startPostRaw = DbxRequestUtil.startPostRaw(DbxRawClientV2.this.requestConfig, DbxRawClientV2.USER_AGENT_ID, str3, str4, bArr, arrayList);
                String requestId = DbxRequestUtil.getRequestId(startPostRaw);
                try {
                    int statusCode = startPostRaw.getStatusCode();
                    if (statusCode == 200 || statusCode == 206) {
                        List list = (List) startPostRaw.getHeaders().get("dropbox-api-result");
                        if (list == null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Missing Dropbox-API-Result header; ");
                            sb.append(startPostRaw.getHeaders());
                            throw new BadResponseException(requestId, sb.toString());
                        } else if (list.size() != 0) {
                            String str = (String) list.get(0);
                            if (str != null) {
                                return new DbxDownloader<>(stoneSerializer4.deserialize(str), startPostRaw.getBody());
                            }
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Null Dropbox-API-Result header; ");
                            sb2.append(startPostRaw.getHeaders());
                            throw new BadResponseException(requestId, sb2.toString());
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("No Dropbox-API-Result header; ");
                            sb3.append(startPostRaw.getHeaders());
                            throw new BadResponseException(requestId, sb3.toString());
                        }
                    } else if (statusCode != 409) {
                        throw DbxRequestUtil.unexpectedStatus(startPostRaw, this.userIdAnon);
                    } else {
                        throw DbxWrappedException.fromResponse(stoneSerializer5, startPostRaw, this.userIdAnon);
                    }
                } catch (JsonProcessingException e) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Bad JSON: ");
                    sb4.append(e.getMessage());
                    throw new BadResponseException(requestId, sb4.toString(), e);
                } catch (IOException e2) {
                    throw new NetworkIOException(e2);
                }
            }

            /* access modifiers changed from: private */
            public RetriableExecution<DbxDownloader<ResT>> init(String str) {
                this.userIdAnon = str;
                return this;
            }
        };
        return (DbxDownloader) executeRetriable(maxRetries, r0.init(this.userId));
    }

    private static <T> byte[] writeAsBytes(StoneSerializer<T> stoneSerializer, T t) throws DbxException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            stoneSerializer.serialize(t, (OutputStream) byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw LangUtil.mkAssert("Impossible", e);
        }
    }

    private static <T> String headerSafeJson(StoneSerializer<T> stoneSerializer, T t) {
        StringWriter stringWriter = new StringWriter();
        try {
            JsonGenerator createGenerator = JSON.createGenerator((Writer) stringWriter);
            createGenerator.setHighestNonEscapedChar(126);
            stoneSerializer.serialize(t, createGenerator);
            createGenerator.flush();
            return stringWriter.toString();
        } catch (IOException e) {
            throw LangUtil.mkAssert("Impossible", e);
        }
    }

    public <ArgT> Uploader uploadStyle(String str, String str2, ArgT argt, boolean z, StoneSerializer<ArgT> stoneSerializer) throws DbxException {
        String buildUri = DbxRequestUtil.buildUri(str, str2);
        ArrayList arrayList = new ArrayList();
        if (!z) {
            addAuthHeaders(arrayList);
        }
        DbxRequestUtil.addUserLocaleHeader(arrayList, this.requestConfig);
        DbxRequestUtil.addPathRootHeader(arrayList, this.pathRoot);
        arrayList.add(new Header("Content-Type", "application/octet-stream"));
        List addUserAgentHeader = DbxRequestUtil.addUserAgentHeader(arrayList, this.requestConfig, USER_AGENT_ID);
        addUserAgentHeader.add(new Header("Dropbox-API-Arg", headerSafeJson(stoneSerializer, argt)));
        try {
            return this.requestConfig.getHttpRequestor().startPost(buildUri, addUserAgentHeader);
        } catch (IOException e) {
            throw new NetworkIOException(e);
        }
    }

    public DbxRequestConfig getRequestConfig() {
        return this.requestConfig;
    }

    public DbxHost getHost() {
        return this.host;
    }

    public String getUserId() {
        return this.userId;
    }

    private static <T> T executeRetriable(int i, RetriableExecution<T> retriableExecution) throws DbxWrappedException, DbxException {
        if (i == 0) {
            return retriableExecution.execute();
        }
        int i2 = 0;
        while (true) {
            try {
                return retriableExecution.execute();
            } catch (RetryException e) {
                if (i2 < i) {
                    i2++;
                    sleepQuietlyWithJitter(e.getBackoffMillis());
                } else {
                    throw e;
                }
            }
        }
    }

    private static void sleepQuietlyWithJitter(long j) {
        long nextInt = j + ((long) RAND.nextInt(1000));
        if (nextInt > 0) {
            try {
                Thread.sleep(nextInt);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
