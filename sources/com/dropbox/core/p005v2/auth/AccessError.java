package com.dropbox.core.p005v2.auth;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.auth.AccessError */
public final class AccessError {
    public static final AccessError OTHER = new AccessError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public InvalidAccountTypeError invalidAccountTypeValue;
    /* access modifiers changed from: private */
    public PaperAccessError paperAccessDeniedValue;

    /* renamed from: com.dropbox.core.v2.auth.AccessError$Serializer */
    public static class Serializer extends UnionSerializer<AccessError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(AccessError accessError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (accessError.tag()) {
                case INVALID_ACCOUNT_TYPE:
                    jsonGenerator.writeStartObject();
                    writeTag("invalid_account_type", jsonGenerator);
                    jsonGenerator.writeFieldName("invalid_account_type");
                    com.dropbox.core.p005v2.auth.InvalidAccountTypeError.Serializer.INSTANCE.serialize(accessError.invalidAccountTypeValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PAPER_ACCESS_DENIED:
                    jsonGenerator.writeStartObject();
                    writeTag("paper_access_denied", jsonGenerator);
                    jsonGenerator.writeFieldName("paper_access_denied");
                    com.dropbox.core.p005v2.auth.PaperAccessError.Serializer.INSTANCE.serialize(accessError.paperAccessDeniedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AccessError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AccessError accessError;
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
                if ("invalid_account_type".equals(str)) {
                    expectField("invalid_account_type", jsonParser);
                    accessError = AccessError.invalidAccountType(com.dropbox.core.p005v2.auth.InvalidAccountTypeError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("paper_access_denied".equals(str)) {
                    expectField("paper_access_denied", jsonParser);
                    accessError = AccessError.paperAccessDenied(com.dropbox.core.p005v2.auth.PaperAccessError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    accessError = AccessError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return accessError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.auth.AccessError$Tag */
    public enum Tag {
        INVALID_ACCOUNT_TYPE,
        PAPER_ACCESS_DENIED,
        OTHER
    }

    private AccessError() {
    }

    private AccessError withTag(Tag tag) {
        AccessError accessError = new AccessError();
        accessError._tag = tag;
        return accessError;
    }

    private AccessError withTagAndInvalidAccountType(Tag tag, InvalidAccountTypeError invalidAccountTypeError) {
        AccessError accessError = new AccessError();
        accessError._tag = tag;
        accessError.invalidAccountTypeValue = invalidAccountTypeError;
        return accessError;
    }

    private AccessError withTagAndPaperAccessDenied(Tag tag, PaperAccessError paperAccessError) {
        AccessError accessError = new AccessError();
        accessError._tag = tag;
        accessError.paperAccessDeniedValue = paperAccessError;
        return accessError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInvalidAccountType() {
        return this._tag == Tag.INVALID_ACCOUNT_TYPE;
    }

    public static AccessError invalidAccountType(InvalidAccountTypeError invalidAccountTypeError) {
        if (invalidAccountTypeError != null) {
            return new AccessError().withTagAndInvalidAccountType(Tag.INVALID_ACCOUNT_TYPE, invalidAccountTypeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public InvalidAccountTypeError getInvalidAccountTypeValue() {
        if (this._tag == Tag.INVALID_ACCOUNT_TYPE) {
            return this.invalidAccountTypeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INVALID_ACCOUNT_TYPE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPaperAccessDenied() {
        return this._tag == Tag.PAPER_ACCESS_DENIED;
    }

    public static AccessError paperAccessDenied(PaperAccessError paperAccessError) {
        if (paperAccessError != null) {
            return new AccessError().withTagAndPaperAccessDenied(Tag.PAPER_ACCESS_DENIED, paperAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public PaperAccessError getPaperAccessDeniedValue() {
        if (this._tag == Tag.PAPER_ACCESS_DENIED) {
            return this.paperAccessDeniedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PAPER_ACCESS_DENIED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.invalidAccountTypeValue, this.paperAccessDeniedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AccessError)) {
            return false;
        }
        AccessError accessError = (AccessError) obj;
        if (this._tag != accessError._tag) {
            return false;
        }
        switch (this._tag) {
            case INVALID_ACCOUNT_TYPE:
                InvalidAccountTypeError invalidAccountTypeError = this.invalidAccountTypeValue;
                InvalidAccountTypeError invalidAccountTypeError2 = accessError.invalidAccountTypeValue;
                if (invalidAccountTypeError != invalidAccountTypeError2 && !invalidAccountTypeError.equals(invalidAccountTypeError2)) {
                    z = false;
                }
                return z;
            case PAPER_ACCESS_DENIED:
                PaperAccessError paperAccessError = this.paperAccessDeniedValue;
                PaperAccessError paperAccessError2 = accessError.paperAccessDeniedValue;
                if (paperAccessError != paperAccessError2 && !paperAccessError.equals(paperAccessError2)) {
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
