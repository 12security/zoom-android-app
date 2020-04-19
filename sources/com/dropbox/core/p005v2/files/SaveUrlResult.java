package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.SaveUrlResult */
public final class SaveUrlResult {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public FileMetadata completeValue;

    /* renamed from: com.dropbox.core.v2.files.SaveUrlResult$Serializer */
    static class Serializer extends UnionSerializer<SaveUrlResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SaveUrlResult saveUrlResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (saveUrlResult.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(saveUrlResult.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(saveUrlResult.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(saveUrlResult.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SaveUrlResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            SaveUrlResult saveUrlResult;
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
                if ("async_job_id".equals(str)) {
                    expectField("async_job_id", jsonParser);
                    saveUrlResult = SaveUrlResult.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    saveUrlResult = SaveUrlResult.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return saveUrlResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.SaveUrlResult$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE
    }

    private SaveUrlResult() {
    }

    private SaveUrlResult withTag(Tag tag) {
        SaveUrlResult saveUrlResult = new SaveUrlResult();
        saveUrlResult._tag = tag;
        return saveUrlResult;
    }

    private SaveUrlResult withTagAndAsyncJobId(Tag tag, String str) {
        SaveUrlResult saveUrlResult = new SaveUrlResult();
        saveUrlResult._tag = tag;
        saveUrlResult.asyncJobIdValue = str;
        return saveUrlResult;
    }

    private SaveUrlResult withTagAndComplete(Tag tag, FileMetadata fileMetadata) {
        SaveUrlResult saveUrlResult = new SaveUrlResult();
        saveUrlResult._tag = tag;
        saveUrlResult.completeValue = fileMetadata;
        return saveUrlResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static SaveUrlResult asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new SaveUrlResult().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
        } else {
            throw new IllegalArgumentException("String is shorter than 1");
        }
    }

    public String getAsyncJobIdValue() {
        if (this._tag == Tag.ASYNC_JOB_ID) {
            return this.asyncJobIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ASYNC_JOB_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isComplete() {
        return this._tag == Tag.COMPLETE;
    }

    public static SaveUrlResult complete(FileMetadata fileMetadata) {
        if (fileMetadata != null) {
            return new SaveUrlResult().withTagAndComplete(Tag.COMPLETE, fileMetadata);
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

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof SaveUrlResult)) {
            return false;
        }
        SaveUrlResult saveUrlResult = (SaveUrlResult) obj;
        if (this._tag != saveUrlResult._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = saveUrlResult.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                FileMetadata fileMetadata = this.completeValue;
                FileMetadata fileMetadata2 = saveUrlResult.completeValue;
                if (fileMetadata != fileMetadata2 && !fileMetadata.equals(fileMetadata2)) {
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
