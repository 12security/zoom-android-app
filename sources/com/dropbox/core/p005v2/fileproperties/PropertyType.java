package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.fileproperties.PropertyType */
public enum PropertyType {
    STRING,
    OTHER;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertyType$1 */
    static /* synthetic */ class C06431 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$fileproperties$PropertyType = null;

        static {
            $SwitchMap$com$dropbox$core$v2$fileproperties$PropertyType = new int[PropertyType.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$fileproperties$PropertyType[PropertyType.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertyType$Serializer */
    static class Serializer extends UnionSerializer<PropertyType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PropertyType propertyType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06431.$SwitchMap$com$dropbox$core$v2$fileproperties$PropertyType[propertyType.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("string");
            }
        }

        public PropertyType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PropertyType propertyType;
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
                if ("string".equals(str)) {
                    propertyType = PropertyType.STRING;
                } else {
                    propertyType = PropertyType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return propertyType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
