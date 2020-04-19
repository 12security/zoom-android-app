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

/* renamed from: com.dropbox.core.v2.teamlog.FileRequestDetails */
public class FileRequestDetails {
    protected final long assetIndex;
    protected final FileRequestDeadline deadline;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestDetails$Serializer */
    static class Serializer extends StructSerializer<FileRequestDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequestDetails fileRequestDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(fileRequestDetails.assetIndex), jsonGenerator);
            if (fileRequestDetails.deadline != null) {
                jsonGenerator.writeFieldName("deadline");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileRequestDetails.deadline, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRequestDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                FileRequestDeadline fileRequestDeadline = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("asset_index".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("deadline".equals(currentName)) {
                        fileRequestDeadline = (FileRequestDeadline) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l != null) {
                    FileRequestDetails fileRequestDetails = new FileRequestDetails(l.longValue(), fileRequestDeadline);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileRequestDetails, fileRequestDetails.toStringMultiline());
                    return fileRequestDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"asset_index\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRequestDetails(long j, FileRequestDeadline fileRequestDeadline) {
        this.assetIndex = j;
        this.deadline = fileRequestDeadline;
    }

    public FileRequestDetails(long j) {
        this(j, null);
    }

    public long getAssetIndex() {
        return this.assetIndex;
    }

    public FileRequestDeadline getDeadline() {
        return this.deadline;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.assetIndex), this.deadline});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r2.equals(r7) == false) goto L_0x002f;
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
            if (r2 == 0) goto L_0x0031
            com.dropbox.core.v2.teamlog.FileRequestDetails r7 = (com.dropbox.core.p005v2.teamlog.FileRequestDetails) r7
            long r2 = r6.assetIndex
            long r4 = r7.assetIndex
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x002f
            com.dropbox.core.v2.teamlog.FileRequestDeadline r2 = r6.deadline
            com.dropbox.core.v2.teamlog.FileRequestDeadline r7 = r7.deadline
            if (r2 == r7) goto L_0x0030
            if (r2 == 0) goto L_0x002f
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x002f
            goto L_0x0030
        L_0x002f:
            r0 = 0
        L_0x0030:
            return r0
        L_0x0031:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.FileRequestDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
