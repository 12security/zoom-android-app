package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ListMembersAppsError */
public enum ListMembersAppsError {
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ListMembersAppsError$1 */
    static /* synthetic */ class C08291 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ListMembersAppsError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ListMembersAppsError = new int[ListMembersAppsError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ListMembersAppsError[ListMembersAppsError.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMembersAppsError$Serializer */
    static class Serializer extends UnionSerializer<ListMembersAppsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListMembersAppsError listMembersAppsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08291.$SwitchMap$com$dropbox$core$v2$team$ListMembersAppsError[listMembersAppsError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("reset");
            }
        }

        public ListMembersAppsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListMembersAppsError listMembersAppsError;
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
                    listMembersAppsError = ListMembersAppsError.RESET;
                } else {
                    listMembersAppsError = ListMembersAppsError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listMembersAppsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
