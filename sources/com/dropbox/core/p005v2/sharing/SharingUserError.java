package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharingUserError */
public enum SharingUserError {
    EMAIL_UNVERIFIED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.SharingUserError$1 */
    static /* synthetic */ class C07881 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$sharing$SharingUserError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$sharing$SharingUserError = new int[SharingUserError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$sharing$SharingUserError[SharingUserError.EMAIL_UNVERIFIED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharingUserError$Serializer */
    static class Serializer extends UnionSerializer<SharingUserError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingUserError sharingUserError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07881.$SwitchMap$com$dropbox$core$v2$sharing$SharingUserError[sharingUserError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("email_unverified");
            }
        }

        public SharingUserError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingUserError sharingUserError;
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
                if ("email_unverified".equals(str)) {
                    sharingUserError = SharingUserError.EMAIL_UNVERIFIED;
                } else {
                    sharingUserError = SharingUserError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharingUserError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
