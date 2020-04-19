package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.GroupJoinPolicy */
public enum GroupJoinPolicy {
    OPEN,
    REQUEST_TO_JOIN,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.GroupJoinPolicy$Serializer */
    static class Serializer extends UnionSerializer<GroupJoinPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupJoinPolicy groupJoinPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupJoinPolicy) {
                case OPEN:
                    jsonGenerator.writeString("open");
                    return;
                case REQUEST_TO_JOIN:
                    jsonGenerator.writeString("request_to_join");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GroupJoinPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupJoinPolicy groupJoinPolicy;
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
                if ("open".equals(str)) {
                    groupJoinPolicy = GroupJoinPolicy.OPEN;
                } else if ("request_to_join".equals(str)) {
                    groupJoinPolicy = GroupJoinPolicy.REQUEST_TO_JOIN;
                } else {
                    groupJoinPolicy = GroupJoinPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupJoinPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
