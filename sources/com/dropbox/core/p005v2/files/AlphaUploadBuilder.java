package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.DbxUploadStyleBuilder;
import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
import com.dropbox.core.p005v2.files.CommitInfoWithProperties.Builder;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.files.AlphaUploadBuilder */
public class AlphaUploadBuilder extends DbxUploadStyleBuilder<FileMetadata, UploadErrorWithProperties, UploadErrorWithPropertiesException> {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    AlphaUploadBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public AlphaUploadBuilder withMode(WriteMode writeMode) {
        this._builder.withMode(writeMode);
        return this;
    }

    public AlphaUploadBuilder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public AlphaUploadBuilder withClientModified(Date date) {
        this._builder.withClientModified(date);
        return this;
    }

    public AlphaUploadBuilder withMute(Boolean bool) {
        this._builder.withMute(bool);
        return this;
    }

    public AlphaUploadBuilder withPropertyGroups(List<PropertyGroup> list) {
        this._builder.withPropertyGroups((List) list);
        return this;
    }

    public AlphaUploadBuilder withStrictConflict(Boolean bool) {
        this._builder.withStrictConflict(bool);
        return this;
    }

    public AlphaUploadUploader start() throws UploadErrorWithPropertiesException, DbxException {
        return this._client.alphaUpload(this._builder.build());
    }
}
