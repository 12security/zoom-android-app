package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.Metadata */
public class Metadata {
    protected final String name;
    protected final String parentSharedFolderId;
    protected final String pathDisplay;
    protected final String pathLower;

    /* renamed from: com.dropbox.core.v2.files.Metadata$Builder */
    public static class Builder {
        protected final String name;
        protected String parentSharedFolderId;
        protected String pathDisplay;
        protected String pathLower;

        protected Builder(String str) {
            if (str != null) {
                this.name = str;
                this.pathLower = null;
                this.pathDisplay = null;
                this.parentSharedFolderId = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'name' is null");
        }

        public Builder withPathLower(String str) {
            this.pathLower = str;
            return this;
        }

        public Builder withPathDisplay(String str) {
            this.pathDisplay = str;
            return this;
        }

        public Builder withParentSharedFolderId(String str) {
            if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.parentSharedFolderId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
        }

        public Metadata build() {
            return new Metadata(this.name, this.pathLower, this.pathDisplay, this.parentSharedFolderId);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.Metadata$Serializer */
    static class Serializer extends StructSerializer<Metadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(Metadata metadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (metadata instanceof FileMetadata) {
                Serializer.INSTANCE.serialize((FileMetadata) metadata, jsonGenerator, z);
            } else if (metadata instanceof FolderMetadata) {
                Serializer.INSTANCE.serialize((FolderMetadata) metadata, jsonGenerator, z);
            } else if (metadata instanceof DeletedMetadata) {
                Serializer.INSTANCE.serialize((DeletedMetadata) metadata, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                jsonGenerator.writeFieldName("name");
                StoneSerializers.string().serialize(metadata.name, jsonGenerator);
                if (metadata.pathLower != null) {
                    jsonGenerator.writeFieldName("path_lower");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(metadata.pathLower, jsonGenerator);
                }
                if (metadata.pathDisplay != null) {
                    jsonGenerator.writeFieldName("path_display");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(metadata.pathDisplay, jsonGenerator);
                }
                if (metadata.parentSharedFolderId != null) {
                    jsonGenerator.writeFieldName("parent_shared_folder_id");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(metadata.parentSharedFolderId, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public Metadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Metadata metadata;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("".equals(str)) {
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
                    metadata = new Metadata(str2, str3, str4, str5);
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                }
            } else if ("".equals(str)) {
                metadata = INSTANCE.deserialize(jsonParser, true);
            } else if (BoxFile.TYPE.equals(str)) {
                metadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if (BoxFolder.TYPE.equals(str)) {
                metadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("deleted".equals(str)) {
                metadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
            if (!z) {
                expectEndObject(jsonParser);
            }
            StoneDeserializerLogger.log(metadata, metadata.toStringMultiline());
            return metadata;
        }
    }

    public Metadata(String str, String str2, String str3, String str4) {
        if (str != null) {
            this.name = str;
            this.pathLower = str2;
            this.pathDisplay = str3;
            if (str4 == null || Pattern.matches("[-_0-9a-zA-Z:]+", str4)) {
                this.parentSharedFolderId = str4;
                return;
            }
            throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
        }
        throw new IllegalArgumentException("Required value for 'name' is null");
    }

    public Metadata(String str) {
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
        return Arrays.hashCode(new Object[]{this.name, this.pathLower, this.pathDisplay, this.parentSharedFolderId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        if (r2.equals(r5) == false) goto L_0x004f;
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
            if (r2 == 0) goto L_0x0051
            com.dropbox.core.v2.files.Metadata r5 = (com.dropbox.core.p005v2.files.Metadata) r5
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0024:
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0032:
            java.lang.String r2 = r4.pathDisplay
            java.lang.String r3 = r5.pathDisplay
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0040:
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r5 = r5.parentSharedFolderId
            if (r2 == r5) goto L_0x0050
            if (r2 == 0) goto L_0x004f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r0 = 0
        L_0x0050:
            return r0
        L_0x0051:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.Metadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
