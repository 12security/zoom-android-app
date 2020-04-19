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
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.FolderSharingInfo */
public class FolderSharingInfo extends SharingInfo {
    protected final boolean noAccess;
    protected final String parentSharedFolderId;
    protected final String sharedFolderId;
    protected final boolean traverseOnly;

    /* renamed from: com.dropbox.core.v2.files.FolderSharingInfo$Builder */
    public static class Builder {
        protected boolean noAccess = false;
        protected String parentSharedFolderId = null;
        protected final boolean readOnly;
        protected String sharedFolderId = null;
        protected boolean traverseOnly = false;

        protected Builder(boolean z) {
            this.readOnly = z;
        }

        public Builder withParentSharedFolderId(String str) {
            if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.parentSharedFolderId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
        }

        public Builder withSharedFolderId(String str) {
            if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.sharedFolderId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }

        public Builder withTraverseOnly(Boolean bool) {
            if (bool != null) {
                this.traverseOnly = bool.booleanValue();
            } else {
                this.traverseOnly = false;
            }
            return this;
        }

        public Builder withNoAccess(Boolean bool) {
            if (bool != null) {
                this.noAccess = bool.booleanValue();
            } else {
                this.noAccess = false;
            }
            return this;
        }

        public FolderSharingInfo build() {
            FolderSharingInfo folderSharingInfo = new FolderSharingInfo(this.readOnly, this.parentSharedFolderId, this.sharedFolderId, this.traverseOnly, this.noAccess);
            return folderSharingInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.FolderSharingInfo$Serializer */
    static class Serializer extends StructSerializer<FolderSharingInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FolderSharingInfo folderSharingInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("read_only");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(folderSharingInfo.readOnly), jsonGenerator);
            if (folderSharingInfo.parentSharedFolderId != null) {
                jsonGenerator.writeFieldName("parent_shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderSharingInfo.parentSharedFolderId, jsonGenerator);
            }
            if (folderSharingInfo.sharedFolderId != null) {
                jsonGenerator.writeFieldName("shared_folder_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(folderSharingInfo.sharedFolderId, jsonGenerator);
            }
            jsonGenerator.writeFieldName("traverse_only");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(folderSharingInfo.traverseOnly), jsonGenerator);
            jsonGenerator.writeFieldName("no_access");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(folderSharingInfo.noAccess), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FolderSharingInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                Boolean valueOf2 = Boolean.valueOf(false);
                String str2 = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("read_only".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("shared_folder_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("traverse_only".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("no_access".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool != null) {
                    FolderSharingInfo folderSharingInfo = new FolderSharingInfo(bool.booleanValue(), str2, str3, valueOf.booleanValue(), valueOf2.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(folderSharingInfo, folderSharingInfo.toStringMultiline());
                    return folderSharingInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"read_only\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FolderSharingInfo(boolean z, String str, String str2, boolean z2, boolean z3) {
        super(z);
        if (str == null || Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.parentSharedFolderId = str;
            if (str2 == null || Pattern.matches("[-_0-9a-zA-Z:]+", str2)) {
                this.sharedFolderId = str2;
                this.traverseOnly = z2;
                this.noAccess = z3;
                return;
            }
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }
        throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
    }

    public FolderSharingInfo(boolean z) {
        this(z, null, null, false, false);
    }

    public boolean getReadOnly() {
        return this.readOnly;
    }

    public String getParentSharedFolderId() {
        return this.parentSharedFolderId;
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public boolean getTraverseOnly() {
        return this.traverseOnly;
    }

    public boolean getNoAccess() {
        return this.noAccess;
    }

    public static Builder newBuilder(boolean z) {
        return new Builder(z);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.parentSharedFolderId, this.sharedFolderId, Boolean.valueOf(this.traverseOnly), Boolean.valueOf(this.noAccess)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
        if (r2.equals(r3) == false) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0044, code lost:
        if (r4.noAccess != r5.noAccess) goto L_0x0047;
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
            if (r2 == 0) goto L_0x0049
            com.dropbox.core.v2.files.FolderSharingInfo r5 = (com.dropbox.core.p005v2.files.FolderSharingInfo) r5
            boolean r2 = r4.readOnly
            boolean r3 = r5.readOnly
            if (r2 != r3) goto L_0x0047
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            if (r2 == r3) goto L_0x002c
            if (r2 == 0) goto L_0x0047
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x002c:
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x003a
            if (r2 == 0) goto L_0x0047
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x003a:
            boolean r2 = r4.traverseOnly
            boolean r3 = r5.traverseOnly
            if (r2 != r3) goto L_0x0047
            boolean r2 = r4.noAccess
            boolean r5 = r5.noAccess
            if (r2 != r5) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r0 = 0
        L_0x0048:
            return r0
        L_0x0049:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.FolderSharingInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
