package com.dropbox.core.p005v2.async;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.async.LaunchEmptyResult */
public final class LaunchEmptyResult {
    public static final LaunchEmptyResult COMPLETE = new LaunchEmptyResult().withTag(Tag.COMPLETE);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;

    /* renamed from: com.dropbox.core.v2.async.LaunchEmptyResult$Serializer */
    public static class Serializer extends UnionSerializer<LaunchEmptyResult> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(LaunchEmptyResult launchEmptyResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (launchEmptyResult.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(launchEmptyResult.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeString("complete");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(launchEmptyResult.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public LaunchEmptyResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LaunchEmptyResult launchEmptyResult;
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
                if ("async_job_id".equals(str)) {
                    expectField("async_job_id", jsonParser);
                    launchEmptyResult = LaunchEmptyResult.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    launchEmptyResult = LaunchEmptyResult.COMPLETE;
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
                return launchEmptyResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.async.LaunchEmptyResult$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE
    }

    private LaunchEmptyResult() {
    }

    private LaunchEmptyResult withTag(Tag tag) {
        LaunchEmptyResult launchEmptyResult = new LaunchEmptyResult();
        launchEmptyResult._tag = tag;
        return launchEmptyResult;
    }

    private LaunchEmptyResult withTagAndAsyncJobId(Tag tag, String str) {
        LaunchEmptyResult launchEmptyResult = new LaunchEmptyResult();
        launchEmptyResult._tag = tag;
        launchEmptyResult.asyncJobIdValue = str;
        return launchEmptyResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static LaunchEmptyResult asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new LaunchEmptyResult().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof LaunchEmptyResult)) {
            return false;
        }
        LaunchEmptyResult launchEmptyResult = (LaunchEmptyResult) obj;
        if (this._tag != launchEmptyResult._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = launchEmptyResult.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
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
