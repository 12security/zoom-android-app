package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderListError */
public class TeamFolderListError {
    protected final TeamFolderAccessError accessError;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderListError$Serializer */
    static class Serializer extends StructSerializer<TeamFolderListError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderListError teamFolderListError, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_error");
            Serializer.INSTANCE.serialize(teamFolderListError.accessError, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderListError deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamFolderAccessError teamFolderAccessError = null;
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
                    if ("access_error".equals(currentName)) {
                        teamFolderAccessError = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamFolderAccessError != null) {
                    TeamFolderListError teamFolderListError = new TeamFolderListError(teamFolderAccessError);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderListError, teamFolderListError.toStringMultiline());
                    return teamFolderListError;
                }
                throw new JsonParseException(jsonParser, "Required field \"access_error\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderListError(TeamFolderAccessError teamFolderAccessError) {
        if (teamFolderAccessError != null) {
            this.accessError = teamFolderAccessError;
            return;
        }
        throw new IllegalArgumentException("Required value for 'accessError' is null");
    }

    public TeamFolderAccessError getAccessError() {
        return this.accessError;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessError});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        TeamFolderListError teamFolderListError = (TeamFolderListError) obj;
        TeamFolderAccessError teamFolderAccessError = this.accessError;
        TeamFolderAccessError teamFolderAccessError2 = teamFolderListError.accessError;
        if (teamFolderAccessError != teamFolderAccessError2 && !teamFolderAccessError.equals(teamFolderAccessError2)) {
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
