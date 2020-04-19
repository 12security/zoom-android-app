package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.PreviewError */
public final class PreviewError {
    public static final PreviewError IN_PROGRESS = new PreviewError().withTag(Tag.IN_PROGRESS);
    public static final PreviewError UNSUPPORTED_CONTENT = new PreviewError().withTag(Tag.UNSUPPORTED_CONTENT);
    public static final PreviewError UNSUPPORTED_EXTENSION = new PreviewError().withTag(Tag.UNSUPPORTED_EXTENSION);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.PreviewError$Serializer */
    static class Serializer extends UnionSerializer<PreviewError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PreviewError previewError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (previewError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(previewError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case UNSUPPORTED_EXTENSION:
                    jsonGenerator.writeString("unsupported_extension");
                    return;
                case UNSUPPORTED_CONTENT:
                    jsonGenerator.writeString("unsupported_content");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(previewError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public PreviewError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PreviewError previewError;
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
                    previewError = PreviewError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("in_progress".equals(str)) {
                    previewError = PreviewError.IN_PROGRESS;
                } else if ("unsupported_extension".equals(str)) {
                    previewError = PreviewError.UNSUPPORTED_EXTENSION;
                } else if ("unsupported_content".equals(str)) {
                    previewError = PreviewError.UNSUPPORTED_CONTENT;
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
                return previewError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.PreviewError$Tag */
    public enum Tag {
        PATH,
        IN_PROGRESS,
        UNSUPPORTED_EXTENSION,
        UNSUPPORTED_CONTENT
    }

    private PreviewError() {
    }

    private PreviewError withTag(Tag tag) {
        PreviewError previewError = new PreviewError();
        previewError._tag = tag;
        return previewError;
    }

    private PreviewError withTagAndPath(Tag tag, LookupError lookupError) {
        PreviewError previewError = new PreviewError();
        previewError._tag = tag;
        previewError.pathValue = lookupError;
        return previewError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static PreviewError path(LookupError lookupError) {
        if (lookupError != null) {
            return new PreviewError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isInProgress() {
        return this._tag == Tag.IN_PROGRESS;
    }

    public boolean isUnsupportedExtension() {
        return this._tag == Tag.UNSUPPORTED_EXTENSION;
    }

    public boolean isUnsupportedContent() {
        return this._tag == Tag.UNSUPPORTED_CONTENT;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof PreviewError)) {
            return false;
        }
        PreviewError previewError = (PreviewError) obj;
        if (this._tag != previewError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = previewError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case IN_PROGRESS:
                return true;
            case UNSUPPORTED_EXTENSION:
                return true;
            case UNSUPPORTED_CONTENT:
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
