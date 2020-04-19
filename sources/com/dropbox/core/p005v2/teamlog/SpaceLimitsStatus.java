package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SpaceLimitsStatus */
public enum SpaceLimitsStatus {
    WITHIN_QUOTA,
    NEAR_QUOTA,
    OVER_QUOTA,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SpaceLimitsStatus$Serializer */
    static class Serializer extends UnionSerializer<SpaceLimitsStatus> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SpaceLimitsStatus spaceLimitsStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (spaceLimitsStatus) {
                case WITHIN_QUOTA:
                    jsonGenerator.writeString("within_quota");
                    return;
                case NEAR_QUOTA:
                    jsonGenerator.writeString("near_quota");
                    return;
                case OVER_QUOTA:
                    jsonGenerator.writeString("over_quota");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SpaceLimitsStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SpaceLimitsStatus spaceLimitsStatus;
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
                if ("within_quota".equals(str)) {
                    spaceLimitsStatus = SpaceLimitsStatus.WITHIN_QUOTA;
                } else if ("near_quota".equals(str)) {
                    spaceLimitsStatus = SpaceLimitsStatus.NEAR_QUOTA;
                } else if ("over_quota".equals(str)) {
                    spaceLimitsStatus = SpaceLimitsStatus.OVER_QUOTA;
                } else {
                    spaceLimitsStatus = SpaceLimitsStatus.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return spaceLimitsStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
