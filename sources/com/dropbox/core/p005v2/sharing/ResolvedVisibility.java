package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.ResolvedVisibility */
public enum ResolvedVisibility {
    PUBLIC,
    TEAM_ONLY,
    PASSWORD,
    TEAM_AND_PASSWORD,
    SHARED_FOLDER_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.ResolvedVisibility$Serializer */
    static class Serializer extends UnionSerializer<ResolvedVisibility> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ResolvedVisibility resolvedVisibility, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (resolvedVisibility) {
                case PUBLIC:
                    jsonGenerator.writeString("public");
                    return;
                case TEAM_ONLY:
                    jsonGenerator.writeString("team_only");
                    return;
                case PASSWORD:
                    jsonGenerator.writeString(BoxSharedLink.FIELD_PASSWORD);
                    return;
                case TEAM_AND_PASSWORD:
                    jsonGenerator.writeString("team_and_password");
                    return;
                case SHARED_FOLDER_ONLY:
                    jsonGenerator.writeString("shared_folder_only");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ResolvedVisibility deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ResolvedVisibility resolvedVisibility;
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
                    resolvedVisibility = ResolvedVisibility.PUBLIC;
                } else if ("team_only".equals(str)) {
                    resolvedVisibility = ResolvedVisibility.TEAM_ONLY;
                } else if (BoxSharedLink.FIELD_PASSWORD.equals(str)) {
                    resolvedVisibility = ResolvedVisibility.PASSWORD;
                } else if ("team_and_password".equals(str)) {
                    resolvedVisibility = ResolvedVisibility.TEAM_AND_PASSWORD;
                } else if ("shared_folder_only".equals(str)) {
                    resolvedVisibility = ResolvedVisibility.SHARED_FOLDER_ONLY;
                } else {
                    resolvedVisibility = ResolvedVisibility.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return resolvedVisibility;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
