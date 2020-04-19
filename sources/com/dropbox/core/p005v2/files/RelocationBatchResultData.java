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

/* renamed from: com.dropbox.core.v2.files.RelocationBatchResultData */
public class RelocationBatchResultData {
    protected final Metadata metadata;

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchResultData$Serializer */
    static class Serializer extends StructSerializer<RelocationBatchResultData> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationBatchResultData relocationBatchResultData, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
            Serializer.INSTANCE.serialize(relocationBatchResultData.metadata, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RelocationBatchResultData deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Metadata metadata = null;
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
                        metadata = (Metadata) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (metadata != null) {
                    RelocationBatchResultData relocationBatchResultData = new RelocationBatchResultData(metadata);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(relocationBatchResultData, relocationBatchResultData.toStringMultiline());
                    return relocationBatchResultData;
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

    public RelocationBatchResultData(Metadata metadata2) {
        if (metadata2 != null) {
            this.metadata = metadata2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'metadata' is null");
    }

    public Metadata getMetadata() {
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
        RelocationBatchResultData relocationBatchResultData = (RelocationBatchResultData) obj;
        Metadata metadata2 = this.metadata;
        Metadata metadata3 = relocationBatchResultData.metadata;
        if (metadata2 != metadata3 && !metadata2.equals(metadata3)) {
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
