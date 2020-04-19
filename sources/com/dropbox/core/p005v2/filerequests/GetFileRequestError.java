package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.filerequests.GetFileRequestError */
public enum GetFileRequestError {
    DISABLED_FOR_TEAM,
    OTHER,
    NOT_FOUND,
    NOT_A_FOLDER,
    APP_LACKS_ACCESS,
    NO_PERMISSION,
    EMAIL_UNVERIFIED,
    VALIDATION_ERROR;

    /* renamed from: com.dropbox.core.v2.filerequests.GetFileRequestError$Serializer */
    static class Serializer extends UnionSerializer<GetFileRequestError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GetFileRequestError getFileRequestError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (getFileRequestError) {
                case DISABLED_FOR_TEAM:
                    jsonGenerator.writeString("disabled_for_team");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case NOT_FOUND:
                    jsonGenerator.writeString("not_found");
                    return;
                case NOT_A_FOLDER:
                    jsonGenerator.writeString("not_a_folder");
                    return;
                case APP_LACKS_ACCESS:
                    jsonGenerator.writeString("app_lacks_access");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case EMAIL_UNVERIFIED:
                    jsonGenerator.writeString("email_unverified");
                    return;
                case VALIDATION_ERROR:
                    jsonGenerator.writeString("validation_error");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(getFileRequestError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GetFileRequestError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetFileRequestError getFileRequestError;
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
                if ("disabled_for_team".equals(str)) {
                    getFileRequestError = GetFileRequestError.DISABLED_FOR_TEAM;
                } else if ("other".equals(str)) {
                    getFileRequestError = GetFileRequestError.OTHER;
                } else if ("not_found".equals(str)) {
                    getFileRequestError = GetFileRequestError.NOT_FOUND;
                } else if ("not_a_folder".equals(str)) {
                    getFileRequestError = GetFileRequestError.NOT_A_FOLDER;
                } else if ("app_lacks_access".equals(str)) {
                    getFileRequestError = GetFileRequestError.APP_LACKS_ACCESS;
                } else if ("no_permission".equals(str)) {
                    getFileRequestError = GetFileRequestError.NO_PERMISSION;
                } else if ("email_unverified".equals(str)) {
                    getFileRequestError = GetFileRequestError.EMAIL_UNVERIFIED;
                } else if ("validation_error".equals(str)) {
                    getFileRequestError = GetFileRequestError.VALIDATION_ERROR;
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
                return getFileRequestError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
