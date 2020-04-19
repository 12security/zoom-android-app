package p021us.zoom.androidlib.util;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import p021us.zoom.androidlib.cache.IoUtils.CopyListener;
import p021us.zoom.androidlib.cache.impl.LimitedAgeDiskCache;

/* renamed from: us.zoom.androidlib.util.ZMDownloadDiscCacheFile */
public class ZMDownloadDiscCacheFile extends ZMAsyncTask<Void, Integer, Runnable> {
    /* access modifiers changed from: private */
    public LimitedAgeDiskCache diskCache;
    /* access modifiers changed from: private */
    public IDownloadFileListener mListener;
    /* access modifiers changed from: private */
    public String mUrl;

    /* renamed from: us.zoom.androidlib.util.ZMDownloadDiscCacheFile$ContentLengthInputStream */
    public class ContentLengthInputStream extends InputStream {
        private final int length;
        private final InputStream stream;

        public ContentLengthInputStream(InputStream inputStream, int i) {
            this.stream = inputStream;
            this.length = i;
        }

        public int available() {
            return this.length;
        }

        public void close() throws IOException {
            this.stream.close();
        }

        public void mark(int i) {
            this.stream.mark(i);
        }

        public int read() throws IOException {
            return this.stream.read();
        }

        public int read(@NonNull byte[] bArr) throws IOException {
            return this.stream.read(bArr);
        }

        public int read(@NonNull byte[] bArr, int i, int i2) throws IOException {
            return this.stream.read(bArr, i, i2);
        }

        public void reset() throws IOException {
            this.stream.reset();
        }

        public long skip(long j) throws IOException {
            return this.stream.skip(j);
        }

        public boolean markSupported() {
            return this.stream.markSupported();
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMDownloadDiscCacheFile$IDownloadFileListener */
    public interface IDownloadFileListener {
        void onDownloadCanceled(ZMDownloadDiscCacheFile zMDownloadDiscCacheFile, String str);

        void onDownloadCompleted(ZMDownloadDiscCacheFile zMDownloadDiscCacheFile, String str, LimitedAgeDiskCache limitedAgeDiskCache);

        void onDownloadFailed(ZMDownloadDiscCacheFile zMDownloadDiscCacheFile, String str);

        void onDownloadProgress(ZMDownloadDiscCacheFile zMDownloadDiscCacheFile, int i, int i2);
    }

    /* renamed from: us.zoom.androidlib.util.ZMDownloadDiscCacheFile$OnErrorRunnable */
    private class OnErrorRunnable implements Runnable {
        private OnErrorRunnable() {
        }

        public void run() {
            if (ZMDownloadDiscCacheFile.this.mListener != null) {
                IDownloadFileListener access$000 = ZMDownloadDiscCacheFile.this.mListener;
                ZMDownloadDiscCacheFile zMDownloadDiscCacheFile = ZMDownloadDiscCacheFile.this;
                access$000.onDownloadFailed(zMDownloadDiscCacheFile, zMDownloadDiscCacheFile.mUrl);
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMDownloadDiscCacheFile$onCanceledRunnable */
    private class onCanceledRunnable implements Runnable {
        private onCanceledRunnable() {
        }

        public void run() {
            if (ZMDownloadDiscCacheFile.this.mListener != null) {
                IDownloadFileListener access$000 = ZMDownloadDiscCacheFile.this.mListener;
                ZMDownloadDiscCacheFile zMDownloadDiscCacheFile = ZMDownloadDiscCacheFile.this;
                access$000.onDownloadCanceled(zMDownloadDiscCacheFile, zMDownloadDiscCacheFile.mUrl);
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.ZMDownloadDiscCacheFile$onCompeletedRunnable */
    private class onCompeletedRunnable implements Runnable {
        private onCompeletedRunnable() {
        }

        public void run() {
            if (ZMDownloadDiscCacheFile.this.mListener != null) {
                IDownloadFileListener access$000 = ZMDownloadDiscCacheFile.this.mListener;
                ZMDownloadDiscCacheFile zMDownloadDiscCacheFile = ZMDownloadDiscCacheFile.this;
                access$000.onDownloadCompleted(zMDownloadDiscCacheFile, zMDownloadDiscCacheFile.mUrl, ZMDownloadDiscCacheFile.this.diskCache);
            }
        }
    }

    public ZMDownloadDiscCacheFile(IDownloadFileListener iDownloadFileListener, String str, LimitedAgeDiskCache limitedAgeDiskCache) {
        this.mListener = iDownloadFileListener;
        this.mUrl = str;
        this.diskCache = limitedAgeDiskCache;
    }

    /* access modifiers changed from: protected */
    public Runnable doInBackground(Void... voidArr) {
        if (TextUtils.isEmpty(this.mUrl) || this.diskCache == null) {
            return new OnErrorRunnable();
        }
        if (isCancelled()) {
            return new onCanceledRunnable();
        }
        try {
            URLConnection openConnection = new URL(this.mUrl).openConnection();
            this.diskCache.save(this.mUrl, new ContentLengthInputStream(openConnection.getInputStream(), openConnection.getContentLength()), new CopyListener() {
                public boolean onBytesCopied(int i, int i2) {
                    return ZMDownloadDiscCacheFile.this.fireProgressEvent(i, i2);
                }
            });
            if (isCancelled()) {
                return new onCanceledRunnable();
            }
            return new onCompeletedRunnable();
        } catch (Exception unused) {
            return new OnErrorRunnable();
        }
    }

    /* access modifiers changed from: private */
    public boolean fireProgressEvent(int i, int i2) {
        if (isTaskInterrupted()) {
            return false;
        }
        publishProgress(Integer.valueOf(i), Integer.valueOf(i2));
        return true;
    }

    private boolean isTaskInterrupted() {
        return Thread.interrupted();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Runnable runnable) {
        runnable.run();
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Integer... numArr) {
        int intValue = numArr[1].intValue();
        int intValue2 = numArr[0].intValue();
        IDownloadFileListener iDownloadFileListener = this.mListener;
        if (iDownloadFileListener != null) {
            iDownloadFileListener.onDownloadProgress(this, intValue, intValue2);
        }
    }
}
