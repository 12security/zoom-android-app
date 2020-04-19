package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.stone.StoneSerializers;

/* renamed from: com.dropbox.core.v2.files.UploadSessionAppendUploader */
public class UploadSessionAppendUploader extends DbxUploader<Void, UploadSessionLookupError, UploadSessionLookupErrorException> {
    public UploadSessionAppendUploader(Uploader uploader, String str) {
        super(uploader, StoneSerializers.void_(), Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public UploadSessionLookupErrorException newException(DbxWrappedException dbxWrappedException) {
        return new UploadSessionLookupErrorException("2/files/upload_session/append", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (UploadSessionLookupError) dbxWrappedException.getErrorValue());
    }
}
