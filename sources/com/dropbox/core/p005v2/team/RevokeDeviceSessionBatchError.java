package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchError */
public enum RevokeDeviceSessionBatchError {
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchError$1 */
    static /* synthetic */ class C08521 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$team$RevokeDeviceSessionBatchError */
        static final /* synthetic */ int[] f141x46d5a6cf = null;

        static {
            f141x46d5a6cf = new int[RevokeDeviceSessionBatchError.values().length];
        }
    }

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchError$Serializer */
    static class Serializer extends UnionSerializer<RevokeDeviceSessionBatchError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RevokeDeviceSessionBatchError revokeDeviceSessionBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            int i = C08521.f141x46d5a6cf[revokeDeviceSessionBatchError.ordinal()];
            jsonGenerator.writeString("other");
        }

        public RevokeDeviceSessionBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
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
                RevokeDeviceSessionBatchError revokeDeviceSessionBatchError = RevokeDeviceSessionBatchError.OTHER;
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return revokeDeviceSessionBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
