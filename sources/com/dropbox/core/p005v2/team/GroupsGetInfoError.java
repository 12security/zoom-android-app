package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupsGetInfoError */
public enum GroupsGetInfoError {
    GROUP_NOT_ON_TEAM,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.GroupsGetInfoError$1 */
    static /* synthetic */ class C08181 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$GroupsGetInfoError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$GroupsGetInfoError = new int[GroupsGetInfoError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$GroupsGetInfoError[GroupsGetInfoError.GROUP_NOT_ON_TEAM.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupsGetInfoError$Serializer */
    static class Serializer extends UnionSerializer<GroupsGetInfoError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupsGetInfoError groupsGetInfoError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08181.$SwitchMap$com$dropbox$core$v2$team$GroupsGetInfoError[groupsGetInfoError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("group_not_on_team");
            }
        }

        public GroupsGetInfoError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupsGetInfoError groupsGetInfoError;
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
                if ("group_not_on_team".equals(str)) {
                    groupsGetInfoError = GroupsGetInfoError.GROUP_NOT_ON_TEAM;
                } else {
                    groupsGetInfoError = GroupsGetInfoError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupsGetInfoError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
