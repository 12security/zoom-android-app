package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupSelectorError */
public enum GroupSelectorError {
    GROUP_NOT_FOUND,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.GroupSelectorError$1 */
    static /* synthetic */ class C08161 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$GroupSelectorError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$GroupSelectorError = new int[GroupSelectorError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$GroupSelectorError[GroupSelectorError.GROUP_NOT_FOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupSelectorError$Serializer */
    static class Serializer extends UnionSerializer<GroupSelectorError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupSelectorError groupSelectorError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08161.$SwitchMap$com$dropbox$core$v2$team$GroupSelectorError[groupSelectorError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("group_not_found");
            }
        }

        public GroupSelectorError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupSelectorError groupSelectorError;
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
                if ("group_not_found".equals(str)) {
                    groupSelectorError = GroupSelectorError.GROUP_NOT_FOUND;
                } else {
                    groupSelectorError = GroupSelectorError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupSelectorError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
