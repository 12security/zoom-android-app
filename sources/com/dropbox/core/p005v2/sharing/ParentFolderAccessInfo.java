package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.ParentFolderAccessInfo */
public class ParentFolderAccessInfo {
    protected final String folderName;
    protected final String path;
    protected final List<MemberPermission> permissions;
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.ParentFolderAccessInfo$Serializer */
    static class Serializer extends StructSerializer<ParentFolderAccessInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ParentFolderAccessInfo parentFolderAccessInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("folder_name");
            StoneSerializers.string().serialize(parentFolderAccessInfo.folderName, jsonGenerator);
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(parentFolderAccessInfo.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("permissions");
            StoneSerializers.list(Serializer.INSTANCE).serialize(parentFolderAccessInfo.permissions, jsonGenerator);
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(parentFolderAccessInfo.path, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ParentFolderAccessInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                List list = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("folder_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("shared_folder_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("permissions".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("path".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"folder_name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
                } else if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"permissions\" missing.");
                } else if (str4 != null) {
                    ParentFolderAccessInfo parentFolderAccessInfo = new ParentFolderAccessInfo(str2, str3, list, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(parentFolderAccessInfo, parentFolderAccessInfo.toStringMultiline());
                    return parentFolderAccessInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
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

    public ParentFolderAccessInfo(String str, String str2, List<MemberPermission> list, String str3) {
        if (str != null) {
            this.folderName = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str2)) {
                this.sharedFolderId = str2;
                if (list != null) {
                    for (MemberPermission memberPermission : list) {
                        if (memberPermission == null) {
                            throw new IllegalArgumentException("An item in list 'permissions' is null");
                        }
                    }
                    this.permissions = list;
                    if (str3 != null) {
                        this.path = str3;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'path' is null");
                }
                throw new IllegalArgumentException("Required value for 'permissions' is null");
            } else {
                throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'folderName' is null");
        }
    }

    public String getFolderName() {
        return this.folderName;
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public List<MemberPermission> getPermissions() {
        return this.permissions;
    }

    public String getPath() {
        return this.path;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.folderName, this.sharedFolderId, this.permissions, this.path});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0046, code lost:
        if (r2.equals(r5) == false) goto L_0x0049;
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
            if (r2 == 0) goto L_0x004b
            com.dropbox.core.v2.sharing.ParentFolderAccessInfo r5 = (com.dropbox.core.p005v2.sharing.ParentFolderAccessInfo) r5
            java.lang.String r2 = r4.folderName
            java.lang.String r3 = r5.folderName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0024:
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x0030:
            java.util.List<com.dropbox.core.v2.sharing.MemberPermission> r2 = r4.permissions
            java.util.List<com.dropbox.core.v2.sharing.MemberPermission> r3 = r5.permissions
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x003c:
            java.lang.String r2 = r4.path
            java.lang.String r5 = r5.path
            if (r2 == r5) goto L_0x004a
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            return r0
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ParentFolderAccessInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
