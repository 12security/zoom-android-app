package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.SharedFolderNestDetails */
public class SharedFolderNestDetails {
    protected final String newNsPath;
    protected final String newParentNsId;
    protected final String previousNsPath;
    protected final String previousParentNsId;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedFolderNestDetails$Builder */
    public static class Builder {
        protected String newNsPath = null;
        protected String newParentNsId = null;
        protected String previousNsPath = null;
        protected String previousParentNsId = null;

        protected Builder() {
        }

        public Builder withPreviousParentNsId(String str) {
            this.previousParentNsId = str;
            return this;
        }

        public Builder withNewParentNsId(String str) {
            this.newParentNsId = str;
            return this;
        }

        public Builder withPreviousNsPath(String str) {
            this.previousNsPath = str;
            return this;
        }

        public Builder withNewNsPath(String str) {
            this.newNsPath = str;
            return this;
        }

        public SharedFolderNestDetails build() {
            return new SharedFolderNestDetails(this.previousParentNsId, this.newParentNsId, this.previousNsPath, this.newNsPath);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.SharedFolderNestDetails$Serializer */
    static class Serializer extends StructSerializer<SharedFolderNestDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedFolderNestDetails sharedFolderNestDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedFolderNestDetails.previousParentNsId != null) {
                jsonGenerator.writeFieldName("previous_parent_ns_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderNestDetails.previousParentNsId, jsonGenerator);
            }
            if (sharedFolderNestDetails.newParentNsId != null) {
                jsonGenerator.writeFieldName("new_parent_ns_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderNestDetails.newParentNsId, jsonGenerator);
            }
            if (sharedFolderNestDetails.previousNsPath != null) {
                jsonGenerator.writeFieldName("previous_ns_path");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderNestDetails.previousNsPath, jsonGenerator);
            }
            if (sharedFolderNestDetails.newNsPath != null) {
                jsonGenerator.writeFieldName("new_ns_path");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderNestDetails.newNsPath, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedFolderNestDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str4 = null;
                String str5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("previous_parent_ns_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_parent_ns_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("previous_ns_path".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_ns_path".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedFolderNestDetails sharedFolderNestDetails = new SharedFolderNestDetails(str2, str3, str4, str5);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedFolderNestDetails, sharedFolderNestDetails.toStringMultiline());
                return sharedFolderNestDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedFolderNestDetails(String str, String str2, String str3, String str4) {
        this.previousParentNsId = str;
        this.newParentNsId = str2;
        this.previousNsPath = str3;
        this.newNsPath = str4;
    }

    public SharedFolderNestDetails() {
        this(null, null, null, null);
    }

    public String getPreviousParentNsId() {
        return this.previousParentNsId;
    }

    public String getNewParentNsId() {
        return this.newParentNsId;
    }

    public String getPreviousNsPath() {
        return this.previousNsPath;
    }

    public String getNewNsPath() {
        return this.newNsPath;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousParentNsId, this.newParentNsId, this.previousNsPath, this.newNsPath});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004e, code lost:
        if (r2.equals(r5) == false) goto L_0x0051;
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
            if (r2 == 0) goto L_0x0053
            com.dropbox.core.v2.teamlog.SharedFolderNestDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedFolderNestDetails) r5
            java.lang.String r2 = r4.previousParentNsId
            java.lang.String r3 = r5.previousParentNsId
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0026:
            java.lang.String r2 = r4.newParentNsId
            java.lang.String r3 = r5.newParentNsId
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0034:
            java.lang.String r2 = r4.previousNsPath
            java.lang.String r3 = r5.previousNsPath
            if (r2 == r3) goto L_0x0042
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0042:
            java.lang.String r2 = r4.newNsPath
            java.lang.String r5 = r5.newNsPath
            if (r2 == r5) goto L_0x0052
            if (r2 == 0) goto L_0x0051
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r0 = 0
        L_0x0052:
            return r0
        L_0x0053:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedFolderNestDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
