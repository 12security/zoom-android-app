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

/* renamed from: com.dropbox.core.v2.team.MembersAddLaunch */
public final class MembersAddLaunch {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public List<MemberAddResult> completeValue;

    /* renamed from: com.dropbox.core.v2.team.MembersAddLaunch$Serializer */
    static class Serializer extends UnionSerializer<MembersAddLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersAddLaunch membersAddLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersAddLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(membersAddLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    jsonGenerator.writeFieldName("complete");
                    StoneSerializers.list(Serializer.INSTANCE).serialize(membersAddLaunch.completeValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersAddLaunch.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersAddLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersAddLaunch membersAddLaunch;
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
                    membersAddLaunch = MembersAddLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    expectField("complete", jsonParser);
                    membersAddLaunch = MembersAddLaunch.complete((List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser));
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
                return membersAddLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersAddLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE
    }

    private MembersAddLaunch() {
    }

    private MembersAddLaunch withTag(Tag tag) {
        MembersAddLaunch membersAddLaunch = new MembersAddLaunch();
        membersAddLaunch._tag = tag;
        return membersAddLaunch;
    }

    private MembersAddLaunch withTagAndAsyncJobId(Tag tag, String str) {
        MembersAddLaunch membersAddLaunch = new MembersAddLaunch();
        membersAddLaunch._tag = tag;
        membersAddLaunch.asyncJobIdValue = str;
        return membersAddLaunch;
    }

    private MembersAddLaunch withTagAndComplete(Tag tag, List<MemberAddResult> list) {
        MembersAddLaunch membersAddLaunch = new MembersAddLaunch();
        membersAddLaunch._tag = tag;
        membersAddLaunch.completeValue = list;
        return membersAddLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static MembersAddLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new MembersAddLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public static MembersAddLaunch complete(List<MemberAddResult> list) {
        if (list != null) {
            for (MemberAddResult memberAddResult : list) {
                if (memberAddResult == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new MembersAddLaunch().withTagAndComplete(Tag.COMPLETE, list);
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

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof MembersAddLaunch)) {
            return false;
        }
        MembersAddLaunch membersAddLaunch = (MembersAddLaunch) obj;
        if (this._tag != membersAddLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = membersAddLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                List<MemberAddResult> list = this.completeValue;
                List<MemberAddResult> list2 = membersAddLaunch.completeValue;
                if (list != list2 && !list.equals(list2)) {
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
