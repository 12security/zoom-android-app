package com.box.androidsdk.content.utils;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.BoxFutureTask.OnCompletedListener;
import com.box.androidsdk.content.models.BoxRealTimeServer;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxSimpleMessage;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxResponse;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import p021us.zoom.androidlib.util.TimeUtil;

public class RealTimeServerConnection implements OnCompletedListener<BoxSimpleMessage> {
    private BoxRealTimeServer mBoxRealTimeServer;
    private final OnChangeListener mChangeListener;
    private final ThreadPoolExecutor mExecutor = SdkUtils.createDefaultThreadPoolExecutor(1, 1, TimeUtil.ONE_HOUR_IN_SECONDS, TimeUnit.SECONDS);
    private BoxRequest mRequest;
    private int mRetries = 0;
    private BoxSession mSession;

    public interface OnChangeListener {
        void onChange(BoxSimpleMessage boxSimpleMessage, RealTimeServerConnection realTimeServerConnection);

        void onException(Exception exc, RealTimeServerConnection realTimeServerConnection);
    }

    public RealTimeServerConnection(BoxRequest boxRequest, OnChangeListener onChangeListener, BoxSession boxSession) {
        this.mRequest = boxRequest;
        this.mSession = boxSession;
        this.mChangeListener = onChangeListener;
    }

    public BoxRequest getRequest() {
        return this.mRequest;
    }

    public int getTimesRetried() {
        return this.mRetries;
    }

    public BoxRealTimeServer getRealTimeServer() {
        return this.mBoxRealTimeServer;
    }

