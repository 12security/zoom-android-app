package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxFolder;
import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
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
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.FolderMetadata */
public class FolderMetadata extends Metadata {

    /* renamed from: id */
    protected final String f96id;
    protected final List<PropertyGroup> propertyGroups;
    protected final String sharedFolderId;
    protected final FolderSharingInfo sharingInfo;

    /* renamed from: com.dropbox.core.v2.files.FolderMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.Metadata.Builder {

        /* renamed from: id */
        protected final String f97id;
        protected List<PropertyGroup> propertyGroups;
        protected String sharedFolderId;
        protected FolderSharingInfo sharingInfo;

        protected Builder(String str, String str2) {
            super(str);
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'id' is null");
            } else if (str2.length() >= 1) {
                this.f97id = str2;
                this.sharedFolderId = null;
                this.sharingInfo = null;
                this.propertyGroups = null;
            } else {
                throw new IllegalArgumentException("String 'id' is shorter than 1");
            }
        }

        public Builder withSharedFolderId(String str) {
            if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.sharedFolderId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }

        public Builder withSharingInfo(FolderSharingInfo folderSharingInfo) {
            this.sharingInfo = folderSharingInfo;
            return this;
        }

        public Builder withPropertyGroups(List<PropertyGroup> list) {
            if (list != null) {
                for (PropertyGroup propertyGroup : list) {
                    if (propertyGroup == null) {
                        throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                    }
                }
            }
            this.propertyGroups = list;
            return this;
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

        public FolderMetadata build() {
            FolderMetadata folderMetadata = new FolderMetadata(this.name, this.f97id, this.pathLower, this.pathDisplay, this.parentSharedFolderId, this.sharedFolderId, this.sharingInfo, this.propertyGroups);
            return folderMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.FolderMetadata$Serializer */
    static class Serializer extends StructSerializer<FolderMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FolderMetadata folderMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag(BoxFolder.TYPE, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(folderMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(folderMetadata.f96id, jsonGenerator);
            if (folderMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderMetadata.pathLower, jsonGenerator);
            }
            if (folderMetadata.pathDisplay != null) {
                jsonGenerator.writeFieldName("path_display");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderMetadata.pathDisplay, jsonGenerator);
            }
            if (folderMetadata.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderMetadata.parentSharedFolderId, jsonGenerator);
            }
            if (folderMetadata.sharedFolderId != null) {
                jsonGenerator.writeFieldName("shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderMetadata.sharedFolderId, jsonGenerator);
            }
            if (folderMetadata.sharingInfo != null) {
                jsonGenerator.writeFieldName("sharing_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(folderMetadata.sharingInfo, jsonGenerator);
            }
            if (folderMetadata.propertyGroups != null) {
                jsonGenerator.writeFieldName("property_groups");
                StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).serialize(folderMetadata.propertyGroups, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FolderMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if (BoxFolder.TYPE.equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                String str7 = null;
                FolderSharingInfo folderSharingInfo = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("path_lower".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("path_display".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("shared_folder_id".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("sharing_info".equals(currentName)) {
                        folderSharingInfo = (FolderSharingInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("property_groups".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (str3 != null) {
                    FolderMetadata folderMetadata = new FolderMetadata(str2, str3, str4, str5, str6, str7, folderSharingInfo, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(folderMetadata, folderMetadata.toStringMultiline());
                    return folderMetadata;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
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

    public FolderMetadata(String str, String str2, String str3, String str4, String str5, String str6, FolderSharingInfo folderSharingInfo, List<PropertyGroup> list) {
        super(str, str3, str4, str5);
        if (str2 == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str2.length() >= 1) {
            this.f96id = str2;
            if (str6 == null || Pattern.matches("[-_0-9a-zA-Z:]+", str6)) {
                this.sharedFolderId = str6;
                this.sharingInfo = folderSharingInfo;
                if (list != null) {
                    for (PropertyGroup propertyGroup : list) {
                        if (propertyGroup == null) {
                            throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                        }
                    }
                }
                this.propertyGroups = list;
                return;
            }
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        } else {
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        }
    }

    public FolderMetadata(String str, String str2) {
        this(str, str2, null, null, null, null, null, null);
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.f96id;
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

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public FolderSharingInfo getSharingInfo() {
        return this.sharingInfo;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return this.propertyGroups;
    }

    public static Builder newBuilder(String str, String str2) {
        return new Builder(str, str2);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.f96id, this.sharedFolderId, this.sharingInfo, this.propertyGroups});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0098, code lost:
        if (r2.equals(r5) == false) goto L_0x009b;
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
            if (r2 == 0) goto L_0x009d
            com.dropbox.core.v2.files.FolderMetadata r5 = (com.dropbox.core.p005v2.files.FolderMetadata) r5
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0028:
            java.lang.String r2 = r4.f96id
            java.lang.String r3 = r5.f96id
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0034:
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            if (r2 == r3) goto L_0x0048
            java.lang.String r2 = r4.pathLower
            if (r2 == 0) goto L_0x009b
            java.lang.String r2 = r4.pathLower
            java.lang.String r3 = r5.pathLower
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0048:
            java.lang.String r2 = r4.pathDisplay
            java.lang.String r3 = r5.pathDisplay
            if (r2 == r3) goto L_0x005c
            java.lang.String r2 = r4.pathDisplay
            if (r2 == 0) goto L_0x009b
            java.lang.String r2 = r4.pathDisplay
            java.lang.String r3 = r5.pathDisplay
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x005c:
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            if (r2 == r3) goto L_0x0070
            java.lang.String r2 = r4.parentSharedFolderId
            if (r2 == 0) goto L_0x009b
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x0070:
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x007e
            if (r2 == 0) goto L_0x009b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x007e:
            com.dropbox.core.v2.files.FolderSharingInfo r2 = r4.sharingInfo
            com.dropbox.core.v2.files.FolderSharingInfo r3 = r5.sharingInfo
            if (r2 == r3) goto L_0x008c
            if (r2 == 0) goto L_0x009b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009b
        L_0x008c:
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r2 = r4.propertyGroups
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r5 = r5.propertyGroups
            if (r2 == r5) goto L_0x009c
            if (r2 == 0) goto L_0x009b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x009b
            goto L_0x009c
        L_0x009b:
            r0 = 0
        L_0x009c:
            return r0
        L_0x009d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.FolderMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
