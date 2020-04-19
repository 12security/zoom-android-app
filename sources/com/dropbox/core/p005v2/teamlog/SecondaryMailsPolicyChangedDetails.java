package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.SecondaryMailsPolicyChangedDetails */
public class SecondaryMailsPolicyChangedDetails {
    protected final SecondaryMailsPolicy newValue;
    protected final SecondaryMailsPolicy previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SecondaryMailsPolicyChangedDetails$Serializer */
    static class Serializer extends StructSerializer<SecondaryMailsPolicyChangedDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SecondaryMailsPolicyChangedDetails secondaryMailsPolicyChangedDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("previous_value");
            Serializer.INSTANCE.serialize(secondaryMailsPolicyChangedDetails.previousValue, jsonGenerator);
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(secondaryMailsPolicyChangedDetails.newValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SecondaryMailsPolicyChangedDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SecondaryMailsPolicy secondaryMailsPolicy = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SecondaryMailsPolicy secondaryMailsPolicy2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("previous_value".equals(currentName)) {
                        secondaryMailsPolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("new_value".equals(currentName)) {
                        secondaryMailsPolicy2 = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (secondaryMailsPolicy == null) {
                    throw new JsonParseException(jsonParser, "Required field \"previous_value\" missing.");
                } else if (secondaryMailsPolicy2 != null) {
                    SecondaryMailsPolicyChangedDetails secondaryMailsPolicyChangedDetails = new SecondaryMailsPolicyChangedDetails(secondaryMailsPolicy, secondaryMailsPolicy2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(secondaryMailsPolicyChangedDetails, secondaryMailsPolicyChangedDetails.toStringMultiline());
                    return secondaryMailsPolicyChangedDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
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

    public SecondaryMailsPolicyChangedDetails(SecondaryMailsPolicy secondaryMailsPolicy, SecondaryMailsPolicy secondaryMailsPolicy2) {
        if (secondaryMailsPolicy != null) {
            this.previousValue = secondaryMailsPolicy;
            if (secondaryMailsPolicy2 != null) {
                this.newValue = secondaryMailsPolicy2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newValue' is null");
        }
        throw new IllegalArgumentException("Required value for 'previousValue' is null");
    }

    public SecondaryMailsPolicy getPreviousValue() {
        return this.previousValue;
    }

    public SecondaryMailsPolicy getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousValue, this.newValue});
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
            com.dropbox.core.v2.teamlog.SecondaryMailsPolicyChangedDetails r5 = (com.dropbox.core.p005v2.teamlog.SecondaryMailsPolicyChangedDetails) r5
            com.dropbox.core.v2.teamlog.SecondaryMailsPolicy r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.SecondaryMailsPolicy r3 = r5.previousValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.teamlog.SecondaryMailsPolicy r2 = r4.newValue
            com.dropbox.core.v2.teamlog.SecondaryMailsPolicy r5 = r5.newValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SecondaryMailsPolicyChangedDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
