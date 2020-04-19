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

/* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchArg */
class FeaturesGetValuesBatchArg {
    protected final List<Feature> features;

    /* renamed from: com.dropbox.core.v2.team.FeaturesGetValuesBatchArg$Serializer */
    static class Serializer extends StructSerializer<FeaturesGetValuesBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FeaturesGetValuesBatchArg featuresGetValuesBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("features");
            StoneSerializers.list(Serializer.INSTANCE).serialize(featuresGetValuesBatchArg.features, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FeaturesGetValuesBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("features".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    FeaturesGetValuesBatchArg featuresGetValuesBatchArg = new FeaturesGetValuesBatchArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(featuresGetValuesBatchArg, featuresGetValuesBatchArg.toStringMultiline());
                    return featuresGetValuesBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"features\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FeaturesGetValuesBatchArg(List<Feature> list) {
        if (list != null) {
            for (Feature feature : list) {
                if (feature == null) {
                    throw new IllegalArgumentException("An item in list 'features' is null");
                }
            }
            this.features = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'features' is null");
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.features});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        FeaturesGetValuesBatchArg featuresGetValuesBatchArg = (FeaturesGetValuesBatchArg) obj;
        List<Feature> list = this.features;
        List<Feature> list2 = featuresGetValuesBatchArg.features;
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
