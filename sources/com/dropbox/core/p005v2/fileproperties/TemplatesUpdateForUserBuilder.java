package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.UpdateTemplateArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.fileproperties.TemplatesUpdateForUserBuilder */
public class TemplatesUpdateForUserBuilder {
    private final Builder _builder;
    private final DbxUserFilePropertiesRequests _client;

    TemplatesUpdateForUserBuilder(DbxUserFilePropertiesRequests dbxUserFilePropertiesRequests, Builder builder) {
        if (dbxUserFilePropertiesRequests != null) {
            this._client = dbxUserFilePropertiesRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public TemplatesUpdateForUserBuilder withName(String str) {
        this._builder.withName(str);
        return this;
    }

    public TemplatesUpdateForUserBuilder withDescription(String str) {
        this._builder.withDescription(str);
        return this;
    }

    public TemplatesUpdateForUserBuilder withAddFields(List<PropertyFieldTemplate> list) {
        this._builder.withAddFields(list);
        return this;
    }

    public UpdateTemplateResult start() throws ModifyTemplateErrorException, DbxException {
        return this._client.templatesUpdateForUser(this._builder.build());
    }
}
