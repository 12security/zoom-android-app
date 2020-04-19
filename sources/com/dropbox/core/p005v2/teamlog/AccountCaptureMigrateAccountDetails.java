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

/* renamed from: com.dropbox.core.v2.teamlog.AccountCaptureMigrateAccountDetails */
public class AccountCaptureMigrateAccountDetails {
    protected final String domainName;

    /* renamed from: com.dropbox.core.v2.teamlog.AccountCaptureMigrateAccountDetails$Serializer */
    static class Serializer extends StructSerializer<AccountCaptureMigrateAccountDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AccountCaptureMigrateAccountDetails accountCaptureMigrateAccountDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("domain_name");
            StoneSerializers.string().serialize(accountCaptureMigrateAccountDetails.domainName, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AccountCaptureMigrateAccountDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    AccountCaptureMigrateAccountDetails accountCaptureMigrateAccountDetails = new AccountCaptureMigrateAccountDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(accountCaptureMigrateAccountDetails, accountCaptureMigrateAccountDetails.toStringMultiline());
                    return accountCaptureMigrateAccountDetails;
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

    public AccountCaptureMigrateAccountDetails(String str) {
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
        AccountCaptureMigrateAccountDetails accountCaptureMigrateAccountDetails = (AccountCaptureMigrateAccountDetails) obj;
        String str = this.domainName;
        String str2 = accountCaptureMigrateAccountDetails.domainName;
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
