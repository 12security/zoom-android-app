package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersRemoveError */
public enum MembersRemoveError {
    USER_NOT_FOUND,
    USER_NOT_IN_TEAM,
    OTHER,
    REMOVED_AND_TRANSFER_DEST_SHOULD_DIFFER,
    REMOVED_AND_TRANSFER_ADMIN_SHOULD_DIFFER,
    TRANSFER_DEST_USER_NOT_FOUND,
    TRANSFER_DEST_USER_NOT_IN_TEAM,
    TRANSFER_ADMIN_USER_NOT_IN_TEAM,
    TRANSFER_ADMIN_USER_NOT_FOUND,
    UNSPECIFIED_TRANSFER_ADMIN_ID,
    TRANSFER_ADMIN_IS_NOT_ADMIN,
    RECIPIENT_NOT_VERIFIED,
    REMOVE_LAST_ADMIN,
    CANNOT_KEEP_ACCOUNT_AND_TRANSFER,
    CANNOT_KEEP_ACCOUNT_AND_DELETE_DATA,
    EMAIL_ADDRESS_TOO_LONG_TO_BE_DISABLED,
    CANNOT_KEEP_INVITED_USER_ACCOUNT;

    /* renamed from: com.dropbox.core.v2.team.MembersRemoveError$Serializer */
    static class Serializer extends UnionSerializer<MembersRemoveError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersRemoveError membersRemoveError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersRemoveError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case REMOVED_AND_TRANSFER_DEST_SHOULD_DIFFER:
                    jsonGenerator.writeString("removed_and_transfer_dest_should_differ");
                    return;
                case REMOVED_AND_TRANSFER_ADMIN_SHOULD_DIFFER:
                    jsonGenerator.writeString("removed_and_transfer_admin_should_differ");
                    return;
                case TRANSFER_DEST_USER_NOT_FOUND:
                    jsonGenerator.writeString("transfer_dest_user_not_found");
                    return;
                case TRANSFER_DEST_USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("transfer_dest_user_not_in_team");
                    return;
                case TRANSFER_ADMIN_USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("transfer_admin_user_not_in_team");
                    return;
                case TRANSFER_ADMIN_USER_NOT_FOUND:
                    jsonGenerator.writeString("transfer_admin_user_not_found");
                    return;
                case UNSPECIFIED_TRANSFER_ADMIN_ID:
                    jsonGenerator.writeString("unspecified_transfer_admin_id");
                    return;
                case TRANSFER_ADMIN_IS_NOT_ADMIN:
                    jsonGenerator.writeString("transfer_admin_is_not_admin");
                    return;
                case RECIPIENT_NOT_VERIFIED:
                    jsonGenerator.writeString("recipient_not_verified");
                    return;
                case REMOVE_LAST_ADMIN:
                    jsonGenerator.writeString("remove_last_admin");
                    return;
                case CANNOT_KEEP_ACCOUNT_AND_TRANSFER:
                    jsonGenerator.writeString("cannot_keep_account_and_transfer");
                    return;
                case CANNOT_KEEP_ACCOUNT_AND_DELETE_DATA:
                    jsonGenerator.writeString("cannot_keep_account_and_delete_data");
                    return;
                case EMAIL_ADDRESS_TOO_LONG_TO_BE_DISABLED:
                    jsonGenerator.writeString("email_address_too_long_to_be_disabled");
                    return;
                case CANNOT_KEEP_INVITED_USER_ACCOUNT:
                    jsonGenerator.writeString("cannot_keep_invited_user_account");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersRemoveError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersRemoveError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersRemoveError membersRemoveError;
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
                    membersRemoveError = MembersRemoveError.USER_NOT_FOUND;
                } else if ("user_not_in_team".equals(str)) {
                    membersRemoveError = MembersRemoveError.USER_NOT_IN_TEAM;
                } else if ("other".equals(str)) {
                    membersRemoveError = MembersRemoveError.OTHER;
                } else if ("removed_and_transfer_dest_should_differ".equals(str)) {
                    membersRemoveError = MembersRemoveError.REMOVED_AND_TRANSFER_DEST_SHOULD_DIFFER;
                } else if ("removed_and_transfer_admin_should_differ".equals(str)) {
                    membersRemoveError = MembersRemoveError.REMOVED_AND_TRANSFER_ADMIN_SHOULD_DIFFER;
                } else if ("transfer_dest_user_not_found".equals(str)) {
                    membersRemoveError = MembersRemoveError.TRANSFER_DEST_USER_NOT_FOUND;
                } else if ("transfer_dest_user_not_in_team".equals(str)) {
                    membersRemoveError = MembersRemoveError.TRANSFER_DEST_USER_NOT_IN_TEAM;
                } else if ("transfer_admin_user_not_in_team".equals(str)) {
                    membersRemoveError = MembersRemoveError.TRANSFER_ADMIN_USER_NOT_IN_TEAM;
                } else if ("transfer_admin_user_not_found".equals(str)) {
                    membersRemoveError = MembersRemoveError.TRANSFER_ADMIN_USER_NOT_FOUND;
                } else if ("unspecified_transfer_admin_id".equals(str)) {
                    membersRemoveError = MembersRemoveError.UNSPECIFIED_TRANSFER_ADMIN_ID;
                } else if ("transfer_admin_is_not_admin".equals(str)) {
                    membersRemoveError = MembersRemoveError.TRANSFER_ADMIN_IS_NOT_ADMIN;
                } else if ("recipient_not_verified".equals(str)) {
                    membersRemoveError = MembersRemoveError.RECIPIENT_NOT_VERIFIED;
                } else if ("remove_last_admin".equals(str)) {
                    membersRemoveError = MembersRemoveError.REMOVE_LAST_ADMIN;
                } else if ("cannot_keep_account_and_transfer".equals(str)) {
                    membersRemoveError = MembersRemoveError.CANNOT_KEEP_ACCOUNT_AND_TRANSFER;
                } else if ("cannot_keep_account_and_delete_data".equals(str)) {
                    membersRemoveError = MembersRemoveError.CANNOT_KEEP_ACCOUNT_AND_DELETE_DATA;
                } else if ("email_address_too_long_to_be_disabled".equals(str)) {
                    membersRemoveError = MembersRemoveError.EMAIL_ADDRESS_TOO_LONG_TO_BE_DISABLED;
                } else if ("cannot_keep_invited_user_account".equals(str)) {
                    membersRemoveError = MembersRemoveError.CANNOT_KEEP_INVITED_USER_ACCOUNT;
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
                return membersRemoveError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
