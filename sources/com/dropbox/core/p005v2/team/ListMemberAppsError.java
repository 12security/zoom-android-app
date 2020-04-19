package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ListMemberAppsError */
public enum ListMemberAppsError {
    MEMBER_NOT_FOUND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ListMemberAppsError$1 */
    static /* synthetic */ class C08271 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ListMemberAppsError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ListMemberAppsError = new int[ListMemberAppsError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ListMemberAppsError[ListMemberAppsError.MEMBER_NOT_FOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMemberAppsError$Serializer */
    static class Serializer extends UnionSerializer<ListMemberAppsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListMemberAppsError listMemberAppsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08271.$SwitchMap$com$dropbox$core$v2$team$ListMemberAppsError[listMemberAppsError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("member_not_found");
            }
        }

        public ListMemberAppsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListMemberAppsError listMemberAppsError;
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
                    listMemberAppsError = ListMemberAppsError.MEMBER_NOT_FOUND;
                } else {
                    listMemberAppsError = ListMemberAppsError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listMemberAppsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
