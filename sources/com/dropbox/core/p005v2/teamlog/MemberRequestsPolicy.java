package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.MemberRequestsPolicy */
public enum MemberRequestsPolicy {
    AUTO_ACCEPT,
    DISABLED,
    REQUIRE_APPROVAL,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberRequestsPolicy$Serializer */
    static class Serializer extends UnionSerializer<MemberRequestsPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MemberRequestsPolicy memberRequestsPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberRequestsPolicy) {
                case AUTO_ACCEPT:
                    jsonGenerator.writeString("auto_accept");
                    return;
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case REQUIRE_APPROVAL:
                    jsonGenerator.writeString("require_approval");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberRequestsPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberRequestsPolicy memberRequestsPolicy;
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
                if ("auto_accept".equals(str)) {
                    memberRequestsPolicy = MemberRequestsPolicy.AUTO_ACCEPT;
                } else if ("disabled".equals(str)) {
                    memberRequestsPolicy = MemberRequestsPolicy.DISABLED;
                } else if ("require_approval".equals(str)) {
                    memberRequestsPolicy = MemberRequestsPolicy.REQUIRE_APPROVAL;
                } else {
                    memberRequestsPolicy = MemberRequestsPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberRequestsPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
