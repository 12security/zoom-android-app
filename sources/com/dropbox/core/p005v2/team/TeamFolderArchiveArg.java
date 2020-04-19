package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveArg */
class TeamFolderArchiveArg extends TeamFolderIdArg {
    protected final boolean forceAsyncOff;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderArchiveArg$Serializer */
    static class Serializer extends StructSerializer<TeamFolderArchiveArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderArchiveArg teamFolderArchiveArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_folder_id");
            StoneSerializers.string().serialize(teamFolderArchiveArg.teamFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("force_async_off");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(teamFolderArchiveArg.forceAsyncOff), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderArchiveArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("force_async_off".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    TeamFolderArchiveArg teamFolderArchiveArg = new TeamFolderArchiveArg(str2, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderArchiveArg, teamFolderArchiveArg.toStringMultiline());
                    return teamFolderArchiveArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"team_folder_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderArchiveArg(String str, boolean z) {
        super(str);
        this.forceAsyncOff = z;
    }

    public TeamFolderArchiveArg(String str) {
        this(str, false);
    }

    public String getTeamFolderId() {
        return this.teamFolderId;
    }

    public boolean getForceAsyncOff() {
        return this.forceAsyncOff;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Boolean.valueOf(this.forceAsyncOff)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        TeamFolderArchiveArg teamFolderArchiveArg = (TeamFolderArchiveArg) obj;
        if ((this.teamFolderId != teamFolderArchiveArg.teamFolderId && !this.teamFolderId.equals(teamFolderArchiveArg.teamFolderId)) || this.forceAsyncOff != teamFolderArchiveArg.forceAsyncOff) {
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
