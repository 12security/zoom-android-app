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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.SharedContentLinkMetadata */
public class SharedContentLinkMetadata extends SharedContentLinkMetadataBase {
    protected final AudienceExceptions audienceExceptions;
    protected final String url;

    /* renamed from: com.dropbox.core.v2.sharing.SharedContentLinkMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.SharedContentLinkMetadataBase.Builder {
        protected AudienceExceptions audienceExceptions;
        protected final String url;

        protected Builder(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z, String str) {
            super(list, linkAudience, list2, z);
            if (str != null) {
                this.url = str;
                this.audienceExceptions = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'url' is null");
        }

        public Builder withAudienceExceptions(AudienceExceptions audienceExceptions2) {
            this.audienceExceptions = audienceExceptions2;
            return this;
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

        public SharedContentLinkMetadata build() {
            SharedContentLinkMetadata sharedContentLinkMetadata = new SharedContentLinkMetadata(this.audienceOptions, this.currentAudience, this.linkPermissions, this.passwordProtected, this.url, this.accessLevel, this.audienceRestrictingSharedFolder, this.expiry, this.audienceExceptions);
            return sharedContentLinkMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedContentLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<SharedContentLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentLinkMetadata sharedContentLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("audience_options");
            StoneSerializers.list(com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE).serialize(sharedContentLinkMetadata.audienceOptions, jsonGenerator);
            jsonGenerator.writeFieldName("current_audience");
            com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE.serialize(sharedContentLinkMetadata.currentAudience, jsonGenerator);
            jsonGenerator.writeFieldName("link_permissions");
            StoneSerializers.list(Serializer.INSTANCE).serialize(sharedContentLinkMetadata.linkPermissions, jsonGenerator);
            jsonGenerator.writeFieldName("password_protected");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(sharedContentLinkMetadata.passwordProtected), jsonGenerator);
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(sharedContentLinkMetadata.url, jsonGenerator);
            if (sharedContentLinkMetadata.accessLevel != null) {
                jsonGenerator.writeFieldName("access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(sharedContentLinkMetadata.accessLevel, jsonGenerator);
            }
            if (sharedContentLinkMetadata.audienceRestrictingSharedFolder != null) {
                jsonGenerator.writeFieldName("audience_restricting_shared_folder");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedContentLinkMetadata.audienceRestrictingSharedFolder, jsonGenerator);
            }
            if (sharedContentLinkMetadata.expiry != null) {
                jsonGenerator.writeFieldName("expiry");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedContentLinkMetadata.expiry, jsonGenerator);
            }
            if (sharedContentLinkMetadata.audienceExceptions != null) {
                jsonGenerator.writeFieldName("audience_exceptions");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedContentLinkMetadata.audienceExceptions, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str2 = null;
                AccessLevel accessLevel = null;
                AudienceRestrictingSharedFolder audienceRestrictingSharedFolder = null;
                Date date = null;
                AudienceExceptions audienceExceptions = null;
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
                    } else if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("access_level".equals(currentName)) {
                        accessLevel = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("audience_restricting_shared_folder".equals(currentName)) {
                        audienceRestrictingSharedFolder = (AudienceRestrictingSharedFolder) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("expiry".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("audience_exceptions".equals(currentName)) {
                        audienceExceptions = (AudienceExceptions) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
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
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"password_protected\" missing.");
                } else if (str2 != null) {
                    SharedContentLinkMetadata sharedContentLinkMetadata = new SharedContentLinkMetadata(list, linkAudience, list2, bool.booleanValue(), str2, accessLevel, audienceRestrictingSharedFolder, date, audienceExceptions);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentLinkMetadata, sharedContentLinkMetadata.toStringMultiline());
                    return sharedContentLinkMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
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

    public SharedContentLinkMetadata(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z, String str, AccessLevel accessLevel, AudienceRestrictingSharedFolder audienceRestrictingSharedFolder, Date date, AudienceExceptions audienceExceptions2) {
        String str2 = str;
        super(list, linkAudience, list2, z, accessLevel, audienceRestrictingSharedFolder, date);
        this.audienceExceptions = audienceExceptions2;
        if (str2 != null) {
            this.url = str2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'url' is null");
    }

    public SharedContentLinkMetadata(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z, String str) {
        this(list, linkAudience, list2, z, str, null, null, null, null);
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

    public String getUrl() {
        return this.url;
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

    public AudienceExceptions getAudienceExceptions() {
        return this.audienceExceptions;
    }

    public static Builder newBuilder(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z, String str) {
        Builder builder = new Builder(list, linkAudience, list2, z, str);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.audienceExceptions, this.url});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00a2, code lost:
        if (r2.equals(r5) == false) goto L_0x00a5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a7
            com.dropbox.core.v2.sharing.SharedContentLinkMetadata r5 = (com.dropbox.core.p005v2.sharing.SharedContentLinkMetadata) r5
            java.util.List r2 = r4.audienceOptions
            java.util.List r3 = r5.audienceOptions
            if (r2 == r3) goto L_0x0028
            java.util.List r2 = r4.audienceOptions
            java.util.List r3 = r5.audienceOptions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0028:
            com.dropbox.core.v2.sharing.LinkAudience r2 = r4.currentAudience
            com.dropbox.core.v2.sharing.LinkAudience r3 = r5.currentAudience
            if (r2 == r3) goto L_0x0038
            com.dropbox.core.v2.sharing.LinkAudience r2 = r4.currentAudience
            com.dropbox.core.v2.sharing.LinkAudience r3 = r5.currentAudience
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0038:
            java.util.List r2 = r4.linkPermissions
            java.util.List r3 = r5.linkPermissions
            if (r2 == r3) goto L_0x0048
            java.util.List r2 = r4.linkPermissions
            java.util.List r3 = r5.linkPermissions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0048:
            boolean r2 = r4.passwordProtected
            boolean r3 = r5.passwordProtected
            if (r2 != r3) goto L_0x00a5
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            if (r2 == r3) goto L_0x005a
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x005a:
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessLevel
            if (r2 == r3) goto L_0x006e
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            if (r2 == 0) goto L_0x00a5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessLevel
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x006e:
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r2 = r4.audienceRestrictingSharedFolder
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r3 = r5.audienceRestrictingSharedFolder
            if (r2 == r3) goto L_0x0082
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r2 = r4.audienceRestrictingSharedFolder
            if (r2 == 0) goto L_0x00a5
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r2 = r4.audienceRestrictingSharedFolder
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r3 = r5.audienceRestrictingSharedFolder
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0082:
            java.util.Date r2 = r4.expiry
            java.util.Date r3 = r5.expiry
            if (r2 == r3) goto L_0x0096
            java.util.Date r2 = r4.expiry
            if (r2 == 0) goto L_0x00a5
            java.util.Date r2 = r4.expiry
            java.util.Date r3 = r5.expiry
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a5
        L_0x0096:
            com.dropbox.core.v2.sharing.AudienceExceptions r2 = r4.audienceExceptions
            com.dropbox.core.v2.sharing.AudienceExceptions r5 = r5.audienceExceptions
            if (r2 == r5) goto L_0x00a6
            if (r2 == 0) goto L_0x00a5
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00a5
            goto L_0x00a6
        L_0x00a5:
            r0 = 0
        L_0x00a6:
            return r0
        L_0x00a7:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedContentLinkMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
