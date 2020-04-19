package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.extensions.AsyncOperationStatus;
import com.onedrive.sdk.http.IConnection;
import com.onedrive.sdk.http.IStatefulResponseHandler;

public class AsyncMonitorResponseHandler implements IStatefulResponseHandler<AsyncOperationStatus, String> {
    public void configConnection(IConnection iConnection) {
        iConnection.setFollowRedirects(false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.onedrive.sdk.extensions.AsyncOperationStatus generateResult(com.onedrive.sdk.http.IHttpRequest r2, com.onedrive.sdk.http.IConnection r3, com.onedrive.sdk.serializer.ISerializer r4, com.onedrive.sdk.logger.ILogger r5) throws java.lang.Exception {
        /*
            r1 = this;
            int r2 = r3.getResponseCode()
            r0 = 303(0x12f, float:4.25E-43)
            if (r2 != r0) goto L_0x001e
            java.lang.String r2 = "Item copy job has completed."
            r5.logDebug(r2)
            java.util.Map r2 = r3.getHeaders()
            java.lang.String r3 = "Location"
            java.lang.Object r2 = r2.get(r3)
            java.lang.String r2 = (java.lang.String) r2
            com.onedrive.sdk.extensions.AsyncOperationStatus r2 = com.onedrive.sdk.extensions.AsyncOperationStatus.createdCompleted(r2)
            return r2
        L_0x001e:
            r2 = 0
            java.io.BufferedInputStream r5 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0048 }
            java.io.InputStream r0 = r3.getInputStream()     // Catch:{ all -> 0x0048 }
            r5.<init>(r0)     // Catch:{ all -> 0x0048 }
            java.lang.String r2 = com.onedrive.sdk.http.DefaultHttpProvider.streamToString(r5)     // Catch:{ all -> 0x0046 }
            java.lang.Class<com.onedrive.sdk.extensions.AsyncOperationStatus> r0 = com.onedrive.sdk.extensions.AsyncOperationStatus.class
            java.lang.Object r2 = r4.deserializeObject(r2, r0)     // Catch:{ all -> 0x0046 }
            com.onedrive.sdk.extensions.AsyncOperationStatus r2 = (com.onedrive.sdk.extensions.AsyncOperationStatus) r2     // Catch:{ all -> 0x0046 }
            java.util.Map r3 = r3.getHeaders()     // Catch:{ all -> 0x0046 }
            java.lang.String r4 = "Location"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x0046 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0046 }
            r2.seeOther = r3     // Catch:{ all -> 0x0046 }
            r5.close()
            return r2
        L_0x0046:
            r2 = move-exception
            goto L_0x004b
        L_0x0048:
            r3 = move-exception
            r5 = r2
            r2 = r3
        L_0x004b:
            if (r5 == 0) goto L_0x0050
            r5.close()
        L_0x0050:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.onedrive.sdk.concurrency.AsyncMonitorResponseHandler.generateResult(com.onedrive.sdk.http.IHttpRequest, com.onedrive.sdk.http.IConnection, com.onedrive.sdk.serializer.ISerializer, com.onedrive.sdk.logger.ILogger):com.onedrive.sdk.extensions.AsyncOperationStatus");
    }
}
