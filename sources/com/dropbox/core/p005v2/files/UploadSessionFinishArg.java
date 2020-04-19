package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishArg */
public class UploadSessionFinishArg {
    protected final CommitInfo commit;
    protected final UploadSessionCursor cursor;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishArg$Serializer */
    static class Serializer extends StructSerializer<UploadSessionFinishArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionFinishArg uploadSessionFinishArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("cursor");
            Serializer.INSTANCE.serialize(uploadSessionFinishArg.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("commit");
            Serializer.INSTANCE.serialize(uploadSessionFinishArg.commit, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UploadSessionFinishArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UploadSessionCursor uploadSessionCursor = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                CommitInfo commitInfo = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("cursor".equals(currentName)) {
                        uploadSessionCursor = (UploadSessionCursor) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("commit".equals(currentName)) {
                        commitInfo = (CommitInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (uploadSessionCursor == null) {
                    throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
                } else if (commitInfo != null) {
                    UploadSessionFinishArg uploadSessionFinishArg = new UploadSessionFinishArg(uploadSessionCursor, commitInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(uploadSessionFinishArg, uploadSessionFinishArg.toStringMultiline());
                    return uploadSessionFinishArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"commit\" missing.");
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

    public UploadSessionFinishArg(UploadSessionCursor uploadSessionCursor, CommitInfo commitInfo) {
        if (uploadSessionCursor != null) {
            this.cursor = uploadSessionCursor;
            if (commitInfo != null) {
                this.commit = commitInfo;
                return;
            }
            throw new IllegalArgumentException("Required value for 'commit' is null");
        }
        throw new IllegalArgumentException("Required value for 'cursor' is null");
    }

    public UploadSessionCursor getCursor() {
        return this.cursor;
    }

    public CommitInfo getCommit() {
        return this.commit;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.cursor, this.commit});
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
            com.dropbox.core.v2.files.UploadSessionFinishArg r5 = (com.dropbox.core.p005v2.files.UploadSessionFinishArg) r5
            com.dropbox.core.v2.files.UploadSessionCursor r2 = r4.cursor
            com.dropbox.core.v2.files.UploadSessionCursor r3 = r5.cursor
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.files.CommitInfo r2 = r4.commit
            com.dropbox.core.v2.files.CommitInfo r5 = r5.commit
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.UploadSessionFinishArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
