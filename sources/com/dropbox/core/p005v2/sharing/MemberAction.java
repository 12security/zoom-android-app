package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.MemberAction */
public enum MemberAction {
    LEAVE_A_COPY,
    MAKE_EDITOR,
    MAKE_OWNER,
    MAKE_VIEWER,
    MAKE_VIEWER_NO_COMMENT,
    REMOVE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.MemberAction$Serializer */
    static class Serializer extends UnionSerializer<MemberAction> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MemberAction memberAction, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberAction) {
                case LEAVE_A_COPY:
                    jsonGenerator.writeString("leave_a_copy");
                    return;
                case MAKE_EDITOR:
                    jsonGenerator.writeString("make_editor");
                    return;
                case MAKE_OWNER:
                    jsonGenerator.writeString("make_owner");
                    return;
                case MAKE_VIEWER:
                    jsonGenerator.writeString("make_viewer");
                    return;
                case MAKE_VIEWER_NO_COMMENT:
                    jsonGenerator.writeString("make_viewer_no_comment");
                    return;
                case REMOVE:
                    jsonGenerator.writeString("remove");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberAction deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberAction memberAction;
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
                if ("leave_a_copy".equals(str)) {
                    memberAction = MemberAction.LEAVE_A_COPY;
                } else if ("make_editor".equals(str)) {
                    memberAction = MemberAction.MAKE_EDITOR;
                } else if ("make_owner".equals(str)) {
                    memberAction = MemberAction.MAKE_OWNER;
                } else if ("make_viewer".equals(str)) {
                    memberAction = MemberAction.MAKE_VIEWER;
                } else if ("make_viewer_no_comment".equals(str)) {
                    memberAction = MemberAction.MAKE_VIEWER_NO_COMMENT;
                } else if ("remove".equals(str)) {
                    memberAction = MemberAction.REMOVE;
                } else {
                    memberAction = MemberAction.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberAction;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
