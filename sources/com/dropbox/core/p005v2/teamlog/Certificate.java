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

/* renamed from: com.dropbox.core.v2.teamlog.Certificate */
public class Certificate {
    protected final String commonName;
    protected final String expirationDate;
    protected final String issueDate;
    protected final String issuer;
    protected final String serialNumber;
    protected final String sha1Fingerprint;
    protected final String subject;

    /* renamed from: com.dropbox.core.v2.teamlog.Certificate$Serializer */
    static class Serializer extends StructSerializer<Certificate> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(Certificate certificate, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("subject");
            StoneSerializers.string().serialize(certificate.subject, jsonGenerator);
            jsonGenerator.writeFieldName("issuer");
            StoneSerializers.string().serialize(certificate.issuer, jsonGenerator);
            jsonGenerator.writeFieldName("issue_date");
            StoneSerializers.string().serialize(certificate.issueDate, jsonGenerator);
            jsonGenerator.writeFieldName("expiration_date");
            StoneSerializers.string().serialize(certificate.expirationDate, jsonGenerator);
            jsonGenerator.writeFieldName("serial_number");
            StoneSerializers.string().serialize(certificate.serialNumber, jsonGenerator);
            jsonGenerator.writeFieldName("sha1_fingerprint");
            StoneSerializers.string().serialize(certificate.sha1Fingerprint, jsonGenerator);
            if (certificate.commonName != null) {
                jsonGenerator.writeFieldName("common_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(certificate.commonName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public Certificate deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                String str7 = null;
                String str8 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("subject".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("issuer".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("issue_date".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("expiration_date".equals(currentName)) {
                        str5 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("serial_number".equals(currentName)) {
                        str6 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("sha1_fingerprint".equals(currentName)) {
                        str7 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("common_name".equals(currentName)) {
                        str8 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"subject\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"issuer\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"issue_date\" missing.");
                } else if (str5 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"expiration_date\" missing.");
                } else if (str6 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"serial_number\" missing.");
                } else if (str7 != null) {
                    Certificate certificate = new Certificate(str2, str3, str4, str5, str6, str7, str8);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(certificate, certificate.toStringMultiline());
                    return certificate;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"sha1_fingerprint\" missing.");
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

    public Certificate(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        if (str != null) {
            this.subject = str;
            if (str2 != null) {
                this.issuer = str2;
                if (str3 != null) {
                    this.issueDate = str3;
                    if (str4 != null) {
                        this.expirationDate = str4;
                        if (str5 != null) {
                            this.serialNumber = str5;
                            if (str6 != null) {
                                this.sha1Fingerprint = str6;
                                this.commonName = str7;
                                return;
                            }
                            throw new IllegalArgumentException("Required value for 'sha1Fingerprint' is null");
                        }
                        throw new IllegalArgumentException("Required value for 'serialNumber' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'expirationDate' is null");
                }
                throw new IllegalArgumentException("Required value for 'issueDate' is null");
            }
            throw new IllegalArgumentException("Required value for 'issuer' is null");
        }
        throw new IllegalArgumentException("Required value for 'subject' is null");
    }

    public Certificate(String str, String str2, String str3, String str4, String str5, String str6) {
        this(str, str2, str3, str4, str5, str6, null);
    }

    public String getSubject() {
        return this.subject;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public String getIssueDate() {
        return this.issueDate;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getSha1Fingerprint() {
        return this.sha1Fingerprint;
    }

    public String getCommonName() {
        return this.commonName;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.subject, this.issuer, this.issueDate, this.expirationDate, this.serialNumber, this.sha1Fingerprint, this.commonName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006c, code lost:
        if (r2.equals(r5) == false) goto L_0x006f;
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
            if (r2 == 0) goto L_0x0071
            com.dropbox.core.v2.teamlog.Certificate r5 = (com.dropbox.core.p005v2.teamlog.Certificate) r5
            java.lang.String r2 = r4.subject
            java.lang.String r3 = r5.subject
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0024:
            java.lang.String r2 = r4.issuer
            java.lang.String r3 = r5.issuer
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0030:
            java.lang.String r2 = r4.issueDate
            java.lang.String r3 = r5.issueDate
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x003c:
            java.lang.String r2 = r4.expirationDate
            java.lang.String r3 = r5.expirationDate
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0048:
            java.lang.String r2 = r4.serialNumber
            java.lang.String r3 = r5.serialNumber
            if (r2 == r3) goto L_0x0054
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0054:
            java.lang.String r2 = r4.sha1Fingerprint
            java.lang.String r3 = r5.sha1Fingerprint
            if (r2 == r3) goto L_0x0060
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0060:
            java.lang.String r2 = r4.commonName
            java.lang.String r5 = r5.commonName
            if (r2 == r5) goto L_0x0070
            if (r2 == 0) goto L_0x006f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x006f
            goto L_0x0070
        L_0x006f:
            r0 = 0
        L_0x0070:
            return r0
        L_0x0071:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.Certificate.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
