package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.users.Team;
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
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.SharedFolderMetadataBase */
public class SharedFolderMetadataBase {
    protected final AccessLevel accessType;
    protected final boolean isInsideTeamFolder;
    protected final boolean isTeamFolder;
    protected final List<String> ownerDisplayNames;
    protected final Team ownerTeam;
    protected final String parentSharedFolderId;
    protected final String pathLower;

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderMetadataBase$Builder */
    public static class Builder {
        protected final AccessLevel accessType;
        protected final boolean isInsideTeamFolder;
        protected final boolean isTeamFolder;
        protected List<String> ownerDisplayNames;
        protected Team ownerTeam;
        protected String parentSharedFolderId;
        protected String pathLower;

        protected Builder(AccessLevel accessLevel, boolean z, boolean z2) {
            if (accessLevel != null) {
                this.accessType = accessLevel;
                this.isInsideTeamFolder = z;
                this.isTeamFolder = z2;
                this.ownerDisplayNames = null;
                this.ownerTeam = null;
                this.parentSharedFolderId = null;
                this.pathLower = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'accessType' is null");
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

        public Builder withPathLower(String str) {
            this.pathLower = str;
            return this;
        }

        public SharedFolderMetadataBase build() {
            SharedFolderMetadataBase sharedFolderMetadataBase = new SharedFolderMetadataBase(this.accessType, this.isInsideTeamFolder, this.isTeamFolder, this.ownerDisplayNames, this.ownerTeam, this.parentSharedFolderId, this.pathLower);
            return sharedFolderMetadataBase;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderMetadataBase$Serializer */
    private static class Serializer extends StructSerializer<SharedFolderMetadataBase> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(SharedFolderMetadataBase sharedFolderMetadataBase, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_type");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedFolderMetadataBase.accessType, jsonGenerator);
            jsonGenerator.writeFieldName("is_inside_team_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(sharedFolderMetadataBase.isInsideTeamFolder), jsonGenerator);
            jsonGenerator.writeFieldName("is_team_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(sharedFolderMetadataBase.isTeamFolder), jsonGenerator);
            if (sharedFolderMetadataBase.ownerDisplayNames != null) {
                jsonGenerator.writeFieldName("owner_display_names");
                StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).serialize(sharedFolderMetadataBase.ownerDisplayNames, jsonGenerator);
            }
            if (sharedFolderMetadataBase.ownerTeam != null) {
                jsonGenerator.writeFieldName("owner_team");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).serialize(sharedFolderMetadataBase.ownerTeam, jsonGenerator);
            }
            if (sharedFolderMetadataBase.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderMetadataBase.parentSharedFolderId, jsonGenerator);
            }
            if (sharedFolderMetadataBase.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderMetadataBase.pathLower, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedFolderMetadataBase deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool2 = null;
                AccessLevel accessLevel = null;
                List list = null;
                Team team = null;
                String str2 = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_type".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("is_inside_team_folder".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("is_team_folder".equals(currentName)) {
                        bool2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("owner_display_names".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).deserialize(jsonParser);
                    } else if ("owner_team".equals(currentName)) {
                        team = (Team) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("path_lower".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_inside_team_folder\" missing.");
                } else if (bool2 != null) {
                    SharedFolderMetadataBase sharedFolderMetadataBase = new SharedFolderMetadataBase(accessLevel, bool.booleanValue(), bool2.booleanValue(), list, team, str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedFolderMetadataBase, sharedFolderMetadataBase.toStringMultiline());
                    return sharedFolderMetadataBase;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"is_team_folder\" missing.");
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

    public SharedFolderMetadataBase(AccessLevel accessLevel, boolean z, boolean z2, List<String> list, Team team, String str, String str2) {
        if (accessLevel != null) {
            this.accessType = accessLevel;
            this.isInsideTeamFolder = z;
            this.isTeamFolder = z2;
            if (list != null) {
                for (String str3 : list) {
                    if (str3 == null) {
                        throw new IllegalArgumentException("An item in list 'ownerDisplayNames' is null");
                    }
                }
            }
            this.ownerDisplayNames = list;
            this.ownerTeam = team;
            if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.parentSharedFolderId = str;
                this.pathLower = str2;
                return;
            }
            throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
        }
        throw new IllegalArgumentException("Required value for 'accessType' is null");
    }

    public SharedFolderMetadataBase(AccessLevel accessLevel, boolean z, boolean z2) {
        this(accessLevel, z, z2, null, null, null, null);
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

    public static Builder newBuilder(AccessLevel accessLevel, boolean z, boolean z2) {
        return new Builder(accessLevel, z, z2);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessType, Boolean.valueOf(this.isInsideTeamFolder), Boolean.valueOf(this.isTeamFolder), this.ownerDisplayNames, this.ownerTeam, this.parentSharedFolderId, this.pathLower});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0066, code lost:
        if (r2.equals(r5) == false) goto L_0x0069;
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
            if (r2 == 0) goto L_0x006b
            com.dropbox.core.v2.sharing.SharedFolderMetadataBase r5 = (com.dropbox.core.p005v2.sharing.SharedFolderMetadataBase) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0069
        L_0x0024:
            boolean r2 = r4.isInsideTeamFolder
            boolean r3 = r5.isInsideTeamFolder
            if (r2 != r3) goto L_0x0069
            boolean r2 = r4.isTeamFolder
            boolean r3 = r5.isTeamFolder
            if (r2 != r3) goto L_0x0069
            java.util.List<java.lang.String> r2 = r4.ownerDisplayNames
            java.util.List<java.lang.String> r3 = r5.ownerDisplayNames
            if (r2 == r3) goto L_0x003e
            if (r2 == 0) goto L_0x0069
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0069
        L_0x003e:
            com.dropbox.core.v2.users.Team r2 = r4.ownerTeam
            com.dropbox.core.v2.users.Team r3 = r5.ownerTeam
            if (r2 == r3) goto L_0x004c
            if (r2 == 0) goto L_0x0069
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0069
        L_0x004c:
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            if (r2 == r3) goto L_0x005a
            if (r2 == 0) goto L_0x0069
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0069
        L_0x005a:
            java.lang.String r2 = r4.pathLower
            java.lang.String r5 = r5.pathLower
            if (r2 == r5) goto L_0x006a
            if (r2 == 0) goto L_0x0069
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0069
            goto L_0x006a
        L_0x0069:
            r0 = 0
        L_0x006a:
            return r0
        L_0x006b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedFolderMetadataBase.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
