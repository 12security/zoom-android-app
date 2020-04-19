package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.ThumbnailError */
public final class ThumbnailError {
    public static final ThumbnailError CONVERSION_ERROR = new ThumbnailError().withTag(Tag.CONVERSION_ERROR);
    public static final ThumbnailError UNSUPPORTED_EXTENSION = new ThumbnailError().withTag(Tag.UNSUPPORTED_EXTENSION);
    public static final ThumbnailError UNSUPPORTED_IMAGE = new ThumbnailError().withTag(Tag.UNSUPPORTED_IMAGE);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.ThumbnailError$Serializer */
    static class Serializer extends UnionSerializer<ThumbnailError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ThumbnailError thumbnailError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (thumbnailError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(thumbnailError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case UNSUPPORTED_EXTENSION:
                    jsonGenerator.writeString("unsupported_extension");
                    return;
                case UNSUPPORTED_IMAGE:
                    jsonGenerator.writeString("unsupported_image");
                    return;
                case CONVERSION_ERROR:
                    jsonGenerator.writeString("conversion_error");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(thumbnailError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ThumbnailError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ThumbnailError thumbnailError;
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
                if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    thumbnailError = ThumbnailError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("unsupported_extension".equals(str)) {
                    thumbnailError = ThumbnailError.UNSUPPORTED_EXTENSION;
                } else if ("unsupported_image".equals(str)) {
                    thumbnailError = ThumbnailError.UNSUPPORTED_IMAGE;
                } else if ("conversion_error".equals(str)) {
                    thumbnailError = ThumbnailError.CONVERSION_ERROR;
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
                return thumbnailError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ThumbnailError$Tag */
    public enum Tag {
        PATH,
        UNSUPPORTED_EXTENSION,
        UNSUPPORTED_IMAGE,
        CONVERSION_ERROR
    }

    private ThumbnailError() {
    }

    private ThumbnailError withTag(Tag tag) {
        ThumbnailError thumbnailError = new ThumbnailError();
        thumbnailError._tag = tag;
        return thumbnailError;
    }

    private ThumbnailError withTagAndPath(Tag tag, LookupError lookupError) {
        ThumbnailError thumbnailError = new ThumbnailError();
        thumbnailError._tag = tag;
        thumbnailError.pathValue = lookupError;
        return thumbnailError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static ThumbnailError path(LookupError lookupError) {
        if (lookupError != null) {
            return new ThumbnailError().withTagAndPath(Tag.PATH, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getPathValue() {
        if (this._tag == Tag.PATH) {
            return this.pathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUnsupportedExtension() {
        return this._tag == Tag.UNSUPPORTED_EXTENSION;
    }

    public boolean isUnsupportedImage() {
        return this._tag == Tag.UNSUPPORTED_IMAGE;
    }

    public boolean isConversionError() {
        return this._tag == Tag.CONVERSION_ERROR;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ThumbnailError)) {
            return false;
        }
        ThumbnailError thumbnailError = (ThumbnailError) obj;
        if (this._tag != thumbnailError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = thumbnailError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case UNSUPPORTED_EXTENSION:
                return true;
            case UNSUPPORTED_IMAGE:
                return true;
            case CONVERSION_ERROR:
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
