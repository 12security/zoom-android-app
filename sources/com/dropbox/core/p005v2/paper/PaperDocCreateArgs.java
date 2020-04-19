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

/* renamed from: com.dropbox.core.v2.paper.PaperDocCreateArgs */
class PaperDocCreateArgs {
    protected final ImportFormat importFormat;
    protected final String parentFolderId;

    /* renamed from: com.dropbox.core.v2.paper.PaperDocCreateArgs$Serializer */
    static class Serializer extends StructSerializer<PaperDocCreateArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocCreateArgs paperDocCreateArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("import_format");
            Serializer.INSTANCE.serialize(paperDocCreateArgs.importFormat, jsonGenerator);
            if (paperDocCreateArgs.parentFolderId != null) {
                jsonGenerator.writeFieldName("parent_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(paperDocCreateArgs.parentFolderId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocCreateArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            ImportFormat importFormat = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("import_format".equals(currentName)) {
                        importFormat = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("parent_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (importFormat != null) {
                    PaperDocCreateArgs paperDocCreateArgs = new PaperDocCreateArgs(importFormat, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocCreateArgs, paperDocCreateArgs.toStringMultiline());
                    return paperDocCreateArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"import_format\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PaperDocCreateArgs(ImportFormat importFormat2, String str) {
        this.parentFolderId = str;
        if (importFormat2 != null) {
            this.importFormat = importFormat2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'importFormat' is null");
    }

    public PaperDocCreateArgs(ImportFormat importFormat2) {
        this(importFormat2, null);
    }

    public ImportFormat getImportFormat() {
        return this.importFormat;
    }

    public String getParentFolderId() {
        return this.parentFolderId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.parentFolderId, this.importFormat});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.paper.PaperDocCreateArgs r5 = (com.dropbox.core.p005v2.paper.PaperDocCreateArgs) r5
            com.dropbox.core.v2.paper.ImportFormat r2 = r4.importFormat
            com.dropbox.core.v2.paper.ImportFormat r3 = r5.importFormat
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.parentFolderId
            java.lang.String r5 = r5.parentFolderId
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.PaperDocCreateArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
