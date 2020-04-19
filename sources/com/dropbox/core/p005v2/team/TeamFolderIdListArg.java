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
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.TeamFolderIdListArg */
class TeamFolderIdListArg {
    protected final List<String> teamFolderIds;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderIdListArg$Serializer */
    static class Serializer extends StructSerializer<TeamFolderIdListArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderIdListArg teamFolderIdListArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_folder_ids");
            StoneSerializers.list(StoneSerializers.string()).serialize(teamFolderIdListArg.teamFolderIds, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderIdListArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_folder_ids".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    TeamFolderIdListArg teamFolderIdListArg = new TeamFolderIdListArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderIdListArg, teamFolderIdListArg.toStringMultiline());
                    return teamFolderIdListArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"team_folder_ids\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderIdListArg(List<String> list) {
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'teamFolderIds' is null");
        } else if (list.size() >= 1) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'teamFolderIds' is null");
                } else if (!Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                    throw new IllegalArgumentException("Stringan item in list 'teamFolderIds' does not match pattern");
                }
            }
            this.teamFolderIds = list;
        } else {
            throw new IllegalArgumentException("List 'teamFolderIds' has fewer than 1 items");
        }
    }

    public List<String> getTeamFolderIds() {
        return this.teamFolderIds;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamFolderIds});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        TeamFolderIdListArg teamFolderIdListArg = (TeamFolderIdListArg) obj;
        List<String> list = this.teamFolderIds;
        List<String> list2 = teamFolderIdListArg.teamFolderIds;
        if (list != list2 && !list.equals(list2)) {
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
