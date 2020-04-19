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

/* renamed from: com.dropbox.core.v2.paper.PaperDocExport */
class PaperDocExport extends RefPaperDoc {
    protected final ExportFormat exportFormat;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocExport$Serializer */
    static class Serializer extends StructSerializer<PaperDocExport> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocExport paperDocExport, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(paperDocExport.docId, jsonGenerator);
            jsonGenerator.writeFieldName("export_format");
            Serializer.INSTANCE.serialize(paperDocExport.exportFormat, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocExport deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ExportFormat exportFormat = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("export_format".equals(currentName)) {
                        exportFormat = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
                } else if (exportFormat != null) {
                    PaperDocExport paperDocExport = new PaperDocExport(str2, exportFormat);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocExport, paperDocExport.toStringMultiline());
                    return paperDocExport;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"export_format\" missing.");
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

    public PaperDocExport(String str, ExportFormat exportFormat2) {
        super(str);
        if (exportFormat2 != null) {
            this.exportFormat = exportFormat2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'exportFormat' is null");
    }

    public String getDocId() {
        return this.docId;
    }

    public ExportFormat getExportFormat() {
        return this.exportFormat;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.exportFormat});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
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
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.paper.PaperDocExport r5 = (com.dropbox.core.p005v2.paper.PaperDocExport) r5
            java.lang.String r2 = r4.docId
            java.lang.String r3 = r5.docId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.docId
            java.lang.String r3 = r5.docId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0028:
            com.dropbox.core.v2.paper.ExportFormat r2 = r4.exportFormat
            com.dropbox.core.v2.paper.ExportFormat r5 = r5.exportFormat
            if (r2 == r5) goto L_0x0036
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.PaperDocExport.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
