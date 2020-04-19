package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.MembersAddJobStatus */
public final class MembersAddJobStatus {
    public static final MembersAddJobStatus IN_PROGRESS = new MembersAddJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public List<MemberAddResult> completeValue;
    /* access modifiers changed from: private */
    public String failedValue;

    /* renamed from: com.dropbox.core.v2.team.MembersAddJobStatus$Serializer */
    static class Serializer extends UnionSerializer<MembersAddJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersAddJobStatus membersAddJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersAddJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    jsonGenerator.writeFieldName("complete");
                    StoneSerializers.list(Serializer.INSTANCE).serialize(membersAddJobStatus.completeValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    StoneSerializers.string().serialize(membersAddJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersAddJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersAddJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersAddJobStatus membersAddJobStatus;
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
                    membersAddJobStatus = MembersAddJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    expectField("complete", jsonParser);
                    membersAddJobStatus = MembersAddJobStatus.complete((List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    membersAddJobStatus = MembersAddJobStatus.failed((String) StoneSerializers.string().deserialize(jsonParser));
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
                return membersAddJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersAddJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private MembersAddJobStatus() {
    }

    private MembersAddJobStatus withTag(Tag tag) {
        MembersAddJobStatus membersAddJobStatus = new MembersAddJobStatus();
        membersAddJobStatus._tag = tag;
        return membersAddJobStatus;
    }

    private MembersAddJobStatus withTagAndComplete(Tag tag, List<MemberAddResult> list) {
        MembersAddJobStatus membersAddJobStatus = new MembersAddJobStatus();
        membersAddJobStatus._tag = tag;
        membersAddJobStatus.completeValue = list;
        return membersAddJobStatus;
    }

    private MembersAddJobStatus withTagAndFailed(Tag tag, String str) {
        MembersAddJobStatus membersAddJobStatus = new MembersAddJobStatus();
        membersAddJobStatus._tag = tag;
        membersAddJobStatus.failedValue = str;
        return membersAddJobStatus;
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

    public static MembersAddJobStatus complete(List<MemberAddResult> list) {
        if (list != null) {
            for (MemberAddResult memberAddResult : list) {
                if (memberAddResult == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new MembersAddJobStatus().withTagAndComplete(Tag.COMPLETE, list);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public List<MemberAddResult> getCompleteValue() {
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

    public static MembersAddJobStatus failed(String str) {
        if (str != null) {
            return new MembersAddJobStatus().withTagAndFailed(Tag.FAILED, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getFailedValue() {
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
        if (obj == null || !(obj instanceof MembersAddJobStatus)) {
            return false;
        }
        MembersAddJobStatus membersAddJobStatus = (MembersAddJobStatus) obj;
        if (this._tag != membersAddJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                List<MemberAddResult> list = this.completeValue;
                List<MemberAddResult> list2 = membersAddJobStatus.completeValue;
                if (list != list2 && !list.equals(list2)) {
                    z = false;
                }
                return z;
            case FAILED:
                String str = this.failedValue;
                String str2 = membersAddJobStatus.failedValue;
                if (str != str2 && !str.equals(str2)) {
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
