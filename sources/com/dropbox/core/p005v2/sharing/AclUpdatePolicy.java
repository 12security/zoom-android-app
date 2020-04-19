package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.AclUpdatePolicy */
public enum AclUpdatePolicy {
    OWNER,
    EDITORS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.AclUpdatePolicy$Serializer */
    public static class Serializer extends UnionSerializer<AclUpdatePolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AclUpdatePolicy aclUpdatePolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (aclUpdatePolicy) {
                case OWNER:
                    jsonGenerator.writeString("owner");
                    return;
                case EDITORS:
                    jsonGenerator.writeString("editors");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AclUpdatePolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AclUpdatePolicy aclUpdatePolicy;
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
                if ("owner".equals(str)) {
                    aclUpdatePolicy = AclUpdatePolicy.OWNER;
                } else if ("editors".equals(str)) {
                    aclUpdatePolicy = AclUpdatePolicy.EDITORS;
                } else {
                    aclUpdatePolicy = AclUpdatePolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return aclUpdatePolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
