package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.stone.StoneSerializers;

/* renamed from: com.dropbox.core.v2.files.UploadSessionStartUploader */
public class UploadSessionStartUploader extends DbxUploader<UploadSessionStartResult, Void, DbxApiException> {
    public UploadSessionStartUploader(Uploader uploader, String str) {
        super(uploader, Serializer.INSTANCE, StoneSerializers.void_(), str);
    }

    /* access modifiers changed from: protected */
    public DbxApiException newException(DbxWrappedException dbxWrappedException) {
        String requestId = dbxWrappedException.getRequestId();
        LocalizedText userMessage = dbxWrappedException.getUserMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected error response for \"upload_session/start\":");
        sb.append(dbxWrappedException.getErrorValue());
        return new DbxApiException(requestId, userMessage, sb.toString());
    }
}
