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

/* renamed from: com.dropbox.core.v2.files.FileSharingInfo */
public class FileSharingInfo extends SharingInfo {
    protected final String modifiedBy;
    protected final String parentSharedFolderId;

    /* renamed from: com.dropbox.core.v2.files.FileSharingInfo$Serializer */
    static class Serializer extends StructSerializer<FileSharingInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileSharingInfo fileSharingInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("read_only");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(fileSharingInfo.readOnly), jsonGenerator);
            jsonGenerator.writeFieldName("parent_shared_folder_id");
            StoneSerializers.string().serialize(fileSharingInfo.parentSharedFolderId, jsonGenerator);
            if (fileSharingInfo.modifiedBy != null) {
                jsonGenerator.writeFieldName("modified_by");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileSharingInfo.modifiedBy, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileSharingInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
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
                    if ("read_only".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("parent_shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("modified_by".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"read_only\" missing.");
                } else if (str2 != null) {
                    FileSharingInfo fileSharingInfo = new FileSharingInfo(bool.booleanValue(), str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileSharingInfo, fileSharingInfo.toStringMultiline());
                    return fileSharingInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"parent_shared_folder_id\" missing.");
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

    public FileSharingInfo(boolean z, String str, String str2) {
        super(z);
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'parentSharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.parentSharedFolderId = str;
            if (str2 != null) {
                if (str2.length() < 40) {
                    throw new IllegalArgumentException("String 'modifiedBy' is shorter than 40");
                } else if (str2.length() > 40) {
                    throw new IllegalArgumentException("String 'modifiedBy' is longer than 40");
                }
            }
            this.modifiedBy = str2;
        } else {
            throw new IllegalArgumentException("String 'parentSharedFolderId' does not match pattern");
        }
    }

    public FileSharingInfo(boolean z, String str) {
        this(z, str, null);
    }

    public boolean getReadOnly() {
        return this.readOnly;
    }

    public String getParentSharedFolderId() {
        return this.parentSharedFolderId;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.parentSharedFolderId, this.modifiedBy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        if (r2.equals(r5) == false) goto L_0x0039;
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
            if (r2 == 0) goto L_0x003b
            com.dropbox.core.v2.files.FileSharingInfo r5 = (com.dropbox.core.p005v2.files.FileSharingInfo) r5
            boolean r2 = r4.readOnly
            boolean r3 = r5.readOnly
            if (r2 != r3) goto L_0x0039
            java.lang.String r2 = r4.parentSharedFolderId
            java.lang.String r3 = r5.parentSharedFolderId
            if (r2 == r3) goto L_0x002a
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0039
        L_0x002a:
            java.lang.String r2 = r4.modifiedBy
            java.lang.String r5 = r5.modifiedBy
            if (r2 == r5) goto L_0x003a
            if (r2 == 0) goto L_0x0039
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r0 = 0
        L_0x003a:
            return r0
        L_0x003b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.FileSharingInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
