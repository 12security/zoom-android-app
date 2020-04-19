package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.LinkAction */
public enum LinkAction {
    CHANGE_ACCESS_LEVEL,
    CHANGE_AUDIENCE,
    REMOVE_EXPIRY,
    REMOVE_PASSWORD,
    SET_EXPIRY,
    SET_PASSWORD,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.LinkAction$Serializer */
    static class Serializer extends UnionSerializer<LinkAction> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(LinkAction linkAction, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (linkAction) {
                case CHANGE_ACCESS_LEVEL:
                    jsonGenerator.writeString("change_access_level");
                    return;
                case CHANGE_AUDIENCE:
                    jsonGenerator.writeString("change_audience");
                    return;
                case REMOVE_EXPIRY:
                    jsonGenerator.writeString("remove_expiry");
                    return;
                case REMOVE_PASSWORD:
                    jsonGenerator.writeString("remove_password");
                    return;
                case SET_EXPIRY:
                    jsonGenerator.writeString("set_expiry");
                    return;
                case SET_PASSWORD:
                    jsonGenerator.writeString("set_password");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LinkAction deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LinkAction linkAction;
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
                if ("change_access_level".equals(str)) {
                    linkAction = LinkAction.CHANGE_ACCESS_LEVEL;
                } else if ("change_audience".equals(str)) {
                    linkAction = LinkAction.CHANGE_AUDIENCE;
                } else if ("remove_expiry".equals(str)) {
                    linkAction = LinkAction.REMOVE_EXPIRY;
                } else if ("remove_password".equals(str)) {
                    linkAction = LinkAction.REMOVE_PASSWORD;
                } else if ("set_expiry".equals(str)) {
                    linkAction = LinkAction.SET_EXPIRY;
                } else if ("set_password".equals(str)) {
                    linkAction = LinkAction.SET_PASSWORD;
                } else {
                    linkAction = LinkAction.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return linkAction;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
