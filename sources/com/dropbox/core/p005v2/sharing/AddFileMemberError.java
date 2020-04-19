package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.AddFileMemberError */
public final class AddFileMemberError {
    public static final AddFileMemberError INVALID_COMMENT = new AddFileMemberError().withTag(Tag.INVALID_COMMENT);
    public static final AddFileMemberError OTHER = new AddFileMemberError().withTag(Tag.OTHER);
    public static final AddFileMemberError RATE_LIMIT = new AddFileMemberError().withTag(Tag.RATE_LIMIT);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharingUserError userErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.AddFileMemberError$Serializer */
    static class Serializer extends UnionSerializer<AddFileMemberError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddFileMemberError addFileMemberError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (addFileMemberError.tag()) {
                case USER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("user_error", jsonGenerator);
                    jsonGenerator.writeFieldName("user_error");
                    Serializer.INSTANCE.serialize(addFileMemberError.userErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(addFileMemberError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RATE_LIMIT:
                    jsonGenerator.writeString("rate_limit");
                    return;
                case INVALID_COMMENT:
                    jsonGenerator.writeString("invalid_comment");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AddFileMemberError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AddFileMemberError addFileMemberError;
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
                if ("user_error".equals(str)) {
                    expectField("user_error", jsonParser);
                    addFileMemberError = AddFileMemberError.userError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    addFileMemberError = AddFileMemberError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("rate_limit".equals(str)) {
                    addFileMemberError = AddFileMemberError.RATE_LIMIT;
                } else if ("invalid_comment".equals(str)) {
                    addFileMemberError = AddFileMemberError.INVALID_COMMENT;
                } else {
                    addFileMemberError = AddFileMemberError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return addFileMemberError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.AddFileMemberError$Tag */
    public enum Tag {
        USER_ERROR,
        ACCESS_ERROR,
        RATE_LIMIT,
        INVALID_COMMENT,
        OTHER
    }

    private AddFileMemberError() {
    }

    private AddFileMemberError withTag(Tag tag) {
        AddFileMemberError addFileMemberError = new AddFileMemberError();
        addFileMemberError._tag = tag;
        return addFileMemberError;
    }

    private AddFileMemberError withTagAndUserError(Tag tag, SharingUserError sharingUserError) {
        AddFileMemberError addFileMemberError = new AddFileMemberError();
        addFileMemberError._tag = tag;
        addFileMemberError.userErrorValue = sharingUserError;
        return addFileMemberError;
    }

    private AddFileMemberError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        AddFileMemberError addFileMemberError = new AddFileMemberError();
        addFileMemberError._tag = tag;
        addFileMemberError.accessErrorValue = sharingFileAccessError;
        return addFileMemberError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserError() {
        return this._tag == Tag.USER_ERROR;
    }

    public static AddFileMemberError userError(SharingUserError sharingUserError) {
        if (sharingUserError != null) {
            return new AddFileMemberError().withTagAndUserError(Tag.USER_ERROR, sharingUserError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingUserError getUserErrorValue() {
        if (this._tag == Tag.USER_ERROR) {
            return this.userErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static AddFileMemberError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new AddFileMemberError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingFileAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isRateLimit() {
        return this._tag == Tag.RATE_LIMIT;
    }

    public boolean isInvalidComment() {
        return this._tag == Tag.INVALID_COMMENT;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.userErrorValue, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AddFileMemberError)) {
            return false;
        }
        AddFileMemberError addFileMemberError = (AddFileMemberError) obj;
        if (this._tag != addFileMemberError._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_ERROR:
                SharingUserError sharingUserError = this.userErrorValue;
                SharingUserError sharingUserError2 = addFileMemberError.userErrorValue;
                if (sharingUserError != sharingUserError2 && !sharingUserError.equals(sharingUserError2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = addFileMemberError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
                    z = false;
                }
                return z;
            case RATE_LIMIT:
                return true;
            case INVALID_COMMENT:
                return true;
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
