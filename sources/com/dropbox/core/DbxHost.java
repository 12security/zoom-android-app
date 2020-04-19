package com.dropbox.core;

import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.json.JsonWriter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

public final class DbxHost {
    public static final DbxHost DEFAULT = new DbxHost("api.dropboxapi.com", "content.dropboxapi.com", "www.dropbox.com", "notify.dropboxapi.com");
    public static final JsonReader<DbxHost> Reader = new JsonReader<DbxHost>() {
        public DbxHost read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_STRING) {
                String text = jsonParser.getText();
                JsonReader.nextToken(jsonParser);
                return DbxHost.fromBaseHost(text);
            } else if (currentToken == JsonToken.START_OBJECT) {
                JsonLocation tokenLocation = jsonParser.getTokenLocation();
                nextToken(jsonParser);
                String str = null;
                String str2 = null;
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    try {
                        if (currentName.equals("api")) {
                            str = (String) JsonReader.StringReader.readField(jsonParser, currentName, str);
                        } else if (currentName.equals(Param.CONTENT)) {
                            str2 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str2);
                        } else if (currentName.equals("web")) {
                            str3 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str3);
                        } else if (currentName.equals("notify")) {
                            str4 = (String) JsonReader.StringReader.readField(jsonParser, currentName, str4);
                        } else {
                            throw new JsonReadException("unknown field", jsonParser.getCurrentLocation());
                        }
                    } catch (JsonReadException e) {
                        throw e.addFieldContext(currentName);
                    }
                }
                JsonReader.expectObjectEnd(jsonParser);
                if (str == null) {
                    throw new JsonReadException("missing field \"api\"", tokenLocation);
                } else if (str2 == null) {
                    throw new JsonReadException("missing field \"content\"", tokenLocation);
                } else if (str3 == null) {
                    throw new JsonReadException("missing field \"web\"", tokenLocation);
                } else if (str4 != null) {
                    return new DbxHost(str, str2, str3, str4);
                } else {
                    throw new JsonReadException("missing field \"notify\"", tokenLocation);
                }
            } else {
                throw new JsonReadException("expecting a string or an object", jsonParser.getTokenLocation());
            }
        }
    };
    public static final JsonWriter<DbxHost> Writer = new JsonWriter<DbxHost>() {
        public void write(DbxHost dbxHost, JsonGenerator jsonGenerator) throws IOException {
            String access$100 = dbxHost.inferBaseHost();
            if (access$100 != null) {
                jsonGenerator.writeString(access$100);
                return;
            }
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("api", dbxHost.api);
            jsonGenerator.writeStringField(Param.CONTENT, dbxHost.content);
            jsonGenerator.writeStringField("web", dbxHost.web);
            jsonGenerator.writeStringField("notify", dbxHost.notify);
            jsonGenerator.writeEndObject();
        }
    };
    /* access modifiers changed from: private */
    public final String api;
    /* access modifiers changed from: private */
    public final String content;
    /* access modifiers changed from: private */
    public final String notify;
    /* access modifiers changed from: private */
    public final String web;

    public DbxHost(String str, String str2, String str3, String str4) {
        this.api = str;
        this.content = str2;
        this.web = str3;
        this.notify = str4;
    }

    public String getApi() {
        return this.api;
    }

    public String getContent() {
        return this.content;
    }

    public String getWeb() {
        return this.web;
    }

    public String getNotify() {
        return this.notify;
    }

    public int hashCode() {
        return Arrays.hashCode(new String[]{this.api, this.content, this.web, this.notify});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DbxHost)) {
            return false;
        }
        DbxHost dbxHost = (DbxHost) obj;
        if (!dbxHost.api.equals(this.api) || !dbxHost.content.equals(this.content) || !dbxHost.web.equals(this.web) || !dbxHost.notify.equals(this.notify)) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public static DbxHost fromBaseHost(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("api-");
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("api-content-");
        sb3.append(str);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("meta-");
        sb5.append(str);
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append("api-notify-");
        sb7.append(str);
        return new DbxHost(sb2, sb4, sb6, sb7.toString());
    }

    /* access modifiers changed from: private */
    public String inferBaseHost() {
        if (this.web.startsWith("meta-") && this.api.startsWith("api-") && this.content.startsWith("api-content-") && this.notify.startsWith("api-notify-")) {
            String substring = this.web.substring(5);
            String substring2 = this.api.substring(4);
            String substring3 = this.content.substring(12);
            String substring4 = this.notify.substring(11);
            if (substring.equals(substring2) && substring.equals(substring3) && substring.equals(substring4)) {
                return substring;
            }
        }
        return null;
    }
}
