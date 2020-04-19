package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppStatus */
public class RevokeLinkedAppStatus {
    protected final RevokeLinkedAppError errorType;
    protected final boolean success;

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppStatus$Serializer */
    static class Serializer extends StructSerializer<RevokeLinkedAppStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeLinkedAppStatus revokeLinkedAppStatus, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.SUCCESS);
            StoneSerializers.boolean_().serialize(Boolean.valueOf(revokeLinkedAppStatus.success), jsonGenerator);
            if (revokeLinkedAppStatus.errorType != null) {
                jsonGenerator.writeFieldName("error_type");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(revokeLinkedAppStatus.errorType, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeLinkedAppStatus deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                RevokeLinkedAppError revokeLinkedAppError = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.SUCCESS.equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("error_type".equals(currentName)) {
                        revokeLinkedAppError = (RevokeLinkedAppError) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool != null) {
                    RevokeLinkedAppStatus revokeLinkedAppStatus = new RevokeLinkedAppStatus(bool.booleanValue(), revokeLinkedAppError);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeLinkedAppStatus, revokeLinkedAppStatus.toStringMultiline());
                    return revokeLinkedAppStatus;
                }
                throw new JsonParseException(jsonParser, "Required field \"success\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RevokeLinkedAppStatus(boolean z, RevokeLinkedAppError revokeLinkedAppError) {
        this.success = z;
        this.errorType = revokeLinkedAppError;
    }

    public RevokeLinkedAppStatus(boolean z) {
        this(z, null);
    }

    public boolean getSuccess() {
        return this.success;
    }

    public RevokeLinkedAppError getErrorType() {
        return this.errorType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Boolean.valueOf(this.success), this.errorType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
        if (r2.equals(r5) == false) goto L_0x002d;
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
            if (r2 == 0) goto L_0x002f
            com.dropbox.core.v2.team.RevokeLinkedAppStatus r5 = (com.dropbox.core.p005v2.team.RevokeLinkedAppStatus) r5
            boolean r2 = r4.success
            boolean r3 = r5.success
            if (r2 != r3) goto L_0x002d
            com.dropbox.core.v2.team.RevokeLinkedAppError r2 = r4.errorType
            com.dropbox.core.v2.team.RevokeLinkedAppError r5 = r5.errorType
            if (r2 == r5) goto L_0x002e
            if (r2 == 0) goto L_0x002d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x002d
            goto L_0x002e
        L_0x002d:
            r0 = 0
        L_0x002e:
            return r0
        L_0x002f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.RevokeLinkedAppStatus.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
