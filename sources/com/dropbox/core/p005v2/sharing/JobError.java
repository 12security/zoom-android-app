package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.JobError */
public final class JobError {
    public static final JobError OTHER = new JobError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public RelinquishFolderMembershipError relinquishFolderMembershipErrorValue;
    /* access modifiers changed from: private */
    public RemoveFolderMemberError removeFolderMemberErrorValue;
    /* access modifiers changed from: private */
    public UnshareFolderError unshareFolderErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.JobError$Serializer */
    static class Serializer extends UnionSerializer<JobError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(JobError jobError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (jobError.tag()) {
                case UNSHARE_FOLDER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("unshare_folder_error", jsonGenerator);
                    jsonGenerator.writeFieldName("unshare_folder_error");
                    Serializer.INSTANCE.serialize(jobError.unshareFolderErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case REMOVE_FOLDER_MEMBER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("remove_folder_member_error", jsonGenerator);
                    jsonGenerator.writeFieldName("remove_folder_member_error");
                    Serializer.INSTANCE.serialize(jobError.removeFolderMemberErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RELINQUISH_FOLDER_MEMBERSHIP_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("relinquish_folder_membership_error", jsonGenerator);
                    jsonGenerator.writeFieldName("relinquish_folder_membership_error");
                    Serializer.INSTANCE.serialize(jobError.relinquishFolderMembershipErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public JobError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            JobError jobError;
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
                if ("unshare_folder_error".equals(str)) {
                    expectField("unshare_folder_error", jsonParser);
                    jobError = JobError.unshareFolderError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("remove_folder_member_error".equals(str)) {
                    expectField("remove_folder_member_error", jsonParser);
                    jobError = JobError.removeFolderMemberError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("relinquish_folder_membership_error".equals(str)) {
                    expectField("relinquish_folder_membership_error", jsonParser);
                    jobError = JobError.relinquishFolderMembershipError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    jobError = JobError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return jobError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.JobError$Tag */
    public enum Tag {
        UNSHARE_FOLDER_ERROR,
        REMOVE_FOLDER_MEMBER_ERROR,
        RELINQUISH_FOLDER_MEMBERSHIP_ERROR,
        OTHER
    }

    private JobError() {
    }

    private JobError withTag(Tag tag) {
        JobError jobError = new JobError();
        jobError._tag = tag;
        return jobError;
    }

    private JobError withTagAndUnshareFolderError(Tag tag, UnshareFolderError unshareFolderError) {
        JobError jobError = new JobError();
        jobError._tag = tag;
        jobError.unshareFolderErrorValue = unshareFolderError;
        return jobError;
    }

    private JobError withTagAndRemoveFolderMemberError(Tag tag, RemoveFolderMemberError removeFolderMemberError) {
        JobError jobError = new JobError();
        jobError._tag = tag;
        jobError.removeFolderMemberErrorValue = removeFolderMemberError;
        return jobError;
    }

    private JobError withTagAndRelinquishFolderMembershipError(Tag tag, RelinquishFolderMembershipError relinquishFolderMembershipError) {
        JobError jobError = new JobError();
        jobError._tag = tag;
        jobError.relinquishFolderMembershipErrorValue = relinquishFolderMembershipError;
        return jobError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUnshareFolderError() {
        return this._tag == Tag.UNSHARE_FOLDER_ERROR;
    }

    public static JobError unshareFolderError(UnshareFolderError unshareFolderError) {
        if (unshareFolderError != null) {
            return new JobError().withTagAndUnshareFolderError(Tag.UNSHARE_FOLDER_ERROR, unshareFolderError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UnshareFolderError getUnshareFolderErrorValue() {
        if (this._tag == Tag.UNSHARE_FOLDER_ERROR) {
            return this.unshareFolderErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.UNSHARE_FOLDER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isRemoveFolderMemberError() {
        return this._tag == Tag.REMOVE_FOLDER_MEMBER_ERROR;
    }

    public static JobError removeFolderMemberError(RemoveFolderMemberError removeFolderMemberError) {
        if (removeFolderMemberError != null) {
            return new JobError().withTagAndRemoveFolderMemberError(Tag.REMOVE_FOLDER_MEMBER_ERROR, removeFolderMemberError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RemoveFolderMemberError getRemoveFolderMemberErrorValue() {
        if (this._tag == Tag.REMOVE_FOLDER_MEMBER_ERROR) {
            return this.removeFolderMemberErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.REMOVE_FOLDER_MEMBER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isRelinquishFolderMembershipError() {
        return this._tag == Tag.RELINQUISH_FOLDER_MEMBERSHIP_ERROR;
    }

    public static JobError relinquishFolderMembershipError(RelinquishFolderMembershipError relinquishFolderMembershipError) {
        if (relinquishFolderMembershipError != null) {
            return new JobError().withTagAndRelinquishFolderMembershipError(Tag.RELINQUISH_FOLDER_MEMBERSHIP_ERROR, relinquishFolderMembershipError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RelinquishFolderMembershipError getRelinquishFolderMembershipErrorValue() {
        if (this._tag == Tag.RELINQUISH_FOLDER_MEMBERSHIP_ERROR) {
            return this.relinquishFolderMembershipErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.RELINQUISH_FOLDER_MEMBERSHIP_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.unshareFolderErrorValue, this.removeFolderMemberErrorValue, this.relinquishFolderMembershipErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof JobError)) {
            return false;
        }
        JobError jobError = (JobError) obj;
        if (this._tag != jobError._tag) {
            return false;
        }
        switch (this._tag) {
            case UNSHARE_FOLDER_ERROR:
                UnshareFolderError unshareFolderError = this.unshareFolderErrorValue;
                UnshareFolderError unshareFolderError2 = jobError.unshareFolderErrorValue;
                if (unshareFolderError != unshareFolderError2 && !unshareFolderError.equals(unshareFolderError2)) {
                    z = false;
                }
                return z;
            case REMOVE_FOLDER_MEMBER_ERROR:
                RemoveFolderMemberError removeFolderMemberError = this.removeFolderMemberErrorValue;
                RemoveFolderMemberError removeFolderMemberError2 = jobError.removeFolderMemberErrorValue;
                if (removeFolderMemberError != removeFolderMemberError2 && !removeFolderMemberError.equals(removeFolderMemberError2)) {
                    z = false;
                }
                return z;
            case RELINQUISH_FOLDER_MEMBERSHIP_ERROR:
                RelinquishFolderMembershipError relinquishFolderMembershipError = this.relinquishFolderMembershipErrorValue;
                RelinquishFolderMembershipError relinquishFolderMembershipError2 = jobError.relinquishFolderMembershipErrorValue;
                if (relinquishFolderMembershipError != relinquishFolderMembershipError2 && !relinquishFolderMembershipError.equals(relinquishFolderMembershipError2)) {
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
