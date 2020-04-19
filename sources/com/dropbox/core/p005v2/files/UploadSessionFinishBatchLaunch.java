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

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchLaunch */
public final class UploadSessionFinishBatchLaunch {
    public static final UploadSessionFinishBatchLaunch OTHER = new UploadSessionFinishBatchLaunch().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public UploadSessionFinishBatchResult completeValue;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchLaunch$Serializer */
    static class Serializer extends UnionSerializer<UploadSessionFinishBatchLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionFinishBatchLaunch uploadSessionFinishBatchLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadSessionFinishBatchLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(uploadSessionFinishBatchLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(uploadSessionFinishBatchLaunch.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UploadSessionFinishBatchLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UploadSessionFinishBatchLaunch uploadSessionFinishBatchLaunch;
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
                    uploadSessionFinishBatchLaunch = UploadSessionFinishBatchLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    uploadSessionFinishBatchLaunch = UploadSessionFinishBatchLaunch.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    uploadSessionFinishBatchLaunch = UploadSessionFinishBatchLaunch.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return uploadSessionFinishBatchLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE,
        OTHER
    }

    private UploadSessionFinishBatchLaunch() {
    }

    private UploadSessionFinishBatchLaunch withTag(Tag tag) {
        UploadSessionFinishBatchLaunch uploadSessionFinishBatchLaunch = new UploadSessionFinishBatchLaunch();
        uploadSessionFinishBatchLaunch._tag = tag;
        return uploadSessionFinishBatchLaunch;
    }

    private UploadSessionFinishBatchLaunch withTagAndAsyncJobId(Tag tag, String str) {
        UploadSessionFinishBatchLaunch uploadSessionFinishBatchLaunch = new UploadSessionFinishBatchLaunch();
        uploadSessionFinishBatchLaunch._tag = tag;
        uploadSessionFinishBatchLaunch.asyncJobIdValue = str;
        return uploadSessionFinishBatchLaunch;
    }

    private UploadSessionFinishBatchLaunch withTagAndComplete(Tag tag, UploadSessionFinishBatchResult uploadSessionFinishBatchResult) {
        UploadSessionFinishBatchLaunch uploadSessionFinishBatchLaunch = new UploadSessionFinishBatchLaunch();
        uploadSessionFinishBatchLaunch._tag = tag;
        uploadSessionFinishBatchLaunch.completeValue = uploadSessionFinishBatchResult;
        return uploadSessionFinishBatchLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static UploadSessionFinishBatchLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new UploadSessionFinishBatchLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public static UploadSessionFinishBatchLaunch complete(UploadSessionFinishBatchResult uploadSessionFinishBatchResult) {
        if (uploadSessionFinishBatchResult != null) {
            return new UploadSessionFinishBatchLaunch().withTagAndComplete(Tag.COMPLETE, uploadSessionFinishBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadSessionFinishBatchResult getCompleteValue() {
        if (this._tag == Tag.COMPLETE) {
            return this.completeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.COMPLETE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadSessionFinishBatchLaunch)) {
            return false;
        }
        UploadSessionFinishBatchLaunch uploadSessionFinishBatchLaunch = (UploadSessionFinishBatchLaunch) obj;
        if (this._tag != uploadSessionFinishBatchLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = uploadSessionFinishBatchLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                UploadSessionFinishBatchResult uploadSessionFinishBatchResult = this.completeValue;
                UploadSessionFinishBatchResult uploadSessionFinishBatchResult2 = uploadSessionFinishBatchLaunch.completeValue;
                if (uploadSessionFinishBatchResult != uploadSessionFinishBatchResult2 && !uploadSessionFinishBatchResult.equals(uploadSessionFinishBatchResult2)) {
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
