package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.fileproperties.ModifyTemplateErrorException;
import com.dropbox.core.p005v2.fileproperties.PropertyFieldTemplate;
import com.dropbox.core.p005v2.fileproperties.UpdateTemplateArg.Builder;
import com.dropbox.core.p005v2.fileproperties.UpdateTemplateResult;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.PropertiesTemplateUpdateBuilder */
public class PropertiesTemplateUpdateBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    PropertiesTemplateUpdateBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
        if (dbxTeamTeamRequests != null) {
            this._client = dbxTeamTeamRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public PropertiesTemplateUpdateBuilder withName(String str) {
        this._builder.withName(str);
        return this;
    }

    public PropertiesTemplateUpdateBuilder withDescription(String str) {
        this._builder.withDescription(str);
        return this;
    }

    public PropertiesTemplateUpdateBuilder withAddFields(List<PropertyFieldTemplate> list) {
        this._builder.withAddFields(list);
        return this;
    }

    public UpdateTemplateResult start() throws ModifyTemplateErrorException, DbxException {
        return this._client.propertiesTemplateUpdate(this._builder.build());
    }
}
