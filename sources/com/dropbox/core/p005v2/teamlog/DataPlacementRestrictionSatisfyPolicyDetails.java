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

/* renamed from: com.dropbox.core.v2.teamlog.DataPlacementRestrictionSatisfyPolicyDetails */
public class DataPlacementRestrictionSatisfyPolicyDetails {
    protected final PlacementRestriction placementRestriction;

    /* renamed from: com.dropbox.core.v2.teamlog.DataPlacementRestrictionSatisfyPolicyDetails$Serializer */
    static class Serializer extends StructSerializer<DataPlacementRestrictionSatisfyPolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DataPlacementRestrictionSatisfyPolicyDetails dataPlacementRestrictionSatisfyPolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("placement_restriction");
            Serializer.INSTANCE.serialize(dataPlacementRestrictionSatisfyPolicyDetails.placementRestriction, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DataPlacementRestrictionSatisfyPolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            PlacementRestriction placementRestriction = null;
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
                    if ("placement_restriction".equals(currentName)) {
                        placementRestriction = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (placementRestriction != null) {
                    DataPlacementRestrictionSatisfyPolicyDetails dataPlacementRestrictionSatisfyPolicyDetails = new DataPlacementRestrictionSatisfyPolicyDetails(placementRestriction);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(dataPlacementRestrictionSatisfyPolicyDetails, dataPlacementRestrictionSatisfyPolicyDetails.toStringMultiline());
                    return dataPlacementRestrictionSatisfyPolicyDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"placement_restriction\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DataPlacementRestrictionSatisfyPolicyDetails(PlacementRestriction placementRestriction2) {
        if (placementRestriction2 != null) {
            this.placementRestriction = placementRestriction2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'placementRestriction' is null");
    }

    public PlacementRestriction getPlacementRestriction() {
        return this.placementRestriction;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.placementRestriction});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DataPlacementRestrictionSatisfyPolicyDetails dataPlacementRestrictionSatisfyPolicyDetails = (DataPlacementRestrictionSatisfyPolicyDetails) obj;
        PlacementRestriction placementRestriction2 = this.placementRestriction;
        PlacementRestriction placementRestriction3 = dataPlacementRestrictionSatisfyPolicyDetails.placementRestriction;
        if (placementRestriction2 != placementRestriction3 && !placementRestriction2.equals(placementRestriction3)) {
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
