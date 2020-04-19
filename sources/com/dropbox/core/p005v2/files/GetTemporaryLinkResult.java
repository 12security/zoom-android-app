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

/* renamed from: com.dropbox.core.v2.files.GetTemporaryLinkResult */
public class GetTemporaryLinkResult {
    protected final String link;
    protected final FileMetadata metadata;

    /* renamed from: com.dropbox.core.v2.files.GetTemporaryLinkResult$Serializer */
    static class Serializer extends StructSerializer<GetTemporaryLinkResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetTemporaryLinkResult getTemporaryLinkResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
            Serializer.INSTANCE.serialize(getTemporaryLinkResult.metadata, jsonGenerator);
            jsonGenerator.writeFieldName("link");
            StoneSerializers.string().serialize(getTemporaryLinkResult.link, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetTemporaryLinkResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    } else if ("link".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (fileMetadata == null) {
                    throw new JsonParseException(jsonParser, "Required field \"metadata\" missing.");
                } else if (str2 != null) {
                    GetTemporaryLinkResult getTemporaryLinkResult = new GetTemporaryLinkResult(fileMetadata, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getTemporaryLinkResult, getTemporaryLinkResult.toStringMultiline());
                    return getTemporaryLinkResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"link\" missing.");
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

    public GetTemporaryLinkResult(FileMetadata fileMetadata, String str) {
        if (fileMetadata != null) {
            this.metadata = fileMetadata;
            if (str != null) {
                this.link = str;
                return;
            }
            throw new IllegalArgumentException("Required value for 'link' is null");
        }
        throw new IllegalArgumentException("Required value for 'metadata' is null");
    }

    public FileMetadata getMetadata() {
        return this.metadata;
    }

    public String getLink() {
        return this.link;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.metadata, this.link});
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
            com.dropbox.core.v2.files.GetTemporaryLinkResult r5 = (com.dropbox.core.p005v2.files.GetTemporaryLinkResult) r5
            com.dropbox.core.v2.files.FileMetadata r2 = r4.metadata
            com.dropbox.core.v2.files.FileMetadata r3 = r5.metadata
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.link
            java.lang.String r5 = r5.link
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.GetTemporaryLinkResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
