package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.FileRequestDeadline */
public class FileRequestDeadline {
    protected final String allowLateUploads;
    protected final Date deadline;

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestDeadline$Builder */
    public static class Builder {
        protected String allowLateUploads = null;
        protected Date deadline = null;

        protected Builder() {
        }

        public Builder withDeadline(Date date) {
            this.deadline = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withAllowLateUploads(String str) {
            this.allowLateUploads = str;
            return this;
        }

        public FileRequestDeadline build() {
            return new FileRequestDeadline(this.deadline, this.allowLateUploads);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.FileRequestDeadline$Serializer */
    static class Serializer extends StructSerializer<FileRequestDeadline> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequestDeadline fileRequestDeadline, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (fileRequestDeadline.deadline != null) {
                jsonGenerator.writeFieldName("deadline");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(fileRequestDeadline.deadline, jsonGenerator);
            }
            if (fileRequestDeadline.allowLateUploads != null) {
                jsonGenerator.writeFieldName("allow_late_uploads");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileRequestDeadline.allowLateUploads, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileRequestDeadline deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Date date = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("deadline".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("allow_late_uploads".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                FileRequestDeadline fileRequestDeadline = new FileRequestDeadline(date, str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(fileRequestDeadline, fileRequestDeadline.toStringMultiline());
                return fileRequestDeadline;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRequestDeadline(Date date, String str) {
        this.deadline = LangUtil.truncateMillis(date);
        this.allowLateUploads = str;
    }

    public FileRequestDeadline() {
        this(null, null);
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public String getAllowLateUploads() {
        return this.allowLateUploads;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.deadline, this.allowLateUploads});
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
            com.dropbox.core.v2.teamlog.FileRequestDeadline r5 = (com.dropbox.core.p005v2.teamlog.FileRequestDeadline) r5
            java.util.Date r2 = r4.deadline
            java.util.Date r3 = r5.deadline
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.lang.String r2 = r4.allowLateUploads
            java.lang.String r5 = r5.allowLateUploads
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.FileRequestDeadline.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
