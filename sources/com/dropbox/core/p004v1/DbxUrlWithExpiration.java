package com.dropbox.core.p004v1;

import com.dropbox.core.json.JsonDateReader;
import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Date;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v1.DbxUrlWithExpiration */
public final class DbxUrlWithExpiration {
    public static final JsonReader<DbxUrlWithExpiration> Reader = new JsonReader<DbxUrlWithExpiration>() {
        public DbxUrlWithExpiration read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            String str = null;
            Date date = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                try {
                    if (currentName.equals("url")) {
                        str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                    } else if (currentName.equals(ClientCookie.EXPIRES_ATTR)) {
                        date = (Date) JsonDateReader.Dropbox.readField(jsonParser, currentName, date);
                    } else {
                        JsonReader.skipValue(jsonParser);
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (str == null) {
                throw new JsonReadException("missing field \"url\"", expectObjectStart);
            } else if (date != null) {
                return new DbxUrlWithExpiration(str, date);
            } else {
                throw new JsonReadException("missing field \"expires\"", expectObjectStart);
            }
        }
    };
    public final Date expires;
    public final String url;

    public DbxUrlWithExpiration(String str, Date date) {
        this.url = str;
        this.expires = date;
    }
}
