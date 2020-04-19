package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.PaperDocDownloadDetails */
public class PaperDocDownloadDetails {
    protected final String eventUuid;
    protected final PaperDownloadFormat exportFileFormat;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDocDownloadDetails$Serializer */
    static class Serializer extends StructSerializer<PaperDocDownloadDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocDownloadDetails paperDocDownloadDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperDocDownloadDetails.eventUuid, jsonGenerator);
            jsonGenerator.writeFieldName("export_file_format");
            Serializer.INSTANCE.serialize(paperDocDownloadDetails.exportFileFormat, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocDownloadDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                PaperDownloadFormat paperDownloadFormat = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("export_file_format".equals(currentName)) {
                        paperDownloadFormat = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
                } else if (paperDownloadFormat != null) {
                    PaperDocDownloadDetails paperDocDownloadDetails = new PaperDocDownloadDetails(str2, paperDownloadFormat);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocDownloadDetails, paperDocDownloadDetails.toStringMultiline());
                    return paperDocDownloadDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"export_file_format\" missing.");
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

    public PaperDocDownloadDetails(String str, PaperDownloadFormat paperDownloadFormat) {
        if (str != null) {
            this.eventUuid = str;
            if (paperDownloadFormat != null) {
                this.exportFileFormat = paperDownloadFormat;
                return;
            }
            throw new IllegalArgumentException("Required value for 'exportFileFormat' is null");
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public PaperDownloadFormat getExportFileFormat() {
        return this.exportFileFormat;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, this.exportFileFormat});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.teamlog.PaperDocDownloadDetails r5 = (com.dropbox.core.p005v2.teamlog.PaperDocDownloadDetails) r5
            java.lang.String r2 = r4.eventUuid
            java.lang.String r3 = r5.eventUuid
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.teamlog.PaperDownloadFormat r2 = r4.exportFileFormat
            com.dropbox.core.v2.teamlog.PaperDownloadFormat r5 = r5.exportFileFormat
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PaperDocDownloadDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
