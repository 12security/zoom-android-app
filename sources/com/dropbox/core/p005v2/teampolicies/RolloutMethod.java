package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.RolloutMethod */
public enum RolloutMethod {
    UNLINK_ALL,
    UNLINK_MOST_INACTIVE,
    ADD_MEMBER_TO_EXCEPTIONS;

    /* renamed from: com.dropbox.core.v2.teampolicies.RolloutMethod$Serializer */
    public static class Serializer extends UnionSerializer<RolloutMethod> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RolloutMethod rolloutMethod, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (rolloutMethod) {
                case UNLINK_ALL:
                    jsonGenerator.writeString("unlink_all");
                    return;
                case UNLINK_MOST_INACTIVE:
                    jsonGenerator.writeString("unlink_most_inactive");
                    return;
                case ADD_MEMBER_TO_EXCEPTIONS:
                    jsonGenerator.writeString("add_member_to_exceptions");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(rolloutMethod);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RolloutMethod deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RolloutMethod rolloutMethod;
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
                if ("unlink_all".equals(str)) {
                    rolloutMethod = RolloutMethod.UNLINK_ALL;
                } else if ("unlink_most_inactive".equals(str)) {
                    rolloutMethod = RolloutMethod.UNLINK_MOST_INACTIVE;
                } else if ("add_member_to_exceptions".equals(str)) {
                    rolloutMethod = RolloutMethod.ADD_MEMBER_TO_EXCEPTIONS;
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
                return rolloutMethod;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
