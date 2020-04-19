package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchResult */
public class FeaturesGetValuesBatchResult {
    protected final List<FeatureValue> values;

    /* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchResult$Serializer */
    static class Serializer extends StructSerializer<FeaturesGetValuesBatchResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FeaturesGetValuesBatchResult featuresGetValuesBatchResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("values");
            StoneSerializers.list(Serializer.INSTANCE).serialize(featuresGetValuesBatchResult.values, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FeaturesGetValuesBatchResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
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
                    if ("values".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    FeaturesGetValuesBatchResult featuresGetValuesBatchResult = new FeaturesGetValuesBatchResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(featuresGetValuesBatchResult, featuresGetValuesBatchResult.toStringMultiline());
                    return featuresGetValuesBatchResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"values\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FeaturesGetValuesBatchResult(List<FeatureValue> list) {
        if (list != null) {
            for (FeatureValue featureValue : list) {
                if (featureValue == null) {
                    throw new IllegalArgumentException("An item in list 'values' is null");
                }
            }
            this.values = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'values' is null");
    }

    public List<FeatureValue> getValues() {
        return this.values;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.values});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        FeaturesGetValuesBatchResult featuresGetValuesBatchResult = (FeaturesGetValuesBatchResult) obj;
        List<FeatureValue> list = this.values;
        List<FeatureValue> list2 = featuresGetValuesBatchResult.values;
        if (list != list2 && !list.equals(list2)) {
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
