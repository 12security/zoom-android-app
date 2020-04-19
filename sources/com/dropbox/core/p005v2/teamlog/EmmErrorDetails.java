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

/* renamed from: com.dropbox.core.v2.teamlog.EmmErrorDetails */
public class EmmErrorDetails {
    protected final FailureDetailsLogInfo errorDetails;

    /* renamed from: com.dropbox.core.v2.teamlog.EmmErrorDetails$Serializer */
    static class Serializer extends StructSerializer<EmmErrorDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(EmmErrorDetails emmErrorDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("error_details");
            Serializer.INSTANCE.serialize(emmErrorDetails.errorDetails, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public EmmErrorDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            FailureDetailsLogInfo failureDetailsLogInfo = null;
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
                    if ("error_details".equals(currentName)) {
                        failureDetailsLogInfo = (FailureDetailsLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (failureDetailsLogInfo != null) {
                    EmmErrorDetails emmErrorDetails = new EmmErrorDetails(failureDetailsLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(emmErrorDetails, emmErrorDetails.toStringMultiline());
                    return emmErrorDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"error_details\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public EmmErrorDetails(FailureDetailsLogInfo failureDetailsLogInfo) {
        if (failureDetailsLogInfo != null) {
            this.errorDetails = failureDetailsLogInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'errorDetails' is null");
    }

    public FailureDetailsLogInfo getErrorDetails() {
        return this.errorDetails;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.errorDetails});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        EmmErrorDetails emmErrorDetails = (EmmErrorDetails) obj;
        FailureDetailsLogInfo failureDetailsLogInfo = this.errorDetails;
        FailureDetailsLogInfo failureDetailsLogInfo2 = emmErrorDetails.errorDetails;
        if (failureDetailsLogInfo != failureDetailsLogInfo2 && !failureDetailsLogInfo.equals(failureDetailsLogInfo2)) {
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
