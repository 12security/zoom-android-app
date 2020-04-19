package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.Visibility */
public enum Visibility {
    PUBLIC,
    TEAM_ONLY,
    PASSWORD,
    TEAM_AND_PASSWORD,
    SHARED_FOLDER_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.Visibility$Serializer */
    static class Serializer extends UnionSerializer<Visibility> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(Visibility visibility, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (visibility) {
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

        public Visibility deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            Visibility visibility;
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
                    visibility = Visibility.PUBLIC;
                } else if ("team_only".equals(str)) {
                    visibility = Visibility.TEAM_ONLY;
                } else if (BoxSharedLink.FIELD_PASSWORD.equals(str)) {
                    visibility = Visibility.PASSWORD;
                } else if ("team_and_password".equals(str)) {
                    visibility = Visibility.TEAM_AND_PASSWORD;
                } else if ("shared_folder_only".equals(str)) {
                    visibility = Visibility.SHARED_FOLDER_ONLY;
                } else {
                    visibility = Visibility.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return visibility;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
