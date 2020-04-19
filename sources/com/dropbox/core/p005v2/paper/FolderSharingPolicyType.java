package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.FolderSharingPolicyType */
public enum FolderSharingPolicyType {
    TEAM,
    INVITE_ONLY;

    /* renamed from: com.dropbox.core.v2.paper.FolderSharingPolicyType$Serializer */
    static class Serializer extends UnionSerializer<FolderSharingPolicyType> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(FolderSharingPolicyType folderSharingPolicyType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (folderSharingPolicyType) {
                case TEAM:
                    jsonGenerator.writeString("team");
                    return;
                case INVITE_ONLY:
                    jsonGenerator.writeString("invite_only");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(folderSharingPolicyType);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public FolderSharingPolicyType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FolderSharingPolicyType folderSharingPolicyType;
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
                if ("team".equals(str)) {
                    folderSharingPolicyType = FolderSharingPolicyType.TEAM;
                } else if ("invite_only".equals(str)) {
                    folderSharingPolicyType = FolderSharingPolicyType.INVITE_ONLY;
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
                return folderSharingPolicyType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
