package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.BoxApiMetadata;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.DownloadZipResult */
public class DownloadZipResult {
    protected final FolderMetadata metadata;

    /* renamed from: com.dropbox.core.v2.files.DownloadZipResult$Serializer */
    static class Serializer extends StructSerializer<DownloadZipResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DownloadZipResult downloadZipResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
            Serializer.INSTANCE.serialize(downloadZipResult.metadata, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DownloadZipResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            FolderMetadata folderMetadata = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxApiMetadata.BOX_API_METADATA.equals(currentName)) {
                        folderMetadata = (FolderMetadata) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (folderMetadata != null) {
                    DownloadZipResult downloadZipResult = new DownloadZipResult(folderMetadata);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(downloadZipResult, downloadZipResult.toStringMultiline());
                    return downloadZipResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"metadata\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DownloadZipResult(FolderMetadata folderMetadata) {
        if (folderMetadata != null) {
            this.metadata = folderMetadata;
            return;
        }
        throw new IllegalArgumentException("Required value for 'metadata' is null");
    }

    public FolderMetadata getMetadata() {
        return this.metadata;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.metadata});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DownloadZipResult downloadZipResult = (DownloadZipResult) obj;
        FolderMetadata folderMetadata = this.metadata;
        FolderMetadata folderMetadata2 = downloadZipResult.metadata;
        if (folderMetadata != folderMetadata2 && !folderMetadata.equals(folderMetadata2)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
