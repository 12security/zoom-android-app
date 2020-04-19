package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchError */
public enum FeaturesGetValuesBatchError {
    EMPTY_FEATURES_LIST,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchError$1 */
    static /* synthetic */ class C08081 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$FeaturesGetValuesBatchError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$FeaturesGetValuesBatchError = new int[FeaturesGetValuesBatchError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$FeaturesGetValuesBatchError[FeaturesGetValuesBatchError.EMPTY_FEATURES_LIST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchError$Serializer */
    static class Serializer extends UnionSerializer<FeaturesGetValuesBatchError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(FeaturesGetValuesBatchError featuresGetValuesBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08081.$SwitchMap$com$dropbox$core$v2$team$FeaturesGetValuesBatchError[featuresGetValuesBatchError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("empty_features_list");
            }
        }

        public FeaturesGetValuesBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FeaturesGetValuesBatchError featuresGetValuesBatchError;
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
                if ("empty_features_list".equals(str)) {
                    featuresGetValuesBatchError = FeaturesGetValuesBatchError.EMPTY_FEATURES_LIST;
                } else {
                    featuresGetValuesBatchError = FeaturesGetValuesBatchError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return featuresGetValuesBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
