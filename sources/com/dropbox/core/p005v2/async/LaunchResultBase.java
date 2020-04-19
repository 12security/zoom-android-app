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

/* renamed from: com.dropbox.core.v2.async.LaunchResultBase */
public final class LaunchResultBase {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;

    /* renamed from: com.dropbox.core.v2.async.LaunchResultBase$1 */
    static /* synthetic */ class C06241 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$async$LaunchResultBase$Tag = new int[Tag.values().length];

        static {
            try {
                $SwitchMap$com$dropbox$core$v2$async$LaunchResultBase$Tag[Tag.ASYNC_JOB_ID.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.async.LaunchResultBase$Serializer */
    public static class Serializer extends UnionSerializer<LaunchResultBase> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(LaunchResultBase launchResultBase, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06241.$SwitchMap$com$dropbox$core$v2$async$LaunchResultBase$Tag[launchResultBase.tag().ordinal()] == 1) {
                jsonGenerator.writeStartObject();
                writeTag("async_job_id", jsonGenerator);
                jsonGenerator.writeFieldName("async_job_id");
                StoneSerializers.string().serialize(launchResultBase.asyncJobIdValue, jsonGenerator);
                jsonGenerator.writeEndObject();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized tag: ");
            sb.append(launchResultBase.tag());
            throw new IllegalArgumentException(sb.toString());
        }

        public LaunchResultBase deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str == null) {
                throw new JsonParseException(jsonParser, "Required field missing: .tag");
            } else if ("async_job_id".equals(str)) {
                expectField("async_job_id", jsonParser);
                LaunchResultBase asyncJobId = LaunchResultBase.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return asyncJobId;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown tag: ");
                sb.append(str);
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.async.LaunchResultBase$Tag */
    public enum Tag {
        ASYNC_JOB_ID
    }

    private LaunchResultBase() {
    }

    private LaunchResultBase withTag(Tag tag) {
        LaunchResultBase launchResultBase = new LaunchResultBase();
        launchResultBase._tag = tag;
        return launchResultBase;
    }

    private LaunchResultBase withTagAndAsyncJobId(Tag tag, String str) {
        LaunchResultBase launchResultBase = new LaunchResultBase();
        launchResultBase._tag = tag;
        launchResultBase.asyncJobIdValue = str;
        return launchResultBase;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static LaunchResultBase asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new LaunchResultBase().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof LaunchResultBase)) {
            return false;
        }
        LaunchResultBase launchResultBase = (LaunchResultBase) obj;
        if (this._tag != launchResultBase._tag || C06241.$SwitchMap$com$dropbox$core$v2$async$LaunchResultBase$Tag[this._tag.ordinal()] != 1) {
            return false;
        }
        String str = this.asyncJobIdValue;
        String str2 = launchResultBase.asyncJobIdValue;
        if (str != str2 && !str.equals(str2)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
