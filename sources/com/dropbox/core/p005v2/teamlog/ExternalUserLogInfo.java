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

/* renamed from: com.dropbox.core.v2.teamlog.ExternalUserLogInfo */
public class ExternalUserLogInfo {
    protected final IdentifierType identifierType;
    protected final String userIdentifier;

    /* renamed from: com.dropbox.core.v2.teamlog.ExternalUserLogInfo$Serializer */
    static class Serializer extends StructSerializer<ExternalUserLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ExternalUserLogInfo externalUserLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user_identifier");
            StoneSerializers.string().serialize(externalUserLogInfo.userIdentifier, jsonGenerator);
            jsonGenerator.writeFieldName("identifier_type");
            Serializer.INSTANCE.serialize(externalUserLogInfo.identifierType, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ExternalUserLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                IdentifierType identifierType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user_identifier".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("identifier_type".equals(currentName)) {
                        identifierType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user_identifier\" missing.");
                } else if (identifierType != null) {
                    ExternalUserLogInfo externalUserLogInfo = new ExternalUserLogInfo(str2, identifierType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(externalUserLogInfo, externalUserLogInfo.toStringMultiline());
                    return externalUserLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"identifier_type\" missing.");
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

    public ExternalUserLogInfo(String str, IdentifierType identifierType2) {
        if (str != null) {
            this.userIdentifier = str;
            if (identifierType2 != null) {
                this.identifierType = identifierType2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'identifierType' is null");
        }
        throw new IllegalArgumentException("Required value for 'userIdentifier' is null");
    }

    public String getUserIdentifier() {
        return this.userIdentifier;
    }

    public IdentifierType getIdentifierType() {
        return this.identifierType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.userIdentifier, this.identifierType});
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
            com.dropbox.core.v2.teamlog.ExternalUserLogInfo r5 = (com.dropbox.core.p005v2.teamlog.ExternalUserLogInfo) r5
            java.lang.String r2 = r4.userIdentifier
            java.lang.String r3 = r5.userIdentifier
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.teamlog.IdentifierType r2 = r4.identifierType
            com.dropbox.core.v2.teamlog.IdentifierType r5 = r5.identifierType
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.ExternalUserLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
