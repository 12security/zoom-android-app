package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.BoxApiMetadata;
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

/* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchResultData */
public class GetThumbnailBatchResultData {
    protected final FileMetadata metadata;
    protected final String thumbnail;

    /* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchResultData$Serializer */
    static class Serializer extends StructSerializer<GetThumbnailBatchResultData> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetThumbnailBatchResultData getThumbnailBatchResultData, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
            Serializer.INSTANCE.serialize(getThumbnailBatchResultData.metadata, jsonGenerator);
            jsonGenerator.writeFieldName("thumbnail");
            StoneSerializers.string().serialize(getThumbnailBatchResultData.thumbnail, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetThumbnailBatchResultData deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            FileMetadata fileMetadata = null;
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
                    if (BoxApiMetadata.BOX_API_METADATA.equals(currentName)) {
                        fileMetadata = (FileMetadata) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("thumbnail".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (fileMetadata == null) {
                    throw new JsonParseException(jsonParser, "Required field \"metadata\" missing.");
                } else if (str2 != null) {
                    GetThumbnailBatchResultData getThumbnailBatchResultData = new GetThumbnailBatchResultData(fileMetadata, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getThumbnailBatchResultData, getThumbnailBatchResultData.toStringMultiline());
                    return getThumbnailBatchResultData;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"thumbnail\" missing.");
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

    public GetThumbnailBatchResultData(FileMetadata fileMetadata, String str) {
        if (fileMetadata != null) {
            this.metadata = fileMetadata;
            if (str != null) {
                this.thumbnail = str;
                return;
            }
            throw new IllegalArgumentException("Required value for 'thumbnail' is null");
        }
        throw new IllegalArgumentException("Required value for 'metadata' is null");
    }

    public FileMetadata getMetadata() {
        return this.metadata;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.metadata, this.thumbnail});
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
            com.dropbox.core.v2.files.GetThumbnailBatchResultData r5 = (com.dropbox.core.p005v2.files.GetThumbnailBatchResultData) r5
            com.dropbox.core.v2.files.FileMetadata r2 = r4.metadata
            com.dropbox.core.v2.files.FileMetadata r3 = r5.metadata
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.thumbnail
            java.lang.String r5 = r5.thumbnail
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.GetThumbnailBatchResultData.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
