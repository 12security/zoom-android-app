package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.UnshareFileError */
public final class UnshareFileError {
    public static final UnshareFileError OTHER = new UnshareFileError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharingUserError userErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.UnshareFileError$Serializer */
    static class Serializer extends UnionSerializer<UnshareFileError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UnshareFileError unshareFileError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (unshareFileError.tag()) {
                case USER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("user_error", jsonGenerator);
                    jsonGenerator.writeFieldName("user_error");
                    Serializer.INSTANCE.serialize(unshareFileError.userErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(unshareFileError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UnshareFileError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UnshareFileError unshareFileError;
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
                    unshareFileError = UnshareFileError.userError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    unshareFileError = UnshareFileError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    unshareFileError = UnshareFileError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return unshareFileError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UnshareFileError$Tag */
    public enum Tag {
        USER_ERROR,
        ACCESS_ERROR,
        OTHER
    }

    private UnshareFileError() {
    }

    private UnshareFileError withTag(Tag tag) {
        UnshareFileError unshareFileError = new UnshareFileError();
        unshareFileError._tag = tag;
        return unshareFileError;
    }

    private UnshareFileError withTagAndUserError(Tag tag, SharingUserError sharingUserError) {
        UnshareFileError unshareFileError = new UnshareFileError();
        unshareFileError._tag = tag;
        unshareFileError.userErrorValue = sharingUserError;
        return unshareFileError;
    }

    private UnshareFileError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        UnshareFileError unshareFileError = new UnshareFileError();
        unshareFileError._tag = tag;
        unshareFileError.accessErrorValue = sharingFileAccessError;
        return unshareFileError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserError() {
        return this._tag == Tag.USER_ERROR;
    }

    public static UnshareFileError userError(SharingUserError sharingUserError) {
        if (sharingUserError != null) {
            return new UnshareFileError().withTagAndUserError(Tag.USER_ERROR, sharingUserError);
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

    public static UnshareFileError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new UnshareFileError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
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
        if (obj == null || !(obj instanceof UnshareFileError)) {
            return false;
        }
        UnshareFileError unshareFileError = (UnshareFileError) obj;
        if (this._tag != unshareFileError._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_ERROR:
                SharingUserError sharingUserError = this.userErrorValue;
                SharingUserError sharingUserError2 = unshareFileError.userErrorValue;
                if (sharingUserError != sharingUserError2 && !sharingUserError.equals(sharingUserError2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = unshareFileError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
                    z = false;
                }
                return z;
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
