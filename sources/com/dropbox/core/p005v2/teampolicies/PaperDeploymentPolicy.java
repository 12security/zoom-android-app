package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teampolicies.PaperDeploymentPolicy */
public enum PaperDeploymentPolicy {
    FULL,
    PARTIAL,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teampolicies.PaperDeploymentPolicy$Serializer */
    public static class Serializer extends UnionSerializer<PaperDeploymentPolicy> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PaperDeploymentPolicy paperDeploymentPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (paperDeploymentPolicy) {
                case FULL:
                    jsonGenerator.writeString("full");
                    return;
                case PARTIAL:
                    jsonGenerator.writeString("partial");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PaperDeploymentPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PaperDeploymentPolicy paperDeploymentPolicy;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("full".equals(str)) {
                    paperDeploymentPolicy = PaperDeploymentPolicy.FULL;
                } else if ("partial".equals(str)) {
                    paperDeploymentPolicy = PaperDeploymentPolicy.PARTIAL;
                } else {
                    paperDeploymentPolicy = PaperDeploymentPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return paperDeploymentPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
