package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.JobStatus */
public final class JobStatus {
    public static final JobStatus COMPLETE = new JobStatus().withTag(Tag.COMPLETE);
    public static final JobStatus IN_PROGRESS = new JobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public JobError failedValue;

    /* renamed from: com.dropbox.core.v2.sharing.JobStatus$Serializer */
    static class Serializer extends UnionSerializer<JobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(JobStatus jobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (jobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeString("complete");
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(jobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(jobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public JobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            JobStatus jobStatus;
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
                if ("in_progress".equals(str)) {
                    jobStatus = JobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    jobStatus = JobStatus.COMPLETE;
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    jobStatus = JobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
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
                return jobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.JobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private JobStatus() {
    }

    private JobStatus withTag(Tag tag) {
        JobStatus jobStatus = new JobStatus();
        jobStatus._tag = tag;
        return jobStatus;
    }

    private JobStatus withTagAndFailed(Tag tag, JobError jobError) {
        JobStatus jobStatus = new JobStatus();
        jobStatus._tag = tag;
        jobStatus.failedValue = jobError;
        return jobStatus;
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

    public boolean isFailed() {
        return this._tag == Tag.FAILED;
    }

    public static JobStatus failed(JobError jobError) {
        if (jobError != null) {
            return new JobStatus().withTagAndFailed(Tag.FAILED, jobError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public JobError getFailedValue() {
        if (this._tag == Tag.FAILED) {
            return this.failedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.failedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof JobStatus)) {
            return false;
        }
        JobStatus jobStatus = (JobStatus) obj;
        if (this._tag != jobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                return true;
            case FAILED:
                JobError jobError = this.failedValue;
                JobError jobError2 = jobStatus.failedValue;
                if (jobError != jobError2 && !jobError.equals(jobError2)) {
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
