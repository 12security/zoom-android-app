package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersGetInfoError */
public enum MembersGetInfoError {
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersGetInfoError$1 */
    static /* synthetic */ class C08361 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$MembersGetInfoError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$MembersGetInfoError = new int[MembersGetInfoError.values().length];
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersGetInfoError$Serializer */
    static class Serializer extends UnionSerializer<MembersGetInfoError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersGetInfoError membersGetInfoError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            int i = C08361.$SwitchMap$com$dropbox$core$v2$team$MembersGetInfoError[membersGetInfoError.ordinal()];
            jsonGenerator.writeString("other");
        }

        public MembersGetInfoError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
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
                MembersGetInfoError membersGetInfoError = MembersGetInfoError.OTHER;
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersGetInfoError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
