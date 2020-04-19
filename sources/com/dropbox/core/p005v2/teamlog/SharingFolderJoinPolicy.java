package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.SharingFolderJoinPolicy */
public enum SharingFolderJoinPolicy {
    FROM_ANYONE,
    FROM_TEAM_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SharingFolderJoinPolicy$Serializer */
    static class Serializer extends UnionSerializer<SharingFolderJoinPolicy> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingFolderJoinPolicy sharingFolderJoinPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharingFolderJoinPolicy) {
                case FROM_ANYONE:
                    jsonGenerator.writeString("from_anyone");
                    return;
                case FROM_TEAM_ONLY:
                    jsonGenerator.writeString("from_team_only");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharingFolderJoinPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingFolderJoinPolicy sharingFolderJoinPolicy;
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
                if ("from_anyone".equals(str)) {
                    sharingFolderJoinPolicy = SharingFolderJoinPolicy.FROM_ANYONE;
                } else if ("from_team_only".equals(str)) {
                    sharingFolderJoinPolicy = SharingFolderJoinPolicy.FROM_TEAM_ONLY;
                } else {
                    sharingFolderJoinPolicy = SharingFolderJoinPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharingFolderJoinPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
