package com.dropbox.core.p005v2.files;

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

/* renamed from: com.dropbox.core.v2.files.ListFolderLongpollResult */
public class ListFolderLongpollResult {
    protected final Long backoff;
    protected final boolean changes;

    /* renamed from: com.dropbox.core.v2.files.ListFolderLongpollResult$Serializer */
    static class Serializer extends StructSerializer<ListFolderLongpollResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderLongpollResult listFolderLongpollResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxRequestEvent.STREAM_TYPE_CHANGES);
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderLongpollResult.changes), jsonGenerator);
            if (listFolderLongpollResult.backoff != null) {
                jsonGenerator.writeFieldName("backoff");
                StoneSerializers.nullable(StoneSerializers.uInt64()).serialize(listFolderLongpollResult.backoff, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFolderLongpollResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxRequestEvent.STREAM_TYPE_CHANGES.equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("backoff".equals(currentName)) {
                        l = (Long) StoneSerializers.nullable(StoneSerializers.uInt64()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool != null) {
                    ListFolderLongpollResult listFolderLongpollResult = new ListFolderLongpollResult(bool.booleanValue(), l);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFolderLongpollResult, listFolderLongpollResult.toStringMultiline());
                    return listFolderLongpollResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"changes\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListFolderLongpollResult(boolean z, Long l) {
        this.changes = z;
        this.backoff = l;
    }

    public ListFolderLongpollResult(boolean z) {
        this(z, null);
    }

    public boolean getChanges() {
        return this.changes;
    }

    public Long getBackoff() {
        return this.backoff;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Boolean.valueOf(this.changes), this.backoff});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
        if (r2.equals(r5) == false) goto L_0x002d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x002f
            com.dropbox.core.v2.files.ListFolderLongpollResult r5 = (com.dropbox.core.p005v2.files.ListFolderLongpollResult) r5
            boolean r2 = r4.changes
            boolean r3 = r5.changes
            if (r2 != r3) goto L_0x002d
            java.lang.Long r2 = r4.backoff
            java.lang.Long r5 = r5.backoff
            if (r2 == r5) goto L_0x002e
            if (r2 == 0) goto L_0x002d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x002d
            goto L_0x002e
        L_0x002d:
            r0 = 0
        L_0x002e:
            return r0
        L_0x002f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ListFolderLongpollResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
