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

/* renamed from: com.dropbox.core.v2.teamlog.AccountCaptureRelinquishAccountDetails */
public class AccountCaptureRelinquishAccountDetails {
    protected final String domainName;

    /* renamed from: com.dropbox.core.v2.teamlog.AccountCaptureRelinquishAccountDetails$Serializer */
    static class Serializer extends StructSerializer<AccountCaptureRelinquishAccountDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AccountCaptureRelinquishAccountDetails accountCaptureRelinquishAccountDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("domain_name");
            StoneSerializers.string().serialize(accountCaptureRelinquishAccountDetails.domainName, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AccountCaptureRelinquishAccountDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
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
                    if ("domain_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    AccountCaptureRelinquishAccountDetails accountCaptureRelinquishAccountDetails = new AccountCaptureRelinquishAccountDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(accountCaptureRelinquishAccountDetails, accountCaptureRelinquishAccountDetails.toStringMultiline());
                    return accountCaptureRelinquishAccountDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"domain_name\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public AccountCaptureRelinquishAccountDetails(String str) {
        if (str != null) {
            this.domainName = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'domainName' is null");
    }

    public String getDomainName() {
        return this.domainName;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.domainName});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        AccountCaptureRelinquishAccountDetails accountCaptureRelinquishAccountDetails = (AccountCaptureRelinquishAccountDetails) obj;
        String str = this.domainName;
        String str2 = accountCaptureRelinquishAccountDetails.domainName;
        if (str != str2 && !str.equals(str2)) {
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
