package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.TemplateFilterBase;
import com.dropbox.core.p005v2.files.ListFolderArg.Builder;

/* renamed from: com.dropbox.core.v2.files.ListFolderGetLatestCursorBuilder */
public class ListFolderGetLatestCursorBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    ListFolderGetLatestCursorBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
        if (dbxUserFilesRequests != null) {
            this._client = dbxUserFilesRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public ListFolderGetLatestCursorBuilder withRecursive(Boolean bool) {
        this._builder.withRecursive(bool);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withIncludeMediaInfo(Boolean bool) {
        this._builder.withIncludeMediaInfo(bool);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withIncludeDeleted(Boolean bool) {
        this._builder.withIncludeDeleted(bool);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withIncludeHasExplicitSharedMembers(Boolean bool) {
        this._builder.withIncludeHasExplicitSharedMembers(bool);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withIncludeMountedFolders(Boolean bool) {
        this._builder.withIncludeMountedFolders(bool);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withSharedLink(SharedLink sharedLink) {
        this._builder.withSharedLink(sharedLink);
        return this;
    }

    public ListFolderGetLatestCursorBuilder withIncludePropertyGroups(TemplateFilterBase templateFilterBase) {
        this._builder.withIncludePropertyGroups(templateFilterBase);
        return this;
    }

    public ListFolderGetLatestCursorResult start() throws ListFolderErrorException, DbxException {
        return this._client.listFolderGetLatestCursor(this._builder.build());
    }
}
