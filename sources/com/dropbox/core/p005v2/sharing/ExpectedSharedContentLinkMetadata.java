package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ExpectedSharedContentLinkMetadata */
public class ExpectedSharedContentLinkMetadata extends SharedContentLinkMetadataBase {

    /* renamed from: com.dropbox.core.v2.sharing.ExpectedSharedContentLinkMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.SharedContentLinkMetadataBase.Builder {
        protected Builder(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z) {
            super(list, linkAudience, list2, z);
        }

        public Builder withAccessLevel(AccessLevel accessLevel) {
            super.withAccessLevel(accessLevel);
            return this;
        }

        public Builder withAudienceRestrictingSharedFolder(AudienceRestrictingSharedFolder audienceRestrictingSharedFolder) {
            super.withAudienceRestrictingSharedFolder(audienceRestrictingSharedFolder);
            return this;
        }

        public Builder withExpiry(Date date) {
            super.withExpiry(date);
            return this;
        }

        public ExpectedSharedContentLinkMetadata build() {
            ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata = new ExpectedSharedContentLinkMetadata(this.audienceOptions, this.currentAudience, this.linkPermissions, this.passwordProtected, this.accessLevel, this.audienceRestrictingSharedFolder, this.expiry);
            return expectedSharedContentLinkMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ExpectedSharedContentLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<ExpectedSharedContentLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("audience_options");
            StoneSerializers.list(com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE).serialize(expectedSharedContentLinkMetadata.audienceOptions, jsonGenerator);
            jsonGenerator.writeFieldName("current_audience");
            com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE.serialize(expectedSharedContentLinkMetadata.currentAudience, jsonGenerator);
            jsonGenerator.writeFieldName("link_permissions");
            StoneSerializers.list(Serializer.INSTANCE).serialize(expectedSharedContentLinkMetadata.linkPermissions, jsonGenerator);
            jsonGenerator.writeFieldName("password_protected");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(expectedSharedContentLinkMetadata.passwordProtected), jsonGenerator);
            if (expectedSharedContentLinkMetadata.accessLevel != null) {
                jsonGenerator.writeFieldName("access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(expectedSharedContentLinkMetadata.accessLevel, jsonGenerator);
            }
            if (expectedSharedContentLinkMetadata.audienceRestrictingSharedFolder != null) {
                jsonGenerator.writeFieldName("audience_restricting_shared_folder");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(expectedSharedContentLinkMetadata.audienceRestrictingSharedFolder, jsonGenerator);
            }
            if (expectedSharedContentLinkMetadata.expiry != null) {
                jsonGenerator.writeFieldName("expiry");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(expectedSharedContentLinkMetadata.expiry, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ExpectedSharedContentLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                LinkAudience linkAudience = null;
                List list2 = null;
                AccessLevel accessLevel = null;
                AudienceRestrictingSharedFolder audienceRestrictingSharedFolder = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("audience_options".equals(currentName)) {
                        list = (List) StoneSerializers.list(com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("current_audience".equals(currentName)) {
                        linkAudience = com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("link_permissions".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("password_protected".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("access_level".equals(currentName)) {
                        accessLevel = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("audience_restricting_shared_folder".equals(currentName)) {
                        audienceRestrictingSharedFolder = (AudienceRestrictingSharedFolder) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("expiry".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"audience_options\" missing.");
                } else if (linkAudience == null) {
                    throw new JsonParseException(jsonParser, "Required field \"current_audience\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"link_permissions\" missing.");
                } else if (bool != null) {
                    ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata = new ExpectedSharedContentLinkMetadata(list, linkAudience, list2, bool.booleanValue(), accessLevel, audienceRestrictingSharedFolder, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(expectedSharedContentLinkMetadata, expectedSharedContentLinkMetadata.toStringMultiline());
                    return expectedSharedContentLinkMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"password_protected\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public ExpectedSharedContentLinkMetadata(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z, AccessLevel accessLevel, AudienceRestrictingSharedFolder audienceRestrictingSharedFolder, Date date) {
        super(list, linkAudience, list2, z, accessLevel, audienceRestrictingSharedFolder, date);
    }

    public ExpectedSharedContentLinkMetadata(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z) {
        this(list, linkAudience, list2, z, null, null, null);
    }

    public List<LinkAudience> getAudienceOptions() {
        return this.audienceOptions;
    }

    public LinkAudience getCurrentAudience() {
        return this.currentAudience;
    }

    public List<LinkPermission> getLinkPermissions() {
        return this.linkPermissions;
    }

    public boolean getPasswordProtected() {
        return this.passwordProtected;
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public AudienceRestrictingSharedFolder getAudienceRestrictingSharedFolder() {
        return this.audienceRestrictingSharedFolder;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    public static Builder newBuilder(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z) {
        return new Builder(list, linkAudience, list2, z);
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata = (ExpectedSharedContentLinkMetadata) obj;
        if ((this.audienceOptions != expectedSharedContentLinkMetadata.audienceOptions && !this.audienceOptions.equals(expectedSharedContentLinkMetadata.audienceOptions)) || ((this.currentAudience != expectedSharedContentLinkMetadata.currentAudience && !this.currentAudience.equals(expectedSharedContentLinkMetadata.currentAudience)) || ((this.linkPermissions != expectedSharedContentLinkMetadata.linkPermissions && !this.linkPermissions.equals(expectedSharedContentLinkMetadata.linkPermissions)) || this.passwordProtected != expectedSharedContentLinkMetadata.passwordProtected || ((this.accessLevel != expectedSharedContentLinkMetadata.accessLevel && (this.accessLevel == null || !this.accessLevel.equals(expectedSharedContentLinkMetadata.accessLevel))) || ((this.audienceRestrictingSharedFolder != expectedSharedContentLinkMetadata.audienceRestrictingSharedFolder && (this.audienceRestrictingSharedFolder == null || !this.audienceRestrictingSharedFolder.equals(expectedSharedContentLinkMetadata.audienceRestrictingSharedFolder))) || (this.expiry != expectedSharedContentLinkMetadata.expiry && (this.expiry == null || !this.expiry.equals(expectedSharedContentLinkMetadata.expiry)))))))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
