package com.dropbox.core;

import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public final class DbxAuthInfo {
    public static final JsonReader<DbxAuthInfo> Reader = new JsonReader<DbxAuthInfo>() {
        public final DbxAuthInfo read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            String str = null;
            DbxHost dbxHost = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                try {
                    if (currentName.equals("host")) {
                        dbxHost = (DbxHost) DbxHost.Reader.readField(jsonParser, currentName, dbxHost);
                    } else if (currentName.equals("access_token")) {
                        str = (String) StringReader.readField(jsonParser, currentName, str);
                    } else {
                        JsonReader.skipValue(jsonParser);
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (str != null) {
                if (dbxHost == null) {
                    dbxHost = DbxHost.DEFAULT;
                }
                return new DbxAuthInfo(str, dbxHost);
            }
            throw new JsonReadException("missing field \"access_token\"", expectObjectStart);
        }
    };
    public static final JsonWriter<DbxAuthInfo> Writer = new JsonWriter<DbxAuthInfo>() {
        public void write(DbxAuthInfo dbxAuthInfo, JsonGenerator jsonGenerator) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("access_token", dbxAuthInfo.accessToken);
            if (!dbxAuthInfo.host.equals(DbxHost.DEFAULT)) {
                jsonGenerator.writeFieldName("host");
                DbxHost.Writer.write(dbxAuthInfo.host, jsonGenerator);
            }
            jsonGenerator.writeEndObject();
        }
    };
    /* access modifiers changed from: private */
    public final String accessToken;
    /* access modifiers changed from: private */
    public final DbxHost host;

    public DbxAuthInfo(String str, DbxHost dbxHost) {
        if (str == null) {
            throw new IllegalArgumentException("'accessToken' can't be null");
        } else if (dbxHost != null) {
            this.accessToken = str;
            this.host = dbxHost;
        } else {
            throw new IllegalArgumentException("'host' can't be null");
        }
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public DbxHost getHost() {
        return this.host;
    }
}
