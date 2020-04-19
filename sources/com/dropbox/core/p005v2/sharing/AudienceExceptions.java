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
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.AudienceExceptions */
public class AudienceExceptions {
    protected final long count;
    protected final List<AudienceExceptionContentInfo> exceptions;

    /* renamed from: com.dropbox.core.v2.sharing.AudienceExceptions$Serializer */
    static class Serializer extends StructSerializer<AudienceExceptions> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AudienceExceptions audienceExceptions, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("count");
            StoneSerializers.uInt32().serialize(Long.valueOf(audienceExceptions.count), jsonGenerator);
            jsonGenerator.writeFieldName("exceptions");
            StoneSerializers.list(Serializer.INSTANCE).serialize(audienceExceptions.exceptions, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AudienceExceptions deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("count".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else if ("exceptions".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"count\" missing.");
                } else if (list != null) {
                    AudienceExceptions audienceExceptions = new AudienceExceptions(l.longValue(), list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(audienceExceptions, audienceExceptions.toStringMultiline());
                    return audienceExceptions;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"exceptions\" missing.");
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

    public AudienceExceptions(long j, List<AudienceExceptionContentInfo> list) {
        this.count = j;
        if (list != null) {
            for (AudienceExceptionContentInfo audienceExceptionContentInfo : list) {
                if (audienceExceptionContentInfo == null) {
                    throw new IllegalArgumentException("An item in list 'exceptions' is null");
                }
            }
            this.exceptions = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'exceptions' is null");
    }

    public long getCount() {
        return this.count;
    }

    public List<AudienceExceptionContentInfo> getExceptions() {
        return this.exceptions;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.count), this.exceptions});
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
            com.dropbox.core.v2.sharing.AudienceExceptions r7 = (com.dropbox.core.p005v2.sharing.AudienceExceptions) r7
            long r2 = r6.count
            long r4 = r7.count
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x002d
            java.util.List<com.dropbox.core.v2.sharing.AudienceExceptionContentInfo> r2 = r6.exceptions
            java.util.List<com.dropbox.core.v2.sharing.AudienceExceptionContentInfo> r7 = r7.exceptions
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.AudienceExceptions.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
