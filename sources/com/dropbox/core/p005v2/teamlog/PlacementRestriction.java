package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import p015io.reactivex.annotations.SchedulerSupport;

/* renamed from: com.dropbox.core.v2.teamlog.PlacementRestriction */
public enum PlacementRestriction {
    EUROPE_ONLY,
    NONE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.PlacementRestriction$Serializer */
    static class Serializer extends UnionSerializer<PlacementRestriction> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PlacementRestriction placementRestriction, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (placementRestriction) {
                case EUROPE_ONLY:
                    jsonGenerator.writeString("europe_only");
                    return;
                case NONE:
                    jsonGenerator.writeString(SchedulerSupport.NONE);
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PlacementRestriction deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PlacementRestriction placementRestriction;
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
                if ("europe_only".equals(str)) {
                    placementRestriction = PlacementRestriction.EUROPE_ONLY;
                } else if (SchedulerSupport.NONE.equals(str)) {
                    placementRestriction = PlacementRestriction.NONE;
                } else {
                    placementRestriction = PlacementRestriction.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return placementRestriction;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
