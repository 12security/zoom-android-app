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

/* renamed from: com.dropbox.core.v2.sharing.SharedFileMetadata */
public class SharedFileMetadata {
    protected final AccessLevel accessType;
    protected final ExpectedSharedContentLinkMetadata expectedLinkMetadata;

    /* renamed from: id */
    protected final String f125id;
    protected final SharedContentLinkMetadata linkMetadata;
    protected final String name;
    protected final List<String> ownerDisplayNames;
    protected final Team ownerTeam;
    protected final String parentSharedFolderId;
    protected final String pathDisplay;
    protected final String pathLower;
    protected final List<FilePermission> permissions;
    protected final FolderPolicy policy;
    protected final String previewUrl;
    protected final Date timeInvited;

    /* renamed from: com.dropbox.core.v2.sharing.SharedFileMetadata$Builder */
    public static class Builder {
        protected AccessLevel accessType;
        protected ExpectedSharedContentLinkMetadata expectedLinkMetadata;

        /* renamed from: id */
        protected final String f126id;
        protected SharedContentLinkMetadata linkMetadata;
        protected final String name;
        protected List<String> ownerDisplayNames;
        protected Team ownerTeam;
        protected String parentSharedFolderId;
        protected String pathDisplay;
        protected String pathLower;
        protected List<FilePermission> permissions;
        protected final FolderPolicy policy;
        protected final String previewUrl;
        protected Date timeInvited;

