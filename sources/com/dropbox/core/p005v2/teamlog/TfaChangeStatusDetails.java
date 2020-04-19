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

/* renamed from: com.dropbox.core.v2.teamlog.TfaChangeStatusDetails */
public class TfaChangeStatusDetails {
    protected final TfaConfiguration newValue;
    protected final TfaConfiguration previousValue;
    protected final Boolean usedRescueCode;

    /* renamed from: com.dropbox.core.v2.teamlog.TfaChangeStatusDetails$Builder */
    public static class Builder {
        protected final TfaConfiguration newValue;
        protected TfaConfiguration previousValue;
        protected Boolean usedRescueCode;

        protected Builder(TfaConfiguration tfaConfiguration) {
            if (tfaConfiguration != null) {
                this.newValue = tfaConfiguration;
                this.previousValue = null;
                this.usedRescueCode = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newValue' is null");
        }

        public Builder withPreviousValue(TfaConfiguration tfaConfiguration) {
            this.previousValue = tfaConfiguration;
            return this;
        }

        public Builder withUsedRescueCode(Boolean bool) {
            this.usedRescueCode = bool;
            return this;
        }

        public TfaChangeStatusDetails build() {
            return new TfaChangeStatusDetails(this.newValue, this.previousValue, this.usedRescueCode);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.TfaChangeStatusDetails$Serializer */
    static class Serializer extends StructSerializer<TfaChangeStatusDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TfaChangeStatusDetails tfaChangeStatusDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(tfaChangeStatusDetails.newValue, jsonGenerator);
            if (tfaChangeStatusDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(tfaChangeStatusDetails.previousValue, jsonGenerator);
            }
            if (tfaChangeStatusDetails.usedRescueCode != null) {
                jsonGenerator.writeFieldName("used_rescue_code");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(tfaChangeStatusDetails.usedRescueCode, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TfaChangeStatusDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TfaConfiguration tfaConfiguration = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                TfaConfiguration tfaConfiguration2 = null;
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        tfaConfiguration = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        tfaConfiguration2 = (TfaConfiguration) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("used_rescue_code".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (tfaConfiguration != null) {
                    TfaChangeStatusDetails tfaChangeStatusDetails = new TfaChangeStatusDetails(tfaConfiguration, tfaConfiguration2, bool);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(tfaChangeStatusDetails, tfaChangeStatusDetails.toStringMultiline());
                    return tfaChangeStatusDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TfaChangeStatusDetails(TfaConfiguration tfaConfiguration, TfaConfiguration tfaConfiguration2, Boolean bool) {
        if (tfaConfiguration != null) {
            this.newValue = tfaConfiguration;
            this.previousValue = tfaConfiguration2;
            this.usedRescueCode = bool;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public TfaChangeStatusDetails(TfaConfiguration tfaConfiguration) {
        this(tfaConfiguration, null, null);
    }

    public TfaConfiguration getNewValue() {
        return this.newValue;
    }

    public TfaConfiguration getPreviousValue() {
        return this.previousValue;
    }

    public Boolean getUsedRescueCode() {
        return this.usedRescueCode;
    }

    public static Builder newBuilder(TfaConfiguration tfaConfiguration) {
        return new Builder(tfaConfiguration);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue, this.previousValue, this.usedRescueCode});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.teamlog.TfaChangeStatusDetails r5 = (com.dropbox.core.p005v2.teamlog.TfaChangeStatusDetails) r5
            com.dropbox.core.v2.teamlog.TfaConfiguration r2 = r4.newValue
            com.dropbox.core.v2.teamlog.TfaConfiguration r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            com.dropbox.core.v2.teamlog.TfaConfiguration r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.TfaConfiguration r3 = r5.previousValue
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            java.lang.Boolean r2 = r4.usedRescueCode
            java.lang.Boolean r5 = r5.usedRescueCode
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TfaChangeStatusDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
