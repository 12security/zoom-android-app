package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.DbxDownloadStyleBuilder;

/* renamed from: com.dropbox.core.v2.files.DownloadZipBuilder */
public class DownloadZipBuilder extends DbxDownloadStyleBuilder<DownloadZipResult> {
    private final DbxUserFilesRequests _client;
    private final String path;

    DownloadZipBuilder(DbxUserFilesRequests dbxUserFilesRequests, String str) {
        if (dbxUserFilesRequests != null) {
            this._client = dbxUserFilesRequests;
            this.path = str;
            return;
        }
        throw new NullPointerException("_client");
    }

    public DbxDownloader<DownloadZipResult> start() throws DownloadZipErrorException, DbxException {
        return this._client.downloadZip(new DownloadZipArg(this.path), getHeaders());
    }
}
