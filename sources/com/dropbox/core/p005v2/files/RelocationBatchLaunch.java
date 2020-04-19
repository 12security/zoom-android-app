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

/* renamed from: com.dropbox.core.v2.files.RelocationBatchLaunch */
public final class RelocationBatchLaunch {
    public static final RelocationBatchLaunch OTHER = new RelocationBatchLaunch().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public RelocationBatchResult completeValue;

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchLaunch$Serializer */
    static class Serializer extends UnionSerializer<RelocationBatchLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationBatchLaunch relocationBatchLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (relocationBatchLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(relocationBatchLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(relocationBatchLaunch.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RelocationBatchLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            RelocationBatchLaunch relocationBatchLaunch;
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
                    relocationBatchLaunch = RelocationBatchLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    relocationBatchLaunch = RelocationBatchLaunch.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    relocationBatchLaunch = RelocationBatchLaunch.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return relocationBatchLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE,
        OTHER
    }

    private RelocationBatchLaunch() {
    }

    private RelocationBatchLaunch withTag(Tag tag) {
        RelocationBatchLaunch relocationBatchLaunch = new RelocationBatchLaunch();
        relocationBatchLaunch._tag = tag;
        return relocationBatchLaunch;
    }

    private RelocationBatchLaunch withTagAndAsyncJobId(Tag tag, String str) {
        RelocationBatchLaunch relocationBatchLaunch = new RelocationBatchLaunch();
        relocationBatchLaunch._tag = tag;
        relocationBatchLaunch.asyncJobIdValue = str;
        return relocationBatchLaunch;
    }

    private RelocationBatchLaunch withTagAndComplete(Tag tag, RelocationBatchResult relocationBatchResult) {
        RelocationBatchLaunch relocationBatchLaunch = new RelocationBatchLaunch();
        relocationBatchLaunch._tag = tag;
        relocationBatchLaunch.completeValue = relocationBatchResult;
        return relocationBatchLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static RelocationBatchLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new RelocationBatchLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public static RelocationBatchLaunch complete(RelocationBatchResult relocationBatchResult) {
        if (relocationBatchResult != null) {
            return new RelocationBatchLaunch().withTagAndComplete(Tag.COMPLETE, relocationBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RelocationBatchResult getCompleteValue() {
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
        if (obj == null || !(obj instanceof RelocationBatchLaunch)) {
            return false;
        }
        RelocationBatchLaunch relocationBatchLaunch = (RelocationBatchLaunch) obj;
        if (this._tag != relocationBatchLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = relocationBatchLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                RelocationBatchResult relocationBatchResult = this.completeValue;
                RelocationBatchResult relocationBatchResult2 = relocationBatchLaunch.completeValue;
                if (relocationBatchResult != relocationBatchResult2 && !relocationBatchResult.equals(relocationBatchResult2)) {
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
