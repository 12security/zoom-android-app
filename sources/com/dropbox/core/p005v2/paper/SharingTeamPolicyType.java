package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.SharingTeamPolicyType */
public enum SharingTeamPolicyType {
    PEOPLE_WITH_LINK_CAN_EDIT,
    PEOPLE_WITH_LINK_CAN_VIEW_AND_COMMENT,
    INVITE_ONLY;

    /* renamed from: com.dropbox.core.v2.paper.SharingTeamPolicyType$Serializer */
    static class Serializer extends UnionSerializer<SharingTeamPolicyType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingTeamPolicyType sharingTeamPolicyType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharingTeamPolicyType) {
                case PEOPLE_WITH_LINK_CAN_EDIT:
                    jsonGenerator.writeString("people_with_link_can_edit");
                    return;
                case PEOPLE_WITH_LINK_CAN_VIEW_AND_COMMENT:
                    jsonGenerator.writeString("people_with_link_can_view_and_comment");
                    return;
                case INVITE_ONLY:
                    jsonGenerator.writeString("invite_only");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(sharingTeamPolicyType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SharingTeamPolicyType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingTeamPolicyType sharingTeamPolicyType;
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
                if ("people_with_link_can_edit".equals(str)) {
                    sharingTeamPolicyType = SharingTeamPolicyType.PEOPLE_WITH_LINK_CAN_EDIT;
                } else if ("people_with_link_can_view_and_comment".equals(str)) {
                    sharingTeamPolicyType = SharingTeamPolicyType.PEOPLE_WITH_LINK_CAN_VIEW_AND_COMMENT;
                } else if ("invite_only".equals(str)) {
                    sharingTeamPolicyType = SharingTeamPolicyType.INVITE_ONLY;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharingTeamPolicyType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
