package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersSendWelcomeError */
public enum MembersSendWelcomeError {
    USER_NOT_FOUND,
    USER_NOT_IN_TEAM,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersSendWelcomeError$Serializer */
    static class Serializer extends UnionSerializer<MembersSendWelcomeError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersSendWelcomeError membersSendWelcomeError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersSendWelcomeError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MembersSendWelcomeError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersSendWelcomeError membersSendWelcomeError;
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
                if ("user_not_found".equals(str)) {
                    membersSendWelcomeError = MembersSendWelcomeError.USER_NOT_FOUND;
                } else if ("user_not_in_team".equals(str)) {
                    membersSendWelcomeError = MembersSendWelcomeError.USER_NOT_IN_TEAM;
                } else {
                    membersSendWelcomeError = MembersSendWelcomeError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersSendWelcomeError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
