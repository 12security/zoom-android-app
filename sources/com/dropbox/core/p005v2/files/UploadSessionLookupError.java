package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.UploadSessionLookupError */
public final class UploadSessionLookupError {
    public static final UploadSessionLookupError CLOSED = new UploadSessionLookupError().withTag(Tag.CLOSED);
    public static final UploadSessionLookupError NOT_CLOSED = new UploadSessionLookupError().withTag(Tag.NOT_CLOSED);
    public static final UploadSessionLookupError NOT_FOUND = new UploadSessionLookupError().withTag(Tag.NOT_FOUND);
    public static final UploadSessionLookupError OTHER = new UploadSessionLookupError().withTag(Tag.OTHER);
    public static final UploadSessionLookupError TOO_LARGE = new UploadSessionLookupError().withTag(Tag.TOO_LARGE);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UploadSessionOffsetError incorrectOffsetValue;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionLookupError$Serializer */
    static class Serializer extends UnionSerializer<UploadSessionLookupError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionLookupError uploadSessionLookupError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadSessionLookupError.tag()) {
                case NOT_FOUND:
                    jsonGenerator.writeString("not_found");
                    return;
                case INCORRECT_OFFSET:
                    jsonGenerator.writeStartObject();
                    writeTag("incorrect_offset", jsonGenerator);
                    Serializer.INSTANCE.serialize(uploadSessionLookupError.incorrectOffsetValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case CLOSED:
                    jsonGenerator.writeString("closed");
                    return;
                case NOT_CLOSED:
                    jsonGenerator.writeString("not_closed");
                    return;
                case TOO_LARGE:
                    jsonGenerator.writeString("too_large");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UploadSessionLookupError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UploadSessionLookupError uploadSessionLookupError;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("not_found".equals(str)) {
                    uploadSessionLookupError = UploadSessionLookupError.NOT_FOUND;
                } else if ("incorrect_offset".equals(str)) {
                    uploadSessionLookupError = UploadSessionLookupError.incorrectOffset(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("closed".equals(str)) {
                    uploadSessionLookupError = UploadSessionLookupError.CLOSED;
                } else if ("not_closed".equals(str)) {
                    uploadSessionLookupError = UploadSessionLookupError.NOT_CLOSED;
                } else if ("too_large".equals(str)) {
                    uploadSessionLookupError = UploadSessionLookupError.TOO_LARGE;
                } else {
                    uploadSessionLookupError = UploadSessionLookupError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return uploadSessionLookupError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadSessionLookupError$Tag */
    public enum Tag {
        NOT_FOUND,
        INCORRECT_OFFSET,
        CLOSED,
        NOT_CLOSED,
        TOO_LARGE,
        OTHER
    }

    private UploadSessionLookupError() {
    }

    private UploadSessionLookupError withTag(Tag tag) {
        UploadSessionLookupError uploadSessionLookupError = new UploadSessionLookupError();
        uploadSessionLookupError._tag = tag;
        return uploadSessionLookupError;
    }

    private UploadSessionLookupError withTagAndIncorrectOffset(Tag tag, UploadSessionOffsetError uploadSessionOffsetError) {
        UploadSessionLookupError uploadSessionLookupError = new UploadSessionLookupError();
        uploadSessionLookupError._tag = tag;
        uploadSessionLookupError.incorrectOffsetValue = uploadSessionOffsetError;
        return uploadSessionLookupError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isNotFound() {
        return this._tag == Tag.NOT_FOUND;
    }

    public boolean isIncorrectOffset() {
        return this._tag == Tag.INCORRECT_OFFSET;
    }

    public static UploadSessionLookupError incorrectOffset(UploadSessionOffsetError uploadSessionOffsetError) {
        if (uploadSessionOffsetError != null) {
            return new UploadSessionLookupError().withTagAndIncorrectOffset(Tag.INCORRECT_OFFSET, uploadSessionOffsetError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadSessionOffsetError getIncorrectOffsetValue() {
        if (this._tag == Tag.INCORRECT_OFFSET) {
            return this.incorrectOffsetValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INCORRECT_OFFSET, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isClosed() {
        return this._tag == Tag.CLOSED;
    }

    public boolean isNotClosed() {
        return this._tag == Tag.NOT_CLOSED;
    }

    public boolean isTooLarge() {
        return this._tag == Tag.TOO_LARGE;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.incorrectOffsetValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadSessionLookupError)) {
            return false;
        }
        UploadSessionLookupError uploadSessionLookupError = (UploadSessionLookupError) obj;
        if (this._tag != uploadSessionLookupError._tag) {
            return false;
        }
        switch (this._tag) {
            case NOT_FOUND:
                return true;
            case INCORRECT_OFFSET:
                UploadSessionOffsetError uploadSessionOffsetError = this.incorrectOffsetValue;
                UploadSessionOffsetError uploadSessionOffsetError2 = uploadSessionLookupError.incorrectOffsetValue;
                if (uploadSessionOffsetError != uploadSessionOffsetError2 && !uploadSessionOffsetError.equals(uploadSessionOffsetError2)) {
                    z = false;
                }
                return z;
            case CLOSED:
                return true;
            case NOT_CLOSED:
                return true;
            case TOO_LARGE:
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
