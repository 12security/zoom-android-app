package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ListTeamDevicesError */
public enum ListTeamDevicesError {
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ListTeamDevicesError$1 */
    static /* synthetic */ class C08321 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ListTeamDevicesError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ListTeamDevicesError = new int[ListTeamDevicesError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ListTeamDevicesError[ListTeamDevicesError.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListTeamDevicesError$Serializer */
    static class Serializer extends UnionSerializer<ListTeamDevicesError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListTeamDevicesError listTeamDevicesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08321.$SwitchMap$com$dropbox$core$v2$team$ListTeamDevicesError[listTeamDevicesError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("reset");
            }
        }

        public ListTeamDevicesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListTeamDevicesError listTeamDevicesError;
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
                    listTeamDevicesError = ListTeamDevicesError.RESET;
                } else {
                    listTeamDevicesError = ListTeamDevicesError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listTeamDevicesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
