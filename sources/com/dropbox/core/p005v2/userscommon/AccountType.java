package com.dropbox.core.p005v2.userscommon;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.userscommon.AccountType */
public enum AccountType {
    BASIC,
    PRO,
    BUSINESS;

    /* renamed from: com.dropbox.core.v2.userscommon.AccountType$Serializer */
    public static class Serializer extends UnionSerializer<AccountType> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AccountType accountType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accountType) {
                case BASIC:
                    jsonGenerator.writeString("basic");
                    return;
                case PRO:
                    jsonGenerator.writeString("pro");
                    return;
                case BUSINESS:
                    jsonGenerator.writeString("business");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(accountType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public AccountType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AccountType accountType;
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
                if ("basic".equals(str)) {
                    accountType = AccountType.BASIC;
                } else if ("pro".equals(str)) {
                    accountType = AccountType.PRO;
                } else if ("business".equals(str)) {
                    accountType = AccountType.BUSINESS;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accountType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
