package com.dropbox.core.p005v2.auth;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.kubi.KubiContract;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.auth.RateLimitError */
public class RateLimitError {
    protected final RateLimitReason reason;
    protected final long retryAfter;

    /* renamed from: com.dropbox.core.v2.auth.RateLimitError$Serializer */
    public static class Serializer extends StructSerializer<RateLimitError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(RateLimitError rateLimitError, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(KubiContract.EXTRA_REASON);
            com.dropbox.core.p005v2.auth.RateLimitReason.Serializer.INSTANCE.serialize(rateLimitError.reason, jsonGenerator);
            jsonGenerator.writeFieldName("retry_after");
            StoneSerializers.uInt64().serialize(Long.valueOf(rateLimitError.retryAfter), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RateLimitError deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            RateLimitReason rateLimitReason = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(1);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (KubiContract.EXTRA_REASON.equals(currentName)) {
                        rateLimitReason = com.dropbox.core.p005v2.auth.RateLimitReason.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("retry_after".equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (rateLimitReason != null) {
                    RateLimitError rateLimitError = new RateLimitError(rateLimitReason, valueOf.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(rateLimitError, rateLimitError.toStringMultiline());
                    return rateLimitError;
                }
                throw new JsonParseException(jsonParser, "Required field \"reason\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RateLimitError(RateLimitReason rateLimitReason, long j) {
        if (rateLimitReason != null) {
            this.reason = rateLimitReason;
            this.retryAfter = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'reason' is null");
    }

    public RateLimitError(RateLimitReason rateLimitReason) {
        this(rateLimitReason, 1);
    }

    public RateLimitReason getReason() {
        return this.reason;
    }

    public long getRetryAfter() {
        return this.retryAfter;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.reason, Long.valueOf(this.retryAfter)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RateLimitError rateLimitError = (RateLimitError) obj;
        RateLimitReason rateLimitReason = this.reason;
        RateLimitReason rateLimitReason2 = rateLimitError.reason;
        if ((rateLimitReason != rateLimitReason2 && !rateLimitReason.equals(rateLimitReason2)) || this.retryAfter != rateLimitError.retryAfter) {
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
