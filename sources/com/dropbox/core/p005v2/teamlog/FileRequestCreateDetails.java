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

/* renamed from: com.dropbox.core.v2.teamlog.FileRequestCreateDetails */
public class FileRequestCreateDetails {
    protected final String fileRequestId;
    protected final FileRequestDetails requestDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestCreateDetails$Builder */
    public static class Builder {
        protected String fileRequestId = null;
        protected FileRequestDetails requestDetails = null;

        protected Builder() {
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

        public Builder withRequestDetails(FileRequestDetails fileRequestDetails) {
            this.requestDetails = fileRequestDetails;
            return this;
        }

        public FileRequestCreateDetails build() {
            return new FileRequestCreateDetails(this.fileRequestId, this.requestDetails);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestCreateDetails$Serializer */
    static class Serializer extends StructSerializer<FileRequestCreateDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequestCreateDetails fileRequestCreateDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (fileRequestCreateDetails.fileRequestId != null) {
                jsonGenerator.writeFieldName("file_request_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequestCreateDetails.fileRequestId, jsonGenerator);
            }
            if (fileRequestCreateDetails.requestDetails != null) {
                jsonGenerator.writeFieldName("request_details");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileRequestCreateDetails.requestDetails, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRequestCreateDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                FileRequestDetails fileRequestDetails = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("file_request_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("request_details".equals(currentName)) {
                        fileRequestDetails = (FileRequestDetails) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                FileRequestCreateDetails fileRequestCreateDetails = new FileRequestCreateDetails(str2, fileRequestDetails);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(fileRequestCreateDetails, fileRequestCreateDetails.toStringMultiline());
                return fileRequestCreateDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRequestCreateDetails(String str, FileRequestDetails fileRequestDetails) {
        if (str != null) {
            if (str.length() < 1) {
                throw new IllegalArgumentException("String 'fileRequestId' is shorter than 1");
            } else if (!Pattern.matches("[-_0-9a-zA-Z]+", str)) {
                throw new IllegalArgumentException("String 'fileRequestId' does not match pattern");
            }
        }
        this.fileRequestId = str;
        this.requestDetails = fileRequestDetails;
    }

    public FileRequestCreateDetails() {
        this(null, null);
    }

    public String getFileRequestId() {
        return this.fileRequestId;
    }

    public FileRequestDetails getRequestDetails() {
        return this.requestDetails;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.fileRequestId, this.requestDetails});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
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
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.teamlog.FileRequestCreateDetails r5 = (com.dropbox.core.p005v2.teamlog.FileRequestCreateDetails) r5
            java.lang.String r2 = r4.fileRequestId
            java.lang.String r3 = r5.fileRequestId
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.teamlog.FileRequestDetails r2 = r4.requestDetails
            com.dropbox.core.v2.teamlog.FileRequestDetails r5 = r5.requestDetails
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.FileRequestCreateDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
