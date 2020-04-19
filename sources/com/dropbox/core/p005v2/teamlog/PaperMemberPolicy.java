package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.PaperMemberPolicy */
public enum PaperMemberPolicy {
    ANYONE_WITH_LINK,
    ONLY_TEAM,
    TEAM_AND_EXPLICITLY_SHARED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperMemberPolicy$Serializer */
    static class Serializer extends UnionSerializer<PaperMemberPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperMemberPolicy paperMemberPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperMemberPolicy) {
                case ANYONE_WITH_LINK:
                    jsonGenerator.writeString("anyone_with_link");
                    return;
                case ONLY_TEAM:
                    jsonGenerator.writeString("only_team");
                    return;
                case TEAM_AND_EXPLICITLY_SHARED:
                    jsonGenerator.writeString("team_and_explicitly_shared");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperMemberPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperMemberPolicy paperMemberPolicy;
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
                if ("anyone_with_link".equals(str)) {
                    paperMemberPolicy = PaperMemberPolicy.ANYONE_WITH_LINK;
                } else if ("only_team".equals(str)) {
                    paperMemberPolicy = PaperMemberPolicy.ONLY_TEAM;
                } else if ("team_and_explicitly_shared".equals(str)) {
                    paperMemberPolicy = PaperMemberPolicy.TEAM_AND_EXPLICITLY_SHARED;
                } else {
                    paperMemberPolicy = PaperMemberPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperMemberPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
