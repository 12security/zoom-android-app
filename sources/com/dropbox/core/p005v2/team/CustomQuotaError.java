package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.CustomQuotaError */
public enum CustomQuotaError {
    TOO_MANY_USERS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.CustomQuotaError$1 */
    static /* synthetic */ class C07981 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$CustomQuotaError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$CustomQuotaError = new int[CustomQuotaError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$CustomQuotaError[CustomQuotaError.TOO_MANY_USERS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.CustomQuotaError$Serializer */
    static class Serializer extends UnionSerializer<CustomQuotaError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(CustomQuotaError customQuotaError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07981.$SwitchMap$com$dropbox$core$v2$team$CustomQuotaError[customQuotaError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("too_many_users");
            }
        }

        public CustomQuotaError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            CustomQuotaError customQuotaError;
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
                if ("too_many_users".equals(str)) {
                    customQuotaError = CustomQuotaError.TOO_MANY_USERS;
                } else {
                    customQuotaError = CustomQuotaError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return customQuotaError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
