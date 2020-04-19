package p021us.zoom.androidlib.util;

import android.net.Uri;
import java.io.Closeable;

/* renamed from: us.zoom.androidlib.util.ZMAsyncURLDownloadFile */
public class ZMAsyncURLDownloadFile extends ZMAsyncTask<Void, Long, Runnable> {
    private static int BUFFER_SIZE = 1024;
    /* access modifiers changed from: private */
    public Uri mInput;
    /* access modifiers changed from: private */
    public IDownloadFileListener mListener;
    /* access modifiers changed from: private */
    public String mOutput;
    private long mReadBytes = 0;
    private long mTotalBytes;

    /* renamed from: us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable */
    private class OnErrorRunnable implements Runnable {
        private OnErrorRunnable() {
        }

        public void run() {
            if (ZMAsyncURLDownloadFile.this.mListener != null) {
                IDownloadFileListener access$000 = ZMAsyncURLDownloadFile.this.mListener;
                ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = ZMAsyncURLDownloadFile.this;
                access$000.onDownloadFailed(zMAsyncURLDownloadFile, zMAsyncURLDownloadFile.mInput);
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable */
    private class onCanceledRunnable implements Runnable {
        private onCanceledRunnable() {
        }

        public void run() {
            if (ZMAsyncURLDownloadFile.this.mListener != null) {
                IDownloadFileListener access$000 = ZMAsyncURLDownloadFile.this.mListener;
                ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = ZMAsyncURLDownloadFile.this;
                access$000.onDownloadCanceled(zMAsyncURLDownloadFile, zMAsyncURLDownloadFile.mInput);
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCompeletedRunnable */
    private class onCompeletedRunnable implements Runnable {
        private onCompeletedRunnable() {
        }

        public void run() {
            if (ZMAsyncURLDownloadFile.this.mListener != null) {
                IDownloadFileListener access$000 = ZMAsyncURLDownloadFile.this.mListener;
                ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = ZMAsyncURLDownloadFile.this;
                access$000.onDownloadCompleted(zMAsyncURLDownloadFile, zMAsyncURLDownloadFile.mInput, ZMAsyncURLDownloadFile.this.mOutput);
            }
        }
    }

    public ZMAsyncURLDownloadFile(Uri uri, long j, String str, IDownloadFileListener iDownloadFileListener) {
        this.mInput = uri;
        this.mOutput = str;
        this.mListener = iDownloadFileListener;
        this.mTotalBytes = j;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004b, code lost:
        r2 = new p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile.onCanceledRunnable(r10, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0053, code lost:
        if (r11 == null) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        r11.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0058, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007e, code lost:
        if (r11 == null) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r11.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0087, code lost:
        if (isCancelled() == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x008e, code lost:
        return new p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile.onCanceledRunnable(r10, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0094, code lost:
        return new p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile.onCompeletedRunnable(r10, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ad, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00ae, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00b2, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00b3, code lost:
        r9 = r2;
        r2 = r1;
        r1 = r9;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00ad A[ExcHandler: all (th java.lang.Throwable), Splitter:B:12:0x002e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Runnable doInBackground(java.lang.Void... r11) {
        /*
            r10 = this;
            android.net.Uri r11 = r10.mInput
            r0 = 0
            if (r11 == 0) goto L_0x00d3
            java.lang.String r11 = r10.mOutput
            boolean r11 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)
            if (r11 == 0) goto L_0x000f
            goto L_0x00d3
        L_0x000f:
            boolean r11 = r10.isCancelled()
            if (r11 == 0) goto L_0x001b
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable r11 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable
            r11.<init>()
            return r11
        L_0x001b:
            java.net.URL r11 = new java.net.URL     // Catch:{ MalformedURLException -> 0x00cd }
            android.net.Uri r1 = r10.mInput     // Catch:{ MalformedURLException -> 0x00cd }
            java.lang.String r1 = r1.toString()     // Catch:{ MalformedURLException -> 0x00cd }
            r11.<init>(r1)     // Catch:{ MalformedURLException -> 0x00cd }
            java.net.URLConnection r11 = r11.openConnection()     // Catch:{ Exception -> 0x00c7 }
            java.io.InputStream r11 = r11.getInputStream()     // Catch:{ Exception -> 0x00c7 }
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            java.lang.String r3 = r10.mOutput     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            int r2 = BUFFER_SIZE     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            byte[] r2 = new byte[r2]     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
        L_0x003e:
            int r3 = r11.read(r2)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r4 = -1
            if (r3 == r4) goto L_0x007b
            boolean r4 = r10.isCancelled()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            if (r4 == 0) goto L_0x0059
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable r2 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r2.<init>()     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r1.close()     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            if (r11 == 0) goto L_0x0058
            r11.close()     // Catch:{ Exception -> 0x00c7 }
        L_0x0058:
            return r2
        L_0x0059:
            r4 = 0
            r1.write(r2, r4, r3)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            long r5 = r10.mReadBytes     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            long r7 = (long) r3     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            long r5 = r5 + r7
            r10.mReadBytes = r5     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r3 = 2
            java.lang.Long[] r3 = new java.lang.Long[r3]     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            long r5 = r10.mTotalBytes     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r4 = 1
            long r5 = r10.mReadBytes     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            r10.publishProgress(r3)     // Catch:{ Throwable -> 0x0098, all -> 0x0095 }
            goto L_0x003e
        L_0x007b:
            r1.close()     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            if (r11 == 0) goto L_0x0083
            r11.close()     // Catch:{ Exception -> 0x00c7 }
        L_0x0083:
            boolean r11 = r10.isCancelled()
            if (r11 == 0) goto L_0x008f
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable r11 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCanceledRunnable
            r11.<init>()
            return r11
        L_0x008f:
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCompeletedRunnable r11 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$onCompeletedRunnable
            r11.<init>()
            return r11
        L_0x0095:
            r2 = move-exception
            r3 = r0
            goto L_0x009e
        L_0x0098:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x009a }
        L_0x009a:
            r3 = move-exception
            r9 = r3
            r3 = r2
            r2 = r9
        L_0x009e:
            if (r3 == 0) goto L_0x00a9
            r1.close()     // Catch:{ Throwable -> 0x00a4, all -> 0x00ad }
            goto L_0x00ac
        L_0x00a4:
            r1 = move-exception
            r3.addSuppressed(r1)     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
            goto L_0x00ac
        L_0x00a9:
            r1.close()     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
        L_0x00ac:
            throw r2     // Catch:{ Throwable -> 0x00b0, all -> 0x00ad }
        L_0x00ad:
            r1 = move-exception
            r2 = r0
            goto L_0x00b6
        L_0x00b0:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x00b2 }
        L_0x00b2:
            r2 = move-exception
            r9 = r2
            r2 = r1
            r1 = r9
        L_0x00b6:
            if (r11 == 0) goto L_0x00c6
            if (r2 == 0) goto L_0x00c3
            r11.close()     // Catch:{ Throwable -> 0x00be }
            goto L_0x00c6
        L_0x00be:
            r11 = move-exception
            r2.addSuppressed(r11)     // Catch:{ Exception -> 0x00c7 }
            goto L_0x00c6
        L_0x00c3:
            r11.close()     // Catch:{ Exception -> 0x00c7 }
        L_0x00c6:
            throw r1     // Catch:{ Exception -> 0x00c7 }
        L_0x00c7:
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable r11 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable
            r11.<init>()
            return r11
        L_0x00cd:
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable r11 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable
            r11.<init>()
            return r11
        L_0x00d3:
            us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable r11 = new us.zoom.androidlib.util.ZMAsyncURLDownloadFile$OnErrorRunnable
            r11.<init>()
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile.doInBackground(java.lang.Void[]):java.lang.Runnable");
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Runnable runnable) {
        runnable.run();
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Long... lArr) {
        long longValue = lArr[0].longValue();
        long longValue2 = lArr[1].longValue();
        IDownloadFileListener iDownloadFileListener = this.mListener;
        if (iDownloadFileListener != null) {
            iDownloadFileListener.onDownloadProgress(this, longValue, longValue2);
        }
    }

    public String getmOutput() {
        return this.mOutput;
    }

    private void closeSilently(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception unused) {
        }
    }
}
