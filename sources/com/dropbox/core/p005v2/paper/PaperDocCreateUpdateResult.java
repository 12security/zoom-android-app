package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.PaperDocCreateUpdateResult */
public class PaperDocCreateUpdateResult {
    protected final String docId;
    protected final long revision;
    protected final String title;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocCreateUpdateResult$Serializer */
    static class Serializer extends StructSerializer<PaperDocCreateUpdateResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocCreateUpdateResult paperDocCreateUpdateResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(paperDocCreateUpdateResult.docId, jsonGenerator);
            jsonGenerator.writeFieldName("revision");
            StoneSerializers.int64().serialize(Long.valueOf(paperDocCreateUpdateResult.revision), jsonGenerator);
            jsonGenerator.writeFieldName("title");
            StoneSerializers.string().serialize(paperDocCreateUpdateResult.title, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocCreateUpdateResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("revision".equals(currentName)) {
                        l = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else if ("title".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
                } else if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"revision\" missing.");
                } else if (str3 != null) {
                    PaperDocCreateUpdateResult paperDocCreateUpdateResult = new PaperDocCreateUpdateResult(str2, l.longValue(), str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocCreateUpdateResult, paperDocCreateUpdateResult.toStringMultiline());
                    return paperDocCreateUpdateResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"title\" missing.");
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

    public PaperDocCreateUpdateResult(String str, long j, String str2) {
        if (str != null) {
            this.docId = str;
            this.revision = j;
            if (str2 != null) {
                this.title = str2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'title' is null");
        }
        throw new IllegalArgumentException("Required value for 'docId' is null");
    }

    public String getDocId() {
        return this.docId;
    }

    public long getRevision() {
        return this.revision;
    }

    public String getTitle() {
        return this.title;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.docId, Long.valueOf(this.revision), this.title});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        if (r2.equals(r7) == false) goto L_0x0039;
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
            if (r2 == 0) goto L_0x003b
            com.dropbox.core.v2.paper.PaperDocCreateUpdateResult r7 = (com.dropbox.core.p005v2.paper.PaperDocCreateUpdateResult) r7
            java.lang.String r2 = r6.docId
            java.lang.String r3 = r7.docId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x0024:
            long r2 = r6.revision
            long r4 = r7.revision
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0039
            java.lang.String r2 = r6.title
            java.lang.String r7 = r7.title
            if (r2 == r7) goto L_0x003a
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r0 = 0
        L_0x003a:
            return r0
        L_0x003b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.PaperDocCreateUpdateResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
