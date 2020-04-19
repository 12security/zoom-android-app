package p021us.zoom.androidlib.util;

import android.net.Uri;

/* renamed from: us.zoom.androidlib.util.IDownloadFileListener */
public interface IDownloadFileListener {
    void onDownloadCanceled(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri);

    void onDownloadCompleted(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri, String str);

    void onDownloadFailed(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, Uri uri);

    void onDownloadProgress(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, long j, long j2);
}
