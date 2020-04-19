package com.dropbox.core.p004v1;

import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v1.DbxLongpollDeltaResult */
public class DbxLongpollDeltaResult {
    public static final JsonReader<DbxLongpollDeltaResult> Reader = new JsonReader<DbxLongpollDeltaResult>() {
        public DbxLongpollDeltaResult read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            Boolean bool = null;
            long j = -1;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                try {
                    if (currentName.equals(BoxRequestEvent.STREAM_TYPE_CHANGES)) {
                        bool = (Boolean) JsonReader.BooleanReader.readField(jsonParser, currentName, bool);
                    } else if (currentName.equals("backoff")) {
                        j = JsonReader.readUnsignedLongField(jsonParser, currentName, j);
                    } else {
                        JsonReader.skipValue(jsonParser);
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (bool != null) {
                return new DbxLongpollDeltaResult(bool.booleanValue(), j);
            }
            throw new JsonReadException("missing field \"changes\"", expectObjectStart);
        }
    };
    public long backoff;
    public boolean mightHaveChanges;

    public DbxLongpollDeltaResult(boolean z, long j) {
        this.mightHaveChanges = z;
        this.backoff = j;
    }
}
