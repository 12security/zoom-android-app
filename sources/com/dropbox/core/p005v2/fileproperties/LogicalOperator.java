package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.fileproperties.LogicalOperator */
public enum LogicalOperator {
    OR_OPERATOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.fileproperties.LogicalOperator$1 */
    static /* synthetic */ class C06361 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$fileproperties$LogicalOperator = null;

        static {
            $SwitchMap$com$dropbox$core$v2$fileproperties$LogicalOperator = new int[LogicalOperator.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$fileproperties$LogicalOperator[LogicalOperator.OR_OPERATOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.LogicalOperator$Serializer */
    static class Serializer extends UnionSerializer<LogicalOperator> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(LogicalOperator logicalOperator, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06361.$SwitchMap$com$dropbox$core$v2$fileproperties$LogicalOperator[logicalOperator.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("or_operator");
            }
        }

        public LogicalOperator deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LogicalOperator logicalOperator;
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
                if ("or_operator".equals(str)) {
                    logicalOperator = LogicalOperator.OR_OPERATOR;
                } else {
                    logicalOperator = LogicalOperator.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return logicalOperator;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
