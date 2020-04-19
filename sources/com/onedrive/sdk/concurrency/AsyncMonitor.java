package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.extensions.AsyncOperationStatus;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.http.IHttpRequest;
import com.onedrive.sdk.http.IStatefulResponseHandler;
import com.onedrive.sdk.logger.ILogger;

public class AsyncMonitor<T> {
    /* access modifiers changed from: private */
    public final IOneDriveClient mClient;
    private final AsyncMonitorResponseHandler mHandler = new AsyncMonitorResponseHandler();
    private final AsyncMonitorLocation mMonitorLocation;
    private final ResultGetter<T> mResultGetter;

    public AsyncMonitor(IOneDriveClient iOneDriveClient, AsyncMonitorLocation asyncMonitorLocation, ResultGetter<T> resultGetter) {
        this.mClient = iOneDriveClient;
        this.mMonitorLocation = asyncMonitorLocation;
        this.mResultGetter = resultGetter;
    }

    public void getStatus(final ICallback<AsyncOperationStatus> iCallback) {
        this.mClient.getExecutors().performOnBackground(new Runnable() {
            public void run() {
                try {
                    AsyncMonitor.this.mClient.getExecutors().performOnForeground(AsyncMonitor.this.getStatus(), iCallback);
                } catch (ClientException e) {
                    AsyncMonitor.this.mClient.getExecutors().performOnForeground(e, iCallback);
                }
            }
        });
    }

    public AsyncOperationStatus getStatus() throws ClientException {
        C17832 r0 = new BaseRequest(this.mMonitorLocation.getLocation(), this.mClient, null, null) {
        };
        r0.setHttpMethod(HttpMethod.GET);
        return (AsyncOperationStatus) this.mClient.getHttpProvider().send((IHttpRequest) r0, AsyncOperationStatus.class, null, (IStatefulResponseHandler<Result, DeserializeType>) this.mHandler);
    }

    public void getResult(final ICallback<T> iCallback) {
        this.mClient.getExecutors().performOnBackground(new Runnable() {
            public void run() {
                try {
                    AsyncMonitor.this.mClient.getExecutors().performOnForeground(AsyncMonitor.this.getResult(), iCallback);
                } catch (ClientException e) {
                    AsyncMonitor.this.mClient.getExecutors().performOnForeground(e, iCallback);
                }
            }
        });
    }

