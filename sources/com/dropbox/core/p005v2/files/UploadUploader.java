package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;

/* renamed from: com.dropbox.core.v2.files.UploadUploader */
public class UploadUploader extends DbxUploader<FileMetadata, UploadError, UploadErrorException> {
    public UploadUploader(Uploader uploader, String str) {
        super(uploader, Serializer.INSTANCE, Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public UploadErrorException newException(DbxWrappedException dbxWrappedException) {
        return new UploadErrorException("2/files/upload", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (UploadError) dbxWrappedException.getErrorValue());
    }
}
