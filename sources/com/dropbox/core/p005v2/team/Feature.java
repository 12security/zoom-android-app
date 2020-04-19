package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.Feature */
public enum Feature {
    UPLOAD_API_RATE_LIMIT,
    HAS_TEAM_SHARED_DROPBOX,
    HAS_TEAM_FILE_EVENTS,
    HAS_TEAM_SELECTIVE_SYNC,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.Feature$Serializer */
    static class Serializer extends UnionSerializer<Feature> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(Feature feature, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (feature) {
                case UPLOAD_API_RATE_LIMIT:
                    jsonGenerator.writeString("upload_api_rate_limit");
                    return;
                case HAS_TEAM_SHARED_DROPBOX:
                    jsonGenerator.writeString("has_team_shared_dropbox");
                    return;
                case HAS_TEAM_FILE_EVENTS:
                    jsonGenerator.writeString("has_team_file_events");
                    return;
                case HAS_TEAM_SELECTIVE_SYNC:
                    jsonGenerator.writeString("has_team_selective_sync");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public Feature deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            Feature feature;
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
                if ("upload_api_rate_limit".equals(str)) {
                    feature = Feature.UPLOAD_API_RATE_LIMIT;
                } else if ("has_team_shared_dropbox".equals(str)) {
                    feature = Feature.HAS_TEAM_SHARED_DROPBOX;
                } else if ("has_team_file_events".equals(str)) {
                    feature = Feature.HAS_TEAM_FILE_EVENTS;
                } else if ("has_team_selective_sync".equals(str)) {
                    feature = Feature.HAS_TEAM_SELECTIVE_SYNC;
                } else {
                    feature = Feature.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return feature;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
