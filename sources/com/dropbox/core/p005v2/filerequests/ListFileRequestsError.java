package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.filerequests.ListFileRequestsError */
public enum ListFileRequestsError {
    DISABLED_FOR_TEAM,
    OTHER;

    /* renamed from: com.dropbox.core.v2.filerequests.ListFileRequestsError$Serializer */
    static class Serializer extends UnionSerializer<ListFileRequestsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListFileRequestsError listFileRequestsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFileRequestsError) {
                case DISABLED_FOR_TEAM:
                    jsonGenerator.writeString("disabled_for_team");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(listFileRequestsError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ListFileRequestsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFileRequestsError listFileRequestsError;
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
                if ("disabled_for_team".equals(str)) {
                    listFileRequestsError = ListFileRequestsError.DISABLED_FOR_TEAM;
                } else if ("other".equals(str)) {
                    listFileRequestsError = ListFileRequestsError.OTHER;
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
                return listFileRequestsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
