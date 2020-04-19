package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.RequestedVisibility */
public enum RequestedVisibility {
    PUBLIC,
    TEAM_ONLY,
    PASSWORD;

    /* renamed from: com.dropbox.core.v2.sharing.RequestedVisibility$Serializer */
    static class Serializer extends UnionSerializer<RequestedVisibility> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RequestedVisibility requestedVisibility, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (requestedVisibility) {
                case PUBLIC:
                    jsonGenerator.writeString("public");
                    return;
                case TEAM_ONLY:
                    jsonGenerator.writeString("team_only");
                    return;
                case PASSWORD:
                    jsonGenerator.writeString(BoxSharedLink.FIELD_PASSWORD);
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(requestedVisibility);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RequestedVisibility deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RequestedVisibility requestedVisibility;
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
                if ("public".equals(str)) {
                    requestedVisibility = RequestedVisibility.PUBLIC;
                } else if ("team_only".equals(str)) {
                    requestedVisibility = RequestedVisibility.TEAM_ONLY;
                } else if (BoxSharedLink.FIELD_PASSWORD.equals(str)) {
                    requestedVisibility = RequestedVisibility.PASSWORD;
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
                return requestedVisibility;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
