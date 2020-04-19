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
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.FileOrFolderLogInfo */
public class FileOrFolderLogInfo {
    protected final String displayName;
    protected final String fileId;
    protected final PathLogInfo path;

    /* renamed from: com.dropbox.core.v2.teamlog.FileOrFolderLogInfo$Builder */
    public static class Builder {
        protected String displayName;
        protected String fileId;
        protected final PathLogInfo path;

        protected Builder(PathLogInfo pathLogInfo) {
            if (pathLogInfo != null) {
                this.path = pathLogInfo;
                this.displayName = null;
                this.fileId = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'path' is null");
        }

        public Builder withDisplayName(String str) {
            this.displayName = str;
            return this;
        }

        public Builder withFileId(String str) {
            this.fileId = str;
            return this;
        }

        public FileOrFolderLogInfo build() {
            return new FileOrFolderLogInfo(this.path, this.displayName, this.fileId);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.FileOrFolderLogInfo$Serializer */
    private static class Serializer extends StructSerializer<FileOrFolderLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(FileOrFolderLogInfo fileOrFolderLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            Serializer.INSTANCE.serialize(fileOrFolderLogInfo.path, jsonGenerator);
            if (fileOrFolderLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileOrFolderLogInfo.displayName, jsonGenerator);
            }
            if (fileOrFolderLogInfo.fileId != null) {
                jsonGenerator.writeFieldName(Columns.FILE_ID);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileOrFolderLogInfo.fileId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileOrFolderLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    FileOrFolderLogInfo fileOrFolderLogInfo = new FileOrFolderLogInfo(pathLogInfo, str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileOrFolderLogInfo, fileOrFolderLogInfo.toStringMultiline());
                    return fileOrFolderLogInfo;
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

    public FileOrFolderLogInfo(PathLogInfo pathLogInfo, String str, String str2) {
        if (pathLogInfo != null) {
            this.path = pathLogInfo;
            this.displayName = str;
            this.fileId = str2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'path' is null");
    }

    public FileOrFolderLogInfo(PathLogInfo pathLogInfo) {
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
        return Arrays.hashCode(new Object[]{this.path, this.displayName, this.fileId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.teamlog.FileOrFolderLogInfo r5 = (com.dropbox.core.p005v2.teamlog.FileOrFolderLogInfo) r5
            com.dropbox.core.v2.teamlog.PathLogInfo r2 = r4.path
            com.dropbox.core.v2.teamlog.PathLogInfo r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            java.lang.String r2 = r4.fileId
            java.lang.String r5 = r5.fileId
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.FileOrFolderLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
