package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ListFoldersArgs.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ListFoldersBuilder */
public class ListFoldersBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ListFoldersBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public ListFoldersBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public ListFoldersBuilder withActions(List<FolderAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public ListFoldersResult start() throws DbxApiException, DbxException {
        return this._client.listFolders(this._builder.build());
    }
}
