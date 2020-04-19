package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkVisibility */
public enum SharedLinkVisibility {
    PASSWORD,
    PUBLIC,
    TEAM_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkVisibility$Serializer */
    static class Serializer extends UnionSerializer<SharedLinkVisibility> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkVisibility sharedLinkVisibility, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkVisibility) {
                case PASSWORD:
                    jsonGenerator.writeString(BoxSharedLink.FIELD_PASSWORD);
                    return;
                case PUBLIC:
                    jsonGenerator.writeString("public");
                    return;
                case TEAM_ONLY:
                    jsonGenerator.writeString("team_only");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedLinkVisibility deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkVisibility sharedLinkVisibility;
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
                if (BoxSharedLink.FIELD_PASSWORD.equals(str)) {
                    sharedLinkVisibility = SharedLinkVisibility.PASSWORD;
                } else if ("public".equals(str)) {
                    sharedLinkVisibility = SharedLinkVisibility.PUBLIC;
                } else if ("team_only".equals(str)) {
                    sharedLinkVisibility = SharedLinkVisibility.TEAM_ONLY;
                } else {
                    sharedLinkVisibility = SharedLinkVisibility.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedLinkVisibility;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
