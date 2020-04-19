package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.MemberPolicy */
public enum MemberPolicy {
    TEAM,
    ANYONE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.MemberPolicy$Serializer */
    public static class Serializer extends UnionSerializer<MemberPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MemberPolicy memberPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberPolicy) {
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                case ANYONE:
                    jsonGenerator.writeString("anyone");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberPolicy memberPolicy;
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
                if ("team".equals(str)) {
                    memberPolicy = MemberPolicy.TEAM;
                } else if ("anyone".equals(str)) {
                    memberPolicy = MemberPolicy.ANYONE;
                } else {
                    memberPolicy = MemberPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
