package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.fileproperties.AddPropertiesArg.Serializer;
import com.dropbox.core.stone.StoneSerializers;
import java.util.List;

/* renamed from: com.dropbox.core.v2.fileproperties.DbxUserFilePropertiesRequests */
public class DbxUserFilePropertiesRequests {
    private final DbxRawClientV2 client;

    public DbxUserFilePropertiesRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public void propertiesAdd(AddPropertiesArg addPropertiesArg) throws AddPropertiesErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/properties/add", addPropertiesArg, false, Serializer.INSTANCE, StoneSerializers.void_(), AddPropertiesError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new AddPropertiesErrorException("2/file_properties/properties/add", e.getRequestId(), e.getUserMessage(), (AddPropertiesError) e.getErrorValue());
        }
    }

    public void propertiesAdd(String str, List<PropertyGroup> list) throws AddPropertiesErrorException, DbxException {
        propertiesAdd(new AddPropertiesArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public void propertiesOverwrite(OverwritePropertyGroupArg overwritePropertyGroupArg) throws InvalidPropertyGroupErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/properties/overwrite", overwritePropertyGroupArg, false, OverwritePropertyGroupArg.Serializer.INSTANCE, StoneSerializers.void_(), InvalidPropertyGroupError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new InvalidPropertyGroupErrorException("2/file_properties/properties/overwrite", e.getRequestId(), e.getUserMessage(), (InvalidPropertyGroupError) e.getErrorValue());
        }
    }

    public void propertiesOverwrite(String str, List<PropertyGroup> list) throws InvalidPropertyGroupErrorException, DbxException {
        propertiesOverwrite(new OverwritePropertyGroupArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public void propertiesRemove(RemovePropertiesArg removePropertiesArg) throws RemovePropertiesErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/properties/remove", removePropertiesArg, false, RemovePropertiesArg.Serializer.INSTANCE, StoneSerializers.void_(), RemovePropertiesError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RemovePropertiesErrorException("2/file_properties/properties/remove", e.getRequestId(), e.getUserMessage(), (RemovePropertiesError) e.getErrorValue());
        }
    }

    public void propertiesRemove(String str, List<String> list) throws RemovePropertiesErrorException, DbxException {
        propertiesRemove(new RemovePropertiesArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public PropertiesSearchResult propertiesSearch(PropertiesSearchArg propertiesSearchArg) throws PropertiesSearchErrorException, DbxException {
        try {
            return (PropertiesSearchResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/properties/search", propertiesSearchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PropertiesSearchErrorException("2/file_properties/properties/search", e.getRequestId(), e.getUserMessage(), (PropertiesSearchError) e.getErrorValue());
        }
    }

    public PropertiesSearchResult propertiesSearch(List<PropertiesSearchQuery> list) throws PropertiesSearchErrorException, DbxException {
        return propertiesSearch(new PropertiesSearchArg(list));
    }

    public PropertiesSearchResult propertiesSearch(List<PropertiesSearchQuery> list, TemplateFilter templateFilter) throws PropertiesSearchErrorException, DbxException {
        if (templateFilter != null) {
            return propertiesSearch(new PropertiesSearchArg(list, templateFilter));
        }
        throw new IllegalArgumentException("Required value for 'templateFilter' is null");
    }

    /* access modifiers changed from: 0000 */
    public PropertiesSearchResult propertiesSearchContinue(PropertiesSearchContinueArg propertiesSearchContinueArg) throws PropertiesSearchContinueErrorException, DbxException {
        try {
            return (PropertiesSearchResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/properties/search/continue", propertiesSearchContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PropertiesSearchContinueErrorException("2/file_properties/properties/search/continue", e.getRequestId(), e.getUserMessage(), (PropertiesSearchContinueError) e.getErrorValue());
        }
    }

    public PropertiesSearchResult propertiesSearchContinue(String str) throws PropertiesSearchContinueErrorException, DbxException {
        return propertiesSearchContinue(new PropertiesSearchContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void propertiesUpdate(UpdatePropertiesArg updatePropertiesArg) throws UpdatePropertiesErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/properties/update", updatePropertiesArg, false, UpdatePropertiesArg.Serializer.INSTANCE, StoneSerializers.void_(), UpdatePropertiesError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UpdatePropertiesErrorException("2/file_properties/properties/update", e.getRequestId(), e.getUserMessage(), (UpdatePropertiesError) e.getErrorValue());
        }
    }

    public void propertiesUpdate(String str, List<PropertyGroupUpdate> list) throws UpdatePropertiesErrorException, DbxException {
        propertiesUpdate(new UpdatePropertiesArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public AddTemplateResult templatesAddForUser(AddTemplateArg addTemplateArg) throws ModifyTemplateErrorException, DbxException {
        try {
            return (AddTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/add_for_user", addTemplateArg, false, AddTemplateArg.Serializer.INSTANCE, AddTemplateResult.Serializer.INSTANCE, ModifyTemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifyTemplateErrorException("2/file_properties/templates/add_for_user", e.getRequestId(), e.getUserMessage(), (ModifyTemplateError) e.getErrorValue());
        }
    }

    public AddTemplateResult templatesAddForUser(String str, String str2, List<PropertyFieldTemplate> list) throws ModifyTemplateErrorException, DbxException {
        return templatesAddForUser(new AddTemplateArg(str, str2, list));
    }

    /* access modifiers changed from: 0000 */
    public GetTemplateResult templatesGetForUser(GetTemplateArg getTemplateArg) throws TemplateErrorException, DbxException {
        try {
            return (GetTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/get_for_user", getTemplateArg, false, GetTemplateArg.Serializer.INSTANCE, GetTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/file_properties/templates/get_for_user", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    public GetTemplateResult templatesGetForUser(String str) throws TemplateErrorException, DbxException {
        return templatesGetForUser(new GetTemplateArg(str));
    }

    public ListTemplateResult templatesListForUser() throws TemplateErrorException, DbxException {
        try {
            return (ListTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/list_for_user", null, false, StoneSerializers.void_(), ListTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/file_properties/templates/list_for_user", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public void templatesRemoveForUser(RemoveTemplateArg removeTemplateArg) throws TemplateErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/remove_for_user", removeTemplateArg, false, Serializer.INSTANCE, StoneSerializers.void_(), TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/file_properties/templates/remove_for_user", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    public void templatesRemoveForUser(String str) throws TemplateErrorException, DbxException {
        templatesRemoveForUser(new RemoveTemplateArg(str));
    }

    /* access modifiers changed from: 0000 */
    public UpdateTemplateResult templatesUpdateForUser(UpdateTemplateArg updateTemplateArg) throws ModifyTemplateErrorException, DbxException {
        try {
            return (UpdateTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/file_properties/templates/update_for_user", updateTemplateArg, false, UpdateTemplateArg.Serializer.INSTANCE, UpdateTemplateResult.Serializer.INSTANCE, ModifyTemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ModifyTemplateErrorException("2/file_properties/templates/update_for_user", e.getRequestId(), e.getUserMessage(), (ModifyTemplateError) e.getErrorValue());
        }
    }

    public UpdateTemplateResult templatesUpdateForUser(String str) throws ModifyTemplateErrorException, DbxException {
        return templatesUpdateForUser(new UpdateTemplateArg(str));
    }

    public TemplatesUpdateForUserBuilder templatesUpdateForUserBuilder(String str) {
        return new TemplatesUpdateForUserBuilder(this, UpdateTemplateArg.newBuilder(str));
    }
}
