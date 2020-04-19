package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.TemplateFilterBase;
import com.dropbox.core.p005v2.files.AlphaGetMetadataArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataBuilder */
public class AlphaGetMetadataBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    AlphaGetMetadataBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public AlphaGetMetadataBuilder withIncludeMediaInfo(Boolean bool) {
        this._builder.withIncludeMediaInfo(bool);
        return this;
    }

    public AlphaGetMetadataBuilder withIncludeDeleted(Boolean bool) {
        this._builder.withIncludeDeleted(bool);
        return this;
    }

    public AlphaGetMetadataBuilder withIncludeHasExplicitSharedMembers(Boolean bool) {
        this._builder.withIncludeHasExplicitSharedMembers(bool);
        return this;
    }

    public AlphaGetMetadataBuilder withIncludePropertyGroups(TemplateFilterBase templateFilterBase) {
        this._builder.withIncludePropertyGroups(templateFilterBase);
        return this;
    }

    public AlphaGetMetadataBuilder withIncludePropertyTemplates(List<String> list) {
        this._builder.withIncludePropertyTemplates(list);
        return this;
    }

    public Metadata start() throws AlphaGetMetadataErrorException, DbxException {
        return this._client.alphaGetMetadata(this._builder.build());
    }
}
