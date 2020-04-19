package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
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
import java.util.regex.Pattern;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.FileLinkMetadata */
public class FileLinkMetadata extends SharedLinkMetadata {
    protected final Date clientModified;
    protected final String rev;
    protected final Date serverModified;
    protected final long size;

    /* renamed from: com.dropbox.core.v2.sharing.FileLinkMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.SharedLinkMetadata.Builder {
        protected final Date clientModified;
        protected final String rev;
        protected final Date serverModified;
        protected final long size;

        protected Builder(String str, String str2, LinkPermissions linkPermissions, Date date, Date date2, String str3, long j) {
            super(str, str2, linkPermissions);
            if (date != null) {
                this.clientModified = LangUtil.truncateMillis(date);
                if (date2 != null) {
                    this.serverModified = LangUtil.truncateMillis(date2);
                    if (str3 == null) {
                        throw new IllegalArgumentException("Required value for 'rev' is null");
                    } else if (str3.length() < 9) {
                        throw new IllegalArgumentException("String 'rev' is shorter than 9");
                    } else if (Pattern.matches("[0-9a-f]+", str3)) {
                        this.rev = str3;
                        this.size = j;
                    } else {
                        throw new IllegalArgumentException("String 'rev' does not match pattern");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'serverModified' is null");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'clientModified' is null");
            }
        }

        public Builder withId(String str) {
            super.withId(str);
            return this;
        }

        public Builder withExpires(Date date) {
            super.withExpires(date);
            return this;
        }

        public Builder withPathLower(String str) {
            super.withPathLower(str);
            return this;
        }

        public Builder withTeamMemberInfo(TeamMemberInfo teamMemberInfo) {
            super.withTeamMemberInfo(teamMemberInfo);
            return this;
        }

        public Builder withContentOwnerTeamInfo(Team team) {
            super.withContentOwnerTeamInfo(team);
            return this;
        }

        public FileLinkMetadata build() {
            FileLinkMetadata fileLinkMetadata = new FileLinkMetadata(this.url, this.name, this.linkPermissions, this.clientModified, this.serverModified, this.rev, this.size, this.f130id, this.expires, this.pathLower, this.teamMemberInfo, this.contentOwnerTeamInfo);
            return fileLinkMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.FileLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<FileLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileLinkMetadata fileLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag(BoxFile.TYPE, jsonGenerator);
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(fileLinkMetadata.url, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(fileLinkMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("link_permissions");
            Serializer.INSTANCE.serialize(fileLinkMetadata.linkPermissions, jsonGenerator);
            jsonGenerator.writeFieldName("client_modified");
            StoneSerializers.timestamp().serialize(fileLinkMetadata.clientModified, jsonGenerator);
            jsonGenerator.writeFieldName("server_modified");
            StoneSerializers.timestamp().serialize(fileLinkMetadata.serverModified, jsonGenerator);
            jsonGenerator.writeFieldName("rev");
            StoneSerializers.string().serialize(fileLinkMetadata.rev, jsonGenerator);
            jsonGenerator.writeFieldName("size");
            StoneSerializers.uInt64().serialize(Long.valueOf(fileLinkMetadata.size), jsonGenerator);
            if (fileLinkMetadata.f129id != null) {
                jsonGenerator.writeFieldName("id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileLinkMetadata.f129id, jsonGenerator);
            }
            if (fileLinkMetadata.expires != null) {
                jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(fileLinkMetadata.expires, jsonGenerator);
            }
            if (fileLinkMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileLinkMetadata.pathLower, jsonGenerator);
            }
            if (fileLinkMetadata.teamMemberInfo != null) {
                jsonGenerator.writeFieldName("team_member_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileLinkMetadata.teamMemberInfo, jsonGenerator);
            }
            if (fileLinkMetadata.contentOwnerTeamInfo != null) {
                jsonGenerator.writeFieldName("content_owner_team_info");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).serialize(fileLinkMetadata.contentOwnerTeamInfo, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if (BoxFile.TYPE.equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                LinkPermissions linkPermissions = null;
                Date date = null;
                Date date2 = null;
                String str4 = null;
                String str5 = null;
                Date date3 = null;
                String str6 = null;
                TeamMemberInfo teamMemberInfo = null;
                Team team = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("url".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("link_permissions".equals(currentName)) {
                        linkPermissions = (LinkPermissions) Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("client_modified".equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser2);
                    } else if ("server_modified".equals(currentName)) {
                        date2 = (Date) StoneSerializers.timestamp().deserialize(jsonParser2);
                    } else if ("rev".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("size".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser2);
                    } else if ("id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date3 = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser2);
                    } else if ("path_lower".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("team_member_info".equals(currentName)) {
                        teamMemberInfo = (TeamMemberInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("content_owner_team_info".equals(currentName)) {
                        team = (Team) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"url\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"name\" missing.");
                } else if (linkPermissions == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"link_permissions\" missing.");
                } else if (date == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"client_modified\" missing.");
                } else if (date2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"server_modified\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"rev\" missing.");
                } else if (l != null) {
                    FileLinkMetadata fileLinkMetadata = new FileLinkMetadata(str2, str3, linkPermissions, date, date2, str4, l.longValue(), str5, date3, str6, teamMemberInfo, team);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileLinkMetadata, fileLinkMetadata.toStringMultiline());
                    return fileLinkMetadata;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"size\" missing.");
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

    public FileLinkMetadata(String str, String str2, LinkPermissions linkPermissions, Date date, Date date2, String str3, long j, String str4, Date date3, String str5, TeamMemberInfo teamMemberInfo, Team team) {
        String str6 = str3;
        super(str, str2, linkPermissions, str4, date3, str5, teamMemberInfo, team);
        if (date != null) {
            this.clientModified = LangUtil.truncateMillis(date);
            if (date2 != null) {
                this.serverModified = LangUtil.truncateMillis(date2);
                if (str6 == null) {
                    throw new IllegalArgumentException("Required value for 'rev' is null");
                } else if (str3.length() < 9) {
                    throw new IllegalArgumentException("String 'rev' is shorter than 9");
                } else if (Pattern.matches("[0-9a-f]+", str6)) {
                    this.rev = str6;
                    this.size = j;
                } else {
                    throw new IllegalArgumentException("String 'rev' does not match pattern");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'serverModified' is null");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'clientModified' is null");
        }
    }

    public FileLinkMetadata(String str, String str2, LinkPermissions linkPermissions, Date date, Date date2, String str3, long j) {
        this(str, str2, linkPermissions, date, date2, str3, j, null, null, null, null, null);
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

    public Date getClientModified() {
        return this.clientModified;
    }

    public Date getServerModified() {
        return this.serverModified;
    }

    public String getRev() {
        return this.rev;
    }

    public long getSize() {
        return this.size;
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

    public static Builder newBuilder(String str, String str2, LinkPermissions linkPermissions, Date date, Date date2, String str3, long j) {
        Builder builder = new Builder(str, str2, linkPermissions, date, date2, str3, j);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.clientModified, this.serverModified, this.rev, Long.valueOf(this.size)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006a, code lost:
        if (r2.equals(r3) == false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0086, code lost:
        if (r6.f129id.equals(r7.f129id) == false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009a, code lost:
        if (r6.expires.equals(r7.expires) == false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ae, code lost:
        if (r6.pathLower.equals(r7.pathLower) == false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00c2, code lost:
        if (r6.teamMemberInfo.equals(r7.teamMemberInfo) == false) goto L_0x00d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d6, code lost:
        if (r6.contentOwnerTeamInfo.equals(r7.contentOwnerTeamInfo) == false) goto L_0x00d9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00db
            com.dropbox.core.v2.sharing.FileLinkMetadata r7 = (com.dropbox.core.p005v2.sharing.FileLinkMetadata) r7
            java.lang.String r2 = r6.url
            java.lang.String r3 = r7.url
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r6.url
            java.lang.String r3 = r7.url
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x0028:
            java.lang.String r2 = r6.name
            java.lang.String r3 = r7.name
            if (r2 == r3) goto L_0x0038
            java.lang.String r2 = r6.name
            java.lang.String r3 = r7.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x0038:
            com.dropbox.core.v2.sharing.LinkPermissions r2 = r6.linkPermissions
            com.dropbox.core.v2.sharing.LinkPermissions r3 = r7.linkPermissions
            if (r2 == r3) goto L_0x0048
            com.dropbox.core.v2.sharing.LinkPermissions r2 = r6.linkPermissions
            com.dropbox.core.v2.sharing.LinkPermissions r3 = r7.linkPermissions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x0048:
            java.util.Date r2 = r6.clientModified
            java.util.Date r3 = r7.clientModified
            if (r2 == r3) goto L_0x0054
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x0054:
            java.util.Date r2 = r6.serverModified
            java.util.Date r3 = r7.serverModified
            if (r2 == r3) goto L_0x0060
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x0060:
            java.lang.String r2 = r6.rev
            java.lang.String r3 = r7.rev
            if (r2 == r3) goto L_0x006c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x006c:
            long r2 = r6.size
            long r4 = r7.size
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x00d9
            java.lang.String r2 = r6.f129id
            java.lang.String r3 = r7.f129id
            if (r2 == r3) goto L_0x0088
            java.lang.String r2 = r6.f129id
            if (r2 == 0) goto L_0x00d9
            java.lang.String r2 = r6.f129id
            java.lang.String r3 = r7.f129id
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x0088:
            java.util.Date r2 = r6.expires
            java.util.Date r3 = r7.expires
            if (r2 == r3) goto L_0x009c
            java.util.Date r2 = r6.expires
            if (r2 == 0) goto L_0x00d9
            java.util.Date r2 = r6.expires
            java.util.Date r3 = r7.expires
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x009c:
            java.lang.String r2 = r6.pathLower
            java.lang.String r3 = r7.pathLower
            if (r2 == r3) goto L_0x00b0
            java.lang.String r2 = r6.pathLower
            if (r2 == 0) goto L_0x00d9
            java.lang.String r2 = r6.pathLower
            java.lang.String r3 = r7.pathLower
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x00b0:
            com.dropbox.core.v2.sharing.TeamMemberInfo r2 = r6.teamMemberInfo
            com.dropbox.core.v2.sharing.TeamMemberInfo r3 = r7.teamMemberInfo
            if (r2 == r3) goto L_0x00c4
            com.dropbox.core.v2.sharing.TeamMemberInfo r2 = r6.teamMemberInfo
            if (r2 == 0) goto L_0x00d9
            com.dropbox.core.v2.sharing.TeamMemberInfo r2 = r6.teamMemberInfo
            com.dropbox.core.v2.sharing.TeamMemberInfo r3 = r7.teamMemberInfo
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00d9
        L_0x00c4:
            com.dropbox.core.v2.users.Team r2 = r6.contentOwnerTeamInfo
            com.dropbox.core.v2.users.Team r3 = r7.contentOwnerTeamInfo
            if (r2 == r3) goto L_0x00da
            com.dropbox.core.v2.users.Team r2 = r6.contentOwnerTeamInfo
            if (r2 == 0) goto L_0x00d9
            com.dropbox.core.v2.users.Team r2 = r6.contentOwnerTeamInfo
            com.dropbox.core.v2.users.Team r7 = r7.contentOwnerTeamInfo
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x00d9
            goto L_0x00da
        L_0x00d9:
            r0 = 0
        L_0x00da:
            return r0
        L_0x00db:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.FileLinkMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
