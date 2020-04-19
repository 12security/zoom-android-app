package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.kubi.KubiContract;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.UploadWriteFailed */
public class UploadWriteFailed {
    protected final WriteError reason;
    protected final String uploadSessionId;

    /* renamed from: com.dropbox.core.v2.files.UploadWriteFailed$Serializer */
    static class Serializer extends StructSerializer<UploadWriteFailed> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadWriteFailed uploadWriteFailed, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(KubiContract.EXTRA_REASON);
            Serializer.INSTANCE.serialize(uploadWriteFailed.reason, jsonGenerator);
            jsonGenerator.writeFieldName("upload_session_id");
            StoneSerializers.string().serialize(uploadWriteFailed.uploadSessionId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UploadWriteFailed deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            WriteError writeError = null;
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
                    if (KubiContract.EXTRA_REASON.equals(currentName)) {
                        writeError = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("upload_session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (writeError == null) {
                    throw new JsonParseException(jsonParser, "Required field \"reason\" missing.");
                } else if (str2 != null) {
                    UploadWriteFailed uploadWriteFailed = new UploadWriteFailed(writeError, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(uploadWriteFailed, uploadWriteFailed.toStringMultiline());
                    return uploadWriteFailed;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"upload_session_id\" missing.");
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

    public UploadWriteFailed(WriteError writeError, String str) {
        if (writeError != null) {
            this.reason = writeError;
            if (str != null) {
                this.uploadSessionId = str;
                return;
            }
            throw new IllegalArgumentException("Required value for 'uploadSessionId' is null");
        }
        throw new IllegalArgumentException("Required value for 'reason' is null");
    }

    public WriteError getReason() {
        return this.reason;
    }

    public String getUploadSessionId() {
        return this.uploadSessionId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.reason, this.uploadSessionId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.files.UploadWriteFailed r5 = (com.dropbox.core.p005v2.files.UploadWriteFailed) r5
            com.dropbox.core.v2.files.WriteError r2 = r4.reason
            com.dropbox.core.v2.files.WriteError r3 = r5.reason
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.uploadSessionId
            java.lang.String r5 = r5.uploadSessionId
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.UploadWriteFailed.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
