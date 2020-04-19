package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.TemplateFilterBase;
import com.dropbox.core.p005v2.files.ListFolderArg.Builder;

/* renamed from: com.dropbox.core.v2.files.ListFolderBuilder */
public class ListFolderBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    ListFolderBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public ListFolderBuilder withRecursive(Boolean bool) {
        this._builder.withRecursive(bool);
        return this;
    }

    public ListFolderBuilder withIncludeMediaInfo(Boolean bool) {
        this._builder.withIncludeMediaInfo(bool);
        return this;
    }

    public ListFolderBuilder withIncludeDeleted(Boolean bool) {
        this._builder.withIncludeDeleted(bool);
        return this;
    }

    public ListFolderBuilder withIncludeHasExplicitSharedMembers(Boolean bool) {
        this._builder.withIncludeHasExplicitSharedMembers(bool);
        return this;
    }

    public ListFolderBuilder withIncludeMountedFolders(Boolean bool) {
        this._builder.withIncludeMountedFolders(bool);
        return this;
    }

    public ListFolderBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public ListFolderBuilder withSharedLink(SharedLink sharedLink) {
        this._builder.withSharedLink(sharedLink);
        return this;
    }

    public ListFolderBuilder withIncludePropertyGroups(TemplateFilterBase templateFilterBase) {
        this._builder.withIncludePropertyGroups(templateFilterBase);
        return this;
    }

    public ListFolderResult start() throws ListFolderErrorException, DbxException {
        return this._client.listFolder(this._builder.build());
    }
}
