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

/* renamed from: com.dropbox.core.v2.teamlog.SsoChangeCertDetails */
public class SsoChangeCertDetails {
    protected final Certificate newCertificateDetails;
    protected final Certificate previousCertificateDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.SsoChangeCertDetails$Serializer */
    static class Serializer extends StructSerializer<SsoChangeCertDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SsoChangeCertDetails ssoChangeCertDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_certificate_details");
            Serializer.INSTANCE.serialize(ssoChangeCertDetails.newCertificateDetails, jsonGenerator);
            if (ssoChangeCertDetails.previousCertificateDetails != null) {
                jsonGenerator.writeFieldName("previous_certificate_details");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(ssoChangeCertDetails.previousCertificateDetails, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SsoChangeCertDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Certificate certificate = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Certificate certificate2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_certificate_details".equals(currentName)) {
                        certificate = (Certificate) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_certificate_details".equals(currentName)) {
                        certificate2 = (Certificate) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (certificate != null) {
                    SsoChangeCertDetails ssoChangeCertDetails = new SsoChangeCertDetails(certificate, certificate2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(ssoChangeCertDetails, ssoChangeCertDetails.toStringMultiline());
                    return ssoChangeCertDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_certificate_details\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SsoChangeCertDetails(Certificate certificate, Certificate certificate2) {
        this.previousCertificateDetails = certificate2;
        if (certificate != null) {
            this.newCertificateDetails = certificate;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newCertificateDetails' is null");
    }

    public SsoChangeCertDetails(Certificate certificate) {
        this(certificate, null);
    }

    public Certificate getNewCertificateDetails() {
        return this.newCertificateDetails;
    }

    public Certificate getPreviousCertificateDetails() {
        return this.previousCertificateDetails;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousCertificateDetails, this.newCertificateDetails});
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
            com.dropbox.core.v2.teamlog.SsoChangeCertDetails r5 = (com.dropbox.core.p005v2.teamlog.SsoChangeCertDetails) r5
            com.dropbox.core.v2.teamlog.Certificate r2 = r4.newCertificateDetails
            com.dropbox.core.v2.teamlog.Certificate r3 = r5.newCertificateDetails
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.teamlog.Certificate r2 = r4.previousCertificateDetails
            com.dropbox.core.v2.teamlog.Certificate r5 = r5.previousCertificateDetails
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SsoChangeCertDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
