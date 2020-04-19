package com.dropbox.core.p005v2.users;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.users.GetAccountError */
public enum GetAccountError {
    NO_ACCOUNT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.users.GetAccountError$1 */
    static /* synthetic */ class C09521 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$users$GetAccountError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$users$GetAccountError = new int[GetAccountError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$users$GetAccountError[GetAccountError.NO_ACCOUNT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.users.GetAccountError$Serializer */
    public static class Serializer extends UnionSerializer<GetAccountError> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GetAccountError getAccountError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C09521.$SwitchMap$com$dropbox$core$v2$users$GetAccountError[getAccountError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("no_account");
            }
        }

        public GetAccountError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetAccountError getAccountError;
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
                if ("no_account".equals(str)) {
                    getAccountError = GetAccountError.NO_ACCOUNT;
                } else {
                    getAccountError = GetAccountError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getAccountError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
