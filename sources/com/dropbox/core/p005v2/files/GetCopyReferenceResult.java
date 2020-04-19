package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.BoxApiMetadata;
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
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.files.GetCopyReferenceResult */
public class GetCopyReferenceResult {
    protected final String copyReference;
    protected final Date expires;
    protected final Metadata metadata;

    /* renamed from: com.dropbox.core.v2.files.GetCopyReferenceResult$Serializer */
    static class Serializer extends StructSerializer<GetCopyReferenceResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetCopyReferenceResult getCopyReferenceResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
            Serializer.INSTANCE.serialize(getCopyReferenceResult.metadata, jsonGenerator);
            jsonGenerator.writeFieldName("copy_reference");
            StoneSerializers.string().serialize(getCopyReferenceResult.copyReference, jsonGenerator);
            jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
            StoneSerializers.timestamp().serialize(getCopyReferenceResult.expires, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetCopyReferenceResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Metadata metadata = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxApiMetadata.BOX_API_METADATA.equals(currentName)) {
                        metadata = (Metadata) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("copy_reference".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (metadata == null) {
                    throw new JsonParseException(jsonParser, "Required field \"metadata\" missing.");
                } else if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"copy_reference\" missing.");
                } else if (date != null) {
                    GetCopyReferenceResult getCopyReferenceResult = new GetCopyReferenceResult(metadata, str2, date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getCopyReferenceResult, getCopyReferenceResult.toStringMultiline());
                    return getCopyReferenceResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"expires\" missing.");
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

    public GetCopyReferenceResult(Metadata metadata2, String str, Date date) {
        if (metadata2 != null) {
            this.metadata = metadata2;
            if (str != null) {
                this.copyReference = str;
                if (date != null) {
                    this.expires = LangUtil.truncateMillis(date);
                    return;
                }
                throw new IllegalArgumentException("Required value for 'expires' is null");
            }
            throw new IllegalArgumentException("Required value for 'copyReference' is null");
        }
        throw new IllegalArgumentException("Required value for 'metadata' is null");
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public String getCopyReference() {
        return this.copyReference;
    }

    public Date getExpires() {
        return this.expires;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.metadata, this.copyReference, this.expires});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r5) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.files.GetCopyReferenceResult r5 = (com.dropbox.core.p005v2.files.GetCopyReferenceResult) r5
            com.dropbox.core.v2.files.Metadata r2 = r4.metadata
            com.dropbox.core.v2.files.Metadata r3 = r5.metadata
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            java.lang.String r2 = r4.copyReference
            java.lang.String r3 = r5.copyReference
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            java.util.Date r2 = r4.expires
            java.util.Date r5 = r5.expires
            if (r2 == r5) goto L_0x003e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.GetCopyReferenceResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
