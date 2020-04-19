package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.SharingPublicPolicyType */
public enum SharingPublicPolicyType {
    PEOPLE_WITH_LINK_CAN_EDIT,
    PEOPLE_WITH_LINK_CAN_VIEW_AND_COMMENT,
    INVITE_ONLY,
    DISABLED;

    /* renamed from: com.dropbox.core.v2.paper.SharingPublicPolicyType$Serializer */
    static class Serializer extends UnionSerializer<SharingPublicPolicyType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingPublicPolicyType sharingPublicPolicyType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharingPublicPolicyType) {
                case PEOPLE_WITH_LINK_CAN_EDIT:
                    jsonGenerator.writeString("people_with_link_can_edit");
                    return;
                case PEOPLE_WITH_LINK_CAN_VIEW_AND_COMMENT:
                    jsonGenerator.writeString("people_with_link_can_view_and_comment");
                    return;
                case INVITE_ONLY:
                    jsonGenerator.writeString("invite_only");
                    return;
                case DISABLED:
                    jsonGenerator.writeString("disabled");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(sharingPublicPolicyType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SharingPublicPolicyType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingPublicPolicyType sharingPublicPolicyType;
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
                    sharingPublicPolicyType = SharingPublicPolicyType.PEOPLE_WITH_LINK_CAN_EDIT;
                } else if ("people_with_link_can_view_and_comment".equals(str)) {
                    sharingPublicPolicyType = SharingPublicPolicyType.PEOPLE_WITH_LINK_CAN_VIEW_AND_COMMENT;
                } else if ("invite_only".equals(str)) {
                    sharingPublicPolicyType = SharingPublicPolicyType.INVITE_ONLY;
                } else if ("disabled".equals(str)) {
                    sharingPublicPolicyType = SharingPublicPolicyType.DISABLED;
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
                return sharingPublicPolicyType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
