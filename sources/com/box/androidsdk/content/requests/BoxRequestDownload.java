package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.listeners.DownloadStartListener;
import com.box.androidsdk.content.listeners.ProgressListener;
import com.box.androidsdk.content.models.BoxDownload;
import com.box.androidsdk.content.models.BoxObject;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.BoxRequestHandler;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.BoxLogUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public abstract class BoxRequestDownload<E extends BoxObject, R extends BoxRequest<E, R>> extends BoxRequest<E, R> {
    private static final String QUERY_VERSION = "version";
    DownloadStartListener mDownloadStartListener;
    OutputStream mFileOutputStream;
    long mRangeEnd;
    long mRangeStart;
    File mTarget;

    public static class DownloadRequestHandler extends BoxRequestHandler<BoxRequestDownload> {
        protected static final int DEFAULT_MAX_WAIT_MILLIS = 90000;
        protected static final int DEFAULT_NUM_RETRIES = 2;
        protected int mNumAcceptedRetries = 0;
        protected int mRetryAfterMillis = 1000;

        public DownloadRequestHandler(BoxRequestDownload boxRequestDownload) {
            super(boxRequestDownload);
        }

        /* access modifiers changed from: protected */
        public OutputStream getOutputStream(BoxDownload boxDownload) throws FileNotFoundException, IOException {
            if (((BoxRequestDownload) this.mRequest).mFileOutputStream != null) {
                return ((BoxRequestDownload) this.mRequest).mFileOutputStream;
            }
            if (!boxDownload.getOutputFile().exists()) {
                boxDownload.getOutputFile().createNewFile();
            }
            return new FileOutputStream(boxDownload.getOutputFile());
        }

        /* JADX WARNING: Removed duplicated region for block: B:63:0x0148 A[SYNTHETIC, Splitter:B:63:0x0148] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.box.androidsdk.content.models.BoxDownload onResponse(java.lang.Class r20, com.box.androidsdk.content.requests.BoxHttpResponse r21) throws java.lang.IllegalAccessException, java.lang.InstantiationException, com.box.androidsdk.content.BoxException {
            /*
                r19 = this;
                r10 = r19
                r11 = r21
                java.lang.String r6 = r21.getContentType()
                int r0 = r21.getResponseCode()
                r1 = 429(0x1ad, float:6.01E-43)
                if (r0 != r1) goto L_0x0017
                com.box.androidsdk.content.models.BoxObject r0 = r10.retryRateLimited(r11)
                com.box.androidsdk.content.models.BoxDownload r0 = (com.box.androidsdk.content.models.BoxDownload) r0
                return r0
            L_0x0017:
                int r0 = r21.getResponseCode()
                r1 = 202(0xca, float:2.83E-43)
                if (r0 != r1) goto L_0x0072
                int r0 = r10.mNumAcceptedRetries     // Catch:{ InterruptedException -> 0x0067 }
                r1 = 2
                if (r0 >= r1) goto L_0x0031
                int r0 = r10.mNumAcceptedRetries     // Catch:{ InterruptedException -> 0x0067 }
                r1 = 1
                int r0 = r0 + r1
                r10.mNumAcceptedRetries = r0     // Catch:{ InterruptedException -> 0x0067 }
                int r0 = getRetryAfterFromResponse(r11, r1)     // Catch:{ InterruptedException -> 0x0067 }
                r10.mRetryAfterMillis = r0     // Catch:{ InterruptedException -> 0x0067 }
                goto L_0x004c
            L_0x0031:
                int r0 = r10.mRetryAfterMillis     // Catch:{ InterruptedException -> 0x0067 }
                r1 = 90000(0x15f90, float:1.26117E-40)
                if (r0 >= r1) goto L_0x005d
                int r0 = r10.mRetryAfterMillis     // Catch:{ InterruptedException -> 0x0067 }
                double r0 = (double) r0     // Catch:{ InterruptedException -> 0x0067 }
                r2 = 4609434218613702656(0x3ff8000000000000, double:1.5)
                java.security.SecureRandom r4 = new java.security.SecureRandom     // Catch:{ InterruptedException -> 0x0067 }
                r4.<init>()     // Catch:{ InterruptedException -> 0x0067 }
                double r4 = r4.nextDouble()     // Catch:{ InterruptedException -> 0x0067 }
                double r4 = r4 + r2
                double r0 = r0 * r4
                int r0 = (int) r0     // Catch:{ InterruptedException -> 0x0067 }
                r10.mRetryAfterMillis = r0     // Catch:{ InterruptedException -> 0x0067 }
            L_0x004c:
                int r0 = r10.mRetryAfterMillis     // Catch:{ InterruptedException -> 0x0067 }
                long r0 = (long) r0     // Catch:{ InterruptedException -> 0x0067 }
                java.lang.Thread.sleep(r0)     // Catch:{ InterruptedException -> 0x0067 }
                com.box.androidsdk.content.requests.BoxRequest r0 = r10.mRequest     // Catch:{ InterruptedException -> 0x0067 }
                com.box.androidsdk.content.requests.BoxRequestDownload r0 = (com.box.androidsdk.content.requests.BoxRequestDownload) r0     // Catch:{ InterruptedException -> 0x0067 }
                com.box.androidsdk.content.models.BoxObject r0 = r0.send()     // Catch:{ InterruptedException -> 0x0067 }
                com.box.androidsdk.content.models.BoxDownload r0 = (com.box.androidsdk.content.models.BoxDownload) r0     // Catch:{ InterruptedException -> 0x0067 }
                return r0
            L_0x005d:
                com.box.androidsdk.content.BoxException$MaxAttemptsExceeded r0 = new com.box.androidsdk.content.BoxException$MaxAttemptsExceeded     // Catch:{ InterruptedException -> 0x0067 }
                java.lang.String r1 = "Max wait time exceeded."
                int r2 = r10.mNumAcceptedRetries     // Catch:{ InterruptedException -> 0x0067 }
                r0.<init>(r1, r2)     // Catch:{ InterruptedException -> 0x0067 }
                throw r0     // Catch:{ InterruptedException -> 0x0067 }
            L_0x0067:
                r0 = move-exception
                com.box.androidsdk.content.BoxException r1 = new com.box.androidsdk.content.BoxException
                java.lang.String r0 = r0.getMessage()
                r1.<init>(r0, r11)
                throw r1
            L_0x0072:
                int r0 = r21.getResponseCode()
                r1 = 200(0xc8, float:2.8E-43)
                if (r0 == r1) goto L_0x0094
                int r0 = r21.getResponseCode()
                r1 = 206(0xce, float:2.89E-43)
                if (r0 != r1) goto L_0x0083
                goto L_0x0094
            L_0x0083:
                com.box.androidsdk.content.models.BoxDownload r0 = new com.box.androidsdk.content.models.BoxDownload
                r12 = 0
                r13 = 0
                r15 = 0
                r16 = 0
                r17 = 0
                r18 = 0
                r11 = r0
                r11.<init>(r12, r13, r15, r16, r17, r18)
                return r0
            L_0x0094:
                java.net.HttpURLConnection r0 = r21.getHttpURLConnection()
                java.lang.String r1 = "Content-Length"
                java.lang.String r0 = r0.getHeaderField(r1)
                java.net.HttpURLConnection r1 = r21.getHttpURLConnection()
                java.lang.String r2 = "Content-Disposition"
                java.lang.String r3 = r1.getHeaderField(r2)
                long r0 = java.lang.Long.parseLong(r0)     // Catch:{ Exception -> 0x00ae }
                r12 = r0
                goto L_0x00b1
            L_0x00ae:
                r0 = -1
                r12 = r0
            L_0x00b1:
                java.net.HttpURLConnection r0 = r21.getHttpURLConnection()
                java.lang.String r1 = "Content-Range"
                java.lang.String r7 = r0.getHeaderField(r1)
                java.net.HttpURLConnection r0 = r21.getHttpURLConnection()
                java.lang.String r1 = "Date"
                java.lang.String r8 = r0.getHeaderField(r1)
                java.net.HttpURLConnection r0 = r21.getHttpURLConnection()
                java.lang.String r1 = "Expiration"
                java.lang.String r9 = r0.getHeaderField(r1)
                com.box.androidsdk.content.requests.BoxRequestDownload$DownloadRequestHandler$1 r0 = new com.box.androidsdk.content.requests.BoxRequestDownload$DownloadRequestHandler$1
                r1 = r0
                r2 = r19
                r4 = r12
                r1.<init>(r3, r4, r6, r7, r8, r9)
                com.box.androidsdk.content.requests.BoxRequest r1 = r10.mRequest
                com.box.androidsdk.content.requests.BoxRequestDownload r1 = (com.box.androidsdk.content.requests.BoxRequestDownload) r1
                com.box.androidsdk.content.listeners.DownloadStartListener r1 = r1.mDownloadStartListener
                if (r1 == 0) goto L_0x00e9
                com.box.androidsdk.content.requests.BoxRequest r1 = r10.mRequest
                com.box.androidsdk.content.requests.BoxRequestDownload r1 = (com.box.androidsdk.content.requests.BoxRequestDownload) r1
                com.box.androidsdk.content.listeners.DownloadStartListener r1 = r1.mDownloadStartListener
                r1.onStart(r0)
            L_0x00e9:
                r1 = 0
                com.box.androidsdk.content.requests.BoxRequest r2 = r10.mRequest     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.requests.BoxRequestDownload r2 = (com.box.androidsdk.content.requests.BoxRequestDownload) r2     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.listeners.ProgressListener r2 = r2.mListener     // Catch:{ Exception -> 0x0133 }
                if (r2 == 0) goto L_0x0114
                com.box.androidsdk.content.utils.ProgressOutputStream r2 = new com.box.androidsdk.content.utils.ProgressOutputStream     // Catch:{ Exception -> 0x0133 }
                java.io.OutputStream r3 = r10.getOutputStream(r0)     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.requests.BoxRequest r4 = r10.mRequest     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.requests.BoxRequestDownload r4 = (com.box.androidsdk.content.requests.BoxRequestDownload) r4     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.listeners.ProgressListener r4 = r4.mListener     // Catch:{ Exception -> 0x0133 }
                r2.<init>(r3, r4, r12)     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.requests.BoxRequest r1 = r10.mRequest     // Catch:{ Exception -> 0x0111, all -> 0x010e }
                com.box.androidsdk.content.requests.BoxRequestDownload r1 = (com.box.androidsdk.content.requests.BoxRequestDownload) r1     // Catch:{ Exception -> 0x0111, all -> 0x010e }
                com.box.androidsdk.content.listeners.ProgressListener r1 = r1.mListener     // Catch:{ Exception -> 0x0111, all -> 0x010e }
                r3 = 0
                r1.onProgressChanged(r3, r12)     // Catch:{ Exception -> 0x0111, all -> 0x010e }
                r1 = r2
                goto L_0x0118
            L_0x010e:
                r0 = move-exception
                r1 = r2
                goto L_0x013e
            L_0x0111:
                r0 = move-exception
                r1 = r2
                goto L_0x0134
            L_0x0114:
                java.io.OutputStream r1 = r10.getOutputStream(r0)     // Catch:{ Exception -> 0x0133 }
            L_0x0118:
                java.net.HttpURLConnection r2 = r21.getHttpURLConnection()     // Catch:{ Exception -> 0x0133 }
                java.io.InputStream r2 = r2.getInputStream()     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.utils.SdkUtils.copyStream(r2, r1)     // Catch:{ Exception -> 0x0133 }
                com.box.androidsdk.content.requests.BoxRequest r2 = r10.mRequest
                com.box.androidsdk.content.requests.BoxRequestDownload r2 = (com.box.androidsdk.content.requests.BoxRequestDownload) r2
                java.io.OutputStream r2 = r2.getTargetStream()
                if (r2 != 0) goto L_0x0130
                r1.close()     // Catch:{ IOException -> 0x0130 }
            L_0x0130:
                return r0
            L_0x0131:
                r0 = move-exception
                goto L_0x013e
            L_0x0133:
                r0 = move-exception
            L_0x0134:
                com.box.androidsdk.content.BoxException r2 = new com.box.androidsdk.content.BoxException     // Catch:{ all -> 0x0131 }
                java.lang.String r3 = r0.getMessage()     // Catch:{ all -> 0x0131 }
                r2.<init>(r3, r0)     // Catch:{ all -> 0x0131 }
                throw r2     // Catch:{ all -> 0x0131 }
            L_0x013e:
                com.box.androidsdk.content.requests.BoxRequest r2 = r10.mRequest
                com.box.androidsdk.content.requests.BoxRequestDownload r2 = (com.box.androidsdk.content.requests.BoxRequestDownload) r2
                java.io.OutputStream r2 = r2.getTargetStream()
                if (r2 != 0) goto L_0x014b
                r1.close()     // Catch:{ IOException -> 0x014b }
            L_0x014b:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.box.androidsdk.content.requests.BoxRequestDownload.DownloadRequestHandler.onResponse(java.lang.Class, com.box.androidsdk.content.requests.BoxHttpResponse):com.box.androidsdk.content.models.BoxDownload");
        }
    }

    public BoxRequestDownload(Class<E> cls, OutputStream outputStream, String str, BoxSession boxSession) {
        super(cls, str, boxSession);
        this.mRangeStart = -1;
        this.mRangeEnd = -1;
        this.mRequestMethod = Methods.GET;
        this.mRequestUrlString = str;
        this.mFileOutputStream = outputStream;
        setRequestHandler(new DownloadRequestHandler(this));
    }

    public BoxRequestDownload(Class<E> cls, File file, String str, BoxSession boxSession) {
        super(cls, str, boxSession);
        this.mRangeStart = -1;
        this.mRangeEnd = -1;
        this.mRequestMethod = Methods.GET;
        this.mRequestUrlString = str;
        this.mTarget = file;
        setRequestHandler(new DownloadRequestHandler(this));
    }

    /* access modifiers changed from: protected */
    public void setHeaders(BoxHttpRequest boxHttpRequest) {
        super.setHeaders(boxHttpRequest);
        long j = this.mRangeStart;
        if (j != -1 && this.mRangeEnd != -1) {
            boxHttpRequest.addHeader("Range", String.format("bytes=%s-%s", new Object[]{Long.toString(j), Long.toString(this.mRangeEnd)}));
        }
    }

    /* access modifiers changed from: protected */
    public void logDebug(BoxHttpResponse boxHttpResponse) throws BoxException {
        logRequest();
        BoxLogUtils.m13i(BoxConstants.TAG, String.format(Locale.ENGLISH, "Response (%s)", new Object[]{Integer.valueOf(boxHttpResponse.getResponseCode())}));
    }

    public File getTarget() {
        return this.mTarget;
    }

    public OutputStream getTargetStream() {
        return this.mFileOutputStream;
    }

    public long getRangeStart() {
        return this.mRangeStart;
    }

    public long getRangeEnd() {
        return this.mRangeEnd;
    }

    public R setRange(long j, long j2) {
        this.mRangeStart = j;
        this.mRangeEnd = j2;
        return this;
    }

    public R setVersion(String str) {
        this.mQueryMap.put("version", str);
        return this;
    }

    public String getVersion() {
        return (String) this.mQueryMap.get("version");
    }

    public R setProgressListener(ProgressListener progressListener) {
        this.mListener = progressListener;
        return this;
    }

    public R setDownloadStartListener(DownloadStartListener downloadStartListener) {
        this.mDownloadStartListener = downloadStartListener;
        return this;
    }
}
