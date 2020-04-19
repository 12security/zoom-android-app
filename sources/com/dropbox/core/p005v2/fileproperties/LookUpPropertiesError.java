package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.fileproperties.LookUpPropertiesError */
public enum LookUpPropertiesError {
    PROPERTY_GROUP_NOT_FOUND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.fileproperties.LookUpPropertiesError$1 */
    static /* synthetic */ class C06371 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$fileproperties$LookUpPropertiesError */
        static final /* synthetic */ int[] f72x96137ab4 = null;

        static {
            f72x96137ab4 = new int[LookUpPropertiesError.values().length];
            try {
                f72x96137ab4[LookUpPropertiesError.PROPERTY_GROUP_NOT_FOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.LookUpPropertiesError$Serializer */
    public static class Serializer extends UnionSerializer<LookUpPropertiesError> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(LookUpPropertiesError lookUpPropertiesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06371.f72x96137ab4[lookUpPropertiesError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("property_group_not_found");
            }
        }

        public LookUpPropertiesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LookUpPropertiesError lookUpPropertiesError;
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
                if ("property_group_not_found".equals(str)) {
                    lookUpPropertiesError = LookUpPropertiesError.PROPERTY_GROUP_NOT_FOUND;
                } else {
                    lookUpPropertiesError = LookUpPropertiesError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return lookUpPropertiesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
