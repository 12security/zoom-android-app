package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.DbxUploadStyleBuilder;
import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
import com.dropbox.core.p005v2.files.CommitInfo.Builder;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.files.UploadBuilder */
public class UploadBuilder extends DbxUploadStyleBuilder<FileMetadata, UploadError, UploadErrorException> {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    UploadBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public UploadBuilder withMode(WriteMode writeMode) {
        this._builder.withMode(writeMode);
        return this;
    }

    public UploadBuilder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public UploadBuilder withClientModified(Date date) {
        this._builder.withClientModified(date);
        return this;
    }

    public UploadBuilder withMute(Boolean bool) {
        this._builder.withMute(bool);
        return this;
    }

    public UploadBuilder withPropertyGroups(List<PropertyGroup> list) {
        this._builder.withPropertyGroups(list);
        return this;
    }

    public UploadBuilder withStrictConflict(Boolean bool) {
        this._builder.withStrictConflict(bool);
        return this;
    }

    public UploadUploader start() throws UploadErrorException, DbxException {
        return this._client.upload(this._builder.build());
    }
}
