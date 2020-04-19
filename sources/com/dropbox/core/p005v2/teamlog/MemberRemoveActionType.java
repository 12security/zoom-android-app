package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.MemberRemoveActionType */
public enum MemberRemoveActionType {
    DELETE,
    OFFBOARD,
    LEAVE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberRemoveActionType$Serializer */
    static class Serializer extends UnionSerializer<MemberRemoveActionType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MemberRemoveActionType memberRemoveActionType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberRemoveActionType) {
                case DELETE:
                    jsonGenerator.writeString("delete");
                    return;
                case OFFBOARD:
                    jsonGenerator.writeString("offboard");
                    return;
                case LEAVE:
                    jsonGenerator.writeString("leave");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberRemoveActionType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberRemoveActionType memberRemoveActionType;
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
                if ("delete".equals(str)) {
                    memberRemoveActionType = MemberRemoveActionType.DELETE;
                } else if ("offboard".equals(str)) {
                    memberRemoveActionType = MemberRemoveActionType.OFFBOARD;
                } else if ("leave".equals(str)) {
                    memberRemoveActionType = MemberRemoveActionType.LEAVE;
                } else {
                    memberRemoveActionType = MemberRemoveActionType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberRemoveActionType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
