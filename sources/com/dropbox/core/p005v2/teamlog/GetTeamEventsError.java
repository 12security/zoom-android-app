package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsError */
public enum GetTeamEventsError {
    ACCOUNT_ID_NOT_FOUND,
    INVALID_TIME_RANGE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsError$Serializer */
    static class Serializer extends UnionSerializer<GetTeamEventsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GetTeamEventsError getTeamEventsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (getTeamEventsError) {
                case ACCOUNT_ID_NOT_FOUND:
                    jsonGenerator.writeString("account_id_not_found");
                    return;
                case INVALID_TIME_RANGE:
                    jsonGenerator.writeString("invalid_time_range");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GetTeamEventsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetTeamEventsError getTeamEventsError;
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
                if ("account_id_not_found".equals(str)) {
                    getTeamEventsError = GetTeamEventsError.ACCOUNT_ID_NOT_FOUND;
                } else if ("invalid_time_range".equals(str)) {
                    getTeamEventsError = GetTeamEventsError.INVALID_TIME_RANGE;
                } else {
                    getTeamEventsError = GetTeamEventsError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getTeamEventsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
