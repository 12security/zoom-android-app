package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ListMemberDevicesError */
public enum ListMemberDevicesError {
    MEMBER_NOT_FOUND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ListMemberDevicesError$1 */
    static /* synthetic */ class C08281 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ListMemberDevicesError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ListMemberDevicesError = new int[ListMemberDevicesError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ListMemberDevicesError[ListMemberDevicesError.MEMBER_NOT_FOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMemberDevicesError$Serializer */
    static class Serializer extends UnionSerializer<ListMemberDevicesError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListMemberDevicesError listMemberDevicesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08281.$SwitchMap$com$dropbox$core$v2$team$ListMemberDevicesError[listMemberDevicesError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("member_not_found");
            }
        }

        public ListMemberDevicesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListMemberDevicesError listMemberDevicesError;
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
                if ("member_not_found".equals(str)) {
                    listMemberDevicesError = ListMemberDevicesError.MEMBER_NOT_FOUND;
                } else {
                    listMemberDevicesError = ListMemberDevicesError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listMemberDevicesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
