package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxUploader;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.http.HttpRequestor.Uploader;

/* renamed from: com.dropbox.core.v2.paper.DocsUpdateUploader */
public class DocsUpdateUploader extends DbxUploader<PaperDocCreateUpdateResult, PaperDocUpdateError, PaperDocUpdateErrorException> {
    public DocsUpdateUploader(Uploader uploader, String str) {
        super(uploader, Serializer.INSTANCE, Serializer.INSTANCE, str);
    }

    /* access modifiers changed from: protected */
    public PaperDocUpdateErrorException newException(DbxWrappedException dbxWrappedException) {
        return new PaperDocUpdateErrorException("2/paper/docs/update", dbxWrappedException.getRequestId(), dbxWrappedException.getUserMessage(), (PaperDocUpdateError) dbxWrappedException.getErrorValue());
    }
}
