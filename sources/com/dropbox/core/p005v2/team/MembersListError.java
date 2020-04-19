package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersListError */
public enum MembersListError {
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersListError$1 */
    static /* synthetic */ class C08391 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$MembersListError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$MembersListError = new int[MembersListError.values().length];
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersListError$Serializer */
    static class Serializer extends UnionSerializer<MembersListError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersListError membersListError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            int i = C08391.$SwitchMap$com$dropbox$core$v2$team$MembersListError[membersListError.ordinal()];
            jsonGenerator.writeString("other");
        }

        public MembersListError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
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
                MembersListError membersListError = MembersListError.OTHER;
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersListError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
