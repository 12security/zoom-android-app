package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.SaveUrlError */
public final class SaveUrlError {
    public static final SaveUrlError DOWNLOAD_FAILED = new SaveUrlError().withTag(Tag.DOWNLOAD_FAILED);
    public static final SaveUrlError INVALID_URL = new SaveUrlError().withTag(Tag.INVALID_URL);
    public static final SaveUrlError NOT_FOUND = new SaveUrlError().withTag(Tag.NOT_FOUND);
    public static final SaveUrlError OTHER = new SaveUrlError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public WriteError pathValue;

    /* renamed from: com.dropbox.core.v2.files.SaveUrlError$Serializer */
    static class Serializer extends UnionSerializer<SaveUrlError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SaveUrlError saveUrlError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (saveUrlError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    Serializer.INSTANCE.serialize(saveUrlError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case DOWNLOAD_FAILED:
                    jsonGenerator.writeString("download_failed");
                    return;
                case INVALID_URL:
                    jsonGenerator.writeString("invalid_url");
                    return;
                case NOT_FOUND:
                    jsonGenerator.writeString("not_found");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SaveUrlError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SaveUrlError saveUrlError;
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
                    saveUrlError = SaveUrlError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("download_failed".equals(str)) {
                    saveUrlError = SaveUrlError.DOWNLOAD_FAILED;
                } else if ("invalid_url".equals(str)) {
                    saveUrlError = SaveUrlError.INVALID_URL;
                } else if ("not_found".equals(str)) {
                    saveUrlError = SaveUrlError.NOT_FOUND;
                } else {
                    saveUrlError = SaveUrlError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return saveUrlError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.SaveUrlError$Tag */
    public enum Tag {
        PATH,
        DOWNLOAD_FAILED,
        INVALID_URL,
        NOT_FOUND,
        OTHER
    }

    private SaveUrlError() {
    }

    private SaveUrlError withTag(Tag tag) {
        SaveUrlError saveUrlError = new SaveUrlError();
        saveUrlError._tag = tag;
        return saveUrlError;
    }

    private SaveUrlError withTagAndPath(Tag tag, WriteError writeError) {
        SaveUrlError saveUrlError = new SaveUrlError();
        saveUrlError._tag = tag;
        saveUrlError.pathValue = writeError;
        return saveUrlError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static SaveUrlError path(WriteError writeError) {
        if (writeError != null) {
            return new SaveUrlError().withTagAndPath(Tag.PATH, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getPathValue() {
        if (this._tag == Tag.PATH) {
            return this.pathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isDownloadFailed() {
        return this._tag == Tag.DOWNLOAD_FAILED;
    }

    public boolean isInvalidUrl() {
        return this._tag == Tag.INVALID_URL;
    }

    public boolean isNotFound() {
        return this._tag == Tag.NOT_FOUND;
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
        if (obj == null || !(obj instanceof SaveUrlError)) {
            return false;
        }
        SaveUrlError saveUrlError = (SaveUrlError) obj;
        if (this._tag != saveUrlError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                WriteError writeError = this.pathValue;
                WriteError writeError2 = saveUrlError.pathValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case DOWNLOAD_FAILED:
                return true;
            case INVALID_URL:
                return true;
            case NOT_FOUND:
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
