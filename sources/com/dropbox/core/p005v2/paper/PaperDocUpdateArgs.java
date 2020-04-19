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

/* renamed from: com.dropbox.core.v2.paper.PaperDocUpdateArgs */
class PaperDocUpdateArgs extends RefPaperDoc {
    protected final PaperDocUpdatePolicy docUpdatePolicy;
    protected final ImportFormat importFormat;
    protected final long revision;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocUpdateArgs$Serializer */
    static class Serializer extends StructSerializer<PaperDocUpdateArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocUpdateArgs paperDocUpdateArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(paperDocUpdateArgs.docId, jsonGenerator);
            jsonGenerator.writeFieldName("doc_update_policy");
            Serializer.INSTANCE.serialize(paperDocUpdateArgs.docUpdatePolicy, jsonGenerator);
            jsonGenerator.writeFieldName("revision");
            StoneSerializers.int64().serialize(Long.valueOf(paperDocUpdateArgs.revision), jsonGenerator);
            jsonGenerator.writeFieldName("import_format");
            Serializer.INSTANCE.serialize(paperDocUpdateArgs.importFormat, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocUpdateArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                PaperDocUpdatePolicy paperDocUpdatePolicy = null;
                ImportFormat importFormat = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("doc_update_policy".equals(currentName)) {
                        paperDocUpdatePolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("revision".equals(currentName)) {
                        l = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else if ("import_format".equals(currentName)) {
                        importFormat = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
                } else if (paperDocUpdatePolicy == null) {
                    throw new JsonParseException(jsonParser, "Required field \"doc_update_policy\" missing.");
                } else if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"revision\" missing.");
                } else if (importFormat != null) {
                    PaperDocUpdateArgs paperDocUpdateArgs = new PaperDocUpdateArgs(str2, paperDocUpdatePolicy, l.longValue(), importFormat);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocUpdateArgs, paperDocUpdateArgs.toStringMultiline());
                    return paperDocUpdateArgs;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"import_format\" missing.");
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

    public PaperDocUpdateArgs(String str, PaperDocUpdatePolicy paperDocUpdatePolicy, long j, ImportFormat importFormat2) {
        super(str);
        if (paperDocUpdatePolicy != null) {
            this.docUpdatePolicy = paperDocUpdatePolicy;
            this.revision = j;
            if (importFormat2 != null) {
                this.importFormat = importFormat2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'importFormat' is null");
        }
        throw new IllegalArgumentException("Required value for 'docUpdatePolicy' is null");
    }

    public String getDocId() {
        return this.docId;
    }

    public PaperDocUpdatePolicy getDocUpdatePolicy() {
        return this.docUpdatePolicy;
    }

    public long getRevision() {
        return this.revision;
    }

    public ImportFormat getImportFormat() {
        return this.importFormat;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.docUpdatePolicy, Long.valueOf(this.revision), this.importFormat});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0046, code lost:
        if (r2.equals(r7) == false) goto L_0x0049;
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
            if (r2 == 0) goto L_0x004b
            com.dropbox.core.v2.paper.PaperDocUpdateArgs r7 = (com.dropbox.core.p005v2.paper.PaperDocUpdateArgs) r7
            java.lang.String r2 = r6.docId
            java.lang.String r3 = r7.docId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r6.docId
            java.lang.String r3 = r7.docId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0028:
            com.dropbox.core.v2.paper.PaperDocUpdatePolicy r2 = r6.docUpdatePolicy
            com.dropbox.core.v2.paper.PaperDocUpdatePolicy r3 = r7.docUpdatePolicy
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0034:
            long r2 = r6.revision
            long r4 = r7.revision
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0049
            com.dropbox.core.v2.paper.ImportFormat r2 = r6.importFormat
            com.dropbox.core.v2.paper.ImportFormat r7 = r7.importFormat
            if (r2 == r7) goto L_0x004a
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            return r0
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.PaperDocUpdateArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
