package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.SharedContentLinkMetadataBase */
public class SharedContentLinkMetadataBase {
    protected final AccessLevel accessLevel;
    protected final List<LinkAudience> audienceOptions;
    protected final AudienceRestrictingSharedFolder audienceRestrictingSharedFolder;
    protected final LinkAudience currentAudience;
    protected final Date expiry;
    protected final List<LinkPermission> linkPermissions;
    protected final boolean passwordProtected;

    /* renamed from: com.dropbox.core.v2.sharing.SharedContentLinkMetadataBase$Builder */
    public static class Builder {
        protected AccessLevel accessLevel;
        protected final List<LinkAudience> audienceOptions;
        protected AudienceRestrictingSharedFolder audienceRestrictingSharedFolder;
        protected final LinkAudience currentAudience;
        protected Date expiry;
        protected final List<LinkPermission> linkPermissions;
        protected final boolean passwordProtected;

        protected Builder(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z) {
            if (list != null) {
                for (LinkAudience linkAudience2 : list) {
                    if (linkAudience2 == null) {
                        throw new IllegalArgumentException("An item in list 'audienceOptions' is null");
                    }
                }
                this.audienceOptions = list;
                if (linkAudience != null) {
                    this.currentAudience = linkAudience;
                    if (list2 != null) {
                        for (LinkPermission linkPermission : list2) {
                            if (linkPermission == null) {
                                throw new IllegalArgumentException("An item in list 'linkPermissions' is null");
                            }
                        }
                        this.linkPermissions = list2;
                        this.passwordProtected = z;
                        this.accessLevel = null;
                        this.audienceRestrictingSharedFolder = null;
                        this.expiry = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'linkPermissions' is null");
                }
                throw new IllegalArgumentException("Required value for 'currentAudience' is null");
            }
            throw new IllegalArgumentException("Required value for 'audienceOptions' is null");
        }

        public Builder withAccessLevel(AccessLevel accessLevel2) {
            this.accessLevel = accessLevel2;
            return this;
        }

        public Builder withAudienceRestrictingSharedFolder(AudienceRestrictingSharedFolder audienceRestrictingSharedFolder2) {
            this.audienceRestrictingSharedFolder = audienceRestrictingSharedFolder2;
            return this;
        }

        public Builder withExpiry(Date date) {
            this.expiry = LangUtil.truncateMillis(date);
            return this;
        }

        public SharedContentLinkMetadataBase build() {
            SharedContentLinkMetadataBase sharedContentLinkMetadataBase = new SharedContentLinkMetadataBase(this.audienceOptions, this.currentAudience, this.linkPermissions, this.passwordProtected, this.accessLevel, this.audienceRestrictingSharedFolder, this.expiry);
            return sharedContentLinkMetadataBase;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedContentLinkMetadataBase$Serializer */
    private static class Serializer extends StructSerializer<SharedContentLinkMetadataBase> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(SharedContentLinkMetadataBase sharedContentLinkMetadataBase, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("audience_options");
            StoneSerializers.list(com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE).serialize(sharedContentLinkMetadataBase.audienceOptions, jsonGenerator);
            jsonGenerator.writeFieldName("current_audience");
            com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE.serialize(sharedContentLinkMetadataBase.currentAudience, jsonGenerator);
            jsonGenerator.writeFieldName("link_permissions");
            StoneSerializers.list(Serializer.INSTANCE).serialize(sharedContentLinkMetadataBase.linkPermissions, jsonGenerator);
            jsonGenerator.writeFieldName("password_protected");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(sharedContentLinkMetadataBase.passwordProtected), jsonGenerator);
            if (sharedContentLinkMetadataBase.accessLevel != null) {
                jsonGenerator.writeFieldName("access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(sharedContentLinkMetadataBase.accessLevel, jsonGenerator);
            }
            if (sharedContentLinkMetadataBase.audienceRestrictingSharedFolder != null) {
                jsonGenerator.writeFieldName("audience_restricting_shared_folder");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedContentLinkMetadataBase.audienceRestrictingSharedFolder, jsonGenerator);
            }
            if (sharedContentLinkMetadataBase.expiry != null) {
                jsonGenerator.writeFieldName("expiry");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedContentLinkMetadataBase.expiry, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentLinkMetadataBase deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    SharedContentLinkMetadataBase sharedContentLinkMetadataBase = new SharedContentLinkMetadataBase(list, linkAudience, list2, bool.booleanValue(), accessLevel, audienceRestrictingSharedFolder, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentLinkMetadataBase, sharedContentLinkMetadataBase.toStringMultiline());
                    return sharedContentLinkMetadataBase;
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

    public SharedContentLinkMetadataBase(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z, AccessLevel accessLevel2, AudienceRestrictingSharedFolder audienceRestrictingSharedFolder2, Date date) {
        this.accessLevel = accessLevel2;
        if (list != null) {
            for (LinkAudience linkAudience2 : list) {
                if (linkAudience2 == null) {
                    throw new IllegalArgumentException("An item in list 'audienceOptions' is null");
                }
            }
            this.audienceOptions = list;
            this.audienceRestrictingSharedFolder = audienceRestrictingSharedFolder2;
            if (linkAudience != null) {
                this.currentAudience = linkAudience;
                this.expiry = LangUtil.truncateMillis(date);
                if (list2 != null) {
                    for (LinkPermission linkPermission : list2) {
                        if (linkPermission == null) {
                            throw new IllegalArgumentException("An item in list 'linkPermissions' is null");
                        }
                    }
                    this.linkPermissions = list2;
                    this.passwordProtected = z;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'linkPermissions' is null");
            }
            throw new IllegalArgumentException("Required value for 'currentAudience' is null");
        }
        throw new IllegalArgumentException("Required value for 'audienceOptions' is null");
    }

    public SharedContentLinkMetadataBase(List<LinkAudience> list, LinkAudience linkAudience, List<LinkPermission> list2, boolean z) {
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
        return Arrays.hashCode(new Object[]{this.accessLevel, this.audienceOptions, this.audienceRestrictingSharedFolder, this.currentAudience, this.expiry, this.linkPermissions, Boolean.valueOf(this.passwordProtected)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006a, code lost:
        if (r2.equals(r5) == false) goto L_0x006d;
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
            if (r2 == 0) goto L_0x006f
            com.dropbox.core.v2.sharing.SharedContentLinkMetadataBase r5 = (com.dropbox.core.p005v2.sharing.SharedContentLinkMetadataBase) r5
            java.util.List<com.dropbox.core.v2.sharing.LinkAudience> r2 = r4.audienceOptions
            java.util.List<com.dropbox.core.v2.sharing.LinkAudience> r3 = r5.audienceOptions
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0024:
            com.dropbox.core.v2.sharing.LinkAudience r2 = r4.currentAudience
            com.dropbox.core.v2.sharing.LinkAudience r3 = r5.currentAudience
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0030:
            java.util.List<com.dropbox.core.v2.sharing.LinkPermission> r2 = r4.linkPermissions
            java.util.List<com.dropbox.core.v2.sharing.LinkPermission> r3 = r5.linkPermissions
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x003c:
            boolean r2 = r4.passwordProtected
            boolean r3 = r5.passwordProtected
            if (r2 != r3) goto L_0x006d
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessLevel
            if (r2 == r3) goto L_0x0050
            if (r2 == 0) goto L_0x006d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0050:
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r2 = r4.audienceRestrictingSharedFolder
            com.dropbox.core.v2.sharing.AudienceRestrictingSharedFolder r3 = r5.audienceRestrictingSharedFolder
            if (r2 == r3) goto L_0x005e
            if (r2 == 0) goto L_0x006d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x005e:
            java.util.Date r2 = r4.expiry
            java.util.Date r5 = r5.expiry
            if (r2 == r5) goto L_0x006e
            if (r2 == 0) goto L_0x006d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x006d
            goto L_0x006e
        L_0x006d:
            r0 = 0
        L_0x006e:
            return r0
        L_0x006f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedContentLinkMetadataBase.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
