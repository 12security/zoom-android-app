package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFolder;
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
import java.util.Date;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.FolderLinkMetadata */
public class FolderLinkMetadata extends SharedLinkMetadata {

    /* renamed from: com.dropbox.core.v2.sharing.FolderLinkMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.SharedLinkMetadata.Builder {
        protected Builder(String str, String str2, LinkPermissions linkPermissions) {
            super(str, str2, linkPermissions);
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

        public FolderLinkMetadata build() {
            FolderLinkMetadata folderLinkMetadata = new FolderLinkMetadata(this.url, this.name, this.linkPermissions, this.f130id, this.expires, this.pathLower, this.teamMemberInfo, this.contentOwnerTeamInfo);
            return folderLinkMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.FolderLinkMetadata$Serializer */
    static class Serializer extends StructSerializer<FolderLinkMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FolderLinkMetadata folderLinkMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag(BoxFolder.TYPE, jsonGenerator);
            jsonGenerator.writeFieldName("url");
            StoneSerializers.string().serialize(folderLinkMetadata.url, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(folderLinkMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("link_permissions");
            Serializer.INSTANCE.serialize(folderLinkMetadata.linkPermissions, jsonGenerator);
            if (folderLinkMetadata.f129id != null) {
                jsonGenerator.writeFieldName("id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderLinkMetadata.f129id, jsonGenerator);
            }
            if (folderLinkMetadata.expires != null) {
                jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(folderLinkMetadata.expires, jsonGenerator);
            }
            if (folderLinkMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderLinkMetadata.pathLower, jsonGenerator);
            }
            if (folderLinkMetadata.teamMemberInfo != null) {
                jsonGenerator.writeFieldName("team_member_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(folderLinkMetadata.teamMemberInfo, jsonGenerator);
            }
            if (folderLinkMetadata.contentOwnerTeamInfo != null) {
                jsonGenerator.writeFieldName("content_owner_team_info");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE).serialize(folderLinkMetadata.contentOwnerTeamInfo, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FolderLinkMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if (BoxFolder.TYPE.equals(str)) {
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
                    FolderLinkMetadata folderLinkMetadata = new FolderLinkMetadata(str2, str3, linkPermissions, str4, date, str5, teamMemberInfo, team);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(folderLinkMetadata, folderLinkMetadata.toStringMultiline());
                    return folderLinkMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"link_permissions\" missing.");
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

    public FolderLinkMetadata(String str, String str2, LinkPermissions linkPermissions, String str3, Date date, String str4, TeamMemberInfo teamMemberInfo, Team team) {
        super(str, str2, linkPermissions, str3, date, str4, teamMemberInfo, team);
    }

    public FolderLinkMetadata(String str, String str2, LinkPermissions linkPermissions) {
        this(str, str2, linkPermissions, null, null, null, null, null);
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

    public static Builder newBuilder(String str, String str2, LinkPermissions linkPermissions) {
        return new Builder(str, str2, linkPermissions);
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
        FolderLinkMetadata folderLinkMetadata = (FolderLinkMetadata) obj;
        if ((this.url != folderLinkMetadata.url && !this.url.equals(folderLinkMetadata.url)) || ((this.name != folderLinkMetadata.name && !this.name.equals(folderLinkMetadata.name)) || ((this.linkPermissions != folderLinkMetadata.linkPermissions && !this.linkPermissions.equals(folderLinkMetadata.linkPermissions)) || ((this.f129id != folderLinkMetadata.f129id && (this.f129id == null || !this.f129id.equals(folderLinkMetadata.f129id))) || ((this.expires != folderLinkMetadata.expires && (this.expires == null || !this.expires.equals(folderLinkMetadata.expires))) || ((this.pathLower != folderLinkMetadata.pathLower && (this.pathLower == null || !this.pathLower.equals(folderLinkMetadata.pathLower))) || ((this.teamMemberInfo != folderLinkMetadata.teamMemberInfo && (this.teamMemberInfo == null || !this.teamMemberInfo.equals(folderLinkMetadata.teamMemberInfo))) || (this.contentOwnerTeamInfo != folderLinkMetadata.contentOwnerTeamInfo && (this.contentOwnerTeamInfo == null || !this.contentOwnerTeamInfo.equals(folderLinkMetadata.contentOwnerTeamInfo)))))))))) {
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
