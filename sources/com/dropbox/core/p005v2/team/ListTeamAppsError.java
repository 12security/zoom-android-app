package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ListTeamAppsError */
public enum ListTeamAppsError {
    RESET,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ListTeamAppsError$1 */
    static /* synthetic */ class C08311 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ListTeamAppsError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ListTeamAppsError = new int[ListTeamAppsError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ListTeamAppsError[ListTeamAppsError.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListTeamAppsError$Serializer */
    static class Serializer extends UnionSerializer<ListTeamAppsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ListTeamAppsError listTeamAppsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08311.$SwitchMap$com$dropbox$core$v2$team$ListTeamAppsError[listTeamAppsError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("reset");
            }
        }

        public ListTeamAppsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListTeamAppsError listTeamAppsError;
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
                    listTeamAppsError = ListTeamAppsError.RESET;
                } else {
                    listTeamAppsError = ListTeamAppsError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listTeamAppsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
