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

/* renamed from: com.dropbox.core.v2.teamlog.SsoAddCertDetails */
public class SsoAddCertDetails {
    protected final Certificate certificateDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.SsoAddCertDetails$Serializer */
    static class Serializer extends StructSerializer<SsoAddCertDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SsoAddCertDetails ssoAddCertDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("certificate_details");
            Serializer.INSTANCE.serialize(ssoAddCertDetails.certificateDetails, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SsoAddCertDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Certificate certificate = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("certificate_details".equals(currentName)) {
                        certificate = (Certificate) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (certificate != null) {
                    SsoAddCertDetails ssoAddCertDetails = new SsoAddCertDetails(certificate);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(ssoAddCertDetails, ssoAddCertDetails.toStringMultiline());
                    return ssoAddCertDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"certificate_details\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SsoAddCertDetails(Certificate certificate) {
        if (certificate != null) {
            this.certificateDetails = certificate;
            return;
        }
        throw new IllegalArgumentException("Required value for 'certificateDetails' is null");
    }

    public Certificate getCertificateDetails() {
        return this.certificateDetails;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.certificateDetails});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SsoAddCertDetails ssoAddCertDetails = (SsoAddCertDetails) obj;
        Certificate certificate = this.certificateDetails;
        Certificate certificate2 = ssoAddCertDetails.certificateDetails;
        if (certificate != certificate2 && !certificate.equals(certificate2)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
