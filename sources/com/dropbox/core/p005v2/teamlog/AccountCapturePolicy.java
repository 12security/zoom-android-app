package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.AccountCapturePolicy */
public enum AccountCapturePolicy {
    DISABLED,
    INVITED_USERS,
    ALL_USERS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.AccountCapturePolicy$Serializer */
    static class Serializer extends UnionSerializer<AccountCapturePolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AccountCapturePolicy accountCapturePolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accountCapturePolicy) {
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                case INVITED_USERS:
                    jsonGenerator.writeString("invited_users");
                    return;
                case ALL_USERS:
                    jsonGenerator.writeString("all_users");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AccountCapturePolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AccountCapturePolicy accountCapturePolicy;
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
                if ("disabled".equals(str)) {
                    accountCapturePolicy = AccountCapturePolicy.DISABLED;
                } else if ("invited_users".equals(str)) {
                    accountCapturePolicy = AccountCapturePolicy.INVITED_USERS;
                } else if ("all_users".equals(str)) {
                    accountCapturePolicy = AccountCapturePolicy.ALL_USERS;
                } else {
                    accountCapturePolicy = AccountCapturePolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accountCapturePolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
