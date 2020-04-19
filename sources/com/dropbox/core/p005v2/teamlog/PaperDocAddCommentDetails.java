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

/* renamed from: com.dropbox.core.v2.teamlog.PaperDocAddCommentDetails */
public class PaperDocAddCommentDetails {
    protected final String commentText;
    protected final String eventUuid;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDocAddCommentDetails$Serializer */
    static class Serializer extends StructSerializer<PaperDocAddCommentDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocAddCommentDetails paperDocAddCommentDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperDocAddCommentDetails.eventUuid, jsonGenerator);
            if (paperDocAddCommentDetails.commentText != null) {
                jsonGenerator.writeFieldName("comment_text");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(paperDocAddCommentDetails.commentText, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocAddCommentDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("comment_text".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    PaperDocAddCommentDetails paperDocAddCommentDetails = new PaperDocAddCommentDetails(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocAddCommentDetails, paperDocAddCommentDetails.toStringMultiline());
                    return paperDocAddCommentDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PaperDocAddCommentDetails(String str, String str2) {
        if (str != null) {
            this.eventUuid = str;
            this.commentText = str2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public PaperDocAddCommentDetails(String str) {
        this(str, null);
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, this.commentText});
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
            com.dropbox.core.v2.teamlog.PaperDocAddCommentDetails r5 = (com.dropbox.core.p005v2.teamlog.PaperDocAddCommentDetails) r5
            java.lang.String r2 = r4.eventUuid
            java.lang.String r3 = r5.eventUuid
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.commentText
            java.lang.String r5 = r5.commentText
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PaperDocAddCommentDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
