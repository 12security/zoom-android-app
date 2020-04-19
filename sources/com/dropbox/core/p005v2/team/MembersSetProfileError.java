package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersSetProfileError */
public enum MembersSetProfileError {
    USER_NOT_FOUND,
    USER_NOT_IN_TEAM,
    EXTERNAL_ID_AND_NEW_EXTERNAL_ID_UNSAFE,
    NO_NEW_DATA_SPECIFIED,
    EMAIL_RESERVED_FOR_OTHER_USER,
    EXTERNAL_ID_USED_BY_OTHER_USER,
    SET_PROFILE_DISALLOWED,
    PARAM_CANNOT_BE_EMPTY,
    PERSISTENT_ID_DISABLED,
    PERSISTENT_ID_USED_BY_OTHER_USER,
    DIRECTORY_RESTRICTED_OFF,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersSetProfileError$Serializer */
    static class Serializer extends UnionSerializer<MembersSetProfileError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersSetProfileError membersSetProfileError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersSetProfileError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                case EXTERNAL_ID_AND_NEW_EXTERNAL_ID_UNSAFE:
                    jsonGenerator.writeString("external_id_and_new_external_id_unsafe");
                    return;
                case NO_NEW_DATA_SPECIFIED:
                    jsonGenerator.writeString("no_new_data_specified");
                    return;
                case EMAIL_RESERVED_FOR_OTHER_USER:
                    jsonGenerator.writeString("email_reserved_for_other_user");
                    return;
                case EXTERNAL_ID_USED_BY_OTHER_USER:
                    jsonGenerator.writeString("external_id_used_by_other_user");
                    return;
                case SET_PROFILE_DISALLOWED:
                    jsonGenerator.writeString("set_profile_disallowed");
                    return;
                case PARAM_CANNOT_BE_EMPTY:
                    jsonGenerator.writeString("param_cannot_be_empty");
                    return;
                case PERSISTENT_ID_DISABLED:
                    jsonGenerator.writeString("persistent_id_disabled");
                    return;
                case PERSISTENT_ID_USED_BY_OTHER_USER:
                    jsonGenerator.writeString("persistent_id_used_by_other_user");
                    return;
                case DIRECTORY_RESTRICTED_OFF:
                    jsonGenerator.writeString("directory_restricted_off");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MembersSetProfileError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersSetProfileError membersSetProfileError;
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
                if ("user_not_found".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.USER_NOT_FOUND;
                } else if ("user_not_in_team".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.USER_NOT_IN_TEAM;
                } else if ("external_id_and_new_external_id_unsafe".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.EXTERNAL_ID_AND_NEW_EXTERNAL_ID_UNSAFE;
                } else if ("no_new_data_specified".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.NO_NEW_DATA_SPECIFIED;
                } else if ("email_reserved_for_other_user".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.EMAIL_RESERVED_FOR_OTHER_USER;
                } else if ("external_id_used_by_other_user".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.EXTERNAL_ID_USED_BY_OTHER_USER;
                } else if ("set_profile_disallowed".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.SET_PROFILE_DISALLOWED;
                } else if ("param_cannot_be_empty".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.PARAM_CANNOT_BE_EMPTY;
                } else if ("persistent_id_disabled".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.PERSISTENT_ID_DISABLED;
                } else if ("persistent_id_used_by_other_user".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.PERSISTENT_ID_USED_BY_OTHER_USER;
                } else if ("directory_restricted_off".equals(str)) {
                    membersSetProfileError = MembersSetProfileError.DIRECTORY_RESTRICTED_OFF;
                } else {
                    membersSetProfileError = MembersSetProfileError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersSetProfileError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
