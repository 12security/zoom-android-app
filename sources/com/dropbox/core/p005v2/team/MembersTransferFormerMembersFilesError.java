package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersTransferFormerMembersFilesError */
public enum MembersTransferFormerMembersFilesError {
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
    USER_DATA_IS_BEING_TRANSFERRED,
    USER_NOT_REMOVED,
    USER_DATA_CANNOT_BE_TRANSFERRED,
    USER_DATA_ALREADY_TRANSFERRED;

    /* renamed from: com.dropbox.core.v2.team.MembersTransferFormerMembersFilesError$Serializer */
    static class Serializer extends UnionSerializer<MembersTransferFormerMembersFilesError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersTransferFormerMembersFilesError membersTransferFormerMembersFilesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersTransferFormerMembersFilesError) {
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
                case USER_DATA_IS_BEING_TRANSFERRED:
                    jsonGenerator.writeString("user_data_is_being_transferred");
                    return;
                case USER_NOT_REMOVED:
                    jsonGenerator.writeString("user_not_removed");
                    return;
                case USER_DATA_CANNOT_BE_TRANSFERRED:
                    jsonGenerator.writeString("user_data_cannot_be_transferred");
                    return;
                case USER_DATA_ALREADY_TRANSFERRED:
                    jsonGenerator.writeString("user_data_already_transferred");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersTransferFormerMembersFilesError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersTransferFormerMembersFilesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersTransferFormerMembersFilesError membersTransferFormerMembersFilesError;
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
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.USER_NOT_FOUND;
                } else if ("user_not_in_team".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.USER_NOT_IN_TEAM;
                } else if ("other".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.OTHER;
                } else if ("removed_and_transfer_dest_should_differ".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.REMOVED_AND_TRANSFER_DEST_SHOULD_DIFFER;
                } else if ("removed_and_transfer_admin_should_differ".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.REMOVED_AND_TRANSFER_ADMIN_SHOULD_DIFFER;
                } else if ("transfer_dest_user_not_found".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.TRANSFER_DEST_USER_NOT_FOUND;
                } else if ("transfer_dest_user_not_in_team".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.TRANSFER_DEST_USER_NOT_IN_TEAM;
                } else if ("transfer_admin_user_not_in_team".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.TRANSFER_ADMIN_USER_NOT_IN_TEAM;
                } else if ("transfer_admin_user_not_found".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.TRANSFER_ADMIN_USER_NOT_FOUND;
                } else if ("unspecified_transfer_admin_id".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.UNSPECIFIED_TRANSFER_ADMIN_ID;
                } else if ("transfer_admin_is_not_admin".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.TRANSFER_ADMIN_IS_NOT_ADMIN;
                } else if ("recipient_not_verified".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.RECIPIENT_NOT_VERIFIED;
                } else if ("user_data_is_being_transferred".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.USER_DATA_IS_BEING_TRANSFERRED;
                } else if ("user_not_removed".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.USER_NOT_REMOVED;
                } else if ("user_data_cannot_be_transferred".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.USER_DATA_CANNOT_BE_TRANSFERRED;
                } else if ("user_data_already_transferred".equals(str)) {
                    membersTransferFormerMembersFilesError = MembersTransferFormerMembersFilesError.USER_DATA_ALREADY_TRANSFERRED;
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
                return membersTransferFormerMembersFilesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
