package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.InsufficientQuotaAmounts */
public class InsufficientQuotaAmounts {
    protected final long spaceLeft;
    protected final long spaceNeeded;
    protected final long spaceShortage;

    /* renamed from: com.dropbox.core.v2.sharing.InsufficientQuotaAmounts$Serializer */
    static class Serializer extends StructSerializer<InsufficientQuotaAmounts> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(InsufficientQuotaAmounts insufficientQuotaAmounts, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("space_needed");
            StoneSerializers.uInt64().serialize(Long.valueOf(insufficientQuotaAmounts.spaceNeeded), jsonGenerator);
            jsonGenerator.writeFieldName("space_shortage");
            StoneSerializers.uInt64().serialize(Long.valueOf(insufficientQuotaAmounts.spaceShortage), jsonGenerator);
            jsonGenerator.writeFieldName("space_left");
            StoneSerializers.uInt64().serialize(Long.valueOf(insufficientQuotaAmounts.spaceLeft), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public InsufficientQuotaAmounts deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l2 = null;
                Long l3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("space_needed".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("space_shortage".equals(currentName)) {
                        l2 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("space_left".equals(currentName)) {
                        l3 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"space_needed\" missing.");
                } else if (l2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"space_shortage\" missing.");
                } else if (l3 != null) {
                    InsufficientQuotaAmounts insufficientQuotaAmounts = new InsufficientQuotaAmounts(l.longValue(), l2.longValue(), l3.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(insufficientQuotaAmounts, insufficientQuotaAmounts.toStringMultiline());
                    return insufficientQuotaAmounts;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"space_left\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public InsufficientQuotaAmounts(long j, long j2, long j3) {
        this.spaceNeeded = j;
        this.spaceShortage = j2;
        this.spaceLeft = j3;
    }

    public long getSpaceNeeded() {
        return this.spaceNeeded;
    }

    public long getSpaceShortage() {
        return this.spaceShortage;
    }

    public long getSpaceLeft() {
        return this.spaceLeft;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.spaceNeeded), Long.valueOf(this.spaceShortage), Long.valueOf(this.spaceLeft)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        InsufficientQuotaAmounts insufficientQuotaAmounts = (InsufficientQuotaAmounts) obj;
        if (!(this.spaceNeeded == insufficientQuotaAmounts.spaceNeeded && this.spaceShortage == insufficientQuotaAmounts.spaceShortage && this.spaceLeft == insufficientQuotaAmounts.spaceLeft)) {
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
