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
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.teamlog.FileRequestReceiveFileDetails */
public class FileRequestReceiveFileDetails {
    protected final FileRequestDetails fileRequestDetails;
    protected final String fileRequestId;
    protected final List<String> submittedFileNames;
    protected final String submitterEmail;
    protected final String submitterName;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestReceiveFileDetails$Builder */
    public static class Builder {
        protected FileRequestDetails fileRequestDetails;
        protected String fileRequestId;
        protected final List<String> submittedFileNames;
        protected String submitterEmail;
        protected String submitterName;

        protected Builder(List<String> list) {
            if (list != null) {
                for (String str : list) {
                    if (str == null) {
                        throw new IllegalArgumentException("An item in list 'submittedFileNames' is null");
                    }
                }
                this.submittedFileNames = list;
                this.fileRequestId = null;
                this.fileRequestDetails = null;
                this.submitterName = null;
                this.submitterEmail = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'submittedFileNames' is null");
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

        public Builder withFileRequestDetails(FileRequestDetails fileRequestDetails2) {
            this.fileRequestDetails = fileRequestDetails2;
            return this;
        }

        public Builder withSubmitterName(String str) {
            if (str == null || str.length() >= 1) {
                this.submitterName = str;
                return this;
            }
            throw new IllegalArgumentException("String 'submitterName' is shorter than 1");
        }

        public Builder withSubmitterEmail(String str) {
            if (str == null || str.length() <= 255) {
                this.submitterEmail = str;
                return this;
            }
            throw new IllegalArgumentException("String 'submitterEmail' is longer than 255");
        }

        public FileRequestReceiveFileDetails build() {
            FileRequestReceiveFileDetails fileRequestReceiveFileDetails = new FileRequestReceiveFileDetails(this.submittedFileNames, this.fileRequestId, this.fileRequestDetails, this.submitterName, this.submitterEmail);
            return fileRequestReceiveFileDetails;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestReceiveFileDetails$Serializer */
    static class Serializer extends StructSerializer<FileRequestReceiveFileDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequestReceiveFileDetails fileRequestReceiveFileDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("submitted_file_names");
            StoneSerializers.list(StoneSerializers.string()).serialize(fileRequestReceiveFileDetails.submittedFileNames, jsonGenerator);
            if (fileRequestReceiveFileDetails.fileRequestId != null) {
                jsonGenerator.writeFieldName("file_request_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequestReceiveFileDetails.fileRequestId, jsonGenerator);
            }
            if (fileRequestReceiveFileDetails.fileRequestDetails != null) {
                jsonGenerator.writeFieldName("file_request_details");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fileRequestReceiveFileDetails.fileRequestDetails, jsonGenerator);
            }
            if (fileRequestReceiveFileDetails.submitterName != null) {
                jsonGenerator.writeFieldName("submitter_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequestReceiveFileDetails.submitterName, jsonGenerator);
            }
            if (fileRequestReceiveFileDetails.submitterEmail != null) {
                jsonGenerator.writeFieldName("submitter_email");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequestReceiveFileDetails.submitterEmail, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRequestReceiveFileDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                String str2 = null;
                FileRequestDetails fileRequestDetails = null;
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("submitted_file_names".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("file_request_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("file_request_details".equals(currentName)) {
                        fileRequestDetails = (FileRequestDetails) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("submitter_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("submitter_email".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    FileRequestReceiveFileDetails fileRequestReceiveFileDetails = new FileRequestReceiveFileDetails(list, str2, fileRequestDetails, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileRequestReceiveFileDetails, fileRequestReceiveFileDetails.toStringMultiline());
                    return fileRequestReceiveFileDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"submitted_file_names\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRequestReceiveFileDetails(List<String> list, String str, FileRequestDetails fileRequestDetails2, String str2, String str3) {
        if (str != null) {
            if (str.length() < 1) {
                throw new IllegalArgumentException("String 'fileRequestId' is shorter than 1");
            } else if (!Pattern.matches("[-_0-9a-zA-Z]+", str)) {
                throw new IllegalArgumentException("String 'fileRequestId' does not match pattern");
            }
        }
        this.fileRequestId = str;
        this.fileRequestDetails = fileRequestDetails2;
        if (list != null) {
            for (String str4 : list) {
                if (str4 == null) {
                    throw new IllegalArgumentException("An item in list 'submittedFileNames' is null");
                }
            }
            this.submittedFileNames = list;
            if (str2 == null || str2.length() >= 1) {
                this.submitterName = str2;
                if (str3 == null || str3.length() <= 255) {
                    this.submitterEmail = str3;
                    return;
                }
                throw new IllegalArgumentException("String 'submitterEmail' is longer than 255");
            }
            throw new IllegalArgumentException("String 'submitterName' is shorter than 1");
        }
        throw new IllegalArgumentException("Required value for 'submittedFileNames' is null");
    }

    public FileRequestReceiveFileDetails(List<String> list) {
        this(list, null, null, null, null);
    }

    public List<String> getSubmittedFileNames() {
        return this.submittedFileNames;
    }

    public String getFileRequestId() {
        return this.fileRequestId;
    }

    public FileRequestDetails getFileRequestDetails() {
        return this.fileRequestDetails;
    }

    public String getSubmitterName() {
        return this.submitterName;
    }

    public String getSubmitterEmail() {
        return this.submitterEmail;
    }

    public static Builder newBuilder(List<String> list) {
        return new Builder(list);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.fileRequestId, this.fileRequestDetails, this.submittedFileNames, this.submitterName, this.submitterEmail});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005a, code lost:
        if (r2.equals(r5) == false) goto L_0x005d;
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
            if (r2 == 0) goto L_0x005f
            com.dropbox.core.v2.teamlog.FileRequestReceiveFileDetails r5 = (com.dropbox.core.p005v2.teamlog.FileRequestReceiveFileDetails) r5
            java.util.List<java.lang.String> r2 = r4.submittedFileNames
            java.util.List<java.lang.String> r3 = r5.submittedFileNames
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0024:
            java.lang.String r2 = r4.fileRequestId
            java.lang.String r3 = r5.fileRequestId
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x005d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0032:
            com.dropbox.core.v2.teamlog.FileRequestDetails r2 = r4.fileRequestDetails
            com.dropbox.core.v2.teamlog.FileRequestDetails r3 = r5.fileRequestDetails
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x005d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0040:
            java.lang.String r2 = r4.submitterName
            java.lang.String r3 = r5.submitterName
            if (r2 == r3) goto L_0x004e
            if (r2 == 0) goto L_0x005d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x004e:
            java.lang.String r2 = r4.submitterEmail
            java.lang.String r5 = r5.submitterEmail
            if (r2 == r5) goto L_0x005e
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r0 = 0
        L_0x005e:
            return r0
        L_0x005f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.FileRequestReceiveFileDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
