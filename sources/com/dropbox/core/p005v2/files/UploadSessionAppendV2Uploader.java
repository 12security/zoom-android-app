package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.stone.StoneSerializers;

/* renamed from: com.dropbox.core.v2.files.UploadSessionAppendV2Uploader */
public class UploadSessionAppendV2Uploader extends DbxUploader<Void, UploadSessionLookupError, UploadSessionLookupErrorException> {
    public UploadSessionAppendV2Uploader(Uploader uploader, String str) {
        super(uploader, StoneSerializers.void_(), Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public UploadSessionLookupErrorException newException(DbxWrappedException dbxWrappedException) {
        return new UploadSessionLookupErrorException("2/files/upload_session/append_v2", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (UploadSessionLookupError) dbxWrappedException.getErrorValue());
    }
}
