package com.dropbox.core.p005v2.teamlog;

import androidx.core.provider.FontsContractCompat.Columns;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.FolderLogInfo */
public class FolderLogInfo extends FileOrFolderLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.FolderLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.FileOrFolderLogInfo.Builder {
        protected Builder(PathLogInfo pathLogInfo) {
            super(pathLogInfo);
        }

        public Builder withDisplayName(String str) {
            super.withDisplayName(str);
            return this;
        }

        public Builder withFileId(String str) {
            super.withFileId(str);
            return this;
        }

        public FolderLogInfo build() {
            return new FolderLogInfo(this.path, this.displayName, this.fileId);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.FolderLogInfo$Serializer */
    static class Serializer extends StructSerializer<FolderLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FolderLogInfo folderLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            Serializer.INSTANCE.serialize(folderLogInfo.path, jsonGenerator);
            if (folderLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderLogInfo.displayName, jsonGenerator);
            }
            if (folderLogInfo.fileId != null) {
                jsonGenerator.writeFieldName(Columns.FILE_ID);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderLogInfo.fileId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FolderLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            PathLogInfo pathLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        pathLogInfo = (PathLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if (Columns.FILE_ID.equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (pathLogInfo != null) {
                    FolderLogInfo folderLogInfo = new FolderLogInfo(pathLogInfo, str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(folderLogInfo, folderLogInfo.toStringMultiline());
                    return folderLogInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FolderLogInfo(PathLogInfo pathLogInfo, String str, String str2) {
        super(pathLogInfo, str, str2);
    }

    public FolderLogInfo(PathLogInfo pathLogInfo) {
        this(pathLogInfo, null, null);
    }

    public PathLogInfo getPath() {
        return this.path;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getFileId() {
        return this.fileId;
    }

    public static Builder newBuilder(PathLogInfo pathLogInfo) {
        return new Builder(pathLogInfo);
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
        FolderLogInfo folderLogInfo = (FolderLogInfo) obj;
        if ((this.path != folderLogInfo.path && !this.path.equals(folderLogInfo.path)) || ((this.displayName != folderLogInfo.displayName && (this.displayName == null || !this.displayName.equals(folderLogInfo.displayName))) || (this.fileId != folderLogInfo.fileId && (this.fileId == null || !this.fileId.equals(folderLogInfo.fileId))))) {
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
