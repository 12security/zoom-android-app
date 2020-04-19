package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsMetadata.AddFileMetadata;
import com.box.androidsdk.content.requests.BoxRequestsMetadata.DeleteFileMetadata;
import com.box.androidsdk.content.requests.BoxRequestsMetadata.GetFileMetadata;
import com.box.androidsdk.content.requests.BoxRequestsMetadata.GetMetadataTemplateSchema;
import com.box.androidsdk.content.requests.BoxRequestsMetadata.GetMetadataTemplates;
import com.box.androidsdk.content.requests.BoxRequestsMetadata.UpdateFileMetadata;
import java.util.LinkedHashMap;
import java.util.Locale;

public class BoxApiMetadata extends BoxApi {
    public static final String BOX_API_METADATA = "metadata";
    public static final String BOX_API_METADATA_SCHEMA = "schema";
    public static final String BOX_API_METADATA_TEMPLATES = "metadata_templates";
    public static final String BOX_API_SCOPE_ENTERPRISE = "enterprise";
    public static final String BOX_API_SCOPE_GLOBAL = "global";

    public BoxApiMetadata(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getFilesUrl() {
        return String.format(Locale.ENGLISH, "%s/files", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getFileInfoUrl(String str) {
        return String.format(Locale.ENGLISH, "%s/%s", new Object[]{getFilesUrl(), str});
    }

    /* access modifiers changed from: protected */
    public String getFileMetadataUrl(String str) {
        return String.format(Locale.ENGLISH, "%s/%s", new Object[]{getFileInfoUrl(str), BOX_API_METADATA});
    }

    /* access modifiers changed from: protected */
    public String getFileMetadataUrl(String str, String str2, String str3) {
        return String.format(Locale.ENGLISH, "%s/%s/%s", new Object[]{getFileMetadataUrl(str), str2, str3});
    }

    /* access modifiers changed from: protected */
    public String getFileMetadataUrl(String str, String str2) {
        return getFileMetadataUrl(str, "enterprise", str2);
    }

    /* access modifiers changed from: protected */
    public String getMetadataTemplatesUrl(String str) {
        return String.format(Locale.ENGLISH, "%s/%s/%s", new Object[]{getBaseUri(), BOX_API_METADATA_TEMPLATES, str});
    }

    /* access modifiers changed from: protected */
    public String getMetadataTemplatesUrl() {
        return getMetadataTemplatesUrl("enterprise");
    }

    /* access modifiers changed from: protected */
    public String getMetadataTemplatesUrl(String str, String str2) {
        return String.format(Locale.ENGLISH, "%s/%s/%s", new Object[]{getMetadataTemplatesUrl(str), str2, BOX_API_METADATA_SCHEMA});
    }

    public AddFileMetadata getAddMetadataRequest(String str, LinkedHashMap<String, Object> linkedHashMap, String str2, String str3) {
        return new AddFileMetadata(linkedHashMap, getFileMetadataUrl(str, str2, str3), this.mSession);
    }

    public GetFileMetadata getMetadataRequest(String str) {
        return new GetFileMetadata(getFileMetadataUrl(str), this.mSession);
    }

    public GetFileMetadata getMetadataRequest(String str, String str2) {
        return new GetFileMetadata(getFileMetadataUrl(str, str2), this.mSession);
    }

    public UpdateFileMetadata getUpdateMetadataRequest(String str, String str2, String str3) {
        return new UpdateFileMetadata(getFileMetadataUrl(str, str2, str3), this.mSession);
    }

    public DeleteFileMetadata getDeleteMetadataTemplateRequest(String str, String str2) {
        return new DeleteFileMetadata(getFileMetadataUrl(str, str2), this.mSession);
    }

    public GetMetadataTemplates getMetadataTemplatesRequest() {
        return new GetMetadataTemplates(getMetadataTemplatesUrl(), this.mSession);
    }

    public GetMetadataTemplateSchema getMetadataTemplateSchemaRequest(String str, String str2) {
        return new GetMetadataTemplateSchema(getMetadataTemplatesUrl(str, str2), this.mSession);
    }

    public GetMetadataTemplateSchema getMetadataTemplateSchemaRequest(String str) {
        return getMetadataTemplateSchemaRequest("enterprise", str);
    }
}
