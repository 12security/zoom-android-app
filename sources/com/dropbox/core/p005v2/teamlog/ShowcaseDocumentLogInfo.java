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

/* renamed from: com.dropbox.core.v2.teamlog.ShowcaseDocumentLogInfo */
public class ShowcaseDocumentLogInfo {
    protected final String showcaseId;
    protected final String showcaseTitle;

    /* renamed from: com.dropbox.core.v2.teamlog.ShowcaseDocumentLogInfo$Serializer */
    static class Serializer extends StructSerializer<ShowcaseDocumentLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShowcaseDocumentLogInfo showcaseDocumentLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("showcase_id");
            StoneSerializers.string().serialize(showcaseDocumentLogInfo.showcaseId, jsonGenerator);
            jsonGenerator.writeFieldName("showcase_title");
            StoneSerializers.string().serialize(showcaseDocumentLogInfo.showcaseTitle, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShowcaseDocumentLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("showcase_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("showcase_title".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"showcase_id\" missing.");
                } else if (str3 != null) {
                    ShowcaseDocumentLogInfo showcaseDocumentLogInfo = new ShowcaseDocumentLogInfo(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(showcaseDocumentLogInfo, showcaseDocumentLogInfo.toStringMultiline());
                    return showcaseDocumentLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"showcase_title\" missing.");
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

    public ShowcaseDocumentLogInfo(String str, String str2) {
        if (str != null) {
            this.showcaseId = str;
            if (str2 != null) {
                this.showcaseTitle = str2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'showcaseTitle' is null");
        }
        throw new IllegalArgumentException("Required value for 'showcaseId' is null");
    }

    public String getShowcaseId() {
        return this.showcaseId;
    }

    public String getShowcaseTitle() {
        return this.showcaseTitle;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.showcaseId, this.showcaseTitle});
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
            com.dropbox.core.v2.teamlog.ShowcaseDocumentLogInfo r5 = (com.dropbox.core.p005v2.teamlog.ShowcaseDocumentLogInfo) r5
            java.lang.String r2 = r4.showcaseId
            java.lang.String r3 = r5.showcaseId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.showcaseTitle
            java.lang.String r5 = r5.showcaseTitle
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.ShowcaseDocumentLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
