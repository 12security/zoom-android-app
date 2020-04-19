package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.SaveUrlJobStatus */
public final class SaveUrlJobStatus {
    public static final SaveUrlJobStatus IN_PROGRESS = new SaveUrlJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public FileMetadata completeValue;
    /* access modifiers changed from: private */
    public SaveUrlError failedValue;

    /* renamed from: com.dropbox.core.v2.files.SaveUrlJobStatus$Serializer */
    static class Serializer extends UnionSerializer<SaveUrlJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SaveUrlJobStatus saveUrlJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (saveUrlJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(saveUrlJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(saveUrlJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(saveUrlJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SaveUrlJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            SaveUrlJobStatus saveUrlJobStatus;
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
                if ("in_progress".equals(str)) {
                    saveUrlJobStatus = SaveUrlJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    saveUrlJobStatus = SaveUrlJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    saveUrlJobStatus = SaveUrlJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
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
                return saveUrlJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.SaveUrlJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private SaveUrlJobStatus() {
    }

    private SaveUrlJobStatus withTag(Tag tag) {
        SaveUrlJobStatus saveUrlJobStatus = new SaveUrlJobStatus();
        saveUrlJobStatus._tag = tag;
        return saveUrlJobStatus;
    }

    private SaveUrlJobStatus withTagAndComplete(Tag tag, FileMetadata fileMetadata) {
        SaveUrlJobStatus saveUrlJobStatus = new SaveUrlJobStatus();
        saveUrlJobStatus._tag = tag;
        saveUrlJobStatus.completeValue = fileMetadata;
        return saveUrlJobStatus;
    }

    private SaveUrlJobStatus withTagAndFailed(Tag tag, SaveUrlError saveUrlError) {
        SaveUrlJobStatus saveUrlJobStatus = new SaveUrlJobStatus();
        saveUrlJobStatus._tag = tag;
        saveUrlJobStatus.failedValue = saveUrlError;
        return saveUrlJobStatus;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInProgress() {
        return this._tag == Tag.IN_PROGRESS;
    }

    public boolean isComplete() {
        return this._tag == Tag.COMPLETE;
    }

    public static SaveUrlJobStatus complete(FileMetadata fileMetadata) {
        if (fileMetadata != null) {
            return new SaveUrlJobStatus().withTagAndComplete(Tag.COMPLETE, fileMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public FileMetadata getCompleteValue() {
        if (this._tag == Tag.COMPLETE) {
            return this.completeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.COMPLETE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFailed() {
        return this._tag == Tag.FAILED;
    }

    public static SaveUrlJobStatus failed(SaveUrlError saveUrlError) {
        if (saveUrlError != null) {
            return new SaveUrlJobStatus().withTagAndFailed(Tag.FAILED, saveUrlError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SaveUrlError getFailedValue() {
        if (this._tag == Tag.FAILED) {
            return this.failedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.completeValue, this.failedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof SaveUrlJobStatus)) {
            return false;
        }
        SaveUrlJobStatus saveUrlJobStatus = (SaveUrlJobStatus) obj;
        if (this._tag != saveUrlJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                FileMetadata fileMetadata = this.completeValue;
                FileMetadata fileMetadata2 = saveUrlJobStatus.completeValue;
                if (fileMetadata != fileMetadata2 && !fileMetadata.equals(fileMetadata2)) {
                    z = false;
                }
                return z;
            case FAILED:
                SaveUrlError saveUrlError = this.failedValue;
                SaveUrlError saveUrlError2 = saveUrlJobStatus.failedValue;
                if (saveUrlError != saveUrlError2 && !saveUrlError.equals(saveUrlError2)) {
                    z = false;
                }
                return z;
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
