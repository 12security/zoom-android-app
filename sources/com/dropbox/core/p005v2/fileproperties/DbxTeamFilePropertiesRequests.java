package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.fileproperties.AddTemplateArg.Serializer;
import com.dropbox.core.stone.StoneSerializers;
import java.util.List;

/* renamed from: com.dropbox.core.v2.fileproperties.DbxTeamFilePropertiesRequests */
public class DbxTeamFilePropertiesRequests {
    private final DbxRawClientV2 client;

    public DbxTeamFilePropertiesRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public AddTemplateResult templatesAddForTeam(AddTemplateArg addTemplateArg) throws ModifyTemplateErrorException, DbxException {
        try {
            return (AddTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/add_for_team", addTemplateArg, false, Serializer.INSTANCE, AddTemplateResult.Serializer.INSTANCE, ModifyTemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifyTemplateErrorException("2/file_properties/templates/add_for_team", e.getRequestId(), e.getUserMessage(), (ModifyTemplateError) e.getErrorValue());
        }
    }

    public AddTemplateResult templatesAddForTeam(String str, String str2, List<PropertyFieldTemplate> list) throws ModifyTemplateErrorException, DbxException {
        return templatesAddForTeam(new AddTemplateArg(str, str2, list));
    }

    /* access modifiers changed from: 0000 */
    public GetTemplateResult templatesGetForTeam(GetTemplateArg getTemplateArg) throws TemplateErrorException, DbxException {
        try {
            return (GetTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/get_for_team", getTemplateArg, false, GetTemplateArg.Serializer.INSTANCE, GetTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/file_properties/templates/get_for_team", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    public GetTemplateResult templatesGetForTeam(String str) throws TemplateErrorException, DbxException {
        return templatesGetForTeam(new GetTemplateArg(str));
    }

    public ListTemplateResult templatesListForTeam() throws TemplateErrorException, DbxException {
        try {
            return (ListTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/list_for_team", null, false, StoneSerializers.void_(), ListTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/file_properties/templates/list_for_team", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public void templatesRemoveForTeam(RemoveTemplateArg removeTemplateArg) throws TemplateErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/remove_for_team", removeTemplateArg, false, Serializer.INSTANCE, StoneSerializers.void_(), TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/file_properties/templates/remove_for_team", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    public void templatesRemoveForTeam(String str) throws TemplateErrorException, DbxException {
        templatesRemoveForTeam(new RemoveTemplateArg(str));
    }

    /* access modifiers changed from: 0000 */
    public UpdateTemplateResult templatesUpdateForTeam(UpdateTemplateArg updateTemplateArg) throws ModifyTemplateErrorException, DbxException {
        try {
            return (UpdateTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/update_for_team", updateTemplateArg, false, UpdateTemplateArg.Serializer.INSTANCE, UpdateTemplateResult.Serializer.INSTANCE, ModifyTemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifyTemplateErrorException("2/file_properties/templates/update_for_team", e.getRequestId(), e.getUserMessage(), (ModifyTemplateError) e.getErrorValue());
        }
    }

    public UpdateTemplateResult templatesUpdateForTeam(String str) throws ModifyTemplateErrorException, DbxException {
        return templatesUpdateForTeam(new UpdateTemplateArg(str));
    }

    public TemplatesUpdateForTeamBuilder templatesUpdateForTeamBuilder(String str) {
        return new TemplatesUpdateForTeamBuilder(this, UpdateTemplateArg.newBuilder(str));
    }
}
