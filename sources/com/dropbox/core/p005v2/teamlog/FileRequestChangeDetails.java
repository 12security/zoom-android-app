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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.teamlog.FileRequestChangeDetails */
public class FileRequestChangeDetails {
    protected final String fileRequestId;
    protected final FileRequestDetails newDetails;
    protected final FileRequestDetails previousDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestChangeDetails$Builder */
    public static class Builder {
        protected String fileRequestId;
        protected final FileRequestDetails newDetails;
        protected FileRequestDetails previousDetails;

        protected Builder(FileRequestDetails fileRequestDetails) {
            if (fileRequestDetails != null) {
                this.newDetails = fileRequestDetails;
                this.fileRequestId = null;
                this.previousDetails = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newDetails' is null");
        }

        public Builder withFileRequestId(String str) {
            if (str != null) {
                if (str.length() < 1) {
                    throw new IllegalArgumentException("String 'fileRequestId' is shorter than 1");
                } else if (!Pattern.matches("[-_0-9a-zA-Z]+", str)) {
                    throw new IllegalArgumentException("String 'fileRequestId' does not match pattern");
                }
            }
            this.fileRequestId = str;
            return this;
        }

        public Builder withPreviousDetails(FileRequestDetails fileRequestDetails) {
            this.previousDetails = fileRequestDetails;
            return this;
        }

        public FileRequestChangeDetails build() {
            return new FileRequestChangeDetails(this.newDetails, this.fileRequestId, this.previousDetails);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestChangeDetails$Serializer */
    static class Serializer extends StructSerializer<FileRequestChangeDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequestChangeDetails fileRequestChangeDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_details");
            Serializer.INSTANCE.serialize(fileRequestChangeDetails.newDetails, jsonGenerator);
            if (fileRequestChangeDetails.fileRequestId != null) {
                jsonGenerator.writeFieldName("file_request_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequestChangeDetails.fileRequestId, jsonGenerator);
            }
            if (fileRequestChangeDetails.previousDetails != null) {
                jsonGenerator.writeFieldName("previous_details");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileRequestChangeDetails.previousDetails, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRequestChangeDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            FileRequestDetails fileRequestDetails = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                FileRequestDetails fileRequestDetails2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_details".equals(currentName)) {
                        fileRequestDetails = (FileRequestDetails) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("file_request_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("previous_details".equals(currentName)) {
                        fileRequestDetails2 = (FileRequestDetails) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (fileRequestDetails != null) {
                    FileRequestChangeDetails fileRequestChangeDetails = new FileRequestChangeDetails(fileRequestDetails, str2, fileRequestDetails2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileRequestChangeDetails, fileRequestChangeDetails.toStringMultiline());
                    return fileRequestChangeDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_details\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRequestChangeDetails(FileRequestDetails fileRequestDetails, String str, FileRequestDetails fileRequestDetails2) {
        if (str != null) {
            if (str.length() < 1) {
                throw new IllegalArgumentException("String 'fileRequestId' is shorter than 1");
            } else if (!Pattern.matches("[-_0-9a-zA-Z]+", str)) {
                throw new IllegalArgumentException("String 'fileRequestId' does not match pattern");
            }
        }
        this.fileRequestId = str;
        this.previousDetails = fileRequestDetails2;
        if (fileRequestDetails != null) {
            this.newDetails = fileRequestDetails;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newDetails' is null");
    }

    public FileRequestChangeDetails(FileRequestDetails fileRequestDetails) {
        this(fileRequestDetails, null, null);
    }

    public FileRequestDetails getNewDetails() {
        return this.newDetails;
    }

    public String getFileRequestId() {
        return this.fileRequestId;
    }

    public FileRequestDetails getPreviousDetails() {
        return this.previousDetails;
    }

    public static Builder newBuilder(FileRequestDetails fileRequestDetails) {
        return new Builder(fileRequestDetails);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.fileRequestId, this.previousDetails, this.newDetails});
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
            com.dropbox.core.v2.teamlog.FileRequestChangeDetails r5 = (com.dropbox.core.p005v2.teamlog.FileRequestChangeDetails) r5
            com.dropbox.core.v2.teamlog.FileRequestDetails r2 = r4.newDetails
            com.dropbox.core.v2.teamlog.FileRequestDetails r3 = r5.newDetails
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.lang.String r2 = r4.fileRequestId
            java.lang.String r3 = r5.fileRequestId
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            com.dropbox.core.v2.teamlog.FileRequestDetails r2 = r4.previousDetails
            com.dropbox.core.v2.teamlog.FileRequestDetails r5 = r5.previousDetails
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.FileRequestChangeDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
