package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.LinkAudience */
public enum LinkAudience {
    PUBLIC,
    TEAM,
    MEMBERS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.LinkAudience$Serializer */
    public static class Serializer extends UnionSerializer<LinkAudience> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(LinkAudience linkAudience, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (linkAudience) {
                case PUBLIC:
                    jsonGenerator.writeString("public");
                    return;
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                case MEMBERS:
                    jsonGenerator.writeString("members");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LinkAudience deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LinkAudience linkAudience;
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
                    linkAudience = LinkAudience.PUBLIC;
                } else if ("team".equals(str)) {
                    linkAudience = LinkAudience.TEAM;
                } else if ("members".equals(str)) {
                    linkAudience = LinkAudience.MEMBERS;
                } else {
                    linkAudience = LinkAudience.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return linkAudience;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
