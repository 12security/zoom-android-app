package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchContinueError */
public enum PropertiesSearchContinueError {
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchContinueError$1 */
    static /* synthetic */ class C06401 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$fileproperties$PropertiesSearchContinueError */
        static final /* synthetic */ int[] f74x38d55abf = null;

        static {
            f74x38d55abf = new int[PropertiesSearchContinueError.values().length];
            try {
                f74x38d55abf[PropertiesSearchContinueError.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchContinueError$Serializer */
    static class Serializer extends UnionSerializer<PropertiesSearchContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PropertiesSearchContinueError propertiesSearchContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06401.f74x38d55abf[propertiesSearchContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("reset");
            }
        }

        public PropertiesSearchContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PropertiesSearchContinueError propertiesSearchContinueError;
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
                if ("reset".equals(str)) {
                    propertiesSearchContinueError = PropertiesSearchContinueError.RESET;
                } else {
                    propertiesSearchContinueError = PropertiesSearchContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return propertiesSearchContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
