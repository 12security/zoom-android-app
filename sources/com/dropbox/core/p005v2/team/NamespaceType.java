package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.NamespaceType */
public enum NamespaceType {
    APP_FOLDER,
    SHARED_FOLDER,
    TEAM_FOLDER,
    TEAM_MEMBER_FOLDER,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.NamespaceType$Serializer */
    static class Serializer extends UnionSerializer<NamespaceType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(NamespaceType namespaceType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (namespaceType) {
                case APP_FOLDER:
                    jsonGenerator.writeString("app_folder");
                    return;
                case SHARED_FOLDER:
                    jsonGenerator.writeString("shared_folder");
                    return;
                case TEAM_FOLDER:
                    jsonGenerator.writeString("team_folder");
                    return;
                case TEAM_MEMBER_FOLDER:
                    jsonGenerator.writeString("team_member_folder");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public NamespaceType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            NamespaceType namespaceType;
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
                if ("app_folder".equals(str)) {
                    namespaceType = NamespaceType.APP_FOLDER;
                } else if ("shared_folder".equals(str)) {
                    namespaceType = NamespaceType.SHARED_FOLDER;
                } else if ("team_folder".equals(str)) {
                    namespaceType = NamespaceType.TEAM_FOLDER;
                } else if ("team_member_folder".equals(str)) {
                    namespaceType = NamespaceType.TEAM_MEMBER_FOLDER;
                } else {
                    namespaceType = NamespaceType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return namespaceType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
