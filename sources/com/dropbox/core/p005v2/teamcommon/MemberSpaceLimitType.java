package com.dropbox.core.p005v2.teamcommon;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamcommon.MemberSpaceLimitType */
public enum MemberSpaceLimitType {
    OFF,
    ALERT_ONLY,
    STOP_SYNC,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamcommon.MemberSpaceLimitType$Serializer */
    public static class Serializer extends UnionSerializer<MemberSpaceLimitType> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MemberSpaceLimitType memberSpaceLimitType, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberSpaceLimitType) {
                case OFF:
                    jsonGenerator.writeString("off");
                    return;
                case ALERT_ONLY:
                    jsonGenerator.writeString("alert_only");
                    return;
                case STOP_SYNC:
                    jsonGenerator.writeString("stop_sync");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberSpaceLimitType deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberSpaceLimitType memberSpaceLimitType;
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
                if ("off".equals(str)) {
                    memberSpaceLimitType = MemberSpaceLimitType.OFF;
                } else if ("alert_only".equals(str)) {
                    memberSpaceLimitType = MemberSpaceLimitType.ALERT_ONLY;
                } else if ("stop_sync".equals(str)) {
                    memberSpaceLimitType = MemberSpaceLimitType.STOP_SYNC;
                } else {
                    memberSpaceLimitType = MemberSpaceLimitType.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberSpaceLimitType;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