    public T getResult() throws ClientException {
        AsyncOperationStatus status = getStatus();
        if (status.seeOther != null) {
            return this.mResultGetter.getResultFrom(status.seeOther, this.mClient);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Async operation '");
        sb.append(status.operation);
        sb.append("' has not completed!");
        throw new ClientException(sb.toString(), null, OneDriveErrorCodes.AsyncTaskNotCompleted);
    }

    public void pollForResult(final long j, final IProgressCallback<T> iProgressCallback) {
        ILogger logger = this.mClient.getLogger();
        StringBuilder sb = new StringBuilder();
        sb.append("Starting to poll for request ");
        sb.append(this.mMonitorLocation.getLocation());
        logger.logDebug(sb.toString());
        this.mClient.getExecutors().performOnBackground(new Runnable() {
            /* JADX WARNING: Can't wrap try/catch for region: R(4:2|3|4|5) */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x009b, code lost:
                r0 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:18:0x009c, code lost:
                com.onedrive.sdk.concurrency.AsyncMonitor.access$000(r5.this$0).getExecutors().performOnForeground(r0, (com.onedrive.sdk.concurrency.ICallback<Result>) r6);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
                return;
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0009 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r5 = this;
                    r0 = 0
                L_0x0001:
                    if (r0 == 0) goto L_0x0018
                    long r0 = r4     // Catch:{ InterruptedException -> 0x0009 }
                    java.lang.Thread.sleep(r0)     // Catch:{ InterruptedException -> 0x0009 }
                    goto L_0x0018
                L_0x0009:
                    com.onedrive.sdk.concurrency.AsyncMonitor r0 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.extensions.IOneDriveClient r0 = r0.mClient     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.logger.ILogger r0 = r0.getLogger()     // Catch:{ ClientException -> 0x009b }
                    java.lang.String r1 = "InterruptedException ignored"
                    r0.logDebug(r1)     // Catch:{ ClientException -> 0x009b }
                L_0x0018:
                    com.onedrive.sdk.concurrency.AsyncMonitor r0 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.extensions.AsyncOperationStatus r0 = r0.getStatus()     // Catch:{ ClientException -> 0x009b }
                    java.lang.Double r1 = r0.percentageComplete     // Catch:{ ClientException -> 0x009b }
                    if (r1 == 0) goto L_0x0039
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.extensions.IOneDriveClient r1 = r1.mClient     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.IExecutors r1 = r1.getExecutors()     // Catch:{ ClientException -> 0x009b }
                    java.lang.Double r2 = r0.percentageComplete     // Catch:{ ClientException -> 0x009b }
                    int r2 = r2.intValue()     // Catch:{ ClientException -> 0x009b }
                    r3 = 100
                    com.onedrive.sdk.concurrency.IProgressCallback r4 = r6     // Catch:{ ClientException -> 0x009b }
                    r1.performOnForeground(r2, r3, r4)     // Catch:{ ClientException -> 0x009b }
                L_0x0039:
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    boolean r1 = r1.isCompleted(r0)     // Catch:{ ClientException -> 0x009b }
                    if (r1 != 0) goto L_0x0049
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    boolean r1 = r1.isFailed(r0)     // Catch:{ ClientException -> 0x009b }
                    if (r1 == 0) goto L_0x0001
                L_0x0049:
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.extensions.IOneDriveClient r1 = r1.mClient     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.logger.ILogger r1 = r1.getLogger()     // Catch:{ ClientException -> 0x009b }
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ClientException -> 0x009b }
                    r2.<init>()     // Catch:{ ClientException -> 0x009b }
                    java.lang.String r3 = "Polling has completed, got final status: "
                    r2.append(r3)     // Catch:{ ClientException -> 0x009b }
                    java.lang.String r3 = r0.status     // Catch:{ ClientException -> 0x009b }
                    r2.append(r3)     // Catch:{ ClientException -> 0x009b }
                    java.lang.String r2 = r2.toString()     // Catch:{ ClientException -> 0x009b }
                    r1.logDebug(r2)     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    boolean r1 = r1.isFailed(r0)     // Catch:{ ClientException -> 0x009b }
                    if (r1 == 0) goto L_0x0085
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.extensions.IOneDriveClient r1 = r1.mClient     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.IExecutors r1 = r1.getExecutors()     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.AsyncOperationException r2 = new com.onedrive.sdk.concurrency.AsyncOperationException     // Catch:{ ClientException -> 0x009b }
                    r2.<init>(r0)     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.IProgressCallback r0 = r6     // Catch:{ ClientException -> 0x009b }
                    r1.performOnForeground(r2, r0)     // Catch:{ ClientException -> 0x009b }
                L_0x0085:
                    com.onedrive.sdk.concurrency.AsyncMonitor r0 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.extensions.IOneDriveClient r0 = r0.mClient     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.IExecutors r0 = r0.getExecutors()     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this     // Catch:{ ClientException -> 0x009b }
                    java.lang.Object r1 = r1.getResult()     // Catch:{ ClientException -> 0x009b }
                    com.onedrive.sdk.concurrency.IProgressCallback r2 = r6     // Catch:{ ClientException -> 0x009b }
                    r0.performOnForeground(r1, r2)     // Catch:{ ClientException -> 0x009b }
                    goto L_0x00ab
                L_0x009b:
                    r0 = move-exception
                    com.onedrive.sdk.concurrency.AsyncMonitor r1 = com.onedrive.sdk.concurrency.AsyncMonitor.this
                    com.onedrive.sdk.extensions.IOneDriveClient r1 = r1.mClient
                    com.onedrive.sdk.concurrency.IExecutors r1 = r1.getExecutors()
                    com.onedrive.sdk.concurrency.IProgressCallback r2 = r6
                    r1.performOnForeground(r0, r2)
                L_0x00ab:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.onedrive.sdk.concurrency.AsyncMonitor.C17854.run():void");
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isCompleted(AsyncOperationStatus asyncOperationStatus) {
        return asyncOperationStatus.status.equalsIgnoreCase("completed");
    }

    /* access modifiers changed from: private */
    public boolean isFailed(AsyncOperationStatus asyncOperationStatus) {
        return asyncOperationStatus.status.equalsIgnoreCase("failed");
    }
}
