package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SharingMemberPolicy */
public enum SharingMemberPolicy {
    ALLOW,
    FORBID,
    TEAM_MEMBERS_AND_WHITELIST,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SharingMemberPolicy$Serializer */
    static class Serializer extends UnionSerializer<SharingMemberPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingMemberPolicy sharingMemberPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharingMemberPolicy) {
                case ALLOW:
                    jsonGenerator.writeString("allow");
                    return;
                case FORBID:
                    jsonGenerator.writeString("forbid");
                    return;
                case TEAM_MEMBERS_AND_WHITELIST:
                    jsonGenerator.writeString("team_members_and_whitelist");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharingMemberPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingMemberPolicy sharingMemberPolicy;
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
                if ("allow".equals(str)) {
                    sharingMemberPolicy = SharingMemberPolicy.ALLOW;
                } else if ("forbid".equals(str)) {
                    sharingMemberPolicy = SharingMemberPolicy.FORBID;
                } else if ("team_members_and_whitelist".equals(str)) {
                    sharingMemberPolicy = SharingMemberPolicy.TEAM_MEMBERS_AND_WHITELIST;
                } else {
                    sharingMemberPolicy = SharingMemberPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharingMemberPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