    public FutureTask<BoxSimpleMessage> toTask() {
        return new FutureTask<>(new Callable<BoxSimpleMessage>() {
            public BoxSimpleMessage call() throws Exception {
                return RealTimeServerConnection.this.connect();
            }
        });
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0073, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0074, code lost:
        r9.mChangeListener.onException(r5, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x007a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x007b, code lost:
        r9.mChangeListener.onException(r5, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r5.cancel(true);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0073 A[ExcHandler: ExecutionException (r5v9 'e' java.util.concurrent.ExecutionException A[CUSTOM_DECLARE]), Splitter:B:4:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x007a A[ExcHandler: InterruptedException (r5v8 'e' java.lang.InterruptedException A[CUSTOM_DECLARE]), Splitter:B:4:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0084 A[SYNTHETIC, Splitter:B:21:0x0084] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.box.androidsdk.content.models.BoxSimpleMessage connect() {
        /*
            r9 = this;
            r0 = 0
            r9.mRetries = r0
            r1 = 0
            com.box.androidsdk.content.requests.BoxRequest r2 = r9.mRequest     // Catch:{ BoxException -> 0x00b1 }
            com.box.androidsdk.content.models.BoxObject r2 = r2.send()     // Catch:{ BoxException -> 0x00b1 }
            com.box.androidsdk.content.models.BoxListRealTimeServers r2 = (com.box.androidsdk.content.models.BoxListRealTimeServers) r2     // Catch:{ BoxException -> 0x00b1 }
            com.box.androidsdk.content.models.BoxJsonObject r2 = r2.get(r0)     // Catch:{ BoxException -> 0x00b1 }
            com.box.androidsdk.content.models.BoxRealTimeServer r2 = (com.box.androidsdk.content.models.BoxRealTimeServer) r2     // Catch:{ BoxException -> 0x00b1 }
            r9.mBoxRealTimeServer = r2     // Catch:{ BoxException -> 0x00b1 }
            com.box.androidsdk.content.requests.BoxRequestsEvent$LongPollMessageRequest r2 = new com.box.androidsdk.content.requests.BoxRequestsEvent$LongPollMessageRequest
            com.box.androidsdk.content.models.BoxRealTimeServer r3 = r9.mBoxRealTimeServer
            java.lang.String r3 = r3.getUrl()
            com.box.androidsdk.content.models.BoxSession r4 = r9.mSession
            r2.<init>(r3, r4)
            com.box.androidsdk.content.models.BoxRealTimeServer r3 = r9.mBoxRealTimeServer
            java.lang.Long r3 = r3.getFieldRetryTimeout()
            int r3 = r3.intValue()
            int r3 = r3 * 1000
            r2.setTimeOut(r3)
            r3 = 1
            r4 = 1
        L_0x0032:
            com.box.androidsdk.content.BoxFutureTask r5 = r2.toTask()     // Catch:{ TimeoutException -> 0x0081, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            com.box.androidsdk.content.BoxFutureTask r5 = r5.addOnCompletedListener(r9)     // Catch:{ TimeoutException -> 0x0081, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            java.util.concurrent.ThreadPoolExecutor r6 = r9.mExecutor     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            r6.submit(r5)     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            com.box.androidsdk.content.models.BoxRealTimeServer r6 = r9.mBoxRealTimeServer     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            java.lang.Long r6 = r6.getFieldRetryTimeout()     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            int r6 = r6.intValue()     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            long r6 = (long) r6     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            java.util.concurrent.TimeUnit r8 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            java.lang.Object r6 = r5.get(r6, r8)     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            com.box.androidsdk.content.requests.BoxResponse r6 = (com.box.androidsdk.content.requests.BoxResponse) r6     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            boolean r7 = r6.isSuccess()     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            if (r7 == 0) goto L_0x0089
            com.box.androidsdk.content.models.BoxObject r7 = r6.getResult()     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            com.box.androidsdk.content.models.BoxSimpleMessage r7 = (com.box.androidsdk.content.models.BoxSimpleMessage) r7     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            java.lang.String r7 = r7.getMessage()     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            java.lang.String r8 = "reconnect"
            boolean r7 = r7.equals(r8)     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            if (r7 != 0) goto L_0x0089
            com.box.androidsdk.content.models.BoxObject r6 = r6.getResult()     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            com.box.androidsdk.content.models.BoxSimpleMessage r6 = (com.box.androidsdk.content.models.BoxSimpleMessage) r6     // Catch:{ TimeoutException -> 0x0071, InterruptedException -> 0x007a, ExecutionException -> 0x0073 }
            return r6
        L_0x0071:
            goto L_0x0082
        L_0x0073:
            r5 = move-exception
            com.box.androidsdk.content.utils.RealTimeServerConnection$OnChangeListener r6 = r9.mChangeListener
            r6.onException(r5, r9)
            goto L_0x0089
        L_0x007a:
            r5 = move-exception
            com.box.androidsdk.content.utils.RealTimeServerConnection$OnChangeListener r6 = r9.mChangeListener
            r6.onException(r5, r9)
            goto L_0x0089
        L_0x0081:
            r5 = r1
        L_0x0082:
            if (r5 == 0) goto L_0x0089
            r5.cancel(r3)     // Catch:{ CancellationException -> 0x0088 }
            goto L_0x0089
        L_0x0088:
        L_0x0089:
            int r5 = r9.mRetries
            int r5 = r5 + r3
            r9.mRetries = r5
            com.box.androidsdk.content.models.BoxRealTimeServer r5 = r9.mBoxRealTimeServer
            java.lang.Long r5 = r5.getMaxRetries()
            long r5 = r5.longValue()
            int r7 = r9.mRetries
            long r7 = (long) r7
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 >= 0) goto L_0x00a0
            r4 = 0
        L_0x00a0:
            if (r4 != 0) goto L_0x0032
            com.box.androidsdk.content.utils.RealTimeServerConnection$OnChangeListener r0 = r9.mChangeListener
            com.box.androidsdk.content.BoxException$MaxAttemptsExceeded r2 = new com.box.androidsdk.content.BoxException$MaxAttemptsExceeded
            java.lang.String r3 = "Max retries exceeded, "
            int r4 = r9.mRetries
            r2.<init>(r3, r4)
            r0.onException(r2, r9)
            return r1
        L_0x00b1:
            r0 = move-exception
            com.box.androidsdk.content.utils.RealTimeServerConnection$OnChangeListener r2 = r9.mChangeListener
            r2.onException(r0, r9)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.utils.RealTimeServerConnection.connect():com.box.androidsdk.content.models.BoxSimpleMessage");
    }

    /* access modifiers changed from: protected */
    public void handleResponse(BoxResponse<BoxSimpleMessage> boxResponse) {
        if (boxResponse.isSuccess()) {
            if (!((BoxSimpleMessage) boxResponse.getResult()).getMessage().equals(BoxSimpleMessage.MESSAGE_RECONNECT)) {
                this.mChangeListener.onChange((BoxSimpleMessage) boxResponse.getResult(), this);
            }
        } else if (!(boxResponse.getException() instanceof BoxException) || !(boxResponse.getException().getCause() instanceof SocketTimeoutException)) {
            this.mChangeListener.onException(boxResponse.getException(), this);
        }
    }

    public void onCompleted(BoxResponse<BoxSimpleMessage> boxResponse) {
        handleResponse(boxResponse);
    }
}
