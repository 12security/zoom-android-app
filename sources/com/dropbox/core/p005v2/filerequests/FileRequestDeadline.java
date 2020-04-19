package com.dropbox.core.p005v2.filerequests;

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

/* renamed from: com.dropbox.core.v2.filerequests.FileRequestDeadline */
public class FileRequestDeadline {
    protected final GracePeriod allowLateUploads;
    protected final Date deadline;

    /* renamed from: com.dropbox.core.v2.filerequests.FileRequestDeadline$Serializer */
    static class Serializer extends StructSerializer<FileRequestDeadline> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileRequestDeadline fileRequestDeadline, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("deadline");
            StoneSerializers.timestamp().serialize(fileRequestDeadline.deadline, jsonGenerator);
            if (fileRequestDeadline.allowLateUploads != null) {
                jsonGenerator.writeFieldName("allow_late_uploads");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(fileRequestDeadline.allowLateUploads, jsonGenerator);
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
                GracePeriod gracePeriod = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("deadline".equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else if ("allow_late_uploads".equals(currentName)) {
                        gracePeriod = (GracePeriod) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (date != null) {
                    FileRequestDeadline fileRequestDeadline = new FileRequestDeadline(date, gracePeriod);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fileRequestDeadline, fileRequestDeadline.toStringMultiline());
                    return fileRequestDeadline;
                }
                throw new JsonParseException(jsonParser, "Required field \"deadline\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileRequestDeadline(Date date, GracePeriod gracePeriod) {
        if (date != null) {
            this.deadline = LangUtil.truncateMillis(date);
            this.allowLateUploads = gracePeriod;
            return;
        }
        throw new IllegalArgumentException("Required value for 'deadline' is null");
    }

    public FileRequestDeadline(Date date) {
        this(date, null);
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public GracePeriod getAllowLateUploads() {
        return this.allowLateUploads;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.deadline, this.allowLateUploads});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.filerequests.FileRequestDeadline r5 = (com.dropbox.core.p005v2.filerequests.FileRequestDeadline) r5
            java.util.Date r2 = r4.deadline
            java.util.Date r3 = r5.deadline
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.filerequests.GracePeriod r2 = r4.allowLateUploads
            com.dropbox.core.v2.filerequests.GracePeriod r5 = r5.allowLateUploads
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.filerequests.FileRequestDeadline.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