        protected Builder(String str, String str2, FolderPolicy folderPolicy, String str3) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'id' is null");
            } else if (str.length() < 4) {
                throw new IllegalArgumentException("String 'id' is shorter than 4");
            } else if (Pattern.matches("id:.+", str)) {
                this.f126id = str;
                if (str2 != null) {
                    this.name = str2;
                    if (folderPolicy != null) {
                        this.policy = folderPolicy;
                        if (str3 != null) {
                            this.previewUrl = str3;
                            this.accessType = null;
                            this.expectedLinkMetadata = null;
                            this.linkMetadata = null;
                            this.ownerDisplayNames = null;
                            this.ownerTeam = null;
                            this.parentSharedFolderId = null;
                            this.pathDisplay = null;
                            this.pathLower = null;
                            this.permissions = null;
                            this.timeInvited = null;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'previewUrl' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'policy' is null");
                }
                throw new IllegalArgumentException("Required value for 'name' is null");
            } else {
                throw new IllegalArgumentException("String 'id' does not match pattern");
            }
        }

        public Builder withAccessType(AccessLevel accessLevel) {
            this.accessType = accessLevel;
            return this;
        }

        public Builder withExpectedLinkMetadata(ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata) {
            this.expectedLinkMetadata = expectedSharedContentLinkMetadata;
            return this;
        }

        public Builder withLinkMetadata(SharedContentLinkMetadata sharedContentLinkMetadata) {
            this.linkMetadata = sharedContentLinkMetadata;
            return this;
        }

        public Builder withOwnerDisplayNames(List<String> list) {
            if (list != null) {
                for (String str : list) {
                    if (str == null) {
                        throw new IllegalArgumentException("An item in list 'ownerDisplayNames' is null");
                    }
                }
            }
            this.ownerDisplayNames = list;
            return this;
        }

        public Builder withOwnerTeam(Team team) {
            this.ownerTeam = team;
            return this;
        }

        public Builder withParentSharedFolderId(String str) {
            if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.parentSharedFolderId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
        }

        public Builder withPathDisplay(String str) {
            this.pathDisplay = str;
            return this;
        }

        public Builder withPathLower(String str) {
            this.pathLower = str;
            return this;
        }

        public Builder withPermissions(List<FilePermission> list) {
            if (list != null) {
                for (FilePermission filePermission : list) {
                    if (filePermission == null) {
                        throw new IllegalArgumentException("An item in list 'permissions' is null");
                    }
                }
            }
            this.permissions = list;
            return this;
        }

        public Builder withTimeInvited(Date date) {
            this.timeInvited = LangUtil.truncateMillis(date);
            return this;
        }

        public SharedFileMetadata build() {
            SharedFileMetadata sharedFileMetadata = new SharedFileMetadata(this.f126id, this.name, this.policy, this.previewUrl, this.accessType, this.expectedLinkMetadata, this.linkMetadata, this.ownerDisplayNames, this.ownerTeam, this.parentSharedFolderId, this.pathDisplay, this.pathLower, this.permissions, this.timeInvited);
            return sharedFileMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedFileMetadata$Serializer */
    static class Serializer extends StructSerializer<SharedFileMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedFileMetadata sharedFileMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(sharedFileMetadata.f125id, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(sharedFileMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("policy");
            Serializer.INSTANCE.serialize(sharedFileMetadata.policy, jsonGenerator);
            jsonGenerator.writeFieldName("preview_url");
            StoneSerializers.string().serialize(sharedFileMetadata.previewUrl, jsonGenerator);
            if (sharedFileMetadata.accessType != null) {
                jsonGenerator.writeFieldName("access_type");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(sharedFileMetadata.accessType, jsonGenerator);
            }
            if (sharedFileMetadata.expectedLinkMetadata != null) {
                jsonGenerator.writeFieldName("expected_link_metadata");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedFileMetadata.expectedLinkMetadata, jsonGenerator);
            }
            if (sharedFileMetadata.linkMetadata != null) {
                jsonGenerator.writeFieldName("link_metadata");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedFileMetadata.linkMetadata, jsonGenerator);
            }
            if (sharedFileMetadata.ownerDisplayNames != null) {
                jsonGenerator.writeFieldName("owner_display_names");
                StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).serialize(sharedFileMetadata.ownerDisplayNames, jsonGenerator);
            }
            if (sharedFileMetadata.ownerTeam != null) {
                jsonGenerator.writeFieldName("owner_team");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).serialize(sharedFileMetadata.ownerTeam, jsonGenerator);
            }
            if (sharedFileMetadata.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFileMetadata.parentSharedFolderId, jsonGenerator);
            }
            if (sharedFileMetadata.pathDisplay != null) {
                jsonGenerator.writeFieldName("path_display");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFileMetadata.pathDisplay, jsonGenerator);
            }
            if (sharedFileMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFileMetadata.pathLower, jsonGenerator);
            }
            if (sharedFileMetadata.permissions != null) {
                jsonGenerator.writeFieldName("permissions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(sharedFileMetadata.permissions, jsonGenerator);
            }
            if (sharedFileMetadata.timeInvited != null) {
                jsonGenerator.writeFieldName("time_invited");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedFileMetadata.timeInvited, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedFileMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                FolderPolicy folderPolicy = null;
                String str4 = null;
                AccessLevel accessLevel = null;
                ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata = null;
                SharedContentLinkMetadata sharedContentLinkMetadata = null;
                List list = null;
                Team team = null;
                String str5 = null;
                String str6 = null;
                String str7 = null;
                List list2 = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("policy".equals(currentName)) {
                        folderPolicy = (FolderPolicy) Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("preview_url".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("access_type".equals(currentName)) {
                        accessLevel = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("expected_link_metadata".equals(currentName)) {
                        expectedSharedContentLinkMetadata = (ExpectedSharedContentLinkMetadata) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("link_metadata".equals(currentName)) {
                        sharedContentLinkMetadata = (SharedContentLinkMetadata) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("owner_display_names".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).deserialize(jsonParser2);
                    } else if ("owner_team".equals(currentName)) {
                        team = (Team) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("path_display".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("path_lower".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("permissions".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser2);
                    } else if ("time_invited".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"name\" missing.");
                } else if (folderPolicy == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"policy\" missing.");
                } else if (str4 != null) {
                    SharedFileMetadata sharedFileMetadata = new SharedFileMetadata(str2, str3, folderPolicy, str4, accessLevel, expectedSharedContentLinkMetadata, sharedContentLinkMetadata, list, team, str5, str6, str7, list2, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedFileMetadata, sharedFileMetadata.toStringMultiline());
                    return sharedFileMetadata;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"preview_url\" missing.");
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

    public SharedFileMetadata(String str, String str2, FolderPolicy folderPolicy, String str3, AccessLevel accessLevel, ExpectedSharedContentLinkMetadata expectedSharedContentLinkMetadata, SharedContentLinkMetadata sharedContentLinkMetadata, List<String> list, Team team, String str4, String str5, String str6, List<FilePermission> list2, Date date) {
        this.accessType = accessLevel;
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str.length() < 4) {
            throw new IllegalArgumentException("String 'id' is shorter than 4");
        } else if (Pattern.matches("id:.+", str)) {
            this.f125id = str;
            this.expectedLinkMetadata = expectedSharedContentLinkMetadata;
            this.linkMetadata = sharedContentLinkMetadata;
            if (str2 != null) {
                this.name = str2;
                if (list != null) {
                    for (String str7 : list) {
                        if (str7 == null) {
                            throw new IllegalArgumentException("An item in list 'ownerDisplayNames' is null");
                        }
                    }
                }
                this.ownerDisplayNames = list;
                this.ownerTeam = team;
                if (str4 == null || Pattern.matches("[-_0-9a-zA-Z:]+", str4)) {
                    this.parentSharedFolderId = str4;
                    this.pathDisplay = str5;
                    this.pathLower = str6;
                    if (list2 != null) {
                        for (FilePermission filePermission : list2) {
                            if (filePermission == null) {
                                throw new IllegalArgumentException("An item in list 'permissions' is null");
                            }
                        }
                    }
                    this.permissions = list2;
                    if (folderPolicy != null) {
                        this.policy = folderPolicy;
                        if (str3 != null) {
                            this.previewUrl = str3;
                            this.timeInvited = LangUtil.truncateMillis(date);
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'previewUrl' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'policy' is null");
                }
                throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
            }
            throw new IllegalArgumentException("Required value for 'name' is null");
        } else {
            throw new IllegalArgumentException("String 'id' does not match pattern");
        }
    }

    public SharedFileMetadata(String str, String str2, FolderPolicy folderPolicy, String str3) {
        this(str, str2, folderPolicy, str3, null, null, null, null, null, null, null, null, null, null);
    }

    public String getId() {
        return this.f125id;
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

    public AccessLevel getAccessType() {
        return this.accessType;
    }

    public ExpectedSharedContentLinkMetadata getExpectedLinkMetadata() {
        return this.expectedLinkMetadata;
    }

    public SharedContentLinkMetadata getLinkMetadata() {
        return this.linkMetadata;
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

    public String getPathDisplay() {
        return this.pathDisplay;
    }

    public String getPathLower() {
        return this.pathLower;
    }

    public List<FilePermission> getPermissions() {
        return this.permissions;
    }

    public Date getTimeInvited() {
        return this.timeInvited;
    }

    public static Builder newBuilder(String str, String str2, FolderPolicy folderPolicy, String str3) {
        return new Builder(str, str2, folderPolicy, str3);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessType, this.f125id, this.expectedLinkMetadata, this.linkMetadata, this.name, this.ownerDisplayNames, this.ownerTeam, this.parentSharedFolderId, this.pathDisplay, this.pathLower, this.permissions, this.policy, this.previewUrl, this.timeInvited});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00d2, code lost:
        if (r2.equals(r5) == false) goto L_0x00d5;
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
            if (r2 == 0) goto L_0x00d7
            com.dropbox.core.v2.sharing.SharedFileMetadata r5 = (com.dropbox.core.p005v2.sharing.SharedFileMetadata) r5
            java.lang.String r2 = r4.f125id
            java.lang.String r3 = r5.f125id
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0024:
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0030:
            com.dropbox.core.v2.sharing.FolderPolicy r2 = r4.policy
            com.dropbox.core.v2.sharing.FolderPolicy r3 = r5.policy
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x003c:
            java.lang.String r2 = r4.previewUrl
            java.lang.String r3 = r5.previewUrl
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0048:
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0056
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0056:
            com.dropbox.core.v2.sharing.ExpectedSharedContentLinkMetadata r2 = r4.expectedLinkMetadata
            com.dropbox.core.v2.sharing.ExpectedSharedContentLinkMetadata r3 = r5.expectedLinkMetadata
            if (r2 == r3) goto L_0x0064
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0064:
            com.dropbox.core.v2.sharing.SharedContentLinkMetadata r2 = r4.linkMetadata
            com.dropbox.core.v2.sharing.SharedContentLinkMetadata r3 = r5.linkMetadata
            if (r2 == r3) goto L_0x0072
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0072:
            java.util.List<java.lang.String> r2 = r4.ownerDisplayNames
            java.util.List<java.lang.String> r3 = r5.ownerDisplayNames
            if (r2 == r3) goto L_0x0080
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x0080:
            com.dropbox.core.v2.users.Team r2 = r4.ownerTeam
            com.dropbox.core.v2.users.Team r3 = r5.ownerTeam
            if (r2 == r3) goto L_0x008e
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x008e:
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            if (r2 == r3) goto L_0x009c
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x009c:
            java.lang.String r2 = r4.pathDisplay
            java.lang.String r3 = r5.pathDisplay
            if (r2 == r3) goto L_0x00aa
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x00aa:
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            if (r2 == r3) goto L_0x00b8
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x00b8:
            java.util.List<com.dropbox.core.v2.sharing.FilePermission> r2 = r4.permissions
            java.util.List<com.dropbox.core.v2.sharing.FilePermission> r3 = r5.permissions
            if (r2 == r3) goto L_0x00c6
            if (r2 == 0) goto L_0x00d5
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d5
        L_0x00c6:
            java.util.Date r2 = r4.timeInvited
            java.util.Date r5 = r5.timeInvited
            if (r2 == r5) goto L_0x00d6
            if (r2 == 0) goto L_0x00d5
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00d5
            goto L_0x00d6
        L_0x00d5:
            r0 = 0
        L_0x00d6:
            return r0
        L_0x00d7:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedFileMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
