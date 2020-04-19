package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.DbxDownloadStyleBuilder;

/* renamed from: com.dropbox.core.v2.paper.DocsDownloadBuilder */
public class DocsDownloadBuilder extends DbxDownloadStyleBuilder<PaperDocExportResult> {
    private final DbxUserPaperRequests _client;
    private final String docId;
    private final ExportFormat exportFormat;

    DocsDownloadBuilder(DbxUserPaperRequests dbxUserPaperRequests, String str, ExportFormat exportFormat2) {
        if (dbxUserPaperRequests != null) {
            this._client = dbxUserPaperRequests;
            this.docId = str;
            this.exportFormat = exportFormat2;
            return;
        }
        throw new NullPointerException("_client");
    }

    public DbxDownloader<PaperDocExportResult> start() throws DocLookupErrorException, DbxException {
        return this._client.docsDownload(new PaperDocExport(this.docId, this.exportFormat), getHeaders());
    }
}
