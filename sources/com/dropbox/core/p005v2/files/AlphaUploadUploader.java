package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;

/* renamed from: com.dropbox.core.v2.files.AlphaUploadUploader */
public class AlphaUploadUploader extends DbxUploader<FileMetadata, UploadErrorWithProperties, UploadErrorWithPropertiesException> {
    public AlphaUploadUploader(Uploader uploader, String str) {
        super(uploader, Serializer.INSTANCE, Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public UploadErrorWithPropertiesException newException(DbxWrappedException dbxWrappedException) {
        return new UploadErrorWithPropertiesException("2/files/alpha/upload", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (UploadErrorWithProperties) dbxWrappedException.getErrorValue());
    }
}
