package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.InsufficientPlan */
public class InsufficientPlan {
    protected final String message;
    protected final String upsellUrl;

    /* renamed from: com.dropbox.core.v2.sharing.InsufficientPlan$Serializer */
    static class Serializer extends StructSerializer<InsufficientPlan> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(InsufficientPlan insufficientPlan, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("message");
            StoneSerializers.string().serialize(insufficientPlan.message, jsonGenerator);
            if (insufficientPlan.upsellUrl != null) {
                jsonGenerator.writeFieldName("upsell_url");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(insufficientPlan.upsellUrl, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public InsufficientPlan deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("message".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("upsell_url".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    InsufficientPlan insufficientPlan = new InsufficientPlan(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(insufficientPlan, insufficientPlan.toStringMultiline());
                    return insufficientPlan;
                }
                throw new JsonParseException(jsonParser, "Required field \"message\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public InsufficientPlan(String str, String str2) {
        if (str != null) {
            this.message = str;
            this.upsellUrl = str2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'message' is null");
    }

    public InsufficientPlan(String str) {
        this(str, null);
    }

    public String getMessage() {
        return this.message;
    }

    public String getUpsellUrl() {
        return this.upsellUrl;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.message, this.upsellUrl});
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
            com.dropbox.core.v2.sharing.InsufficientPlan r5 = (com.dropbox.core.p005v2.sharing.InsufficientPlan) r5
            java.lang.String r2 = r4.message
            java.lang.String r3 = r5.message
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.upsellUrl
            java.lang.String r5 = r5.upsellUrl
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.InsufficientPlan.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
