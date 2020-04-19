package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TeamMembershipType */
public enum TeamMembershipType {
    FULL,
    LIMITED;

    /* renamed from: com.dropbox.core.v2.team.TeamMembershipType$Serializer */
    static class Serializer extends UnionSerializer<TeamMembershipType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamMembershipType teamMembershipType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamMembershipType) {
                case FULL:
                    jsonGenerator.writeString("full");
                    return;
                case LIMITED:
                    jsonGenerator.writeString("limited");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamMembershipType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamMembershipType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamMembershipType teamMembershipType;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("full".equals(str)) {
                    teamMembershipType = TeamMembershipType.FULL;
                } else if ("limited".equals(str)) {
                    teamMembershipType = TeamMembershipType.LIMITED;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamMembershipType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
