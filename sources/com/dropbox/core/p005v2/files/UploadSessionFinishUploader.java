package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishUploader */
public class UploadSessionFinishUploader extends DbxUploader<FileMetadata, UploadSessionFinishError, UploadSessionFinishErrorException> {
    public UploadSessionFinishUploader(Uploader uploader, String str) {
        super(uploader, Serializer.INSTANCE, Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public UploadSessionFinishErrorException newException(DbxWrappedException dbxWrappedException) {
        return new UploadSessionFinishErrorException("2/files/upload_session/finish", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (UploadSessionFinishError) dbxWrappedException.getErrorValue());
    }
}
