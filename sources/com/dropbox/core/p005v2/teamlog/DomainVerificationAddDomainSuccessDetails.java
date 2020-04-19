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
import java.util.List;

/* renamed from: com.dropbox.core.v2.teamlog.DomainVerificationAddDomainSuccessDetails */
public class DomainVerificationAddDomainSuccessDetails {
    protected final List<String> domainNames;
    protected final String verificationMethod;

    /* renamed from: com.dropbox.core.v2.teamlog.DomainVerificationAddDomainSuccessDetails$Serializer */
    static class Serializer extends StructSerializer<DomainVerificationAddDomainSuccessDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DomainVerificationAddDomainSuccessDetails domainVerificationAddDomainSuccessDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("domain_names");
            StoneSerializers.list(StoneSerializers.string()).serialize(domainVerificationAddDomainSuccessDetails.domainNames, jsonGenerator);
            if (domainVerificationAddDomainSuccessDetails.verificationMethod != null) {
                jsonGenerator.writeFieldName("verification_method");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(domainVerificationAddDomainSuccessDetails.verificationMethod, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DomainVerificationAddDomainSuccessDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("domain_names".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("verification_method".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    DomainVerificationAddDomainSuccessDetails domainVerificationAddDomainSuccessDetails = new DomainVerificationAddDomainSuccessDetails(list, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(domainVerificationAddDomainSuccessDetails, domainVerificationAddDomainSuccessDetails.toStringMultiline());
                    return domainVerificationAddDomainSuccessDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"domain_names\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DomainVerificationAddDomainSuccessDetails(List<String> list, String str) {
        if (list != null) {
            for (String str2 : list) {
                if (str2 == null) {
                    throw new IllegalArgumentException("An item in list 'domainNames' is null");
                }
            }
            this.domainNames = list;
            this.verificationMethod = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'domainNames' is null");
    }

    public DomainVerificationAddDomainSuccessDetails(List<String> list) {
        this(list, null);
    }

    public List<String> getDomainNames() {
        return this.domainNames;
    }

    public String getVerificationMethod() {
        return this.verificationMethod;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.domainNames, this.verificationMethod});
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
            com.dropbox.core.v2.teamlog.DomainVerificationAddDomainSuccessDetails r5 = (com.dropbox.core.p005v2.teamlog.DomainVerificationAddDomainSuccessDetails) r5
            java.util.List<java.lang.String> r2 = r4.domainNames
            java.util.List<java.lang.String> r3 = r5.domainNames
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.String r2 = r4.verificationMethod
            java.lang.String r5 = r5.verificationMethod
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DomainVerificationAddDomainSuccessDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
