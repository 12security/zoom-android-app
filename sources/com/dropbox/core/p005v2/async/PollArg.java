package com.dropbox.core.p005v2.async;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.async.PollArg */
public class PollArg {
    protected final String asyncJobId;

    /* renamed from: com.dropbox.core.v2.async.PollArg$Serializer */
    public static class Serializer extends StructSerializer<PollArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(PollArg pollArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("async_job_id");
            StoneSerializers.string().serialize(pollArg.asyncJobId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PollArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("async_job_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    PollArg pollArg = new PollArg(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(pollArg, pollArg.toStringMultiline());
                    return pollArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"async_job_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PollArg(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'asyncJobId' is null");
        } else if (str.length() >= 1) {
            this.asyncJobId = str;
        } else {
            throw new IllegalArgumentException("String 'asyncJobId' is shorter than 1");
        }
    }

    public String getAsyncJobId() {
        return this.asyncJobId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.asyncJobId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        PollArg pollArg = (PollArg) obj;
        String str = this.asyncJobId;
        String str2 = pollArg.asyncJobId;
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
