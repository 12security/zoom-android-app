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

/* renamed from: com.dropbox.core.v2.files.DeleteBatchLaunch */
public final class DeleteBatchLaunch {
    public static final DeleteBatchLaunch OTHER = new DeleteBatchLaunch().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public DeleteBatchResult completeValue;

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchLaunch$Serializer */
    static class Serializer extends UnionSerializer<DeleteBatchLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeleteBatchLaunch deleteBatchLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deleteBatchLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(deleteBatchLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(deleteBatchLaunch.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DeleteBatchLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            DeleteBatchLaunch deleteBatchLaunch;
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
                    deleteBatchLaunch = DeleteBatchLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    deleteBatchLaunch = DeleteBatchLaunch.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    deleteBatchLaunch = DeleteBatchLaunch.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deleteBatchLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE,
        OTHER
    }

    private DeleteBatchLaunch() {
    }

    private DeleteBatchLaunch withTag(Tag tag) {
        DeleteBatchLaunch deleteBatchLaunch = new DeleteBatchLaunch();
        deleteBatchLaunch._tag = tag;
        return deleteBatchLaunch;
    }

    private DeleteBatchLaunch withTagAndAsyncJobId(Tag tag, String str) {
        DeleteBatchLaunch deleteBatchLaunch = new DeleteBatchLaunch();
        deleteBatchLaunch._tag = tag;
        deleteBatchLaunch.asyncJobIdValue = str;
        return deleteBatchLaunch;
    }

    private DeleteBatchLaunch withTagAndComplete(Tag tag, DeleteBatchResult deleteBatchResult) {
        DeleteBatchLaunch deleteBatchLaunch = new DeleteBatchLaunch();
        deleteBatchLaunch._tag = tag;
        deleteBatchLaunch.completeValue = deleteBatchResult;
        return deleteBatchLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static DeleteBatchLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new DeleteBatchLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public static DeleteBatchLaunch complete(DeleteBatchResult deleteBatchResult) {
        if (deleteBatchResult != null) {
            return new DeleteBatchLaunch().withTagAndComplete(Tag.COMPLETE, deleteBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeleteBatchResult getCompleteValue() {
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
        if (obj == null || !(obj instanceof DeleteBatchLaunch)) {
            return false;
        }
        DeleteBatchLaunch deleteBatchLaunch = (DeleteBatchLaunch) obj;
        if (this._tag != deleteBatchLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = deleteBatchLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                DeleteBatchResult deleteBatchResult = this.completeValue;
                DeleteBatchResult deleteBatchResult2 = deleteBatchLaunch.completeValue;
                if (deleteBatchResult != deleteBatchResult2 && !deleteBatchResult.equals(deleteBatchResult2)) {
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
