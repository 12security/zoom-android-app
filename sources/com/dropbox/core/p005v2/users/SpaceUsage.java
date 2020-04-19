package com.dropbox.core.p005v2.users;

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

/* renamed from: com.dropbox.core.v2.users.SpaceUsage */
public class SpaceUsage {
    protected final SpaceAllocation allocation;
    protected final long used;

    /* renamed from: com.dropbox.core.v2.users.SpaceUsage$Serializer */
    static class Serializer extends StructSerializer<SpaceUsage> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SpaceUsage spaceUsage, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("used");
            StoneSerializers.uInt64().serialize(Long.valueOf(spaceUsage.used), jsonGenerator);
            jsonGenerator.writeFieldName("allocation");
            Serializer.INSTANCE.serialize(spaceUsage.allocation, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SpaceUsage deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SpaceAllocation spaceAllocation = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("used".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("allocation".equals(currentName)) {
                        spaceAllocation = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"used\" missing.");
                } else if (spaceAllocation != null) {
                    SpaceUsage spaceUsage = new SpaceUsage(l.longValue(), spaceAllocation);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(spaceUsage, spaceUsage.toStringMultiline());
                    return spaceUsage;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"allocation\" missing.");
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

    public SpaceUsage(long j, SpaceAllocation spaceAllocation) {
        this.used = j;
        if (spaceAllocation != null) {
            this.allocation = spaceAllocation;
            return;
        }
        throw new IllegalArgumentException("Required value for 'allocation' is null");
    }

    public long getUsed() {
        return this.used;
    }

    public SpaceAllocation getAllocation() {
        return this.allocation;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.used), this.allocation});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        if (r2.equals(r7) == false) goto L_0x002d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x002f
            com.dropbox.core.v2.users.SpaceUsage r7 = (com.dropbox.core.p005v2.users.SpaceUsage) r7
            long r2 = r6.used
            long r4 = r7.used
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x002d
            com.dropbox.core.v2.users.SpaceAllocation r2 = r6.allocation
            com.dropbox.core.v2.users.SpaceAllocation r7 = r7.allocation
            if (r2 == r7) goto L_0x002e
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x002d
            goto L_0x002e
        L_0x002d:
            r0 = 0
        L_0x002e:
            return r0
        L_0x002f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.SpaceUsage.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
