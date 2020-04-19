package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupsPollError */
public enum GroupsPollError {
    INVALID_ASYNC_JOB_ID,
    INTERNAL_ERROR,
    OTHER,
    ACCESS_DENIED;

    /* renamed from: com.dropbox.core.v2.team.GroupsPollError$Serializer */
    static class Serializer extends UnionSerializer<GroupsPollError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupsPollError groupsPollError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupsPollError) {
                case INVALID_ASYNC_JOB_ID:
                    jsonGenerator.writeString("invalid_async_job_id");
                    return;
                case INTERNAL_ERROR:
                    jsonGenerator.writeString("internal_error");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case ACCESS_DENIED:
                    jsonGenerator.writeString(AAD.WEB_UI_CANCEL);
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupsPollError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupsPollError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupsPollError groupsPollError;
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
                if ("invalid_async_job_id".equals(str)) {
                    groupsPollError = GroupsPollError.INVALID_ASYNC_JOB_ID;
                } else if ("internal_error".equals(str)) {
                    groupsPollError = GroupsPollError.INTERNAL_ERROR;
                } else if ("other".equals(str)) {
                    groupsPollError = GroupsPollError.OTHER;
                } else if (AAD.WEB_UI_CANCEL.equals(str)) {
                    groupsPollError = GroupsPollError.ACCESS_DENIED;
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
                return groupsPollError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
