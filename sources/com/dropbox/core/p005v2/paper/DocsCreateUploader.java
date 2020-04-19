package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;

/* renamed from: com.dropbox.core.v2.paper.DocsCreateUploader */
public class DocsCreateUploader extends DbxUploader<PaperDocCreateUpdateResult, PaperDocCreateError, PaperDocCreateErrorException> {
    public DocsCreateUploader(Uploader uploader, String str) {
        super(uploader, Serializer.INSTANCE, Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public PaperDocCreateErrorException newException(DbxWrappedException dbxWrappedException) {
        return new PaperDocCreateErrorException("2/paper/docs/create", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (PaperDocCreateError) dbxWrappedException.getErrorValue());
    }
}
