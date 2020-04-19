package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.filerequests.CreateFileRequestError */
public enum CreateFileRequestError {
    DISABLED_FOR_TEAM,
    OTHER,
    NOT_FOUND,
    NOT_A_FOLDER,
    APP_LACKS_ACCESS,
    NO_PERMISSION,
    EMAIL_UNVERIFIED,
    VALIDATION_ERROR,
    INVALID_LOCATION,
    RATE_LIMIT;

    /* renamed from: com.dropbox.core.v2.filerequests.CreateFileRequestError$Serializer */
    static class Serializer extends UnionSerializer<CreateFileRequestError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(CreateFileRequestError createFileRequestError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (createFileRequestError) {
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
                case INVALID_LOCATION:
                    jsonGenerator.writeString("invalid_location");
                    return;
                case RATE_LIMIT:
                    jsonGenerator.writeString("rate_limit");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(createFileRequestError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public CreateFileRequestError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            CreateFileRequestError createFileRequestError;
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
                    createFileRequestError = CreateFileRequestError.DISABLED_FOR_TEAM;
                } else if ("other".equals(str)) {
                    createFileRequestError = CreateFileRequestError.OTHER;
                } else if ("not_found".equals(str)) {
                    createFileRequestError = CreateFileRequestError.NOT_FOUND;
                } else if ("not_a_folder".equals(str)) {
                    createFileRequestError = CreateFileRequestError.NOT_A_FOLDER;
                } else if ("app_lacks_access".equals(str)) {
                    createFileRequestError = CreateFileRequestError.APP_LACKS_ACCESS;
                } else if ("no_permission".equals(str)) {
                    createFileRequestError = CreateFileRequestError.NO_PERMISSION;
                } else if ("email_unverified".equals(str)) {
                    createFileRequestError = CreateFileRequestError.EMAIL_UNVERIFIED;
                } else if ("validation_error".equals(str)) {
                    createFileRequestError = CreateFileRequestError.VALIDATION_ERROR;
                } else if ("invalid_location".equals(str)) {
                    createFileRequestError = CreateFileRequestError.INVALID_LOCATION;
                } else if ("rate_limit".equals(str)) {
                    createFileRequestError = CreateFileRequestError.RATE_LIMIT;
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
                return createFileRequestError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
