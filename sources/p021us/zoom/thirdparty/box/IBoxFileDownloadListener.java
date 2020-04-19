package p021us.zoom.thirdparty.box;

/* renamed from: us.zoom.thirdparty.box.IBoxFileDownloadListener */
public interface IBoxFileDownloadListener {
    void onCancel(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject);

    void onDownloadFileCompeleted(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject, String str);

    void onError(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject, Exception exc);

    void onProgress(BoxAsyncDownloadFile boxAsyncDownloadFile, BoxFileObject boxFileObject, long j, long j2);
}
