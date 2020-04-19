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

/* renamed from: com.dropbox.core.v2.teamlog.ShowcaseChangeExternalSharingPolicyDetails */
public class ShowcaseChangeExternalSharingPolicyDetails {
    protected final ShowcaseExternalSharingPolicy newValue;
    protected final ShowcaseExternalSharingPolicy previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.ShowcaseChangeExternalSharingPolicyDetails$Serializer */
    static class Serializer extends StructSerializer<ShowcaseChangeExternalSharingPolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShowcaseChangeExternalSharingPolicyDetails showcaseChangeExternalSharingPolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(showcaseChangeExternalSharingPolicyDetails.newValue, jsonGenerator);
            jsonGenerator.writeFieldName("previous_value");
            Serializer.INSTANCE.serialize(showcaseChangeExternalSharingPolicyDetails.previousValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShowcaseChangeExternalSharingPolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            ShowcaseExternalSharingPolicy showcaseExternalSharingPolicy = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ShowcaseExternalSharingPolicy showcaseExternalSharingPolicy2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        showcaseExternalSharingPolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        showcaseExternalSharingPolicy2 = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (showcaseExternalSharingPolicy == null) {
                    throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
                } else if (showcaseExternalSharingPolicy2 != null) {
                    ShowcaseChangeExternalSharingPolicyDetails showcaseChangeExternalSharingPolicyDetails = new ShowcaseChangeExternalSharingPolicyDetails(showcaseExternalSharingPolicy, showcaseExternalSharingPolicy2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(showcaseChangeExternalSharingPolicyDetails, showcaseChangeExternalSharingPolicyDetails.toStringMultiline());
                    return showcaseChangeExternalSharingPolicyDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"previous_value\" missing.");
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

    public ShowcaseChangeExternalSharingPolicyDetails(ShowcaseExternalSharingPolicy showcaseExternalSharingPolicy, ShowcaseExternalSharingPolicy showcaseExternalSharingPolicy2) {
        if (showcaseExternalSharingPolicy != null) {
            this.newValue = showcaseExternalSharingPolicy;
            if (showcaseExternalSharingPolicy2 != null) {
                this.previousValue = showcaseExternalSharingPolicy2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'previousValue' is null");
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public ShowcaseExternalSharingPolicy getNewValue() {
        return this.newValue;
    }

    public ShowcaseExternalSharingPolicy getPreviousValue() {
        return this.previousValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue, this.previousValue});
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
            com.dropbox.core.v2.teamlog.ShowcaseChangeExternalSharingPolicyDetails r5 = (com.dropbox.core.p005v2.teamlog.ShowcaseChangeExternalSharingPolicyDetails) r5
            com.dropbox.core.v2.teamlog.ShowcaseExternalSharingPolicy r2 = r4.newValue
            com.dropbox.core.v2.teamlog.ShowcaseExternalSharingPolicy r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.teamlog.ShowcaseExternalSharingPolicy r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.ShowcaseExternalSharingPolicy r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.ShowcaseChangeExternalSharingPolicyDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
