package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppBatchError */
public enum RevokeLinkedAppBatchError {
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppBatchError$1 */
    static /* synthetic */ class C08541 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$RevokeLinkedAppBatchError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$RevokeLinkedAppBatchError = new int[RevokeLinkedAppBatchError.values().length];
        }
    }

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppBatchError$Serializer */
    static class Serializer extends UnionSerializer<RevokeLinkedAppBatchError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RevokeLinkedAppBatchError revokeLinkedAppBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            int i = C08541.$SwitchMap$com$dropbox$core$v2$team$RevokeLinkedAppBatchError[revokeLinkedAppBatchError.ordinal()];
            jsonGenerator.writeString("other");
        }

        public RevokeLinkedAppBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
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
                RevokeLinkedAppBatchError revokeLinkedAppBatchError = RevokeLinkedAppBatchError.OTHER;
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return revokeLinkedAppBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
