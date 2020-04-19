package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.users.Team;
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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.SharedFolderMetadata */
public class SharedFolderMetadata extends SharedFolderMetadataBase {
    protected final AccessInheritance accessInheritance;
    protected final SharedContentLinkMetadata linkMetadata;
    protected final String name;
    protected final List<FolderPermission> permissions;
    protected final FolderPolicy policy;
    protected final String previewUrl;
    protected final String sharedFolderId;
    protected final Date timeInvited;

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.SharedFolderMetadataBase.Builder {
        protected AccessInheritance accessInheritance;
        protected SharedContentLinkMetadata linkMetadata;
        protected final String name;
        protected List<FolderPermission> permissions;
        protected final FolderPolicy policy;
        protected final String previewUrl;
        protected final String sharedFolderId;
        protected final Date timeInvited;

        protected Builder(AccessLevel accessLevel, boolean z, boolean z2, String str, FolderPolicy folderPolicy, String str2, String str3, Date date) {
            super(accessLevel, z, z2);
            if (str != null) {
                this.name = str;
                if (folderPolicy != null) {
                    this.policy = folderPolicy;
                    if (str2 != null) {
                        this.previewUrl = str2;
                        if (str3 == null) {
                            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
                        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str3)) {
                            this.sharedFolderId = str3;
                            if (date != null) {
                                this.timeInvited = LangUtil.truncateMillis(date);
                                this.linkMetadata = null;
                                this.permissions = null;
                                this.accessInheritance = AccessInheritance.INHERIT;
                                return;
                            }
                            throw new IllegalArgumentException("Required value for 'timeInvited' is null");
                        } else {
                            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
                        }
                    } else {
                        throw new IllegalArgumentException("Required value for 'previewUrl' is null");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'policy' is null");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'name' is null");
            }
        }

        public Builder withLinkMetadata(SharedContentLinkMetadata sharedContentLinkMetadata) {
            this.linkMetadata = sharedContentLinkMetadata;
            return this;
        }

        public Builder withPermissions(List<FolderPermission> list) {
            if (list != null) {
                for (FolderPermission folderPermission : list) {
                    if (folderPermission == null) {
                        throw new IllegalArgumentException("An item in list 'permissions' is null");
                    }
                }
            }
            this.permissions = list;
            return this;
        }

        public Builder withAccessInheritance(AccessInheritance accessInheritance2) {
            if (accessInheritance2 != null) {
                this.accessInheritance = accessInheritance2;
            } else {
                this.accessInheritance = AccessInheritance.INHERIT;
            }
            return this;
        }

        public Builder withOwnerDisplayNames(List<String> list) {
            super.withOwnerDisplayNames(list);
            return this;
        }

        public Builder withOwnerTeam(Team team) {
            super.withOwnerTeam(team);
            return this;
        }

        public Builder withParentSharedFolderId(String str) {
            super.withParentSharedFolderId(str);
            return this;
        }

        public Builder withPathLower(String str) {
            super.withPathLower(str);
            return this;
        }

        public SharedFolderMetadata build() {
            SharedFolderMetadata sharedFolderMetadata = new SharedFolderMetadata(this.accessType, this.isInsideTeamFolder, this.isTeamFolder, this.name, this.policy, this.previewUrl, this.sharedFolderId, this.timeInvited, this.ownerDisplayNames, this.ownerTeam, this.parentSharedFolderId, this.pathLower, this.linkMetadata, this.permissions, this.accessInheritance);
            return sharedFolderMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderMetadata$Serializer */
    static class Serializer extends StructSerializer<SharedFolderMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedFolderMetadata sharedFolderMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_type");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedFolderMetadata.accessType, jsonGenerator);
            jsonGenerator.writeFieldName("is_inside_team_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(sharedFolderMetadata.isInsideTeamFolder), jsonGenerator);
            jsonGenerator.writeFieldName("is_team_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(sharedFolderMetadata.isTeamFolder), jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(sharedFolderMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("policy");
            Serializer.INSTANCE.serialize(sharedFolderMetadata.policy, jsonGenerator);
            jsonGenerator.writeFieldName("preview_url");
            StoneSerializers.string().serialize(sharedFolderMetadata.previewUrl, jsonGenerator);
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(sharedFolderMetadata.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("time_invited");
            StoneSerializers.timestamp().serialize(sharedFolderMetadata.timeInvited, jsonGenerator);
            if (sharedFolderMetadata.ownerDisplayNames != null) {
                jsonGenerator.writeFieldName("owner_display_names");
                StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).serialize(sharedFolderMetadata.ownerDisplayNames, jsonGenerator);
            }
            if (sharedFolderMetadata.ownerTeam != null) {
                jsonGenerator.writeFieldName("owner_team");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).serialize(sharedFolderMetadata.ownerTeam, jsonGenerator);
            }
            if (sharedFolderMetadata.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderMetadata.parentSharedFolderId, jsonGenerator);
            }
            if (sharedFolderMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderMetadata.pathLower, jsonGenerator);
            }
            if (sharedFolderMetadata.linkMetadata != null) {
                jsonGenerator.writeFieldName("link_metadata");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedFolderMetadata.linkMetadata, jsonGenerator);
            }
            if (sharedFolderMetadata.permissions != null) {
                jsonGenerator.writeFieldName("permissions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(sharedFolderMetadata.permissions, jsonGenerator);
            }
            jsonGenerator.writeFieldName("access_inheritance");
            Serializer.INSTANCE.serialize(sharedFolderMetadata.accessInheritance, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedFolderMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AccessLevel accessLevel = null;
                String str2 = null;
                FolderPolicy folderPolicy = null;
                String str3 = null;
                String str4 = null;
                Date date = null;
                List list = null;
                Team team = null;
                String str5 = null;
                String str6 = null;
                SharedContentLinkMetadata sharedContentLinkMetadata = null;
                List list2 = null;
                AccessInheritance accessInheritance = AccessInheritance.INHERIT;
                Boolean bool2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_type".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("is_inside_team_folder".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("is_team_folder".equals(currentName)) {
                        bool2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("policy".equals(currentName)) {
                        folderPolicy = (FolderPolicy) Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("preview_url".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("shared_folder_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("time_invited".equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser2);
                    } else if ("owner_display_names".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).deserialize(jsonParser2);
                    } else if ("owner_team".equals(currentName)) {
                        team = (Team) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("path_lower".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("link_metadata".equals(currentName)) {
                        sharedContentLinkMetadata = (SharedContentLinkMetadata) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("permissions".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser2);
                    } else if ("access_inheritance".equals(currentName)) {
                        accessInheritance = Serializer.INSTANCE.deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"access_type\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"is_inside_team_folder\" missing.");
                } else if (bool2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"is_team_folder\" missing.");
                } else if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"name\" missing.");
                } else if (folderPolicy == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"policy\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"preview_url\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"shared_folder_id\" missing.");
                } else if (date != null) {
                    boolean booleanValue = bool.booleanValue();
                    boolean booleanValue2 = bool2.booleanValue();
                    SharedFolderMetadata sharedFolderMetadata = r3;
                    SharedFolderMetadata sharedFolderMetadata2 = new SharedFolderMetadata(accessLevel, booleanValue, booleanValue2, str2, folderPolicy, str3, str4, date, list, team, str5, str6, sharedContentLinkMetadata, list2, accessInheritance);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedFolderMetadata, sharedFolderMetadata.toStringMultiline());
                    return sharedFolderMetadata;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"time_invited\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser2, sb.toString());
            }
        }
    }

    public SharedFolderMetadata(AccessLevel accessLevel, boolean z, boolean z2, String str, FolderPolicy folderPolicy, String str2, String str3, Date date, List<String> list, Team team, String str4, String str5, SharedContentLinkMetadata sharedContentLinkMetadata, List<FolderPermission> list2, AccessInheritance accessInheritance2) {
        String str6 = str;
        FolderPolicy folderPolicy2 = folderPolicy;
        String str7 = str2;
        String str8 = str3;
        List<FolderPermission> list3 = list2;
        AccessInheritance accessInheritance3 = accessInheritance2;
        super(accessLevel, z, z2, list, team, str4, str5);
        this.linkMetadata = sharedContentLinkMetadata;
        if (str6 != null) {
            this.name = str6;
            if (list3 != null) {
                for (FolderPermission folderPermission : list2) {
                    if (folderPermission == null) {
                        throw new IllegalArgumentException("An item in list 'permissions' is null");
                    }
                }
            }
            this.permissions = list3;
            if (folderPolicy2 != null) {
                this.policy = folderPolicy2;
                if (str7 != null) {
                    this.previewUrl = str7;
                    if (str8 == null) {
                        throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
                    } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str8)) {
                        this.sharedFolderId = str8;
                        if (date != null) {
                            this.timeInvited = LangUtil.truncateMillis(date);
                            if (accessInheritance3 != null) {
                                this.accessInheritance = accessInheritance3;
                                return;
                            }
                            throw new IllegalArgumentException("Required value for 'accessInheritance' is null");
                        }
                        throw new IllegalArgumentException("Required value for 'timeInvited' is null");
                    } else {
                        throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'previewUrl' is null");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'policy' is null");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'name' is null");
        }
    }

    public SharedFolderMetadata(AccessLevel accessLevel, boolean z, boolean z2, String str, FolderPolicy folderPolicy, String str2, String str3, Date date) {
        this(accessLevel, z, z2, str, folderPolicy, str2, str3, date, null, null, null, null, null, null, AccessInheritance.INHERIT);
    }

    public AccessLevel getAccessType() {
        return this.accessType;
    }

    public boolean getIsInsideTeamFolder() {
        return this.isInsideTeamFolder;
    }

    public boolean getIsTeamFolder() {
        return this.isTeamFolder;
    }

    public String getName() {
        return this.name;
    }

    public FolderPolicy getPolicy() {
        return this.policy;
    }

    public String getPreviewUrl() {
        return this.previewUrl;
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public Date getTimeInvited() {
        return this.timeInvited;
    }

    public List<String> getOwnerDisplayNames() {
        return this.ownerDisplayNames;
    }

    public Team getOwnerTeam() {
        return this.ownerTeam;
    }

    public String getParentSharedFolderId() {
        return this.parentSharedFolderId;
    }

    public String getPathLower() {
        return this.pathLower;
    }

    public SharedContentLinkMetadata getLinkMetadata() {
        return this.linkMetadata;
    }

    public List<FolderPermission> getPermissions() {
        return this.permissions;
    }

    public AccessInheritance getAccessInheritance() {
        return this.accessInheritance;
    }

    public static Builder newBuilder(AccessLevel accessLevel, boolean z, boolean z2, String str, FolderPolicy folderPolicy, String str2, String str3, Date date) {
        Builder builder = new Builder(accessLevel, z, z2, str, folderPolicy, str2, str3, date);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.linkMetadata, this.name, this.permissions, this.policy, this.previewUrl, this.sharedFolderId, this.timeInvited, this.accessInheritance});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00e6, code lost:
        if (r2.equals(r5) == false) goto L_0x00e9;
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
            if (r2 == 0) goto L_0x00eb
            com.dropbox.core.v2.sharing.SharedFolderMetadata r5 = (com.dropbox.core.p005v2.sharing.SharedFolderMetadata) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0028:
            boolean r2 = r4.isInsideTeamFolder
            boolean r3 = r5.isInsideTeamFolder
            if (r2 != r3) goto L_0x00e9
            boolean r2 = r4.isTeamFolder
            boolean r3 = r5.isTeamFolder
            if (r2 != r3) goto L_0x00e9
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0040:
            com.dropbox.core.v2.sharing.FolderPolicy r2 = r4.policy
            com.dropbox.core.v2.sharing.FolderPolicy r3 = r5.policy
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x004c:
            java.lang.String r2 = r4.previewUrl
            java.lang.String r3 = r5.previewUrl
            if (r2 == r3) goto L_0x0058
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0058:
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0064
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0064:
            java.util.Date r2 = r4.timeInvited
            java.util.Date r3 = r5.timeInvited
            if (r2 == r3) goto L_0x0070
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0070:
            java.util.List r2 = r4.ownerDisplayNames
            java.util.List r3 = r5.ownerDisplayNames
            if (r2 == r3) goto L_0x0084
            java.util.List r2 = r4.ownerDisplayNames
            if (r2 == 0) goto L_0x00e9
            java.util.List r2 = r4.ownerDisplayNames
            java.util.List r3 = r5.ownerDisplayNames
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0084:
            com.dropbox.core.v2.users.Team r2 = r4.ownerTeam
            com.dropbox.core.v2.users.Team r3 = r5.ownerTeam
            if (r2 == r3) goto L_0x0098
            com.dropbox.core.v2.users.Team r2 = r4.ownerTeam
            if (r2 == 0) goto L_0x00e9
            com.dropbox.core.v2.users.Team r2 = r4.ownerTeam
            com.dropbox.core.v2.users.Team r3 = r5.ownerTeam
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x0098:
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            if (r2 == r3) goto L_0x00ac
            java.lang.String r2 = r4.parentSharedFolderId
            if (r2 == 0) goto L_0x00e9
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x00ac:
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            if (r2 == r3) goto L_0x00c0
            java.lang.String r2 = r4.pathLower
            if (r2 == 0) goto L_0x00e9
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x00c0:
            com.dropbox.core.v2.sharing.SharedContentLinkMetadata r2 = r4.linkMetadata
            com.dropbox.core.v2.sharing.SharedContentLinkMetadata r3 = r5.linkMetadata
            if (r2 == r3) goto L_0x00ce
            if (r2 == 0) goto L_0x00e9
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x00ce:
            java.util.List<com.dropbox.core.v2.sharing.FolderPermission> r2 = r4.permissions
            java.util.List<com.dropbox.core.v2.sharing.FolderPermission> r3 = r5.permissions
            if (r2 == r3) goto L_0x00dc
            if (r2 == 0) goto L_0x00e9
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00e9
        L_0x00dc:
            com.dropbox.core.v2.sharing.AccessInheritance r2 = r4.accessInheritance
            com.dropbox.core.v2.sharing.AccessInheritance r5 = r5.accessInheritance
            if (r2 == r5) goto L_0x00ea
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00e9
            goto L_0x00ea
        L_0x00e9:
            r0 = 0
        L_0x00ea:
            return r0
        L_0x00eb:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedFolderMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
