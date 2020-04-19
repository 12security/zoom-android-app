package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.AccountCaptureAvailability */
public enum AccountCaptureAvailability {
    UNAVAILABLE,
    AVAILABLE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.AccountCaptureAvailability$Serializer */
    static class Serializer extends UnionSerializer<AccountCaptureAvailability> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AccountCaptureAvailability accountCaptureAvailability, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accountCaptureAvailability) {
                case UNAVAILABLE:
                    jsonGenerator.writeString("unavailable");
                    return;
                case AVAILABLE:
                    jsonGenerator.writeString("available");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AccountCaptureAvailability deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AccountCaptureAvailability accountCaptureAvailability;
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
                if ("unavailable".equals(str)) {
                    accountCaptureAvailability = AccountCaptureAvailability.UNAVAILABLE;
                } else if ("available".equals(str)) {
                    accountCaptureAvailability = AccountCaptureAvailability.AVAILABLE;
                } else {
                    accountCaptureAvailability = AccountCaptureAvailability.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accountCaptureAvailability;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
