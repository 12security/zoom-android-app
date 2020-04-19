package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ListFoldersArgs.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ListMountableFoldersBuilder */
public class ListMountableFoldersBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ListMountableFoldersBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public ListMountableFoldersBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public ListMountableFoldersBuilder withActions(List<FolderAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public ListFoldersResult start() throws DbxApiException, DbxException {
        return this._client.listMountableFolders(this._builder.build());
    }
}
