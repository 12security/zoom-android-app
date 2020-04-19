package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.TemplateFilterBase;
import com.dropbox.core.p005v2.files.GetMetadataArg.Builder;

/* renamed from: com.dropbox.core.v2.files.GetMetadataBuilder */
public class GetMetadataBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    GetMetadataBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public GetMetadataBuilder withIncludeMediaInfo(Boolean bool) {
        this._builder.withIncludeMediaInfo(bool);
        return this;
    }

    public GetMetadataBuilder withIncludeDeleted(Boolean bool) {
        this._builder.withIncludeDeleted(bool);
        return this;
    }

    public GetMetadataBuilder withIncludeHasExplicitSharedMembers(Boolean bool) {
        this._builder.withIncludeHasExplicitSharedMembers(bool);
        return this;
    }

    public GetMetadataBuilder withIncludePropertyGroups(TemplateFilterBase templateFilterBase) {
        this._builder.withIncludePropertyGroups(templateFilterBase);
        return this;
    }

    public Metadata start() throws GetMetadataErrorException, DbxException {
        return this._client.getMetadata(this._builder.build());
    }
}
