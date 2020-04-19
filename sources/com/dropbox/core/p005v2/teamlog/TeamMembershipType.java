package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.TeamMembershipType */
public enum TeamMembershipType {
    FREE,
    FULL,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamMembershipType$Serializer */
    static class Serializer extends UnionSerializer<TeamMembershipType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamMembershipType teamMembershipType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamMembershipType) {
                case FREE:
                    jsonGenerator.writeString("free");
                    return;
                case FULL:
                    jsonGenerator.writeString("full");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
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
                if ("free".equals(str)) {
                    teamMembershipType = TeamMembershipType.FREE;
                } else if ("full".equals(str)) {
                    teamMembershipType = TeamMembershipType.FULL;
                } else {
                    teamMembershipType = TeamMembershipType.OTHER;
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
