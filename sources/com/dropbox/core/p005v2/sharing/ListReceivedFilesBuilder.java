package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ListFilesArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ListReceivedFilesBuilder */
public class ListReceivedFilesBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ListReceivedFilesBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
        if (dbxUserSharingRequests != null) {
            this._client = dbxUserSharingRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public ListReceivedFilesBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public ListReceivedFilesBuilder withActions(List<FileAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public ListFilesResult start() throws SharingUserErrorException, DbxException {
        return this._client.listReceivedFiles(this._builder.build());
    }
}
