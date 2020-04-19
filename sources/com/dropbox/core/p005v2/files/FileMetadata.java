package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxFile;
import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.FileMetadata */
public class FileMetadata extends Metadata {
    protected final Date clientModified;
    protected final String contentHash;
    protected final Boolean hasExplicitSharedMembers;

    /* renamed from: id */
    protected final String f94id;
    protected final MediaInfo mediaInfo;
    protected final List<PropertyGroup> propertyGroups;
    protected final String rev;
    protected final Date serverModified;
    protected final FileSharingInfo sharingInfo;
    protected final long size;
    protected final SymlinkInfo symlinkInfo;

    /* renamed from: com.dropbox.core.v2.files.FileMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.Metadata.Builder {
        protected final Date clientModified;
        protected String contentHash;
        protected Boolean hasExplicitSharedMembers;

        /* renamed from: id */
        protected final String f95id;
        protected MediaInfo mediaInfo;
        protected List<PropertyGroup> propertyGroups;
        protected final String rev;
        protected final Date serverModified;
        protected FileSharingInfo sharingInfo;
        protected final long size;
        protected SymlinkInfo symlinkInfo;

        protected Builder(String str, String str2, Date date, Date date2, String str3, long j) {
            super(str);
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'id' is null");
            } else if (str2.length() >= 1) {
                this.f95id = str2;
                if (date != null) {
                    this.clientModified = LangUtil.truncateMillis(date);
                    if (date2 != null) {
                        this.serverModified = LangUtil.truncateMillis(date2);
                        if (str3 == null) {
                            throw new IllegalArgumentException("Required value for 'rev' is null");
                        } else if (str3.length() < 9) {
                            throw new IllegalArgumentException("String 'rev' is shorter than 9");
                        } else if (Pattern.matches("[0-9a-f]+", str3)) {
                            this.rev = str3;
                            this.size = j;
                            this.mediaInfo = null;
                            this.symlinkInfo = null;
                            this.sharingInfo = null;
                            this.propertyGroups = null;
                            this.hasExplicitSharedMembers = null;
                            this.contentHash = null;
                        } else {
                            throw new IllegalArgumentException("String 'rev' does not match pattern");
                        }
                    } else {
                        throw new IllegalArgumentException("Required value for 'serverModified' is null");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'clientModified' is null");
                }
            } else {
                throw new IllegalArgumentException("String 'id' is shorter than 1");
            }
        }

        public Builder withMediaInfo(MediaInfo mediaInfo2) {
            this.mediaInfo = mediaInfo2;
            return this;
        }

        public Builder withSymlinkInfo(SymlinkInfo symlinkInfo2) {
            this.symlinkInfo = symlinkInfo2;
            return this;
        }

