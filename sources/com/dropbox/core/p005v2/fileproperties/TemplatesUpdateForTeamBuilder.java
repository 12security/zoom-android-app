package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.UpdateTemplateArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.fileproperties.TemplatesUpdateForTeamBuilder */
public class TemplatesUpdateForTeamBuilder {
    private final Builder _builder;
    private final DbxTeamFilePropertiesRequests _client;

    TemplatesUpdateForTeamBuilder(DbxTeamFilePropertiesRequests dbxTeamFilePropertiesRequests, Builder builder) {
        if (dbxTeamFilePropertiesRequests != null) {
            this._client = dbxTeamFilePropertiesRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public TemplatesUpdateForTeamBuilder withName(String str) {
        this._builder.withName(str);
        return this;
    }

    public TemplatesUpdateForTeamBuilder withDescription(String str) {
        this._builder.withDescription(str);
        return this;
    }

    public TemplatesUpdateForTeamBuilder withAddFields(List<PropertyFieldTemplate> list) {
        this._builder.withAddFields(list);
        return this;
    }

    public UpdateTemplateResult start() throws ModifyTemplateErrorException, DbxException {
        return this._client.templatesUpdateForTeam(this._builder.build());
    }
}
