package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.DateRangeError */
public enum DateRangeError {
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.DateRangeError$1 */
    static /* synthetic */ class C08001 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$DateRangeError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$DateRangeError = new int[DateRangeError.values().length];
        }
    }

    /* renamed from: com.dropbox.core.v2.team.DateRangeError$Serializer */
    static class Serializer extends UnionSerializer<DateRangeError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DateRangeError dateRangeError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            int i = C08001.$SwitchMap$com$dropbox$core$v2$team$DateRangeError[dateRangeError.ordinal()];
            jsonGenerator.writeString("other");
        }

        public DateRangeError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
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
                DateRangeError dateRangeError = DateRangeError.OTHER;
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return dateRangeError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
