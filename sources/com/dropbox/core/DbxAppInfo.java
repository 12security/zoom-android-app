package com.dropbox.core;

import com.dropbox.core.json.JsonReadException;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.util.DumpWriter;
import com.dropbox.core.util.Dumpable;
import com.dropbox.core.util.StringUtil;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public class DbxAppInfo extends Dumpable {
    public static final JsonReader<String> KeyReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            try {
                String text = jsonParser.getText();
                String keyFormatError = DbxAppInfo.getKeyFormatError(text);
                if (keyFormatError == null) {
                    jsonParser.nextToken();
                    return text;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("bad format for app key: ");
                sb.append(keyFormatError);
                throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            }
        }
    };
    public static final JsonReader<DbxAppInfo> Reader = new JsonReader<DbxAppInfo>() {
        public final DbxAppInfo read(JsonParser jsonParser) throws IOException, JsonReadException {
            JsonLocation expectObjectStart = JsonReader.expectObjectStart(jsonParser);
            String str = null;
            String str2 = null;
            DbxHost dbxHost = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                try {
                    if (currentName.equals("key")) {
                        str = (String) DbxAppInfo.KeyReader.readField(jsonParser, currentName, str);
                    } else if (currentName.equals("secret")) {
                        str2 = (String) DbxAppInfo.SecretReader.readField(jsonParser, currentName, str2);
                    } else if (currentName.equals("host")) {
                        dbxHost = (DbxHost) DbxHost.Reader.readField(jsonParser, currentName, dbxHost);
                    } else {
                        JsonReader.skipValue(jsonParser);
                    }
                } catch (JsonReadException e) {
                    throw e.addFieldContext(currentName);
                }
            }
            JsonReader.expectObjectEnd(jsonParser);
            if (str == null) {
                throw new JsonReadException("missing field \"key\"", expectObjectStart);
            } else if (str2 != null) {
                if (dbxHost == null) {
                    dbxHost = DbxHost.DEFAULT;
                }
                return new DbxAppInfo(str, str2, dbxHost);
            } else {
                throw new JsonReadException("missing field \"secret\"", expectObjectStart);
            }
        }
    };
    public static final JsonReader<String> SecretReader = new JsonReader<String>() {
        public String read(JsonParser jsonParser) throws IOException, JsonReadException {
            try {
                String text = jsonParser.getText();
                String keyFormatError = DbxAppInfo.getKeyFormatError(text);
                if (keyFormatError == null) {
                    jsonParser.nextToken();
                    return text;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("bad format for app secret: ");
                sb.append(keyFormatError);
                throw new JsonReadException(sb.toString(), jsonParser.getTokenLocation());
            } catch (JsonParseException e) {
                throw JsonReadException.fromJackson(e);
            }
        }
    };
    private final DbxHost host;
    private final String key;
    private final String secret;

    public DbxAppInfo(String str, String str2) {
        checkKeyArg(str);
        checkSecretArg(str2);
        this.key = str;
        this.secret = str2;
        this.host = DbxHost.DEFAULT;
    }

    public DbxAppInfo(String str, String str2, DbxHost dbxHost) {
        checkKeyArg(str);
        checkSecretArg(str2);
        this.key = str;
        this.secret = str2;
        this.host = dbxHost;
    }

    public String getKey() {
        return this.key;
    }

    public String getSecret() {
        return this.secret;
    }

    public DbxHost getHost() {
        return this.host;
    }

    /* access modifiers changed from: protected */
    public void dumpFields(DumpWriter dumpWriter) {
        dumpWriter.mo10658f("key").mo10671v(this.key);
        dumpWriter.mo10658f("secret").mo10671v(this.secret);
    }

    public static String getKeyFormatError(String str) {
        return getTokenPartError(str);
    }

    public static String getSecretFormatError(String str) {
        return getTokenPartError(str);
    }

    public static String getTokenPartError(String str) {
        if (str == null) {
            return "can't be null";
        }
        if (str.length() == 0) {
            return "can't be empty";
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt < '!' || charAt > '~') {
                StringBuilder sb = new StringBuilder();
                sb.append("invalid character at index ");
                sb.append(i);
                sb.append(": ");
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(charAt);
                sb.append(StringUtil.m33jq(sb2.toString()));
                return sb.toString();
            }
        }
        return null;
    }

    public static void checkKeyArg(String str) {
        String tokenPartError = getTokenPartError(str);
        if (tokenPartError != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad 'key': ");
            sb.append(tokenPartError);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public static void checkSecretArg(String str) {
        String tokenPartError = getTokenPartError(str);
        if (tokenPartError != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad 'secret': ");
            sb.append(tokenPartError);
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
