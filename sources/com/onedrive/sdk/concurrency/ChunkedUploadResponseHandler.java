package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.extensions.ChunkedUploadResult;
import com.onedrive.sdk.http.IConnection;
import com.onedrive.sdk.http.IStatefulResponseHandler;

public class ChunkedUploadResponseHandler<UploadType> implements IStatefulResponseHandler<ChunkedUploadResult, UploadType> {
    private final Class<UploadType> mDeserializeTypeClass;

    public void configConnection(IConnection iConnection) {
    }

    public ChunkedUploadResponseHandler(Class<UploadType> cls) {
        this.mDeserializeTypeClass = cls;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x007c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.onedrive.sdk.extensions.ChunkedUploadResult generateResult(com.onedrive.sdk.http.IHttpRequest r4, com.onedrive.sdk.http.IConnection r5, com.onedrive.sdk.serializer.ISerializer r6, com.onedrive.sdk.logger.ILogger r7) throws java.lang.Exception {
        /*
            r3 = this;
            r0 = 0
            int r1 = r5.getResponseCode()     // Catch:{ all -> 0x0079 }
            r2 = 202(0xca, float:2.83E-43)
            if (r1 != r2) goto L_0x002f
            java.lang.String r4 = "Chunk bytes has been accepted by the server."
            r7.logDebug(r4)     // Catch:{ all -> 0x0079 }
            java.io.BufferedInputStream r4 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0079 }
            java.io.InputStream r5 = r5.getInputStream()     // Catch:{ all -> 0x0079 }
            r4.<init>(r5)     // Catch:{ all -> 0x0079 }
            java.lang.String r5 = com.onedrive.sdk.http.DefaultHttpProvider.streamToString(r4)     // Catch:{ all -> 0x002c }
            java.lang.Class<com.onedrive.sdk.extensions.UploadSession> r7 = com.onedrive.sdk.extensions.UploadSession.class
            java.lang.Object r5 = r6.deserializeObject(r5, r7)     // Catch:{ all -> 0x002c }
            com.onedrive.sdk.extensions.UploadSession r5 = (com.onedrive.sdk.extensions.UploadSession) r5     // Catch:{ all -> 0x002c }
            com.onedrive.sdk.extensions.ChunkedUploadResult r6 = new com.onedrive.sdk.extensions.ChunkedUploadResult     // Catch:{ all -> 0x002c }
            r6.<init>(r5)     // Catch:{ all -> 0x002c }
            r4.close()
            return r6
        L_0x002c:
            r5 = move-exception
            r0 = r4
            goto L_0x007a
        L_0x002f:
            int r1 = r5.getResponseCode()     // Catch:{ all -> 0x0079 }
            r2 = 201(0xc9, float:2.82E-43)
            if (r1 == r2) goto L_0x0058
            int r1 = r5.getResponseCode()     // Catch:{ all -> 0x0079 }
            r2 = 200(0xc8, float:2.8E-43)
            if (r1 != r2) goto L_0x0040
            goto L_0x0058
        L_0x0040:
            int r1 = r5.getResponseCode()     // Catch:{ all -> 0x0079 }
            r2 = 400(0x190, float:5.6E-43)
            if (r1 < r2) goto L_0x0057
            java.lang.String r1 = "Receiving error during upload, see detail on result error"
            r7.logDebug(r1)     // Catch:{ all -> 0x0079 }
            com.onedrive.sdk.extensions.ChunkedUploadResult r7 = new com.onedrive.sdk.extensions.ChunkedUploadResult     // Catch:{ all -> 0x0079 }
            com.onedrive.sdk.http.OneDriveServiceException r4 = com.onedrive.sdk.http.OneDriveServiceException.createFromConnection(r4, r0, r6, r5)     // Catch:{ all -> 0x0079 }
            r7.<init>(r4)     // Catch:{ all -> 0x0079 }
            return r7
        L_0x0057:
            return r0
        L_0x0058:
            java.lang.String r4 = "Upload session is completed, uploaded item returned."
            r7.logDebug(r4)     // Catch:{ all -> 0x0079 }
            java.io.BufferedInputStream r4 = new java.io.BufferedInputStream     // Catch:{ all -> 0x0079 }
            java.io.InputStream r5 = r5.getInputStream()     // Catch:{ all -> 0x0079 }
            r4.<init>(r5)     // Catch:{ all -> 0x0079 }
            java.lang.String r5 = com.onedrive.sdk.http.DefaultHttpProvider.streamToString(r4)     // Catch:{ all -> 0x002c }
            java.lang.Class<UploadType> r7 = r3.mDeserializeTypeClass     // Catch:{ all -> 0x002c }
            java.lang.Object r5 = r6.deserializeObject(r5, r7)     // Catch:{ all -> 0x002c }
            com.onedrive.sdk.extensions.ChunkedUploadResult r6 = new com.onedrive.sdk.extensions.ChunkedUploadResult     // Catch:{ all -> 0x002c }
            r6.<init>(r5)     // Catch:{ all -> 0x002c }
            r4.close()
            return r6
        L_0x0079:
            r5 = move-exception
        L_0x007a:
            if (r0 == 0) goto L_0x007f
            r0.close()
        L_0x007f:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.onedrive.sdk.concurrency.ChunkedUploadResponseHandler.generateResult(com.onedrive.sdk.http.IHttpRequest, com.onedrive.sdk.http.IConnection, com.onedrive.sdk.serializer.ISerializer, com.onedrive.sdk.logger.ILogger):com.onedrive.sdk.extensions.ChunkedUploadResult");
    }
}
