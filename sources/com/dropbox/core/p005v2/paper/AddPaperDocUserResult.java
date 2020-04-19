package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.paper.AddPaperDocUserResult */
public enum AddPaperDocUserResult {
    SUCCESS,
    UNKNOWN_ERROR,
    SHARING_OUTSIDE_TEAM_DISABLED,
    DAILY_LIMIT_REACHED,
    USER_IS_OWNER,
    FAILED_USER_DATA_RETRIEVAL,
    PERMISSION_ALREADY_GRANTED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.paper.AddPaperDocUserResult$Serializer */
    static class Serializer extends UnionSerializer<AddPaperDocUserResult> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AddPaperDocUserResult addPaperDocUserResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (addPaperDocUserResult) {
                case SUCCESS:
                    jsonGenerator.writeString(Param.SUCCESS);
                    return;
                case UNKNOWN_ERROR:
                    jsonGenerator.writeString("unknown_error");
                    return;
                case SHARING_OUTSIDE_TEAM_DISABLED:
                    jsonGenerator.writeString("sharing_outside_team_disabled");
                    return;
                case DAILY_LIMIT_REACHED:
                    jsonGenerator.writeString("daily_limit_reached");
                    return;
                case USER_IS_OWNER:
                    jsonGenerator.writeString("user_is_owner");
                    return;
                case FAILED_USER_DATA_RETRIEVAL:
                    jsonGenerator.writeString("failed_user_data_retrieval");
                    return;
                case PERMISSION_ALREADY_GRANTED:
                    jsonGenerator.writeString("permission_already_granted");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AddPaperDocUserResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AddPaperDocUserResult addPaperDocUserResult;
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
                if (Param.SUCCESS.equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.SUCCESS;
                } else if ("unknown_error".equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.UNKNOWN_ERROR;
                } else if ("sharing_outside_team_disabled".equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.SHARING_OUTSIDE_TEAM_DISABLED;
                } else if ("daily_limit_reached".equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.DAILY_LIMIT_REACHED;
                } else if ("user_is_owner".equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.USER_IS_OWNER;
                } else if ("failed_user_data_retrieval".equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.FAILED_USER_DATA_RETRIEVAL;
                } else if ("permission_already_granted".equals(str)) {
                    addPaperDocUserResult = AddPaperDocUserResult.PERMISSION_ALREADY_GRANTED;
                } else {
                    addPaperDocUserResult = AddPaperDocUserResult.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return addPaperDocUserResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
