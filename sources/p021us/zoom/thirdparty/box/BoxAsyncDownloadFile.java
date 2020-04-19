package p021us.zoom.thirdparty.box;

import com.box.androidsdk.content.BoxApiFile;
import com.box.androidsdk.content.listeners.ProgressListener;
import com.box.androidsdk.content.requests.BoxRequestsFile.DownloadFile;
import java.io.File;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;

/* renamed from: us.zoom.thirdparty.box.BoxAsyncDownloadFile */
public class BoxAsyncDownloadFile extends ZMAsyncTask<Void, Long, Runnable> {
    private BoxApiFile mApiFile;
    /* access modifiers changed from: private */
    public Exception mException;
    /* access modifiers changed from: private */
    public BoxFileObject mFile;
    private long mFileSize;
    /* access modifiers changed from: private */
    public IBoxFileDownloadListener mListener;
    /* access modifiers changed from: private */
    public String mOutPath;

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncDownloadFile$onCancelRunnable */
    private class onCancelRunnable implements Runnable {
        private onCancelRunnable() {
        }

        public void run() {
            if (BoxAsyncDownloadFile.this.mListener != null) {
                IBoxFileDownloadListener access$400 = BoxAsyncDownloadFile.this.mListener;
                BoxAsyncDownloadFile boxAsyncDownloadFile = BoxAsyncDownloadFile.this;
                access$400.onCancel(boxAsyncDownloadFile, boxAsyncDownloadFile.mFile);
            }
        }
    }

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncDownloadFile$onCompletedRunnable */
    private class onCompletedRunnable implements Runnable {
        private onCompletedRunnable() {
        }

        public void run() {
            if (BoxAsyncDownloadFile.this.mListener != null) {
                IBoxFileDownloadListener access$400 = BoxAsyncDownloadFile.this.mListener;
                BoxAsyncDownloadFile boxAsyncDownloadFile = BoxAsyncDownloadFile.this;
                access$400.onDownloadFileCompeleted(boxAsyncDownloadFile, boxAsyncDownloadFile.mFile, BoxAsyncDownloadFile.this.mOutPath);
            }
        }
    }

    /* renamed from: us.zoom.thirdparty.box.BoxAsyncDownloadFile$onErrorRunnable */
    private class onErrorRunnable implements Runnable {
        private onErrorRunnable() {
        }

        public void run() {
            if (BoxAsyncDownloadFile.this.mListener != null) {
                IBoxFileDownloadListener access$400 = BoxAsyncDownloadFile.this.mListener;
                BoxAsyncDownloadFile boxAsyncDownloadFile = BoxAsyncDownloadFile.this;
                access$400.onError(boxAsyncDownloadFile, boxAsyncDownloadFile.mFile, BoxAsyncDownloadFile.this.mException);
            }
        }
    }

    public BoxAsyncDownloadFile(BoxApiFile boxApiFile, BoxFileObject boxFileObject, String str, IBoxFileDownloadListener iBoxFileDownloadListener) {
        this.mApiFile = boxApiFile;
        this.mFile = boxFileObject;
        this.mOutPath = str;
        this.mListener = iBoxFileDownloadListener;
        BoxFileObject boxFileObject2 = this.mFile;
        if (boxFileObject2 != null) {
            this.mFileSize = boxFileObject2.getSize();
        }
    }

    /* access modifiers changed from: protected */
    public Runnable doInBackground(Void... voidArr) {
        this.mException = null;
        if (this.mApiFile != null) {
            BoxFileObject boxFileObject = this.mFile;
            if (boxFileObject != null && boxFileObject.isFileObject() && !this.mFile.isDir() && !StringUtil.isEmptyOrNull(this.mOutPath)) {
                File file = new File(this.mOutPath);
                if (isCancelled()) {
                    return new onCancelRunnable();
                }
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    DownloadFile downloadRequest = this.mApiFile.getDownloadRequest(file, this.mFile.getId());
                    downloadRequest.setProgressListener(new ProgressListener() {
                        public void onProgressChanged(long j, long j2) {
                            BoxAsyncDownloadFile.this.publishProgress(Long.valueOf(j));
                        }
                    });
                    downloadRequest.send();
                    if (isCancelled()) {
                        return new onCancelRunnable();
                    }
                    return new onCompletedRunnable();
                } catch (Exception e) {
                    if (isCancelled()) {
                        return new onCancelRunnable();
                    }
                    this.mException = e;
                    return new onErrorRunnable();
                }
            }
        }
        this.mException = null;
        return new onErrorRunnable();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Runnable runnable) {
        runnable.run();
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Long... lArr) {
        long longValue = lArr[0].longValue();
        IBoxFileDownloadListener iBoxFileDownloadListener = this.mListener;
        if (iBoxFileDownloadListener != null) {
            iBoxFileDownloadListener.onProgress(this, this.mFile, this.mFileSize, longValue);
        }
    }
}
