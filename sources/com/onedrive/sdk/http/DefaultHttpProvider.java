package com.onedrive.sdk.http;

import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.concurrency.IProgressCallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.serializer.ISerializer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class DefaultHttpProvider implements IHttpProvider {
    static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    static final String JSON_CONTENT_TYPE = "application/json";
    private IConnectionFactory mConnectionFactory = new DefaultConnectionFactory();
    /* access modifiers changed from: private */
    public final IExecutors mExecutors;
    private final ILogger mLogger;
    private final IRequestInterceptor mRequestInterceptor;
    private final ISerializer mSerializer;

    private InputStream handleBinaryStream(InputStream inputStream) {
        return inputStream;
    }

    public DefaultHttpProvider(ISerializer iSerializer, IRequestInterceptor iRequestInterceptor, IExecutors iExecutors, ILogger iLogger) {
        this.mSerializer = iSerializer;
        this.mRequestInterceptor = iRequestInterceptor;
        this.mExecutors = iExecutors;
        this.mLogger = iLogger;
    }

    public ISerializer getSerializer() {
        return this.mSerializer;
    }

    public <Result, Body> void send(IHttpRequest iHttpRequest, ICallback<Result> iCallback, Class<Result> cls, Body body) {
        final IProgressCallback iProgressCallback = iCallback instanceof IProgressCallback ? (IProgressCallback) iCallback : null;
        IExecutors iExecutors = this.mExecutors;
        final IHttpRequest iHttpRequest2 = iHttpRequest;
        final Class<Result> cls2 = cls;
        final Body body2 = body;
        final ICallback<Result> iCallback2 = iCallback;
        C18061 r1 = new Runnable() {
            public void run() {
                try {
                    DefaultHttpProvider.this.mExecutors.performOnForeground(DefaultHttpProvider.this.sendRequestInternal(iHttpRequest2, cls2, body2, iProgressCallback, null), iCallback2);
                } catch (ClientException e) {
                    DefaultHttpProvider.this.mExecutors.performOnForeground(e, iCallback2);
                }
            }
        };
        iExecutors.performOnBackground(r1);
    }

    public <Result, Body> Result send(IHttpRequest iHttpRequest, Class<Result> cls, Body body) throws ClientException {
        return send(iHttpRequest, cls, body, null);
    }

    public <Result, Body, DeserializeType> Result send(IHttpRequest iHttpRequest, Class<Result> cls, Body body, IStatefulResponseHandler<Result, DeserializeType> iStatefulResponseHandler) throws ClientException {
        return sendRequestInternal(iHttpRequest, cls, body, null, iStatefulResponseHandler);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01eb A[Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:88:0x01c7=Splitter:B:88:0x01c7, B:65:0x017b=Splitter:B:65:0x017b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <Result, Body, DeserializeType> Result sendRequestInternal(com.onedrive.sdk.http.IHttpRequest r17, java.lang.Class<Result> r18, Body r19, com.onedrive.sdk.concurrency.IProgressCallback<Result> r20, com.onedrive.sdk.http.IStatefulResponseHandler<Result, DeserializeType> r21) throws com.onedrive.sdk.core.ClientException {
        /*
            r16 = this;
            r1 = r16
            r0 = r17
            r2 = r18
            r3 = r19
            r4 = r20
            r5 = r21
            r6 = 1
            r7 = 0
            com.onedrive.sdk.http.IRequestInterceptor r8 = r1.mRequestInterceptor     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            if (r8 == 0) goto L_0x0017
            com.onedrive.sdk.http.IRequestInterceptor r8 = r1.mRequestInterceptor     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r8.intercept(r0)     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x0017:
            java.net.URL r8 = r17.getRequestUrl()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            com.onedrive.sdk.logger.ILogger r9 = r1.mLogger     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r10.<init>()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            java.lang.String r11 = "Starting to send request, URL "
            r10.append(r11)     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            java.lang.String r8 = r8.toString()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r10.append(r8)     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            java.lang.String r8 = r10.toString()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r9.logDebug(r8)     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            com.onedrive.sdk.http.IConnectionFactory r8 = r1.mConnectionFactory     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            com.onedrive.sdk.http.IConnection r8 = r8.createFromRequest(r0)     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            com.onedrive.sdk.logger.ILogger r10 = r1.mLogger     // Catch:{ all -> 0x01e5 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x01e5 }
            r11.<init>()     // Catch:{ all -> 0x01e5 }
            java.lang.String r12 = "Request Method "
            r11.append(r12)     // Catch:{ all -> 0x01e5 }
            com.onedrive.sdk.http.HttpMethod r12 = r17.getHttpMethod()     // Catch:{ all -> 0x01e5 }
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x01e5 }
            r11.append(r12)     // Catch:{ all -> 0x01e5 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x01e5 }
            r10.logDebug(r11)     // Catch:{ all -> 0x01e5 }
            if (r3 != 0) goto L_0x005d
            r10 = 0
            goto L_0x00b7
        L_0x005d:
            boolean r10 = r3 instanceof byte[]     // Catch:{ all -> 0x01e5 }
            if (r10 == 0) goto L_0x007f
            com.onedrive.sdk.logger.ILogger r10 = r1.mLogger     // Catch:{ all -> 0x0079 }
            java.lang.String r11 = "Sending byte[] as request body"
            r10.logDebug(r11)     // Catch:{ all -> 0x0079 }
            r10 = r3
            byte[] r10 = (byte[]) r10     // Catch:{ all -> 0x0079 }
            byte[] r10 = (byte[]) r10     // Catch:{ all -> 0x0079 }
            java.lang.String r11 = "Content-Type"
            java.lang.String r12 = "application/octet-stream"
            r8.addRequestHeader(r11, r12)     // Catch:{ all -> 0x0079 }
            int r11 = r10.length     // Catch:{ all -> 0x0079 }
            r8.setContentLength(r11)     // Catch:{ all -> 0x0079 }
            goto L_0x00b7
        L_0x0079:
            r0 = move-exception
            r2 = 0
            r3 = 0
            r9 = 0
            goto L_0x01e9
        L_0x007f:
            com.onedrive.sdk.logger.ILogger r10 = r1.mLogger     // Catch:{ all -> 0x01e5 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x01e5 }
            r11.<init>()     // Catch:{ all -> 0x01e5 }
            java.lang.String r12 = "Sending "
            r11.append(r12)     // Catch:{ all -> 0x01e5 }
            java.lang.Class r12 = r19.getClass()     // Catch:{ all -> 0x01e5 }
            java.lang.String r12 = r12.getName()     // Catch:{ all -> 0x01e5 }
            r11.append(r12)     // Catch:{ all -> 0x01e5 }
            java.lang.String r12 = " as request body"
            r11.append(r12)     // Catch:{ all -> 0x01e5 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x01e5 }
            r10.logDebug(r11)     // Catch:{ all -> 0x01e5 }
            com.onedrive.sdk.serializer.ISerializer r10 = r1.mSerializer     // Catch:{ all -> 0x01e5 }
            java.lang.String r10 = r10.serializeObject(r3)     // Catch:{ all -> 0x01e5 }
            byte[] r10 = r10.getBytes()     // Catch:{ all -> 0x01e5 }
            java.lang.String r11 = "Content-Type"
            java.lang.String r12 = "application/json"
            r8.addRequestHeader(r11, r12)     // Catch:{ all -> 0x01e5 }
            int r11 = r10.length     // Catch:{ all -> 0x01e5 }
            r8.setContentLength(r11)     // Catch:{ all -> 0x01e5 }
        L_0x00b7:
            if (r10 == 0) goto L_0x00e2
            java.io.OutputStream r11 = r8.getOutputStream()     // Catch:{ all -> 0x0079 }
            java.io.BufferedOutputStream r12 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x00de }
            r12.<init>(r11)     // Catch:{ all -> 0x00de }
            r13 = 0
        L_0x00c3:
            r14 = 4096(0x1000, float:5.74E-42)
            int r15 = r10.length     // Catch:{ all -> 0x00de }
            int r15 = r15 - r13
            int r14 = java.lang.Math.min(r14, r15)     // Catch:{ all -> 0x00de }
            r12.write(r10, r13, r14)     // Catch:{ all -> 0x00de }
            int r13 = r13 + r14
            if (r4 == 0) goto L_0x00d7
            com.onedrive.sdk.concurrency.IExecutors r15 = r1.mExecutors     // Catch:{ all -> 0x00de }
            int r9 = r10.length     // Catch:{ all -> 0x00de }
            r15.performOnForeground(r13, r9, r4)     // Catch:{ all -> 0x00de }
        L_0x00d7:
            if (r14 > 0) goto L_0x00c3
            r12.close()     // Catch:{ all -> 0x00de }
            r9 = r11
            goto L_0x00e3
        L_0x00de:
            r0 = move-exception
            r9 = r11
            goto L_0x01e3
        L_0x00e2:
            r9 = 0
        L_0x00e3:
            if (r5 == 0) goto L_0x00e8
            r5.configConnection(r8)     // Catch:{ all -> 0x01e2 }
        L_0x00e8:
            com.onedrive.sdk.logger.ILogger r4 = r1.mLogger     // Catch:{ all -> 0x01e2 }
            java.lang.String r10 = "Response code %d, %s"
            r11 = 2
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ all -> 0x01e2 }
            int r12 = r8.getResponseCode()     // Catch:{ all -> 0x01e2 }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x01e2 }
            r11[r7] = r12     // Catch:{ all -> 0x01e2 }
            java.lang.String r12 = r8.getResponseMessage()     // Catch:{ all -> 0x01e2 }
            r11[r6] = r12     // Catch:{ all -> 0x01e2 }
            java.lang.String r10 = java.lang.String.format(r10, r11)     // Catch:{ all -> 0x01e2 }
            r4.logDebug(r10)     // Catch:{ all -> 0x01e2 }
            if (r5 == 0) goto L_0x011f
            com.onedrive.sdk.logger.ILogger r2 = r1.mLogger     // Catch:{ all -> 0x01e2 }
            java.lang.String r3 = "StatefulResponse is handling the HTTP response."
            r2.logDebug(r3)     // Catch:{ all -> 0x01e2 }
            com.onedrive.sdk.serializer.ISerializer r2 = r16.getSerializer()     // Catch:{ all -> 0x01e2 }
            com.onedrive.sdk.logger.ILogger r3 = r1.mLogger     // Catch:{ all -> 0x01e2 }
            java.lang.Object r0 = r5.generateResult(r0, r8, r2, r3)     // Catch:{ all -> 0x01e2 }
            if (r9 == 0) goto L_0x011e
            r9.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x011e:
            return r0
        L_0x011f:
            int r4 = r8.getResponseCode()     // Catch:{ all -> 0x01e2 }
            r5 = 400(0x190, float:5.6E-43)
            if (r4 < r5) goto L_0x0136
            com.onedrive.sdk.logger.ILogger r4 = r1.mLogger     // Catch:{ all -> 0x01e2 }
            java.lang.String r5 = "Handling error response"
            r4.logDebug(r5)     // Catch:{ all -> 0x01e2 }
            java.io.InputStream r4 = r8.getInputStream()     // Catch:{ all -> 0x01e2 }
            r1.handleErrorResponse(r0, r3, r8)     // Catch:{ all -> 0x01df }
            goto L_0x0137
        L_0x0136:
            r4 = 0
        L_0x0137:
            int r0 = r8.getResponseCode()     // Catch:{ all -> 0x01df }
            r3 = 204(0xcc, float:2.86E-43)
            if (r0 == r3) goto L_0x01c7
            int r0 = r8.getResponseCode()     // Catch:{ all -> 0x01df }
            r3 = 304(0x130, float:4.26E-43)
            if (r0 != r3) goto L_0x0149
            goto L_0x01c7
        L_0x0149:
            int r0 = r8.getResponseCode()     // Catch:{ all -> 0x01df }
            r3 = 202(0xca, float:2.83E-43)
            if (r0 != r3) goto L_0x017b
            com.onedrive.sdk.logger.ILogger r0 = r1.mLogger     // Catch:{ all -> 0x01df }
            java.lang.String r3 = "Handling accepted response"
            r0.logDebug(r3)     // Catch:{ all -> 0x01df }
            java.lang.Class<com.onedrive.sdk.concurrency.AsyncMonitorLocation> r0 = com.onedrive.sdk.concurrency.AsyncMonitorLocation.class
            if (r2 != r0) goto L_0x017b
            com.onedrive.sdk.concurrency.AsyncMonitorLocation r0 = new com.onedrive.sdk.concurrency.AsyncMonitorLocation     // Catch:{ all -> 0x01df }
            java.util.Map r2 = r8.getHeaders()     // Catch:{ all -> 0x01df }
            java.lang.String r3 = "Location"
            java.lang.Object r2 = r2.get(r3)     // Catch:{ all -> 0x01df }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x01df }
            r0.<init>(r2)     // Catch:{ all -> 0x01df }
            if (r9 == 0) goto L_0x0172
            r9.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x0172:
            if (r4 == 0) goto L_0x017a
            r4.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r8.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x017a:
            return r0
        L_0x017b:
            java.io.BufferedInputStream r3 = new java.io.BufferedInputStream     // Catch:{ all -> 0x01df }
            java.io.InputStream r0 = r8.getInputStream()     // Catch:{ all -> 0x01df }
            r3.<init>(r0)     // Catch:{ all -> 0x01df }
            java.util.Map r0 = r8.getHeaders()     // Catch:{ all -> 0x01c4 }
            java.lang.String r4 = "Content-Type"
            java.lang.Object r0 = r0.get(r4)     // Catch:{ all -> 0x01c4 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x01c4 }
            java.lang.String r4 = "application/json"
            boolean r0 = r0.contains(r4)     // Catch:{ all -> 0x01c4 }
            if (r0 == 0) goto L_0x01af
            com.onedrive.sdk.logger.ILogger r0 = r1.mLogger     // Catch:{ all -> 0x01c4 }
            java.lang.String r4 = "Response json"
            r0.logDebug(r4)     // Catch:{ all -> 0x01c4 }
            java.lang.Object r0 = r1.handleJsonResponse(r3, r2)     // Catch:{ all -> 0x01c4 }
            if (r9 == 0) goto L_0x01a8
            r9.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x01a8:
            r3.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r8.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            return r0
        L_0x01af:
            com.onedrive.sdk.logger.ILogger r0 = r1.mLogger     // Catch:{ all -> 0x01c4 }
            java.lang.String r2 = "Response binary"
            r0.logDebug(r2)     // Catch:{ all -> 0x01c4 }
            java.io.InputStream r0 = r1.handleBinaryStream(r3)     // Catch:{ all -> 0x01c0 }
            if (r9 == 0) goto L_0x01bf
            r9.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x01bf:
            return r0
        L_0x01c0:
            r0 = move-exception
            r2 = r3
            r3 = 1
            goto L_0x01e9
        L_0x01c4:
            r0 = move-exception
            r2 = r3
            goto L_0x01e8
        L_0x01c7:
            com.onedrive.sdk.logger.ILogger r0 = r1.mLogger     // Catch:{ all -> 0x01df }
            java.lang.String r2 = "Handling response with no body"
            r0.logDebug(r2)     // Catch:{ all -> 0x01df }
            if (r9 == 0) goto L_0x01d3
            r9.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x01d3:
            if (r4 == 0) goto L_0x01dd
            r4.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r8.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r2 = 0
            goto L_0x01de
        L_0x01dd:
            r2 = 0
        L_0x01de:
            return r2
        L_0x01df:
            r0 = move-exception
            r2 = r4
            goto L_0x01e8
        L_0x01e2:
            r0 = move-exception
        L_0x01e3:
            r2 = 0
            goto L_0x01e8
        L_0x01e5:
            r0 = move-exception
            r2 = 0
            r9 = r2
        L_0x01e8:
            r3 = 0
        L_0x01e9:
            if (r9 == 0) goto L_0x01ee
            r9.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x01ee:
            if (r3 != 0) goto L_0x01f8
            if (r2 == 0) goto L_0x01f8
            r2.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
            r8.close()     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x01f8:
            throw r0     // Catch:{ OneDriveServiceException -> 0x020b, Exception -> 0x01f9 }
        L_0x01f9:
            r0 = move-exception
            com.onedrive.sdk.core.ClientException r2 = new com.onedrive.sdk.core.ClientException
            com.onedrive.sdk.core.OneDriveErrorCodes r3 = com.onedrive.sdk.core.OneDriveErrorCodes.GeneralException
            java.lang.String r4 = "Error during http request"
            r2.<init>(r4, r0, r3)
            com.onedrive.sdk.logger.ILogger r0 = r1.mLogger
            java.lang.String r3 = "Error during http request"
            r0.logError(r3, r2)
            throw r2
        L_0x020b:
            r0 = move-exception
            com.onedrive.sdk.logger.ILogger r2 = r1.mLogger
            com.onedrive.sdk.logger.LoggerLevel r2 = r2.getLoggingLevel()
            com.onedrive.sdk.logger.LoggerLevel r3 = com.onedrive.sdk.logger.LoggerLevel.Debug
            if (r2 != r3) goto L_0x0217
            goto L_0x0218
        L_0x0217:
            r6 = 0
        L_0x0218:
            com.onedrive.sdk.logger.ILogger r2 = r1.mLogger
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "OneDrive Service exception "
            r3.append(r4)
            java.lang.String r4 = r0.getMessage(r6)
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.logError(r3, r0)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.onedrive.sdk.http.DefaultHttpProvider.sendRequestInternal(com.onedrive.sdk.http.IHttpRequest, java.lang.Class, java.lang.Object, com.onedrive.sdk.concurrency.IProgressCallback, com.onedrive.sdk.http.IStatefulResponseHandler):java.lang.Object");
    }

    private <Body> void handleErrorResponse(IHttpRequest iHttpRequest, Body body, IConnection iConnection) throws IOException {
        throw OneDriveServiceException.createFromConnection(iHttpRequest, body, this.mSerializer, iConnection);
    }

    private <Result> Result handleJsonResponse(InputStream inputStream, Class<Result> cls) {
        if (cls == null) {
            return null;
        }
        return getSerializer().deserializeObject(streamToString(inputStream), cls);
    }

    /* access modifiers changed from: 0000 */
    public void setConnectionFactory(IConnectionFactory iConnectionFactory) {
        this.mConnectionFactory = iConnectionFactory;
    }

    public static String streamToString(InputStream inputStream) {
        return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
    }
}