        public Builder withSharingInfo(FileSharingInfo fileSharingInfo) {
            this.sharingInfo = fileSharingInfo;
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

        public Builder withHasExplicitSharedMembers(Boolean bool) {
            this.hasExplicitSharedMembers = bool;
            return this;
        }

        public Builder withContentHash(String str) {
            if (str != null) {
                if (str.length() < 64) {
                    throw new IllegalArgumentException("String 'contentHash' is shorter than 64");
                } else if (str.length() > 64) {
                    throw new IllegalArgumentException("String 'contentHash' is longer than 64");
                }
            }
            this.contentHash = str;
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

        public FileMetadata build() {
            FileMetadata fileMetadata = new FileMetadata(this.name, this.f95id, this.clientModified, this.serverModified, this.rev, this.size, this.pathLower, this.pathDisplay, this.parentSharedFolderId, this.mediaInfo, this.symlinkInfo, this.sharingInfo, this.propertyGroups, this.hasExplicitSharedMembers, this.contentHash);
            return fileMetadata;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.FileMetadata$Serializer */
    static class Serializer extends StructSerializer<FileMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileMetadata fileMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag(BoxFile.TYPE, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(fileMetadata.name, jsonGenerator);
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(fileMetadata.f94id, jsonGenerator);
            jsonGenerator.writeFieldName("client_modified");
            StoneSerializers.timestamp().serialize(fileMetadata.clientModified, jsonGenerator);
            jsonGenerator.writeFieldName("server_modified");
            StoneSerializers.timestamp().serialize(fileMetadata.serverModified, jsonGenerator);
            jsonGenerator.writeFieldName("rev");
            StoneSerializers.string().serialize(fileMetadata.rev, jsonGenerator);
            jsonGenerator.writeFieldName("size");
            StoneSerializers.uInt64().serialize(Long.valueOf(fileMetadata.size), jsonGenerator);
            if (fileMetadata.pathLower != null) {
                jsonGenerator.writeFieldName("path_lower");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileMetadata.pathLower, jsonGenerator);
            }
            if (fileMetadata.pathDisplay != null) {
                jsonGenerator.writeFieldName("path_display");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileMetadata.pathDisplay, jsonGenerator);
            }
            if (fileMetadata.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileMetadata.parentSharedFolderId, jsonGenerator);
            }
            if (fileMetadata.mediaInfo != null) {
                jsonGenerator.writeFieldName("media_info");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(fileMetadata.mediaInfo, jsonGenerator);
            }
            if (fileMetadata.symlinkInfo != null) {
                jsonGenerator.writeFieldName("symlink_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileMetadata.symlinkInfo, jsonGenerator);
            }
            if (fileMetadata.sharingInfo != null) {
                jsonGenerator.writeFieldName("sharing_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileMetadata.sharingInfo, jsonGenerator);
            }
            if (fileMetadata.propertyGroups != null) {
                jsonGenerator.writeFieldName("property_groups");
                StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).serialize(fileMetadata.propertyGroups, jsonGenerator);
            }
            if (fileMetadata.hasExplicitSharedMembers != null) {
                jsonGenerator.writeFieldName("has_explicit_shared_members");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(fileMetadata.hasExplicitSharedMembers, jsonGenerator);
            }
            if (fileMetadata.contentHash != null) {
                jsonGenerator.writeFieldName("content_hash");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileMetadata.contentHash, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if (BoxFile.TYPE.equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                Date date = null;
                Date date2 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                String str7 = null;
                MediaInfo mediaInfo = null;
                SymlinkInfo symlinkInfo = null;
                FileSharingInfo fileSharingInfo = null;
                List list = null;
                Boolean bool = null;
                String str8 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("client_modified".equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser2);
                    } else if ("server_modified".equals(currentName)) {
                        date2 = (Date) StoneSerializers.timestamp().deserialize(jsonParser2);
                    } else if ("rev".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("size".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser2);
                    } else if ("path_lower".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("path_display".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("media_info".equals(currentName)) {
                        mediaInfo = (MediaInfo) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("symlink_info".equals(currentName)) {
                        symlinkInfo = (SymlinkInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("sharing_info".equals(currentName)) {
                        fileSharingInfo = (FileSharingInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("property_groups".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).deserialize(jsonParser2);
                    } else if ("has_explicit_shared_members".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser2);
                    } else if ("content_hash".equals(currentName)) {
                        str8 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"id\" missing.");
                } else if (date == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"client_modified\" missing.");
                } else if (date2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"server_modified\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"rev\" missing.");
                } else if (l != null) {
                    FileMetadata fileMetadata = new FileMetadata(str2, str3, date, date2, str4, l.longValue(), str5, str6, str7, mediaInfo, symlinkInfo, fileSharingInfo, list, bool, str8);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileMetadata, fileMetadata.toStringMultiline());
                    return fileMetadata;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"size\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser2, sb.toString());
            }
        }
    }

    public FileMetadata(String str, String str2, Date date, Date date2, String str3, long j, String str4, String str5, String str6, MediaInfo mediaInfo2, SymlinkInfo symlinkInfo2, FileSharingInfo fileSharingInfo, List<PropertyGroup> list, Boolean bool, String str7) {
        String str8 = str2;
        String str9 = str3;
        List<PropertyGroup> list2 = list;
        String str10 = str;
        String str11 = str7;
        super(str, str4, str5, str6);
        if (str8 == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str2.length() >= 1) {
            this.f94id = str8;
            if (date != null) {
                this.clientModified = LangUtil.truncateMillis(date);
                if (date2 != null) {
                    this.serverModified = LangUtil.truncateMillis(date2);
                    if (str9 == null) {
                        throw new IllegalArgumentException("Required value for 'rev' is null");
                    } else if (str3.length() < 9) {
                        throw new IllegalArgumentException("String 'rev' is shorter than 9");
                    } else if (Pattern.matches("[0-9a-f]+", str3)) {
                        this.rev = str9;
                        this.size = j;
                        this.mediaInfo = mediaInfo2;
                        this.symlinkInfo = symlinkInfo2;
                        this.sharingInfo = fileSharingInfo;
                        if (list2 != null) {
                            for (PropertyGroup propertyGroup : list) {
                                if (propertyGroup == null) {
                                    throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                                }
                            }
                        }
                        this.propertyGroups = list2;
                        this.hasExplicitSharedMembers = bool;
                        if (str11 != null) {
                            if (str7.length() < 64) {
                                throw new IllegalArgumentException("String 'contentHash' is shorter than 64");
                            } else if (str7.length() > 64) {
                                throw new IllegalArgumentException("String 'contentHash' is longer than 64");
                            }
                        }
                        this.contentHash = str11;
                    } else {
                        throw new IllegalArgumentException("String 'rev' does not match pattern");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'serverModified' is null");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'clientModified' is null");
            }
        } else {
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        }
    }

    public FileMetadata(String str, String str2, Date date, Date date2, String str3, long j) {
        this(str, str2, date, date2, str3, j, null, null, null, null, null, null, null, null, null);
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.f94id;
    }

    public Date getClientModified() {
        return this.clientModified;
    }

    public Date getServerModified() {
        return this.serverModified;
    }

    public String getRev() {
        return this.rev;
    }

    public long getSize() {
        return this.size;
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

    public MediaInfo getMediaInfo() {
        return this.mediaInfo;
    }

    public SymlinkInfo getSymlinkInfo() {
        return this.symlinkInfo;
    }

    public FileSharingInfo getSharingInfo() {
        return this.sharingInfo;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return this.propertyGroups;
    }

    public Boolean getHasExplicitSharedMembers() {
        return this.hasExplicitSharedMembers;
    }

    public String getContentHash() {
        return this.contentHash;
    }

    public static Builder newBuilder(String str, String str2, Date date, Date date2, String str3, long j) {
        Builder builder = new Builder(str, str2, date, date2, str3, j);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.f94id, this.clientModified, this.serverModified, this.rev, Long.valueOf(this.size), this.mediaInfo, this.symlinkInfo, this.sharingInfo, this.propertyGroups, this.hasExplicitSharedMembers, this.contentHash});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:77:0x00ee, code lost:
        if (r2.equals(r7) == false) goto L_0x00f1;
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
            if (r2 == 0) goto L_0x00f3
            com.dropbox.core.v2.files.FileMetadata r7 = (com.dropbox.core.p005v2.files.FileMetadata) r7
            java.lang.String r2 = r6.name
            java.lang.String r3 = r7.name
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r6.name
            java.lang.String r3 = r7.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x0028:
            java.lang.String r2 = r6.f94id
            java.lang.String r3 = r7.f94id
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x0034:
            java.util.Date r2 = r6.clientModified
            java.util.Date r3 = r7.clientModified
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x0040:
            java.util.Date r2 = r6.serverModified
            java.util.Date r3 = r7.serverModified
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x004c:
            java.lang.String r2 = r6.rev
            java.lang.String r3 = r7.rev
            if (r2 == r3) goto L_0x0058
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x0058:
            long r2 = r6.size
            long r4 = r7.size
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x00f1
            java.lang.String r2 = r6.pathLower
            java.lang.String r3 = r7.pathLower
            if (r2 == r3) goto L_0x0074
            java.lang.String r2 = r6.pathLower
            if (r2 == 0) goto L_0x00f1
            java.lang.String r2 = r6.pathLower
            java.lang.String r3 = r7.pathLower
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x0074:
            java.lang.String r2 = r6.pathDisplay
            java.lang.String r3 = r7.pathDisplay
            if (r2 == r3) goto L_0x0088
            java.lang.String r2 = r6.pathDisplay
            if (r2 == 0) goto L_0x00f1
            java.lang.String r2 = r6.pathDisplay
            java.lang.String r3 = r7.pathDisplay
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x0088:
            java.lang.String r2 = r6.parentSharedFolderId
            java.lang.String r3 = r7.parentSharedFolderId
            if (r2 == r3) goto L_0x009c
            java.lang.String r2 = r6.parentSharedFolderId
            if (r2 == 0) goto L_0x00f1
            java.lang.String r2 = r6.parentSharedFolderId
            java.lang.String r3 = r7.parentSharedFolderId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x009c:
            com.dropbox.core.v2.files.MediaInfo r2 = r6.mediaInfo
            com.dropbox.core.v2.files.MediaInfo r3 = r7.mediaInfo
            if (r2 == r3) goto L_0x00aa
            if (r2 == 0) goto L_0x00f1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x00aa:
            com.dropbox.core.v2.files.SymlinkInfo r2 = r6.symlinkInfo
            com.dropbox.core.v2.files.SymlinkInfo r3 = r7.symlinkInfo
            if (r2 == r3) goto L_0x00b8
            if (r2 == 0) goto L_0x00f1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x00b8:
            com.dropbox.core.v2.files.FileSharingInfo r2 = r6.sharingInfo
            com.dropbox.core.v2.files.FileSharingInfo r3 = r7.sharingInfo
            if (r2 == r3) goto L_0x00c6
            if (r2 == 0) goto L_0x00f1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x00c6:
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r2 = r6.propertyGroups
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r3 = r7.propertyGroups
            if (r2 == r3) goto L_0x00d4
            if (r2 == 0) goto L_0x00f1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x00d4:
            java.lang.Boolean r2 = r6.hasExplicitSharedMembers
            java.lang.Boolean r3 = r7.hasExplicitSharedMembers
            if (r2 == r3) goto L_0x00e2
            if (r2 == 0) goto L_0x00f1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00f1
        L_0x00e2:
            java.lang.String r2 = r6.contentHash
            java.lang.String r7 = r7.contentHash
            if (r2 == r7) goto L_0x00f2
            if (r2 == 0) goto L_0x00f1
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x00f1
            goto L_0x00f2
        L_0x00f1:
            r0 = 0
        L_0x00f2:
            return r0
        L_0x00f3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.FileMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
