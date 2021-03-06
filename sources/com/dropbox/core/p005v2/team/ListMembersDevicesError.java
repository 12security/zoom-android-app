package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ListMembersDevicesError */
public enum ListMembersDevicesError {
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ListMembersDevicesError$1 */
    static /* synthetic */ class C08301 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ListMembersDevicesError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ListMembersDevicesError = new int[ListMembersDevicesError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ListMembersDevicesError[ListMembersDevicesError.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMembersDevicesError$Serializer */
    static class Serializer extends UnionSerializer<ListMembersDevicesError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListMembersDevicesError listMembersDevicesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08301.$SwitchMap$com$dropbox$core$v2$team$ListMembersDevicesError[listMembersDevicesError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("reset");
            }
        }

        public ListMembersDevicesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListMembersDevicesError listMembersDevicesError;
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
                if ("reset".equals(str)) {
                    listMembersDevicesError = ListMembersDevicesError.RESET;
                } else {
                    listMembersDevicesError = ListMembersDevicesError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listMembersDevicesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
