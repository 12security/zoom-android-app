package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.DownloadZipError */
public final class DownloadZipError {
    public static final DownloadZipError OTHER = new DownloadZipError().withTag(Tag.OTHER);
    public static final DownloadZipError TOO_LARGE = new DownloadZipError().withTag(Tag.TOO_LARGE);
    public static final DownloadZipError TOO_MANY_FILES = new DownloadZipError().withTag(Tag.TOO_MANY_FILES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.DownloadZipError$Serializer */
    static class Serializer extends UnionSerializer<DownloadZipError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DownloadZipError downloadZipError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (downloadZipError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(downloadZipError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TOO_LARGE:
                    jsonGenerator.writeString("too_large");
                    return;
                case TOO_MANY_FILES:
                    jsonGenerator.writeString("too_many_files");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DownloadZipError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DownloadZipError downloadZipError;
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
                    downloadZipError = DownloadZipError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("too_large".equals(str)) {
                    downloadZipError = DownloadZipError.TOO_LARGE;
                } else if ("too_many_files".equals(str)) {
                    downloadZipError = DownloadZipError.TOO_MANY_FILES;
                } else {
                    downloadZipError = DownloadZipError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return downloadZipError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DownloadZipError$Tag */
    public enum Tag {
        PATH,
        TOO_LARGE,
        TOO_MANY_FILES,
        OTHER
    }

    private DownloadZipError() {
    }

    private DownloadZipError withTag(Tag tag) {
        DownloadZipError downloadZipError = new DownloadZipError();
        downloadZipError._tag = tag;
        return downloadZipError;
    }

    private DownloadZipError withTagAndPath(Tag tag, LookupError lookupError) {
        DownloadZipError downloadZipError = new DownloadZipError();
        downloadZipError._tag = tag;
        downloadZipError.pathValue = lookupError;
        return downloadZipError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static DownloadZipError path(LookupError lookupError) {
        if (lookupError != null) {
            return new DownloadZipError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isTooLarge() {
        return this._tag == Tag.TOO_LARGE;
    }

    public boolean isTooManyFiles() {
        return this._tag == Tag.TOO_MANY_FILES;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof DownloadZipError)) {
            return false;
        }
        DownloadZipError downloadZipError = (DownloadZipError) obj;
        if (this._tag != downloadZipError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = downloadZipError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case TOO_LARGE:
                return true;
            case TOO_MANY_FILES:
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
