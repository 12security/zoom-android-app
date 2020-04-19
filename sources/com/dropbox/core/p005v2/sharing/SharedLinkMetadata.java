package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
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
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkMetadata */
public class SharedLinkMetadata {
    protected final Team contentOwnerTeamInfo;
    protected final Date expires;

    /* renamed from: id */
    protected final String f129id;
    protected final LinkPermissions linkPermissions;
    protected final String name;
    protected final String pathLower;
    protected final TeamMemberInfo teamMemberInfo;
    protected final String url;

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkMetadata$Builder */
    public static class Builder {
        protected Team contentOwnerTeamInfo;
        protected Date expires;

        /* renamed from: id */
        protected String f130id;
        protected final LinkPermissions linkPermissions;
        protected final String name;
        protected String pathLower;
        protected TeamMemberInfo teamMemberInfo;
        protected final String url;

        protected Builder(String str, String str2, LinkPermissions linkPermissions2) {
            if (str != null) {
                this.url = str;
                if (str2 != null) {
                    this.name = str2;
                    if (linkPermissions2 != null) {
                        this.linkPermissions = linkPermissions2;
                        this.f130id = null;
                        this.expires = null;
                        this.pathLower = null;
                        this.teamMemberInfo = null;
                        this.contentOwnerTeamInfo = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'linkPermissions' is null");
                }
                throw new IllegalArgumentException("Required value for 'name' is null");
            }
            throw new IllegalArgumentException("Required value for 'url' is null");
        }

        public Builder withId(String str) {
            if (str == null || str.length() >= 1) {
                this.f130id = str;
                return this;
            }
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        }

        public Builder withExpires(Date date) {
            this.expires = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withPathLower(String str) {
            this.pathLower = str;
            return this;
        }

        public Builder withTeamMemberInfo(TeamMemberInfo teamMemberInfo2) {
            this.teamMemberInfo = teamMemberInfo2;
            return this;
        }

        public Builder withContentOwnerTeamInfo(Team team) {
            this.contentOwnerTeamInfo = team;
            return this;
        }

        public SharedLinkMetadata build() {
            SharedLinkMetadata sharedLinkMetadata = new SharedLinkMetadata(this.url, this.name, this.linkPermissions, this.f130id, this.expires, this.pathLower, this.teamMemberInfo, this.contentOwnerTeamInfo);
            return sharedLinkMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<SharedLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkMetadata sharedLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (sharedLinkMetadata instanceof FileLinkMetadata) {
                Serializer.INSTANCE.serialize((FileLinkMetadata) sharedLinkMetadata, jsonGenerator, z);
            } else if (sharedLinkMetadata instanceof FolderLinkMetadata) {
                Serializer.INSTANCE.serialize((FolderLinkMetadata) sharedLinkMetadata, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                jsonGenerator.writeFieldName("url");
                StoneSerializers.string().serialize(sharedLinkMetadata.url, jsonGenerator);
                jsonGenerator.writeFieldName("name");
                StoneSerializers.string().serialize(sharedLinkMetadata.name, jsonGenerator);
                jsonGenerator.writeFieldName("link_permissions");
                Serializer.INSTANCE.serialize(sharedLinkMetadata.linkPermissions, jsonGenerator);
                if (sharedLinkMetadata.f129id != null) {
                    jsonGenerator.writeFieldName("id");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedLinkMetadata.f129id, jsonGenerator);
                }
                if (sharedLinkMetadata.expires != null) {
                    jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                    StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedLinkMetadata.expires, jsonGenerator);
                }
                if (sharedLinkMetadata.pathLower != null) {
                    jsonGenerator.writeFieldName("path_lower");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedLinkMetadata.pathLower, jsonGenerator);
                }
                if (sharedLinkMetadata.teamMemberInfo != null) {
                    jsonGenerator.writeFieldName("team_member_info");
                    StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedLinkMetadata.teamMemberInfo, jsonGenerator);
                }
                if (sharedLinkMetadata.contentOwnerTeamInfo != null) {
                    jsonGenerator.writeFieldName("content_owner_team_info");
                    StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).serialize(sharedLinkMetadata.contentOwnerTeamInfo, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public SharedLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SharedLinkMetadata sharedLinkMetadata;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                LinkPermissions linkPermissions = null;
                String str4 = null;
                Date date = null;
                String str5 = null;
                TeamMemberInfo teamMemberInfo = null;
                Team team = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("link_permissions".equals(currentName)) {
                        linkPermissions = (LinkPermissions) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("path_lower".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("team_member_info".equals(currentName)) {
                        teamMemberInfo = (TeamMemberInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("content_owner_team_info".equals(currentName)) {
                        team = (Team) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"url\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (linkPermissions != null) {
                    sharedLinkMetadata = new SharedLinkMetadata(str2, str3, linkPermissions, str4, date, str5, teamMemberInfo, team);
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"link_permissions\" missing.");
                }
            } else if ("".equals(str)) {
                sharedLinkMetadata = INSTANCE.deserialize(jsonParser, true);
            } else if (BoxFile.TYPE.equals(str)) {
                sharedLinkMetadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if (BoxFolder.TYPE.equals(str)) {
                sharedLinkMetadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
            if (!z) {
                expectEndObject(jsonParser);
            }
            StoneDeserializerLogger.log(sharedLinkMetadata, sharedLinkMetadata.toStringMultiline());
            return sharedLinkMetadata;
        }
    }

    public SharedLinkMetadata(String str, String str2, LinkPermissions linkPermissions2, String str3, Date date, String str4, TeamMemberInfo teamMemberInfo2, Team team) {
        if (str != null) {
            this.url = str;
            if (str3 == null || str3.length() >= 1) {
                this.f129id = str3;
                if (str2 != null) {
                    this.name = str2;
                    this.expires = LangUtil.truncateMillis(date);
                    this.pathLower = str4;
                    if (linkPermissions2 != null) {
                        this.linkPermissions = linkPermissions2;
                        this.teamMemberInfo = teamMemberInfo2;
                        this.contentOwnerTeamInfo = team;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'linkPermissions' is null");
                }
                throw new IllegalArgumentException("Required value for 'name' is null");
            }
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        }
        throw new IllegalArgumentException("Required value for 'url' is null");
    }

    public SharedLinkMetadata(String str, String str2, LinkPermissions linkPermissions2) {
        this(str, str2, linkPermissions2, null, null, null, null, null);
    }

    public String getUrl() {
        return this.url;
    }

    public String getName() {
        return this.name;
    }

    public LinkPermissions getLinkPermissions() {
        return this.linkPermissions;
    }

    public String getId() {
        return this.f129id;
    }

    public Date getExpires() {
        return this.expires;
    }

    public String getPathLower() {
        return this.pathLower;
    }

    public TeamMemberInfo getTeamMemberInfo() {
        return this.teamMemberInfo;
    }

    public Team getContentOwnerTeamInfo() {
        return this.contentOwnerTeamInfo;
    }

    public static Builder newBuilder(String str, String str2, LinkPermissions linkPermissions2) {
        return new Builder(str, str2, linkPermissions2);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.url, this.f129id, this.name, this.expires, this.pathLower, this.linkPermissions, this.teamMemberInfo, this.contentOwnerTeamInfo});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0080, code lost:
        if (r2.equals(r5) == false) goto L_0x0083;
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
            if (r2 == 0) goto L_0x0085
            com.dropbox.core.v2.sharing.SharedLinkMetadata r5 = (com.dropbox.core.p005v2.sharing.SharedLinkMetadata) r5
            java.lang.String r2 = r4.url
            java.lang.String r3 = r5.url
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0024:
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0030:
            com.dropbox.core.v2.sharing.LinkPermissions r2 = r4.linkPermissions
            com.dropbox.core.v2.sharing.LinkPermissions r3 = r5.linkPermissions
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x003c:
            java.lang.String r2 = r4.f129id
            java.lang.String r3 = r5.f129id
            if (r2 == r3) goto L_0x004a
            if (r2 == 0) goto L_0x0083
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x004a:
            java.util.Date r2 = r4.expires
            java.util.Date r3 = r5.expires
            if (r2 == r3) goto L_0x0058
            if (r2 == 0) goto L_0x0083
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0058:
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            if (r2 == r3) goto L_0x0066
            if (r2 == 0) goto L_0x0083
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0066:
            com.dropbox.core.v2.sharing.TeamMemberInfo r2 = r4.teamMemberInfo
            com.dropbox.core.v2.sharing.TeamMemberInfo r3 = r5.teamMemberInfo
            if (r2 == r3) goto L_0x0074
            if (r2 == 0) goto L_0x0083
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0074:
            com.dropbox.core.v2.users.Team r2 = r4.contentOwnerTeamInfo
            com.dropbox.core.v2.users.Team r5 = r5.contentOwnerTeamInfo
            if (r2 == r5) goto L_0x0084
            if (r2 == 0) goto L_0x0083
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0083
            goto L_0x0084
        L_0x0083:
            r0 = 0
        L_0x0084:
            return r0
        L_0x0085:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedLinkMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
