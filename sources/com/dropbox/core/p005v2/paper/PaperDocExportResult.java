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

/* renamed from: com.dropbox.core.v2.paper.PaperDocExportResult */
public class PaperDocExportResult {
    protected final String mimeType;
    protected final String owner;
    protected final long revision;
    protected final String title;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocExportResult$Serializer */
    static class Serializer extends StructSerializer<PaperDocExportResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocExportResult paperDocExportResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("owner");
            StoneSerializers.string().serialize(paperDocExportResult.owner, jsonGenerator);
            jsonGenerator.writeFieldName("title");
            StoneSerializers.string().serialize(paperDocExportResult.title, jsonGenerator);
            jsonGenerator.writeFieldName("revision");
            StoneSerializers.int64().serialize(Long.valueOf(paperDocExportResult.revision), jsonGenerator);
            jsonGenerator.writeFieldName("mime_type");
            StoneSerializers.string().serialize(paperDocExportResult.mimeType, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocExportResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("owner".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("title".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("revision".equals(currentName)) {
                        l = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else if ("mime_type".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"owner\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"title\" missing.");
                } else if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"revision\" missing.");
                } else if (str4 != null) {
                    PaperDocExportResult paperDocExportResult = new PaperDocExportResult(str2, str3, l.longValue(), str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocExportResult, paperDocExportResult.toStringMultiline());
                    return paperDocExportResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"mime_type\" missing.");
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

    public PaperDocExportResult(String str, String str2, long j, String str3) {
        if (str != null) {
            this.owner = str;
            if (str2 != null) {
                this.title = str2;
                this.revision = j;
                if (str3 != null) {
                    this.mimeType = str3;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'mimeType' is null");
            }
            throw new IllegalArgumentException("Required value for 'title' is null");
        }
        throw new IllegalArgumentException("Required value for 'owner' is null");
    }

    public String getOwner() {
        return this.owner;
    }

    public String getTitle() {
        return this.title;
    }

    public long getRevision() {
        return this.revision;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.owner, this.title, Long.valueOf(this.revision), this.mimeType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0042, code lost:
        if (r2.equals(r7) == false) goto L_0x0045;
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
            if (r2 == 0) goto L_0x0047
            com.dropbox.core.v2.paper.PaperDocExportResult r7 = (com.dropbox.core.p005v2.paper.PaperDocExportResult) r7
            java.lang.String r2 = r6.owner
            java.lang.String r3 = r7.owner
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0024:
            java.lang.String r2 = r6.title
            java.lang.String r3 = r7.title
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0045
        L_0x0030:
            long r2 = r6.revision
            long r4 = r7.revision
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0045
            java.lang.String r2 = r6.mimeType
            java.lang.String r7 = r7.mimeType
            if (r2 == r7) goto L_0x0046
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0045
            goto L_0x0046
        L_0x0045:
            r0 = 0
        L_0x0046:
            return r0
        L_0x0047:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.PaperDocExportResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
