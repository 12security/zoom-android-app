package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.DeletedMetadata */
public class DeletedMetadata extends Metadata {

    /* renamed from: com.dropbox.core.v2.files.DeletedMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.Metadata.Builder {
        protected Builder(String str) {
            super(str);
        }

        public Builder withPathLower(String str) {
            super.withPathLower(str);
            return this;
        }

        public Builder withPathDisplay(String str) {
            super.withPathDisplay(str);
            return this;
        }

        public Builder withParentSharedFolderId(String str) {
            super.withParentSharedFolderId(str);
            return this;
        }

        public DeletedMetadata build() {
            return new DeletedMetadata(this.name, this.pathLower, this.pathDisplay, this.parentSharedFolderId);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DeletedMetadata$Serializer */
    static class Serializer extends StructSerializer<DeletedMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeletedMetadata deletedMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("deleted", jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(deletedMetadata.name, jsonGenerator);
            if (deletedMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deletedMetadata.pathLower, jsonGenerator);
            }
            if (deletedMetadata.pathDisplay != null) {
                jsonGenerator.writeFieldName("path_display");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deletedMetadata.pathDisplay, jsonGenerator);
            }
            if (deletedMetadata.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deletedMetadata.parentSharedFolderId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeletedMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("deleted".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                String str5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("path_lower".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("path_display".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    DeletedMetadata deletedMetadata = new DeletedMetadata(str2, str3, str4, str5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deletedMetadata, deletedMetadata.toStringMultiline());
                    return deletedMetadata;
                }
                throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeletedMetadata(String str, String str2, String str3, String str4) {
        super(str, str2, str3, str4);
    }

    public DeletedMetadata(String str) {
        this(str, null, null, null);
    }

    public String getName() {
        return this.name;
    }

    public String getPathLower() {
        return this.pathLower;
    }

    public String getPathDisplay() {
        return this.pathDisplay;
    }

    public String getParentSharedFolderId() {
        return this.parentSharedFolderId;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DeletedMetadata deletedMetadata = (DeletedMetadata) obj;
        if ((this.name != deletedMetadata.name && !this.name.equals(deletedMetadata.name)) || ((this.pathLower != deletedMetadata.pathLower && (this.pathLower == null || !this.pathLower.equals(deletedMetadata.pathLower))) || ((this.pathDisplay != deletedMetadata.pathDisplay && (this.pathDisplay == null || !this.pathDisplay.equals(deletedMetadata.pathDisplay))) || (this.parentSharedFolderId != deletedMetadata.parentSharedFolderId && (this.parentSharedFolderId == null || !this.parentSharedFolderId.equals(deletedMetadata.parentSharedFolderId)))))) {
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
