package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.ShmodelGroupShareDetails */
public class ShmodelGroupShareDetails {

    /* renamed from: com.dropbox.core.v2.teamlog.ShmodelGroupShareDetails$Serializer */
    static class Serializer extends StructSerializer<ShmodelGroupShareDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShmodelGroupShareDetails shmodelGroupShareDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShmodelGroupShareDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ShmodelGroupShareDetails shmodelGroupShareDetails = new ShmodelGroupShareDetails();
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(shmodelGroupShareDetails, shmodelGroupShareDetails.toStringMultiline());
                return shmodelGroupShareDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ShmodelGroupShareDetails shmodelGroupShareDetails = (ShmodelGroupShareDetails) obj;
        return true;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
